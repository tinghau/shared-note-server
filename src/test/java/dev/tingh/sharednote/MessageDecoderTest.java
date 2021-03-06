package dev.tingh.sharednote;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MessageDecoderTest {

    private final MessageDecoder decoder = new MessageDecoder();

    @Test
    public void testWillDecode() {
        assertTrue(decoder.willDecode(""));
    }

    @Test
    public void testDecode() {
        String value = "{\"blocks\":[{\"text\":\"hello\",\"version\":0}]}";
        Message message = decoder.decode(value);

        Assert.assertEquals("hello", message.getBlocks().get(0).getText());
    }
}
