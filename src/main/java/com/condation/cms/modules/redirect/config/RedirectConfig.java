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
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author t.marx
 */
@Slf4j
@RequiredArgsConstructor
public class RedirectConfig {

	private final List<RedirectRule> rules = new ArrayList<>();

	private final Path configFile;

	public void update() {
		try (FileInputStream fis = new FileInputStream(this.configFile.toFile())) {
			Yaml yaml = new Yaml(new Constructor(RedirectConfigData.class, new LoaderOptions()));
			RedirectConfigData data = yaml.load(fis);

			if (data.getRedirects() != null) {
				for (RedirectRuleData ruleData : data.getRedirects()) {
					addRedirect(
							ruleData.getOldUriPattern(), 
							ruleData.getNewUri(), 
							ruleData.isFolderRedirect(), 
							ruleData.getHttpStatus());
				}
			}
		} catch (Exception e) {
			log.error("", e);
		}

	}

	public void addRedirect(String oldUriPattern, String newUri, int httpStatus) {
		addRedirect(oldUriPattern, newUri, false, httpStatus);
	}

	public void addRedirect(String oldUriPattern, String newUri, boolean isFolderRedirect, int httpStatus) {
		rules.add(new RedirectRule(oldUriPattern, newUri, isFolderRedirect, httpStatus));
	}

	public Optional<RedirectRule> findRedirect(String oldUri) {
		for (RedirectRule rule : rules) {
			if (rule.match(oldUri)) {
				return Optional.of(rule);
			}
		}
		return Optional.empty();
	}
}
