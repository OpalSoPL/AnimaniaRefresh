package dev.opalsopl.animania_refresh.blocks.entities.client;

import dev.opalsopl.animania_refresh.blocks.entities.NestBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class NestBlockRenderer extends GeoBlockRenderer<NestBlockEntity> {
    public NestBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new NestBlockModel());
    }
}
