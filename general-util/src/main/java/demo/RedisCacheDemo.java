package demo;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.junit.Test;
import util.ESKit;
import util.JdbcUtil;
import util.RedisCache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat;
import static util.ChineseNumToArabicNumUtil.arabicNumToChineseNum;

/**
 * RedisCache测试类
 *
 * @author CaoJing
 * @date 2019/10/22 20:49
 */
@Slf4j
public class RedisCacheDemo {

    /**
     * 获取token
     */
    public static void test20191022204927(String env) throws Exception {

        // 初始化redis
        RedisCache.initialPool(env);

        List<Map> list = RedisCache.keys("pusher:user:token*list")
            .stream()
            .map(x -> {
                String[] split = x.split(":");
                String officeId = split[3];
                String userId = split[4];

                String sql = "select a.*, b.name AS roleName from pusher_user a, pusher_role b where a.id = '" + userId + "' and a.role_id = b.id";

                Map<String, Object> user = null;
                Map<String, Object> office = null;
                try {
                    List<Map<String, Object>> list1 = JdbcUtil.executeQuery(JdbcUtil.getByEnv(env).getConnection(), sql, null);
                    if (list1.isEmpty()) {
                        return null;
                    }

                    user = list1.get(0);
                    if (null == user || "99".equals(user.get("status"))) {
                        return null;
                    }

                    String sql2 = "select * from pusher_office a, pusher_user b where a.id = b.pusher_office_id and b.id = '" + userId + "'";

                    List<Map<String, Object>> list2 = JdbcUtil.executeQuery(JdbcUtil.getByEnv(env).getConnection(), sql2, null);
                    if (list2.isEmpty()) {
                        return null;
                    }

                    office = list2.get(0);
                } catch (Exception e) {
                    log.error("", e);
                }

                Map map = new HashMap();
                map.put("officeId", officeId);
                map.put("officeName", office.get("name"));
                map.put("userId", userId);
                map.put("userName", user.get("name"));
                map.put("roleName", user.get("roleName"));
                map.put("token", JSON.parseArray(RedisCache.getString(x), String.class));
                return map;

            }).filter(Objects::nonNull)
            .collect(Collectors.toList());

//        FileOutputStream fos = new FileOutputStream(new File("token.txt"));
        FileOutputStream fos = new FileOutputStream(new File("/Users/icourt/IdeaProjects/springBoot/general-util/token.txt"));

        // 以人类可读方式打印
        log.info(JSON.toJSONString(list, true));

        // 将结果输出到文件
        JSON.writeJSONString(fos, list, PrettyFormat);
    }

    public static void main(String[] args) throws Exception {
        args = new String[]{"dev"};
        if (ArrayUtils.isEmpty(args)) {
            test20191022204927("dev");
        } else {
            test20191022204927(args[0]);
        }
    }

    /**
     * 一次性锁测试
     */
    @Test
    public void test20191128164924() throws InterruptedException {
        System.out.println(RedisCache.onceLock("caojing666", 4));
        System.out.println(RedisCache.onceLock("caojing666", 4));
        Thread.sleep(4 * 1000);
        System.out.println(RedisCache.isExists("autoReleaseLock:caojing666"));
    }

    @Test
    public void test20200105194731() {
        final Map<String, String> map = RedisCache.getJedis().hgetAll("schedule:2020-01-31 00:00:00");
        System.out.println(JSON.toJSONString(map, PrettyFormat));
    }

    @Test
    public void test20200201021719() {
        RedisCache.getJedis()
            .keys("schedule:*")
            .stream()
            .sorted()
            .forEach(System.out::println);
    }

    @Test
    public void test20200130041641() {
        final Map<String, String> map = RedisCache.getJedis().hgetAll("schedule:2020-02-16 00:00:00 2020-02-17 00:00:00");
        System.out.println(JSON.toJSONString(map, PrettyFormat));
    }

    @Test
    public void test20200207024018() {
        System.out.println("lower:" + RedisCache.getJedis().get("lower"));
        System.out.println("upper:" + RedisCache.getJedis().get("upper"));
        System.out.println("esDelet:" + RedisCache.getJedis().get("esDelet"));
        System.out.println("hbDelet:" + RedisCache.getJedis().get("hbDelet"));
        System.out.println("dsidMod:" + RedisCache.getJedis().get("dsidMod"));
    }

