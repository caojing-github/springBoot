package demo;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

import static com.pivovarit.function.ThrowingConsumer.sneaky;
import static util.HBaseKit.findOne;


/**
 * throwing-function示例 https://github.com/pivovarit/throwing-function
 *
 * @author CaoJing
 * @date 2020/02/13 01:21
 */
@Slf4j
public class ThrowingFunctionDemo {

    @Test
    public void test20200213013842() {
        Lists.newArrayList("1").forEach(sneaky(x -> System.out.println(1 / 0)));
    }

    @Test
    public void test20200213012228() {
        List<String> list = Lists.newArrayList("35C0D1C8A8729AAC804A055C74E25055");
        list.forEach(sneaky(x -> System.out.println(findOne("judgement_ds", x))));
    }


}
