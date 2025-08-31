package dev.opalsopl.animania_refresh;

import com.mojang.logging.LogUtils;
import dev.opalsopl.animania_refresh.blocks.AllBlocks;
import dev.opalsopl.animania_refresh.blocks.entities.client.TroughBlockRenderer;
import dev.opalsopl.animania_refresh.fluid.AllFluidTypes;
import dev.opalsopl.animania_refresh.fluid.AllFluids;
import dev.opalsopl.animania_refresh.items.AllItems;

import dev.opalsopl.animania_refresh.network.NetworkHandler;
import dev.opalsopl.animania_refresh.network.ParticlePacket;
import dev.opalsopl.animania_refresh.recipes.AllRecipeTypes;
import dev.opalsopl.animania_refresh.sounds.AllSounds;
import dev.opalsopl.animania_refresh.tabs.AllTabs;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AnimaniaRefresh.MODID)
public class AnimaniaRefresh {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "animania_refresh";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public AnimaniaRefresh(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        //network packets
        ParticlePacket.register(NetworkHandler.NETWORK);

        //note new deferred registers
        AllItems.register(modEventBus);
        AllBlocks.register(modEventBus);
        AllFluidTypes.register(modEventBus);
        AllFluids.register(modEventBus);
        AllSounds.register(modEventBus);
        AllRecipeTypes.register(modEventBus);
        AllTabs.register(modEventBus);

        GeckoLib.initialize();


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        if (Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
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
            BlockEntityRenderers.register(AllBlocks.TROUGH_BE.get(), TroughBlockRenderer::new);
        }
    }
}
