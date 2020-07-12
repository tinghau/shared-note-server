package com.ting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ClientEndpoint(
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class)
@ServerEndpoint(
        value="/events/",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class )
public class SharedNoteLoggingEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(SharedNoteLoggingEndpoint.class);

    @OnOpen
    public void onWebSocketConnect(Session session) {
        logger.info("Socket connected: " + session);
    }

    @OnMessage
    public void onWebSocketMessage(Message message) {
        logger.info("Received message: " + message);
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason) {
        logger.info("Socket closed: " + reason);
    }

    @OnError
    public void onWebSocketError(Throwable throwable) {
        logger.error("Socket error", throwable);
        throwable.printStackTrace(System.err);
    }

}
