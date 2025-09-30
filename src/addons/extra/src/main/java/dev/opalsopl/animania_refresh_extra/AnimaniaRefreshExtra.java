package dev.opalsopl.animania_refresh_extra;

import dev.opalsopl.animania_refresh.AnimaniaRefresh;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("animania_refresh_extra")
public class AnimaniaRefreshExtra {

    public AnimaniaRefreshExtra() {
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
