package dev.opalsopl.animania_refresh_catsdogs;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("animania_refresh_catsdogs")
public class AnimaniaRefreshCatsdogs {

    public AnimaniaRefreshCatsdogs() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
    }

    private void setup(final FMLCommonSetupEvent event) {
        System.out.println("AnimaniaRefreshFarm: Common setup completed");
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        System.out.println("AnimaniaRefreshFarm: Client setup completed");
    }
}
