package kanatti.justjava.networking.threads;

import kanatti.justjava.networking.io.Streams;

import javax.xml.bind.DatatypeConverter;
import java.util.List;

public class CallbackDigestRunner {
    public interface CallbackInterface {
        void receive(byte[] digest);
    }

    public static class CallbackDigest extends Thread {
        private final String filename;
        private final CallbackInterface callback;

        public CallbackDigest(String filename, CallbackInterface callback) {
            this.filename = filename;
            this.callback = callback;
        }

        @Override
        public void run() {
            System.out.println("Reading data from " + filename);
            callback.receive(Util.getSha256Digest(filename));
        }
    }

    public static void main(String[] args) {
        List<String> filenames = Util.getDefaultFileNames();
        for (String filename: filenames) {
            Streams.writeToTemp("Hey, World\nHow are you? " + filename, filename);
        }

        for (String filename: filenames) {
            Thread t = new CallbackDigest(filename, (digest -> {
                String result = filename + " : " +
                        DatatypeConverter.printHexBinary(digest);
                System.out.println(result);
            }));
            t.start();
        }
    }
}
