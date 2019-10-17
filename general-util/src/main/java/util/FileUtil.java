package util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件工具类
 *
 * @author CaoJing
 * @date 2019/10/17 18:36
 */
@Slf4j
public final class FileUtil {

    /**
     * 将文件转换为byte[]
     *
     * @param filePath 文件路径
     * @return byte[]
     * @author CaoJing
     * @date 2019/10/17 18:36
     */
    public static byte[] toByteArray(String filePath) throws IOException {
        try (FileChannel fc = new RandomAccessFile(filePath, "r").getChannel()) {
            MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()).load();

            log.info(byteBuffer.isLoaded() + "");

            byte[] result = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0) {
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            return result;
        }
    }
}
