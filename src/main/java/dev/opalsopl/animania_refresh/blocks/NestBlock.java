package dev.opalsopl.animania_refresh.blocks;

import dev.opalsopl.animania_refresh.blocks.entities.NestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class NestBlock extends BaseEntityBlock {
    protected NestBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NestBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        if (!(level.getBlockEntity(pos) instanceof NestBlockEntity nest))
            return InteractionResult.PASS;

        int slot = nest.nestContents.getFirstSlotWithItem();
        if (slot == -1) return InteractionResult.PASS;

        ItemStack item = nest.nestContents.extractItem(slot, 1, false);

        if(!player.addItem(item) && !player.isCreative())
        {
            popResourceFromFace(level, pos, Direction.UP, item);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState blockState, boolean movedByPiston) {
        if (!(level.getBlockEntity(pos) instanceof NestBlockEntity nest)) return;

        for (int i = 0; i < nest.nestContents.getCurrentSize(); i++)
        {
            ItemStack item = nest.nestContents.getStackInSlot(i);
            popResourceFromFace(level, pos, Direction.UP, item);
        }

        super.onRemove(state, level, pos, blockState, movedByPiston);
    }
}
