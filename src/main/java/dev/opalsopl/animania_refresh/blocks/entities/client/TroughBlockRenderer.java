package dev.opalsopl.animania_refresh.blocks.entities.client;

import dev.opalsopl.animania_refresh.blocks.entities.TroughBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
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
    }
}
