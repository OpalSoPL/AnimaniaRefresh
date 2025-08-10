package dev.opalsopl.animania_refresh.fluid;

import dev.opalsopl.animania_refresh.AnimaniaRefresh;
import dev.opalsopl.animania_refresh.blocks.AllBlocks;
import dev.opalsopl.animania_refresh.items.AllItems;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, AnimaniaRefresh.MODID);

    //register fluids
    public static final RegistryObject<FlowingFluid> SLOP_SOURCE_FLUID = FLUIDS.register("slop_fluid",
            () -> new ForgeFlowingFluid.Source(AllFluids.SLOP_FLUID_PROPERTIES));

    public static final RegistryObject<FlowingFluid> SLOP_FLOWING_FLUID = FLUIDS.register("flowing_slop_fluid",
            () -> new ForgeFlowingFluid.Flowing(AllFluids.SLOP_FLUID_PROPERTIES));

    //fluid properties
    public static final ForgeFlowingFluid.Properties SLOP_FLUID_PROPERTIES
            = new ForgeFlowingFluid.Properties(AllFluidTypes.SLOP_FLUID_TYPE, SLOP_SOURCE_FLUID, SLOP_FLOWING_FLUID)
            .slopeFindDistance(2)
            .levelDecreasePerBlock(2)
            .block(AllBlocks.SLOP_BLOCK)
            .bucket(AllItems.SLOP_BUCKET);

    public static void register(IEventBus eventBus)
    {
        FLUIDS.register(eventBus);
    }
}
