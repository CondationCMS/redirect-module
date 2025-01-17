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
import com.condation.cms.api.extensions.HttpHandler;
import com.condation.cms.api.feature.features.RequestFeature;
import com.condation.cms.api.module.CMSRequestContext;
import com.condation.cms.modules.redirect.config.RedirectRule;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;

/**
 *
 * @author t.marx
 */
@RequiredArgsConstructor
public class RedirectHandler implements HttpHandler {

	private final CMSRequestContext requestContext;

	@Override
	public boolean handle(Request request, Response response, Callback callback) throws Exception {

		var uri = requestContext.get(RequestFeature.class).uri();
		if (!uri.startsWith("/")) {
			uri = "/" + uri;
		}

		Optional<RedirectRule> redirectRule = LifeCycleExtension.REDIRECT_CONFIG.findRedirect(uri);
		if (redirectRule.isPresent()) {
			Optional<String> rewritten = redirectRule.get().rewrite(uri);
			if (rewritten.isPresent()) {
				response.getHeaders().add(HttpHeader.LOCATION, rewritten.get());
				response.setStatus(redirectRule.get().getHttpStatus());
				callback.succeeded();
				return true;
			}
		}

		return false;
	}

}
