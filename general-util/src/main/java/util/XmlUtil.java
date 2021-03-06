package util;

import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Xml工具类
 *
 * @author CaoJing
 * @date 2019/3/28 17:30
 */
@Slf4j
public class XmlUtil {

    public static Map<String, Object> parseXml(InputStream inputStream) {
        try {
            Map<String, Object> map = new HashMap<>();
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            Element root = document.getRootElement();
            List<Element> elements = root.elements();

            for (Element e : elements) {
                map.put(e.getName(), e.getText());
            }

            return map;
        } catch (Exception e) {
            log.error("", e);
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("", e);
                return null;
            }
        }
    }

    public static JSONObject parseXmlJson(String xml) {
        try {
            SAXReader builder = new SAXReader();
            Document document = builder.read(new StringReader(xml));
            Element root = document.getRootElement();
            List<Element> elements = root.elements();
            JSONObject json = new JSONObject();

            for (Element e : elements) {
                if (e.isTextOnly()) {
                    json.put(e.getName(), e.getText());
                } else {
                    json.put(e.getName(), parseXmlJson(e));
                }
            }
            return json;
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    public static JSONObject parseXmlJson(Element root) {
        try {
            List<Element> elements = root.elements();
            JSONObject json = new JSONObject();
            for (Element e : elements) {
                if (e.isTextOnly()) {
                    json.put(e.getName(), e.getText());
                } else {
                    json.put(e.getName(), parseXmlJson(e));
                }
            }
            return json;
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    public static Map<String, Object> parseXml(String xml) {
        try {
            Map<String, Object> map = new HashMap<>();
            SAXReader builder = new SAXReader();
            Document document = builder.read(new StringReader(xml));
            Element root = document.getRootElement();
            List<Element> elements = root.elements();

            for (Element e : elements) {
                map.put(e.getName(), e.getText());
            }

            return map;
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    public static String toXml(Map<String, Object> data) {
        Document document = DocumentHelper.createDocument();
        Element nodeElement = document.addElement("xml");
        for (String key : data.keySet()) {
            Element keyElement = nodeElement.addElement(key);
            keyElement.setText(String.valueOf(data.get(key)));
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputFormat format = new OutputFormat("   ", true, "UTF-8");
            XMLWriter writer = new XMLWriter(out, format);
            writer.write(document);
            return out.toString("UTF-8");
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }


    public static String toXml(Map<String, String> headData, Map<String, String> bodyData) {
        Document document = DocumentHelper.createDocument();
        Element message = document.addElement("message");
        Element head = message.addElement("head");
        for (String key : headData.keySet()) {
            Element keyElement = head.addElement(key);
            keyElement.setText(headData.get(key));
        }
        Element body = message.addElement("body");
        for (String key : bodyData.keySet()) {
            Element keyElement = body.addElement(key);
            keyElement.setText(bodyData.get(key));
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputFormat format = new OutputFormat("", true, "UTF-8");
            XMLWriter writer = new XMLWriter(out, format);
            writer.write(document);
            return out.toString("UTF-8");
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * xml转实体类
     */
    public static <T> T xmlToBean(String xml, Class<T> cls) {
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(cls);
        return (T) xstream.fromXML(xml);
    }

    /**
     * 实体类转xml
     */
    public static String beanToXml(Object obj) {
        XStream xStream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));
        xStream.processAnnotations(obj.getClass());
        return xStream.toXML(obj);
    }
}
