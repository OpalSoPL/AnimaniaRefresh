package dev.opalsopl.animania_refresh.types;

import dev.opalsopl.animania_refresh.blocks.entities.NestBlockEntity;
import dev.opalsopl.animania_refresh.helper.ResourceHelper;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class EggStackHandler extends ItemStackHandler {
    private static final TagKey<Item> EGGS = ItemTags.create(ResourceHelper.getModResourceLocation("forge","eggs"));
    private final NestBlockEntity parent;

    public EggStackHandler(NestBlockEntity parent, int size) {
        super(size);
        this.parent = parent;
    }

    @Override
    protected void onContentsChanged(int slot)
    {
        parent.setChanged();
        Level level = parent.getLevel();
        BlockState state = parent.getBlockState();

        if (level != null) {
            level.sendBlockUpdated(parent.getBlockPos(), state, state, 3);
        }
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return 1;
    }

    public boolean isEmpty()
    {
        return stacks.stream().allMatch(ItemStack::isEmpty);
    }

    public int getCurrentSize()
    {
        return stacks.size();
    }

    public Map<Integer, ItemStack> getFilledSlots()
    {
        Map<Integer, ItemStack> items = new HashMap<>();
        int max = Math.min(4, stacks.size());

        for (int i = 0; i < max; i++)
        {
            ItemStack stack = stacks.get(i);

            if (stack.isEmpty()) continue;
            items.put(i, stack);
        }

        return items;
    }

    public int getFirstSlotWithItem()
    {
        for (int i = 0; i < getSlots(); i++)
        {
            if (!getStackInSlot(i).isEmpty()) return i;
        }
        return -1;
    }


    //note: DEBUG, remove later
    public int getFirstSlotWithItem(Item item)
    {
        for (int i = 0; i < getSlots(); i++)
        {
            if (getStackInSlot(i).is(item)) return i;
        }
        return -1;
    }

    public int getFirstEmptySlot()
    {
        for (int i = 0; i < getSlots(); i++)
        {
            if (getStackInSlot(i).isEmpty()) return i;
        }
        return -1;
    }
    //note: DEBUG END

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack)
    {
        return stack.is(EGGS);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
    {
        return stack;
    }

    public @NotNull ItemStack animalInsert(int slot, @NotNull ItemStack stack, boolean simulate)
    {
        return super.insertItem(slot, stack, simulate);
    }
}
