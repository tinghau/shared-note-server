package dev.tingh.sharednote;

import org.junit.Test;

import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class SharedNoteClientTest {

    private final MockSharedNoteClient client = new MockSharedNoteClient();

    @Test
    public void testRun() throws IOException, EncodeException {
        client.run();

        verify(client.basic).sendObject(new Message(new Block("hello", 1)));
    }

    private static class MockSharedNoteClient extends SharedNoteClient {
        private RemoteEndpoint.Basic basic = mock(RemoteEndpoint.Basic.class);
        private Session session = mock(Session.class);


        public MockSharedNoteClient() {
            when(session.getBasicRemote()).thenReturn(basic);
        }

        @Override
        protected Session getSession() {
            return session;
        }
    }
}
