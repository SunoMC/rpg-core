package net.sunomc.rpg.core.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.yaml.snakeyaml.Yaml;

/**
 * Universal data storage with nested path support and special type handling.
 * Supports JSON/YAML serialization and automatic type conversion.
 */
public class Data {
    private final Map<String, Object> data = new HashMap<>();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Yaml yaml = new Yaml();

    /**
     * Stores a value at the specified path with automatic type handling.
     *
     * @param path The dot-separated path (e.g. "player.stats.health")
     * @param value The value to store (supports special type conversion)
     */
    public void set(String path, Object value) {
        if (path == null || path.isEmpty()) return;

        switch (value) {
            case UUID uuid -> setDefault(path, uuid.toString());
            case null -> remove(path);

            default -> setDefault(path, value);
        }
    }

    /**
     * Retrieves a value with automatic type conversion.
     *
     * @param path The dot-separated path to the value
     * @param type The expected return type class
     * @param defaultValue Fallback value if conversion fails
     * @return The converted value or defaultValue
     * @param <T> The expected return type
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String path, Class<T> type, T defaultValue) {
        Object value = getPathValue(path);
        if (value == null) return defaultValue;

        try {
            return switch (type) {
                case Class<?> uuidClass when uuidClass.isAssignableFrom(UUID.class) -> (T) UUID.fromString((String) value);
                default -> type.isInstance(value)
                        ? (T) value
                        : defaultValue;
            };
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Retrieves a value with automatic type conversion (null default).
     *
     * @param path The dot-separated path to the value
     * @param type The expected return type class
     * @return The converted value or null
     * @param <T> The expected return type
     */
    public <T> T get(String path, Class<T> type) {
        return get(path, type, null);
    }

    /**
     * Retrieves a subsection of the data as a new Data object.
     *
     * @param path Dot-separated path to the section (e.g. "player.stats")
     * @return A new Data object representing the section, or null if path is invalid
     */
    public @Nullable Data getSection(@NotNull String path) {
        Object section = getPathValue(path);
        if (section instanceof Map<?, ?> map) {
            Data newData = new Data();
            //noinspection unchecked
            newData.data.putAll((Map<String, Object>) map);
            return newData;
        }
        return null;
    }

    /**
     * Inserts a section from another Data object at the given path.
     *
     * @param path Dot-separated path to set the section (e.g. "player.stats")
     * @param section The Data object to insert
     */
    public void setSection(@NotNull String path, @NotNull Data section) {
        setDefault(path, section.data);
    }

    /**
     * Converts all data to a pretty-printed JSON string.
     *
     * @return JSON representation of the data
     */
    public String toJson() {
        return gson.toJson(data);
    }

    /**
     * Converts all data to a YAML string.
     *
     * @return YAML representation of the data
     */
    public String toYaml() {
        return yaml.dump(data);
    }


    /**
     * Internal path-based value retrieval.
     */
    public @Nullable Object getPathValue(@NotNull String path) {
        String[] parts = path.split("\\.");
        Map<String, Object> current = data;

        for (int i = 0; i < parts.length - 1; i++) {
            Object next = current.get(parts[i]);
            if (!(next instanceof Map)) return null;
            //noinspection unchecked
            current = (Map<String, Object>) next;
        }

        return current.get(parts[parts.length - 1]);
    }

    /**
     * Internal path-based value storage with nested map creation.
     */
    private void setDefault(@NotNull String path, Object value) {
        String[] parts = path.split("\\.");
        Map<String, Object> current = data;

        for (int i = 0; i < parts.length - 1; i++) {
            //noinspection unchecked
            current = (Map<String, Object>) current.computeIfAbsent(
                    parts[i], k -> new HashMap<String, Object>()
            );
        }

        current.put(parts[parts.length - 1], value);
    }

    /**
     * Removes a value at the specified path.
     */
    private void remove(@NotNull String path) {
        String[] parts = path.split("\\.");
        Map<String, Object> current = data;

        for (int i = 0; i < parts.length - 1; i++) {
            Object next = current.get(parts[i]);
            if (!(next instanceof Map)) return;
            //noinspection unchecked
            current = (Map<String, Object>) next;
        }

        current.remove(parts[parts.length - 1]);
    }

}