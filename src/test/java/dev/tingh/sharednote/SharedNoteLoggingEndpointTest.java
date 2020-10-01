package dev.tingh.sharednote;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.websocket.CloseReason;
import javax.websocket.Session;

import static org.apache.logging.log4j.Level.INFO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class SharedNoteLoggingEndpointTest {

    private static final ListAppender appender = new ListAppender(SharedNoteLoggingEndpoint.class.getName());

    private final SharedNoteLoggingEndpoint endpoint = new SharedNoteLoggingEndpoint();

    @BeforeClass
    public static void setupBeforeClass() {
        Logger logger = (Logger)LogManager.getLogger(SharedNoteLoggingEndpoint.class);
        logger.addAppender(appender);
        logger.setLevel(Level.ALL);
    }

    @Before
    public void setup() {
        appender.clear();
    }

    @Test
    public void testOnWebSocketConnect() {
        Session session = mock(Session.class);
        endpoint.onWebSocketConnect(session);

        LogEvent event = appender.getEvents().get(0);
        assertEquals(INFO, event.getLevel());
        assertTrue(event.getMessage().getFormattedMessage().contains("Socket connected"));
        assertTrue(event.getMessage().getFormattedMessage().contains("Session"));
    }

    @Test
    public void testOnWebSocketMessage() {
        Message message = new Message(new Block("hello", 1));
        endpoint.onWebSocketMessage(message);

        LogEvent event = appender.getEvents().get(0);
        assertEquals(INFO, event.getLevel());
        assertTrue(event.getMessage().getFormattedMessage().contains("Received message"));
        assertTrue(event.getMessage().getFormattedMessage().contains("Message"));
    }

    @Test
    public void testWebSocketClose() {
        CloseReason reason = mock(CloseReason.class);
        endpoint.onWebSocketClose(reason);

        LogEvent event = appender.getEvents().get(0);
        assertEquals(INFO, event.getLevel());
        assertTrue(event.getMessage().getFormattedMessage().contains("Socket closed"));
        assertTrue(event.getMessage().getFormattedMessage().contains("CloseReason"));
    }

    @Test
    public void testOnWebSocketError() {
        Exception exception = new Exception();
        endpoint.onWebSocketError(exception);

        LogEvent event = appender.getEvents().get(0);
        assertEquals(Level.ERROR, event.getLevel());
        assertTrue(event.getMessage().getFormattedMessage().contains("Socket error"));
    }
}
