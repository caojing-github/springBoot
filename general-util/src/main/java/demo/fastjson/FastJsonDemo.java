package demo.fastjson;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 示例
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
        FileInputStream fis = new FileInputStream(new File("/Users/icourt/IdeaProjects/springBoot/token.txt"));
        List<Map> o = JSON.parseObject(fis, List.class);
        System.out.println();
    }
}
