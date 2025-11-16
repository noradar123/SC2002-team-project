package filter;

public interface Filter<T> {
    boolean matches(T t);
    default void clear() {} // for optional filters
}
