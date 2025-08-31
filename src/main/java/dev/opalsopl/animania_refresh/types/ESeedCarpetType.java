package dev.opalsopl.animania_refresh.types;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

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

    public static ESeedCarpetType convert(ItemStack item)
    {
        if (item.is(Items.WHEAT_SEEDS))
        {
            return ESeedCarpetType.WHEAT;
        }
        else if (item.is(Items.PUMPKIN_SEEDS))
        {
            return ESeedCarpetType.PUMPKIN;
        }
        else if (item.is(Items.BEETROOT_SEEDS))
        {
            return ESeedCarpetType.BEETROOT;
        }
        else if (item.is(Items.MELON_SEEDS))
        {
            return ESeedCarpetType.MELON;
        }
        return null;
    }
}
