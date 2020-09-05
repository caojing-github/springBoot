package demo;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 原子类测试集合
 *
 * @author CaoJing
 * @date 2020/07/08 10:28
 */
public class AtomicDemo {

    /**
     * AtomicReference..get()
     */
    @Test
    public void test20200708102837() {
        JSONObject jsonObject = new JSONObject().fluentPut("name", "caojing");
        AtomicReference<JSONObject> reference = new AtomicReference<>(jsonObject);

        JSONObject jsonObject2 = reference.get();
        System.out.println(jsonObject2);
    }

    /**
     * AtomicReference.compareAndSet
     */
    @Test
    public void test20200708103101() {
        JSONObject jsonObject = new JSONObject().fluentPut("name", "caojing");
        // 引用为caojing
        AtomicReference<JSONObject> reference = new AtomicReference<>(jsonObject);

        JSONObject jsonObject2 = new JSONObject().fluentPut("name", "caojing666");
        // 引用改为caojing666
        boolean b = reference.compareAndSet(jsonObject, jsonObject2);
        System.out.println(b);

        JSONObject jsonObject3 = new JSONObject().fluentPut("name", "caojing");
        // 交换失败
        boolean b2 = reference.compareAndSet(jsonObject, jsonObject3);
        System.out.println(b2);

        JSONObject jsonObject4 = new JSONObject().fluentPut("name", "caojing666");
        // 虽然对象值一样但地址不一样交换失败
        boolean b3 = reference.compareAndSet(jsonObject4, jsonObject);
        System.out.println(b3);
    }

    /**
     * AtomicReference.getAndSet
     */
    @Test
    public void test20200708104148() {
        JSONObject jsonObject = new JSONObject().fluentPut("name", "caojing");
        // 引用为caojing
        AtomicReference<JSONObject> reference = new AtomicReference<>(jsonObject);

        JSONObject jsonObject2 = new JSONObject().fluentPut("name", "caojing666");
        // 以原子方式设置为新值返回旧值
        JSONObject jsonObject3 = reference.getAndSet(jsonObject2);
        System.out.println(jsonObject3);
        System.out.println(reference.get());
    }

    /**
     *
     */
    @Test
    public void test20200708111909() {
        AtomicReference<List<String>> reference = new AtomicReference<>(Lists.newArrayList());
        reference.get().add("aaa");
        List<String> list = reference.get();
        System.out.println(list);
    }

}
