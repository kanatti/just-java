package kanatti.justjava.networking.threads;

import java.util.List;

import kanatti.justjava.networking.io.Streams;

import javax.xml.bind.DatatypeConverter;

public class DigestThread  extends Thread {
    private final String filename;

    private byte[] digest;

    public DigestThread(String filename) {
        this.filename = filename;
    }

    @Override
    public void run() {
        System.out.println("Reading data from " + filename);
        this.digest = Util.getSha256Digest(filename);
        String result = filename + " : " +
                DatatypeConverter.printHexBinary(digest);
        System.out.println(result);
    }

    public byte[] getDigest() {
        return this.digest;
    }

    public static void main(String[] args) {
        List<String> filenames = Util.getDefaultFileNames();
        for (String filename: filenames) {
            Streams.writeToTemp("Hello, World\nHow are you? " + filename, filename);
        }

        for (String filename: filenames) {
            Thread t = new DigestThread(filename);
            t.start();
        }
    }
}
