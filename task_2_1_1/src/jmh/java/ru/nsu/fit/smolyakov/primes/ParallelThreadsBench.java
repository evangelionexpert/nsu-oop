package ru.nsu.fit.smolyakov.primes;

import org.openjdk.jmh.annotations.*;

@BenchmarkMode({Mode.SingleShotTime})
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@State(Scope.Benchmark)
public class ParallelThreadsBench {
    @Param({"1", "2", "3", "4",
        "5", "6", "7", "8",
        "9", "10", "11", "12",
        "13", "14", "15", "16"})
    private static int amountOfCores;

    @Benchmark
    public void smallerTest() {
        var finder = new ParallelThreadsNonPrimeFinder(amountOfCores);
        finder.containsNonPrime(JmhSources.smallestArrayTrue);
    }

    @Benchmark
    public void smallTest() {
        var finder = new ParallelThreadsNonPrimeFinder(amountOfCores);
        finder.containsNonPrime(JmhSources.smallArrayFalse);
    }

    @Benchmark
    public void largeTest() {
        var finder = new ParallelThreadsNonPrimeFinder(amountOfCores);
        finder.containsNonPrime(JmhSources.largeArrayTrue);
    }
}
