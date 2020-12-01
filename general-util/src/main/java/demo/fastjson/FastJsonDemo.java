package demo.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * https://blog.csdn.net/GeekSnow/article/details/87984468
 *
 * @author CaoJing
 * @date 2019/12/19 17:42
 */
@SuppressWarnings("all")
public class FastJsonDemo {

    /**
     * 从文件解析json数据
     */
    @Test
    public void test20191219170919() throws IOException {
        FileInputStream fis = new FileInputStream(new File("/Users/caojing/IdeaProjects/springBoot/token.txt"));
        List<JSONObject> o = JSON.parseObject(fis, List.class);
        System.out.println();
    }

    /**
     * JSONPath.read
     */
    @Test
    public void test20200515173907() {
        String jsonStr = "{\"ext\":\"{\\\"judgeLevel\\\":\\\"\\\",\\\"caseNum\\\":\\\"（2020）湘0121执2531号\\\",\\\"judgeDate\\\":1589212800000,\\\"courtName\\\":\\\"湖南省长沙县人民法院\\\",\\\"caseName\\\":\\\"王龙平、陈彭林执行实施类执行通知书\\\",\\\"caseType\\\":\\\"执行实施\\\"}\",\"publish_type\":\"\\u0000\\u0000\\u0000\\u0000\",\"flag\":\"\\u0000\\u0000\\u0000\\u0000\",\"jid\":\"71FC7D6EA545E96E764BA4F97E13B632\",\"dsid\":\"71FC7D6EA545E96E764BA4F97E13B632\",\"thirdId\":\"36ad2840-f312-42f0-8285-abb9018597d3\",\"all_judgementinfo_date\":\"2020-05-12\",\"modifier\":\"caojing\",\"type\":\"\\u0000\\u0000\\u0000\\u0000\",\"crawl_time\":\"1589404299000\",\"upload_time\":\"1589299200000\",\"judge_level\":\"\",\"modifytype\":\"ds_update\",\"all_caseinfo_casename\":\"王龙平、陈彭林执行实施类执行通知书\",\"trial_case_type\":\"执行实施\",\"text\":\"王龙平、陈彭林执行实施类执行通知书\\n湖南省长沙县人民法院\\n执行案件结案通知书\\n（2020）湘0121执2531号\\n王龙平、陈彭林：\\n关于被执行人王龙平、陈彭林罚金执行一案，经本院依法执行，被执行人王龙平、陈彭林已经履行了本院已经发生法律效力的（2019）湘0121刑初92号刑事判决书所确定的义务，现该案已全部执行完毕，特此通知。\\n二〇二〇年五月十四日\",\"id\":\"71FC7D6EA545E96E764BA4F97E13B632\",\"all_caseinfo_casenumber\":\"（2020）湘0121执2531号\"}";
        System.out.println(JSONPath.read(jsonStr, "$.thirdId"));
        System.out.println(JSONPath.read(jsonStr, "$.ext.caseNum"));

        String s = "";
        System.out.println(JSONPath.read(s, "$.thirdId"));
        System.out.println(JSONPath.read(s, "$.ext.caseNum"));
    }

