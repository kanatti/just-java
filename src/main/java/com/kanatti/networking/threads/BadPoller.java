package com.kanatti.networking.threads;

import com.kanatti.networking.io.Streams;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BadPoller {

    public static long getCurrentTenthMillis() {
        return System.currentTimeMillis() / 10;
    }

    public static class TimeBucket {
        private final Map<Long, Integer> hits = new HashMap<>();

        public void addHit(long time) {
            int currentHits = this.hits.getOrDefault(time, 0);
            this.hits.put(time, currentHits + 1);
        }

        public String getStatus() {
            return hits.toString();
        }
    }

    public static void main(String[] args) {
        List<String> filenames = Arrays.asList("/tmp/data1.txt", "/tmp/data2.txt", "/tmp/data3.txt");
        for (String filename: filenames) {
            Streams.writeToTemp("Hello, World\nHow are you? " + filename, filename);
        }

        DigestThread[] threads = new DigestThread[filenames.size()];

        for (int i = 0; i < filenames.size(); i++) {
            threads[i] = new DigestThread(filenames.get(i));
            threads[i].start();
        }

        int polledCount = 0;

        List<TimeBucket> hitsList = new ArrayList<>();
        for (int i = 0; i < filenames.size(); i++) {
            hitsList.add(new TimeBucket());
        }

        while (polledCount < threads.length) {
            for(int i = 0; i < filenames.size(); i++) {
                hitsList.get(i).addHit(getCurrentTenthMillis());
                byte[] digest = threads[i].getDigest();
                if (digest != null) {
                    String result = "Polled - " +  filenames.get(i) + " : " + DatatypeConverter.printHexBinary(digest);
                    System.out.println(result);
                    polledCount +=1 ;
                }
            }
        }

        System.out.println("Polling Status:");
        for (int i = 0; i < filenames.size(); i++) {
            System.out.println(filenames.get(i));
            System.out.println(hitsList.get(i).getStatus());
        }
    }

}
