package dev.tingh.sharednote;

import java.util.Objects;

public class Block {

    private final String text;
    private final long version;

    public Block(String text, long version) {
        this.text = text;
        this.version = version;
    }

    public String getText() {
        return text;
    }

    public long getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "Block{" +
                "text='" + text + '\'' +
                ", version=" + version +
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

        Block block = (Block) o;

        if (version != block.version) {
            return false;
        }
        return Objects.equals(text, block.text);
    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (int) (version ^ (version >>> 32));
        return result;
    }
}
