import java.util.Random;
import java.util.concurrent.*;

public class Main {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    int biggestNumber = 0;
    String[] texts = new String[25];
    int pullSize = 5;

    for (int i = 0; i < texts.length; i++) {
      texts[i] = generateText("aab", 30_000);
    }

    ExecutorService executor = Executors.newFixedThreadPool(pullSize);

    for (String text : texts) {
      Callable<Integer> task = () -> {
        int maxSize = 0;
        for (int i = 0; i < text.length(); i++) {
          for (int j = 0; j < text.length(); j++) {
            if (i >= j) {
              continue;
            }
            boolean bFound = false;
            for (int k = i; k < j; k++) {
              if (text.charAt(k) == 'b') {
                bFound = true;
                break;
              }
            }
            if (!bFound && maxSize < j - i) {
              maxSize = j - i;
            }
          }
        }
        return maxSize;
      };

      Future<Integer> futureResult = executor.submit(task);
      Integer result = futureResult.get();

      if(result > biggestNumber) {
        biggestNumber = result;
      }
    }

    System.out.println("The maximum range among all presented values " + biggestNumber);
    executor.shutdown();
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
