package demo.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Test;

/**
 * jackson
 */
@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PusherApp {

    private String id;

    private String appId;

    private String officeId;

    private String appSecret;

    /**
     * 驼峰与下划线互转
     * https://www.cnblogs.com/majianming/p/8491020.html
     */
    @Test
    public void test20191204191750() throws Exception {
        PusherApp app = new PusherApp();
        app.setAppId("caojing666");

        ObjectMapper mapper = new ObjectMapper();
        // 在类上加注解 @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class) 或 下面
//        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

        // 驼峰对象 转 下划线json
        String s1 = mapper.writeValueAsString(app);
        System.out.println(s1);

//        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

        // 下划线json 转 驼峰对象
        PusherApp app1 = mapper.readValue(s1, PusherApp.class);
        System.out.println();
    }
}
