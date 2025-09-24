package dev.opalsopl.animania_refresh.blocks;

import dev.opalsopl.animania_refresh.types.ESeedCarpetType;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class SeedCarpetBlock extends StrawBlock {
    public SeedCarpetBlock(Properties properties) {
        super(properties);
    }

    public static final EnumProperty<ESeedCarpetType> TYPE = EnumProperty.create("type", ESeedCarpetType.class);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter getter, BlockPos pos, BlockState state) {
        return switch (state.getValue(TYPE))
        {
            case WHEAT -> new ItemStack(Items.WHEAT_SEEDS);
            case BEETROOT -> new ItemStack(Items.BEETROOT_SEEDS);
            case MELON -> new ItemStack(Items.MELON_SEEDS);
            case PUMPKIN -> new ItemStack(Items.PUMPKIN_SEEDS);
        };
    }
}
