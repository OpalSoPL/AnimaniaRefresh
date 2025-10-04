package dev.opalsopl.animania_refresh.types;

import dev.opalsopl.animania_refresh.api.interfaces.IAnimalBreed;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public enum EGender {
    child,
    male,
    female;

    public Entity getEntity(IAnimalBreed<?> breed, Level level)
    {
        return switch (this) {
            case child -> breed.getChild(level);
            case male -> breed.getMale(level);
            case female -> breed.getFemale(level);
        };
    }
}
