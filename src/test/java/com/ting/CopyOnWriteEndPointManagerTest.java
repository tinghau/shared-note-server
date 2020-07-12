package com.ting;

import com.ting.CopyOnWriteEndpointManager;
import com.ting.IEndpointManager;

import java.util.Map;
import java.util.Set;

public class CopyOnWriteEndPointManagerTest extends AbstractEndpointManagerTest {

    private final CopyOnWriteEndpointManager manager = new CopyOnWriteEndpointManager();

    @Override
    protected IEndpointManager getEndpointManager() {
        return manager;
    }

    @Override
    protected Map<String, Set<SharedNoteServerEndpoint>> getIdToEndpoints() {
        return manager.idToEndpoints;
    }
}
