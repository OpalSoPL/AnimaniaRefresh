package dev.opalsopl.animania_refresh;

import com.mojang.logging.LogUtils;
import dev.opalsopl.animania_refresh.blocks.AllBlocks;
import dev.opalsopl.animania_refresh.fluid.AllFluidTypes;
import dev.opalsopl.animania_refresh.fluid.AllFluids;
import dev.opalsopl.animania_refresh.items.AllItems;

import dev.opalsopl.animania_refresh.network.NetworkHandler;
import dev.opalsopl.animania_refresh.network.ParticlePacket;
import dev.opalsopl.animania_refresh.sounds.AllSounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import static dev.opalsopl.animania_refresh.items.AllItems.SLOP_BUCKET;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AnimaniaRefresh.MODID)
public class AnimaniaRefresh {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "animania_refresh";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "animania_refresh" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);


    // Creates a creative tab with the id "animania_refresh:example_tab" for the example item, that is placed after the combat tab
    public static final RegistryObject<CreativeModeTab> ENTITY_TAB = CREATIVE_MODE_TABS.register("entites_tab", ()
            -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT) //note after default tabs
            .icon(() ->
            {
//                if (true) //check if any addon is present
//                {
//                    //get first egg
//                }
                return new ItemStack(Items.EGG);
            }
            /*SLOP_BUCKET.get().getDefaultInstance()*/) //note retrieve Item from registry and then get ItemStack
            .displayItems((parameters, output) -> {
                output.accept(SLOP_BUCKET.get()); //note add Example item to that tab
            })
            .title(Component.translatable("tab.animania_entities.label")) //note p_237116 is translation code
            .build());

    public static final RegistryObject<CreativeModeTab> RESOURCES_TAB = CREATIVE_MODE_TABS.register("resources_tab", ()
            -> CreativeModeTab.builder()
            .withTabsBefore(ENTITY_TAB.getKey())
            .icon(() -> new ItemStack(Items.DIAMOND)/*SLOP_BUCKET.get().getDefaultInstance()*/)
            .displayItems((parameters, output) -> {
                output.accept(SLOP_BUCKET.get());
            })
            .title(Component.translatable("tab.animania_resources.label"))
            .build());

    public AnimaniaRefresh() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        //network packets
        ParticlePacket.register(NetworkHandler.NETWORK);

        //note new deferred registers
        AllItems.register(modEventBus);
        AllBlocks.register(modEventBus);
        AllFluidTypes.register(modEventBus);
        AllFluids.register(modEventBus);
        AllSounds.register(modEventBus);


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        if (Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        //if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) event.accept(EXAMPLE_BLOCK_ITEM);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}
