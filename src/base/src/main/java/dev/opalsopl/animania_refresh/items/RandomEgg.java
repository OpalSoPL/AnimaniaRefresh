package dev.opalsopl.animania_refresh.items;

import dev.opalsopl.animania_refresh.api.registries.AnimaniaMobRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.jetbrains.annotations.Nullable;

public class RandomEgg extends ForgeSpawnEggItem {
    public RandomEgg(Properties props) {
        super(() -> EntityType.CHICKEN,0, 0, props);
    }

    @Override
    public EntityType<?> getType(@Nullable CompoundTag tag) {
        return AnimaniaMobRegistry.getRandomEntity();
    }

    @Override
    public int getColor(int pTintIndex) {
        return 0xFFFFFF;
    }
}
