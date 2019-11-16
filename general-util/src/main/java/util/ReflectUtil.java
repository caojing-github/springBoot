package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 反射工具
 *
 * @author CaoJing
 * @date 2019/10/20 16:18
 */
@Slf4j
public class ReflectUtil {

    private ReflectUtil() {
    }

    public static Field getFieldByFieldName(Object obj, String fieldName) {
        Class superClass = obj.getClass();

        while (superClass != Object.class) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException var5) {
                log.error("", var5);
                superClass = superClass.getSuperclass();
            }
        }

        return null;
    }

    public static Object getValueByFieldName(Object obj, String fieldName) throws Exception {
        Field field = getFieldByFieldName(obj, fieldName);
        Object value = null;
        if (field != null) {
            if (field.isAccessible()) {
                value = field.get(obj);
            } else {
                field.setAccessible(true);
                value = field.get(obj);
                field.setAccessible(false);
            }
        }

        return value;
    }

    public static void setValueByFieldName(Object obj, String fieldName, Object value) throws Exception {
        Field field = getFieldByFieldName(obj, fieldName);
        if (field.isAccessible()) {
            field.set(obj, value);
        } else {
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        }
    }

    /**
     * map转对象（map中的key为下划线格式）
     */
    @SuppressWarnings("all")
    public static <T> T mapToObject(Map<String, String> map, Class<T> clazz) {
        // 下划线转驼峰
        Converter<String, String> c = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);
        JSONObject jsonObject = new JSONObject();
        map.forEach((x, y) -> jsonObject.put(c.convert(x), y));
        return JSON.parseObject(jsonObject.toJSONString(), clazz);
    }
}