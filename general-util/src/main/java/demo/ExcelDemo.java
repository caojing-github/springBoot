package demo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.junit.Test;
import util.ESKit;
import util.HttpUtils;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static util.HttpUtils.doGet;

/**
 * Excel
 * https://hutool.cn/docs/#/poi/Excel%E5%B7%A5%E5%85%B7-ExcelUtil
 *
 * @author CaoJing
 * @date 2020/03/30 01:15
 */
public class ExcelDemo {

    /**
     * 将行列对象写出到Excel
     */
    @Test
    public void test20200330011559() {
        List<String> row1 = CollUtil.newArrayList("aa", "bb", "cc", "dd");
        List<String> row2 = CollUtil.newArrayList("aa1", "bb1", "cc1", "dd1");
        List<String> row3 = CollUtil.newArrayList("aa2", "bb2", "cc2", "dd2");
        List<String> row4 = CollUtil.newArrayList("aa3", "bb3", "cc3", "dd3");
        List<String> row5 = CollUtil.newArrayList("aa4", "bb4", "cc4", "dd4");

        List<List<String>> rows = CollUtil.newArrayList(row1, row2, row3, row4, row5);

        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter("/users/caojing/Desktop/测试.xlsx");

        //跳过当前行，既第一行，非必须，在此演示用
        writer.passCurrentRow();

        //合并单元格后的标题行，使用默认标题样式
        writer.merge(row1.size() - 1, "测试标题");
        //一次性写出内容，强制输出标题
        writer.write(rows, true);
        //关闭writer，释放内存
        writer.close();
    }

