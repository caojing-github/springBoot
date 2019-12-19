package demo;

import cn.hutool.core.io.file.FileReader;
import org.junit.Test;

import java.io.File;

/**
 * 文件处理示例
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
}
