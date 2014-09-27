package com.hyphen;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class ChainCollection<O, T extends Collection<O>> {
    private final T collection;

    public ChainCollection(T collection) {
        this.collection = collection;
    }

    public ChainCollection<O, T> filter(Predicate<? super O> predicate) {
        return new ChainCollection<>(Hyphen.filter(collection, predicate));
    }


    public <F, R extends Collection<F>> ChainCollection<F, R> pluck(Function<? super O, ? extends F> function) {
        R pluck = Hyphen.pluck(collection, function);
        return new ChainCollection<>(pluck);
    }

    public <F, R extends Collection<F>> ChainCollection<F,R> map(Function<? super O, ? extends F> mapper) {
        R map = Hyphen.map(collection, mapper);
        return new ChainCollection<>(map);
    }

    public ChainCollection<O,T> reject(Predicate<? super O> predicate) {
        return new ChainCollection<>(Hyphen.reject(collection, predicate));
    }

    public ChainCollection<O,T> where(Map<String, Object> whereClause) {
        return new ChainCollection<>(Hyphen.where(collection, whereClause));
    }

    public <F extends Comparable> ChainCollection<F, Collection<F>> sort(Function<? super O, ? extends F> mapper) {
        return new ChainCollection<>(Hyphen.sort(collection, mapper));
    }

    public <F> ChainCollection<F, Collection<F>> flatten() {
        return new ChainCollection<>(Hyphen.flatten(collection));
    }

    public ChainCollection<O, T> without(O... ignores) {
        return new ChainCollection<>(Hyphen.without(collection, ignores));
    }

    public List<O> value() {
        return collection.stream().collect(toList());
    }
}
