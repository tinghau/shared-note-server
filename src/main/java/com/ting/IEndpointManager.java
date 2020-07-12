package com.ting;

public interface IEndpointManager {
    void add(String id, SharedNoteServerEndpoint endpoint);

    void remove(String id, SharedNoteServerEndpoint endpoint);

    Iterable<SharedNoteServerEndpoint> iterable(String id);
}
