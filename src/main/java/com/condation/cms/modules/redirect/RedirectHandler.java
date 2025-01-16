package com.condation.cms.modules.redirect;

import com.condation.cms.api.extensions.HttpHandler;
import com.condation.cms.api.feature.features.RequestFeature;
import com.condation.cms.api.module.CMSRequestContext;

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

		Optional<String> redirect = LifeCycleExtension.REDIRECT_CONFIG.findRedirect(uri);
		if (redirect.isPresent()) {
			response.getHeaders().add(HttpHeader.LOCATION, redirect.get());
			response.setStatus(301);
			callback.succeeded();
			return true;
		}

		return false;
	}

}
