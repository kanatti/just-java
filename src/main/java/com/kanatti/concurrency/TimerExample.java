package com.kanatti.concurrency;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class TimerExample {
    public static void main(String[] args) throws InterruptedException {
        final Counter safeCounter = new SafeCounter();
        final Counter unsafeCounter = new UnsafeCounter();
        for (int i = 0; i < 1000; i++) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    safeCounter.increment();
                    unsafeCounter.increment();
                }
            }, i);
        }

        for (int i = 0; i < 2000; i++) {
            safeCounter.increment();
            unsafeCounter.increment();
            Thread.sleep(1);
        }
        System.out.println("Safe Count: " + safeCounter.getCount());
        System.out.println("Unsafe Count: " + unsafeCounter.getCount());
    }

    static interface Counter {
        void increment();

        int getCount();
    }

    static class SafeCounter implements Counter {
        AtomicInteger _count = new AtomicInteger(0);

        public void increment() {
            _count.incrementAndGet();
        }

        public int getCount() {
            return _count.get();
        }
    }

    static class UnsafeCounter implements Counter {
        int _count = 0;

        public void increment() {
            _count += 1;
        }

        public int getCount() {
            return _count;
        }
    }
}
