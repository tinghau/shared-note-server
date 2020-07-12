package com.ting;

public class Block {

    private String text;
    private long version;

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
        return "com.ting.Block{" +
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
        return text != null ? text.equals(block.text) : block.text == null;
    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (int) (version ^ (version >>> 32));
        return result;
    }
}
