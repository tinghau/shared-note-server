package dev.tingh.sharednote;

import com.google.gson.Gson;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<Message>{

    private static final Gson gson = new Gson();

    @Override
    public Message decode(String value) {
        return gson.fromJson(value, Message.class);
    }

    @Override
    public boolean willDecode(String value) {
        return value != null;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Do nothing
    }

    @Override
    public void destroy() {
        // Do nothing
    }
}
