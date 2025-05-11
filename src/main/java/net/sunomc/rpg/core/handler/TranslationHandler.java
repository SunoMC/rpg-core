package net.sunomc.rpg.core.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.sunomc.rpg.core.common.Language;
import net.sunomc.rpg.core.common.Translation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles loading and retrieving translations from JSON language files.
 * <p>
 * This class provides a caching mechanism for language files and supports
 * nested JSON structures for translations. If a requested translation
 * is not found, it falls back to using the translation key as the value.
 * </p>
 */
public final class TranslationHandler {
    private static final String DEFAULT_LANG_DIR = ".lang";
    private static final Map<String, JsonObject> langCache = new HashMap<>();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static String langPath;

    /**
     * Initializes the TranslationHandler with a custom path to language files.
     * @param path Custom path to language files directory (null to use default)
     */
    public TranslationHandler(String path) {
        TranslationHandler.langPath = path != null ? path : DEFAULT_LANG_DIR;
    }

    /**
     * Gets a translation using the default English language.
     * @param key The translation key to look up
     * @return Translation object containing the key and translated value
     */
    public static @NotNull Translation getTranslationFor(String key) {
        return getTranslationFromJson("messages", key, Language.of("en-en.json"));
    }

    /**
     * Gets a translation using a specific language.
     * @param key The translation key to look up
     * @param lang The language to use for translation
     * @return Translation object containing the key and translated value
     */
    @Contract("_, null -> new")
    public static @NotNull Translation getTranslationFor(String key, Language lang) {
        return getTranslationFromJson("messages", key, lang);
    }

    /**
     * Gets a translation from a specific JSON path using default English.
     * @param path The dot-notation path in the JSON structure
     * @param key The translation key to look up
     * @return Translation object containing the key and translated value
     */
    public static @NotNull Translation getTranslationFor(String path, String key) {
        return getTranslationFromJson(path, key, Language.of("en-en.json"));
    }

    /**
     * Gets a translation from a specific JSON path using a specified language.
     * @param path The dot-notation path in the JSON structure
     * @param key The translation key to look up
     * @param lang The language to use for translation
     * @return Translation object containing the key and translated value
     */
    @Contract("_, _, null -> new")
    public static @NotNull Translation getTranslationFor(String path, String key, Language lang) {
        return getTranslationFromJson(path, key, lang);
    }

    /**
     * Internal method to retrieve translation from JSON structure.
     * @param path The dot-notation path in the JSON structure (e.g., "category.subcategory")
     * @param key The translation key to find
     * @param lang The language object containing translations
     * @return Translation object containing the key and found value (or key as value if not found)
     */
    @Contract("_, _, null -> new")
    private static @NotNull Translation getTranslationFromJson(String path, String key, Language lang) {
        if (lang == null) {
            return new Translation(key, key);
        }

        try {
            JsonObject rootObject = loadLanguageFile(lang.id());
            if (rootObject == null) {
                return new Translation(key, key);
            }

            JsonElement currentElement = rootObject;
            if (path != null && !path.isEmpty()) {
                String[] pathParts = path.split("\\.");
                for (String part : pathParts) {
                    if (!currentElement.isJsonObject()) {
                        return new Translation(key, key);
                    }
                    currentElement = currentElement.getAsJsonObject().get(part);
                    if (currentElement == null || currentElement.isJsonNull()) {
                        return new Translation(key, key);
                    }
                }
            }

            if (!currentElement.isJsonObject()) {
                return new Translation(key, key);
            }

            JsonElement valueElement = currentElement.getAsJsonObject().get(key);
            if (valueElement == null || valueElement.isJsonNull()) {
                return new Translation(key, key);
            }

            if (valueElement.isJsonPrimitive() && valueElement.getAsJsonPrimitive().isString()) {
                return new Translation(key, valueElement.getAsString());
            } else {
                return new Translation(key, key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Translation(key, key);
        }
    }

    /**
     * Loads a language file from disk or cache.
     * <p>
     * If the requested language file doesn't exist, falls back to English.
     * If neither exists, returns null.
     * </p>
     * @param langCode The language code (e.g., "en-en")
     * @return JsonObject containing all translations or null if not found
     * @throws IOException If there's an error reading the file
     */
    public static @Nullable JsonObject loadLanguageFile(@NotNull String langCode) throws IOException {
        if (langCache.containsKey(langCode)) {
            return langCache.get(langCode);
        }

        String fullPath = Paths.get(langPath, langCode + ".json").toString();
        File langFile = new File(fullPath);

        if (!langFile.exists()) {
            String fallbackPath = Paths.get(langPath, "en-en.json").toString();
            File fallbackFile = new File(fallbackPath);

            if (!fallbackFile.exists()) {
                return null;
            }

            String content = new String(Files.readAllBytes(Paths.get(fallbackPath)));
            JsonObject rootObject = gson.fromJson(content, JsonObject.class);
            langCache.put(langCode, rootObject);
            return rootObject;
        }

        String content = new String(Files.readAllBytes(Paths.get(fullPath)));
        JsonObject rootObject = gson.fromJson(content, JsonObject.class);
        langCache.put(langCode, rootObject);
        return rootObject;
    }

    /**
     * Clears the language file cache.
     * <p>
     * Useful when language files are reloaded during runtime.
     * </p>
     */
    public static void clearCache() {
        langCache.clear();
    }
}