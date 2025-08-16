package dev.opalsopl.animania_refresh.blocks.entities.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.opalsopl.animania_refresh.blocks.entities.TroughBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class TroughBlockRenderer extends GeoBlockRenderer<TroughBlockEntity> {
    public TroughBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new TroughBlockModel());

        this.addRenderLayer(new TroughBlockRenderLayer(this));
    }

    public static class TroughBlockRenderLayer extends GeoRenderLayer<TroughBlockEntity> {
        public TroughBlockRenderLayer(GeoRenderer entityRendererIn) {
            super(entityRendererIn);
        }

        @Override
        public void renderForBone(PoseStack poseStack, TroughBlockEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            super.renderForBone(poseStack, animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);

            if (bone.getName().equals("fluid"))
            {
                bone.setHidden(animatable.tank.isEmpty());
            }
        }
    }
}
