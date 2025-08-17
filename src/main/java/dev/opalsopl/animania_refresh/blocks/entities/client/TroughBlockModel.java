package dev.opalsopl.animania_refresh.blocks.entities.client;

import dev.opalsopl.animania_refresh.AnimaniaRefresh;
import dev.opalsopl.animania_refresh.blocks.entities.TroughBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

import java.util.Optional;

public class TroughBlockModel extends GeoModel<TroughBlockEntity> {
    @Override
    public ResourceLocation getModelResource(TroughBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(AnimaniaRefresh.MODID, "geo/trough.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TroughBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(AnimaniaRefresh.MODID, "textures/block/entity/block_trough.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TroughBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(AnimaniaRefresh.MODID, "animations/trough_fluid.animation.json");
    }

    @Override
    public void setCustomAnimations(TroughBlockEntity animatable, long instanceId, AnimationState<TroughBlockEntity> animationState) {
        Optional<GeoBone> fluidOptional = getBone("fluid");
        Optional<GeoBone> foodOptional = getBone("food");

        if (fluidOptional.isPresent() && foodOptional.isPresent())
        {
            fluidOptional.get().setHidden(animatable.isEmpty());
            foodOptional.get().setHidden(animatable.items.getStackInSlot(0).isEmpty());
        }
    }
}
