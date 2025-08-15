package dev.opalsopl.animania_refresh.blocks;

import dev.opalsopl.animania_refresh.AnimaniaRefresh;
import dev.opalsopl.animania_refresh.fluid.AllFluids;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AnimaniaRefresh.MODID);

    public static final RegistryObject<LiquidBlock> SLOP_BLOCK = BLOCKS.register("slop",
            () -> new SlopLiquidBlock(AllFluids.SLOP_SOURCE_FLUID, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<StrawBlock> STRAW_BLOCK = BLOCKS.register("straw",
            () -> new StrawBlock(BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .sound(SoundType.CHERRY_LEAVES)));

    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }
}
