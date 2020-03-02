import org.junit.Test;

import java.util.Base64;

/**
 * 非spring测试
 *
 * @author CaoJing
 * @date 2020/02/27 12:17
 */
public class CaoJingTest {

    /**
     * Base64编码
     */
    @Test
    public void test20200227121725() {
        String s = "曹靖法院";
        String encode = Base64.getEncoder().encodeToString(s.getBytes());
        System.out.println(encode);
    }
}
