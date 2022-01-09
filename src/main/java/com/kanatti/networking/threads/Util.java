package com.kanatti.networking.threads;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class Util {
    public static byte[] getSha256Digest(String filename) {
        try {
            FileInputStream in = new FileInputStream(filename);
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            DigestInputStream din = new DigestInputStream(in, sha);
            while (din.read() != -1) ;
            din.close();
            return sha.digest();
        } catch (IOException | NoSuchAlgorithmException e) {
            System.err.println(e);
            return new byte[0];
        }
    }

    public static List<String> getDefaultFileNames() {
        return Arrays.asList("/tmp/data1.txt", "/tmp/data2.txt");
    }
}
