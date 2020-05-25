import java.util.ArrayList;
import java.util.List;
import jdk.nashorn.internal.ir.annotations.Ignore;
import tsostore.ObjectStore;
import tsostore.Version;

public class Main {

  private static final int N = 100;   // Number of workers
  private static final int COLLISION_LEVEL = 5;
  private static final boolean PRINT_MESSAGES = true;

  /**
   * Runs a test on {tsostore.ObjectStore}
   */
  public static void main(@Ignore String[] args) {
    // Initialise store
    ObjectStore<Integer, String> store = new ObjectStore<>();
    for (int i = 0; i < N; i++) {
      store.put(new Version(0), i, "Initial content");
    }

    // Spawn worker threads
    List<Thread> threads = new ArrayList<>();
    for (int i = 0; i < N; i++) {
      ArrayList<Integer> requiredObjects = new ArrayList<>();
      for (int j = 0; j < COLLISION_LEVEL; j++) {
        requiredObjects.add(j);
      }
      requiredObjects.add(i);

      Worker worker = new Worker(store, requiredObjects, PRINT_MESSAGES);
      threads.add(new Thread(worker));
    }

    for (Thread thread : threads) {
      thread.start();
    }

    // Await completion
    for (Thread thread : threads) {
      try {
        thread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    System.out.println("Done!");
  }
}
