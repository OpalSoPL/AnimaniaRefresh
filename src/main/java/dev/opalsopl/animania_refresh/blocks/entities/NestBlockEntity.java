package dev.opalsopl.animania_refresh.blocks.entities;

import dev.opalsopl.animania_refresh.AnimaniaRefresh;
import dev.opalsopl.animania_refresh.api.interfaces.IHatchableEgg;
import dev.opalsopl.animania_refresh.blocks.AllBlocks;
import dev.opalsopl.animania_refresh.types.EggStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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

import java.util.Map;
import java.util.Random;

public class NestBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public final EggStackHandler nestContents = new EggStackHandler(this, 4);
    public static Random random = new Random();

    private final LazyOptional<IItemHandler> nestContentHandlerLazyOptional;

    public NestBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlocks.NEST_BE.get(), pos, state);

        nestContentHandlerLazyOptional = LazyOptional.of(() -> nestContents);
    }

    private void tryToHatchEgg(int eggSlot)
    {
        if(random.nextInt(24000) < 1)
        {
            ItemStack egg = nestContents.extractItem(eggSlot, 1, false);

            if (egg.getItem() instanceof IHatchableEgg hatchableEgg)
            {
                hatchableEgg.hatch(level, getBlockPos().getCenter());
            }
        }
    }


    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T entity)
    {
        if (!(entity instanceof NestBlockEntity nest)) return;
        if (nest.nestContents.isEmpty() || level.isClientSide) return;

        Map<Integer, ItemStack> filled = nest.nestContents.getFilledSlots();
        filled.forEach((id, itemStack) -> nest.tryToHatchEgg(id));
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
