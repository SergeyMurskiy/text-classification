package murskiy_sergey.graduate_work.services.learning;

import murskiy_sergey.graduate_work.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class LearningServiceImpl implements LearningService{
    @Autowired
    private RequestService requestService;

    public abstract String getMethodName();

    protected abstract void beforeLearning(String topic);

    protected abstract LearningResponseEntity handleLearningRequest(LearningRequest learningRequest);

    protected abstract <T extends LearningResponseEntity> void saveResult(List<T> learningResponseEntity);

    protected abstract void afterLearning(boolean rebuildClassifier);

    @Override
    public LearningResponse learning(MultipartFile[] textFiles, String topic, String charset, boolean rebuildClassifier) {
        List<LearningRequest> learningRequestList = createLearningRequests(textFiles, topic, charset);
        return learningProcess(learningRequestList, topic, textFiles.length, rebuildClassifier);
    }

    @Override
    public LearningResponse learning(File[] textFiles, String topic, String charset, boolean rebuildClassifier) {
        List<LearningRequest> learningRequestList = createLearningRequests(textFiles, topic, charset);
        return learningProcess(learningRequestList, topic, textFiles.length, rebuildClassifier);
    }

    @Override
    public LearningResponse learning(List<LearningRequest> learningRequests, boolean rebuildClassifier) {
        return learningProcess(learningRequests, learningRequests.get(0).getTopic(), learningRequests.size(), rebuildClassifier);
    }

    private LearningResponse learningProcess(List<LearningRequest> learningRequestList, String topic, long countOfInputTexts, boolean rebuildClassifier) {
        long startLearningProcess = System.currentTimeMillis();
        beforeLearning(topic);

        List<LearningResponseEntity> learningResponseEntities = learningRequestList.stream()
                .map(learningRequest -> {
                    long start = System.currentTimeMillis();
                    LearningResponseEntity learningResponseEntity = handleLearningRequest(learningRequest);
                    long end = System.currentTimeMillis();
                    learningResponseEntity.setTime(end - start);
                    return learningResponseEntity;
                })
                .collect(Collectors.toList());

        saveResult(learningResponseEntities);

        afterLearning(rebuildClassifier);
        long endLearningProcess = System.currentTimeMillis();
        return createResponse(learningResponseEntities, countOfInputTexts, topic, (endLearningProcess - startLearningProcess));
    }

    private List<LearningRequest> createLearningRequests(MultipartFile[] textFiles, String topic, String charset) {
        return Arrays.stream(textFiles)
                .map(textFile -> requestService.createLearningRequest(textFile, topic, charset))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private List<LearningRequest> createLearningRequests(File[] textFiles, String topic, String charset) {
        return Arrays.stream(textFiles)
                .map(textFile -> requestService.createLearningRequest(textFile, topic, charset))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private <T extends LearningResponseEntity> LearningResponse createResponse(List<T> learningResponseEntities, long inputFilesCount,
                                                                               String topic, long time) {
        return LearningResponse.builder()
                .topic(topic)
                .countOfTexts(inputFilesCount)
                .countOfWords(getTotalWordsCount(learningResponseEntities))
                .responseEntities(learningResponseEntities)
                .time(time)
                .build();
    }

    protected <T extends LearningResponseEntity> long getTotalWordsCount(List<T> learningResponseEntityList) {
        return learningResponseEntityList.stream()
                .mapToLong(LearningResponseEntity::getCountOfWords)
                .sum();
    }
}
