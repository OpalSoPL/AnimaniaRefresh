package dev.opalsopl.animania_refresh.api.interfaces;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;


public interface IHatchableEgg {
    IAnimalBreed getBreed();
    Item getEgg();
    int getTint();

    default IAnimalSpecie getSpecie()
    {
        return getBreed().getSpecie();
    }

    default void hatch(Level level, Vec3 position)
    {
        Entity Hatchling = getBreed().getEntityType().create(level);
        Hatchling.moveTo(position);
        level.addFreshEntity(Hatchling);
    }
}
