package dev.tingh.sharednote;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TestLogger extends Logger {

    private List<String> infos = new ArrayList<>();
    private List<String> severes = new ArrayList<>();

    public TestLogger(String name) {
        super(name, null);
    }

    @Override
    public void info(String message) {
        infos.add(message);
    }

    @Override
    public void severe(String message) {
        severes.add(message);
    }
}
