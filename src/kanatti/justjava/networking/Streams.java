package kanatti.justjava.networking;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * InputStream and OutputStreams provides the basic interface for i/o
 * Based on type of sink, there are different implementations like
 * FileInputStream, TelnetInputStream etc..
 */
public class Streams {
    public static void writeToTemp(byte[] bytes)  {
        // Try with resources will automatically close the output (Autocloseable)
        try (OutputStream out = new FileOutputStream("/tmp/data.txt")) {
            out.write(bytes);
            out.flush(); // Output Streams can be buffered so always flush them
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        // Manually handling the closing without try resource
        OutputStream out = null;
        try {
            out = new FileOutputStream("/tmp/data.txt");
            out.write(bytes);
            out.flush();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    // Ignore
                }
            }
        }
    }

    // When reading from networks, InputStream::read is blocking.
    // If we dont want to block, we can use available to get a heuristic of what can immediate read
    public static byte[] readFromTemp() {
        int bytesRead = 0;
        int bytesToRead = 1024; // InputStream read might terminate before reading all the bytes.
        byte[] input = new byte[bytesToRead];

        try (InputStream in = new FileInputStream("/tmp/data.txt")) {
            while (bytesRead < bytesToRead) {
                int result = in.read(input, bytesRead, bytesToRead - bytesRead);
                System.out.println(result);
                if (result == -1) { break; }
                bytesRead = bytesRead + result;
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        return Arrays.copyOfRange(input, 0, bytesRead);
    }

    public static void main(String[] args) throws IOException {
        writeToTemp("helloWorld".getBytes());
        System.out.println(new String(readFromTemp()));
    }
}
