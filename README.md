Hyphen
=========

Java Equivalent of javascript underscore library

The stream api is a cool addition to Java 8 but it's use leads to some verbosity in the code. Underscorejs has a nice api for collection and this is an implementation for those api in java. Language limitations could have restricted the api being slightly different but the name of the method and the functionality provided are retained.

Usage
=========

Data
--

Employee fooEmployee = new Employee(10, "foo")

Employee barEmployee = new Employee(20, "bar")

Employee bazEmployee = new Employee(30, "baz")

Employee employees = [fooEmployee, barEmployee, bazEmployee]


Examples
-----

- each(employees, Employee::increment) => 

                calls increment for each employee

- map(employees, e -> e.getId() * e.getId()) => 

                [100,400,900]

- lapply(employees, e -> e.getId()) =>

                [100,400,900]

- fold(employees, (e, acc) -> acc + e.getName(), "") =>

                foobarbaz

- foldNoReduce(employees, (e, acc) -> acc.add(e.getName()), new ArrayList<String>()) =>

                ["foo","bar","baz"]

- reduce(employees, Employee::getId, 0, (a, b) -> a + b) => 

                60

- find(employees, e -> e.getName().equals("foo")) => 

                fooEmployee

- filter(employees, d -> d.getName().startsWith("ba")) =>

                [barEmployee,bazEmployee]

- pluck(employees, Employee::getName) =>

                ["foo", "bar", "baz"]

- where(employees, m(kv("id", 10),kv("name", "foo"))) =>

                [fooEmployee]

- findWhere(employees, m(kv("id", 10),kv("name", "foo"))) =>

                fooEmployee

- reject(employees, e -> e.getName().equals("bar)) =>

                [barEmployee,bazEmployee]

- every(employees, d -> d.getId() >= 10)

                true

- some(employees, d -> d.getName().equals("foo")) => 

                true
                
- max(employees, d -> d.getId()) =>

                30

- min(employees, d -> d.getId()) =>

                10

- sort([4,3,1,2], d -> d) =>

                [1,2,3,4]

- groupBy([1.2, 1.3, 2.1, 2.2, 3.1], d -> Math.floor(d)) =>

                [
                    {1:[1.2,1.3]}
                    {2:[2.1,2.2]}
                    {3:[3.1]}
                ]

- indexBy(employees, Employee::getId =>

                [
                    {10:fooEmployee}
                    {20:barEmployee}
                    {30:bazEmployee]}
                ]

- countBy([1, 2, 3, 4, 5, 6, 7], d -> d % 2 == 0 ? "even" : "odd" =>

                [
                    {"even":3}
                    {"odd":4}
                ]

- partition([1, 2, 3, 4, 5, 6, 7], d -> d % 2 == 0)

                {
                    true:[2,4,6]
                    false:[1,3,4,7]
                }

- flatten([[1, 2], [3, 4], [5, [6, 7]]])

                [1, 2, 3, 4, 5, 6, 7]


- without([1, 2, 3, 4, 5, 6, 7], 2,3)

                [1, 4, 5, 6, 7]

- chain([6,4,5,3,2,1]).filter(d -> d > 3).map(d -> d * d).filter(d -> d != 25).sort(d -> d).value();

                [16,36]

Version
----

1.0

Tech
-----------

All code in java and no external dependencies. Junit 4.10 will be required to run the tests

Installation
--------------
Should be available in maven central soon.

License
----

MIT

Contribution
------------

Fork and send patch request

What's In the Name
------

If underscore is a small line, hyphen is even smaller. Compact readable code helps !!!

