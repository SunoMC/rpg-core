package net.sunomc.rpg.core.common;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a translated text with placeholder support.
 * <p>
 * Placeholders must be in the format {@code %placeholder%} and will be replaced
 * by values from the format map. Other formats like {@code <placeholder>} will
 * not be processed.
 *
 * @param key The translation key used to identify this text
 * @param value The actual translated text with potential placeholders
 */
public record Translation(
        String key,
        String value) {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("%([a-zA-Z0-9_]+)%");

    /**
     * Returns the raw translated value without any placeholder processing.
     * @return The unmodified translation value
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Alias for {@link #toString()} - returns the raw translated value.
     * @return The unmodified translation value
     */
    public String toRawString() {
        return value;
    }


    public String format(String placeholder, String value) {
        return format(Map.of(placeholder, value));
    }

    /**
     * Replaces placeholders in the format {@code %placeholder%} with values from the provided map.
     * <p>
     * Example:
     * <pre>{@code
     * Translation t = new Translation("key", "Use %command%");
     * String formatted = t.format(Map.of("command", "/msg <player>"));
     * // Result: "Use /msg <player>"
     * }</pre>
     *
     * @param replacements Map of placeholder names (without % symbols) to their replacement values
     * @return The formatted string with placeholders replaced
     */
    public String format(Map<String, String> replacements) {
        if (replacements == null || replacements.isEmpty()) {
            return value;
        }

        StringBuilder result = new StringBuilder();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(value);

        while (matcher.find()) {
            String placeholder = matcher.group(1); // Gets the text between % %
            String replacement = replacements.getOrDefault(placeholder, matcher.group());
            // Only replace if found in map, otherwise keep original
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);

        return result.toString();
    }
}