    /**
     * 重设
     */
    @Test
    public void test20200207024220() {
        RedisCache.getJedis().set("lower", "0");
        RedisCache.getJedis().set("upper", "0");
        RedisCache.getJedis().set("esDelet", "0");
        RedisCache.getJedis().set("hbDelet", "0");
        RedisCache.getJedis().set("dsidMod", "0");
    }

    @Test
    public void test20200206020821() {
        log.info("172.16.69.3 es.scroll:" + RedisCache.getJedis().get("es.scroll"));
        log.info("172.16.69.2 es:scroll:" + RedisCache.getJedis().get("es:scroll"));
        log.info("172.16.69.1:es:scroll:" + RedisCache.getJedis().get("172.16.69.1:es:scroll"));
    }

    /**
     * https://blog.csdn.net/adsl624153/article/details/79562282
     */
    @Test
    public void test20200222014549() {
        ParserConfig config = new ParserConfig();
        config.setAutoTypeSupport(true);

        String tradeInfos = RedisCache.getJedis().hget("\"tradeInfos\"", "\"河北宝生工程科技有限公司\"");
        JSONObject parse = (JSONObject) JSON.parse(tradeInfos, config);

        // 公司信息
        System.out.println(JSON.toJSONString(parse, PrettyFormat));

        // 子行业对应行业大类关系
        System.out.println(JSON.toJSONString(RedisCache.getJedis().hgetAll("\"companyIndustryHash\""), PrettyFormat));

        // 金属制品业对应的大类行业
        System.out.println(RedisCache.getJedis().hget("\"companyIndustryHash\"", "\"金属制品业\""));
    }

    /**
     * 庭审视频
     */
    @Test
    public void test20200501204357() {
        // 查询结果为 tingshen_video_v2/_doc/_search  _id
        System.out.println(RedisCache.getJedis().get("tingshen_e6f652c4f5d311f1baa91cb943c623e9"));
        // 删除
        RedisCache.getJedis().del("tingshen_e6f652c4f5d311f1baa91cb943c623e9");
    }

    /**
     * 法条沿革查询
     */
    @Test
    public void test20200602102403() {
        RedisCache.initialPool("redis_3");
        String lid = "d1fbbf7d9df9b92112e7a1bfbf50d15e";
        String location = "第六十三条";
        String s = RedisCache.get("l:" + lid + ":" + location + ":h");
        JSONArray jsonArray = JSON.parseArray(s);
        System.out.println();
    }

