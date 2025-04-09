package com.kondra.kos.codex;

import com.tccc.kos.commons.core.context.annotations.Autowired;
import com.tccc.kos.commons.core.vfs.VFSSource;
import com.tccc.kos.commons.kab.KabFile;
import com.tccc.kos.commons.util.resource.ClassLoaderResourceLoader;
import com.tccc.kos.core.service.app.SystemApplication;
import com.tccc.kos.core.service.browser.BrowserService;
import com.tccc.kos.core.service.region.XmlRegionFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CodexApplication extends SystemApplication<CodexAppConfig> {

    public static final String MOUNTPOINT_CODEXUI = "/codexui";
    private static final String KABTYPE_CODEXUI = "kos.ui";

    private String codexUrl;

    @Autowired
    private BrowserService browserService;

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

    @Override
    public void start() throws Exception {
        KabFile codexUiKab = getSection().getKabByType(KABTYPE_CODEXUI);
        if (codexUiKab != null) {
            VFSSource source = getVfs().mount(MOUNTPOINT_CODEXUI, codexUiKab);
            codexUrl = source.getFullPath("index.html");
        } else {
            log.error("No codexUi KAB found");
        }
    }

    @Override
    public void started() throws Exception {
        if (codexUrl != null) {
            browserService.goToUrl(codexUrl);
        }
    }

}