    /**
     * JSONPath.set
     */
    @Test
    public void test20201012133249() {
        String jsonStr = "{\"ext\":\"{\\\"judgeLevel\\\":\\\"\\\",\\\"caseNum\\\":\\\"（2020）湘0121执2531号\\\",\\\"judgeDate\\\":1589212800000,\\\"courtName\\\":\\\"湖南省长沙县人民法院\\\",\\\"caseName\\\":\\\"王龙平、陈彭林执行实施类执行通知书\\\",\\\"caseType\\\":\\\"执行实施\\\"}\",\"publish_type\":\"\\u0000\\u0000\\u0000\\u0000\",\"flag\":\"\\u0000\\u0000\\u0000\\u0000\",\"jid\":\"71FC7D6EA545E96E764BA4F97E13B632\",\"dsid\":\"71FC7D6EA545E96E764BA4F97E13B632\",\"thirdId\":\"36ad2840-f312-42f0-8285-abb9018597d3\",\"all_judgementinfo_date\":\"2020-05-12\",\"modifier\":\"caojing\",\"type\":\"\\u0000\\u0000\\u0000\\u0000\",\"crawl_time\":\"1589404299000\",\"upload_time\":\"1589299200000\",\"judge_level\":\"\",\"modifytype\":\"ds_update\",\"all_caseinfo_casename\":\"王龙平、陈彭林执行实施类执行通知书\",\"trial_case_type\":\"执行实施\",\"text\":\"王龙平、陈彭林执行实施类执行通知书\\n湖南省长沙县人民法院\\n执行案件结案通知书\\n（2020）湘0121执2531号\\n王龙平、陈彭林：\\n关于被执行人王龙平、陈彭林罚金执行一案，经本院依法执行，被执行人王龙平、陈彭林已经履行了本院已经发生法律效力的（2019）湘0121刑初92号刑事判决书所确定的义务，现该案已全部执行完毕，特此通知。\\n二〇二〇年五月十四日\",\"id\":\"71FC7D6EA545E96E764BA4F97E13B632\",\"all_caseinfo_casenumber\":\"（2020）湘0121执2531号\"}";
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        System.out.println(JSONPath.read(jsonStr, "$.thirdId"));

        JSONPath.set(jsonObject, "$.thirdId", "66666");
        System.out.println(jsonObject.getString("thirdId"));
    }

