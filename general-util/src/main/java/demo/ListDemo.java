package demo;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

/**
 * 描述
 *
 * @author CaoJing
 * @date 2020/03/18 21:28
 */
public class ListDemo {

    @Test
    public void test20200318212847() {
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8);
        for (Integer i : list) {
            if (i == 3) {
                continue;
            }
            System.out.println(i);
        }
    }
}
