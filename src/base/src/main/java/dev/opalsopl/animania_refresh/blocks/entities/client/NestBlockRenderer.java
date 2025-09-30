package dev.opalsopl.animania_refresh.blocks.entities.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.opalsopl.animania_refresh.api.interfaces.IHatchableEgg;
import dev.opalsopl.animania_refresh.blocks.entities.NestBlockEntity;
import dev.opalsopl.animania_refresh.helper.ImageHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3i;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NestBlockRenderer extends GeoBlockRenderer<NestBlockEntity> {
    public NestBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new NestBlockModel());

        addRenderLayer(new EggRenderLayer(this));
    }

    public class EggRenderLayer extends GeoRenderLayer<NestBlockEntity>
    {

        public EggRenderLayer(GeoRenderer<NestBlockEntity> renderer) {
            super(renderer);
        }
    }

    @Override
    public void renderRecursively(PoseStack poseStack, NestBlockEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.getParent() == null || !bone.getParent().getName().equals("content") || bone.isHidden())
        {
            super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            return;
        }

        int eggSlot = parseEggId(bone.getName());

        if (eggSlot == -1)
        {
            super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, 248/255f, 0, 248/255f, alpha);
            return;
        }

        ItemStack stack = animatable.nestContents.getStackInSlot(eggSlot);

        if (!(stack.getItem() instanceof IHatchableEgg egg))
        {
            super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, 248/255f, 0, 248/255f, alpha);
            return;
        }

        Vector3i color = ImageHelper.intToVectorColor(egg.getTint());
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color.x/255f, color.y/255f, color.z/255f, alpha);
    }

    private int parseEggId(String name)
    {
        Matcher matcher = Pattern
                .compile("egg_(\\d*)")
                .matcher(name);

        if (!matcher.matches()) return -1;

        String id = matcher.group(1);
        return  Integer.decode(id);
    }
}
