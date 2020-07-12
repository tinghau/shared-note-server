package com.ting;

import com.ting.IEndpointManager;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public abstract class AbstractEndpointManagerTest {

    protected abstract IEndpointManager getEndpointManager();

    protected abstract Map<String, Set<SharedNoteServerEndpoint>> getIdToEndpoints();

    @Test
    public void testAdd() {
        SharedNoteServerEndpoint endpoint = mock(SharedNoteServerEndpoint.class);
        getEndpointManager().add("test", endpoint);

        assertEquals(getIdToEndpoints().get("test").size(), 1);
    }

    @Test
    public void testRemove() {
        SharedNoteServerEndpoint endpoint = mock(SharedNoteServerEndpoint.class);
        getEndpointManager().add("test", endpoint);
        getEndpointManager().remove("test", endpoint);

        assertEquals(getIdToEndpoints().get("test").size(), 0);
    }

    @Test
    public void testIterable() {
        SharedNoteServerEndpoint endpoint1 = mock(SharedNoteServerEndpoint.class);
        SharedNoteServerEndpoint endpoint2 = mock(SharedNoteServerEndpoint.class);
        getEndpointManager().add("test", endpoint1);
        getEndpointManager().add("test", endpoint2);

        Iterator<SharedNoteServerEndpoint> iter = getEndpointManager().iterable("test").iterator();

        assertTrue(getIdToEndpoints().get("test").contains(iter.next()));
        assertTrue(getIdToEndpoints().get("test").contains(iter.next()));
    }
}
