package com.kondra.kos.codex;

import com.tccc.kos.commons.core.broker.MessageBroker;
import com.tccc.kos.commons.core.context.annotations.Autowired;
import com.tccc.kos.commons.core.service.AbstractConfigurableService;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CodexBasicService extends AbstractConfigurableService<CodexServiceConfig> {

    private static final String TOPIC_OBJECTS_ADDED = "/objects/added";
    private static final String TOPIC_OBJECTS_REMOVED = "/objects/removed";
    private static final String TOPIC_OBJECTS_MODIFIED = "/objects/modified";

    @Autowired
    private MessageBroker messageBroker;

    private final Map<Integer, TestObject> testObjects = new ConcurrentHashMap<>();

    public Collection<TestObject> getTestObjects() {
        return testObjects.values();
    }

    public void addObject(TestObject testObject) {
        testObjects.put(testObject.getId(), testObject);
        messageBroker.send(TOPIC_OBJECTS_ADDED, testObject);

        log.info("Added object {} to the list", testObject.getId());
    }

    public void modifyObject(TestObject testObject) {
        testObjects.put(testObject.getId(), testObject);
        messageBroker.send(TOPIC_OBJECTS_MODIFIED, testObject);

        log.info("Modified object {} from the list", testObject.getId());
    }

    public void removeObject(int id) {
        TestObject testObject = testObjects.remove(id);
        if(testObject != null) {
            messageBroker.send(TOPIC_OBJECTS_REMOVED, testObject);
            log.info("Removed object {} from the list", id);
        }else{
            log.warn("Could not find object with id {}", id);
        }
    }
}
