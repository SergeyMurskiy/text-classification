package murskiy_sergey.graduate_work.services.analyze.weka;

import murskiy_sergey.graduate_work.config.WekaClassifiers;
import murskiy_sergey.graduate_work.services.TextAnalyzer;
import murskiy_sergey.graduate_work.services.analyze.AnalyzeResponse;
import murskiy_sergey.graduate_work.services.analyze.AnalyzeResponseEntity;
import murskiy_sergey.graduate_work.services.analyze.AnalyzeService;
import murskiy_sergey.graduate_work.services.reader.TextReaderFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class WekaAnalyzeService implements AnalyzeService {
    @Autowired
    protected WekaClassifiers wekaClassifiers;

    @Autowired
    TextReaderFacade textReader;

    @Autowired
    TextAnalyzer textAnalyzer;

    @Override
    public AnalyzeResponse analyze(MultipartFile[] files, String charset) {
        if (wekaClassifiers.isBuild()) {
            List<AnalyzeResponseEntity> analyzeResponseEntities = analyzeFilesContent(files, charset);
            addAnalyzedInstances(analyzeResponseEntities);
            return classifierInstances(analyzeResponseEntities);
        } else {
            return new AnalyzeResponse("Неудалось классифицировать файлы. Необходимо загрузить обучающие примеры в классификатор");
        }
    }

    protected abstract AnalyzeResponse classifierInstances(List<AnalyzeResponseEntity> analyzeResponseEntities);

    private void addAnalyzedInstances(List<AnalyzeResponseEntity> analyzeResponseEntities) {
        Instances dataset = createDataset(analyzeResponseEntities.size());
        analyzeResponseEntities.forEach(analyzeResponseEntity -> analyzeResponseEntity
                .setInstance(createInstance(analyzeResponseEntity.getTextTerms(), analyzeResponseEntity.getWordsCount(), wekaClassifiers.getAttributes(), dataset)));
    }

    private Instances createDataset(int size) {
        Instances dataset = new Instances(wekaClassifiers.getRelationName(), wekaClassifiers.getAttributes(), size);
        dataset.setClassIndex(dataset.numAttributes() - 1);
        return dataset;
    }

    private Instance createInstance(Map<String, Long> textTerms, long wordsCount,  List<Attribute> attributes, Instances dataset) {
        Instance instance = new DenseInstance(attributes.size());
        attributes.forEach(attribute ->
                instance.setValue(attribute, (double) textTerms.getOrDefault(attribute.name(), 0L) / wordsCount));
        instance.setDataset(dataset);
        return instance;
    }

    private List<AnalyzeResponseEntity> analyzeFilesContent(MultipartFile[] files, String charset) {
        List<AnalyzeResponseEntity> analyzeResponseEntities = new ArrayList<>();

        for (MultipartFile file : files) {
            Optional<String> textContent = textReader.getTextContent(file, charset);
            if (textContent.isPresent()) {
                TextAnalyzer.Response textAnalyzerResponse = textAnalyzer.getTextTerms(textContent.get());
                analyzeResponseEntities.add(new AnalyzeResponseEntity(file.getOriginalFilename(), textAnalyzerResponse.getTextWordsCount(), textAnalyzerResponse.getTextTerms()));
            }
        }

        return analyzeResponseEntities;
    }

    protected String getClassNameByClassLabel(double value) {
        return wekaClassifiers.getClasses().get((int) value);
    }

    protected AnalyzeResponse createAnalyzeResponse(List<AnalyzeResponseEntity> analyzeResponseEntities) {
        long allTime = analyzeResponseEntities.stream().mapToLong(AnalyzeResponseEntity::getMilliseconds).sum();
        return new AnalyzeResponse(analyzeResponseEntities, analyzeResponseEntities.size(), allTime);
    }
}
