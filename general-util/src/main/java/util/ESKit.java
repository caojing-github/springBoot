package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pivovarit.function.ThrowingRunnable;
import com.pivovarit.function.ThrowingSupplier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.concurrent.atomic.AtomicInteger;
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
     * 插入
     */
    @Test
    public void test20200422152312() throws IOException {
        String json = "{\"accept_date\":\"2020-03-04\",\"acceptance_fee\":0.0,\"acceptance_fee_level\":-1,\"allChiefJudgeTerm\":\"董玉敏\",\"all_caseinfo_court_term\":\"锦州市凌河区人民法院\",\"all_caseinfo_leveloftria\":-1,\"all_chief_judge\":\"董玉敏\",\"all_clerk\":\"黄彦元\",\"all_judge\":\"董玉敏\",\"all_judge_courts\":[\"董玉敏 锦州市凌河区人民法院\"],\"all_judge_courts_analyzed\":[\"董玉敏 锦州市凌河区人民法院\"],\"all_judge_term\":\"董玉敏\",\"all_judges\":[\"董玉敏\"],\"all_judges_json\":\"[{\\\"names\\\":[\\\"董玉敏\\\"],\\\"needLink\\\":true,\\\"tag\\\":\\\"审判员\\\"},{\\\"names\\\":[\\\"黄彦元\\\"],\\\"needLink\\\":false,\\\"tag\\\":\\\"书记员\\\"}]\",\"all_litigant\":[\"孙世华\",\"河津市腾发工程机械有限公司\"],\"all_litigant_analyzed\":[\"孙世华\",\"河津市腾发工程机械有限公司\"],\"all_litigant_json\":\"[{\\\"extra\\\":{},\\\"handleType\\\":\\\"REG\\\",\\\"name\\\":\\\"河津市腾发工程机械有限公司\\\",\\\"original\\\":\\\"申请人：河津市腾发工程机械有限公司，住所地山西省河津市。\\\",\\\"tags\\\":[\\\"PROPOSER\\\",\\\"COMPANY\\\"]},{\\\"extra\\\":{},\\\"handleType\\\":\\\"REG\\\",\\\"name\\\":\\\"孙世华\\\",\\\"original\\\":\\\"法定代表人：孙世华，该公司经理。\\\",\\\"tags\\\":[\\\"REPRESENTATIVE\\\"]},{\\\"extra\\\":{},\\\"handleType\\\":\\\"REG\\\",\\\"name\\\":\\\"郭蓬恩\\\",\\\"original\\\":\\\"委托诉讼代理人：郭蓬恩，该公司职员。\\\",\\\"tags\\\":[\\\"AGENT\\\"]}]\",\"all_original_text\":[],\"all_text_cause\":\"申请公示催告\",\"all_text_cause_term\":\"申请公示催告\",\"applicant\":[\"河津市腾发工程机械有限公司\"],\"bizScore\":-345,\"caseLevelJson\":\"[{\\\"causeText\\\":\\\"申请公示催告\\\",\\\"level1\\\":\\\"民事\\\",\\\"level2\\\":\\\"适用特殊程序案件案由\\\",\\\"level3\\\":\\\"公示催告程序案件\\\",\\\"level4\\\":\\\"申请公示催告\\\"}]\",\"caseLevels\":[{\"causeText\":\"申请公示催告\",\"level1\":\"民事\",\"level2\":\"适用特殊程序案件案由\",\"level3\":\"公示催告程序案件\",\"level4\":\"申请公示催告\"}],\"case_rel_bean_list\":[],\"causeList\":\"[\\\"申请公示催告\\\"]\",\"city\":\"锦州市\",\"claim_value\":-1,\"courtName\":\"锦州市凌河区人民法院\",\"court_level\":\"基层人民法院\",\"defendant_litigant\":[],\"defendant_litigant_lawfirm\":[],\"defendant_litigant_lawyer\":[],\"defendant_litigant_name\":[],\"documentNumber\":\"（2020）辽0703民催1号\",\"dsid\":\"B752C3197B84A6FDEBB5E6C765931E12\",\"extStr\":\"{\\\"judgeLevel\\\":\\\"\\\",\\\"caseNum\\\":\\\"（2020）辽0703民催1号\\\",\\\"judgeDate\\\":1584892800000,\\\"courtName\\\":\\\"辽宁省锦州市凌河区人民法院\\\",\\\"thirdId\\\":\\\"47c947436f744a8b8d54ab880026690d\\\",\\\"caseName\\\":\\\"河津市腾发工程机械有限公司申请公示催告民事裁定书\\\",\\\"caseType\\\":\\\"催告\\\"}\",\"flag\":0,\"higher_court\":\"辽宁省高级人民法院\",\"instrumentLinks\":[],\"intermediate_court\":\"辽宁省锦州市中级人民法院\",\"isUnPublishedCase\":false,\"is_tax_case\":false,\"jid\":\"B752C3197B84A6FDEBB5E6C765931E12\",\"judgeDate\":1584892800000,\"judgeLevel\":\"\",\"lawLinkInfo\":[{\"articleSequence\":\"第一百五十四条第一款第十一项\",\"end\":0,\"lawName\":\"中华人民共和国民事诉讼法（2017修正）\",\"lawReguDetailFullName\":\"中华人民共和国民事诉讼法（2017修正）第一百五十四条第一款第十一项\",\"lawReguType\":1,\"lid\":\"1c8b5d91f4929b9b464d99a20daaf5e8\",\"start\":0,\"type\":0},{\"articleSequence\":\"第四百五十五条\",\"end\":0,\"lawName\":\"最高人民法院关于适用《中华人民共和国民事诉讼法》的解释\",\"lawReguDetailFullName\":\"最高人民法院关于适用《中华人民共和国民事诉讼法》的解释第四百五十五条\",\"lawReguType\":1,\"lid\":\"acf897fe3e9a193323e9d380eecf3c6c\",\"start\":0,\"type\":0}],\"level1_case\":\"民事\",\"level2_case\":\"适用特殊程序案件案由\",\"level3_case\":\"公示催告程序案件\",\"level4_case\":\"申请公示催告\",\"level_case_all_analyzed\":\"民事 适用特殊程序案件案由 公示催告程序案件 申请公示催告\",\"litigantFlag\":0,\"litigant_company\":[\"河津市腾发工程机械有限公司\"],\"modifier\":\"yujianing\",\"modifyType\":\"ds_update\",\"orderList\":\"[\\\"all_text_litigantinfo\\\",\\\"firstinstance_text_basicinfo\\\",\\\"firstinstance_text_opinion\\\",\\\"firstinstance_text_judgement\\\",\\\"all_text_judge\\\"]\",\"other_litigant\":[\"孙世华\",\"郭蓬恩\"],\"paragraphs\":[{\"labelType\":1,\"lableName\":\"当事人信息\",\"length\":64,\"subParagraphs\":[{\"index\":1,\"length\":28,\"segId\":1,\"sentences\":[{\"index\":1,\"length\":28,\"paragraphId\":1,\"segId\":1,\"text\":\"申请人：河津市腾发工程机械有限公司，住所地山西省河津市。\"}],\"text\":\"申请人：河津市腾发工程机械有限公司，住所地山西省河津市。\"},{\"index\":2,\"length\":16,\"segId\":1,\"sentences\":[{\"index\":1,\"length\":16,\"paragraphId\":2,\"segId\":1,\"text\":\"法定代表人：孙世华，该公司经理。\"}],\"text\":\"法定代表人：孙世华，该公司经理。\"},{\"index\":3,\"length\":18,\"segId\":1,\"sentences\":[{\"index\":1,\"length\":18,\"paragraphId\":3,\"segId\":1,\"text\":\"委托诉讼代理人：郭蓬恩，该公司职员。\"}],\"text\":\"委托诉讼代理人：郭蓬恩，该公司职员。\"}],\"text\":\"申请人：河津市腾发工程机械有限公司，住所地山西省河津市。\\n法定代表人：孙世华，该公司经理。\\n委托诉讼代理人：郭蓬恩，该公司职员。\"},{\"labelType\":2,\"lableName\":\"案件概述\",\"length\":77,\"subParagraphs\":[{\"index\":1,\"length\":77,\"segId\":2,\"sentences\":[{\"index\":1,\"length\":40,\"paragraphId\":1,\"segId\":2,\"text\":\"申请人河津市腾发工程机械有限公司申请公示催告一案，本院于2020年3月4日立案。\"},{\"index\":2,\"length\":37,\"paragraphId\":1,\"segId\":2,\"text\":\"申请人河津市腾发工程机械有限公司于2020年3月17日向本院提出撤回申请。\"}],\"text\":\"申请人河津市腾发工程机械有限公司申请公示催告一案，本院于2020年3月4日立案。申请人河津市腾发工程机械有限公司于2020年3月17日向本院提出撤回申请。\"}],\"text\":\"申请人河津市腾发工程机械有限公司申请公示催告一案，本院于2020年3月4日立案。申请人河津市腾发工程机械有限公司于2020年3月17日向本院提出撤回申请。\"},{\"labelType\":7,\"lableName\":\"一审法院认为\",\"length\":50,\"subParagraphs\":[{\"index\":1,\"length\":50,\"segId\":7,\"sentences\":[{\"index\":1,\"length\":50,\"paragraphId\":1,\"segId\":7,\"text\":\"本院认为，申请人河津市腾发工程机械有限公司在公示催告前向本院提出撤回申请，不违反法律规定，应予准许。\"}],\"text\":\"本院认为，申请人河津市腾发工程机械有限公司在公示催告前向本院提出撤回申请，不违反法律规定，应予准许。\"}],\"text\":\"本院认为，申请人河津市腾发工程机械有限公司在公示催告前向本院提出撤回申请，不违反法律规定，应予准许。\"},{\"labelType\":8,\"lableName\":\"一审裁判结果\",\"length\":345,\"subParagraphs\":[{\"index\":1,\"length\":295,\"segId\":8,\"sentences\":[{\"index\":1,\"length\":295,\"paragraphId\":1,\"segId\":8,\"text\":\"依照<a class=\\\"lawregu_link\\\" lawregu-detail lawreguid=\\\"1c8b5d91f4929b9b464d99a20daaf5e8\\\" lawregutiao=\\\"第一百五十四条\\\">《中华人民共和国民事诉讼法》第一百五十四条第一款第十一项</a>、<a class=\\\"lawregu_link\\\" lawregu-detail lawreguid=\\\"acf897fe3e9a193323e9d380eecf3c6c\\\" lawregutiao=\\\"第四百五十五条\\\">《最高人民法院关于适用<中华人民共和国民事诉讼法>的解释》第四百五十五条</a>规定，裁定如下：\"}],\"text\":\"依照<a class=\\\"lawregu_link\\\" lawregu-detail lawreguid=\\\"1c8b5d91f4929b9b464d99a20daaf5e8\\\" lawregutiao=\\\"第一百五十四条\\\">《中华人民共和国民事诉讼法》第一百五十四条第一款第十一项</a>、<a class=\\\"lawregu_link\\\" lawregu-detail lawreguid=\\\"acf897fe3e9a193323e9d380eecf3c6c\\\" lawregutiao=\\\"第四百五十五条\\\">《最高人民法院关于适用<中华人民共和国民事诉讼法>的解释》第四百五十五条</a>规定，裁定如下：\"},{\"index\":2,\"length\":20,\"segId\":8,\"sentences\":[{\"index\":1,\"length\":20,\"paragraphId\":2,\"segId\":8,\"text\":\"准许河津市腾发工程机械有限公司撤回申请。\"}],\"text\":\"准许河津市腾发工程机械有限公司撤回申请。\"},{\"index\":3,\"length\":28,\"segId\":8,\"sentences\":[{\"index\":1,\"length\":28,\"paragraphId\":3,\"segId\":8,\"text\":\"申请费100元，由申请人河津市腾发工程机械有限公司负担。\"}],\"text\":\"申请费100元，由申请人河津市腾发工程机械有限公司负担。\"}],\"text\":\"依照<a class=\\\"lawregu_link\\\" lawregu-detail lawreguid=\\\"1c8b5d91f4929b9b464d99a20daaf5e8\\\" lawregutiao=\\\"第一百五十四条\\\">《中华人民共和国民事诉讼法》第一百五十四条第一款第十一项</a>、<a class=\\\"lawregu_link\\\" lawregu-detail lawreguid=\\\"acf897fe3e9a193323e9d380eecf3c6c\\\" lawregutiao=\\\"第四百五十五条\\\">《最高人民法院关于适用<中华人民共和国民事诉讼法>的解释》第四百五十五条</a>规定，裁定如下：\\n准许河津市腾发工程机械有限公司撤回申请。\\n申请费100元，由申请人河津市腾发工程机械有限公司负担。\"},{\"labelType\":9,\"lableName\":\"审判人员\",\"length\":25,\"subParagraphs\":[{\"index\":1,\"length\":6,\"segId\":9,\"sentences\":[{\"index\":1,\"length\":6,\"paragraphId\":1,\"segId\":9,\"text\":\"审判员董玉敏\"}],\"text\":\"审判员董玉敏\"},{\"index\":2,\"length\":11,\"segId\":9,\"sentences\":[{\"index\":1,\"length\":11,\"paragraphId\":2,\"segId\":9,\"text\":\"二〇二〇年三月二十三日\"}],\"text\":\"二〇二〇年三月二十三日\"},{\"index\":3,\"length\":6,\"segId\":9,\"sentences\":[{\"index\":1,\"length\":6,\"paragraphId\":3,\"segId\":9,\"text\":\"书记员黄彦元\"}],\"text\":\"书记员黄彦元\"}],\"text\":\"审判员董玉敏\\n二〇二〇年三月二十三日\\n书记员黄彦元\"}],\"prosecutor_litigant\":[{\"lawyerInfo\":[],\"name\":\"河津市腾发工程机械有限公司\"}],\"prosecutor_litigant_lawfirm\":[],\"prosecutor_litigant_lawyer\":[],\"prosecutor_litigant_name\":[\"河津市腾发工程机械有限公司\"],\"province\":\"辽宁省\",\"publish_type\":0,\"raw_public_prosecution\":\"\",\"region\":\"凌河区\",\"super_court\":\"最高人民法院\",\"thirdId\":\"47c947436f744a8b8d54ab880026690d\",\"thirdParty_litigant\":[],\"timeLimitOfTrial\":19,\"timeLimitOfTrialType\":1,\"title\":\"河津市腾发工程机械有限公司申请公示催告民事裁定书\",\"tradeDefendantCity\":[],\"tradeDefendantLevel1\":[],\"tradeDefendantLevel2\":[],\"tradeDefendantProvince\":[],\"tradeEnterprises\":[{\"enterpriseName\":\"河津市腾发工程机械有限公司\",\"litigationStatus\":0,\"tradeCustomizedLevel2\":\"0\",\"tradeIsLawyer\":\"0\"}],\"tradeOtherCity\":[],\"tradeOtherLevel1\":[],\"tradeOtherLevel2\":[],\"tradeOtherPersons\":[\"孙世华\"],\"tradeOtherProvince\":[],\"tradeProsecutorCity\":[null],\"tradeProsecutorCompanys\":[\"河津市腾发工程机械有限公司\"],\"tradeProsecutorLevel1\":[null],\"tradeProsecutorLevel2\":[null],\"tradeProsecutorProvince\":[null],\"trade_acceptance_fee_level\":-1,\"trialCaseType\":\"催告\",\"type\":2}";

        IndexRequest indexRequest = new IndexRequest("judgementsearch_dev", "judgement", "B752C3197B84A6FDEBB5E6C765931E12")
            .source(json, XContentType.JSON)
            .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

        ES.DEV.client.index(indexRequest, RequestOptions.DEFAULT);
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
     * es scroll 案号大于等于35字
     */
    @Test
    public void test20200424112952() throws IOException {
        BufferedWriter writer1 = new BufferedWriter(new java.io.FileWriter("/Users/caojing/Desktop/案号大于等于20字jid.txt"));
        BufferedWriter writer2 = new BufferedWriter(new java.io.FileWriter("/Users/caojing/Desktop/案号大于等于20字.txt"));
        AtomicInteger i = new AtomicInteger();

        Consumer<List<JSONObject>> consumer = x -> x.forEach(y -> {
            System.out.println(i.incrementAndGet());

            String casenumber = y.getString("all_caseinfo_casenumber");
            if (StringUtils.isNotBlank(casenumber) && casenumber.length() >= 35) {
                String jid = y.getString("jid");
                String url = "https://alphalawyer.cn/#/app/tool/result/%7B%5B%5D,%7D/detail/" + jid;

                ThrowingRunnable.sneaky(() -> {
                    writer1.write(jid + "\r\n");
                    writer2.write(url + "\r\n");
                }).run();
            }
        });
        scroll("judgement_1015", "judgement", "{\"match_all\":{}}", new String[]{"jid", "all_caseinfo_casenumber"}, consumer);

        writer1.flush();
        writer1.close();

        writer2.flush();
        writer2.close();
    }

    /**
     * es查询导出文件
     */
    @Test
    public void test20200417164050() throws IOException {
        BufferedWriter writer = new BufferedWriter(new java.io.FileWriter("/Users/caojing/Desktop/标题为空案例.txt"));

        String dsl = "{\"_source\":[\"jid\"],\"size\":1154,\"query\":{\"bool\":{\"must_not\":{\"exists\":{\"field\":\"all_caseinfo_casename\"}}}}}";
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
//            writer.write(jid + "\r\n");
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
