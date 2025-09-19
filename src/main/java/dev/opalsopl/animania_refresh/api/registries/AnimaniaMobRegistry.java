package dev.opalsopl.animania_refresh.api.registries;

import net.minecraft.world.entity.EntityType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class AnimaniaMobRegistry {
    private static final List<EntityType<?>> ADDON_ENTITIES = new ArrayList<>();
    private static final Random random = new Random();

    public static void register(EntityType<?> entityType)
    {
        ADDON_ENTITIES.add(entityType);
    }

    @Nullable
    public static EntityType<?> getRandomEntity()
    {
        if (ADDON_ENTITIES.isEmpty()) return null;

        int idx = random.nextInt(0, ADDON_ENTITIES.size());
        return ADDON_ENTITIES.get(idx);
    }
}
