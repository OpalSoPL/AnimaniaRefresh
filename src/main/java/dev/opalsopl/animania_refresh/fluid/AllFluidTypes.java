package dev.opalsopl.animania_refresh.fluid;

import dev.opalsopl.animania_refresh.AnimaniaRefresh;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;

public class AllFluidTypes {

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, AnimaniaRefresh.MODID);

    //assets and data for fluids
    public static final ResourceLocation SLOP_STILL_RL = ResourceLocation.fromNamespaceAndPath(AnimaniaRefresh.MODID,"block/slop_still");
    public static final ResourceLocation SLOP_FLOWING_RL = ResourceLocation.fromNamespaceAndPath(AnimaniaRefresh.MODID,"block/slop_flow");
    public static final ResourceLocation SLOP_OVERLAY_RL = ResourceLocation.parse("block/water_overlay");

    //register fluids
    public static RegistryObject<FluidType> SLOP_FLUID_TYPE = FLUID_TYPES.register("slop", () ->
        new BaseFluidType(SLOP_STILL_RL, SLOP_FLOWING_RL, SLOP_OVERLAY_RL, 0xFFFFFFFF, new Vector3f(0, 0, 0),
                FluidType.Properties.create().canSwim(false).lightLevel(15))
    );

    public static void register(IEventBus eventBus)
    {
        FLUID_TYPES.register(eventBus);
    }
}
