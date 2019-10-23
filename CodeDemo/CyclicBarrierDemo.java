package com.ycg.test;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> {
            System.out.println("开始开会");
        });

        for (int i = 0; i < 5; i++) {
            final int tempi = i;
            new Thread(() -> {
                System.out.println(tempi + "号同学到了");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, "t" + i).start();
        }
    }
}
