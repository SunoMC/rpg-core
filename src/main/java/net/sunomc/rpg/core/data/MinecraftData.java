package net.sunomc.rpg.core.data;

import java.util.*;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import io.papermc.paper.datacomponent.DataComponentTypes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import net.sunomc.rpg.core.RpgCore;

public class MinecraftData extends Data {

    /**
     * Stores a value at the specified path with automatic type handling.
     *
     * @param path The dot-separated path (e.g. "player.stats.health")
     * @param value The value to store (supports special type conversion)
     */
    @Override
    public void set(String path, Object value) {
        if (path == null || path.isEmpty()) return;

        switch (value) {
            case Location location -> setLocation(path, location);
            case ItemStack item -> setItemStack(path, item);
            case Inventory inventory -> setInventory(path, inventory);
            case World world -> setWorld(path, world);
            case BlockData blockData -> setBlockData(path, blockData);
            case Component component -> setComponent(path, component);
            default -> super.set(path, value);
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
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String path, Class<T> type, T defaultValue) {
        Object value = getPathValue(path);
        if (value == null) return defaultValue;

        try {
            return switch (type) {
                case Class<?> locClass when locClass.isAssignableFrom(Location.class) -> (T) getLocation(path);
                case Class<?> itemClass when itemClass.isAssignableFrom(ItemStack.class) -> (T) getItemStack(path);
                case Class<?> invClass when invClass.isAssignableFrom(Inventory.class) -> (T) getInventory(path);
                case Class<?> worldClass when worldClass.isAssignableFrom(World.class) -> (T) getWorld(path);
                case Class<?> blockDataClass when blockDataClass.isAssignableFrom(BlockData.class) -> (T) getBlockData(path);
                case Class<?> componentClass when componentClass.isAssignableFrom(Component.class) -> (T) getComponent(path);
                default -> super.get(path, type, defaultValue);
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

    /** Component Handling */
    private void setComponent(String path, @NotNull Component component) {
        String serialized = GsonComponentSerializer.gson().serialize(component);
        super.set(path, serialized);
    }

    @Nullable
    private Component getComponent(String path) {
        String json = super.get(path, String.class);
        if (json == null) return null;
        try {
            return GsonComponentSerializer.gson().deserialize(json);
        } catch (Exception e) {
            return null;
        }
    }

    /** Location Handling */
    private void setLocation(String path, @NotNull Location location) {
        setWorld(path + ".world", location.getWorld());
        super.set(path + ".x", location.getX());
        super.set(path + ".y", location.getY());
        super.set(path + ".z", location.getZ());
        super.set(path + ".yaw", location.getYaw());
        super.set(path + ".pitch", location.getPitch());
    }

    @Contract("_ -> new")
    private @NotNull Location getLocation(String path) {
        return new Location(
                Bukkit.getWorld(super.get(path + ".world", String.class)),
                super.get(path + ".x", Double.class),
                super.get(path + ".y", Double.class),
                super.get(path + ".z", Double.class),
                super.get(path + ".yaw", Float.class, 0.0f),
                super.get(path + ".pitch", Float.class, 0.0f)
        );
    }

    /**
     * ItemStack Handling
     */
    @SuppressWarnings("UnstableApiUsage")
    private void setItemStack(String path, @NotNull ItemStack item) {
        super.set(path + ".type", item.getType().name());
        super.set(path + ".amount", item.getAmount());
        super.set(path + ".durability", item.getData(DataComponentTypes.DAMAGE));

        if (item.getItemMeta() != null) {
            if (item.getItemMeta().hasDisplayName()) {
                setComponent(path + ".displayName", Objects.requireNonNull(item.getItemMeta().displayName()));
            }

            if (item.getItemMeta().hasLore()) {
                List<Component> lore = item.getItemMeta().lore();
                if (lore != null && !lore.isEmpty()) {
                    for (int i = 0; i < lore.size(); i++) {
                        setComponent(path + ".lore." + i, lore.get(i));
                    }
                    super.set(path + ".lore.size", lore.size());
                }
            }

            if (item.hasData(DataComponentTypes.CUSTOM_MODEL_DATA)) {
                super.set(path + ".customModelData", item.getData(DataComponentTypes.CUSTOM_MODEL_DATA));
            }

            super.set(path + ".unbreakable", item.getItemMeta().isUnbreakable());

            if (!item.getEnchantments().isEmpty()) {
                for (Map.Entry<Enchantment, Integer> enchant : item.getEnchantments().entrySet()) {
                    super.set(path + ".enchants." + enchant.getKey().getKey().getKey(), enchant.getValue());
                }
            }

            if (!Objects.requireNonNull(item.getItemMeta().getAttributeModifiers()).isEmpty()) {
                for (Map.Entry<Attribute, Collection<AttributeModifier>> entry :
                        item.getItemMeta().getAttributeModifiers().asMap().entrySet()) {
                    for (AttributeModifier modifier : entry.getValue()) {
                        String attributeKey = entry.getKey().getKey().getKey();
                        //noinspection deprecation
                        String modifierValue = modifier.getAmount() + ";" +
                                modifier.getOperation().name() + ";" +
                                Objects.requireNonNull(modifier.getSlot()).name();
                        super.set(path + ".attributes." + attributeKey, modifierValue);
                    }
                }
            }

            if (!item.getItemMeta().getPersistentDataContainer().getKeys().isEmpty()) {
                for (NamespacedKey key : item.getItemMeta().getPersistentDataContainer().getKeys()) {
                    PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
                    String keyStr = key.toString();

                    if (container.has(key, PersistentDataType.STRING)) {
                        super.set(path + ".pdc." + keyStr, container.get(key, PersistentDataType.STRING));
                    } else if (container.has(key, PersistentDataType.INTEGER)) {
                        super.set(path + ".pdc." + keyStr, container.get(key, PersistentDataType.INTEGER));
                    } else if (container.has(key, PersistentDataType.DOUBLE)) {
                        super.set(path + ".pdc." + keyStr, container.get(key, PersistentDataType.DOUBLE));
                    } else if (container.has(key, PersistentDataType.BYTE)) {
                        super.set(path + ".pdc." + keyStr, container.get(key, PersistentDataType.BYTE));
                    } else if (container.has(key, PersistentDataType.LONG)) {
                        super.set(path + ".pdc." + keyStr, container.get(key, PersistentDataType.LONG));
                    } else if (container.has(key, PersistentDataType.FLOAT)) {
                        super.set(path + ".pdc." + keyStr, container.get(key, PersistentDataType.FLOAT));
                    } else if (container.has(key, PersistentDataType.BOOLEAN)) {
                        super.set(path + ".pdc." + keyStr, container.get(key, PersistentDataType.BOOLEAN));
                    }
                }
            }
        }
    }

    @Nullable
    private ItemStack getItemStack(String path) {
        String type = super.get(path + ".type", String.class);
        if (type == null) return null;

        try {
            ItemStack item = new ItemStack(org.bukkit.Material.valueOf(type));
            item.setAmount(super.get(path + ".amount", Integer.class, 1));
            //noinspection deprecation
            item.setDurability(super.get(path + ".durability", Short.class, (short) 0));

            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                Component displayName = getComponent(path + ".displayName");
                if (displayName != null) {
                    meta.displayName(displayName);
                }

                Integer loreSize = super.get(path + ".lore.size", Integer.class);
                if (loreSize != null && loreSize > 0) {
                    List<Component> lore = new ArrayList<>();
                    for (int i = 0; i < loreSize; i++) {
                        Component loreComponent = getComponent(path + ".lore." + i);
                        if (loreComponent != null) {
                            lore.add(loreComponent);
                        }
                    }
                    meta.lore(lore);
                }

                Integer customModelData = super.get(path + ".customModelData", Integer.class);
                if (customModelData != null) //noinspection deprecation
                    meta.setCustomModelData(customModelData);

                Boolean unbreakable = super.get(path + ".unbreakable", Boolean.class, false);
                meta.setUnbreakable(unbreakable);

                item.setItemMeta(meta);

                Map<String, Integer> enchants = getEnchantmentMap(path + ".enchants");
                if (enchants != null) {
                    for (Map.Entry<String, Integer> entry : enchants.entrySet()) {
                        @SuppressWarnings("deprecation")
                        Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(entry.getKey()));
                        if (enchantment != null) {
                            item.addUnsafeEnchantment(enchantment, entry.getValue());
                        }
                    }
                }

                Map<String, String> attributes = getAttributeMap(path + ".attributes");
                if (attributes != null && !attributes.isEmpty()) {
                    meta = item.getItemMeta();
                    if (meta != null) {
                        for (Map.Entry<String, String> entry : attributes.entrySet()) {
                            String[] parts = entry.getValue().split(";");
                            if (parts.length == 3) {
                                try {
                                    double amount = Double.parseDouble(parts[0]);
                                    AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(parts[1]);

                                    Attribute attribute = Registry.ATTRIBUTE.getOrThrow(new NamespacedKey(RpgCore.getInstance(), entry.getKey()));

                                    NamespacedKey modifierKey = new NamespacedKey(RpgCore.getInstance(),
                                            entry.getKey() + "_" + UUID.randomUUID().toString().substring(0, 8));

                                    AttributeModifier modifier = new AttributeModifier(
                                            modifierKey, amount, operation);

                                    meta.addAttributeModifier(attribute, modifier);

                                } catch (IllegalArgumentException ignored) {
                                    // Skip invalid attribute
                                }
                            }
                        }
                        item.setItemMeta(meta);
                    }
                }

                Map<String, Object> pdcMap = getPersistentDataMap(path + ".pdc");
                if (pdcMap != null && !pdcMap.isEmpty()) {
                    meta = item.getItemMeta();
                    if (meta != null) {
                        PersistentDataContainer container = meta.getPersistentDataContainer();

                        for (Map.Entry<String, Object> entry : pdcMap.entrySet()) {
                            String[] keyParts = entry.getKey().split(":");
                            if (keyParts.length != 2) continue;

                            NamespacedKey namespacedKey = new NamespacedKey(keyParts[0], keyParts[1]);
                            Object value = entry.getValue();

                            // Store based on value type
                            if (value instanceof String) {
                                container.set(namespacedKey, PersistentDataType.STRING, (String) value);
                            } else if (value instanceof Integer) {
                                container.set(namespacedKey, PersistentDataType.INTEGER, (Integer) value);
                            } else if (value instanceof Double) {
                                container.set(namespacedKey, PersistentDataType.DOUBLE, (Double) value);
                            } else if (value instanceof Byte) {
                                container.set(namespacedKey, PersistentDataType.BYTE, (Byte) value);
                            } else if (value instanceof Long) {
                                container.set(namespacedKey, PersistentDataType.LONG, (Long) value);
                            } else if (value instanceof Float) {
                                container.set(namespacedKey, PersistentDataType.FLOAT, (Float) value);
                            } else if (value instanceof Boolean) {
                                container.set(namespacedKey, PersistentDataType.BOOLEAN, (Boolean) value);
                            }
                        }

                        item.setItemMeta(meta);
                    }
                }
            }

            return item;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Helper method to get a map of enchantments from the data
     */
    @Nullable
    private Map<String, Integer> getEnchantmentMap(String path) {
        Map<String, Object> enchantsSection = getMapSection(path);
        if (enchantsSection == null) return null;

        Map<String, Integer> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : enchantsSection.entrySet()) {
            if (entry.getValue() instanceof Integer) {
                result.put(entry.getKey(), (Integer) entry.getValue());
            } else if (entry.getValue() != null) {
                try {
                    result.put(entry.getKey(), Integer.parseInt(entry.getValue().toString()));
                } catch (NumberFormatException ignored) {
                    // Skip invalid entries
                }
            }
        }

        return result;
    }

    /**
     * Helper method to get a map of attributes from the data
     */
    @Nullable
    private Map<String, String> getAttributeMap(String path) {
        Map<String, Object> attrSection = getMapSection(path);
        if (attrSection == null) return null;

        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : attrSection.entrySet()) {
            if (entry.getValue() != null) {
                result.put(entry.getKey(), entry.getValue().toString());
            }
        }

        return result;
    }

    /**
     * Helper method to get a map of persistent data from the data
     */
    @Nullable
    private Map<String, Object> getPersistentDataMap(String path) {
        return getMapSection(path);
    }

    /**
     * Helper method to get a map section from the data
     */
    @Nullable
    @SuppressWarnings("unchecked")
    private Map<String, Object> getMapSection(String path) {
        Object sectionObj = getPathValue(path);
        if (sectionObj instanceof Map) {
            return (Map<String, Object>) sectionObj;
        }
        return null;
    }

    /** Inventory Handling */
    private void setInventory(String path, @NotNull Inventory inventory) {
        super.set(path + ".size", inventory.getSize());
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null) {
                setItemStack(path + ".slots." + i, item);
            }
        }
    }

    @Nullable
    private Inventory getInventory(String path) {
        Integer size = super.get(path + ".size", Integer.class);
        if (size == null) return null;

        Inventory inventory = Bukkit.createInventory(null, size);
        for (int i = 0; i < size; i++) {
            ItemStack item = getItemStack(path + ".slot_" + i);
            if (item != null) {
                inventory.setItem(i, item);
            }
        }
        return inventory;
    }

    /** World Handling */
    private void setWorld(String path, @NotNull World world) {
        super.set(path + ".name", world.getName());
        super.set(path + ".uid", world.getUID().toString());
        super.set(path + ".environment", world.getEnvironment().name());
    }

    @Nullable
    private World getWorld(String path) {
        String name = super.get(path + ".name", String.class);
        return name != null ? Bukkit.getWorld(name) : null;
    }

    /** BlockData Handling */
    private void setBlockData(String path, @NotNull BlockData blockData) {
        super.set(path, blockData.getAsString());
    }

    @Nullable
    private BlockData getBlockData(String path) {
        String data = super.get(path + ".asString", String.class);
        return data != null ? Bukkit.createBlockData(data) : null;
    }
}