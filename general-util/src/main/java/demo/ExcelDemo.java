package demo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.style.StyleUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.pivovarit.function.ThrowingSupplier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EntityUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;
import util.ESKit;
import util.HttpUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
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
@Slf4j
@SuppressWarnings("all")
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

    /**
     * 设置单元格颜色
     */
    @Test
    public void test20200603205848() {
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

        String path = "/users/caojing/Desktop/测试.xlsx";
        FileUtil.del(path);

        ArrayList<Map<String, Object>> rows = CollUtil.newArrayList(row1, row2);
        // 通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter(path);
        writer.setStyleSet(null);

        // 合并单元格后的标题行，使用默认标题样式
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows, true);

        // 行数
        int rowCount = writer.getRowCount();
        System.out.println("行数" + rowCount);

        // 设置某一单元格颜色
        {
            Font font = writer.createFont();
            font.setColor(IndexedColors.BLUE.index);

            CellStyle cellStyle = writer.getWorkbook().createCellStyle();
            cellStyle.setFont(font);
            writer.getCell(0, 2).setCellStyle(cellStyle);
        }
        // 设置行颜色
        {
            Font font = writer.createFont();
            font.setColor(IndexedColors.RED.index);

            CellStyle cellStyle = writer.getOrCreateRowStyle(1);
            cellStyle.setFont(font);

            // 设置第二行为红色
            writer.getOrCreateRow(1).cellIterator().forEachRemaining(x -> x.setCellStyle(cellStyle));
        }
        // 设置背景颜色
        {
            CellStyle cellStyle = writer.getOrCreateCellStyle(0, 1);
            StyleUtil.setColor(cellStyle, IndexedColors.YELLOW.getIndex(), FillPatternType.SQUARES);
        }
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
     * 律师事务所Suggest数据批量删除
     */
    @Test
    public void test20200902165757() throws IOException {
        // Excel读取-ExcelReader https://hutool.cn/docs/#/poi/Excel%E8%AF%BB%E5%8F%96-ExcelReader
        ExcelReader reader = ExcelUtil.getReader("/Users/caojing/Library/Containers/com.tencent.WeWorkMac/Data/Library/Application Support/WXWork/Data/1688851822093346/Cache/File/2020-09/需要删除的律师事务所Suggest数据.xlsx");
        List<Map<String, Object>> readAll = reader.readAll();

        BulkRequest request = new BulkRequest()
            .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

        readAll.forEach(x -> request.add(new DeleteRequest("suggest-dic_v11_lawfirm", "lawfirm", (String) x.get("id"))));

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

        int r = 0;
        List<Integer> color = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONArray vos = jsonArray.getJSONObject(i).getJSONArray("lawreguItemLegislationHistoryVos");
            if (vos == null) {
                continue;
            }
            for (int i1 = 1; i1 < vos.size(); i1++) {
                Map<String, Object> map = new HashMap<>();
                String v1 = "　　\n　　" + vos.getJSONObject(0).getString("text").replaceAll("<br/>", "\n　　");
                String v2 = "　　《" + vos.getJSONObject(i1).getString("title") + "》" + "\n　　" + vos.getJSONObject(i1).getString("text").replaceAll("<br/>", "\n　　");

                map.put("《民法典》", get(v1));
                map.put("前法相关规定", get(v2));

                // 是否相同
                boolean b = diff(
                    vos.getJSONObject(0).getString("text").replaceAll("<br/>", "").replaceAll("\n", ""),
                    vos.getJSONObject(i1).getString("text").replaceAll("<br/>", "").replaceAll("\n", "")
                );
                // 上色
                if (b) {
                    color.add(r);
                }
                rows.add(map);
                r++;
            }
        }
