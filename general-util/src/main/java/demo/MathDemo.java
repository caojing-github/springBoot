package demo;

import com.alibaba.fastjson.JSON;
import org.apache.commons.math3.util.Combinations;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.junit.Test;

/**
 * 数学分析demo
 * <p>
 * org.apache.commons.math3 API文档 http://commons.apache.org/proper/commons-math/javadocs/api-3.6.1/index.html
 * 项目主页 http://commons.apache.org/proper/commons-math/
 *
 * @author CaoJing
 * @date 2019/11/16 19:35
 */
public class MathDemo {

    /**
     * 组合
     */
    @Test
    public void test20191116195918() {
        Combinations combinations = new Combinations(5, 3);
        // 对象打印所有组合
        combinations.forEach(x -> System.out.println(JSON.toJSONString(x)));
        // 静态方法打印所有组合
        CombinatoricsUtils.combinationsIterator(5, 3).forEachRemaining(x -> System.out.println(JSON.toJSONString(x)));
        // 从5个数里取3个的总取法数
        System.out.println(CombinatoricsUtils.binomialCoefficient(5, 3));
    }
}
