package cc.caiweiwei.framework.elasticsearch.test;

import cc.caiweiwei.framework.shiguangcms.common.utils.JsonUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ElasticSearchClientTest {

    @Test
    public void testOne() throws IOException {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "sunshine.520"));
        RestClient restClient = RestClient.builder(new HttpHost("192.168.66.201", 9200), new HttpHost("192.168.66.202", 9200), new HttpHost("192.168.66.203", 9200))
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    httpClientBuilder.disableAuthCaching();
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    return httpClientBuilder;
                }).build();
        JacksonJsonpMapper jacksonJsonpMapper = new JacksonJsonpMapper(JsonUtil.OBJECT_MAPPER);
        RestClientTransport restClientTransport = new RestClientTransport(restClient, jacksonJsonpMapper);
        ElasticsearchClient client = new ElasticsearchClient(restClientTransport);
        client.indices().create(c -> c.index("fruits"));

    }
}
