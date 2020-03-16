package demo;

import org.junit.Test;

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

    public static void main(String[] args) {
        String x = String.format("%04d", 123);
        System.out.println(x);
    }
}
