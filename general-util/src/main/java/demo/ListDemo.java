package demo;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述
 *
 * @author CaoJing
 * @date 2020/03/18 21:28
 */
@Slf4j
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

    /**
     * list去重
     */
    @Test
    public void test20200623153306() {
//        List<SignImageListVO.User> users = userDAO.getUsers(companyId, roleType)
//            .stream()
//            .map(x -> new SignImageListVO.User().setUserId(x.getUserId()).setUserName(x.getName()))
//            .collect(Collectors.collectingAndThen(
//                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SignImageListVO.User::getUserId))), ArrayList::new
//            ));
    }

    @Test
    public void test20200724203959() {

        Deque<String> deque = new ArrayDeque<>(10);
        for (int i = 0; i < 50; i++) {
            deque.add(i + "");
        }
        System.out.println();
    }

    /**
     * 测试前后定义变量一致
     */
    @Test
    public void test20200728163331() {
        Lists.newArrayList(1, 2, 3, 4)
            .stream()
            .filter(x -> x != 3)
            .map(x -> x + 5)
            .forEach(x -> System.out.println(x));
    }

    /**
     * peek
     */
    @Test
    public void test20200730195655() {
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4)
            .stream()
            .map(x -> {
                System.out.println(x);
                return x;
            }).collect(Collectors.toList());

        list = Lists.newArrayList(1, 2, 3, 4)
            .stream()
            .peek(x -> {
                System.out.println(x);
            }).collect(Collectors.toList());
    }

    /**
     * parallel
     */
    @Test
    public void test20200730200810() {
        Lists.newArrayList(1, 2, 3, 4).stream().parallel().forEach(x -> {
            log.info(Thread.currentThread().getName());
        });
    }
}
