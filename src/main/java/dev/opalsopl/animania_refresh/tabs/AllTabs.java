package dev.opalsopl.animania_refresh.tabs;

import dev.opalsopl.animania_refresh.AnimaniaRefresh;
import dev.opalsopl.animania_refresh.items.AllItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AllTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AnimaniaRefresh.MODID);

    public static final RegistryObject<CreativeModeTab> ENTITY_TAB = CREATIVE_TABS.register("entites_tab", ()
            -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> new ItemStack(Items.EGG))
            .title(Component.translatable("tab.animania_entities.label"))
            .build());

    public static final RegistryObject<CreativeModeTab> RESOURCES_TAB = CREATIVE_TABS.register("resources_tab", ()
            -> CreativeModeTab.builder()
            .withTabsBefore(ENTITY_TAB.getKey())
            .icon(() -> new ItemStack(AllItems.SLOP_BUCKET.get()))
            .title(Component.translatable("tab.animania_resources.label"))
            .build());

    public static void addCreative (BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTab() == RESOURCES_TAB.get())
        {
            event.accept(AllItems.SLOP_BUCKET);
            event.accept(AllItems.STRAW);
            event.accept(AllItems.TROUGH);
            event.accept(AllItems.NEST);
        }
        else if (event.getTab() == ENTITY_TAB.get())
        {

        }
    }

    public static void register(IEventBus eventBus)
    {
        CREATIVE_TABS.register(eventBus);

        eventBus.addListener(AllTabs::addCreative);
    }
}
