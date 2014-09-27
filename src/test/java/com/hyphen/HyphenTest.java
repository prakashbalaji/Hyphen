package com.hyphen;

import com.hyphen.model.Data;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static com.hyphen.CompactMap.KeyValue.kv;
import static com.hyphen.CompactMap.m;
import static com.hyphen.Hyphen.*;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class HyphenTest {

    @Test
    public void testCollectList() {
        List<Data> list = asList(new Data(10, "field1"), new Data(20, "field2"));
        assertTrue(collect(list.stream(), list) instanceof List );
    }

    @Test
    public void testCollectSet() {
        Set<Data> set = new HashSet<>();
        assertTrue( collect(set.stream(), set) instanceof Set );
    }


    @Test
    public void testEach() throws Exception {
        List<Data> list = asList(new Data(10, "field1"), new Data(20, "field2"));
        each(list, Data::increment);
        assertThat(pluck(list, Data::getId), hasItems(11, 21));
    }

    @Test
    public void testMap() throws Exception {
        List<Data> list = asList(new Data(10, "field1"), new Data(20, "field2"));
        assertThat(map(list, Data::square), hasItems(100, 400));
    }


    @Test
    public void testReduce() throws Exception {
        List<Data> list = asList(new Data(10, "field1"), new Data(20, "field2"));
        Integer total = reduce(list, Data::getId, 0, (a, b) -> a + b);
        String names = reduce(list, Data::getName, "", (a, b) -> a + b);
        assertThat(total, is(30));
        assertThat(names, is("field1field2"));
    }

    @Test
    public void testFold() throws Exception {
        List<Data> list = asList(new Data(10, "field1"), new Data(20, "field2"));
        String names = fold(list, (a, acc) -> acc + a.getName(), "");
        assertThat(names, is("field1field2"));
    }

    @Test
    public void testFoldNoReduce() throws Exception {
        List<Data> list = asList(new Data(10, "field1"), new Data(20, "field2"));
        List<String> names = foldNoReduce(list, (a, acc) -> acc.add(a.getName()), new ArrayList<String>());
        assertThat(names, is(asList("field1", "field2")));
    }

    @Test
    public void testLfold() throws Exception {
        List<Data> list = asList(new Data(10, "field1"), new Data(20, "field2"));
        List<String> names = lapply(list, a -> a.getName());
        assertThat(names, (hasItems("field1", "field2")));
    }


    @Test
    public void testFind() throws Exception {
        List<Data> list = asList(new Data(10, "field1"), new Data(20, "field1"));
        assertThat(find(list, d -> d.getName().equals("field1")).getId(), is(10));
        assertNull(find(list, d -> d.getName().equals("missing")));
    }

    @Test
    public void testFilter() throws Exception {
        List<Data> list = asList(new Data(10, "field1"), new Data(20, "field1"), new Data(10, "field3"));
        List<Data> filteredList = filter(list, d -> d.getId() == 10);
        assertThat(filteredList.size(), is(2));
        assertThat(pluck(filteredList, Data::getName), hasItems("field1", "field3"));
    }


    @Test
    public void testPluck() throws Exception {
        List<Data> list = asList(new Data(10, "field1"), new Data(20, "field2"));
        assertThat(pluck(list, Data::getName), hasItems("field1", "field2"));
        assertThat(pluck(list, Data::getId), hasItems(10, 20));
    }

    @Test
    public void testWhere() throws Exception {
        List<Data> list = asList(new Data(10, "field1"), new Data(20, "field1"), new Data(10, "field1"));
        List<Data> filteredList = where(list, m(
                kv("id", 10),
                kv("name", "field1")
        ));
        assertThat(filteredList.size(), is(2));
        assertThat(pluck(filteredList, Data::getName), hasItems("field1", "field1"));
        assertThat(pluck(filteredList, Data::getId), hasItems(10, 10));

        filteredList = where(list, m(
                kv("id", 10),
                kv("name", "missing")
        ));
        assertThat(filteredList.size(), is(0));
    }

    @Test
    public void testFindWhere() throws Exception {
        Data expectedData = new Data(10, "field1");
        List<Data> list = asList(expectedData, new Data(20, "field1"), new Data(10, "field1"));
        Data filtered = findWhere(list, m(
                kv("id", 10),
                kv("name", "field1")
        ));
        assertEquals(expectedData, filtered);

        filtered = findWhere(list, m(
                kv("id", 10)
        ));
        assertEquals(expectedData, filtered);

        filtered = findWhere(list, m(
                kv("id", 200)
        ));
        assertNull(filtered);
    }

    @Test
    public void testReject() throws Exception {
        List<Data> list = asList(new Data(10, "field1"), new Data(20, "field2"), new Data(10, "field3"));
        List<Data> filteredList = reject(list, d -> d.getId() == 10);
        assertThat(filteredList.size(), is(1));
        assertThat(pluck(filteredList, Data::getName), hasItems("field2"));
    }

    @Test
    public void testEvery() throws Exception {
        List<Data> list = asList(new Data(10, "field1"), new Data(10, "field2"), new Data(10, "field3"));
        assertTrue(every(list, d -> d.getId() == 10));
        assertFalse(every(list, d -> d.getName().equals("field1")));
    }

    @Test
    public void testSome() throws Exception {
        List<Data> list = asList(new Data(10, "field1"), new Data(10, "field2"), new Data(10, "field3"));
        assertTrue(some(list, d -> d.getName().equals("field1")));
        assertFalse(some(list, d -> d.getName().equals("missing")));
    }

    @Test
    public void testMax() throws Exception {
        List<Data> list = asList(new Data(10, "field1"), new Data(20, "field2"), new Data(30, "field3"));
        assertThat(max(list, d -> d.getId()), is(30));
        assertThat(max(list, d -> d.getName()), is("field3"));
        List<BigDecimal> bigDecimals = asList(new BigDecimal("10.3"), new BigDecimal("10.4"), new BigDecimal("10.5"));
        assertThat(max(bigDecimals, d -> d), is(new BigDecimal("10.5")));
    }

    @Test
    public void testMin() throws Exception {
        List<Data> list = asList(new Data(10, "field1"), new Data(20, "field2"), new Data(30, "field3"));
        assertThat(min(list, d -> d.getId()), is(10));
        assertThat(min(list, d -> d.getName()), is("field1"));
        List<BigDecimal> bigDecimals = asList(new BigDecimal("10.3"), new BigDecimal("10.4"), new BigDecimal("10.5"));
        assertThat(min(bigDecimals, d -> d), is(new BigDecimal("10.3")));
    }

    @Test
    public void testSort() throws Exception {
        List<Data> list = asList(new Data(30, "field1"), new Data(20, "field2"), new Data(10, "field3"));
        List<Data> expectedList = asList(new Data(10, "field3"), new Data(20, "field2"), new Data(30, "field1"));
        assertEquals(expectedList, sort(list, d -> d));
        List<Integer> list1 = asList(1, 2, 3, 4, 5, 6);
        assertEquals(asList(-5, -4, -3, -2, -1, 0), sort(list1, d -> d - 6));
    }

    @Test
    public void testGroupBy() throws Exception {
        List<Double> list = asList(1.2, 1.3, 2.1, 2.2, 3.1);
        Map<Integer, List<Double>> groupedList = groupBy(list, d -> (int) Math.floor(d));
        assertThat(groupedList.size(), is(3));
        assertThat(groupedList.get(1), hasItems(1.2, 1.3));
        assertThat(groupedList.get(2), hasItems(2.1, 2.2));
        assertThat(groupedList.get(3), hasItems(3.1));

        Data data1 = new Data(10, "field1");
        Data data2 = new Data(20, "field2");
        Data data3 = new Data(10, "field3");
        List<Data> list1 = asList(data1, data2, data3);
        Map<Integer, List<Data>> groupedList1 = groupBy(list1, Data::getId);
        assertThat(groupedList1.size(), is(2));
        assertThat(groupedList1.get(10), hasItems(data1, data3));
        assertThat(groupedList1.get(20), hasItems(data2));
    }

    @Test
    public void testIndexBy() throws Exception {
        Data data1 = new Data(10, "field1");
        Data data2 = new Data(20, "field2");
        Data data3 = new Data(30, "field3");
        List<Data> list = asList(data1, data2, data3);
        Map<Integer, Data> groupedList1 = indexBy(list, Data::getId);
        assertThat(groupedList1.size(), is(3));
        assertThat(groupedList1.get(10), is(data1));
        assertThat(groupedList1.get(20), is(data2));
        assertThat(groupedList1.get(30), is(data3));
    }

    @Test
    public void testCountBy() throws Exception {
        List<Integer> list = asList(1, 2, 3, 4, 5, 6, 7);
        Map<Object, Integer> counts = countBy(list, d -> d % 2 == 0 ? "even" : "odd");
        assertThat(counts.size(), is(2));
        assertThat(counts.get("even"), is(3));
        assertThat(counts.get("odd"), is(4));
    }

    @Test
    public void testPartitionBy() throws Exception {
        List<Integer> list = asList(1, 2, 3, 4, 5, 6, 7);
        Map<Boolean, List<Integer>> partitions = partition(list, d -> d % 2 == 0);
        assertThat(partitions.size(), is(2));
        assertThat(partitions.get(true), hasItems(2, 4, 6));
        assertThat(partitions.get(false), hasItems(1, 3, 5, 7));

        partitions = partition(list, d -> d % 9 == 0);
        assertTrue(partitions.get(true).isEmpty());
        assertThat(partitions.get(false), hasItems(1, 2, 3, 4, 5, 6, 7));
    }

    @Test
    public void testFlatten() throws Exception {
        List<Object> list = asList(asList(1, 2), asList(3, 4), asList(asList(5, 6)));
        List<Integer> flattened = flatten(list);
        assertThat(flattened.size(), is(6));
        assertThat(flattened, hasItems(1, 2, 3, 4, 5, 6));

        Data data1 = new Data(10, "field1");
        Data data2 = new Data(20, "field2");
        Data data3 = new Data(30, "field3");
        List<Object> list1 = asList(asList(data1, data2), asList(data3));
        assertThat(flatten(list1).size(), is(3));

    }

    @Test
    public void testWithout() throws Exception {
        List<Integer> list = asList(1, 2, 3, 4, 5, 6, 7);
        List<Integer> retained = without(list, 2, 3);
        assertThat(retained.size(), is(5));
        assertThat(retained, hasItems(1, 4, 5, 6, 7));
    }

    @Test
    public void testChainingObjects() throws Exception {
        List<Data> list = asList(new Data(10, "field1"), new Data(20, "field2"), new Data(30, "field3"));
        List<String> filterAndPluck = chain(list).filter(d -> d.getId() > 10).pluck(Data::getName).value();
        assertThat(filterAndPluck.size(), is(2));
        assertThat(filterAndPluck, hasItems("field2", "field3"));
    }

    @Test
    public void testChainingPrimitive() throws Exception {
        List<Integer> list = asList(6, 5, 4, 3, 2, 1);
        List<Integer> processed =
                chain(list)
                        .filter(d -> d > 3)
                        .map(d -> d * d)
                        .filter(d -> d != 25)
                        .sort(d -> d)
                        .value();

        assertThat(processed.size(), is(2));
        assertThat(processed.get(0), is(16));
        assertThat(processed.get(1), is(36));
    }

    @Test
    public void testDistinctForObjects() throws Exception {
        Data data1 = new Data(10, "field1");
        Data data2 = new Data(20, "field2");
        Data data3 = new Data(20, "field2");
        List<Data> list = asList(data1, data2, data3);

        assertEquals(Arrays.asList(data1, data2), distinct(list));
    }

    @Test
    public void testDistinct() throws Exception {
        List<String> strings = asList("a", "b", "a", "c");
        List<Integer> integers = asList(1, 2, 2, 1, 4, 1);

        assertEquals(asList("a", "b", "c"), distinct(strings));
        assertEquals(asList(1, 2, 4), distinct(integers));
    }

}
