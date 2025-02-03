package edn.stratodonut.drivebywire;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WireSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DriveByWireMod.MOD_ID);

    public static final RegistryObject<SoundEvent> PLUG_IN = registerSoundEvents("plug_in");
    public static final RegistryObject<SoundEvent> PLUG_OUT = registerSoundEvents("plug_out");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(DriveByWireMod.MOD_ID, name)));
    }

    public static void register(IEventBus bus) { SOUND_EVENTS.register(bus); }
}