//        writer.disableDefaultStyle();
        writer.autoSizeColumnAll();
        //一次性写出内容，强制输出标题
        writer.write(rows, true);
        //关闭writer，释放内存
        writer.close();

        System.out.println();
    }

    /**
     * 判断"民法典"和其他法规的法条是否相同
     */
    public boolean diff(String text1, String text2) {
        return text1.split("　")[1].equals(text2.split("　")[1]);
    }

    /**
     * 去除a标签
     */
    public String get(String s) {
        return s.replaceAll("<a class=.*?>", "").replaceAll("</a>", "");
    }

    /**
     * 传统POI生成xlsx
     */
    @Test
    public void test20200603185826() throws IOException {
        //创建HSSFWorkbook对象(excel的文档对象)
        XSSFWorkbook wb = new XSSFWorkbook();
        //建立新的sheet对象（excel的表单）
        XSSFSheet sheet = wb.createSheet("成绩表");

        //在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
        XSSFRow row1 = sheet.createRow(0);
        //创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
        row1.createCell(0).setCellValue("姓名");
        row1.createCell(1).setCellValue("班级");
        row1.createCell(2).setCellValue("笔试成绩");
        row1.createCell(3).setCellValue("机试成绩");

        //在sheet里创建第二行
        XSSFRow row2 = sheet.createRow(1);
        //创建单元格并设置单元格内容
        row2.createCell(0).setCellValue("李明");
        row2.createCell(1).setCellValue("As17866666666666666666666666666666666");
        row2.createCell(2).setCellValue(87);
        row2.createCell(3).setCellValue(78);
        // 设置颜色
        row2.getCell(3).setCellStyle(getCellStyle(wb));

        {
            // 设置字体
            XSSFCellStyle style = wb.createCellStyle();
            XSSFFont font = wb.createFont();
            font.setColor((short) 18); // 参照 IndexedColors 类
            style.setFont(font);

            // 超链接
            Hyperlink hyperlink = wb.getCreationHelper().createHyperlink(HyperlinkType.URL);
            hyperlink.setAddress("https://www.google.com/");
            XSSFCell cell = row2.getCell(0);
            cell.setHyperlink(hyperlink);
            cell.setCellStyle(style);
        }

        // 获取sheet的总行数
        int r = sheet.getPhysicalNumberOfRows();
        // 获取该行的总列数
        int l = sheet.getRow(0).getPhysicalNumberOfCells();
        System.out.println(String.format("总行数:%s 总列数:%s", r, l));

        // 设置所有列为自适应宽度 https://blog.csdn.net/fenglingfeixian/article/details/64906400?utm_source=blogxgwz5
        for (int i = 0; i < l; i++) {
            sheet.autoSizeColumn(i);
        }
        FileOutputStream output = new FileOutputStream(new File("/Users/caojing/Desktop/测试.xlsx"));
        wb.write(output);
        output.flush();
    }

    /**
     * 得到不同颜色的style样式
     */
    public XSSFCellStyle getCellStyle(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        //注意这两个属性同时设置才能起作用
//        		style.setFillForegroundColor(color);// 设置背景色
//        		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        style.setFillForegroundColor(color);// 设置背景色
//        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

//        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
//        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
//        style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
//        style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
//		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中

        XSSFFont font = wb.createFont();
        // 默认颜色
//        font.setColor(XSSFColor.toXSSFColor(HSSFColor.HSSFColorPredefined.AUTOMATIC.getColor()));
        font.setColor((short) 22);
        style.setFont(font);
        return style;
    }

    @Test
    public void test20201113005446() throws IOException {
        //创建HSSFWorkbook对象(excel的文档对象)
        XSSFWorkbook wb = new XSSFWorkbook();
        //建立新的sheet对象（excel的表单）
        XSSFSheet sheet = wb.createSheet("超链接");

        //在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
        XSSFRow row1 = sheet.createRow(0);
        //创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
        row1.createCell(0).setCellValue("公司");
        row1.createCell(1).setCellValue("链接二");

        //在sheet里创建第二行
        XSSFRow row2 = sheet.createRow(1);
        row2.createCell(0).setCellValue("谷歌");

        //创建单元格并设置单元格内容
        XSSFCell a11 = row2.createCell(1);
        a11.setCellValue("李明");

        String url = "https://www.google.com/";
        a11.setCellStyle(getImagestyleCellType(wb));
        // 超链接，推荐这种配置。简洁
        a11.setCellFormula(String.format("HYPERLINK(\"%s\",\"%s\")", url, "网址"));

        // 获取sheet的总行数
        int r = sheet.getPhysicalNumberOfRows();
        // 获取该行的总列数
        int l = sheet.getRow(0).getPhysicalNumberOfCells();
        System.out.println(String.format("总行数:%s 总列数:%s", r, l));

        // 设置所有列为自适应宽度 https://blog.csdn.net/fenglingfeixian/article/details/64906400?utm_source=blogxgwz5
        for (int i = 0; i < l; i++) {
            sheet.autoSizeColumn(i);
        }
        FileOutputStream output = new FileOutputStream(new File("/Users/caojing/Desktop/超链接.xlsx"));
        wb.write(output);
        output.flush();
    }

    public static CellStyle getImagestyleCellType(XSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        // 自动换行
        style.setWrapText(true);

        Font font = wb.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
        font.setColor(IndexedColors.BLUE.index);
        font.setUnderline(Font.U_SINGLE);
        style.setFont(font);
        return style;
    }

    /**
     * 超链接，hutool实现，推荐这种写法，数据与样式解耦
     */
    @Test
    public void test20201112192819() {
        ZipSecureFile.setMinInflateRatio(-1.0d);    // fix:java.io.IOException: Zip bomb detected!
        ExcelReader reader = ExcelUtil.getReader("/Users/caojing/Documents/疑似时效性有问题.xlsx");
        List<Map<String, Object>> readAll = reader.readAll();
        readAll.forEach(x -> {
            String thirdId = Optional.ofNullable(x.get("thirdId"))
                .map(Object::toString)
                .orElse("");

            String detailUrl = "";
            if (thirdId.startsWith("A")) {
                detailUrl = "http://www.faxin.cn/lib/zyfl/zyflcontent.aspx?gid=" + thirdId;
            } else if (thirdId.startsWith("B")) {
                detailUrl = "http://www.faxin.cn/lib/dffl/dfflcontent.aspx?gid=" + thirdId;
            } else if (thirdId.endsWith("=")) {
                detailUrl = "https://law.wkinfo.com.cn/legislation/detail/" + thirdId;
            }
            x.put("detailUrl", detailUrl);
            x.remove("source");
        });
        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter("/Users/caojing/Documents/疑似时效性有问题v8.xlsx");

//        writer.autoSizeColumnAll();
        //一次性写出内容，强制输出标题
        writer.write(readAll, true);

        // 设置样式
        {
            Workbook wb = writer.getWorkbook();

            CellStyle cellStyle = writer.getWorkbook().createCellStyle();
            // 设置字体
            Font font = wb.createFont();
            font.setColor(IndexedColors.BLUE.index);
            // 下划线
            font.setUnderline(Font.U_SINGLE);
            // 字体
            font.setFontName("微软雅黑");
            // 加粗
            font.setBold(true);
            cellStyle.setFont(font);

            int rowCount = writer.getPhysicalRowCount();
            for (int i = 1; i < rowCount; i++) {
                // 第2列
                Cell cell1 = writer.getCell(1, i);
                // 超链接
                Hyperlink hyperlink1 = wb.getCreationHelper().createHyperlink(HyperlinkType.URL);
                hyperlink1.setAddress(cell1.getStringCellValue());

                cell1.setHyperlink(hyperlink1);
                cell1.setCellStyle(cellStyle);

                // 第9列
                Cell cell2 = writer.getCell(8, i);
                Hyperlink hyperlink2 = wb.getCreationHelper().createHyperlink(HyperlinkType.URL);
                hyperlink2.setAddress(cell2.getStringCellValue());

                cell2.setHyperlink(hyperlink2);
                cell2.setCellStyle(cellStyle);
            }
        }
        //关闭writer，释放内存
        writer.close();
    }

    /**
     * 超链接，使用原始poi实现，不推荐。数据与样式没有解耦
     */
    @Test
    public void test20201113012740() {
//        // 创建HSSFWorkbook对象(excel的文档对象)
//        XSSFWorkbook wb = new XSSFWorkbook();
//        // 建立新的sheet对象（excel的表单）
//        XSSFSheet sheet = wb.createSheet();
//
//        // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
//        XSSFRow row1 = sheet.createRow(0);
//        //创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
//        row1.createCell(0).setCellValue("lid");
//        row1.createCell(1).setCellValue("链接");
//        row1.createCell(2).setCellValue("标题");
//        row1.createCell(3).setCellValue("source");
//        row1.createCell(4).setCellValue("效力级别");
//        row1.createCell(5).setCellValue("thirdId");
//        row1.createCell(6).setCellValue("hbase时效性");
//        row1.createCell(7).setCellValue("es时效性");
//        row1.createCell(8).setCellValue("时效依据");
//
//        AtomicInteger r = new AtomicInteger();
//
//        String dsl = "{\"term\":{\"time_limited\":\"已被修改\"}}";
//        Consumer<List<LawEntity>> consumer = x -> {
//            List<Get> gets = x.stream().map(y ->
//                new Get(y.getLid().getBytes())
//                    .addColumn("content".getBytes(), "ext".getBytes())
//                    .addColumn("content".getBytes(), "source".getBytes())
//            ).collect(Collectors.toList());
//
//            List<LawEntity> list = hBaseRepository.multiGet(gets);
//            list.stream()
//                .filter(y -> TimeLimitedEnum.失效.name().equals(TimeLimitedNormalizer.normalize(y.getTimeLimited())))
//                .forEach(z -> {
//                    r.incrementAndGet();
//                    XSSFRow row2 = sheet.createRow(r.get());
//                    //创建单元格并设置单元格内容
//                    row2.createCell(0).setCellValue(z.getLid());
//                    {
//                        XSSFCell cell = row2.createCell(1);
//                        String url = getDetailUrl(z.getLid());
//                        cell.setCellValue(url);
//
//                        // 设置字体
//                        XSSFCellStyle style = wb.createCellStyle();
//                        XSSFFont font = wb.createFont();
//                        font.setColor(IndexedColors.BLUE.index);
//                        style.setFont(font);
//
//                        // 超链接
//                        Hyperlink hyperlink = wb.getCreationHelper().createHyperlink(HyperlinkType.URL);
//                        hyperlink.setAddress(url);
//                        cell.setHyperlink(hyperlink);
//                        cell.setCellStyle(style);
//                    }
//                    row2.createCell(2).setCellValue(z.getTitle());
//                    row2.createCell(3).setCellValue(z.getDsSource());
//                    row2.createCell(4).setCellValue(z.getEffLevel());
//                    row2.createCell(5).setCellValue(z.getThirdId());
//                    row2.createCell(6).setCellValue("失效");
//                    row2.createCell(7).setCellValue("已被修改");
//
//                    // 时效依据
//                    String dsl2 = String.format("{\"_source\":[\"lid\"],\"query\":{\"nested\":{\"path\":\"timeLimitedBasis\",\"query\":{\"term\":{\"timeLimitedBasis.lid\":\"%s\"}}}}}", z.getLid());
//                    List<JSONObject> list2 = esRepository.getByDSL(dsl2)
//                        .stream()
//                        .map(a -> {
//                            JSONObject j = new JSONObject();
//                            j.put("lid", a.getLid());
//                            j.put("url", getDetailUrl(a.getLid()));
//                            return j;
//                        }).collect(Collectors.toList());
//
//                    row2.createCell(8).setCellValue(JSON.toJSONString(list2));
//                });
//        };
//        esRepository.scroll(dsl, new String[]{"lid"}, consumer);
//
//        String path = EXPORT_PATH_PREFIX + "疑似时效性有问题.xlsx";
//        FileOutputStream output = new FileOutputStream(new File(path));
//        wb.write(output);
//        output.flush();
//        log.info(path);
    }

    /**
     * 民法典上色
     */
    @Test
    public void test20200603202332() throws Exception {
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

        String url = "https://alphalawyer.cn/ilawregu-search/api/v1/lawregu/38c5d01eedd454cc67a12a22cfe4a84d?format=true&query=";
        Map<String, String> headers = Maps.newHashMap();
        headers.put("token", "eyJhbGciOiJIUzI1NiJ9.eyJvZmZpY2VfaWQiOiI0ZDc5MmUzMTZhMDUxMWU2YWE3NjAwMTYzZTE2MmFkZCIsImRldmljZVR5cGUiOiJ3ZWIiLCJvZmZpY2VfbmFtZSI6ImlDb3VydCIsInVzZXJfdHlwZSI6IkEiLCJ1c2VyX2lkIjoiQzdDN0RFRkYwRTMyMTFFOUIzQzc3Q0QzMEFEM0FCMDYiLCJsb2dpblR5cGUiOiIxIiwidXNlcl9uYW1lIjoi5pu56Z2WIiwiaXNzIjoiaUxhdy5jb20iLCJleHAiOjE1OTMzMTE2NTM5NjQsImlhdCI6MTU5MDcxOTY1Mzk2NCwib2ZmaWNlVHlwZSI6ImludGVncmF0aW9uIn0.84l-tCNs0TpAa5bsEXVxqDbS3llw6FNdqg1fwZm5sVk");

        JSONArray jsonArray = JSON.parseObject(EntityUtils.toString(HttpUtils.doGet(url, "", headers, Maps.newHashMap()).getEntity()), JSONObject.class)
            .getJSONObject("data")
            .getJSONArray("law_regulation_introductions");

        //创建HSSFWorkbook对象(excel的文档对象)
        XSSFWorkbook wb = new XSSFWorkbook();
        //建立新的sheet对象（excel的表单）
        XSSFSheet sheet = wb.createSheet();

        //在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
        XSSFRow row1 = sheet.createRow(0);
        //创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
        row1.createCell(0).setCellValue("《民法典》");
        row1.createCell(1).setCellValue("前法相关规定");

        int r = 0;
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONArray vos = jsonArray.getJSONObject(i).getJSONArray("lawreguItemLegislationHistoryVos");
            if (vos == null) {
                continue;
            }
            String fullName = jsonArray.getJSONObject(i).getString("fullName");
            if (vos.isEmpty()) {
                r++;
                String v1 = "　　\n　　" + fullName + "　" + map.get(fullName).replaceAll("<br/>", "\n　　");
                XSSFRow row2 = sheet.createRow(r);
                //创建单元格并设置单元格内容
                row2.createCell(0).setCellValue(get(v1));
                continue;
            }
            for (int i1 = 1; i1 < vos.size(); i1++) {
                r++;
                String v1 = "　　\n　　" + vos.getJSONObject(0).getString("text").replaceAll("<br/>", "\n　　");
                String v2 = "　　《" + vos.getJSONObject(i1).getString("title") + "》" + "\n　　" + vos.getJSONObject(i1).getString("text").replaceAll("<br/>", "\n　　");

                XSSFRow row2 = sheet.createRow(r);
                //创建单元格并设置单元格内容
                row2.createCell(0).setCellValue(get(v1));
                row2.createCell(1).setCellValue(get(v2));

                // 是否相同
                boolean b = diff(
                    vos.getJSONObject(0).getString("text").replaceAll("<br/>", "").replaceAll("\n", ""),
                    vos.getJSONObject(i1).getString("text").replaceAll("<br/>", "").replaceAll("\n", "")
                );
                // 上色
                if (b) {
                    row2.getCell(0).setCellStyle(getCellStyle(wb));
                    row2.getCell(1).setCellStyle(getCellStyle(wb));
                }
            }
        }
        FileOutputStream output = new FileOutputStream(new File("/Users/caojing/Desktop/民法典对照.xlsx"));
        wb.write(output);
        output.flush();
    }

    /**
     * 处理宁夏辅德数据
     */
    @Test
    public void test20200629115900() {
        ExcelReader reader = ExcelUtil.getReader("/Users/caojing/Desktop/副本 宁夏辅德.xlsx");
        List<Map<String, Object>> readAll = reader.readAll();

        List<Map<String, Object>> rows = readAll
            .stream()
            .map(x -> {
                Map<String, Object> map = new LinkedHashMap<>();
                String lid = x.get("lid").toString();

                // 尽量还是使用dsl解析
                FetchSourceContext sourceContext = new FetchSourceContext(true, new String[]{"title", "eff_level", "dispatch_authority", "posting_date_str"}, new String[]{});
                GetRequest request = new GetRequest("law_upsert", "law_regu", lid)
                    .fetchSourceContext(sourceContext);

                GetResponse response;
                try {
                    response = ESKit.ES.PRO.client.get(request, RequestOptions.DEFAULT);
                } catch (IOException e) {
                    log.error("", e);
                    return map;
                }
                JSONObject jsonObject = JSON.parseObject(response.getSourceAsString());
                if (jsonObject == null) {
                    log.error("lid:{} title为空", lid);
                    return map;
                }
                map.put("lid", lid);
                map.put("标题", jsonObject.getString("title"));
                map.put("效力级别", jsonObject.getString("eff_level"));
                map.put("发文机关", jsonObject.getString("dispatch_authority"));
                map.put("发文日期", jsonObject.getString("posting_date_str"));
                map.put("a", x.get("a").toString());
                return map;

            }).filter(MapUtils::isNotEmpty)
            .collect(Collectors.toList());

        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter("/users/caojing/Desktop/宁夏辅德-处理后.xlsx");
        // 列自适应
        writer.autoSizeColumnAll();
        //一次性写出内容，强制输出标题
        writer.write(rows, true);
        //关闭writer，释放内存
        writer.close();
    }

    /**
     * 通过excel解析法规
     *
     * @author CaoJing
     * @date 2020/07/03 16:28:52
     */
    @Test
    public void parseByXlsx() {
        URL url = ExcelDemo.class.getClassLoader().getResource("parse.xlsx");
        ExcelReader reader = ExcelUtil.getReader(new File(url.getPath()));
        List<Map<String, Object>> readAll = reader.readAll();
        readAll.forEach(x -> {
            String lid = x.get("by_field_0").toString();
            log.info("已解析lid:{}", lid);
        });
        log.info("通过excel解析完成");
    }

    /**
     * 津姐的需求
     */
    @Test
    public void test20200729122210() {
        ExcelReader reader = ExcelUtil.getReader("/Users/caojing/Desktop/副本类案规则案例案号.xlsx");
        List<Map<String, Object>> rows = reader.readAll();
        rows.forEach(x -> {
            if (x.get("案号") == null) {
                return;
            }
            String s = x.get("案号").toString();
            String dsl = String.format("{\"_source\":[\"jid\",\"all_caseinfo_casename\"],\"query\":{\"bool\":{\"must\":[{\"match_phrase\":{\"all_caseinfo_casenumber\":\"%s\"}},{\"term\":{\"publish_type\":7}}]}}}", s);

            JSONObject jsonObject = ThrowingSupplier.sneaky(() -> ESKit.getByDSL(ESKit.ES.PRO, "judgement_1015", "judgement", dsl)).get();
            JSONArray jsonArray = jsonObject.getJSONObject("hits").getJSONArray("hits");

            if (!jsonArray.isEmpty()) {
                JSONObject source = jsonArray.getJSONObject(0).getJSONObject("_source");
                x.put("优案评析标题", source.getString("all_caseinfo_casename"));
                x.put("优案评析url", getUrl(source.getString("jid")));
                return;
            }
            log.warn("{} 没找到对应优案评析", s);
        });

        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter("/users/caojing/Desktop/副本类案规则案例案号-处理后.xlsx");
        writer.setStyleSet(null);
        // 列自适应
        writer.autoSizeColumnAll();
        //一次性写出内容，强制输出标题
        writer.write(rows, true);
        //关闭writer，释放内存
        writer.close();
    }

    public String getUrl(String jid) {
        return String.format("https://alphalawyer.cn/#/app/tool/excellentCase/detail/%s", jid);
    }
}
