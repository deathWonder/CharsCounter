import java.util.concurrent.BlockingQueue;

public class Counter implements Runnable {

    private static final int times = 10000;
    private final BlockingQueue<String> list;
    private final char c;
    private int maxCount = 0;
    private String maxString;

    public Counter(BlockingQueue<String> list, char c) {
        this.list = list;
        this.c = c;
    }

    @Override
    public void run() {
        for (int i = 0; i < times; i++) {
            try {
                String str = list.take();
                int count = countChar(str, c);
                if (count > maxCount) {
                    maxCount = count;
                    maxString = str;
                    Thread.sleep(150);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static int countChar(String str, char c) {
        int counter = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                counter++;
            }
        }
        return counter;
    }

    public String getMaxString() {
        return maxString;
    }

    public int getMaxCount() {
        return maxCount;
    }
}
