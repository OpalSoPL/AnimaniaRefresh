package dev.opalsopl.animania_refresh.api.interfaces;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public interface IAnimalBreed<T extends Entity> {
    IAnimalSpecie getSpecie();
    EntityType<T> getEntityType();
    Entity getDefaultEntity(Level level);

    Entity getMale(Level level);
    Entity getFemale(Level level);
    Entity getChild(Level level); //genderless
}
