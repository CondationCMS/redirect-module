package com.condation.cms.modules.redirect.config;

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
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author t.marx
 */
public class RedirectRule {

	private final Pattern pattern;
	private final String newUri;
	private final boolean isFolderRedirect;
	private final int httpStatus;

	public RedirectRule(String oldUriPattern, String newUri, boolean isFolderRedirect, int httpStatus) {
		this.pattern = Pattern.compile(oldUriPattern);
		this.newUri = newUri;
		this.isFolderRedirect = isFolderRedirect;
		this.httpStatus = httpStatus;
	}

	public boolean match(String oldUri) {
		Matcher matcher = pattern.matcher(oldUri);
		return matcher.matches();
	}

	public Optional<String> rewrite(String oldUri) {
		Matcher matcher = pattern.matcher(oldUri);
		if (!matcher.matches()) {
			return Optional.empty();
		}
		if (isFolderRedirect) {
			String relativePath = oldUri.substring(matcher.end());
			return Optional.of(newUri + relativePath);
		} else {
			String result = matcher.replaceAll(newUri);
			return Optional.of(result);
		}
	}
}
