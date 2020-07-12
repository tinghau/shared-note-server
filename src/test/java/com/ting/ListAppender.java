package com.ting;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.util.ArrayList;
import java.util.List;

public class ListAppender extends AbstractAppender {

    private final List<LogEvent> events = new ArrayList<>();

    protected ListAppender(String name) {
        super(name, null, PatternLayout.createDefaultLayout(), true, Property.EMPTY_ARRAY);
    }

    @Override
    public void append(LogEvent event) {
        events.add(event);
    }

    public List<LogEvent> getEvents() {
        return events;
    }

    public void clear() {
        events.clear();;
    }
}
