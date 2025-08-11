package dev.opalsopl.animania_refresh.fluid;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import java.util.function.Consumer;

public class BaseFluidType extends FluidType {
    private final ResourceLocation stillTexture;
    private final ResourceLocation flowingTexture;
    private final ResourceLocation overlayTexture;
    private final int tintColor;
    private final Vector3f fogColor;
    private final float fogStartDist;
    private final float fogEndDist;

    public BaseFluidType(ResourceLocation StillTexture, ResourceLocation FlowingTexture, ResourceLocation OverlayTexture,
                         int TintColor, Vector3f FogColor, Properties properties)
    {
        this(StillTexture, FlowingTexture, OverlayTexture, TintColor, FogColor, 1f, 10f, properties);
    }

    public BaseFluidType(ResourceLocation StillTexture, ResourceLocation FlowingTexture, ResourceLocation OverlayTexture,
                         int TintColor, Vector3f FogColor, float FogStartDist, float FogEndDist, Properties properties)
    {
        super(properties);
        stillTexture = StillTexture;
        flowingTexture = FlowingTexture;
        overlayTexture = OverlayTexture;
        tintColor = TintColor;
        fogColor = FogColor;
        fogStartDist = FogStartDist;
        fogEndDist = FogEndDist;
     }

    public ResourceLocation getStillTexture() {
        return stillTexture;
    }

    public ResourceLocation getFlowingTexture() {
        return flowingTexture;
    }

    public ResourceLocation getOverlayTexture() {
        return overlayTexture;
    }

    public int getTintColor() {
        return tintColor;
    }

    public Vector3f getFogColor() {
        return fogColor;
    }

    public float getFogStartDist() {
        return fogStartDist;
    }

    public float getFogEndDist() {
        return fogEndDist;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer)
    {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return stillTexture;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return flowingTexture;
            }

            @Override
            public @Nullable ResourceLocation getOverlayTexture() {
                return overlayTexture;
            }

            @Override
            public int getTintColor() {
                return tintColor;
            }

            @Override
            public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance,
                                                    float darkenWorldAmount, Vector3f fluidFogColor) {
                return fogColor;
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick,
                                        float nearDistance, float farDistance, FogShape shape) {
                RenderSystem.setShaderFogStart(fogStartDist); //fog fade out starting distance
                RenderSystem.setShaderFogEnd(fogEndDist); //fog fade out end distance
            }
        });
    }
}
