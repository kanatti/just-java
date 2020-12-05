package kanatti.justjava.networking.threads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Create a singleton of this logger and pass it on to all threads
 * info, error and debug calls are synchronized.
 * Note: Just for demonstration purpose, no need at actually synchronize
 * the log calls, just use a string-builder and write in one step
 */
public class SynchronizedLogger {
    private final String filename;
    private final Writer out;

    SynchronizedLogger(String filename) throws IOException {
        this.filename = filename;
        File file = new File(filename);
        out = new FileWriter(file);
    }

    // Synchronises on `this` but we can also explicitly do it on any object like `out`
    private synchronized void log(String prefix, String message) {
        Date d = new Date();
        try {
            out.write("[" + prefix + "]");
            out.write(d.toString());
            out.write("\t");
            out.write(message);
            out.write("\r\n");
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void info(String message) {
        log("INFO", message);
    }

    public void error(String message) {
        log("ERROR", message);
    }

    public void debug(String message) {
        log("DEBUG", message);
    }

    public void close() throws IOException {
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println("Stream closed");
        }
    }

    public static class Task implements Runnable {
        private final SynchronizedLogger logger;
        private final String threadName;

        Task(SynchronizedLogger logger, String threadName) {
            this.logger = logger;
            this.threadName = threadName;
        }

        @Override
        public void run() {
            logger.info("Running run from " + threadName);
            for (int i = 0; i < 10_000; i++) {
                // Just delaying a bit
            }
            logger.info("Returing from "  + threadName);
        }
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(4);
        SynchronizedLogger logger = new SynchronizedLogger("/tmp/test-logs.txt");

        Task task1 = new Task(logger, "Thread 1");
        Task task2 = new Task(logger, "Thread 2");
        Task task3 = new Task(logger, "Thread 3");
        Task task4 = new Task(logger, "Thread 4");

        Future<?> f1 = service.submit(task1);
        Future<?> f2 = service.submit(task2);
        Future<?> f3  = service.submit(task3);
        Future<?> f4  = service.submit(task4);

        f1.get();
        f2.get();
        f3.get();
        f4.get();

        logger.close();

        service.shutdown();
    }
}
