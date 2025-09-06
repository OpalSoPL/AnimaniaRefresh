package dev.opalsopl.animania_refresh.blocks.entities.client;

import dev.opalsopl.animania_refresh.blocks.entities.NestBlockEntity;
import dev.opalsopl.animania_refresh.helper.ResourceHelper;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class NestBlockModel extends GeoModel<NestBlockEntity> {
    @Override
    public ResourceLocation getModelResource(NestBlockEntity animatable) {
        return ResourceHelper.getModResourceLocation("geo/nest.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(NestBlockEntity animatable) {
        return ResourceHelper.getModResourceLocation("textures/block/entity/block_nest_white.png");
    }

    @Override
    public ResourceLocation getAnimationResource(NestBlockEntity animatable) {
        return null;
    }
}
