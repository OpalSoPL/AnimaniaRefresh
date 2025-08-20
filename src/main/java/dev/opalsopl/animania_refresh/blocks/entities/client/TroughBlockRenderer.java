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
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class TroughBlockRenderer extends GeoBlockRenderer<TroughBlockEntity> {
    public TroughBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new TroughBlockModel());

        this.addRenderLayer(new FluidRenderLayer(this));
    }



    public static class FluidRenderLayer extends GeoRenderLayer<TroughBlockEntity> {
        private final ResourceLocation OTHER_MATERIAL = ResourceHelper.GetModResource("textures/block/entity/trough_elements/other_fluid.png"); //background for items

        public FluidRenderLayer(GeoRenderer entityRendererIn) {
            super(entityRendererIn);
        }

        private ResourceLocation GetTexture (TroughBlockEntity animatable)
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

        @Override
        public void render(PoseStack poseStack, TroughBlockEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            ResourceLocation location = GetTexture(animatable);

            if (location == null) return;

            RenderType type = RenderType.entityCutout(location);

            getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, type,
                    bufferSource.getBuffer(type), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                    1, 1, 1, 1);
        }
    }
}
