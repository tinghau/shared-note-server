package dev.tingh.sharednote;

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
