package demo.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
}
