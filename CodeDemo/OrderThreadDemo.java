package com.ycg.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 实现多个线程顺序执行,A线程先执行后唤醒B线程，B线程执行后唤醒C线程，C线程执行后再次唤醒A线程
 */
public class OrderThreadDemo {
    static int number = 1;
    static Lock lock = new ReentrantLock();

    //下面三个条件对象用于分别控制三个线程
    static Condition c1 = lock.newCondition();
    static Condition c2 = lock.newCondition();
    static Condition c3 = lock.newCondition();

    static void print5() {
        try {
            lock.lock();
            while (number != 1) {
                c1.await();
            }
            for (int i = 1; i <= 5; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            number = 2;
            c2.signal(); //唤醒B线程
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    static void print10() {
        try {
            lock.lock();
            while (number != 2) {
                c2.await();
            }
            for (int i = 1; i <= 10; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            number = 3;
            c3.signal(); //唤醒C线程
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    static void print15() {
        try {
            lock.lock();
            while (number != 3) {
                c3.await();
            }
            for (int i = 1; i <= 15; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            number = 1;
            c1.signal(); //再次唤醒A线程
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {

        new Thread(() -> {
            print5();
        }, "A线程").start();
        new Thread(() -> {
            print10();
        }, "B线程").start();
        new Thread(() -> {
            print15();
        }, "C线程").start();
    }
}
