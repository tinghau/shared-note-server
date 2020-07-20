package dev.tingh.sharednote;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SharedNoteServerEndpointTest {

    private static ListAppender appender = new ListAppender(SharedNoteServerEndpoint.class.getName());

    private IEndpointManager manager;
    private Session session1;
    private Session session2;
    private MockSharedNoteServerEndpoint endpoint1;
    private MockSharedNoteServerEndpoint endpoint2;

    @BeforeClass
    public static void setupBeforeClass() {
        Logger logger = (Logger) LogManager.getLogger(SharedNoteServerEndpoint.class);
        logger.addAppender(appender);
        logger.setLevel(Level.ALL);
    }

    @Before
    public void setup() throws IOException {
        session1 = getSession("1");
        endpoint1 = new MockSharedNoteServerEndpoint();
        endpoint1.onOpen(session1, "test");

        session2 = getSession("2");
        endpoint2 = new MockSharedNoteServerEndpoint();
        endpoint2.onOpen(session2, "test");

        manager = mock(IEndpointManager.class);
        MockSharedNoteServerEndpoint.manager = manager;
        when(manager.iterable("test")).thenReturn(List.of(endpoint1, endpoint2));

        appender.clear();
    }

    private Session getSession(String id) {
        Session session = mock(Session.class);
        RemoteEndpoint.Basic basic = mock(RemoteEndpoint.Basic.class);

        when(session.getBasicRemote()).thenReturn(basic);
        when(session.getId()).thenReturn(id);

        return session;
    }

    @Test
    public void testOnOpen() throws IOException {
        endpoint1.onOpen(session1, "test");
        endpoint2.onOpen(session2, "test");

        verify(manager).add("test", endpoint1);
        verify(manager).add("test", endpoint2);
    }

    @Test
    public void testOnMessage() throws IOException, EncodeException {
        endpoint1.onOpen(session1, "test");
        endpoint2.onOpen(session2, "test");

        Message message = new Message(new Block("hello", 1));

        endpoint1.onMessage(session1, "test", message);

        verify(session1.getBasicRemote()).sendObject(message);
        verify(session2.getBasicRemote()).sendObject(message);
    }

    @Test
    public void testOnClose() {
        endpoint1.onClose("test");
        endpoint2.onClose("test");

        verify(manager).remove("test", endpoint1);
        verify(manager).remove("test", endpoint2);
    }

    @Test
    public void testOnError() {
        Exception throwable = new Exception();
        endpoint1.onError("test", throwable);

        LogEvent event = appender.getEvents().get(0);
        assertEquals(Level.ERROR, event.getLevel());
        assertTrue(event.getMessage().getFormattedMessage().contains("Error encountered"));
        assertEquals(throwable, event.getThrown());
    }


    private static final class MockSharedNoteServerEndpoint extends SharedNoteServerEndpoint {
        private Versioner versioner = new MockVersioner();

        @Override
        protected Versioner getVersioner() {
            return versioner;
        }
    }

    private static final class MockVersioner extends Versioner {

        @Override
        public Message submit(String id, Message message) {
            return message;
        }
    }
}
