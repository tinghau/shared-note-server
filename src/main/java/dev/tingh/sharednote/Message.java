package dev.tingh.sharednote;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

public class Message {

    public static final Message EMPTY = new Message(new ArrayList<>());

    private List<Block> blocks;

    public Message(List<Block> blocks) {
        if (blocks != null) {
            this.blocks = blocks;
        } else {
            this.blocks = new ArrayList<>();
        }
    }

    public Message(Block block) {
        this(singletonList(block));
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public long[] getVersionsString() {
        long[] versions = new long[blocks.size()];

        for (int i=0; i<blocks.size(); i++) {
            versions[i] = blocks.get(i).getVersion();
        }
        return versions;
    }

    @Override
    public String toString() {
        return "com.ting.Message{" +
                "blocks=" + blocks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Message message = (Message) o;

        return blocks.equals(message.blocks);
    }

    @Override
    public int hashCode() {
        return blocks.hashCode();
    }
}
