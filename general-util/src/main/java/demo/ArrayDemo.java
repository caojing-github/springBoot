package demo;

import org.junit.Test;

import java.util.Arrays;

/**
 * 数组
 *
 * @author CaoJing
 * @date 2020/04/11 16:55
 */
public class ArrayDemo {

    /**
     * 二分查找
     */
    @Test
    public void test20200411165543() {
        System.out.println(Arrays.binarySearch(new int[]{1, 1, 2, 3, 5}, 3));
    }
}
