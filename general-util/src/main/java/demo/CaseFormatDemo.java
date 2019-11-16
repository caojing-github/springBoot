package demo;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import lombok.extern.slf4j.Slf4j;

/**
 * 驼峰与下划线及各种格式互转
 */
@Slf4j
public class CaseFormatDemo {

    public static void main(String[] args) {
        // 下划线转驼峰
        Converter<String, String> c = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);
        log.info(c.convert("is_subscribe"));

        Converter<String, String> converter = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE);
        // 输出: user_name
        log.info(converter.convert("userName"));

        // 驼峰转连接符, userName -> user-name
        converter = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN);
        // 输出: user-name
        log.info(converter.convert("userName"));

        // 驼峰转首字符大写驼峰, userName -> UserName
        converter = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.UPPER_CAMEL);
        // 输出: UserName
        log.info(converter.convert("userName"));

        // 驼峰转大写下划线, userName -> USER_NAME
        converter = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.UPPER_UNDERSCORE);
        // 输出: USER_NAME
        log.info(converter.convert("userName"));
    }

}
