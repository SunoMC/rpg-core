package net.sunomc.rpg.game.loot;

import java.util.Map;

public class LootPool {
    private final Map<Integer, RpgItem> map;

    public LootPool(Map<Integer, RpgItem> map) {
        this.map = map;
    }
}
