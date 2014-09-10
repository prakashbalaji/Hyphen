package com.hyphen;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class ChainCollection<O> {
    private final Collection<O> list;

    public ChainCollection(Collection<O> list) {
        this.list = list;
    }

    public ChainCollection<O> filter(Predicate<? super O> predicate) {
        return new ChainCollection<>(Hyphen.filter(list, predicate));
    }


    public <F> ChainCollection<F> pluck(Function<? super O, ? extends F> function) {
        return new ChainCollection<>(Hyphen.pluck(list, function));
    }

    public <F> ChainCollection<F> map(Function<? super O, ? extends F> mapper) {
        return new ChainCollection<>(Hyphen.map(list, mapper));
    }

    public ChainCollection<O> reject(Predicate<? super O> predicate) {
        return new ChainCollection<>(Hyphen.reject(list, predicate));
    }

    public ChainCollection<O> where(Map<String, Object> whereClause) {
        return new ChainCollection<>(Hyphen.where(list, whereClause));
    }

    public <F extends Comparable> ChainCollection<F> sort(Function<? super O, ? extends F> mapper) {
        return new ChainCollection<>(Hyphen.sort(list, mapper));
    }

    public <F> ChainCollection<F> flatten() {
        return new ChainCollection<>(Hyphen.flatten(list));
    }

    public ChainCollection<O> without(O... ignores) {
        return new ChainCollection<>(Hyphen.without(list, ignores));
    }

    public List<O> value() {
        return list.stream().collect(toList());
    }
}
