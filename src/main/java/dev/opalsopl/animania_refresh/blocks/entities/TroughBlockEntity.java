package dev.opalsopl.animania_refresh.blocks.entities;

import dev.opalsopl.animania_refresh.blocks.AllBlocks;
import dev.opalsopl.animania_refresh.types.EContainerType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class TroughBlockEntity extends BlockEntity implements GeoBlockEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private EContainerType type = EContainerType.none;

    public FluidTank tank = new FluidTank(1000)//1 Bucket
    {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    };

    public ItemStackHandler items = new ItemStackHandler()
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
    };

    public TroughBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlocks.TROUGH_BE.get(), pos, state);
    }

    public void setContainerType(EContainerType type)
    {
        this.type = type;
        setChanged();

        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public EContainerType getContainerType()
    {
        return type;
    }

    public boolean isEmpty()
    {
        return tank.isEmpty() && items.getStackInSlot(0).isEmpty();
    }

    //Animation

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<GeoAnimatable> geoAnimatableAnimationState) {
        geoAnimatableAnimationState.getController().setAnimation(RawAnimation.begin());
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
        data.putString("type", type.name());
    }

    @Override
    public void load(CompoundTag data) {
        super.load(data);

        tank.readFromNBT(data.getCompound("tank"));
        items.deserializeNBT(data.getCompound("items"));

        try {
            type = EContainerType.valueOf(data.getString("type"));
        }
        catch (IllegalArgumentException e)
        {
            type = EContainerType.none;
        }
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
