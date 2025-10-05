package dev.opalsopl.animania_refresh_farm;

import com.mojang.logging.LogUtils;
import dev.opalsopl.animania_refresh.AnimaniaRefresh; // import bazowego moda
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(AnimaniaRefreshFarm.MODID)
public class AnimaniaRefreshFarm {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "animania_refresh_farm";

    public AnimaniaRefreshFarm() {
    }
}
