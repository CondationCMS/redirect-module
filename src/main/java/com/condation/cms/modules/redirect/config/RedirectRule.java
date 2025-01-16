package com.condation.cms.modules.redirect.config;

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

        public RedirectRule(String oldUriPattern, String newUri, boolean isFolderRedirect) {
            this.pattern = Pattern.compile(oldUriPattern);
            this.newUri = newUri;
            this.isFolderRedirect = isFolderRedirect;
        }

        public Optional<String> match(String oldUri) {
            Matcher matcher = pattern.matcher(oldUri);
            if (matcher.matches()) {
                if (isFolderRedirect) {
                    String relativePath = oldUri.substring(matcher.end());
                    return Optional.of(newUri + relativePath);
                } else {
                    String result = matcher.replaceAll(newUri);
                    return Optional.of(result);
                }
            }
            return Optional.empty();
        }
    }