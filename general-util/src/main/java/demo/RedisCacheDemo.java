package demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import util.JdbcUtil;
import util.RedisCache;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat;

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
}
