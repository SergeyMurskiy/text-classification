package murskiy_sergey.graduate_work.services.weka;

import murskiy_sergey.graduate_work.config.WekaClassifiers;
import murskiy_sergey.graduate_work.models.dictionary.DictionaryEntity;
import murskiy_sergey.graduate_work.models.weka.data.WekaDataModel;
import murskiy_sergey.graduate_work.repositories.DictionaryEntityRepository;
import murskiy_sergey.graduate_work.repositories.WekaDataEntityRepository;
import murskiy_sergey.graduate_work.repositories.WekaDataModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class WekaDataService {
    private final WekaDataModelRepository wekaDataModelRepository;
    private final WekaDataEntityRepository wekaDataEntityRepository;
    private final DictionaryEntityRepository dictionaryEntityRepository;
    private final WekaClassifiers wekaClassifiers;

    @Autowired
    public WekaDataService(WekaDataModelRepository wekaDataModelRepository, WekaDataEntityRepository wekaDataEntityRepository, DictionaryEntityRepository dictionaryEntityRepository, WekaClassifiers wekaClassifiers) {
        this.wekaDataModelRepository = wekaDataModelRepository;
        this.wekaDataEntityRepository = wekaDataEntityRepository;
        this.dictionaryEntityRepository = dictionaryEntityRepository;
        this.wekaClassifiers = wekaClassifiers;
    }

    public List<WekaDataModel> getAllWekaModels() {
        Iterable<WekaDataModel> allModels = wekaDataModelRepository.findAll();
        List<WekaDataModel> wekaDataModels = new ArrayList<>();
        allModels.forEach(wekaDataModels::add);
        return wekaDataModels;
    }

    public WekaDataModel getWekaModelByModelName(String modelName) {
        return wekaDataModelRepository.findById(modelName).get();
    }

    public List<String> getModelsNames() {
        return StreamSupport.stream(wekaDataModelRepository.findAll().spliterator(), false)
                .map(WekaDataModel::getName)
                .collect(Collectors.toList());
    }

    public WekaDataModel getActiveDataModel() {
        return wekaDataModelRepository.findByActiveTrue();
    }

    public String buildWekaDataModel(String modelName, int sizeOfAttributes, boolean setActive) {
        List<DictionaryEntity> dictionaryEntities = getShuffledDictionaryEntities();

        if (sizeOfAttributes > dictionaryEntities.size()) {
            sizeOfAttributes = dictionaryEntities.size();
        }

        List<String> attributes = dictionaryEntities.stream()
                .map(DictionaryEntity::getWord)
                .limit(sizeOfAttributes)
                .collect(Collectors.toList());

        if (setActive) {
            deactivateActiveModel();
        }

        wekaDataModelRepository.save(new WekaDataModel(UUID.randomUUID().toString(), modelName, attributes, setActive));
        return "Модель сохранена";
    }

    public String buildWekaDataModel(String modelName, int sizeOfAttributes, List<String> topics, boolean setActive) {
        List<DictionaryEntity> dictionaryEntities = getShuffledDictionaryEntities();

        if (sizeOfAttributes > dictionaryEntities.size()) {
            sizeOfAttributes = dictionaryEntities.size();
        }

        List<String> attributes = dictionaryEntities.stream()
                .map(DictionaryEntity::getWord)
                .limit(sizeOfAttributes)
                .collect(Collectors.toList());

        if (setActive) {
            deactivateActiveModel();
        }

        String id = UUID.randomUUID().toString();
        wekaDataModelRepository.save(new WekaDataModel(id, modelName, attributes, topics, setActive));
        wekaClassifiers.rebuildClassifiers(id);

        return "Модель сохранена";
    }

    public void buildWekaDataModel(String modelName, List<String> attributes, List<String> classes, boolean setActive) {
        wekaDataModelRepository.save(new WekaDataModel(UUID.randomUUID().toString(), modelName, attributes, classes, setActive));
    }

    public void deleteModel(String modelName) {

        wekaDataModelRepository.deleteById(modelName);

    }

    public String changeWekaDataModel(String modelName) {
        deactivateActiveModel();
        saveActiveModel(modelName);
        boolean isClassifiersRebuild = wekaClassifiers.rebuildClassifiers(modelName);
        return getChangeModelResponse(modelName, isClassifiersRebuild);
    }

    public void deleteAllWekaDataModels() {
        wekaDataModelRepository.deleteAll();
        wekaDataEntityRepository.deleteAll();
    }

    private void deactivateActiveModel() {
        WekaDataModel activeDataModel = getActiveDataModel();
        if (activeDataModel != null) {
            activeDataModel.setActive(false);
            wekaDataModelRepository.save(activeDataModel);
        }
    }

    private void saveActiveModel(String modelName) {
        WekaDataModel wekaDataModel = getWekaModelByModelName(modelName);
        wekaDataModel.setActive(true);
        wekaDataModelRepository.save(wekaDataModel);
    }
    private String getChangeModelResponse(String modelName, boolean isClassifiersRebuild) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Активная модель изменена. Текущая модель ").append(modelName).append("\\n");

        if (isClassifiersRebuild) {
            stringBuilder.append("Классификаторы обновлены");
        } else {
            stringBuilder.append("Неудалось обновить классификаторы. В моделе недостаточно обучающих примеров");
        }
        return stringBuilder.toString();
    }

    private List<DictionaryEntity> getShuffledDictionaryEntities() {
        Iterable<DictionaryEntity> dictionaryEntityIterator = dictionaryEntityRepository.findAll();
        List<DictionaryEntity> dictionaryEntities= new ArrayList<>();

        dictionaryEntityIterator.forEach(dictionaryEntities::add);
        Collections.shuffle(dictionaryEntities);
        return dictionaryEntities;
    }

//    public void buildWekaDataModel(String relationName, MultipartFile file) {
//        Optional<String> textContent = textReader.getTextContent(file, "default");
//        if (textContent.isPresent()) {
//            String[] attributesNames = textContent.get().split("\n");
//
//            List<String> attributes = new ArrayList<>(Arrays.asList(attributesNames));
//
//            wekaDataModelRepository.save(new WekaDataModel(relationName, attributes, new ArrayList<>(), new ArrayList<>()));
//        }
//    }
}