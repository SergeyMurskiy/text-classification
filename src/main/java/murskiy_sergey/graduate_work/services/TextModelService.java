package murskiy_sergey.graduate_work.services;

import murskiy_sergey.graduate_work.models.text.TextModel;
import murskiy_sergey.graduate_work.repositories.TextRepository;
import murskiy_sergey.graduate_work.services.reader.TextReaderFacade;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TextModelService {
    @Value("${text.to.load.folder}")
    private String textFolderPath;

    private final TextRepository textRepository;
    private final TextReaderFacade textReaderFacade;
    private final TextAnalyzer textAnalyzer;

    @Autowired
    public TextModelService(TextRepository textRepository, TextReaderFacade textReaderFacade, TextAnalyzer textAnalyzer) {
        this.textRepository = textRepository;
        this.textReaderFacade = textReaderFacade;
        this.textAnalyzer = textAnalyzer;
    }

    public void deleteAllTexts() {
        textRepository.deleteAll();
    }

    public List<TextModel> findTextsByTopic(String topicName) {
        return textRepository.findByTextTopic(topicName);
    }

    public Optional<TextModel> findByTextName(String textName) {
        return textRepository.findById(textName);
    }

    public void saveTexts(MultipartFile[] files, String charset, String topic) {
        List<TextModel> textModelsToSave = Arrays.stream(files)
                .map(file -> createTextModel(file, charset, topic))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        textRepository.saveAll(textModelsToSave);
    }

    public List<TextModel> getAllTexts() {
        Iterable<TextModel> allTexts = textRepository.findAll();
        List<TextModel> textModelList = new ArrayList<>();
        allTexts.forEach(textModelList::add);

        return textModelList;
    }

    private Optional<TextModel> createTextModel(MultipartFile file, String charset, String topic) {
        Optional<String> textContent = textReaderFacade.getTextContent(file, charset);

        if (textContent.isPresent()) {
            TextAnalyzer.Response textTerms = textAnalyzer.getTextTerms(textContent.get());
            TextModel textModel = new TextModel(file.getOriginalFilename(), topic,
                    textTerms.getTextTerms(), textTerms.getTextWordsCount());

            return Optional.of(textModel);
        }

        return Optional.empty();
    }

    private Optional<TextModel> createTextModel(File file, String charset, String topic) {
        Optional<String> textContent = textReaderFacade.getTextContent(file, charset);

        if (textContent.isPresent()) {
            TextAnalyzer.Response textTerms = textAnalyzer.getTextTerms(textContent.get());
            TextModel textModel = new TextModel(file.getName(), topic,
                    textTerms.getTextTerms(), textTerms.getTextWordsCount());

            return Optional.of(textModel);
        }

        return Optional.empty();
    }

    public List<String> createAttributesByTFIDF(Map<String, List<File>> texts, int sizeOfAttributes) {
        List<TextModel> textModels = texts.entrySet().stream()
                .map(entry -> entry.getValue()
                        .stream()
                        .map(file -> createTextModel(file, "UTF-8", entry.getKey()))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return getAttributesWithBestWeight(textModels, sizeOfAttributes);
    }


    public List<String> createAttributesByTFIDF(List<String> topics, int sizeOfAttributes) {
        List<TextModel> textModels = StreamSupport.stream(textRepository.findAll().spliterator(), false)
                .filter(textModel -> topics.contains(textModel.getTextTopic()))
                .collect(Collectors.toList());

        return getAttributesWithBestWeight(textModels, sizeOfAttributes);
    }

    private List<String> getAttributesWithBestWeight(List<TextModel> textModels, int sizeOfAttributes) {
        Map<String, List<TermWeight>> weights = createWeights(textModels);

        List<String> attributes = new ArrayList<>();

        weights.forEach((key, value) -> {
            OptionalDouble average = value.stream().mapToDouble(t -> t.weight).average();
            value.sort(Comparator.comparingDouble(o -> o.weight));

            double mediumWeight;

            if (value.size() % 2 == 1) {
                mediumWeight = value.get(value.size() / 2).weight;
            } else {
                mediumWeight = 0.5 * (value.get(value.size() / 2 - 1).weight + value.get(value.size() / 2).weight);
            }

            if (average.isPresent()) {
                value.sort((o1, o2) -> Double.compare(Math.abs(o2.weight - mediumWeight), Math.abs(o1.weight - mediumWeight)));
            }

            int size = 0;

            for (TermWeight termWeight : value) {
                if (!attributes.contains(termWeight.term)) {
                    attributes.add(termWeight.term);
                    size++;
                }

                if (size > sizeOfAttributes) {
                    break;
                }
            }
        });

        return attributes;
    }

    private Map<String, List<TermWeight>> createWeights(List<TextModel> textModels) {
        Set<String> terms = getSetOfTerms(textModels);

        Map<String, List<TermWeight>> weights = new HashMap<>();

//        textModels.stream().map(textModel -> textModel.getTermsOfText())
//                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        terms.forEach(term -> {
            int N_k = textModels.stream()
                    .mapToInt(textModel -> textModel.getTermsOfText().getOrDefault(term, 0))
                    .sum();

            textModels.forEach(textModel -> {
                if (textModel.getTermsOfText().containsKey(term)) {
                    int N_i_k = textModel.getTermsOfText().get(term);

                    double N_i_s = textModels.stream()
                            .mapToDouble(textModel1 -> {
                                if (!textModel1.getTextName().equals(textModel.getTextName())) {
                                    return Math.pow(Math.log(textModel1.getTermsOfText().getOrDefault(term, 0)) + 1, 2);
                                } else {
                                    return 0;
                                }
                            }).sum();
//                    int N_i_s = textModels.stream()
//                            .filter(textModel1 -> !textModel1.getTextName().equals(textModel.getTextName()))
//                            .mapToInt(textModel1 -> (int) Math.pow(Math.log(textModel1.getTermsOfText().getOrDefault(term, 0)) + 1, 2))
//                            .sum();

                    double w_k_i = (1 + Math.log(N_i_k)) * Math.log((double) textModels.size() / N_k) / Math.sqrt(N_i_s);

                    if (weights.containsKey(textModel.getTextTopic())) {
                        List<TermWeight> weightsForTopic = weights.get(textModel.getTextTopic());
                        weightsForTopic.add(new TermWeight(term, w_k_i));
                    } else {
                        List<TermWeight> values = new ArrayList<>();
                        values.add(new TermWeight(term, w_k_i));
                        weights.put(textModel.getTextTopic(), values);
                    }
                }
            });
        });

        return weights;
    }

    private Set<String> getSetOfTerms(List<TextModel> textModelList) {
        return textModelList.stream()
                .map(textModel -> textModel.getTermsOfText().keySet())
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

//    public List<String> createAttributesByTFIDF(Map<String, List<File>> textMap, int sizeOfAttributes) {
//        Set<String> terms = textModels.stream()
//                .map(textModel -> textModel.getTermsOfText().keySet())
//                .flatMap(Set::stream)
//                .collect(Collectors.toSet());
//
//
//        Map<String, List<TermWeight>> weights = new HashMap<>();
//
//        terms.forEach(term -> {
//            int N_k = textModels.stream()
//                    .mapToInt(textModel -> textModel.getTermsOfText().get(term))
//                    .sum();
//
//            textModels.forEach(textModel -> {
//                int N_i_k = textModel.getTermsOfText().get(term);
//
//                int N_i_s = textModels.stream()
//                        .filter(textModel1 -> !textModel1.getTextName().equals(textModel.getTextName()))
//                        .mapToInt(textModel1 -> (int) Math.pow(Math.log(textModel1.getTermsOfText().get(term)) + 1, 2))
//                        .sum();
//
//                double w_k_i = (1 + Math.log(N_i_k)) * Math.log((double) textModels.size() / N_k) / Math.sqrt(N_i_s);
//
//                if (weights.containsKey(textModel.getTextTopic())) {
//                    List<TermWeight> weightsForTopic = weights.get(textModel.getTextTopic());
//                    weightsForTopic.add(new TermWeight(term, w_k_i));
//                } else {
//                    List<TermWeight> values = new ArrayList<>();
//                    values.add(new TermWeight(term, w_k_i));
//                    weights.put(textModel.getTextTopic(), values);
//                }
//            });
//        });
//
//        List<String> attributes = new ArrayList<>();
//
//        weights.forEach((key, value) -> {
//            OptionalDouble average = value.stream().mapToDouble(t -> t.weight).average();
//            value.sort(Comparator.comparingDouble(o -> o.weight));
//
//            int valueSize = value.size();
//
//            double mediumWeight;
//            if (valueSize % 2 == 1) {
//                mediumWeight = value.get(valueSize / 2).weight;
//            } else {
//                mediumWeight = 0.5 * (value.get(valueSize / 2 - 1).weight + value.get(valueSize / 2).weight);
//            }
//
//            if (average.isPresent()) {
//                value.sort((o1, o2) -> Double.compare(Math.abs(o2.weight - mediumWeight), Math.abs(o1.weight - mediumWeight)));
//            }
//
//            int size = 0;
//
//            for (TermWeight termWeight : value) {
//                if (!attributes.contains(termWeight.term)) {
//                    attributes.add(termWeight.term);
//                    size++;
//                }
//
//                if (size > sizeOfAttributes) {
//                    break;
//                }
//            }
//        });
//
//        return attributes;
//    }

    private class TermWeight {
        private String term;
        private double weight;

        TermWeight(String term, double weight) {
            this.term = term;
            this.weight = weight;
        }

    }
}
