package com.hyphen.predicate;

import java.util.function.Predicate;

public class InversePredicate<T> implements Predicate<T> {
    private final Predicate predicate;

    public InversePredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(T t) {
        return !predicate.test(t);
    }
}
