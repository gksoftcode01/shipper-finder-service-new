package ai.yarmook.shipperfinder.core.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticClientConfig {

    @Value("${spring.elasticsearch.uris}")
    String connectionUrl;

    @Value("${spring.elasticsearch.username}")
    String username;

    @Value("${spring.elasticsearch.password}")
    String password;

    @Bean
    public RestClient restClient() {
        //        File certFile = new File("/path/to/http_ca.crt");
        //
        //        SSLContext sslContext = TransportUtils
        //                .sslContextFromHttpCaCrt(certFile);
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        return RestClient.builder(HttpHost.create(connectionUrl))
            .setHttpClientConfigCallback(
                hc -> hc.setDefaultCredentialsProvider(credentialsProvider)
                //                                .setSSLContext(sslContext)
            )
            //API key
            //                .setDefaultHeaders(new Header[]{
            //                        new BasicHeader("Authorization", "ApiKey " + apiKey)
            //                })
            .build();
    }

    //    @Bean
    //    public ElasticsearchClient createElasticsearchClient(RestClient restClient, ObjectMapper objectMapper) {
    //        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper(objectMapper));
    //        return new AutoCloseableElasticsearchClient(transport);
    //    }
    @Bean
    public ElasticsearchClient getElasticsearchClient(RestClient restClient, JsonpMapper jsonpMapper) {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(restClient, jsonpMapper);

        // And create the API client
        ElasticsearchClient esClient = new ElasticsearchClient(transport);
        return esClient;
    }
}
