package filter;

/**
 * A functional interface for filtering objects of type T.
 * Implementations define specific criteria to determine if an object should be included.
 *
 * @param <T> The type of object to be filtered.
 */
public interface Filter<T> {

    /**
     * Determines if the given object matches the filter criteria.
     *
     * @param t The object to check.
     * @return {@code true} if the object matches; {@code false} otherwise.
     */
    boolean matches(T t);

    /**
     * Clears any internal state or optional sub-filters.
     * The default implementation does nothing, but composite filters may override this
     * to reset their configuration.
     */
    default void clear() {} // for optional filters
}