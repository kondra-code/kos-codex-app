package com.kondra.kos.codex;

import com.tccc.kos.commons.util.resource.ClassLoaderResourceLoader;
import com.tccc.kos.core.service.app.SystemApplication;
import com.tccc.kos.core.service.region.XmlRegionFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CodexApplication extends SystemApplication<CodexAppConfig> {

    @Override
    public void load() throws Exception {
        log.info("Starting Codex Application");
        getCtx().add(new CodexController());
        getCtx().add(new CodexBasicService());

        // load regions from xml
        XmlRegionFactory regionFactory = new XmlRegionFactory();
        regionFactory.addLoader(new ClassLoaderResourceLoader(getClass().getClassLoader()));
        regionFactory.load("regions.xml");
        // install regions
        installRegions(regionFactory.getRegions());
    }

}
