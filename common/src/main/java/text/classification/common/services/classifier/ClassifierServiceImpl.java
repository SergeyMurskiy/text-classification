package text.classification.common.services.classifier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import text.classification.common.services.RequestService;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public abstract class ClassifierServiceImpl implements ClassifierService {
    @Autowired
    private RequestService requestService;

    public abstract String getMethodName();

    protected abstract void beforeClassifier();

    protected abstract ClassifierResponseEntity classifierText(ClassifierRequest learningRequest) throws Exception;

    protected abstract void afterClassifier();

    @Override
    public ClassifierResponse classifier(MultipartFile[] textFiles, String charset) {
        long start = System.currentTimeMillis();
        List<ClassifierResponseEntity> responseEntities = Arrays.stream(textFiles)
                .map(textFile -> {
                    Optional<ClassifierRequest> request = requestService.createClassifierRequest(textFile, charset);
                    if (request.isPresent()) {
                        return classifierProcess(request.get());
                    } else {
                        return ClassifierResponseEntity.builder()
                                .textName(textFile.getOriginalFilename())
                                .comment("Не удалось прочитать файл " + textFile.getOriginalFilename())
                                .build();
                    }
                }).collect(Collectors.toList());
        long end = System.currentTimeMillis();

        return ClassifierResponse.builder()
                .responseEntities(responseEntities)
                .textCount(textFiles.length)
                .time(end - start)
                .build();
    }

    @Override
    public ClassifierResponse classifier(File[] textFiles, String charset) {
        long start = System.currentTimeMillis();
        List<ClassifierResponseEntity> responseEntities = Arrays.stream(textFiles)
                .map(textFile -> {
                    Optional<ClassifierRequest> request = requestService.createClassifierRequest(textFile, charset);
                    if (request.isPresent()) {
                        return classifierProcess(request.get());
                    } else {
                        return ClassifierResponseEntity.builder()
                                .textName(textFile.getName())
                                .comment("Не удалось прочитать файл " + textFile.getName())
                                .build();
                    }
                }).collect(Collectors.toList());
        long end = System.currentTimeMillis();

        return ClassifierResponse.builder()
                .responseEntities(responseEntities)
                .textCount(textFiles.length)
                .time(end - start)
                .build();
    }

    @Override
    public ClassifierResponse classifier(List<ClassifierRequest> classifierRequests) {
        long start = System.currentTimeMillis();
        List<ClassifierResponseEntity> responseEntities = classifierRequests.stream()
                .map(this::classifierProcess)
                .collect(Collectors.toList());
        long end = System.currentTimeMillis();

        return ClassifierResponse.builder()
                .responseEntities(responseEntities)
                .textCount(classifierRequests.size())
                .time(end - start)
                .build();
    }

    private ClassifierResponseEntity classifierProcess(ClassifierRequest classifierRequest) {
        try {
            beforeClassifier();
            long methodStart = System.currentTimeMillis();
            ClassifierResponseEntity classifierResponseEntity = classifierText(classifierRequest);
            long methodEnd = System.currentTimeMillis();
            classifierResponseEntity.setTime(methodEnd - methodStart);
            afterClassifier();
            return classifierResponseEntity;
        } catch (Exception e) {
            return createErrorResponse(classifierRequest);
        }
    }

    private ClassifierResponseEntity createErrorResponse(ClassifierRequest classifierRequest) {
        log.error("Can't classifier text with name: " + classifierRequest.getTextName() + " by method " + getMethodName());
        return ClassifierResponseEntity.builder()
                .textName(classifierRequest.getTextName())
                .wordsCount(classifierRequest.getCountOfWords())
                .comment("Во время классификации текста произошла ошибка")
                .build();
    }
}
