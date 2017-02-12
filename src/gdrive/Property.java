package gdrive;

class Property<T>
{
  protected T value;

  public Property()
  {
    this(null); }
  public Property(T value) { this.value = value; }
  public T get() { return this.value; }
  public void set(T value) { this.value = value; }
  public boolean isNull() { return this.value == null; }
  public String toString() {
    return this.value == null ? null : this.value.toString();
  }
}