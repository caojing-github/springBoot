package util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 文件工具类
 *
 * @author CaoJing
 * @date 2019/10/17 18:36
 */
@Slf4j
public final class FileKit {

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

    /**
     * 遍历文件夹 https://blog.csdn.net/EvansYN/article/details/81944662
     */
    public static void loop(File f1) {
        File[] list = f1.listFiles();
        for (File f2 : list) {
            if (f2.isDirectory()) {
                System.out.println(f2);
                loop(f2);
            } else {
                System.out.print(f2);
            }
        }
    }

    /**
     * 遍历文件夹下文件名称
     */
    @Test
    public void test20200410174933() {
        loop(new File("/Users/caojing/Downloads/归档"));
    }

    /**
     * 遍历读文件
     */
    @Test
    public void test20200410174431() {
        for (File f : FileUtil.ls("/Users/caojing/Downloads/归档/民事")) {
            FileReader fr = new FileReader(f);
            JSONObject jsonObject = JSON.parseObject(fr.readString());
            System.out.println();
        }
    }

    /**
     * 文件转换为byte[]
     * https://www.jianshu.com/p/b8b8f1ded401
     */
    @Test
    public void test20201104155050() throws IOException {
        String filePath = "/temp/abc.txt";
        // 方法一
//        byte[] bFile = Files.readAllBytes(new File(filePath).toPath());
        // 方法二
        byte[] bFile = Files.readAllBytes(Paths.get(filePath));
        System.out.println();
    }
}
