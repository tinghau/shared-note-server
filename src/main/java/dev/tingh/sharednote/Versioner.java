package dev.tingh.sharednote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  Multiple endpoints will call into this Versioner.
 *  Need to lock the idToVersionsMap by the id.
 */
public class Versioner {

    private static final Logger logger = LoggerFactory.getLogger(Versioner.class.getName());

    private final Map<String, Message> idToMessage = new ConcurrentHashMap<>();
    private final Map<String, Lock> idToLocks = new ConcurrentHashMap<>();

    public Message submit(String id, Message newMessage) {
        idToLocks.putIfAbsent(id, new ReentrantLock());
        Lock lock = idToLocks.get(id);

        try {
            lock.lock();

            Message currentMessage = idToMessage.get(id);
            if (idToMessage.get(id) == null) {
               logger.info("New message=" + Arrays.toString(newMessage.getVersionsString()));
               idToMessage.put(id, newMessage);
            } else {
               logger.info("Current message=" + Arrays.toString(currentMessage.getVersionsString())
                       + ", new message=" + Arrays.toString(newMessage.getVersionsString()));
               mergeMessages(id, newMessage, currentMessage);
               logger.info("Merged version=" + Arrays.toString(currentMessage.getVersionsString()));
            }
            return idToMessage.get(id);
        } finally {
            lock.unlock();
        }
    }

    public Message getMessage(String id) {
        return idToMessage.getOrDefault(id, Message.EMPTY);
    }

    private void mergeMessages(String id, Message newMessage, Message currentMessage) {
        for (int i = 0; i < newMessage.getBlocks().size(); i++) {
            if (getNextVersion(id, i) == 0) {
                currentMessage.getBlocks().add(newMessage.getBlocks().get(i));
            }
            if (newMessage.getBlocks().get(i).getVersion() == getNextVersion(id, i) ) {
                currentMessage.getBlocks().set(i, newMessage.getBlocks().get(i));
            }
        }
    }

    private long getNextVersion(String id, int index) {
        if (index < idToMessage.get(id).getBlocks().size()) {
            return idToMessage.get(id).getBlocks().get(index).getVersion() + 1;
        }
        return 0;
    }
}
