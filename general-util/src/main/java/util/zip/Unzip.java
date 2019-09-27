package util.zip;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import lombok.extern.slf4j.Slf4j;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 解压工具
 * <p>
 * 相关文章 @link https://cloud.tencent.com/developer/article/1130020
 *
 * @author CaoJing
 * @date 2019/09/27 12:46
 */
@Slf4j
@SuppressWarnings("all")
public final class Unzip {

    public static void main(String[] args) throws IOException {
        unZip("/Users/icourt/IdeaProjects/springBoot/general-util/src/main/resources/zip/安徽蚌埠企业名录4843.zip",
            "/Users/icourt/IdeaProjects/springBoot/general-util/src/main/resources/unzip");

        unRar("/Users/icourt/IdeaProjects/springBoot/general-util/src/main/resources/zip/children/北京昌平区10535.rar",
            "/Users/icourt/IdeaProjects/springBoot/general-util/src/main/resources/unzip");


        String sourceDir = "/Users/icourt/Downloads/全国企业名录";
        String outputDir = "/Users/icourt/Downloads/unzip";

        case1(sourceDir, outputDir);
    }

    /**
     * 递归解压例子
     */
    private static void case1(String sourceDir, String outputDir) throws IOException {

        File file = new File(sourceDir);
        List<File> subFileList = new ArrayList<>();

        listFiles(file, subFileList);

        for (File subFile : subFileList) {

            String fileName = subFile.getName();
            String fileType = fileName.substring(fileName.lastIndexOf('.'));
            String fileSource = subFile.getAbsolutePath();

            switch (fileType) {
//                case ".zip":
//                    unZip(fileSource, outputDir);
//                    break;
                case ".rar":
                    unRar(fileSource, outputDir);
                    break;
                default:
//                    log.debug(fileSource + ": 此文件无法解压");
            }
        }
    }

