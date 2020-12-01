package cn.huanzi.qch.springbootjarwar;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;

/**
 * 测试controller
 */
@Slf4j
@RestController
public class Controller {

    /**
     * 下载文件
     */
    @GetMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        File file = new File("/Users/caojing/Documents/未解析文书id修复情况-2020-12-01.xlsx");
        try (FileInputStream fis = new FileInputStream(file)) {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            IOUtils.copy(fis, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            log.error("下载文件异常", e);
        }
    }
}