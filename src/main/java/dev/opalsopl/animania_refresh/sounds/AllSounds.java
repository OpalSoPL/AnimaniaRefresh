package dev.opalsopl.animania_refresh.sounds;

import dev.opalsopl.animania_refresh.AnimaniaRefresh;
import dev.opalsopl.animania_refresh.helper.ResourceHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AnimaniaRefresh.MODID);

    public static final RegistryObject<SoundEvent> SLOP_FLOW = registerSoundEvents("slop_flow");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceHelper.GetModResource(name)));
    }

    public static void register(IEventBus eventBus)
    {
        SOUNDS.register(eventBus);
    }
}
