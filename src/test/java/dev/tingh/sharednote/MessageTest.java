package dev.tingh.sharednote;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MessageTest {

    @Test
    public void testGetVersionsString() {
        List<Block> blocks = new ArrayList<>();

        blocks.add(new Block("", 1));
        blocks.add(new Block("", 3));
        blocks.add(new Block("", 1));
        blocks.add(new Block("", 2));

        Message message = new Message(blocks);

        Assert.assertArrayEquals(new long[]{1L, 3L, 1L,2L}, message.getVersionsString());
    }
}
