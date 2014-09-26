package com.hyphen;

import com.hyphen.predicate.CustomPredicate;
import com.hyphen.predicate.IgnorePredicate;
import com.hyphen.predicate.InversePredicate;

import java.util.*;
import java.util.function.*;

import static java.util.stream.Collectors.*;

public class Hyphen {

    public static <O> void each(Collection<O> list, Consumer<? super O> action) {
        list.stream().forEach(action);
    }

    public static <O, F> List<F> map(Collection<O> list, Function<? super O, ? extends F> mapper) {
        return list.stream().map(mapper).collect(toList());
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

    public static <O, F> List<F> lapply(Collection<O> list, Function<? super O, ? extends F> mapper) {
        return map(list, mapper);
    }

    public static <O> O find(Collection<O> list, Predicate<? super O> predicate) {
        Optional<O> optional = list.stream().filter(predicate).findFirst();
        return optional.isPresent() ? optional.get() : null;
    }

    public static <O> List<O> filter(Collection<O> list, Predicate<? super O> predicate) {
        return list.stream().filter(predicate).collect(toList());
    }

    public static <O> List<O> reject(Collection<O> list, Predicate<? super O> predicate) {
        return filter(list, new InversePredicate<O>(predicate)).stream().collect(toList());
    }

    public static <O> Boolean every(Collection<O> list, Predicate<? super O> predicate) {
        return filter(list, predicate).size() == list.size();
    }

    public static <O> Boolean some(Collection<O> list, Predicate<? super O> predicate) {
        return filter(list, predicate).size() > 0;
    }

    public static <O, F> Collection<F> pluck(Collection<O> list, Function<? super O, ? extends F> function) {
        return list.stream().map(function).collect(toList());
    }

    public static <O> List<O> where(Collection<O> list, Map<String, Object> whereClause) {
        return list.stream().filter(new CustomPredicate<O>(whereClause)).collect(toList());
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

    public static <O> Map<Boolean, List<O>> partition(Collection<O> list, Predicate<? super O> predicate) {
        Map<Boolean, List<O>> result = new HashMap<>();
        result.put(true, filter(list, predicate));
        result.put(false, filter(list, new InversePredicate<O>(predicate)));
        return result;
    }


    public static <O, F> List<F> flatten(Collection<O> list) {
        List<F> result = new ArrayList<>();
        list.forEach(l -> recursiveFlatten(result, l));
        return result;
    }

    public static <O> List<O> without(Collection<O> list, O... ignores) {
        return filter(list, new IgnorePredicate(ignores));
    }

    public static <O> List<O> distinct(Collection<O> list) {
        return list.stream().distinct().collect(toList());
    }

    private static <O, F> void recursiveFlatten(Collection<F> result, O l) {
        if (l instanceof Iterable) {
            ((Iterable) l).forEach(sl -> recursiveFlatten(result, sl));
        } else {
            result.add((F) l);
        }
    }

    public static <O> ChainCollection<O> chain(Collection<O> list) {
        return new ChainCollection<O>(list);
    }
}
