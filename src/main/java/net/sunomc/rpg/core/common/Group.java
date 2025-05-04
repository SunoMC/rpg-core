package net.sunomc.rpg.core.common;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public record Group(
        String id,
        String prefix,
        String suffix,
        int weight,
        boolean isDefault,
        Set<String> permissions
) {
    public Group {
        if (permissions == null) {
            permissions = Set.of();
        }
    }

    @Contract(pure = true)
    public boolean hasPermission(@NotNull String requiredPermission) {
        return permissions.stream().anyMatch(perm -> matchesPermission(requiredPermission, perm));
    }

    @Contract(pure = true)
    private boolean matchesPermission(@NotNull String required, @NotNull String available) {
        if (available.equals(required)) {
            return true;
        }

        if (available.endsWith(".*")) {
            String parentPermission = available.substring(0, available.length() - 2);
            return required.startsWith(parentPermission + ".");
        }

        return false;
    }

}
