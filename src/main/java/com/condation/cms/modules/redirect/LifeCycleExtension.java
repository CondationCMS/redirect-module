package com.condation.cms.modules.redirect;

import com.condation.cms.api.feature.features.CronJobSchedulerFeature;
import com.condation.cms.api.feature.features.DBFeature;
import com.condation.cms.api.module.CMSModuleContext;
import com.condation.cms.api.module.CMSRequestContext;
import com.condation.cms.api.scheduler.CronJobContext;
import com.condation.cms.modules.redirect.config.RedirectConfig;
import com.condation.modules.api.ModuleLifeCycleExtension;
import com.condation.modules.api.annotation.Extension;

@Extension(ModuleLifeCycleExtension.class)
public class LifeCycleExtension extends ModuleLifeCycleExtension<CMSModuleContext, CMSRequestContext> {

    public static RedirectConfig REDIRECT_CONFIG;
    

    @Override
    public void init() {

    }

    @Override
    public void activate() {

        var redirectFile = getContext().get(DBFeature.class).db().getFileSystem().resolve("config/redirects.yaml");

        REDIRECT_CONFIG = new RedirectConfig(redirectFile);

        getContext().get(CronJobSchedulerFeature.class)
                .cronJobScheduler().schedule("0/15 * * * * ? *", "", (CronJobContext jobContext) -> {
                    REDIRECT_CONFIG.update();
                });
    }

}