    @Test
    public void test20200602104835() throws IOException {
        RedisCache.initialPool("redis_3");

        // 民法典lid
        String lid1 = "38c5d01eedd454cc67a12a22cfe4a84d";
        Map<String, String> map = new LinkedHashMap<>();

        GetRequest request = new GetRequest("law_upsert", "law_regu", lid1);
        GetResponse response = ESKit.ES.PRO.client.get(request, RequestOptions.DEFAULT);
        JSONObject jsonObject = JSON.parseObject(response.getSourceAsString());
        JSONArray lawRegulationIndexesJsons = jsonObject.getJSONArray("law_regulation_article_jsons");

        for (int i = 0; i < lawRegulationIndexesJsons.size(); i++) {
            JSONObject j1 = lawRegulationIndexesJsons.getJSONObject(i);
            String fullName = j1.getString("fullName");
            String text5 = j1.getString("text");

            if (StringUtils.isNotBlank(text5)) {
                if (text5.startsWith("<br/>")) {
                    text5 = StringUtils.removeFirst(text5, "<br/>");
                }
                map.put(fullName, text5);
            }
        }

        ExcelReader reader = ExcelUtil.getReader("/Users/caojing/Desktop/民法典与前法映射 3.xlsx");
        List<Map<String, Object>> readAll = reader.readAll();
        Map<String, Map<String, String>> map2 = new LinkedHashMap<>();

        readAll.forEach(x -> {
            // 民法典
            int s1;
            try {
                s1 = ((Long) x.get("民法典条数")).intValue();
            } catch (Exception e) {
                return;
            }
            // 跳过
            if (s1 != 218) {
                return;
            }
            String location1 = String.format("第%s条", arabicNumToChineseNum(s1));
            String s = RedisCache.get("l:" + lid1 + ":" + location1 + ":h");
            JSONArray jsonArray = JSON.parseArray(s);

            if (jsonArray == null) {
                jsonArray = new JSONArray();
                String text1 = location1 + "　" + map.get(location1);

                JSONObject jsonObject1 = new JSONObject()
                    .fluentPut("actionName", "修改")
                    .fluentPut("lid", lid1)
                    .fluentPut("postingDate", "2020-05-28") // 发文日期
                    .fluentPut("text", text1)
                    .fluentPut("title", "中华人民共和国民法典");

                jsonArray.add(jsonObject1);
            }
            // 以前的法规
            String lid2 = x.get("旧法id").toString();
            if (StringUtils.isBlank(lid2)) {
                return;
            }

            GetRequest request2 = new GetRequest("law_upsert", "law_regu", lid2);
            GetResponse response2 = Try.of(() -> ESKit.ES.PRO.client.get(request2, RequestOptions.DEFAULT)).onFailure(e -> log.error("", e)).get();
            JSONObject jsonObject3 = JSON.parseObject(response2.getSourceAsString());
            String title2 = jsonObject3.getString("title");

            Map<String, String> tempMap = new LinkedHashMap<>();
            if (map2.get(title2) == null) {
                JSONArray lawRegulationIndexesJsons2 = jsonObject3.getJSONArray("law_regulation_article_jsons");

                for (int i = 0; i < lawRegulationIndexesJsons2.size(); i++) {
                    JSONObject j1 = lawRegulationIndexesJsons2.getJSONObject(i);
                    String fullName = j1.getString("fullName");
                    String text3 = j1.getString("text");

                    if (StringUtils.isNotBlank(fullName)) {
                        if (text3.startsWith("<br/>")) {
                            text3 = StringUtils.removeFirst(text3, "<br/>");
                        }
                        tempMap.put(fullName, text3);
                    }
                }
                map2.put(title2, tempMap);
            }

            // 发文日期，格式如：2020-05-28
            String postingDate2 = jsonObject3.getString("posting_date")
                .replace("年", "-")
                .replace("月", "-")
                .replace("日", "");
            String s2 = x.get("旧法条数").toString();
            if (StringUtils.isBlank(s2)) {
                return;
            }
            String[] split = s2.split("；");
            for (String s3 : split) {
                String location2 = String.format("第%s条", arabicNumToChineseNum(Integer.parseInt(s3)));
                String text2 = location2 + "　" + map2.get(title2).get(location2);

                JSONObject jsonObject2 = new JSONObject()
                    .fluentPut("actionName", "    ")
                    .fluentPut("lid", lid2)
                    .fluentPut("postingDate", postingDate2) // 发文日期
                    .fluentPut("text", text2)
                    .fluentPut("title", title2);

                jsonArray.add(jsonObject2);
            }

            String value = jsonArray.toJSONString();
            RedisCache.setString("l:" + lid1 + ":" + location1 + ":h", value);
            System.out.println(value);
        });
        System.out.println();
    }

    /**
     * 清理"法条沿革"
     */
    @Test
    public void test20200602113022() {
        RedisCache.initialPool("redis_3");

        // 民法典lid
        String lid1 = "38c5d01eedd454cc67a12a22cfe4a84d";
        ExcelReader reader = ExcelUtil.getReader("/Users/caojing/Desktop/民法典与前法映射 3.xlsx");
        List<Map<String, Object>> readAll = reader.readAll();

        readAll.forEach(x -> {
            // 民法典
            int s1;
            try {
                s1 = ((Long) x.get("民法典条数")).intValue();
            } catch (Exception e) {
                return;
            }
            String location1 = String.format("第%s条", arabicNumToChineseNum(s1));
            RedisCache.del("l:" + lid1 + ":" + location1 + ":h");
        });
        System.out.println("清理完成");
    }
}
