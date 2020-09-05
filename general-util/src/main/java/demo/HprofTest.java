package demo;

/**
 * Hprof
 *
 * @author CaoJing
 * @date 2020/09/05 21:58
 */
public class HprofTest {

    public void slowMethod() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void slowerMethod() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HprofTest hprofTest = new HprofTest();
        hprofTest.slowerMethod();
        hprofTest.slowMethod();
    }
}
