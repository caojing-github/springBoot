package demo.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Test;

@Data
@Accessors(chain = true)
public class Person {

    public int personId;

    public String personName;

    public String personEmail;

    public String personPhone;

    @Test
    public void test20191204193435() {
        // 创建对象并赋值
        Person person = new Person();
        person.personId = 21;
        person.personName = "小奋斗教程";
        person.personEmail = "1732482792@qq.com";
        person.personPhone = "156983444xx";

        // 序列化配置对象
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

        // 序列化对象
        String json = JSON.toJSONString(person, config);
        System.out.println("反序列 person json -> ");
        System.out.println(json);

        // 反序列化配置对象
        ParserConfig parserConfig = new ParserConfig();
        parserConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

        // 反序列化对象
        person = JSON.parseObject(json, Person.class, parserConfig);
        System.out.println("反序列化 内容 -> ");
        System.out.print(person.personId + " ");
        System.out.print(person.personName + " ");
        System.out.print(person.personEmail + " ");
        System.out.println(person.personPhone);
    }
}