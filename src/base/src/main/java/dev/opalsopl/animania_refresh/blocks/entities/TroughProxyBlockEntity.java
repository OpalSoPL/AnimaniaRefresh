package dev.opalsopl.animania_refresh.blocks.entities;

import dev.opalsopl.animania_refresh.AnimaniaRefresh;
import dev.opalsopl.animania_refresh.blocks.AllBlocks;
import dev.opalsopl.animania_refresh.blocks.TroughBlock;
import dev.opalsopl.animania_refresh.types.EContentType;
import dev.opalsopl.animania_refresh.types.ITroughEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class TroughProxyBlockEntity extends BlockEntity implements ITroughEntity {
    public TroughProxyBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlocks.TROUGH_PROXY_BE.get(), pos, blockState);
    }

    private ITroughEntity getNeighbour()
    {
        Direction facing = TroughBlock.getNeighbourDirection(getBlockState());

        assert level != null;
        BlockEntity entity = level.getBlockEntity(getBlockPos().relative(facing));

        if (entity instanceof ITroughEntity trough)
        {
            return trough;
        }
        AnimaniaRefresh.LOGGER.warn("Parent is missing. Child block of Trough was placed manually");
        return null;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        TroughBlockEntity parent = (TroughBlockEntity) getNeighbour();
        assert parent != null;
        return parent.getCapability(cap, side);
    }

    @Override
    public boolean isEmpty()
    {
        ITroughEntity trough = getNeighbour();
        assert trough != null;
        return trough.isEmpty();
    }

    @Override
    public int getSize() {
        ITroughEntity trough = getNeighbour();
        assert trough != null;
        return trough.getSize();
    }

    @Override
    public FluidTank getFluidTank() {
        ITroughEntity trough = getNeighbour();
        assert trough != null;
        return trough.getFluidTank();
    }

    @Override
    public IItemHandler getItemHandler() {
        ITroughEntity trough = getNeighbour();
        assert trough != null;
        return trough.getItemHandler();
    }

    @Override
    public EContentType getContentType() {
        ITroughEntity trough = getNeighbour();
        assert trough != null;
        return trough.getContentType();
    }
}
