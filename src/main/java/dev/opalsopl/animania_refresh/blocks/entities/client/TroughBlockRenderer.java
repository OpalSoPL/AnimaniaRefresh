package dev.opalsopl.animania_refresh.blocks.entities.client;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.opalsopl.animania_refresh.AnimaniaRefresh;
import dev.opalsopl.animania_refresh.blocks.entities.TroughBlockEntity;
import dev.opalsopl.animania_refresh.helper.ImageHelper;
import dev.opalsopl.animania_refresh.helper.ResourceHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector2i;
import org.joml.Vector3i;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.HashMap;
import java.util.Map;

public class TroughBlockRenderer extends GeoBlockRenderer<TroughBlockEntity> {
    private final static Map<String, Integer> avarageColorCache = new HashMap<>();

    private final ResourceLocation OTHER_MATERIAL = ResourceHelper.getModResourceLocation("textures/block/entity/trough_elements/other_fluid.png"); //background for items

    public TroughBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new TroughBlockModel());

        this.addRenderLayer(new FluidRenderLayer(this));
        this.addRenderLayer(new FoodRendererLayer(this));
    }

    private ResourceLocation getFluidTexture(TroughBlockEntity animatable) {
        if (!animatable.getFluidTank().isEmpty()) {
            ResourceLocation res = ForgeRegistries.FLUIDS.getKey(animatable.getFluidTank().getFluid().getFluid());

            return ResourceHelper.AddPathSufPref(ResourceHelper.ChangeNamespace(res, AnimaniaRefresh.MODID),
                    "textures/block/entity/trough_elements", "_fluid.png");
        } else if (!animatable.isEmpty()) {
            return OTHER_MATERIAL;
        }
        return null;
    }

    private ResourceLocation getFoodTexture(TroughBlockEntity animatable) {
        ItemStack item = animatable.getItemHandler().getStackInSlot(0);

        if (!item.isEmpty()) {
            ResourceLocation res = ForgeRegistries.ITEMS.getKey(item.getItem());

            return ResourceHelper.AddPathSufPref(ResourceHelper.ChangeNamespace(res, AnimaniaRefresh.MODID),
                    "textures/block/entity/trough_elements", "_food.png");
        }
        return null;
    }

    private int getTintColor(ResourceLocation location)
    {
        try
        {
            JsonObject json = ImageHelper.getTextureMetadata(ResourceHelper.AddPathSuffix(location, ".mcmeta"));

            if (json != null && json.has("animania_refresh_texture_overrides"))
            {
                JsonObject overrides = json.get("animania_refresh_texture_overrides").getAsJsonObject();

                String data = overrides.get("tint_color").getAsString();

                return Integer.decode(data);
            }

            return ImageHelper.getAverageColor(location, new Vector2i(), new Vector2i(15, 15), true);
        } catch (Exception e) {
            e.printStackTrace();
            return 0xFFFFFF;
        }
    }


    public class FluidRenderLayer extends GeoRenderLayer<TroughBlockEntity> {

        public FluidRenderLayer(GeoRenderer entityRendererIn) {
            super(entityRendererIn);
        }

        @Override
        public void render(PoseStack poseStack, TroughBlockEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            ResourceLocation location = getFluidTexture(animatable);

            if (location == null) return;

            RenderType type = RenderType.entityTranslucent(location);

            getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, type,
                    bufferSource.getBuffer(type), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                    1, 1, 1, 1);
        }
    }

    public class FoodRendererLayer extends GeoRenderLayer<TroughBlockEntity>
    {

        public FoodRendererLayer(GeoRenderer<TroughBlockEntity> entityRendererIn) {
            super(entityRendererIn);
        }

        @Override
        public void render(PoseStack poseStack, TroughBlockEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            ResourceLocation location = getFoodTexture(animatable);

            if (location == null) return;

            RenderType type = RenderType.entityTranslucent(location);

            getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, type,
                    bufferSource.getBuffer(type), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                    1, 1, 1, 1);
        }

    }

    @Override
    public void renderRecursively(PoseStack poseStack, TroughBlockEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {



        if (bone.getName().equals("fluid"))
        {
            if (animatable.getFluidTank().getFluid().getFluid().equals(Fluids.WATER) &&
                animatable.getLevel() != null)
            {

                int waterColor = animatable.getLevel().getBiome(animatable.getBlockPos()).value().getWaterColor();

                int r = (waterColor >> 16) & 0xFF;
                int g = (waterColor >> 8) & 0xFF;
                int b = waterColor & 0xFF;

                super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, r/255f, g/255f, b/255f, alpha);
                return;
            }
            else if (!animatable.getItemHandler().getStackInSlot(0).isEmpty() &&
                    animatable.getFluidTank().isEmpty())
            {
                ResourceLocation loc = getFoodTexture(animatable);

                assert loc != null;

                String key = loc.toString();
                int avgColor =  avarageColorCache.computeIfAbsent(key, k -> getTintColor(loc));

                Vector3i color = ImageHelper.intToVectorColor(avgColor);

                super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay,
                        color.x/255f, color.y/255f, color.z/255f, alpha);
                return;
            }
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
