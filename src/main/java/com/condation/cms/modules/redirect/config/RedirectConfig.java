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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.yaml.snakeyaml.Yaml;

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
			Yaml yaml = new Yaml();
			Map<String, Object> config = yaml.load(fis);
			List<Map<String, Object>> redirects = (List<Map<String, Object>>) config.getOrDefault("redirects", Collections.emptyList());
			for (var redirect : redirects) {
				addRedirect(
						(String) redirect.get("oldUriPattern"),
						(String) redirect.get("newUri"),
						(Boolean) redirect.getOrDefault("useRegex", false),
						(int) redirect.getOrDefault("httpStatus", 301));
			}
		} catch (Exception e) {
			log.error("", e);
		}

	}

	public void addRedirect(String oldUriPattern, String newUri, int httpStatus) {
		addRedirect(oldUriPattern, newUri, false, httpStatus);
	}

	public void addRedirect(String oldUriPattern, String newUri, boolean useRegex, int httpStatus) {
		rules.add(new RedirectRule(oldUriPattern, newUri, useRegex, httpStatus));
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
