package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pivovarit.function.ThrowingRunnable;
import com.pivovarit.function.ThrowingSupplier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.*;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat;
import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;
import static org.elasticsearch.index.query.QueryBuilders.wrapperQuery;

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
        public RestHighLevelClient client;

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
        FileInputStream fis = new FileInputStream(new File("/Users/caojing/IdeaProjects/springBoot/DSL.txt"));
        JSONObject jsonObject = JSON.parseObject(fis, JSONObject.class);

        Request request = new Request("POST", "/judgement_1015/judgement/_search");
        request.setJsonEntity(jsonObject.toJSONString());

        RestClient client = ES.PRO.client.getLowLevelClient();
        Response response = client.performRequest(request);
        client.close();

        JSONObject parseObject = JSON.parseObject(EntityUtils.toString(response.getEntity()));

        FileOutputStream fos = new FileOutputStream(new File("/Users/caojing/IdeaProjects/springBoot/ES.OUT.txt"));
        // 以人类可读方式打印
        log.info(JSON.toJSONString(parseObject, true));
        // 将结果输出到文件
        JSON.writeJSONString(fos, parseObject, PrettyFormat);
    }

    /**
     * Bulk 添加
     */
    @Test
    public void test20200227120633() throws IOException {
        String court = "曹靖法院";
        String id = Base64.getEncoder().encodeToString(court.getBytes());

        JSONObject jsonObject = new JSONObject()
            .fluentPut("id", id)
            .fluentPut("court", court);

        BulkRequest request = new BulkRequest()
            .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE)
            .add(new IndexRequest("suggest_dic_v2_court", "court", id)
                .source(jsonObject.toJSONString(), XContentType.JSON)
            );

        BulkResponse bulkResponse = ES.PRO.client.bulk(request, RequestOptions.DEFAULT);
        System.out.println();
    }

    /**
     * Bulk 更新
     */
    @Test
    public void test20200421151028() throws IOException {
        Map<String, String> map = new HashMap<>(4);
        map.put("city", "北京市");

        UpdateRequest updateRequest = new UpdateRequest("judgementsearch_dev", "judgement", "91B83307E39952A7975AE65BEC09A5AE");
        updateRequest.doc(map);

        BulkRequest request = new BulkRequest()
            .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE)
            .add(updateRequest);

        BulkResponse bulkResponse = ES.DEV.client.bulk(request, RequestOptions.DEFAULT);
        System.out.println();
    }

    /**
     * Bulk 删除
     */
    @Test
    public void test20200409171245() throws IOException {
        BulkRequest request = new BulkRequest()
            .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE)
            .add(new DeleteRequest("suggest-dic_v11_judge_court", "judgeCourt", "BStJWLP1z6Q7OSJYcF+eKA=="));

        BulkResponse bulkResponse = ES.PRO.client.bulk(request, RequestOptions.DEFAULT);
        System.out.println();
    }

    /**
     * ES scroll
     *
     * @param dsl      DSL语句
     * @param includes ES scroll结果包含字段
     * @param consumer 消费函数
     */
    public static void scroll(String index, String indexType, String dsl, String[] includes, Consumer<List<JSONObject>> consumer) {
        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(5));
        SearchRequest q = new SearchRequest()
            .indices(index)
            .types(indexType)
            .scroll(scroll);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
            .query(wrapperQuery(dsl))
            .size(1000);

        if (ArrayUtils.isNotEmpty(includes)) {
            sourceBuilder.fetchSource(includes, null);
        }
        q.source(sourceBuilder);

        SearchResponse r = ThrowingSupplier.sneaky(() -> ES.PRO.client.search(q, RequestOptions.DEFAULT)).get();
        String scrollId = r.getScrollId();
        SearchHit[] searchHits = r.getHits().getHits();

        while (searchHits != null && searchHits.length > 0) {
            List<JSONObject> list = Arrays.stream(searchHits)
                .map(SearchHit::getSourceAsString)
                .map(JSON::parseObject)
                .collect(Collectors.toList());

            consumer.accept(list);

            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId)
                .scroll(scroll);

            r = ThrowingSupplier.sneaky(() -> ES.PRO.client.scroll(scrollRequest, RequestOptions.DEFAULT)).get();
            scrollId = r.getScrollId();
            searchHits = r.getHits().getHits();
        }
        ClearScrollRequest c = new ClearScrollRequest();
        c.addScrollId(scrollId);
        ThrowingRunnable.sneaky(() -> ES.PRO.client.clearScroll(c, RequestOptions.DEFAULT)).run();
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

    /**
     * es scroll
     */
    @Test
    public void test20200409151019() {
        Consumer<List<JSONObject>> consumer = x -> x.forEach(y -> {
            System.out.println();
        });
        scroll("suggest_dic_v3_court", "court", "{\"match_all\":{}}", null, consumer);
    }

    /**
     * es查询导出文件
     */
    @Test
    public void test20200417164050() throws IOException {
        BufferedWriter writer = new BufferedWriter(new java.io.FileWriter("/Users/caojing/Desktop/目标文件.txt"));

        String dsl = "{\"from\":0,\"size\":29,\"query\":{\"bool\":{\"must\":[{\"bool\":{\"should\":[{\"bool\":{\"should\":[{\"bool\":{\"must\":[{\"term\":{\"intermediate_court\":{\"value\":\"广东省中山市中级人民法院\",\"boost\":1}}}],\"adjust_pure_negative\":true,\"boost\":1}}],\"adjust_pure_negative\":true,\"boost\":1}}],\"adjust_pure_negative\":true,\"boost\":1}},{\"bool\":{\"should\":[{\"bool\":{\"should\":[{\"bool\":{\"must\":[{\"term\":{\"level1_case\":{\"value\":\"刑事\",\"boost\":1}}}],\"adjust_pure_negative\":true,\"boost\":1}}],\"adjust_pure_negative\":true,\"boost\":1}}],\"adjust_pure_negative\":true,\"boost\":1}},{\"bool\":{\"should\":[{\"bool\":{\"should\":[{\"bool\":{\"filter\":[{\"range\":{\"all_judgementinfo_date\":{\"from\":\"2019-01-01\",\"to\":\"2019-12-31\",\"include_lower\":true,\"include_upper\":true,\"boost\":1}}}],\"adjust_pure_negative\":true,\"boost\":1}}],\"adjust_pure_negative\":true,\"boost\":1}}],\"adjust_pure_negative\":true,\"boost\":1}},{\"bool\":{\"adjust_pure_negative\":true,\"boost\":1}},{\"bool\":{\"adjust_pure_negative\":true,\"boost\":1}},{\"bool\":{\"adjust_pure_negative\":true,\"boost\":1}}],\"should\":[{\"term\":{\"publish_type\":{\"value\":\"1\",\"boost\":1000}}},{\"term\":{\"publish_type\":{\"value\":\"2\",\"boost\":1500}}},{\"term\":{\"publish_type\":{\"value\":\"1\",\"boost\":1000}}},{\"term\":{\"publish_type\":{\"value\":\"2\",\"boost\":1500}}},{\"term\":{\"publish_type\":{\"value\":\"1\",\"boost\":1000}}},{\"term\":{\"publish_type\":{\"value\":\"2\",\"boost\":1500}}}],\"must_not\":{\"exists\":{\"field\":\"level2_case\"}},\"adjust_pure_negative\":true,\"minimum_should_match\":\"0\",\"boost\":1}},\"_source\":{\"includes\":[\"jid\"],\"excludes\":[]}}";
        Request request = new Request("POST", "/judgement_1015/judgement/_search");
        request.setJsonEntity(dsl);

        RestClient client = ES.PRO.client.getLowLevelClient();
        Response response = client.performRequest(request);
        client.close();

        JSONArray jsonArray = JSON.parseObject(EntityUtils.toString(response.getEntity()))
            .getJSONObject("hits")
            .getJSONArray("hits");

        for (int i = 0; i < jsonArray.size(); i++) {
            String jid = jsonArray.getJSONObject(i).getJSONObject("_source").getString("jid");
            String url = "https://alphalawyer.cn/#/app/tool/result/%7B%5B%5D,%7D/detail/" + jid;
            writer.write(url + "\r\n");
        }
        writer.flush();
        writer.close();
    }

    /**
     * es scroll查询导出文件
     */
    @Test
    public void test20200417172320() throws IOException {
        BufferedWriter writer = new BufferedWriter(new java.io.FileWriter("/Users/caojing/Desktop/目标文件.txt"));

        Consumer<List<JSONObject>> consumer = x -> x.forEach(y -> {
            String jid = y.getString("jid");
            String url = "https://alphalawyer.cn/#/app/tool/result/%7B%5B%5D,%7D/detail/" + jid;
            ThrowingRunnable.sneaky(() -> writer.write(url + "\r\n")).run();
        });

        String dsl = "{\"bool\":{\"must\":[{\"bool\":{\"should\":[{\"bool\":{\"should\":[{\"bool\":{\"must\":[{\"term\":{\"intermediate_court\":{\"value\":\"广东省中山市中级人民法院\",\"boost\":1}}}],\"adjust_pure_negative\":true,\"boost\":1}}],\"adjust_pure_negative\":true,\"boost\":1}}],\"adjust_pure_negative\":true,\"boost\":1}},{\"bool\":{\"should\":[{\"bool\":{\"should\":[{\"bool\":{\"must\":[{\"term\":{\"level1_case\":{\"value\":\"刑事\",\"boost\":1}}}],\"adjust_pure_negative\":true,\"boost\":1}}],\"adjust_pure_negative\":true,\"boost\":1}}],\"adjust_pure_negative\":true,\"boost\":1}},{\"bool\":{\"should\":[{\"bool\":{\"should\":[{\"bool\":{\"filter\":[{\"range\":{\"all_judgementinfo_date\":{\"from\":\"2019-01-01\",\"to\":\"2019-12-31\",\"include_lower\":true,\"include_upper\":true,\"boost\":1}}}],\"adjust_pure_negative\":true,\"boost\":1}}],\"adjust_pure_negative\":true,\"boost\":1}}],\"adjust_pure_negative\":true,\"boost\":1}},{\"bool\":{\"adjust_pure_negative\":true,\"boost\":1}},{\"bool\":{\"adjust_pure_negative\":true,\"boost\":1}},{\"bool\":{\"adjust_pure_negative\":true,\"boost\":1}}],\"should\":[{\"term\":{\"publish_type\":{\"value\":\"1\",\"boost\":1000}}},{\"term\":{\"publish_type\":{\"value\":\"2\",\"boost\":1500}}},{\"term\":{\"publish_type\":{\"value\":\"1\",\"boost\":1000}}},{\"term\":{\"publish_type\":{\"value\":\"2\",\"boost\":1500}}},{\"term\":{\"publish_type\":{\"value\":\"1\",\"boost\":1000}}},{\"term\":{\"publish_type\":{\"value\":\"2\",\"boost\":1500}}}],\"must_not\":{\"exists\":{\"field\":\"level2_case\"}},\"adjust_pure_negative\":true,\"minimum_should_match\":\"0\",\"boost\":1}}";
        ESKit.scroll("judgement_1015", "judgement", dsl, new String[]{"jid"}, consumer);

        writer.flush();
        writer.close();
    }
}
