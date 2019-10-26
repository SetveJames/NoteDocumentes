package com.ycg.test;


import java.util.concurrent.atomic.AtomicInteger;

class ResouceData {
    public volatile AtomicInteger atomicInteger = new AtomicInteger();

    //负责生产
    public void productor() {
        synchronized (this) {
            try {
                while (atomicInteger.get() != 0) {
                    wait();
                }
                int i = atomicInteger.incrementAndGet();
                System.out.println(Thread.currentThread().getName() + "\t生产了：" + i);
                notifyAll(); //唤醒消费线程
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //负责消费
    public void consumer() {
        synchronized (this) {
            try {
                while (atomicInteger.get() == 0) {
                    wait();
                }
                int i = atomicInteger.get();
                System.out.println(Thread.currentThread().getName() + "\t消费了：" + i);
                atomicInteger.decrementAndGet();
                notifyAll(); //唤醒生产线程
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

public class SynchronizedDemo {
    public static void main(String[] args) {
        ResouceData resouceData = new ResouceData();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                resouceData.productor();
            }
        }, "Product").start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                resouceData.consumer();
            }
        }, "Consumer").start();
    }
}
