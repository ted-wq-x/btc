package com.go2going.temp;

import java.util.concurrent.Semaphore;

/**
 * Created by BlueT on 2017/7/4.
 */
public class TempTest {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1);

        Runnable runnable = () -> {
            try {
                boolean b = semaphore.tryAcquire(1);
                if (b) {
                    System.out.println(Thread.currentThread().getName() + "获得了信号量,时间为" + System.currentTimeMillis());
                    Thread.sleep(5000);
                    System.out.println(Thread.currentThread().getName() + "释放了信号量,时间为" + System.currentTimeMillis());
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                semaphore.release();
            }
        };
        for (int i = 0; i < 2; i++) {
            Thread thread = new Thread(runnable);
            thread.start();
        }

    }

}
