package tsostore;

public class DistributedObject<K, V> {

  private K key;
  private V value;
  private Version version = new Version(0);

  public DistributedObject() {
  }

  /**
   * Creates a new Object to encapsulate a key-value pair and a version.
   *
   * @param key key the object is stored under
   * @param value the value of the object
   * @param version the version of the object
   */
  public DistributedObject(K key, V value, Version version) {
    this.key = key;
    this.value = value;
    this.version = version;
  }

  /**
   * Creates a new Object from a copy of another by setting the version of the new object to
   * {accessVersion}.
   *
   * @param object the object to copy
   * @param accessVersion the version of the new objects
   */
  public DistributedObject(DistributedObject<K, V> object, Version accessVersion) {
    this.key = object.key;
    this.value = object.value;
    this.version = accessVersion;
  }

  public K getKey() {
    return key;
  }

  public V getValue() {
    return value;
  }

  public Version getVersion() {
    return version;
  }
}
