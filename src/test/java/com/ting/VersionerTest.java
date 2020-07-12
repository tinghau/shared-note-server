package com.ting;

import com.ting.Block;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class VersionerTest {

    private final Versioner versioner = new Versioner();

    @Test
    public void testSubmit() {
        Block block = new Block("hello", 1);
        Message message = new Message(block);

        assertEquals(message, versioner.submit("test", message));
    }

    @Test
    public void testGetMessage() {
        Block block = new Block("hello", 1);
        Message message = new Message(block);

        versioner.submit("test", message);
        assertEquals(message, versioner.getMessage("test"));
    }

    private Message newMessage(String[] strings, long[] versions) {
        List<Block> blocks = new ArrayList<>();

        for (int i=0; i<strings.length && i<versions.length; i++) {
            blocks.add(new Block(strings[i], versions[i]));
        }
        return new Message(blocks);
    }

    @Test
    public void testSubmitMerge() {
        Message oldMessage = newMessage(new String[]{"hello", "world", "how", "are", "you"}, new long[]{1, 1, 1, 1, 1});
        versioner.submit("test", oldMessage);

        Message newMessage = newMessage(new String[]{"bye", "alex", "how", "are", "you", "today"}, new long[]{3, 2, 1, 1, 1, 1});
        Message actual = versioner.submit("test", newMessage);

        Message expected = newMessage(new String[]{"hello", "alex", "how", "are", "you", "today"}, new long[]{1, 2, 1, 1, 1, 1});
        assertEquals(expected, actual);
    }
}
