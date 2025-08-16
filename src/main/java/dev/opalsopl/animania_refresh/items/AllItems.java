package dev.opalsopl.animania_refresh.items;

import dev.opalsopl.animania_refresh.AnimaniaRefresh;

import dev.opalsopl.animania_refresh.blocks.AllBlocks;
import dev.opalsopl.animania_refresh.fluid.AllFluids;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AnimaniaRefresh.MODID);

    public static final RegistryObject<Item> SLOP_BUCKET = ITEMS.register("slop_bucket", () ->
            new BucketItem(AllFluids.SLOP_SOURCE_FLUID,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<BlockItem> STRAW = ITEMS.register("straw", () ->
            new BlockItem(AllBlocks.STRAW_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> TROUGH = ITEMS.register("trough", () ->
            new BlockItem(AllBlocks.TROUGH_BLOCK.get(), new Item.Properties()));


    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
