package tsostore;

import java.util.HashMap;
import java.util.Map;

public class ObjectStore<K, V> {

  private Map<K, DistributedObject<K, V>> objectMap = new HashMap<>();
  private int versionCounter = 0;

  /**
   * Get the object stored under {key} with version {version} or earlier.
   *
   * <p>The Timestamp Ordering (TSO) algorithm is used to ensure that transactions on the
   * {ObjectStore} are executed in a serializable order.</p>
   *
   * <p>To be able to call this method, start by calling {ObjectStore.startTx} to start a new
   * transaction, which returns a version number that identifies this transaction. Use this version
   * number for each request to ObjectStore and call {commitTx} at the end of the transaction.</p>
   *
   * <p>The request may result in an AbortException, when the transaction has to be aborted and
   * rolled back to the initial state.</p>
   *
   * @param version version of accessing transaction
   * @param key the key of the object to get
   * @return object stored under {key}
   * @throws AbortException thrown when the object has been accessed by a later thread since the
   *     transaction started
   */
  public synchronized DistributedObject<K, V> get(Version version, K key) throws AbortException {
    DistributedObject<K, V> object = objectMap.getOrDefault(key, new DistributedObject<>());

    // Abort if a thread with version > {version} has accessed the object
    if (object.getVersion().compareTo(version) > 0) {
      throw new AbortException("Abort on object with key: " + key);
    }

    // Update version timestamp on object
    object = new DistributedObject<>(object, version);
    objectMap.put(object.getKey(), object);

    return object;
  }

  public synchronized void put(Version version, K key, V value) {
    objectMap.put(key, new DistributedObject<>(key, value, version));
  }


  public synchronized Version startTx() {
    return new Version(versionCounter++);
  }

  public synchronized void commitTx(Version version) {
    // Nothing to do, as cannot fail commit if got here
    // Could record that {version} is free now
  }
}
