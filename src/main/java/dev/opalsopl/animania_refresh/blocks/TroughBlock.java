package dev.opalsopl.animania_refresh.blocks;

import dev.opalsopl.animania_refresh.blocks.entities.TroughBlockEntity;
import dev.opalsopl.animania_refresh.blocks.entities.TroughProxyBlockEntity;
import dev.opalsopl.animania_refresh.helper.ResourceHelper;
import dev.opalsopl.animania_refresh.types.EContentType;
import dev.opalsopl.animania_refresh.types.ETroughPart;
import dev.opalsopl.animania_refresh.types.ITroughEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class TroughBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final EnumProperty<ETroughPart> PART = EnumProperty.create("part", ETroughPart.class);

    private static final TagKey<Item> ANIMAL_BUCKETS_TAG = ItemTags.create(ResourceHelper.getModResourceLocation("buckets_with_animals"));

    public TroughBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(PART);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(PART, ETroughPart.PARENT);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue(PART) == ETroughPart.PARENT)
        {
            return new TroughBlockEntity(pos, state);
        }
        else
        {
            return new TroughProxyBlockEntity(pos, state);
        }

    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return switch (state.getValue(PART))
        {
            case PARENT -> RenderShape.ENTITYBLOCK_ANIMATED;
            case CHILD -> RenderShape.INVISIBLE;
        };
    }

    public static Direction getNeighbourDirection(BlockState state)
    {
        Direction facing = state.getValue(FACING);
        ETroughPart part = state.getValue(PART);

        return switch (part)
        {
            case PARENT -> facing.getClockWise();
            case CHILD -> facing.getCounterClockWise();
        };
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        Direction facing= state.getValue(FACING);
        return switch (facing)
        {
            case DOWN, UP -> super.getShape(state, getter, pos, context);
            case EAST, WEST -> Block.box(2.0D, 0.0D, 0.0D, 14.0D, 8.0D, 16.0D);
            case NORTH, SOUTH -> Block.box(0.0D, 0.0D, 2.0D, 16.0D, 8.0D, 14.0D);
        };
    }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide)
        {
            BlockEntity entity = level.getBlockEntity(pos);

            if (entity instanceof TroughBlockEntity trough)
            {
                ItemStack items = trough.getContent();

                popResource(level, pos, items);
            }

            Direction direction = getNeighbourDirection(oldState);
            level.setBlockAndUpdate(pos.relative(direction), Blocks.AIR.defaultBlockState());
        }

        super.onRemove(oldState, level, pos, newState, isMoving);
    }

    @Override
    public void onPlace(BlockState newState, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            Direction facing = newState.getValue(FACING);
            ETroughPart part = newState.getValue(PART);

            if (part == ETroughPart.PARENT) {
                level.setBlockAndUpdate(pos.relative(facing.getClockWise()),
                        defaultBlockState()
                                .setValue(FACING, facing)
                                .setValue(PART, ETroughPart.CHILD));
            }
        }

        super.onPlace(newState, level, pos, oldState, isMoving);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        BlockPos childPos = pos.relative(getNeighbourDirection(state));

        boolean isFloorSturdyChild = reader.getBlockState(childPos.below()).isFaceSturdy(reader, childPos.below(), Direction.UP);
        boolean isFloorSturdyParent = reader.getBlockState(pos.below()).isFaceSturdy(reader, childPos.below(), Direction.UP);
        boolean isBlockEmptyChild = reader.isEmptyBlock(childPos);

        return super.canSurvive(state, reader, pos) && isBlockEmptyChild && isFloorSturdyChild && isFloorSturdyParent;
    }

    //Interaction Handling
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        ItemStack held = player.getItemInHand(interactionHand);
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (level.isClientSide) return InteractionResult.SUCCESS;

        if (!(blockEntity instanceof ITroughEntity trough)) return InteractionResult.PASS;

        if (!trough.isEmpty())
        {
           if (trough.getContentType() == EContentType.FLUID && held.is(Items.BUCKET))
           {
                return retrieveFluid(held, trough, player, interactionHand);
           }
           else if (trough.getContentType() == EContentType.ITEM && held.isEmpty())
           {
               return retrieveFood(trough, player);
           }
        }

        if (held.getItem() instanceof BucketItem)
        {
            if (!trough.isEmpty() && trough.getContentType() != EContentType.FLUID) return InteractionResult.PASS;

            return addFluid(held, trough, player, interactionHand);
        }
        else
        {
            return addFood(held, trough, player);
        }
    }

    private InteractionResult addFluid(ItemStack heldItem, ITroughEntity trough, Player player, InteractionHand hand)
    {
        if (heldItem.is(ANIMAL_BUCKETS_TAG)) return InteractionResult.PASS;

        if (!(heldItem.getItem() instanceof BucketItem bucket && bucket.getFluid() != Fluids.EMPTY))
            return InteractionResult.PASS;

        FluidStack fluid = new FluidStack(bucket.getFluid(), 1000);
        FluidTank fluidTank = trough.getFluidTank();

        if (!fluidTank.getFluid().isFluidEqual(fluid) && !fluidTank.isEmpty())
            return InteractionResult.PASS;

        fluidTank.fill(fluid, IFluidHandler.FluidAction.EXECUTE);

        if (!player.isCreative())
        {
            player.setItemInHand(hand, new ItemStack(Items.BUCKET));
        }
        return InteractionResult.CONSUME;
    }

    private InteractionResult retrieveFluid(ItemStack heldItem, ITroughEntity trough, Player player, InteractionHand hand)
    {
        FluidTank fluidTank = trough.getFluidTank();
        if (fluidTank.isEmpty() || fluidTank.getFluidAmount() < 1000) return InteractionResult.PASS;

        FluidStack drained = fluidTank.drain(1000, IFluidHandler.FluidAction.EXECUTE);

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
        return InteractionResult.CONSUME;
    }

    private InteractionResult addFood(ItemStack heldItem, ITroughEntity trough, Player player)
    {
        ItemStack item = new ItemStack(heldItem.getItemHolder(), 1);
        IItemHandler itemHandler = trough.getItemHandler();

        if (item.isEmpty()) return InteractionResult.PASS;

        ItemStack remaining = itemHandler.insertItem(0, item, false);

        if (remaining.isEmpty())
        {
            if (!player.isCreative())
            {
                heldItem.shrink(1);
            }
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    private InteractionResult retrieveFood(ITroughEntity trough, Player player)
    {
        ItemStack item = trough.getItemHandler().extractItem(0, 1, false);
        if (!player.isCreative())
        {
            player.addItem(item);
        }
        return InteractionResult.CONSUME;
    }
}
