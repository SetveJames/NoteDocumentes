package com.ycg.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockConditionDemo {

    static int number;
    static Lock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    //生产者
    public static void increment() {
        try {
            lock.lock();
            while (number != 0) {
                condition.await();
            }
            number++;
            System.out.println(Thread.currentThread().getName() + "\t生产了：" + number);
            condition.signalAll(); //唤醒
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    //消费者
    public static void decrement() {
        try {
            lock.lock();
            while (number == 0) {
                condition.await();
            }
            System.out.println(Thread.currentThread().getName() + "\t消费了：" + number);
            number--;
            condition.signalAll(); //唤醒
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                increment();
            }
        }, "AA").start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                decrement();
            }
        }, "BB").start();
    }
}
