package dev.opalsopl.animania_refresh.types;

import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;

public interface ITroughEntity {

    boolean isEmpty();
    int getSize();
    FluidTank getFluidTank();
    IItemHandler getItemHandler();
    EContentType getContentType();
}
