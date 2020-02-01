package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.*;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat;
import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;

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
        DEV(
            "172.16.71.1:9606,172.16.71.1:9607,172.16.71.1:9608,172.16.71.2:9606,172.16.71.2:9607,172.16.71.2:9608"
        ),

        /**
         * 案例解析线上环境
         */
        PRO(
            "172.16.76.101:9605,172.16.76.102:9605,172.16.76.103:9605,172.16.76.104:9605,172.16.76.105:9605"
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
        GetResponse response = ES.DEV.client.get(request, RequestOptions.DEFAULT);
        log.info(JSON.toJSONString(response.getSourceAsMap()));
        ES.DEV.client.close();
    }

    /**
     * 根据DSL查询
     */
    @Test
    public void test20200111205524() throws Exception {
        FileInputStream fis = new FileInputStream(new File("/Users/icourt/IdeaProjects/springBoot/DSL.txt"));
        JSONObject jsonObject = JSON.parseObject(fis, JSONObject.class);

        Request request = new Request("POST", "/judgement_1015/judgement/_search");
        request.setJsonEntity(jsonObject.toJSONString());

        RestClient client = ES.DEV.client.getLowLevelClient();
        Response response = client.performRequest(request);
        client.close();

        JSONObject parseObject = JSON.parseObject(EntityUtils.toString(response.getEntity()));

        FileOutputStream fos = new FileOutputStream(new File("/Users/icourt/IdeaProjects/springBoot/ES.OUT.txt"));
        // 以人类可读方式打印
        log.info(JSON.toJSONString(parseObject, true));
        // 将结果输出到文件
        JSON.writeJSONString(fos, parseObject, PrettyFormat);
    }

    /**
     * fix:公诉机关:最高人民检察院
     */
    @Test
    public void test20200112231217() throws IOException {

        System.out.println("开始执行");

        RestHighLevelClient client = ES.PRO.client;
        String index = "judgementsearch_dev";
        String indexType = "judgement";

        TimeValue keepAlive = TimeValue.timeValueMinutes(1L);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
            .query(matchPhraseQuery("raw_public_prosecution", "最高人民检察院"))
            .from(0)
            .size(10);

        SearchRequest request = new SearchRequest(index);
        request.types(indexType);
        request.source(sourceBuilder);
        request.scroll(keepAlive);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        String scrollId = response.getScrollId();
        SearchHit[] hits = response.getHits().getHits();

        while (hits != null && hits.length > 0) {

            List<String> list = Arrays.stream(hits)
                .map(SearchHit::getId)
                .collect(Collectors.toList());

            List<String> v = new ArrayList<>();
            v.add("最高人民检察院");

            BulkRequest bulkRequest = new BulkRequest();
            list.forEach(x -> bulkRequest.add(new UpdateRequest(index, indexType, x).doc("prosecution_organ_term", v)));
            client.bulk(bulkRequest, RequestOptions.DEFAULT);

            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(keepAlive);

            response = client.scroll(scrollRequest, RequestOptions.DEFAULT);
            scrollId = response.getScrollId();
            hits = response.getHits().getHits();
        }

        ClearScrollRequest clear = new ClearScrollRequest();
        clear.addScrollId(scrollId);
        client.clearScroll(clear, RequestOptions.DEFAULT);

        client.close();

        System.out.println("执行成功");
    }
}