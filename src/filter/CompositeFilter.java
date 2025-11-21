package filter;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract implementation of the {@link Filter} interface that aggregates multiple filters.
 * This class uses "AND" logic: an item must satisfy <b>all</b> added filters to match.
 * Subclasses should implement {@link #setDefaultFilters()} to define their specific baseline criteria.
 *
 * @param <T> The type of object being filtered.
 */
public abstract class CompositeFilter<T> implements Filter<T> {
    
    /**
     * The list of active filters that checks are delegated to.
     */
    protected final List<Filter<T>> filters = new ArrayList<>();

    /**
     * Constructs a new CompositeFilter.
     * Initializes an empty list of filters.
     */
    public CompositeFilter() {
        // Default constructor
    }

    /**
     * Adds a new filter criteria to the composition.
     * The new filter will be included in subsequent {@link #matches(Object)} checks.
     *
     * @param f The filter to add.
     */
    public void addFilter(Filter<T> f) {
        filters.add(f);
    }

    /**
     * Removes a specific filter criteria from the composition.
     *
     * @param f The filter to remove.
     */
    public void removeFilter(Filter<T> f) {
        filters.remove(f);
    }

    /**
     * Checks if the item satisfies <b>all</b> registered filters.
     *
     * @param item The object to check.
     * @return {@code true} if the item matches every filter in the list; {@code false} if it fails any single filter.
     */
    @Override
    public boolean matches(T item) {
        for (Filter<T> f : filters) {
            if (!f.matches(item)) return false;
        }
        return true;
    }

    /**
     * Resets the filter list to its default state.
     * This clears all manually added filters and re-applies the rules defined in {@link #setDefaultFilters()}.
     */
    @Override
    public void clear() {
        filters.clear();
        setDefaultFilters();
    }

    /**
     * Defines the mandatory or default filters for this specific composite type.
     * Subclasses must implement this to ensure the filter starts in a valid state (e.g., restricting a user to their own data).
     */
    protected abstract void setDefaultFilters(); // subclasses define defaults
}