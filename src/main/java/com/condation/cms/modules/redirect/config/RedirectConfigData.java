package com.condation.cms.modules.redirect.config;

import java.util.List;

/**
 *
 * @author t.marx
 */
public class RedirectConfigData {

	private List<RedirectRuleData> redirects;

	public List<RedirectRuleData> getRedirects() {
		return redirects;
	}

	public void setRedirects(List<RedirectRuleData> redirects) {
		this.redirects = redirects;
	}
}
