package murskiy_sergey.graduate_work.services;

import org.apache.lucene.search.MultiCollectorManager;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TextAnalyzer {
    @Value("${stopwords.path}")
    private String stopWordsPath;

    private final Map<String, Object> stopWordsFilter = new HashMap<String, Object>() {{
        put("type", "stop");
        put("stopwords_path", stopWordsPath);
    }};

    private final Map<String, Object> specialCharactersFilter = new HashMap<String, Object>() {{
        put("type", "pattern_replace");
        put("pattern", "[^\\p{L}| ]+");
        put("replacement", "");
    }};

    private final Map<String, Object> russianKeywords = new HashMap<String, Object>() {{
        put("type", "keyword_marker");
        put("keywords", new String[]{"пример"});
    }};

    private final Map<String, Object> russianStemmer = new HashMap<String, Object>() {{
        put("type", "stemmer");
        put("language", "russian");

    }};

    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public TextAnalyzer(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    public Response getTextTerms(String documentContent) {
        List<AnalyzeResponse.AnalyzeToken> analyzeTokens = analyzeTextContent(documentContent);

        Map<String, Long> collect = analyzeTokens.stream()
                .map(AnalyzeResponse.AnalyzeToken::getTerm)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Map<String, Integer> textTerms = new HashMap<>();

        analyzeTokens.forEach(analyzeToken -> {
            if (analyzeToken.getType().equals("<ALPHANUM>")) {
                String term = analyzeToken.getTerm();
                if (textTerms.containsKey(term)) {
                    int oldCount = textTerms.get(term);
                    textTerms.put(term, ++oldCount);
                } else {
                    textTerms.put(term, 1);
                }
//                if (term.matches("[\\p{L}| ]+") && term.length() >= 3) {
//                    if (textTerms.containsKey(term)) {
//                        int oldCount = textTerms.get(term);
//                        textTerms.put(term, ++oldCount);
//                    } else {
//                        textTerms.put(term, 1);
//                    }
//                }
            }
        });

        return new Response(analyzeTokens.size(), textTerms);
    }

    private List<AnalyzeResponse.AnalyzeToken> analyzeTextContent(String documentContent) {
        return elasticsearchTemplate.getClient()
                .admin()
                .indices()
                .analyze(createAnalyzeRequest(documentContent))
                .actionGet()
                .getTokens();
    }

    private AnalyzeRequest createAnalyzeRequest(String content) {
        AnalyzeRequest request = new AnalyzeRequest();
        request.text(content);
        request.addCharFilter(specialCharactersFilter);
        request.tokenizer("standard");
        request.addTokenFilter("lowercase");
        request.addTokenFilter(russianKeywords);
        request.addTokenFilter(russianStemmer);

        return request;
    }

    public class Response {
        private int documentWordsCount;
        private Map<String, Integer> documentTerms;

        Response(int documentWordsCount, Map<String, Integer> documentTerms) {
            this.documentWordsCount = documentWordsCount;
            this.documentTerms = documentTerms;
        }

        public int getTextWordsCount() {
            return documentWordsCount;
        }

        public Map<String, Integer> getTextTerms() {
            return documentTerms;
        }
    }
}
