package dev.tingh.sharednote;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LockingEndpointManager implements IEndpointManager {

    protected final ConcurrentHashMap<String, Set<SharedNoteServerEndpoint>> idToEndpoints =
            new ConcurrentHashMap<>();

    public synchronized void add(String id, SharedNoteServerEndpoint endpoint) {
        idToEndpoints.putIfAbsent(id, new HashSet<>());
        idToEndpoints.computeIfPresent(id, (s, endpoints) -> {
            endpoints.add(endpoint);
            return endpoints;
        });
    }

    public synchronized void remove(String id, SharedNoteServerEndpoint endpoint) {
        idToEndpoints.computeIfPresent(id, (s, endpoints) -> {
            endpoints.remove(endpoint);
            return endpoints;
        });
    }

    public Iterable<SharedNoteServerEndpoint> iterable(String id) {
        return new ArrayList<>(idToEndpoints.get(id));
    }
}
