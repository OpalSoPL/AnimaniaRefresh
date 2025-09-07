package dev.opalsopl.animania_refresh.commands;

import dev.opalsopl.animania_refresh.AnimaniaRefresh;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AnimaniaRefresh.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AllCommands {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event)
    {
        DebugCommand.register(event.getDispatcher(), event.getBuildContext());
    }
}
