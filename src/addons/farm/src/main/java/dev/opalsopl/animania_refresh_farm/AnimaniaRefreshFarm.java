package dev.opalsopl.animania_refresh_farm;

import dev.opalsopl.animania_refresh.AnimaniaRefresh; // import bazowego moda
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("animania_refresh_farm")
public class AnimaniaRefreshFarm {

    public AnimaniaRefreshFarm() {
        // Rejestracja metod inicjalizacyjnych
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Logika wspólna dla klienta i serwera
        System.out.println("AnimaniaRefreshFarm: Common setup completed");

        // Przykładowe użycie klasy bazowego moda
        System.out.println("Bazowy mod ID: " + AnimaniaRefresh.class.getSimpleName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // Logika tylko dla klienta (renderery, keybindy itp.)
        System.out.println("AnimaniaRefreshFarm: Client setup completed");
    }
}
