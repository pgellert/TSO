import java.util.ArrayList;
import java.util.List;
import tsostore.AbortException;
import tsostore.DistributedObject;
import tsostore.ObjectStore;
import tsostore.Version;

public class Worker implements Runnable {

  private ObjectStore<Integer, String> store;
  private List<Integer> objectIds;
  private boolean printContent = true;

  public Worker(ObjectStore<Integer, String> store, List<Integer> objectIds) {
    this.store = store;
    this.objectIds = objectIds;
  }

  /**
   * Initialises a Runnable that prints the objects in {store} with ids {objectIds}
   *
   * <p>If the {printContent} is set, it prints the objects to stdout.<p/>
   *
   * @param store the shared object store
   * @param objectIds the ids of the objects to print
   * @param printContent print objects to stdout
   */
  public Worker(ObjectStore<Integer, String> store, List<Integer> objectIds, boolean printContent) {
    this.store = store;
    this.objectIds = objectIds;
    this.printContent = printContent;
  }

  @Override
  public void run() {
    boolean success = false;
    int tryCount = 0;
    while (!success) {
      try {
        tryCount++;
        runTransaction();
        success = true;
      } catch (AbortException e) {
        // Abort transaction
        System.err.println("Abort, have to retry");

        // NB.: here we have no state to roll back, as we only print the content of the object.
        // However, we would need to roll back any changes we have made before
        // e.g. if we wanted to ensure that we only print once, we would need to somehow delete the
        //      lines we have sent to print
      } finally {
        if (tryCount == 10000) {
          System.err.println("Transaction failed");
          success = true;
        }
      }
    }

    System.out.flush();
    System.err.flush();
  }

  private void runTransaction() throws AbortException {
    Version version = store.startTx();

    List<DistributedObject<Integer, String>> objects = new ArrayList<>();
    for (int id : objectIds) {
      DistributedObject<Integer, String> obj = store.get(version, id);
      if (printContent) {
        System.out.println(obj.getValue());
      }
    }

    store.commitTx(version);
  }
}
