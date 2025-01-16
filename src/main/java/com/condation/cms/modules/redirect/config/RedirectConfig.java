package com.condation.cms.modules.redirect.config;

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
                    addRedirect(ruleData.getOldUriPattern(), ruleData.getNewUri(), ruleData.isFolderRedirect());
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }

    }

    public void addRedirect(String oldUriPattern, String newUri) {
        addRedirect(oldUriPattern, newUri, false);
    }

    public void addRedirect(String oldUriPattern, String newUri, boolean isFolderRedirect) {
        rules.add(new RedirectRule(oldUriPattern, newUri, isFolderRedirect));
    }

    public Optional<String> findRedirect(String oldUri) {
        for (RedirectRule rule : rules) {
            Optional<String> result = rule.match(oldUri);
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }
}
