package dev.tingh.sharednote;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;

public class SharedNoteClient {

    public static void main(String[] args) {
        new SharedNoteClient().run();
    }

    private final java.net.URI uri = URI.create("ws://localhost:8080/shared/test");

    public void run() {
        try {
            Session session = getSession();
            session.getBasicRemote().sendObject(new Message(new Block("hello", 1)));
            session.close();
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EncodeException e) {
            e.printStackTrace();
        }
    }

    protected Session getSession() throws DeploymentException, IOException {
        return ContainerProvider.getWebSocketContainer().connectToServer(SharedNoteLoggingEndpoint.class, uri);
    }
}
