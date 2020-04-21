package demo;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Set方法操作示例
 *
 * @author CaoJing
 * @date 2020/03/06 13:42
 */
public class SetDemo {

    @Test
    public void test20200306134238() {
        TreeSet treeSet = new TreeSet<>();
        treeSet.add(1);
        treeSet.add(5);
        treeSet.add(2);
        treeSet.add(1);
        treeSet.add(6);

        System.out.println(treeSet);

        treeSet.forEach(System.out::println);
    }

    @Test
    public void test20200417135233() {
        String x = String.format("%04d", 123);
        System.out.println(x);
    }

    @Test
    public void test20200417135248() {
        Set<String> result = new HashSet<>();
        Set<String> set1 = new HashSet<String>() {
            {
                add("王者荣耀");
                add("英雄联盟");
                add("穿越火线");
                add("地下城与勇士");
            }
        };

        Set<String> set2 = new HashSet<String>() {
            {
                add("王者荣耀");
                add("地下城与勇士");
                add("魔兽世界");
            }
        };

        result.clear();
        result.addAll(set1);
        result.retainAll(set2);
        System.out.println("交集：" + result);

        result.clear();
        result.addAll(set1);
        result.removeAll(set2);
        System.out.println("差集：" + result);

        result.clear();
        result.addAll(set1);
        result.addAll(set2);
        System.out.println("并集：" + result);
    }
}
