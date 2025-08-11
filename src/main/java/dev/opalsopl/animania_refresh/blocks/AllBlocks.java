package dev.opalsopl.animania_refresh.blocks;

import dev.opalsopl.animania_refresh.AnimaniaRefresh;
import dev.opalsopl.animania_refresh.fluid.AllFluids;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AnimaniaRefresh.MODID);

    public static final RegistryObject<LiquidBlock> SLOP_BLOCK = BLOCKS.register("slop",
            () -> new LiquidBlock(AllFluids.SLOP_SOURCE_FLUID, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }
}
