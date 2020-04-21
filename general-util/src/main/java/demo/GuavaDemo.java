package demo;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

/**
 * Guava
 *
 * @author CaoJing
 * @date 2020/04/15 16:37
 */
public class GuavaDemo {

    public static final Joiner JOINER = Joiner.on(",").skipNulls();

    /**
     * Joiner.join
     */
    @Test
    public void test20200415163728() {
        List<String> list = Lists.newArrayList("1", null, "2");
        List<String> list2 = Lists.newArrayList();
        list2.add(null);
        System.out.println(JOINER.join(list));
        System.out.println(JOINER.join(list2));
    }
}
