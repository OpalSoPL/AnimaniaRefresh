package dev.opalsopl.animania_refresh.blocks.entities.client;

import dev.opalsopl.animania_refresh.blocks.entities.TroughBlockEntity;
import dev.opalsopl.animania_refresh.helper.ResourceHelper;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

import java.util.Optional;

public class TroughBlockModel extends GeoModel<TroughBlockEntity> {
    @Override
    public ResourceLocation getModelResource(TroughBlockEntity animatable) {
        return ResourceHelper.GetModResource("geo/trough.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TroughBlockEntity animatable) {
        return ResourceHelper.GetModResource("textures/block/entity/block_trough.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TroughBlockEntity animatable) {
        return null;
    }

    @Override
    public void setCustomAnimations(TroughBlockEntity animatable, long instanceId, AnimationState<TroughBlockEntity> animationState) {
        Optional<GeoBone> fluidOptional = getBone("fluid");
        Optional<GeoBone> foodOptional = getBone("food");

        if (fluidOptional.isPresent() && foodOptional.isPresent())
        {
            fluidOptional.get().setHidden(animatable.isEmpty());
            foodOptional.get().setHidden(animatable.items.getStackInSlot(0).isEmpty());

            convertStateToAnimation(animatable, fluidOptional.get());
        }
    }

    private void convertStateToAnimation(TroughBlockEntity animatable, GeoBone fluidBone)
    {
        int size = animatable.getSize();

        switch (size)
        {
            case 1, 100 -> fluidBone.setPosY(-4.9f); //used -4.9f instead of -5 to fix Z-fighing
            case 200, 300 -> fluidBone.setPosY(-4);
            case 2, 400, 500 -> fluidBone.setPosY(-3);
            case 600, 700 -> fluidBone.setPosY(-2);
            case 3, 800, 900 -> fluidBone.setPosY(-1);
            default -> fluidBone.setPosY(0);
        }
    }
}
