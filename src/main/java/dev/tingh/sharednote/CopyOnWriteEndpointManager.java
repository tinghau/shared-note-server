package dev.tingh.sharednote;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class CopyOnWriteEndpointManager implements IEndpointManager {

    protected final ConcurrentHashMap<String, Set<SharedNoteServerEndpoint>> idToEndpoints =
            new ConcurrentHashMap<>();

    @Override
    public void add(String id, SharedNoteServerEndpoint endpoint) {
        idToEndpoints.putIfAbsent(id, new CopyOnWriteArraySet<>());
        idToEndpoints.compute(id, (s, endpoints) -> {
            endpoints.add(endpoint);
            return endpoints;
        });
    }

    @Override
    public void remove(String id, SharedNoteServerEndpoint endpoint) {
        idToEndpoints.computeIfPresent(id, (s, endpoints) -> {
            endpoints.remove(endpoint);
            return endpoints;
        });
    }

    @Override
    public Iterable<SharedNoteServerEndpoint> iterable(String id) {
        return idToEndpoints.get(id);
    }
}
