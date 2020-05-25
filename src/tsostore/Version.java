package tsostore;

import java.util.Objects;

public class Version implements Comparable<Version> {

  private int version;

  public Version(int version) {
    this.version = version;
  }

  public int getVersion() {
    return version;
  }


  @Override
  public int compareTo(Version o) {
    if (o == null) {
      throw new NullPointerException();
    }
    return version - o.version;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Version version1 = (Version) o;
    return version == version1.version;
  }

  @Override
  public int hashCode() {
    return Objects.hash(version);
  }
}
