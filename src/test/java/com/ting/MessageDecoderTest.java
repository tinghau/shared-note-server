package com.ting;

import org.junit.Assert;
import org.junit.Test;

import javax.websocket.DecodeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MessageDecoderTest {

    private final MessageDecoder decoder = new MessageDecoder();

    @Test
    public void testWillDecode() {
        assertTrue(decoder.willDecode(""));
    }

    @Test
    public void testDecode() throws DecodeException {
        String value = "{\"blocks\":[{\"text\":\"hello\",\"version\":0}]}";
        Message message = decoder.decode(value);

        Assert.assertEquals("hello", message.getBlocks().get(0).getText());
    }
}
