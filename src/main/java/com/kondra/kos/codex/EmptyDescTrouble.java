package com.kondra.kos.codex;

import com.tccc.kos.commons.core.service.trouble.Trouble;
import com.tccc.kos.commons.util.concurrent.future.FutureEvent;
import com.tccc.kos.commons.util.concurrent.future.FutureWork;
import com.tccc.kos.commons.util.concurrent.future.SequencedFuture;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmptyDescTrouble extends Trouble {

    private final CodexBasicService codexBasicService;

    EmptyDescTrouble(CodexBasicService codexBasicService) {
        addLink(codexBasicService);
        this.codexBasicService = codexBasicService;
        resolvable();
    }

    @Override
    public FutureWork resolve() {
        SequencedFuture future = new SequencedFuture("TroubleResolveFuture");
        future.setFailState(null);
        future.setProgress(0);

        future.add(new FutureWork("initResolveTroubleFuture", f -> {
            log.info("Resolving Trouble");
            f.success();
        }));

        future.add(new FutureWork("addItem", f -> {
            Thread.sleep(1_000);
            future.setProgress(50);
            Thread.sleep(1_000);
            TestObject testObject = new TestObject();
            testObject.setDesc("resolved desc");
            codexBasicService.addObject(testObject);

            future.setProgress(100);
            f.success();
        }));

        future.append("postProcess", FutureEvent.COMPLETE, f -> {
            TestObject clientDataRes = (TestObject) f.getClientData().getData();
            log.info("clientDataRes: {}", clientDataRes);
        });

        return future;
    }
}
