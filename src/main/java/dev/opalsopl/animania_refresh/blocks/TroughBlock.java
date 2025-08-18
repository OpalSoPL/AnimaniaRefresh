package dev.opalsopl.animania_refresh.blocks;

import dev.opalsopl.animania_refresh.blocks.entities.TroughBlockEntity;
import dev.opalsopl.animania_refresh.helper.ResourceHelper;
import dev.opalsopl.animania_refresh.types.EContainerType;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class TroughBlock extends BaseEntityBlock {
    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 2.0D, 16.0D, 8.0D, 14.0D);
    private static final TagKey<Item> ANIMAL_BUCKETS_TAG = ItemTags.create(ResourceHelper.GetModResource("buckets_with_animals"));

    public TroughBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TroughBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        ItemStack held = player.getItemInHand(interactionHand);
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (level.isClientSide) return InteractionResult.SUCCESS;

        if (!(blockEntity instanceof TroughBlockEntity trough)) return InteractionResult.PASS;

        if (!trough.isEmpty())
        {
           if (trough.getContainerType() == EContainerType.fluid && held.is(Items.BUCKET))
           {
                return retrieveFluid(held, trough, player, interactionHand);
           }
           else if (trough.getContainerType() == EContainerType.item && held.isEmpty())
           {
               return retrieveFood(trough, player);
           }
        }

        if (held.getItem() instanceof BucketItem)
        {
            if (!trough.isEmpty() && trough.getContainerType() != EContainerType.fluid) return InteractionResult.PASS;

            return addFluid(held, trough, player, interactionHand);
        }
        else
        {
            return addFood(held, trough, player);
        }
    }

    private InteractionResult addFluid(ItemStack heldItem, TroughBlockEntity trough, Player player, InteractionHand hand)
    {
        if (heldItem.is(ANIMAL_BUCKETS_TAG)) return InteractionResult.PASS;

        if (!(heldItem.getItem() instanceof BucketItem bucket && bucket.getFluid() != Fluids.EMPTY))
            return InteractionResult.PASS;

        FluidStack fluid = new FluidStack(bucket.getFluid(), 1000);

        if (!trough.tank.getFluid().isFluidEqual(fluid) && !trough.tank.isEmpty())
            return InteractionResult.PASS;

        trough.tank.fill(fluid, IFluidHandler.FluidAction.EXECUTE);

        if (!player.isCreative())
        {
            player.setItemInHand(hand, new ItemStack(Items.BUCKET));
        }
        trough.setContainerType(EContainerType.fluid);
        return InteractionResult.CONSUME;
    }

    private InteractionResult retrieveFluid(ItemStack heldItem, TroughBlockEntity trough, Player player, InteractionHand hand)
    {
        if (trough.tank.isEmpty() || trough.tank.getFluidAmount() < 1000) return InteractionResult.PASS;

        FluidStack drained = trough.tank.drain(1000, IFluidHandler.FluidAction.EXECUTE);

        if (!player.isCreative())
        {
            if (heldItem.getCount() > 1)
            {
                heldItem.shrink(1);
                player.addItem(new ItemStack(drained.getFluid().getBucket()));
            }
            else
            {
                player.setItemInHand(hand, new ItemStack(drained.getFluid().getBucket()));
            }
        }
        trough.setContainerType(EContainerType.none);
        return InteractionResult.CONSUME;
    }

    private InteractionResult addFood(ItemStack heldItem, TroughBlockEntity trough, Player player)
    {
        ItemStack item = new ItemStack(heldItem.getItemHolder(), 1);

        ItemStack remaining = trough.items.insertItem(0, item, false);

        if (remaining.isEmpty())
        {
            if (!player.isCreative())
            {
                heldItem.shrink(1);
            }
            trough.setContainerType(EContainerType.item);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    private InteractionResult retrieveFood(TroughBlockEntity trough, Player player)
    {
        ItemStack item = trough.items.extractItem(0, 1, false);

        player.addItem(item);

        return InteractionResult.PASS;
    }
}
