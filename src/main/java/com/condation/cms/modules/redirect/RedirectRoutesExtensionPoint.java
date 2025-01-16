package com.condation.cms.modules.redirect;

import com.condation.cms.api.extensions.HttpRoutesExtensionPoint;
import com.condation.cms.api.extensions.Mapping;
import com.condation.modules.api.annotation.Extension;
import org.eclipse.jetty.http.pathmap.PathSpec;

/**
 *
 * @author t.marx
 */
@Extension(HttpRoutesExtensionPoint.class)
public class RedirectRoutesExtensionPoint extends HttpRoutesExtensionPoint {

	@Override
	public Mapping getMapping() {
		var mapping = new Mapping();
		
		mapping.add(PathSpec.from("/*"), new RedirectHandler(getRequestContext()));
		
		return mapping;
	}
	
}
