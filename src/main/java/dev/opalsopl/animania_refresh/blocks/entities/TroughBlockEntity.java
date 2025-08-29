package dev.opalsopl.animania_refresh.blocks.entities;

import dev.opalsopl.animania_refresh.blocks.AllBlocks;
import dev.opalsopl.animania_refresh.helper.ResourceHelper;
import dev.opalsopl.animania_refresh.types.EContentType;
import dev.opalsopl.animania_refresh.types.ITroughEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TroughBlockEntity extends BlockEntity implements GeoBlockEntity, ITroughEntity {
    private static final TagKey<Item> TROUGH_FOODS = ItemTags.create(ResourceHelper.getModResourceLocation("trough_food"));
    private static final TagKey<Fluid> TROUGH_FLUIDS = FluidTags.create(ResourceHelper.getModResourceLocation("trough_fluids"));

    private final LazyOptional<IItemHandler> itemHandlerLazyOptional;
    private final LazyOptional<IFluidHandler> fluidHandlerLazyOptional;

    private AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private final FluidTank tank = new FluidTank(1000)//1 Bucket
    {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid().defaultFluidState().holder().is(TROUGH_FLUIDS) &&
                    items.getStackInSlot(0).isEmpty();
        }
    };

    private final ItemStackHandler items = new ItemStackHandler()
    {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return 3;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.is(TROUGH_FOODS) && tank.isEmpty();
        }
    };

    public TroughBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlocks.TROUGH_BE.get(), pos, state);

        itemHandlerLazyOptional = LazyOptional.of(() -> items);
        fluidHandlerLazyOptional = LazyOptional.of(() -> tank);
    }

    //Getters

    @Override
    public boolean isEmpty()
    {
        return tank.isEmpty() && items.getStackInSlot(0).isEmpty();
    }

    public int getSize()
    {
        if (isEmpty()) return 0;

        return !tank.isEmpty()
                ? tank.getFluidAmount() : items.getStackInSlot(0).getCount();
    }

    public FluidTank getFluidTank() {
        return tank;
    }

    public IItemHandler getItemHandler() {
        return items;
    }

    public EContentType getContentType() {
        if (isEmpty())
        {
            return EContentType.EMPTY;
        }
        if (!tank.isEmpty())
        {
            return EContentType.FLUID;
        }
        return EContentType.ITEM;
    }

    public ItemStack getContent() {
        return items.getStackInSlot(0);
    }

    //Capabilities

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
        {
            return itemHandlerLazyOptional.cast();
        }
        else if (cap == ForgeCapabilities.FLUID_HANDLER)
        {
            return fluidHandlerLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandlerLazyOptional.invalidate();
        fluidHandlerLazyOptional.invalidate();
    }

    //Animation

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "fluid_controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<GeoAnimatable> geoAnimatableAnimationState) {
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object blockEntity) {
        return GeoBlockEntity.super.getTick(blockEntity);
    }

    //Networking

    @Override
    protected void saveAdditional(CompoundTag data) {
        super.saveAdditional(data);

        data.put("tank", tank.writeToNBT(new CompoundTag()));
        data.put("items", items.serializeNBT());
    }

    @Override
    public void load(CompoundTag data) {
        super.load(data);

        tank.readFromNBT(data.getCompound("tank"));
        items.deserializeNBT(data.getCompound("items"));
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();

        saveAdditional(tag);
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
