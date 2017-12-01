# java-examples
Examples to use some functions or libs in java.

## future(2017-11-14)
Java future.

## metrics(2017-11-14)
[Metrics](http://metrics.dropwizard.io) to measure the behavior of critical components in java.
- (2017-12-01)old: yammer metrics.

> NOTE: yammer metrics -> from 3.0.0 move to -> codahale metrics -> from 3.1.0 move to -> dropwizard metrics

> `Coda Hale Yammer Inc.` So they are just from one corporation.

- (2017-12-02)Metrics: MetricRegistry & SharedMetricRegistries.

## java8
Usage for java8.
### stream(2017-11-16)
Usage of lambda expressions in Java8.
- (2017-11-16)`map()` vs. `flatMap()`;
- (2017-11-16)`distinct()` & `toMap()`

#### distinct(2017-11-17)
Usage of distinct() and it's replacement.
- (2017-11-17)use Wrapper to accomplish distinct();
- (2017-11-17)use Function + Predicate + filter() to accomplish distinct().

### functions(2017-11-19)
Usage for java.util.function.
- (2017-11-19)usage of all kinds of functions in java.util.function;
- (2017-11-19)use Function to replace other functions in java.util.function.

### Collectors and Stream.collect()(2017-11-22)
Usage for `java.util.Stream.Collectors`. `Stream.collect(Collector)` can collect a stream
into Collections using different Collectors, and `Collectors` can offer
different Collectors to support `Stream.collect(Collector)`.
- (2017-11-22)functions in Collectors.

## concurrency(2017-11-21)
A deep research for java's concurrency.
- (2017-11-21)use `ThreadLocal`;

### blockingqueue
Usage for implemented classes of BlockingQueue.
- (2017-11-28)use `ArrayBlockingQueue`;
- (2017-11-28)use `DelayQueue` & `ExecutorService`;

## jackson(2017-11-29)
- (2017-11-29)Jackson annotation for polymorphism;

## lombok(2017-11-30)
Usage for lombok.
- (2017-11-30)use `@lombok.Builder`;

## serialize(2017-11-30)
Usage for serializing a Java object.
- (2017-11-30)use ObjectInputStream & ObjectOutputStream;

## others(2017-11-19)
Other special usage in java.
- (2017-11-19)class `Void`;

## logs(2017-12-02)
Usage for log4j(outdated)/log4f2/logback/slf4j.

slf4j is just a facade, it relies on different underlying implements.
For example: `logback-classic` to use logback. And it'll download two
jars: `logback-classic` + `slf4j-api`.

- (2017-12-02)package-info.java: What I Think!
> I think using name to get a logger configured in the .xml file is easier now.