    /**
     * 循环出文件夹下的所有文件
     *
     * @param file        文件夹
     * @param subFileList 文件夹下子文件列表
     */
    public static void listFiles(File file, List<File> subFileList) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                // 若是目录，则递归该目录下的文件
                if (f.isDirectory()) {
                    listFiles(f, subFileList);
                } else if (f.isFile()) {
                    // 若是文件则保存
                    subFileList.add(f);
                }
            }
        }
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

                // 如果是文件夹就创建个文件夹
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

    //解压.zip文件
    public static void unZip2(String sourceFile, String outputDir) throws IOException {
        ZipFile zipFile = null;
        File file = new File(sourceFile);
        try {
//            Charset CP866 = Charset.forName("CP866");  //specifying alternative (non UTF-8) charset
            zipFile = new ZipFile(file, Charset.forName("GBK"));
            createDirectory(outputDir, null);//创建输出目录

            Enumeration enums = zipFile.entries();
            while (enums.hasMoreElements()) {

                ZipEntry entry = (ZipEntry) enums.nextElement();
                log.info("解压." + entry.getName());

                //是目录
                if (entry.isDirectory()) {
                    //创建空目录
                    createDirectory(outputDir, entry.getName());
                } else {
                    //是文件
                    File tmpFile = new File(outputDir + "/" + entry.getName());
                    createDirectory(tmpFile.getParent() + "/", null);//创建输出目录

                    InputStream in = null;
                    OutputStream out = null;
                    try {
                        in = zipFile.getInputStream(entry);
                        out = new FileOutputStream(tmpFile);
                        int length = 0;

                        byte[] b = new byte[2048];
                        while ((length = in.read(b)) != -1) {
                            out.write(b, 0, length);
                        }

                    } catch (IOException ex) {
                        throw ex;
                    } finally {
                        if (in != null) {
                            in.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new IOException("解压缩文件出现异常", e);
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (IOException ex) {
                throw new IOException("关闭zipFile出现异常", ex);
            }
        }
    }

    /**
     * 单个rar文件解压
     *
     * @param srcFilePath 单个zip文件zip源文件路径
     * @param destDirPath 解压后的目标文件夹
     * @throws RuntimeException 解压失败会抛出运行时异常
     */
    public static void unRar(String srcFilePath, String destDirPath) throws RuntimeException {

        File file = new File(srcFilePath);

        try (Archive archive = new Archive(file)) {
            FileHeader fileHeader = archive.nextFileHeader();

            int count = 0;
            while (fileHeader != null) {

                // 判断编码，解决中文乱码的问题
                String compressFileName = fileHeader.isUnicode() ? fileHeader.getFileNameW().trim() : fileHeader.getFileNameString().trim();

//                log.info((++count) + ") " + compressFileName);

                File destFileName = new File(destDirPath + "/" + compressFileName);

                if (fileHeader.isDirectory()) {
                    if (!destFileName.exists()) {
                        destFileName.mkdirs();
                    }
                    fileHeader = archive.nextFileHeader();
                    continue;
                }

                if (!destFileName.getParentFile().exists()) {
                    destFileName.getParentFile().mkdirs();
                }

                // TODO 表示解压后的文件人仍旧可以解压
                if (destFileName.getPath().contains("rar")) {
//                    System.out.println(destFileName);
                }

                if (!destFileName.getPath().contains("xls") && !destFileName.getPath().contains("rar")) {
                    try (FileOutputStream fos = new FileOutputStream(destFileName)) {
                        archive.extractFile(fileHeader, fos);
                    } catch (Exception e) {
                        log.error("File:" + file + " 中 " + compressFileName + "文件解压失败", e);
                    }
                }

                fileHeader = archive.nextFileHeader();
            }

        } catch (Exception e) {
            throw new RuntimeException("File:" + file + ":文件解压失败", e);
        }
    }


    //解压.gz文件
    public static void unGz(String sourceFile, String outputDir) {
        String ouputfile = "";
        try {
            //建立gzip压缩文件输入流
            FileInputStream fin = new FileInputStream(sourceFile);
            //建立gzip解压工作流
            GZIPInputStream gzin = new GZIPInputStream(fin);
            //建立解压文件输出流
            /*ouputfile = sourceFile.substring(0,sourceFile.lastIndexOf('.'));
            ouputfile = ouputfile.substring(0,ouputfile.lastIndexOf('.'));*/
            File file = new File(sourceFile);
            String fileName = file.getName();
            outputDir = outputDir + "/" + fileName.substring(0, fileName.lastIndexOf('.'));
            FileOutputStream fout = new FileOutputStream(outputDir);

            int num;
            byte[] buf = new byte[1024];

            while ((num = gzin.read(buf, 0, buf.length)) != -1) {
                fout.write(buf, 0, num);
            }

            gzin.close();
            fout.close();
            fin.close();
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        return;
    }


    //解压.tar.gz文件
    public static void unTarGz(String sourceFile, String outputDir) throws IOException {
        TarInputStream tarIn = null;
        File file = new File(sourceFile);
        try {
            tarIn = new TarInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(file))), 2);

            createDirectory(outputDir, null);//创建输出目录

            TarEntry entry = null;
            while ((entry = tarIn.getNextEntry()) != null) {

                if (entry.isDirectory()) {//是目录
                    entry.getName();
                    createDirectory(outputDir, entry.getName());//创建空目录
                } else {//是文件
                    File tmpFile = new File(outputDir + "/" + entry.getName());
                    createDirectory(tmpFile.getParent() + "/", null);//创建输出目录
                    OutputStream out = null;
                    try {
                        out = new FileOutputStream(tmpFile);
                        int length = 0;

                        byte[] b = new byte[2048];

                        while ((length = tarIn.read(b)) != -1) {
                            out.write(b, 0, length);
                        }

                    } catch (IOException ex) {
                        throw ex;
                    } finally {
                        if (out != null) {
                            out.close();
                        }
                    }
                }
            }
        } catch (IOException ex) {
            throw new IOException("解压归档文件出现异常", ex);
        } finally {
            try {
                if (tarIn != null) {
                    tarIn.close();
                }
            } catch (IOException ex) {
                throw new IOException("关闭tarFile出现异常", ex);
            }
        }
    }

    /**
     * 构建目录
     *
     * @param outputDir
     * @param subDir
     */
    public static void createDirectory(String outputDir, String subDir) {

        File file = new File(outputDir);

        //子目录不为空
        if (!(subDir == null || subDir.trim().equals(""))) {
            file = new File(outputDir + "/" + subDir);
        }

        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.mkdirs();
        }
    }
}
