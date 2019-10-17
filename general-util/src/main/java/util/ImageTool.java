package util;

import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

/**
 * 图片工具
 *
 * @author CaoJing
 * @date 2019/10/17 10:34
 */
public class ImageTool {

    /**
     * 获得图片Base64字符串
     *
     * @author CaoJing
     * @date 2019/10/17 10:34
     */
    public static String getBase64ImageString(File file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file);
        byte[] bytes = ((DataBufferByte) bufferedImage.getData().getDataBuffer()).getData();
        // 图片以base64编码的String base64Image
        return Base64.encodeBase64String(bytes);
    }

}
