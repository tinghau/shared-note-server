package dev.tingh.sharednote;

import java.util.Map;
import java.util.Set;

public class LockingEndpointManagerTest extends AbstractEndpointManagerTest {

    private final LockingEndpointManager manager = new LockingEndpointManager();

    @Override
    protected IEndpointManager getEndpointManager() {
        return manager;
    }

    @Override
    protected Map<String, Set<SharedNoteServerEndpoint>> getIdToEndpoints() {
        return manager.idToEndpoints;
    }
}
