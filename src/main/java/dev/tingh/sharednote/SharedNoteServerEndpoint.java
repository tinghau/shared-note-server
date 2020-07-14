package dev.tingh.sharednote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.stream.StreamSupport;

@ServerEndpoint(
        value="/shared/{id}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class )
public class SharedNoteServerEndpoint {

    private final static Logger logger = LoggerFactory.getLogger(SharedNoteServerEndpoint.class);

    protected Session session;
    // id -> shared end points
    protected static IEndpointManager manager = new CopyOnWriteEndpointManager();
    private static Versioner versioner = new Versioner();

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        this.session = session;

        manager.add(id, this);
        // Need to get session id from an app perspective.
        logger.info("Session=" + session.getId() + ", id=" + id + " connected");

        Message message = versioner.getMessage(id);
        try {
            session.getBasicRemote().sendObject(message);
        } catch (EncodeException e) {
            logger.error("Unable to encode message: " + message, e);
        } catch (IOException e) {
            logger.error("Unable to send: " + message, e);
        }
    }

    protected Versioner getVersioner() {
        return versioner;
    }

    @OnMessage
    public void onMessage(Session session, @PathParam("id") String id, Message message) {
        logger.info("Received id=" + id + ", message=" + message);

        broadcast(id, getVersioner().submit(id, message));
    }

    private static void broadcast(String id, Message message) {
        logger.info("Broadcasting id=" + id + ", message=" + message);

        Iterable<SharedNoteServerEndpoint> endpoints = manager.iterable(id);

        StreamSupport.stream(endpoints.spliterator(), true).forEach(sharedNoteServerEndpoint -> {
            synchronized (sharedNoteServerEndpoint) {
                try {
                    sharedNoteServerEndpoint.session.getBasicRemote().sendObject(message);
                } catch (EncodeException e) {
                    logger.error("Unable to encode message: " + message, e);
                } catch (IOException e) {
                    logger.error("Unable to send: " + message, e);
                }
            }
        });
    }

    @OnClose
    public void onClose(@PathParam("id") String id) {
        manager.remove(id, this);
    }

    @OnError
    public void onError(Throwable throwable) {
        logger.error("Error encountered", throwable);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SharedNoteServerEndpoint that = (SharedNoteServerEndpoint) o;

        return session != null ? session.equals(that.session) : that.session == null;
    }

    @Override
    public int hashCode() {
        return session != null ? session.hashCode() : 0;
    }
}
