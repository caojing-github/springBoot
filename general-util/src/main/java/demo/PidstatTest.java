package demo;

/**
 * pidstat
 *
 * @author CaoJing
 * @date 2020/09/06 14:46
 */
@SuppressWarnings("all")
public class PidstatTest {

    /**
     * pidstat -p 17468 1 3 -u -t
     */
    public static void main(String[] args) {
        Runnable pidStatTask = () -> {
            while (true) {
                double v = Math.random() * Math.random();
            }
        };
        Runnable lasyTask = () -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(pidStatTask).start();
        new Thread(lasyTask).start();
    }
}
