package demo;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 文件处理示例 https://hutool.cn/docs/#/core/IO/%E6%96%87%E4%BB%B6%E5%B7%A5%E5%85%B7%E7%B1%BB-FileUtil
 *
 * @author CaoJing
 * @date 2019/12/19 17:47
 */
public class FileDemo {

    /**
     * 将文件读取为字符串
     */
    @Test
    public void test20191219174448() {
        FileReader reader = FileReader.create(new File("/Users/icourt/IdeaProjects/springBoot/token.txt"));
        String s = reader.readString();
        System.out.println();
    }

    @Test
    public void test20200417164622() {
        FileWriter writer = new FileWriter("/Users/caojing/Desktop/test.properties");
        writer.write("test");
    }

    /**
     * 提取后缀名
     */
    @Test
    public void test20200811175027() {
        String s = "6c63560ce24283be33ffe7d7320f4ebe.rar";
        String suffix = StrUtil.subAfter(s, ".", true);
        System.out.println(suffix);
    }

    /**
     * 提取文件名（包含文件后缀）
     */
    @Test
    public void test20200811172533() {
        String oldUrl = "http://www.pkulaw.cn/upload/pdf/lar/1247625/附件：执法单位报送抽查事项清单.rar";
        String fileName = StrUtil.subAfter(oldUrl, "/", true);
        System.out.println(fileName);
    }

    /**
     * 提取文件名
     */
    @Test
    public void test20200717103916() {
        String s = "/Users/caojing/Downloads/优案评析-案例/2213-云南3--行政--杨屹梅--李江会诉县人社局工伤保险待遇行政给付纠纷案.doc";
        System.out.println(ReUtil.get(".+/(.+)(\\..*)$", s, 1));
    }

    /**
     * 读取resource下文件
     */
    @Test
    public void test20201124142216() {
        List<String> list = IoUtil.readLines(ResourceUtil.getReader("失效法规.txt", Charset.defaultCharset()), Lists.newArrayList());
        System.out.println();
    }

    /**
     * 删除文件
     */
    @Test
    public void test20201201164936() {
        File file = new File("/Users/caojing/Documents/爬取时间2020_08_26_00_00_00_2020_08_27_00_00_00-未解析文书id.xlsx");
        file.delete();
    }
}
