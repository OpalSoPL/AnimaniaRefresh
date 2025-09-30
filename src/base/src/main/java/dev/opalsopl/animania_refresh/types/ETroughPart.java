package dev.opalsopl.animania_refresh.types;

import net.minecraft.util.StringRepresentable;

public enum ETroughPart implements StringRepresentable {
    PARENT("parent"),
    CHILD("child");

    private final String name;

    ETroughPart(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
