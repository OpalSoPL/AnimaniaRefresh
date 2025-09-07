package dev.opalsopl.animania_refresh.blocks.entities;

import dev.opalsopl.animania_refresh.blocks.AllBlocks;
import dev.opalsopl.animania_refresh.types.EggStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class NestBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public final EggStackHandler nestContents = new EggStackHandler(this, 4);

    private final LazyOptional<IItemHandler> nestContentHandlerLazyOptional;

    public NestBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlocks.NEST_BE.get(), pos, state);

        nestContentHandlerLazyOptional = LazyOptional.of(() -> nestContents);
    }

    //Capabilities

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER && (side == Direction.DOWN || side == null))
        {
            return nestContentHandlerLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        nestContentHandlerLazyOptional.invalidate();
    }

    //Animation

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    //Networking

    @Override
    protected void saveAdditional(CompoundTag data) {
        super.saveAdditional(data);

        data.put("items", nestContents.serializeNBT());
    }

    @Override
    public void load(CompoundTag data) {
        super.load(data);

        nestContents.deserializeNBT(data.getCompound("items"));
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
