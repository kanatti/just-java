package kanatti.justjava.networking.threads;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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
        try {
            FileInputStream in = new FileInputStream(filename);
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            DigestInputStream din = new DigestInputStream(in, sha);
            while (din.read() != -1) ;
            din.close();
            this.digest = sha.digest();

            String result = filename + " : " +
                    DatatypeConverter.printHexBinary(digest);
            System.out.println(result);
        } catch (IOException | NoSuchAlgorithmException e) {
            System.err.println(e);
        }
    }

    public byte[] getDigest() {
        return this.digest;
    }

    public static void main(String[] args) {
        List<String> filenames = Arrays.asList("/tmp/data1.txt", "/tmp/data2.txt");
        for (String filename: filenames) {
            Streams.writeToTemp("Hello, World\nHow are you? " + filename, filename);
        }

        for (String filename: filenames) {
            Thread t = new DigestThread(filename);
            t.start();
        }
    }
}
