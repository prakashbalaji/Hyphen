package com.hyphen;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class ChainList<O> {
    private final Collection<O> list;

    public ChainList(Collection<O> list) {
        this.list = list;
    }

    public ChainList<O> filter(Predicate<? super O> predicate) {
        return new ChainList<>(Hyphen.filter(list, predicate));
    }


    public <F> ChainList<F> pluck(Function<? super O, ? extends F> function) {
        return new ChainList<>(Hyphen.pluck(list, function));
    }

    public <F> ChainList<F> map(Function<? super O, ? extends F> mapper) {
        return new ChainList<>(Hyphen.map(list, mapper));
    }

    public ChainList<O> reject(Predicate<? super O> predicate) {
        return new ChainList<>(Hyphen.reject(list, predicate));
    }

    public ChainList<O> where(Map<String, Object> whereClause) {
        return new ChainList<>(Hyphen.where(list, whereClause));
    }

    public <F extends Comparable> ChainList<F> sort(Function<? super O, ? extends F> mapper) {
        return new ChainList<>(Hyphen.sort(list, mapper));
    }

    public <F> ChainList<F> flatten() {
        return new ChainList<>(Hyphen.flatten(list));
    }

    public ChainList<O> without(O... ignores) {
        return new ChainList<>(Hyphen.without(list, ignores));
    }

    public List<O> value() {
        return list.stream().collect(toList());
    }
}
