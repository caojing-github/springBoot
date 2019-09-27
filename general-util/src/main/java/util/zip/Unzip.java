package util.zip;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 解压Zip工具
 *
 * @author CaoJing
 * @date 2019/09/27 12:46
 */
@Slf4j
public final class Unzip {

    public static void main(String[] args) {
        unZip("/Users/icourt/IdeaProjects/springBoot/general-util/src/main/resources/zip/安徽蚌埠企业名录4843.zip",
            "/Users/icourt/IdeaProjects/springBoot/general-util/src/main/resources/unzip");
    }

    /**
     * 单个zip文件解压
     *
     * @param srcFilePath 单个zip文件zip源文件路径
     * @param destDirPath 解压后的目标文件夹
     * @throws RuntimeException 解压失败会抛出运行时异常
     */
    public static void unZip(String srcFilePath, String destDirPath) throws RuntimeException {

        long start = System.currentTimeMillis();

        File srcFile = new File(srcFilePath);

        // 判断zip源文件文件是否存在
        if (!srcFile.exists()) {
            throw new RuntimeException(srcFile.getPath() + "zip源文件文件不存在");
        }

        // 开始解压
        try (ZipFile zipFile = new ZipFile(srcFile, Charset.forName("GBK"))) {

            Enumeration entries = zipFile.entries();
            while (entries.hasMoreElements()) {

                ZipEntry entry = (ZipEntry) entries.nextElement();
                log.info("解压" + entry.getName());

                // 如果是文件夹，就创建个文件夹
                if (entry.isDirectory()) {
                    String dirPath = destDirPath + "/" + entry.getName();
                    File dir = new File(dirPath);
                    dir.mkdirs();

                } else {

                    // 如果是文件，就先创建一个文件，然后用 IO流 把内容copy过去
                    File targetFile = new File(destDirPath + "/" + entry.getName());

                    // 保证这个文件的父文件夹必须要存在
                    if (!targetFile.getParentFile().exists()) {
                        targetFile.getParentFile().mkdirs();
                    }

                    // 当且仅当具有该名称的文件尚不存在时，原子地创建一个由该抽象路径名命名的新的空文件
                    targetFile.createNewFile();

                    // 将压缩文件内容写入到这个文件中
                    InputStream in = zipFile.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(targetFile);

                    int len;
                    byte[] buf = new byte[1024];
                    while ((len = in.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }

                    // 关流顺序，先打开的后关闭
                    fos.close();
                    in.close();
                }
            }
            long end = System.currentTimeMillis();
            log.info("解压完成，耗时：" + (end - start) + " ms");

        } catch (Exception e) {
            throw new RuntimeException("文件不能解压", e);
        }
    }
}
