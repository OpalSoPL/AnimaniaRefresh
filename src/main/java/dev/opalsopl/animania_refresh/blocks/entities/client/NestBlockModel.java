package dev.opalsopl.animania_refresh.blocks.entities.client;

import dev.opalsopl.animania_refresh.blocks.entities.NestBlockEntity;
import dev.opalsopl.animania_refresh.helper.ResourceHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

import java.util.Map;
import java.util.Optional;

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

    @Override
    public void setCustomAnimations(NestBlockEntity animatable, long instanceId, AnimationState<NestBlockEntity> animationState) {
        
        Map<Integer, ItemStack> content = animatable.nestContents.getFilledSlots();

        GeoBone[] eggs = getEggBones();

        content.forEach((slot, item) -> eggs[slot].setHidden(false));
    }
    
    public GeoBone[] getEggBones()
    {
        GeoBone[] bones = new GeoBone[4];

        for (int i = 0; i < 4; i++) {
            Optional<GeoBone> eggOptional = getBone(String.format("egg_%d", i));

            int finalI = i;
            eggOptional.ifPresent((bone) ->
            {
                bones[finalI] = bone;
                bone.setHidden(true);
            });
        }
        return bones;
    }
}
