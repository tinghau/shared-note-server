package dev.tingh.sharednote;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertTrue;

public class MessageEncoderTest {

    private final MessageEncoder encoder = new MessageEncoder();

    @Test
    public void testEncode() {
        Block block = new Block("hello", 0);
        Message message = new Message(Collections.singletonList(block));
        String encoded = encoder.encode(message);

        assertTrue(encoded.contains("hello"));
    }
}
