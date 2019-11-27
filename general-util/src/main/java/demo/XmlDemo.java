package demo;

import org.junit.Test;

import static util.XmlUtil.beanToXml;
import static util.XmlUtil.xmlToBean;

/**
 * xml工具类
 *
 * @author CaoJing
 * @date 2019/11/28 00:24
 */
public class XmlDemo {

    /**
     * 实体类转xml
     */
    @Test
    public void test20191128002012() {
        XmlBean xmlBean = new XmlBean()
            .setDeviceInfo("ios")
            .setOpenid("1234567890")
            .setPromotionDetail("促销细节");

        System.out.println(beanToXml(xmlBean));
    }

    /**
     * xml转实体类
     */
    @Test
    public void test20191128002111() {
        String xml = "<xml>\n" +
            "  <promotion_detail>促销细节</promotion_detail>\n" +
            "  <device_info>ios</device_info>\n" +
            "  <openid>1234567890</openid>\n" +
            "</xml>";

        XmlBean xmlBean = xmlToBean(xml, XmlBean.class);

        System.out.println();
    }
}
