package dev.opalsopl.animania_refresh.items;

import dev.opalsopl.animania_refresh.AnimaniaRefresh;

import dev.opalsopl.animania_refresh.fluid.AllFluids;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AnimaniaRefresh.MODID);

    public static final RegistryObject<Item> SLOP_BUCKET = ITEMS.register("bucket_slop", () ->
            new BucketItem(AllFluids.SLOP_SOURCE_FLUID,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));


    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
