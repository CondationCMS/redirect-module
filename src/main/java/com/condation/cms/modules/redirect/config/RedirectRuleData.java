package com.condation.cms.modules.redirect.config;

/**
 *
 * @author t.marx
 */
public class RedirectRuleData {

	private String oldUriPattern;
	private String newUri;
	private boolean isFolderRedirect = false;

	public String getOldUriPattern() {
		return oldUriPattern;
	}

	public void setOldUriPattern(String oldUriPattern) {
		this.oldUriPattern = oldUriPattern;
	}

	public String getNewUri() {
		return newUri;
	}

	public void setNewUri(String newUri) {
		this.newUri = newUri;
	}

	public boolean isFolderRedirect() {
		return isFolderRedirect;
	}

	public void setFolderRedirect(boolean folderRedirect) {
		isFolderRedirect = folderRedirect;
	}
}
