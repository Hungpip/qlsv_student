
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JOptionPane;

public class KotlinEntry {
    private record Task(String key, String displayName, Runnable action) {
            private Task(String key, String displayName, Runnable action) {
                this.key = key;
                this.displayName = displayName;
                this.action = action;
                if (key == null || key.isBlank()) {
                    throw new IllegalArgumentException("key cannot be null or blank");
                }
            }
        }

    private static final Object lock = new Object();
    private static final LinkedList<Student> list = new LinkedList<>();

    public static void start() {
        synchronized (lock) {
            log("start", LogLevel.I);
            long time = System.currentTimeMillis();
            realStart();
            time = System.currentTimeMillis() - time;
            log("end (time: " + time + "ms)", LogLevel.I);
        }
    }

    private static final Object logLock = new Object();

    private enum LogLevel {
        I,
        W,
        E,
        X
    }

    private static void log(String message, LogLevel type) {
        String threadName = Thread.currentThread().getName();
        String truncatedName = threadName.length() > 12 ? threadName.substring(0, 9) + "..." : threadName;
        synchronized (logLock) {
            System.out.println("[" + type + "] [" + truncatedName + "] > " + message);
        }
    }

    private static final List<Task> tasks = Arrays.asList(
        new Task("0", "Exit", () -> {
            log("Exit", LogLevel.I);
            System.exit(0);
        }),
        new Task("1", "Add", () -> {
        }),
        new Task("2", "Print", () -> {
        }),
        new Task("3", "Sort", () -> {
        })
    );

    private static void realStart() {
        if (tasks.isEmpty()) {
            log("No tasks available. Exit!", LogLevel.X);
            return;
        } else {
            log(tasks.size() + " tasks available", LogLevel.I);
        }
        while (true) {
            boolean breakSignal = flow();
            if (breakSignal) {
                break;
            }
        }
    }

    private static boolean flow() {
        AtomicBoolean exit = new AtomicBoolean(false);
        printAndListen(exit);
        return exit.get();
    }

    private static void printAndListen(AtomicBoolean exit) {
        StringBuilder builder = new StringBuilder();
        for (Task task : tasks) {
            builder.append(task.key).append(" - ").append(task.displayName).append('\n');
        }
        while (true) {
            String input = JOptionPane.showInputDialog(builder.toString());
            if (input != null) {
                Task task = tasks.stream().filter(it -> it.key.equals(input)).findFirst().orElse(null);
                if (task != null) {
                    log("Executing Task@" + task.displayName, LogLevel.I);
                    task.action.run();
                    break;
                } else {
                    log("No task found for '" + input + "'", LogLevel.W);
                }
            } else {
                log("Invalid input", LogLevel.W);
            }
        }
    }

    private static <T extends Comparable<T>> void bubbleSort(T[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j].compareTo(arr[j + 1]) > 0) {
                    T temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    private static <T extends Comparable<T>> void selectionSort(T[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j].compareTo(arr[minIndex]) < 0) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                T temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
            }
        }
    }
}