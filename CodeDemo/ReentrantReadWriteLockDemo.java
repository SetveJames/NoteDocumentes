package com.ycg.test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockDemo {

    public static volatile Map<Integer, Object> cacheMap = new HashMap<>(); //模拟共享存储区域
    public static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            new Thread(() -> {
                write(finalI, finalI);
            }, "t" + i).start();
        }

        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            new Thread(() -> {
                read(finalI);
            }, "t" + i).start();
        }
    }

    public static void read(Integer key) {
        try {
            lock.readLock().lock();
            System.out.println(Thread.currentThread().getName() + "：正在读取");
            TimeUnit.MILLISECONDS.sleep(300);
            System.out.println(Thread.currentThread().getName() + "正在读取完成：" + cacheMap.get(key));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }

    public static void write(Integer key, Object value) {
        try {
            lock.writeLock().lock();
            System.out.println(Thread.currentThread().getName() + "：正在写入");
            TimeUnit.MILLISECONDS.sleep(300);
            cacheMap.put(key, value);
            System.out.println(Thread.currentThread().getName() + "：写入完成");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
