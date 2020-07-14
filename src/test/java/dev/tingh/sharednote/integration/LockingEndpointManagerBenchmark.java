package dev.tingh.sharednote.integration;

import dev.tingh.sharednote.LockingEndpointManager;
import dev.tingh.sharednote.SharedNoteServerEndpoint;
import org.apache.commons.lang3.RandomStringUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;

public class LockingEndpointManagerBenchmark {

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(".*." + LockingEndpointManagerBenchmark.class.getSimpleName() + ".*")
                .warmupIterations(2)
                .measurementIterations(3)
                .forks(2)
                .build();
        new Runner(options).run();
    }

    @State(Scope.Benchmark)
    public static class EmptyManagerBenchmarkState {
        private final LockingEndpointManager manager = new LockingEndpointManager();

        private List<SharedNoteServerEndpoint> endpoints = new ArrayList<SharedNoteServerEndpoint>();
        private List<String> ids = new ArrayList<>();

        @Param({"1000"})
        public int size;

        @Setup(Level.Trial)
        public void setup() {
            for (int i = 0; i < size; i++) {
                endpoints.add(new SharedNoteServerEndpoint());
                ids.add(RandomStringUtils.random(12));
            }
        }
    }

    @State(Scope.Benchmark)
    public static class PopulatedUniqueIdManagerBenchmarkState {
        private final LockingEndpointManager manager = new LockingEndpointManager();
        private final SharedNoteServerEndpoint endpoint = new SharedNoteServerEndpoint();

        private List<String> ids = new ArrayList<>();

        @Param({"1000"})
        public int size;

        @Setup(Level.Trial)
        public void setup() {
            SharedNoteServerEndpoint endpoint = new SharedNoteServerEndpoint();
            for (int i = 0; i < size; i++) {
                String id = RandomStringUtils.random(12);
                ids.add(id);
                manager.add(id, endpoint);
            }
        }
    }

    @State(Scope.Benchmark)
    public static class PopulatedSameIdManagerBenchmarkState {
        private final LockingEndpointManager manager = new LockingEndpointManager();
        private final List<SharedNoteServerEndpoint> endpoints = new ArrayList<>();

        @Param({"1000"})
        public int size;

        @Setup(Level.Trial)
        public void setup() {
            for (int i = 0; i < size; i++) {
                SharedNoteServerEndpoint endpoint = new SharedNoteServerEndpoint();
                endpoints.add(endpoint);
                manager.add("test", endpoint);
            }
        }
    }

    @Benchmark
    public void addToSameId(EmptyManagerBenchmarkState state) {
        state.endpoints.stream().forEach(e -> state.manager.add("test", e));
    }

    @Benchmark
    public void addToSameIdParallel(EmptyManagerBenchmarkState state) {
        state.endpoints.parallelStream().forEach(e -> state.manager.add("test", e));
    }

    @Benchmark
    public void addUniqueId(EmptyManagerBenchmarkState state) {
        state.ids.stream().forEach(i -> state.manager.add(i, state.endpoints.get(0)));
    }

    @Benchmark
    public void addUniqueIdParallel(EmptyManagerBenchmarkState state) {
        state.ids.parallelStream().forEach(i -> state.manager.add(i, state.endpoints.get(0)));
    }

    @Benchmark
    public void removeFromSameId(PopulatedSameIdManagerBenchmarkState state) {
        state.endpoints.stream().forEach(e -> state.manager.remove("test", e));
    }

    @Benchmark
    public void removeFromSameIdParallel(PopulatedSameIdManagerBenchmarkState state) {
        state.endpoints.parallelStream().forEach(e -> state.manager.remove("test", e));
    }

    @Benchmark
    public void removeFromUniqueId(PopulatedUniqueIdManagerBenchmarkState state) {
        state.ids.stream().forEach(i -> state.manager.remove(i, state.endpoint));
    }

    @Benchmark
    public void removeFromUniqueIdParallel(
            PopulatedUniqueIdManagerBenchmarkState state) {
        state.ids.parallelStream().forEach(i -> state.manager.remove(i, state.endpoint));

    }

    @Benchmark
    public void iteratorForSameId(PopulatedSameIdManagerBenchmarkState state) {
        state.endpoints.stream().forEach(e -> state.manager.iterable("test"));
    }

    @Benchmark
    public void iteratorForSameIdParallel(PopulatedSameIdManagerBenchmarkState state) {
        state.endpoints.parallelStream().forEach(e -> state.manager.iterable("test"));
    }

    @Benchmark
    public void iteratorForUniqueId(PopulatedUniqueIdManagerBenchmarkState state) {
        state.ids.stream().forEach(i -> state.manager.iterable(i));
    }

    @Benchmark
    public void iteratorForUniqueIdParallel(
            PopulatedUniqueIdManagerBenchmarkState state) {
        state.ids.parallelStream().forEach(i -> state.manager.iterable(i));
    }
}
