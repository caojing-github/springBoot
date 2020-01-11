package util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;

import java.io.IOException;

/**
 * Elasticsearch 工具
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.3/java-rest-high.html
 *
 * @author CaoJing
 * @date 2020/01/11 00:43
 */
@Slf4j
public final class ESKit {

    public enum ES {

        /**
         * 案例解析dev环境
         */
        ES_1(
            "172.16.71.1:9606,172.16.71.1:9607,172.16.71.1:9608,172.16.71.2:9606,172.16.71.2:9607,172.16.71.2:9608"
        );

        /**
         * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.3/java-rest-low-usage-initialization.html
         * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.3/java-rest-high-getting-started-initialization.html
         */
        private RestHighLevelClient client;

        /**
         * example: 172.16.71.1:9606,172.16.71.1:9607,172.16.71.1:9608,172.16.71.2:9606,172.16.71.2:9607,172.16.71.2:9608
         * 端口号为http端口不是TCP端口
         */
        ES(String httpHosts) {
            String[] split = httpHosts.split(",");
            HttpHost[] hosts = new HttpHost[split.length];

            for (int i = 0; i < split.length; i++) {
                String[] h = split[i].split(":");
                hosts[i] = new HttpHost(h[0], Integer.parseInt(h[1]));
            }
            this.client = new RestHighLevelClient(RestClient.builder(hosts));
        }
    }

    /**
     * 根据id查询
     */
    @Test
    public void test20200111034221() throws IOException {
        GetRequest request = new GetRequest("judgementsearch_dev", "judgement", "0CB010DEC346BD78BF6D771A6EC05D95");
        GetResponse response = ES.ES_1.client.get(request, RequestOptions.DEFAULT);
        log.info(JSON.toJSONString(response.getSourceAsMap()));
    }
}
