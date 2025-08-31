package dev.opalsopl.animania_refresh.types;

import net.minecraft.util.StringRepresentable;

public enum ESeedCarpetType implements StringRepresentable {
    WHEAT("wheat"),
    BEETROOT("beetroot"),
    MELON("melon"),
    PUMPKIN("pumpkin");

    private final String name;

    ESeedCarpetType(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
