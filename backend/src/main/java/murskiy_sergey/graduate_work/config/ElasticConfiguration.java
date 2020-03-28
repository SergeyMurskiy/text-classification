package murskiy_sergey.graduate_work.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetAddress;

@Configuration
@EnableElasticsearchRepositories(basePackages = "murskiy_sergey.graduate_work.repositories")
public class ElasticConfiguration {
    @Value("${elasticsearch.cluster.name}")
    private String clusterName;

    @Value("${elasticsearch.host}")
    private String hostName;

    @Value("${elasticsearch.port}")
    private int port;

    @Bean
    public Client client() throws Exception{
        Settings elasticsearchSettings = Settings.builder()
                .put("cluster.name", clusterName)
                .build();
        TransportClient client = new PreBuiltTransportClient(elasticsearchSettings);
        client.addTransportAddress(new TransportAddress(InetAddress.getByName(hostName), port));
        return client;
    }

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate() throws Exception{
        return new ElasticsearchTemplate(client());
    }
}