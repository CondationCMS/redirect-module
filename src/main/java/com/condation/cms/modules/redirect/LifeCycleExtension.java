package com.condation.cms.modules.redirect;

/*-
 * #%L
 * redirect-module
 * %%
 * Copyright (C) 2024 - 2025 CondationCMS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.condation.cms.api.feature.features.CronJobSchedulerFeature;
import com.condation.cms.api.feature.features.DBFeature;
import com.condation.cms.api.module.SiteModuleContext;
import com.condation.cms.api.module.SiteRequestContext;
import com.condation.cms.api.scheduler.CronJobContext;
import com.condation.cms.modules.redirect.config.RedirectConfig;
import com.condation.modules.api.ModuleLifeCycleExtension;
import com.condation.modules.api.annotation.Extension;

@Extension(ModuleLifeCycleExtension.class)
public class LifeCycleExtension extends ModuleLifeCycleExtension<SiteModuleContext, SiteRequestContext> {

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
