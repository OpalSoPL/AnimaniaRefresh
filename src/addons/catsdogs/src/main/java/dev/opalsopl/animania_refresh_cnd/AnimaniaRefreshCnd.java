package dev.opalsopl.animania_refresh_cnd;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("animania_refresh_cnd")
public class AnimaniaRefreshCnd {

    public AnimaniaRefreshCnd() {
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