    /**
     * JsonPath表达式
     */
    @Test
    public void test20201021101952() {
        String s = "{\"took\":1451,\"timed_out\":false,\"_shards\":{\"total\":80,\"successful\":80,\"skipped\":0,\"failed\":0},\"hits\":{\"total\":103486881,\"max_score\":0,\"hits\":[]},\"aggregations\":{\"is_un_published_case\":{\"doc_count_error_upper_bound\":0,\"sum_other_doc_count\":0,\"buckets\":[{\"key\":0,\"key_as_string\":\"false\",\"doc_count\":21115433,\"un_published_reason\":{\"doc_count_error_upper_bound\":0,\"sum_other_doc_count\":0,\"buckets\":[{\"key\":\"不公开理由：以调解方式结案的\",\"doc_count\":36709},{\"key\":\"不公开理由：离婚诉讼或者涉及未成年子女抚养、监护的\",\"doc_count\":10038},{\"key\":\"不公开理由：人民法院认为不宜在互联网公布的其他情形\",\"doc_count\":3765},{\"key\":\"不公开理由：确认人民调解协议效力的\",\"doc_count\":284},{\"key\":\"不公开理由：涉及国家秘密的\",\"doc_count\":25},{\"key\":\"不公开理由：未成年人犯罪的\",\"doc_count\":7}]}},{\"key\":1,\"key_as_string\":\"true\",\"doc_count\":20027896,\"un_published_reason\":{\"doc_count_error_upper_bound\":0,\"sum_other_doc_count\":0,\"buckets\":[{\"key\":\"不公开理由：以调解方式结案的\",\"doc_count\":1417694},{\"key\":\"不公开理由：人民法院认为不宜在互联网公布的其他情形\",\"doc_count\":903870},{\"key\":\"不公开理由：离婚诉讼或者涉及未成年子女抚养、监护的\",\"doc_count\":505595},{\"key\":\"不公开理由：确认人民调解协议效力的\",\"doc_count\":207711},{\"key\":\"文书种类：调解书\",\"doc_count\":23126},{\"key\":\"不公开理由：未成年人犯罪的\",\"doc_count\":13524},{\"key\":\"不公开理由：涉及国家秘密的\",\"doc_count\":8885},{\"key\":\"文书种类：裁定书\",\"doc_count\":8210},{\"key\":\"文书种类：判决书\",\"doc_count\":3657},{\"key\":\"文书全文\",\"doc_count\":1276},{\"key\":\" \",\"doc_count\":1028},{\"key\":\"贵州省威宁彝族回族苗族自治县人民法院\",\"doc_count\":780},{\"key\":\"文书种类：通知书\",\"doc_count\":375},{\"key\":\"文书种类：其他\",\"doc_count\":270},{\"key\":\"贵州省黔西县人民法院\",\"doc_count\":230},{\"key\":\"甘肃省临夏县人民法院\",\"doc_count\":149},{\"key\":\"不公开理由：\",\"doc_count\":135},{\"key\":\"贵州省金沙县人民法院\",\"doc_count\":122},{\"key\":\"山东省招远市人民法院\",\"doc_count\":116},{\"key\":\"四川省宁南县人民法院\",\"doc_count\":60},{\"key\":\"贵州省纳雍县人民法院\",\"doc_count\":56},{\"key\":\"山东省枣庄市峄城区人民法院\",\"doc_count\":52},{\"key\":\"湖南省常宁市人民法院\",\"doc_count\":48},{\"key\":\"文书种类：决定书\",\"doc_count\":43},{\"key\":\"四川省黑水县人民法院\",\"doc_count\":26},{\"key\":\"辽宁省昌图县人民法院\",\"doc_count\":22},{\"key\":\"不公开理由：不宜在互联网公布的\",\"doc_count\":19},{\"key\":\"文书种类：函\",\"doc_count\":16},{\"key\":\"昌图县人民法院\",\"doc_count\":13},{\"key\":\"秦皇岛市抚宁区人民法院\",\"doc_count\":12},{\"key\":\"寿县人民法院\",\"doc_count\":10},{\"key\":\"淮南市谢家集区人民法院\",\"doc_count\":10},{\"key\":\"河北省献县人民法院\",\"doc_count\":9},{\"key\":\"天津市宝坻区人民法院\",\"doc_count\":5},{\"key\":\"张家口市宣化区人民法院\",\"doc_count\":5},{\"key\":\"文书种类：答复\",\"doc_count\":5},{\"key\":\"深泽县人民法院\",\"doc_count\":5},{\"key\":\".辽宁省昌图县人民法院\",\"doc_count\":3},{\"key\":\"北京市第二中级人民法院\",\"doc_count\":3},{\"key\":\"北京市第四中级人民法院\",\"doc_count\":3},{\"key\":\"广东省湛江市中级人民法院\",\"doc_count\":3},{\"key\":\"招远市人民法院\",\"doc_count\":3},{\"key\":\"文书种类：令\",\"doc_count\":3},{\"key\":\"丹东铁路运输法院\",\"doc_count\":2},{\"key\":\"人民法院认为其他不公开理由\",\"doc_count\":2},{\"key\":\"唐山市曹妃甸区人民法院\",\"doc_count\":2},{\"key\":\"四川省遂宁市中级人民法院\",\"doc_count\":2},{\"key\":\"围场满族蒙古族自治县人民法院\",\"doc_count\":2},{\"key\":\"平山县人民法院\",\"doc_count\":2},{\"key\":\"晋州市人民法院\",\"doc_count\":2},{\"key\":\"江苏省苏州市中级人民法院\",\"doc_count\":2},{\"key\":\"涉县人民法院\",\"doc_count\":2},{\"key\":\"福建省大田县人民法院\",\"doc_count\":2},{\"key\":\"福建省顺昌县人民法院\",\"doc_count\":2},{\"key\":\"行政判决书\",\"doc_count\":2},{\"key\":\"辽宁省葫芦岛市中级人民法院\",\"doc_count\":2},{\"key\":\"上海市第二中级人民法院\",\"doc_count\":1},{\"key\":\"元氏县人民法院\",\"doc_count\":1},{\"key\":\"公布案号：2018粤19民终11220号\",\"doc_count\":1},{\"key\":\"公布案号：2019粤19民终184号\",\"doc_count\":1},{\"key\":\"公布案号：2019粤19民终233号\",\"doc_count\":1},{\"key\":\"其他不公开理由\",\"doc_count\":1},{\"key\":\"南和县人民法院\",\"doc_count\":1},{\"key\":\"吉林省长春市中级人民法院\",\"doc_count\":1},{\"key\":\"唐山市路北区人民法院\",\"doc_count\":1},{\"key\":\"四川省成都市中级人民法院\",\"doc_count\":1},{\"key\":\"四川省自贡市自流井区人民法院\",\"doc_count\":1},{\"key\":\"天津市第一中级人民法院\",\"doc_count\":1},{\"key\":\"太原市小店区人民法院\",\"doc_count\":1},{\"key\":\"山东省滨州市中级人民法院\",\"doc_count\":1},{\"key\":\"广东省高级人民法院\",\"doc_count\":1},{\"key\":\"广西壮族自治区北海市海城区人民法院\",\"doc_count\":1},{\"key\":\"广西壮族自治区恭城瑶族自治县人民法院\",\"doc_count\":1},{\"key\":\"昆明市西山区人民法院\",\"doc_count\":1},{\"key\":\"昌\\ufffd\\ufffd\\ufffd县人民法院\",\"doc_count\":1},{\"key\":\"柏乡县人民法院\",\"doc_count\":1},{\"key\":\"案号：（2014）东中法少刑终字第18号\",\"doc_count\":1},{\"key\":\"案号：（2014）东中法少刑终字第22号\",\"doc_count\":1},{\"key\":\"案号：（2015）东中法少刑初字第5号\",\"doc_count\":1},{\"key\":\"案号：（2015）东中法少刑初字第7号\",\"doc_count\":1},{\"key\":\"案号：（2016）粤19刑终335号\",\"doc_count\":1},{\"key\":\"案号：（2016）粤19刑终661号\",\"doc_count\":1},{\"key\":\"案号：（2016）粤19刑终699号\",\"doc_count\":1},{\"key\":\"案号：（2016）粤19刑终724号\",\"doc_count\":1},{\"key\":\"案号：（2017）粤19刑终571号\",\"doc_count\":1},{\"key\":\"案号：（2018）粤19刑初10号\",\"doc_count\":1},{\"key\":\"案号：（2018）粤19刑终101号\",\"doc_count\":1},{\"key\":\"案号：（2018）粤19刑终18号\",\"doc_count\":1},{\"key\":\"案号：（2018）粤19刑终21号\",\"doc_count\":1},{\"key\":\"案号：（2018）粤19刑终88号\",\"doc_count\":1},{\"key\":\"案号：（2019）粤19刑终1294号\",\"doc_count\":1},{\"key\":\"案号：（2019）粤19民终10960号\",\"doc_count\":1},{\"key\":\"案号：（2019）粤19民终3271号\",\"doc_count\":1},{\"key\":\"案号：（2019）粤19民终6796号\",\"doc_count\":1},{\"key\":\"案号：（2020）粤19刑更162号\",\"doc_count\":1},{\"key\":\"案号：（2020）粤19刑更188号\",\"doc_count\":1},{\"key\":\"江苏省南京市六合区人民法院\",\"doc_count\":1},{\"key\":\"江西省上饶市中级人民法院\",\"doc_count\":1},{\"key\":\"河南省新郑市人民法院\",\"doc_count\":1},{\"key\":\"河南省淮阳县人民法院\",\"doc_count\":1},{\"key\":\"河南省郑州市惠济区人民法院\",\"doc_count\":1},{\"key\":\"济南铁路运输中级法院\",\"doc_count\":1},{\"key\":\"浙江省余姚市人民法院\",\"doc_count\":1},{\"key\":\"浙江省宁波市鄞州区人民法院\",\"doc_count\":1},{\"key\":\"浙江省杭州市中级人民法院\",\"doc_count\":1},{\"key\":\"浙江省桐乡市人民法院\",\"doc_count\":1},{\"key\":\"浙江省高级人民法院\",\"doc_count\":1},{\"key\":\"湖北省武汉市中级人民法院\",\"doc_count\":1},{\"key\":\"湖北省武汉市江岸区人民法院\",\"doc_count\":1},{\"key\":\"湖北省高级人民法院\",\"doc_count\":1},{\"key\":\"湖南省湘潭市中级人民法院\",\"doc_count\":1},{\"key\":\"湖南省湘潭市岳塘区人民法院\",\"doc_count\":1},{\"key\":\"石家庄市栾城区人民法院\",\"doc_count\":1},{\"key\":\"石家庄市桥西区人民法院\",\"doc_count\":1},{\"key\":\"福建省福州市中级人民法院\",\"doc_count\":1},{\"key\":\"辛集市人民法院\",\"doc_count\":1},{\"key\":\"辽宁省丹东市振兴区人民法院\",\"doc_count\":1},{\"key\":\"辽宁省鞍山市中级人民法院\",\"doc_count\":1},{\"key\":\"重庆市永川区人民法院\",\"doc_count\":1},{\"key\":\"长垣市人民法院\",\"doc_count\":1},{\"key\":\"青海省大通回族土族自治县人民法院\",\"doc_count\":1},{\"key\":\"高邑县人民法院\",\"doc_count\":1},{\"key\":\"黑龙江省绥化市中级人民法院\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1012号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1033号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1111号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1112号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1149号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1229号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1255号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1395号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1437号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1468号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1473号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1474号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1539号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1574号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1629号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1666号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1816号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1875号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1876号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1890号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1907号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1930号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1981号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第1987号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第2010号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第2012号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第2093号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第2131号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第2137号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第2210号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第2284号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第2340号摘要\",\"doc_count\":1},{\"key\":\"（2014）东中法民五终字第806号摘要\",\"doc_count\":1},{\"key\":\"（2015）东中法民一终字第2504号调解案件摘要\",\"doc_count\":1},{\"key\":\"（2016）粤19民终2565号调解案件摘要\",\"doc_count\":1},{\"key\":\"（2016）粤19民终3303号调解案件摘要\",\"doc_count\":1},{\"key\":\"（2016）粤19民终5048号调解案件摘要\",\"doc_count\":1},{\"key\":\"（2016）粤19民终5269号调解案件摘要\",\"doc_count\":1},{\"key\":\"（2016）粤19民终5585号调解案件摘要\",\"doc_count\":1},{\"key\":\"（2016）粤19民终6863号调解案件摘要\",\"doc_count\":1},{\"key\":\"（2017）粤19民终10153号调解案件摘要\",\"doc_count\":1},{\"key\":\"（2018）粤19民终1005号调解案件摘要\",\"doc_count\":1},{\"key\":\"（2018）粤19民终11849号调解案件摘要\",\"doc_count\":1},{\"key\":\"（2018）粤19民终1748号调解案件摘要\",\"doc_count\":1},{\"key\":\"（2018）粤19民终1749号调解案件摘要\",\"doc_count\":1},{\"key\":\"（2018）粤19民终1752号调解案件摘要\",\"doc_count\":1},{\"key\":\"（2018）粤19民终2299号调解案件摘要\",\"doc_count\":1},{\"key\":\"（2018）粤19民终2481号调解案件摘要\",\"doc_count\":1},{\"key\":\"（2018）粤19民终4343号调解案件摘要\",\"doc_count\":1},{\"key\":\"（2018）粤19民终5038号调解案件摘要\",\"doc_count\":1},{\"key\":\"（2018）粤19民终7811号调解案件摘要\",\"doc_count\":1},{\"key\":\"（2018）粤19民终8997号调解案件摘要\",\"doc_count\":1},{\"key\":\"（2018）粤19民终9864号调解案件摘要\",\"doc_count\":1},{\"key\":\"（2019）粤19民终8014号调解案件摘要\",\"doc_count\":1}]}}]}}}";
        List<String> list = (List<String>) JSONPath.read(s, "$.aggregations.is_un_published_case.buckets[0].un_published_reason.buckets[*].key");
        System.out.println();
    }

    /**
     * JSON.toJSONBytes
     */
    @Test
    public void test20200528125737() {
        byte[] bytes = JSON.toJSONBytes("caojing666");
        String s = JSONObject.parseObject(bytes, String.class);
        System.out.println();
    }
}
