package com.ting;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.ServerContainer;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SharedNoteServerTest {

    private final SharedNoteServer server = new MockSharedNoteServer();

    @Test
    public void testRun() throws Exception {
        server.run();

        verify(server.getServletContextHandler()).setContextPath("/");
        verify(server.getServerContainer(null)).addEndpoint(SharedNoteServerEndpoint.class);
        verify(server.getServer()).start();
    }

    private static final class MockSharedNoteServer extends SharedNoteServer {
        private Server server = mock(Server.class);
        private ServerConnector connector = mock(ServerConnector.class);
        private ServerContainer container = mock(ServerContainer.class);
        private ServletContextHandler contextHandler = mock(ServletContextHandler.class);

        @Override
        protected ServletContextHandler getServletContextHandler() {
            return contextHandler;
        }

        @Override
        protected ServerConnector getServerConnector() {
            return connector;
        }

        @Override
        protected Server getServer() {
            return server;
        }

        @Override
        protected ServerContainer getServerContainer(ServletContextHandler context) {
            return container;
        }
    }
}
