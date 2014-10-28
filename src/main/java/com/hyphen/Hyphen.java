package com.hyphen;

import com.hyphen.predicate.CustomPredicate;
import com.hyphen.predicate.IgnorePredicate;
import com.hyphen.predicate.InversePredicate;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class Hyphen {

    public static <O> void each(Collection<O> list, Consumer<? super O> action) {
        list.stream().forEach(action);
    }

    public static <O, F, T extends Collection<O>, R extends Collection<F>> R map( T list, Function<? super O, ? extends F> mapper) {
        return collect(list.stream().map(mapper), list);
    }

    static <I,O, C extends Collection<I>, R extends Collection<O>> R collect(Stream<O> stream, C collection) {
        return (R) (collection instanceof List ? stream.collect(toList()) : stream.collect(toSet()));
    }

    public static <O, F> F reduce(Collection<O> list, Function<? super O, ? extends F> mapper, F identity, BinaryOperator<F> accumulator) {
        return list
                .stream()
                .map(mapper)
                .reduce(identity,
                        accumulator);
    }


    public static <O, F> F fold(Collection<O> list, BiFunction<? super O, F, F> mapper, F accumulator) {
        for (O o : list) {
            accumulator = mapper.apply(o, accumulator);
        }
        return accumulator;
    }

    public static <O, F> F foldNoReduce(Collection<O> list,
                                        BiFunction<? super O, F, ?> mapper,
                                        F accumulator) {
        list.forEach(f -> mapper.apply(f, accumulator));
        return accumulator;
    }

    public static <O, F, T extends Collection<O>, R extends Collection<F>> R lapply( T list, Function<? super O, ? extends F> mapper) {
        return map(list, mapper);
    }

    public static <O> O find(Collection<O> list, Predicate<? super O> predicate) {
        Optional<O> optional = list.stream().filter(predicate).findFirst();
        return optional.isPresent() ? optional.get() : null;
    }

    public static <O, T extends Collection<O>> T filter( T collection, Predicate<? super O> predicate) {
        return collect(collection.stream().filter(predicate), collection);
    }

    public static <O,T extends Collection<O>> T reject(T collection, Predicate<? super O> predicate) {
        return filter(collection, new InversePredicate<>(predicate));
    }

    public static <O> Boolean every(Collection<O> list, Predicate<? super O> predicate) {
        return filter(list, predicate).size() == list.size();
    }

    public static <O> Boolean some(Collection<O> list, Predicate<? super O> predicate) {
        return filter(list, predicate).size() > 0;
    }

    public static <O, F, T extends Collection<O>> List<F> pluck( T collection, Function<? super O, ? extends F> function) {
        return collection.stream().map(function).collect(toList());
    }

    public static <O, T extends Collection<O>> T where(T collection, Map<String, Object> whereClause) {
        return collect(collection.stream().filter(new CustomPredicate<O>(whereClause)), collection);
    }

    public static <O> O findWhere(Collection<O> list, Map<String, Object> whereClause) {
        Optional<O> optional = list.stream().filter(new CustomPredicate<O>(whereClause)).findFirst();
        return optional.isPresent() ? optional.get() : null;
    }

    public static <O, F extends Comparable<F>> F max(Collection<O> list, Function<? super O, ? extends F> mapper) {
        Optional<F> optional = list.stream().map(mapper).max(Comparator.<F>naturalOrder());
        return optional.isPresent() ? optional.get() : null;
    }

    public static <O, F extends Comparable<F>> F min(Collection<O> list, Function<? super O, ? extends F> mapper) {
        Optional<F> optional = list.stream().map(mapper).min(Comparator.<F>naturalOrder());
        return optional.isPresent() ? optional.get() : null;
    }

    public static <O, F extends Comparable<F>> Collection<F> sort(Collection<O> list, Function<? super O, ? extends F> mapper) {
        return list.stream().map(mapper).sorted().collect(toList());
    }

    public static <O, F> Map<F, List<O>> groupBy(Collection<O> list, Function<? super O, ? extends F> grouping) {
        return list.stream().collect(groupingBy(grouping, mapping(Function.<O>identity(), toList())));
    }

    public static <O, F> Map<F, O> indexBy(Collection<O> list, Function<? super O, ? extends F> grouping) {
        return list.stream().collect(toMap(grouping, Function.<O>identity()));
    }

    public static <O, F> Map<F, Integer> countBy(Collection<O> list, Function<? super O, ? extends F> grouping) {
        Map<F, Integer> result = new HashMap<>();
        groupBy(list, grouping).forEach((d, l) -> result.put(d, l.size()));
        return result;
    }

    public static <O, T extends Collection<O>> Map<Boolean, T> partition(T collection, Predicate<? super O> predicate) {
        Map<Boolean, T> result = new HashMap<>();
        result.put(true, filter(collection, predicate));
        result.put(false, filter(collection, new InversePredicate<O>(predicate)));
        return result;
    }

    public static <O, F> List<F> flatten(Collection<O> list) {
        List<F> result = new ArrayList<>();
        list.forEach(l -> recursiveFlatten(result, l));
        return result;
    }

    public static <O, T extends Collection<O>> T without(T collection, O... ignores) {
        return (T) filter(collection, new IgnorePredicate(ignores));
    }

    public static <O, T extends Collection<O>> T distinct(T collection) {
        return collect(collection.stream().distinct(), collection);
    }

    private static <O, F> void recursiveFlatten(Collection<F> result, O l) {
        if (l instanceof Iterable) {
            ((Iterable) l).forEach(sl -> recursiveFlatten(result, sl));
        } else {
            result.add((F) l);
        }
    }

    public static <O, T extends Collection<O>> ChainCollection<O,T> chain(T list) {
        return new ChainCollection<>(list);
    }
}