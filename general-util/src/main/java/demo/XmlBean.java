package demo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * xml对应的实体类
 * https://www.cnblogs.com/wuyun-blog/p/7800067.html
 */
@Data
@Accessors(chain = true)
@XStreamAlias("xml")
public class XmlBean {

    @XStreamAlias("promotion_detail")
    private String promotionDetail;

    @XStreamAlias("device_info")
    private String deviceInfo;

    @XStreamAlias("openid")
    private String openid;
}
