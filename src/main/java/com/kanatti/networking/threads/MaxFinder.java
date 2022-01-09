package com.kanatti.networking.threads;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MaxFinder {
    public static class FindMaxTask implements Callable<Integer> {
        private final List<Integer> data;
        private final int start;
        private final int end;

        FindMaxTask(List<Integer> data, int start, int end) {
            this.data = data;
            this.start = start;
            this.end = end;
        }

        @Override
        public Integer call() {
            int max = data.get(start);
            for (int i = start + 1; i < end; i++) {
                if (data.get(i) > max) max = data.get(i);
            }
            System.out.printf("Found max for range %d - %d : %d\n", start, end, max);
            return max;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<Integer> data = Arrays.asList(1, 2, 5, 8, 3, 6, 9, 10, 11, 83, 23, 19, 0);

        FindMaxTask task1 = new FindMaxTask(data, 0, data.size()/2);
        FindMaxTask task2 = new FindMaxTask(data, data.size()/2 + 1, data.size());

        ExecutorService service = Executors.newFixedThreadPool(2);
        Future<Integer> future1 = service.submit(task1);
        Future<Integer> future2 = service.submit(task2);

        System.out.printf("Got max %d\n", Math.max(future1.get(), future2.get()));
        service.shutdown();
    }
}
