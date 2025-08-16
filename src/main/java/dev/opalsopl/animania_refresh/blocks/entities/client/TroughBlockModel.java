package dev.opalsopl.animania_refresh.blocks.entities.client;

import dev.opalsopl.animania_refresh.AnimaniaRefresh;
import dev.opalsopl.animania_refresh.blocks.entities.TroughBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

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
}
