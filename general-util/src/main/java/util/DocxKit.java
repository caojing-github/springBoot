package util;

import com.aspose.words.Document;
import com.aspose.words.ExportHeadersFootersMode;
import com.aspose.words.HtmlSaveOptions;
import com.aspose.words.License;
import com.pivovarit.function.ThrowingRunnable;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * word处理工具
 *
 * @author CaoJing
 * @date 2020/05/25 14:11
 */
public final class DocxKit {

    static {
        // 初始化证书
        InputStream in = DocxKit.class.getClassLoader().getResourceAsStream("license.xml");
        ThrowingRunnable.sneaky(() -> new License().setLicense(Objects.requireNonNull(in))).run();
    }

    /**
     * docx -> html（如无特殊需求，使用word自带的"另存为"转html功能更好）
     */
    public static String word2Html(String soucePath) throws Exception {
        HtmlSaveOptions saveOptions = new HtmlSaveOptions();
        saveOptions.setExportHeadersFootersMode(ExportHeadersFootersMode.NONE);
        saveOptions.setExportImagesAsBase64(Boolean.TRUE);

        ByteArrayOutputStream htmlStream = new ByteArrayOutputStream();

        Document doc = new Document(soucePath);
        doc.save(htmlStream, saveOptions);

        String htmlText = new String(htmlStream.toByteArray(), StandardCharsets.UTF_8);
        htmlStream.close();
        return htmlText;
    }

    public static String word2Txt(String soucePath) throws Exception {
        HtmlSaveOptions saveOptions = new HtmlSaveOptions();
        saveOptions.setExportHeadersFootersMode(ExportHeadersFootersMode.NONE);
        saveOptions.setExportImagesAsBase64(Boolean.TRUE);

        ByteArrayOutputStream htmlStream = new ByteArrayOutputStream();

        Document doc = new Document(soucePath);
        doc.save(htmlStream, saveOptions);

        String htmlText = new String(htmlStream.toByteArray(), StandardCharsets.UTF_8);
        htmlStream.close();
        return htmlText;
    }

    /**
     * docx -> html
     */
    @Test
    public void test20200525141858() throws Exception {
        String html = word2Html("/Users/caojing/Downloads/关于中国民法典.doc");
        System.out.println();
    }
}
