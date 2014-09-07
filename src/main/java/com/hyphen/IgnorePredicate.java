package com.hyphen;

import java.util.List;
import java.util.function.Predicate;

import static java.util.Arrays.asList;

public class IgnorePredicate<O> implements Predicate<O> {
    private final List<O> ignores;

    public IgnorePredicate(O[] os) {
        this.ignores = asList(os);
    }

    @Override
    public boolean test(O o) {
        return !ignores.contains(o);
    }
}
