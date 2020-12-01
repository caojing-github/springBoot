package demo;

import org.junit.Test;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * CompletableFuture
 * runAsync() 运行一个异步任务不返回结果
 * supplyAsync() 运行一个异步任务并且返回结果
 *
 * @author CaoJing
 * @date 2020/08/26 03:29
 */
@SuppressWarnings("all")
public class CompletableFutureDemo {

    /**
     * CompletableFuture.supplyAsync
     */
    @Test
    public void test20200826033507() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            int i = 1 / 0;
            return 100;
        });
//        future.join();
        future.get();
    }

    public static CompletableFuture<Integer> compute() {
        final CompletableFuture<Integer> future = new CompletableFuture<>();
        return future;
    }

    /**
     * https://colobu.com/2016/02/29/Java-CompletableFuture/
     */
    @Test
    public void test20200826034007() throws IOException {
        final CompletableFuture<Integer> f = compute();
        class Client extends Thread {
            CompletableFuture<Integer> f;

            Client(String threadName, CompletableFuture<Integer> f) {
                super(threadName);
                this.f = f;
            }

            @Override
            public void run() {
                try {
                    System.out.println(this.getName() + ": " + f.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        new Client("Client1", f).start();
        new Client("Client2", f).start();
        System.out.println("waiting");
        f.complete(100);
//        f.completeExceptionally(new Exception());
        System.in.read();
    }

    /**
     * applyToEither
     */
    @Test
    public void test20200826042651() {
        Random rand = new Random();
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(10000 + rand.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        });
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(10000 + rand.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 200;
        });
        CompletableFuture<String> f = future.applyToEither(future2, i -> i.toString());

    }
}
