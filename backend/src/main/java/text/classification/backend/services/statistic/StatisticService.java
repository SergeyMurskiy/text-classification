package text.classification.backend.services.statistic;

import org.springframework.context.annotation.ComponentScan;
import text.classification.backend.AutomaticTextRanking;
import text.classification.backend.models.dictionary.DictionaryEntity;
import text.classification.backend.models.statistic.Statistic;
import text.classification.backend.repositories.DictionaryEntityRepository;
import text.classification.backend.repositories.StatisticRepository;
import text.classification.common.services.RequestService;
import text.classification.common.services.classification.ClassifierRequest;
import text.classification.common.services.classification.ClassifierResponse;
import text.classification.common.services.classification.ClassifierResponseEntity;
import text.classification.common.services.classifier.ClassifierConfig;
import text.classification.common.services.classifier.ClassifierModel;
import text.classification.common.services.learning.LearningRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticService {
    private final RequestService requestService;

    private final LearningService learningService;

    private final ClassificationService classificationService;

    private final StatisticRepository statisticRepository;

    private final List<ClassifierModel> classifiers;

    private final DictionaryEntityRepository dictionaryEntityRepository;

    @Autowired
    public StatisticService(StatisticRepository statisticRepository, RequestService requestService, List<ClassifierModel> classifiers, LearningService learningService, ClassificationService classificationService, DictionaryEntityRepository dictionaryEntityRepository) {
        this.statisticRepository = statisticRepository;
        this.requestService = requestService;
        this.classifiers = classifiers;
        this.learningService = learningService;
        this.classificationService = classificationService;
        this.dictionaryEntityRepository = dictionaryEntityRepository;
    }

    static class Counter {
        static int countOfTexts = 0;
    }

    private List<ClassifierModel> getClassifiers() {
        String[] classifierNames = AutomaticTextRanking.context.getBeanNamesForType(ClassifierModel.class);
        return Arrays.stream(classifierNames)
                .map(classifierName -> (ClassifierModel) AutomaticTextRanking.context.getBean(classifierName))
                .collect(Collectors.toList());
    }

    public List<String> getAllMethodsName() {
//        List<ClassifierModel> classifiers = getClassifiers();

        return classifiers.stream()
                .map(ClassifierModel::getClassifierName)
                .collect(Collectors.toList());
    }

    public void deleteAllStatistic() {
        statisticRepository.deleteAll();
    }

    public List<Statistic> getStatistic() {
        Iterable<Statistic> statisticRepositoryAll = statisticRepository.findAll();
        List<Statistic> statisticList = new ArrayList<>();
        statisticRepositoryAll.forEach(statisticList::add);

        return statisticList;
    }

    public void generateStatistic(int sizeOfTexts, int sizeOfAttributes) {
        statisticRepository.deleteAll();

        Counter.countOfTexts = 0;

        Map<String, List<File>> textsFromFolder = getTextsFromFolder();
        Map<String, List<File>> etalons = getEtalonsFromFolder();

        Map<String, List<ClassifierRequest>> classifierRequestMap = createClassifierRequests(etalons);
        Map<String, List<LearningRequest>> learningRequestMap = createLearningRequests(textsFromFolder);

        List<String> topics = new ArrayList<>(textsFromFolder.keySet());

//        List<ClassifierModel> classifiers = getClassifiers();
//        this.learningService = new LearningService(classifiers);
//        this.classificationService = new ClassificationService(classifiers);

        List<String> attributesNames = buildAttributes(sizeOfAttributes);
        ClassifierConfig classifierConfig = new ClassifierConfig(attributesNames, topics);
        classifiers.forEach(classifierModel -> classifierModel.buildClassifier(classifierConfig));

        long start = System.currentTimeMillis();
        for (int i = 0; i < sizeOfTexts; i++) {
            for (String topic : topics) {
                LearningRequest learningRequest = learningRequestMap.get(topic).get(i);
                learningService.learning(Collections.singletonList(learningRequest));
                learningService.rebuildClassifiers();
            }

            Counter.countOfTexts++;

            List<Statistic> collect = classifierRequestMap.entrySet().parallelStream()
                    .map(entry -> {
                        List<ClassifierResponse> classifierResponses = classificationService.classifier(entry.getValue());
                        return classifierResponses.stream()
                                .map(classifierResponse -> classifierResponse.getResponseEntities()
                                        .parallelStream()
                                        .map(classifierResponseEntity -> createStatistic(classifierResponse.getClassifierName(),
                                                entry.getKey(), classifierResponseEntity))
                                        .collect(Collectors.toList()))
                                .collect(Collectors.toList());
                    })
                    .flatMap(List::stream)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            statisticRepository.saveAll(collect);
        }

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    private Statistic createStatistic(String methodName, String topic, ClassifierResponseEntity classifierResponseEntity) {
        String status;
        if (topic.equals(classifierResponseEntity.getClassValue())) {
            status = "correct";
        } else {
            status = "incorrect";
        }
        return Statistic.builder()
                .date(new Date())
                .methodName(methodName)
                .countOfTexts(Counter.countOfTexts)
                .topic(topic)
                .status(status)
                .time(classifierResponseEntity.getTime())
                .build();
    }

    private Map<String, List<File>> getTextsFromFolder() {
        File folder = new File("/usr/local/Cellar/tomcat/9.0.34/libexec/load/learn");
        return getFilesFromFolder(folder);
    }

    private Map<String, List<File>> getEtalonsFromFolder() {
        File etalonsFolder = new File("/usr/local/Cellar/tomcat/9.0.34/libexec/load/etalons");
        Map<String, List<File>> filesFromFolder = getFilesFromFolder(etalonsFolder);
        filesFromFolder.forEach((key, value) -> filesFromFolder.put(key, value.subList(0, 33)));
        return filesFromFolder;
    }

    private Map<String, List<File>> getFilesFromFolder(File folder) {
        File[] topicFolders = folder.listFiles((current, name) -> new File(current, name).isDirectory());

        Map<String, List<File>> result = new HashMap<>();

        Arrays.stream(topicFolders != null ? topicFolders : new File[0])
                .forEach(topicFolder -> {
                    File[] texts = topicFolder.listFiles((current, name) -> !new File(current, name).isDirectory());

                    if (texts != null) {
                        result.put(topicFolder.getName(), Arrays.asList(texts));
                    }
                });

        return result;
    }

    private Map<String, List<ClassifierRequest>> createClassifierRequests(Map<String, List<File>> etalonTextsByTopic) {
        Map<String, List<ClassifierRequest>> result = new HashMap<>();
        etalonTextsByTopic.forEach((topic, textList) -> {
            List<ClassifierRequest> classifierRequests = textList.stream()
                    .map(textFile -> requestService.createClassifierRequest(textFile, "UTF-8"))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            result.put(topic, classifierRequests);
        });

        return result;
    }

    private Map<String, List<LearningRequest>> createLearningRequests(Map<String, List<File>> textToLearnByTopic) {
        Map<String, List<LearningRequest>> result = new HashMap<>();
        textToLearnByTopic.forEach((topic, textList) -> {
            List<LearningRequest> classifierRequests = textList.stream()
                    .map(textFile -> requestService.createLearningRequest(textFile, topic, "UTF-8"))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            result.put(topic, classifierRequests);
        });

        return result;
    }

    private List<String> buildAttributes(int sizeOfAttributes) {
        List<DictionaryEntity> dictionaryEntities = getShuffledDictionaryEntities();

        if (sizeOfAttributes > dictionaryEntities.size()) {
            sizeOfAttributes = dictionaryEntities.size();
        }

         return dictionaryEntities.stream()
                .map(DictionaryEntity::getWord)
                .limit(sizeOfAttributes)
                .collect(Collectors.toList());
    }

    private List<DictionaryEntity> getShuffledDictionaryEntities() {
        Iterable<DictionaryEntity> dictionaryEntityIterator = dictionaryEntityRepository.findAll();
        List<DictionaryEntity> dictionaryEntities= new ArrayList<>();

        dictionaryEntityIterator.forEach(dictionaryEntities::add);
        Collections.shuffle(dictionaryEntities);
        return dictionaryEntities;
    }
}
