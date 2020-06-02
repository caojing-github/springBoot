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
     * JSON.toJSONBytes
     */
    @Test
    public void test20200528125737() {
        byte[] bytes = JSON.toJSONBytes("caojing666");
        String s = JSONObject.parseObject(bytes, String.class);
        System.out.println();
    }
}
