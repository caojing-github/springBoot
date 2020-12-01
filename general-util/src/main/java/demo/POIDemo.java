package demo;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 描述
 *
 * @author CaoJing
 * @date 2020/11/26 15:38
 */
public class POIDemo {

    /**
     * 使用POI生成word
     */
    @Test
    public void test20201126152100() throws IOException {
        XWPFDocument document = new XWPFDocument();

        // 添加标题
        XWPFParagraph title = document.createParagraph();
        // 设置段落居中
        title.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun titleRun = title.createRun();
        titleRun.setText("第二百二十二条");
        titleRun.setColor("000000");
        titleRun.setFontSize(20);

        // 民法典
        XWPFParagraph p2 = document.createParagraph();
        XWPFRun p2Runun = p2.createRun();
        p2Runun.setText("涪陵区地方税务局在职职工274名。设10个机关科室，1个稽查局，1个办税服务厅，9个基层税务所。2014年，全局上下紧紧围绕推进税收现代化和建设“三区一城、幸福涪陵”的总体目标，以深化税收征管改革为重点，以党的群众路线教育实践活动为依托，推动各项工作顺利开展。");
//        p2Runun.setFontSize(16);

        // 其他法规
        XWPFParagraph p3 = document.createParagraph();
        XWPFRun p3Runun = p3.createRun();
        p3Runun.setText("绿色表示新增");
        p3Runun.setColor("00FF00");

        XWPFRun p4Runun = p3.createRun();
        p4Runun.setText("红色表示\n删除");
        p4Runun.setColor("FF3300");

        XWPFRun p5Runun = p3.createRun();
        p5Runun.setText("黑色表示不变");
        p5Runun.setColor("000000");

        FileOutputStream out = new FileOutputStream(new File("民法典新旧对照.docx"));
        document.write(out);
        out.close();
    }
}
