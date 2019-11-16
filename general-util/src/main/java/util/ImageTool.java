package util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import java.io.IOException;

/**
 * 图片工具
 *
 * @author CaoJing
 * @date 2019/10/17 10:34
 */
@Slf4j
public class ImageTool {

    /**
     * 获得图片Base64字符串
     *
     * @author CaoJing
     * @date 2019/10/17 10:34
     */
    public static String getBase64ImageString(String filePath) throws IOException {
        return Base64.encodeBase64String(FileUtil.toByteArray(filePath));
    }

    /**
     * Base64字符串测试
     */
    @Test
    public void test20191107193928() throws IOException {
        String s = Base64.encodeBase64String(FileUtil.toByteArray("/Users/icourt/Desktop/背景.jpeg"));
        log.info(Base64.isBase64(s) + "");
        // 注意：下面的字符串不是Base64字符串
        log.info(Base64.isBase64("data:image/jpeg;base64," + s) + "");
    }

}
