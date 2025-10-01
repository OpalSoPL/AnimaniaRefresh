package dev.opalsopl.animania_refresh.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockState;

public class StrawBlock extends CarpetBlock {

    public StrawBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean skipRendering(BlockState state1, BlockState state2, Direction direction) {
        return direction != Direction.UP;
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos pos) {
        boolean parent = super.canSurvive(blockState, levelReader, pos);

        BlockState below = levelReader.getBlockState(pos.below());

        return  below.isSolid() && !below.is(Blocks.GLASS) && !below.is(BlockTags.LEAVES) && parent;
    }
}
