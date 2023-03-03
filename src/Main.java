import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    private static final String letters = "abc";
    private static final int length = 100000;
    private static final int times = 10000;
    private static final BlockingQueue<String> stringsForA = new ArrayBlockingQueue<>(100, true);
    private static final BlockingQueue<String> stringsForB = new ArrayBlockingQueue<>(100, true);
    private static final BlockingQueue<String> stringsForC = new ArrayBlockingQueue<>(100, true);
    private static final List<Thread> threads = new LinkedList<>();

    public static void main(String[] args) throws InterruptedException {

        Thread stringsMaker = new Thread(() -> {
            for (int i = 0; i < times; i++) {
                String str = generateText(letters, length);
                try {
                    stringsForA.put(str);
                    stringsForB.put(str);
                    stringsForC.put(str);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        stringsMaker.start();

        Counter counterA = new Counter(stringsForA, 'a');
        Counter counterB = new Counter(stringsForB, 'b');
        Counter counterC = new Counter(stringsForC, 'c');

        threads.add(new Thread(counterA));
        threads.add(new Thread(counterB));
        threads.add(new Thread(counterC));

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println(counterA.getMaxString());
        System.out.println("Здесь 'a' встретилась " + counterA.getMaxCount() + " раз.");
        System.out.println(counterB.getMaxString());
        System.out.println("Здесь 'b' встретилась " + counterB.getMaxCount() + " раз.");
        System.out.println(counterC.getMaxString());
        System.out.println("Здесь 'c' встретилась " + counterC.getMaxCount() + " раз.");

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
    
}