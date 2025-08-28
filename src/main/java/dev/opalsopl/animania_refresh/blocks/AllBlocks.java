package dev.opalsopl.animania_refresh.blocks;

import dev.opalsopl.animania_refresh.AnimaniaRefresh;
import dev.opalsopl.animania_refresh.blocks.entities.TroughBlockEntity;
import dev.opalsopl.animania_refresh.fluid.AllFluids;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AnimaniaRefresh.MODID);

    public static final DeferredRegister<BlockEntityType<?>>  BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AnimaniaRefresh.MODID);

    public static final RegistryObject<LiquidBlock> SLOP_BLOCK = BLOCKS.register("slop",
            () -> new SlopLiquidBlock(AllFluids.SLOP_SOURCE_FLUID, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<StrawBlock> STRAW_BLOCK = BLOCKS.register("straw",
            () -> new StrawBlock(BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .sound(SoundType.CHERRY_LEAVES)));

    public static final RegistryObject<Block> TROUGH_BLOCK = BLOCKS.register("trough",
            () -> new TroughBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)
                    .noOcclusion()));

    //BE

    public static final RegistryObject<BlockEntityType<TroughBlockEntity>> TROUGH_BE = BLOCK_ENTITIES.register("trough_be",
            () -> BlockEntityType.Builder.of(TroughBlockEntity::new, TROUGH_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }
}
