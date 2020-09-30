package dev.tingh.sharednote;

import org.eclipse.jetty.server.CustomRequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.ServerContainer;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.websocket.DeploymentException;
import java.io.IOException;

public class SharedNoteServer {

    private final Logger logger = LoggerFactory.getLogger(SharedNoteServer.class);

    public static void main(String[] args) {
        new SharedNoteServer().run();
    }

    private Server server;

    public void run() {
        server = getServer();
        server.addConnector(getServerConnector());

        ServletContextHandler context = getServletContextHandler();
        context.setContextPath("/");
        server.setHandler(context);

        server.setRequestLog(getRequestLog());

        addEndPoints(context);

        logger.info("Starting server");
        start();
    }

    private CustomRequestLog getRequestLog() {
        return new CustomRequestLog("./logs/jetty-request.log",
                CustomRequestLog.EXTENDED_NCSA_FORMAT);
    }

    protected ServletContextHandler getServletContextHandler() {
        return new ServletContextHandler(ServletContextHandler.SESSIONS);
    }

    protected ServerConnector getServerConnector() {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);

        return connector;
    }

    protected Server getServer() {
        return new Server();
    }

    private void addEndPoints(ServletContextHandler context) {
        try {
            ServerContainer container = getServerContainer(context);
            container.setDefaultMaxSessionIdleTimeout(1200000);
            container.addEndpoint(SharedNoteServerEndpoint.class);
        } catch (ServletException e) {
            logger.error("Cannot be configured", e);
        } catch (DeploymentException e) {
            logger.error("Annotated endpoint badly formed: " + SharedNoteServerEndpoint.class.getSimpleName(), e);
        }
    }

    protected ServerContainer getServerContainer(ServletContextHandler context) throws ServletException {
        return WebSocketServerContainerInitializer.configureContext(context);
    }

    private void start() {
        try {
            server.start();
            server.dump(System.err);
            server.join();
        } catch (InterruptedException e) {
            logger.error("Join failure", e);
        } catch (IOException e) {
            logger.error("Unable to append", e);
        } catch (Exception e) {
            logger.error("Failed to start", e);
        }
    }
}
