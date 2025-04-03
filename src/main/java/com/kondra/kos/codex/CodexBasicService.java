package com.kondra.kos.codex;

import com.tccc.kos.commons.core.broker.MessageBroker;
import com.tccc.kos.commons.core.context.annotations.Autowired;
import com.tccc.kos.commons.core.service.AbstractConfigurableService;
import com.tccc.kos.commons.util.concurrent.future.FutureEvent;
import com.tccc.kos.commons.util.concurrent.future.FutureWork;
import com.tccc.kos.commons.util.concurrent.future.SequencedFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CodexBasicService extends AbstractConfigurableService<CodexServiceConfig> {

    private static final String TOPIC_OBJECTS = "/codex/objects";
    private static final String TOPIC_OBJECTS_ADDED = TOPIC_OBJECTS + "/added";
    private static final String TOPIC_OBJECTS_REMOVED = TOPIC_OBJECTS + "/removed";
    private static final String TOPIC_OBJECTS_MODIFIED = TOPIC_OBJECTS + "/modified";

    @Autowired
    private MessageBroker messageBroker;
    private final Map<Integer, TestObject> testObjects = new ConcurrentHashMap<>();

    /**
     * @return Collection<TestObject>
     */
    public Collection<TestObject> getTestObjects() {
        return testObjects.values();
    }

    /**
     * @param testObject TestObject
     */
    public void addObject(TestObject testObject) {
        int id = Math.abs(new Random().nextInt());
        testObject.setId(id);
        String desc = testObject.getDesc() + " " + id;
        testObject.setDesc(desc);

        testObjects.put(testObject.getId(), testObject);
        messageBroker.send(TOPIC_OBJECTS_ADDED, testObject);

        log.info("Added object {} to the list", testObject.getId());
    }

    /**
     * @param id int
     */
    public void modifyObject(int id) {
        TestObject testObject = testObjects.get(id);
        if (testObject != null) {
            String modifiedDesc = "modified " + testObject.getDesc();
            testObject.setDesc(modifiedDesc);
            testObjects.put(testObject.getId(), testObject);
            messageBroker.send(TOPIC_OBJECTS_MODIFIED, testObject);
            log.info("Modified object {} from the list", testObject.getId());
        } else {
            log.warn("Object not found with id {}, unable to modify", id);
        }
    }

    /**
     * @param id int
     */
    public void removeObject(int id) {
        TestObject testObject = testObjects.remove(id);
        if (testObject != null) {
            messageBroker.send(TOPIC_OBJECTS_REMOVED, testObject);
            log.info("Removed object {} from the list", id);
        } else {
            log.warn("Could not find object with id {}", id);
        }
    }

    /**
     * @param numOfItems int
     * @return FutureWork
     */
    public FutureWork getAdditionalData(int numOfItems) {
        SequencedFuture future = new SequencedFuture("sequencedFuture");
        future.setFailState(null);
        future.setProgress(0);

        List<TestObject> clientTestObjects = new ArrayList<>();
        future.getClientData().setData(clientTestObjects);

        future.add(new FutureWork("init", f -> {
            log.info("Adding object {} to the list", numOfItems);
            f.success();
        }));

        future.add(new FutureWork("addItems", f -> {
            for (int i = 1; i <= numOfItems; i++) {
                TestObject testObject = new TestObject();
                int id = Math.abs(new Random().nextInt());
                testObject.setId(id);
                testObject.setDesc("desc " + id);

                clientTestObjects.add(testObject);
                testObjects.put(testObject.getId(), testObject);

                future.setProgress((i * 100) / numOfItems);
                Thread.sleep(500);
            }
            f.success();
        }));

        future.append("postProcess", FutureEvent.COMPLETE, f -> {
            List<TestObject> clientDataRes = (List<TestObject>) f.getClientData().getData();
            log.info("clientDataRes: {}", clientDataRes);
        });

        return future;
    }
}
