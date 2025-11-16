package filter;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositeFilter<T> implements Filter<T> {
    protected final List<Filter<T>> filters = new ArrayList<>();

    public void addFilter(Filter<T> f) {
        filters.add(f);
    }

    public void removeFilter(Filter<T> f) {
        filters.remove(f);
    }

    @Override
    public boolean matches(T item) {
        for (Filter<T> f : filters) {
            if (!f.matches(item)) return false;
        }
        return true;
    }

    @Override
    public void clear() {
        filters.clear();
        setDefaultFilters();
    }

    protected abstract void setDefaultFilters(); // subclasses define defaults
}
