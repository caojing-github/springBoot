package demo;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述
 *
 * @author CaoJing
 * @date 2019/12/12 16:00
 */
public class MapDemo {

    /**
     * https://blog.csdn.net/u010938610/article/details/82622144
     */
    @Test
    public void test20191212163722() {
        Map<String, String> myMap = new HashMap<>();
        String keyA = "A";
        String keyB = "B";
        String keyC = "C";
        String keyD = "D";
        String keyE = "E";
        String keyF = "F";
        String keyG = "G";
        String keyH = "H";
        myMap.put(keyA, "str01A");
        myMap.put(keyB, "str01B");
//        myMap.put(keyC, "str01C");
        myMap.put(keyC, null);

        System.out.println(myMap);

        /**
         * merge
         * 存不存在key都合并
         */
        myMap.merge(keyA, "merge01", (v1, v2) -> v1 + v2);
        myMap.merge(keyD, "merge01", (v1, v2) -> v1 + v2);
        System.out.println(myMap);

        /**
         * compute
         * 存不存在key都操作
         */
        myMap.compute(keyB, (k, v) -> v == null ? "compute" : v + "compute");
        myMap.compute(keyF, (k, v) -> v == null ? "compute" : v + "compute");
        System.out.println(myMap);

        /**
         * computeIfAbsent
         * 不存在key或value为null时才操作
         */
        myMap.computeIfAbsent(keyC, k -> k + "computeIfAbsent");
        myMap.computeIfAbsent(keyG, k -> k + "computeIfAbsent");
        System.out.println(myMap);

        /**
         * computeIfPresent
         * 存在key或value为不为null时才操作
         */
        myMap.computeIfPresent(keyC, (k, v) -> k + v);
        myMap.computeIfPresent(keyH, (k, v) -> k + v);
        System.out.println(myMap);
    }
}
