package kanatti.justjava.networking.io;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Filters can be chained to an InputStream
 * Even though we can read() from any of the stream in the chain,
 * intermixing them can cause unexpected results, so read from the
 * last stream in the chain.
 * 1. Try deliberately overriding the reference
 * 2. Or avoid making intermediate references (inline)
 */
public class FilterStreams {
    /**
     * Extending the BufferedInput stream to peak into buffer details
     * buf: actual buffer
     * pos: current position in buffer
     * count: position of last valid byte in buffer
     */
    private static class ExtendedBufferedInputStream extends BufferedInputStream {

        public ExtendedBufferedInputStream(InputStream in) {
            super(in);
        }

        @Override
        public int read(byte[] b, int off, int len ) throws IOException {
            System.out.println("\nReading from buffer");
            System.out.printf("Buf length: %d, pos: %d, count: %d%n",
                    this.buf.length, this.pos, this.count);
            return super.read(b, off, len);
        }
    }

    public static void bufferedReadFromTemp(int bytesToRead) {
        try (BufferedInputStream buffIn = new ExtendedBufferedInputStream(
                new FileInputStream("/tmp/data.txt"))) {
            int available = buffIn.available();
            byte[] input = new byte[available];
            int result = 0;
            int read = 0;
            while (read < available) {
                int readTill = Math.min(read + bytesToRead, available);
                result = buffIn.read(input, read, readTill - read);
                if (result == -1) { break; }
                read = read + result;
                System.out.println("Bytes read - " + result);
                System.out.println("--" +
                        new String(Arrays.copyOfRange(input, read - result, read)) +
                        "--"
                );
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        Streams.writeToTemp("Hello there \n How are you?".getBytes());
        bufferedReadFromTemp(10);
    }
}
