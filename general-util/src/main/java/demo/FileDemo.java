package demo;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import org.junit.Test;

import java.io.File;

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
}
