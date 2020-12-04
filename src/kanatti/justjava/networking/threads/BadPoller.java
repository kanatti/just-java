package kanatti.justjava.networking.threads;

import kanatti.justjava.networking.io.Streams;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BadPoller {

    public static long getCurrentTenthMillis() {
        return System.currentTimeMillis() / 10;
    }

    public static class TimeBucket {
        private final HashMap<Long, Integer> hits = new HashMap<>();

        public void addHit(long time) {
            int currentHits = this.hits.getOrDefault(time, 0);
            this.hits.put(time, currentHits + 1);
        }

        public String getStatus() {
            return hits.toString();
        }
    }

    public static void main(String[] args) {
        List<String> filenames = Arrays.asList("/tmp/data1.txt", "/tmp/data2.txt");
        for (String filename: filenames) {
            Streams.writeToTemp("Hello, World\nHow are you? " + filename, filename);
        }

        DigestThread[] threads = new DigestThread[filenames.size()];

        for (int i = 0; i < filenames.size(); i++) {
            threads[i] = new DigestThread(filenames.get(i));
            threads[i].start();
        }

        for(int i = 0; i < filenames.size(); i++) {
            TimeBucket hits = new TimeBucket();
            while (true) {
                hits.addHit(getCurrentTenthMillis());
                byte[] digest = threads[i].getDigest();
                if (digest != null) {
                    String result = "Polled - " +  filenames.get(i) + " : " + DatatypeConverter.printHexBinary(digest);
                    System.out.println(result);
                    break;
                }
            }
            System.out.println("Polling Status "  + hits.getStatus());
        }
    }

}
