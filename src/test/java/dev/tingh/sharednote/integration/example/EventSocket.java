package dev.tingh.sharednote.integration.example;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ClientEndpoint
@ServerEndpoint(value="/events/")
public class EventSocket
{
    @OnOpen
    public void onWebSocketConnect(Session sess)
    {
        System.out.println("Socket Connected: " + sess);
    }

    @OnMessage
    public void onWebSocketText(String message)
    {
        System.out.println("Received TEXT message: " + message);
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason)
    {
        System.out.println("Socket Closed: " + reason);
    }

    @OnError
    public void onWebSocketError(Throwable cause)
    {
        cause.printStackTrace(System.err);
    }
}
