package demo;

import org.junit.Test;

import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Queue示例
 *
 * @author CaoJing
 * @date 2019/11/18 16:53
 */
public class QueueDemo {

    /**
     * PriorityQueue 基于优先级的无界优先级队列
     * https://www.jianshu.com/p/f1fd9b82cb72
     */
    @Test
    public void test20191118165422() {
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        for (int i = 0; i < 100; i++) {
            heap.add(i);
        }
        for (int i = 0; i < 100; i++) {
            if (i != heap.poll()) {
                System.out.println(i);
            }
        }
    }

    /**
     * 队列"先进先出"
     */
    @Test
    public void test20191207152634() {
        LinkedList list = new LinkedList();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);

        while (!list.isEmpty()) {
            Object remove = list.remove();
            System.out.println(remove);
        }
    }
}
