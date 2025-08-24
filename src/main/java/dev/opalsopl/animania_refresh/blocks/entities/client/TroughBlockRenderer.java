package dev.opalsopl.animania_refresh.blocks.entities.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.opalsopl.animania_refresh.AnimaniaRefresh;
import dev.opalsopl.animania_refresh.blocks.entities.TroughBlockEntity;
import dev.opalsopl.animania_refresh.helper.ResourceHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class TroughBlockRenderer extends GeoBlockRenderer<TroughBlockEntity> {
    private final ResourceLocation OTHER_MATERIAL = ResourceHelper.GetModResource("textures/block/entity/trough_elements/other_fluid.png"); //background for items

    public TroughBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new TroughBlockModel());

        this.addRenderLayer(new FluidRenderLayer(this));
    }

    private ResourceLocation GetFluidTexture(TroughBlockEntity animatable)
    {
        if (!animatable.tank.isEmpty())
        {
            ResourceLocation res = ForgeRegistries.FLUIDS.getKey(animatable.tank.getFluid().getFluid());

            return ResourceHelper.AddPathSufPref(ResourceHelper.ChangeNamespace(res, AnimaniaRefresh.MODID),
                    "textures/block/entity/trough_elements", "_fluid.png");
        }
        else if (!animatable.isEmpty())
        {
            return OTHER_MATERIAL;
        }
        return null;
    }



    public class FluidRenderLayer extends GeoRenderLayer<TroughBlockEntity> {

        public FluidRenderLayer(GeoRenderer entityRendererIn) {
            super(entityRendererIn);
        }

        @Override
        public void render(PoseStack poseStack, TroughBlockEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            ResourceLocation location = GetFluidTexture(animatable);

            if (location == null) return;

            RenderType type = RenderType.entityTranslucent(location);

            getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, type,
                    bufferSource.getBuffer(type), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                    1, 1, 1, 1);
        }
    }

    @Override
    public void renderRecursively(PoseStack poseStack, TroughBlockEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        if (!bone.getName().equals("fluid") && !animatable.tank.getFluid().getFluid().equals(Fluids.WATER))
        {
            super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            return;
        }

        if (animatable.getLevel() != null) return;

        int waterColor = animatable.getLevel().getBiome(animatable.getBlockPos()).value().getWaterColor();

        int r = (waterColor >> 16) & 0xFF;
        int g = (waterColor >> 8) & 0xFF;
        int b = waterColor & 0xFF;

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, r/255f, g/255f, b/255f, alpha);
    }
}