    /**
     * 写出Map数据
     */
    @Test
    public void test20200330012924() {
        Map<String, Object> row1 = new LinkedHashMap<>();
        row1.put("姓名", "张三");
        row1.put("年龄", 23);
        row1.put("成绩", 88.32);
        row1.put("是否合格", true);
        row1.put("考试日期", DateUtil.date());

        Map<String, Object> row2 = new LinkedHashMap<>();
        row2.put("姓名", "李四");
        row2.put("年龄", 33);
        row2.put("成绩", 59.50);
        row2.put("是否合格", false);
        row2.put("考试日期", DateUtil.date());

        ArrayList<Map<String, Object>> rows = CollUtil.newArrayList(row1, row2);

        // 通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter("/users/caojing/Desktop/测试.xlsx");
        // 合并单元格后的标题行，使用默认标题样式
        writer.merge(row1.size() - 1, "一班成绩单");
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows, true);
        // 关闭writer，释放内存
        writer.close();
    }

    ///////////////////////////////////////////////////////////////////////////
    // 👆是基本的Excel操作示例，👇是具体导的数据
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 导出法院数据
     */
    @Test
    public void test20200401110117() throws Exception {
        int size = 0;
        List<Map<String, Object>> rows = new ArrayList<>();
        for (int i = 1; i < 1024 && size < 3621; i++) {
            JSONObject jsonObject = doGet("https://yjs.alphalawyer.cn/api/v1/courtstat/search?pageSize=100&pageIndex=" + i);
            JSONArray courtinfoList = jsonObject.getJSONObject("data").getJSONArray("courtinfoList");
            for (int j = 0; j < courtinfoList.size(); j++) {
                JSONObject map = courtinfoList.getJSONObject(j);
                rows.add(map);
            }
            size = rows.size();
        }
        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter("/users/caojing/Desktop/最新版法院数据.xlsx");
        //一次性写出内容，强制输出标题
        writer.write(rows, true);
        //关闭writer，释放内存
        writer.close();
    }

    /**
     * 法院Suggest数据
     */
    @Test
    public void test20200409151019() {
        List<Map<String, Object>> rows = new ArrayList<>();
        Consumer<List<JSONObject>> consumer = rows::addAll;
        ESKit.scroll("suggest_dic_v3_court", "court", "{\"match_all\":{}}", null, consumer);

        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter("/users/caojing/Desktop/法院Suggest数据.xlsx");
        //一次性写出内容，强制输出标题
        writer.write(rows, true);
        //关闭writer，释放内存
        writer.close();
    }

    /**
     * 律师事务所Suggest数据
     */
    @Test
    public void test20200409152916() {
        List<Map<String, Object>> rows = new ArrayList<>();
        Consumer<List<JSONObject>> consumer = rows::addAll;
        ESKit.scroll("suggest-dic_v11_lawfirm", "lawfirm", "{\"match_all\":{}}", null, consumer);

        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter("/users/caojing/Desktop/律师事务所Suggest数据.xlsx");
        //一次性写出内容，强制输出标题
        writer.write(rows, true);
        //关闭writer，释放内存
        writer.close();
    }

    /**
     * 法官Suggest数据
     */
    @Test
    public void test20200409160215() {
        List<Map<String, Object>> rows = new ArrayList<>();
        Consumer<List<JSONObject>> consumer = rows::addAll;
        ESKit.scroll("suggest-dic_v11_judge_court", "judgeCourt", "{\"match_all\":{}}", null, consumer);

        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter("/users/caojing/Desktop/法官Suggest数据.xlsx");
        //一次性写出内容，强制输出标题
        writer.write(rows, true);
        //关闭writer，释放内存
        writer.close();
    }

    /**
     * 法官Suggest数据批量删除
     */
    @Test
    public void test20200409170135() throws IOException {
        // Excel读取-ExcelReader https://hutool.cn/docs/#/poi/Excel%E8%AF%BB%E5%8F%96-ExcelReader
        ExcelReader reader = ExcelUtil.getReader("/Users/caojing/Desktop/需要删除的法院suggest.xlsx");
        List<Map<String, Object>> readAll = reader.readAll();

        BulkRequest request = new BulkRequest()
            .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

        readAll.forEach(x -> request.add(new DeleteRequest("suggest-dic_v11_judge_court", "judgeCourt", (String) x.get("id"))));

        BulkResponse bulkResponse = ESKit.ES.PRO.client.bulk(request, RequestOptions.DEFAULT);
    }

    /**
     * 需要跑法规关联的法规lid
     */
    @Test
    public void test20200506143252() throws IOException {
        System.out.println("https://alphalawyer.cn/#/app/tool/lawsResult/%7B%5B%5D,%7D/detail/%7B".length());
        System.out.println("2f11b97a5136e961d97930912fa63656".length());

        // Excel读取-ExcelReader https://hutool.cn/docs/#/poi/Excel%E8%AF%BB%E5%8F%96-ExcelReader
        ExcelReader reader = ExcelUtil.getReader("/Users/caojing/Desktop/常用法律.xlsx");
        List<Map<String, Object>> readAll = reader.readAll();

        FileWriter writer = new FileWriter("/Users/caojing/Desktop/需要跑的法规关联.txt");

        List<String> lines = readAll.stream()
            .map(x -> x.get("2").toString().substring(69, 101))
            .collect(Collectors.toList());

        // 第1种写法
        writer.writeLines(lines);

        // 第2种写法
//        readAll.forEach(x -> {
//            String line = x.get("2").toString().substring(69, 101) + "\r\n";
////            writer.write(line, true);
//        });
    }

    @Test
    public void test20200528172716() {
        ExcelReader reader = ExcelUtil.getReader("/Users/caojing/Desktop/公报案例-再审.xlsx");
        List<Map<String, Object>> readAll = reader.readAll();
        System.out.println();
    }

    /**
     * 民法典对照数据
     */
    @Test
    public void test20200602203317() throws Exception {
        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter("/users/caojing/Desktop/民法典对照数据.xlsx");
        List<Map<String, Object>> rows = new ArrayList<>();

        String url = "https://alphalawyer.cn/ilawregu-search/api/v1/lawregu/38c5d01eedd454cc67a12a22cfe4a84d?format=true&query=";
        Map<String, String> headers = Maps.newHashMap();
        headers.put("token", "eyJhbGciOiJIUzI1NiJ9.eyJvZmZpY2VfaWQiOiI0ZDc5MmUzMTZhMDUxMWU2YWE3NjAwMTYzZTE2MmFkZCIsImRldmljZVR5cGUiOiJ3ZWIiLCJvZmZpY2VfbmFtZSI6ImlDb3VydCIsInVzZXJfdHlwZSI6IkEiLCJ1c2VyX2lkIjoiQzdDN0RFRkYwRTMyMTFFOUIzQzc3Q0QzMEFEM0FCMDYiLCJsb2dpblR5cGUiOiIxIiwidXNlcl9uYW1lIjoi5pu56Z2WIiwiaXNzIjoiaUxhdy5jb20iLCJleHAiOjE1OTMzMTE2NTM5NjQsImlhdCI6MTU5MDcxOTY1Mzk2NCwib2ZmaWNlVHlwZSI6ImludGVncmF0aW9uIn0.84l-tCNs0TpAa5bsEXVxqDbS3llw6FNdqg1fwZm5sVk");

        JSONArray jsonArray = JSON.parseObject(EntityUtils.toString(HttpUtils.doGet(url, "", headers, Maps.newHashMap()).getEntity()), JSONObject.class)
            .getJSONObject("data")
            .getJSONArray("law_regulation_introductions");

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONArray vos = jsonArray.getJSONObject(i).getJSONArray("lawreguItemLegislationHistoryVos");
            if (vos == null) {
                continue;
            }
            for (int i1 = 1; i1 < vos.size(); i1++) {
                Map<String, Object> map = new HashMap<>();
                map.put("《民法典》", vos.getJSONObject(0).getString("text"));
                map.put("前法相关规定", vos.getJSONObject(i1).getString("title") + "\n" + vos.getJSONObject(i1).getString("text"));
                rows.add(map);
            }
        }
        //一次性写出内容，强制输出标题
        writer.write(rows, true);
        //关闭writer，释放内存
        writer.close();

        System.out.println();
    }
}
