package mod.vemerion.mosquitoes;

import mod.vemerion.mosquitoes.capacity.Mosquitoes;
import mod.vemerion.mosquitoes.capacity.MosquitoesStorage;
import mod.vemerion.mosquitoes.item.MosquitoWingItem;
import mod.vemerion.mosquitoes.item.SwatterItem;
import mod.vemerion.mosquitoes.network.AttackMosquitoMessage;
import mod.vemerion.mosquitoes.network.Network;
import mod.vemerion.mosquitoes.network.SpawnMosquitoesMessage;
import mod.vemerion.mosquitoes.network.SynchMosquitoesMessage;
import mod.vemerion.mosquitoes.network.WavingMessage;
import net.minecraft.item.Item;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventSubscriber {

	@SubscribeEvent
	public static void onRegisterItem(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(setup(new SwatterItem(), "swatter_item"));
		event.getRegistry().register(setup(new MosquitoWingItem(), "mosquito_wing_item"));
	}

	@SubscribeEvent
	public static void setup(FMLCommonSetupEvent event) {
		CapabilityManager.INSTANCE.register(Mosquitoes.class, new MosquitoesStorage(), Mosquitoes::new);

		Network.INSTANCE.registerMessage(0, SpawnMosquitoesMessage.class, SpawnMosquitoesMessage::encode,
				SpawnMosquitoesMessage::decode, SpawnMosquitoesMessage::handle);
		Network.INSTANCE.registerMessage(1, WavingMessage.class, WavingMessage::encode,
				WavingMessage::decode, WavingMessage::handle);
		Network.INSTANCE.registerMessage(2, AttackMosquitoMessage.class, AttackMosquitoMessage::encode,
				AttackMosquitoMessage::decode, AttackMosquitoMessage::handle);
		Network.INSTANCE.registerMessage(3, SynchMosquitoesMessage.class, SynchMosquitoesMessage::encode,
				SynchMosquitoesMessage::decode, SynchMosquitoesMessage::handle);
	}

	@SubscribeEvent
	public static void onRegisterSound(RegistryEvent.Register<SoundEvent> event) {
		SoundEvent flying_mosquito_sound = new SoundEvent(new ResourceLocation(Main.MODID, "flying_mosquito_sound"));
		event.getRegistry().register(setup(flying_mosquito_sound, "flying_mosquito_sound"));
		SoundEvent gulp_sound = new SoundEvent(new ResourceLocation(Main.MODID, "gulp_sound"));
		event.getRegistry().register(setup(gulp_sound, "gulp_sound"));
		SoundEvent smack_sound = new SoundEvent(new ResourceLocation(Main.MODID, "smack_sound"));
		event.getRegistry().register(setup(smack_sound, "smack_sound"));
		SoundEvent splash_sound = new SoundEvent(new ResourceLocation(Main.MODID, "splash_sound"));
		event.getRegistry().register(setup(splash_sound, "splash_sound"));
	}

	@SubscribeEvent
	public static void onIParticleTypeRegistration(RegistryEvent.Register<ParticleType<?>> event) {
		event.getRegistry().register(setup(new BasicParticleType(true), "mosquito_particle_type"));
	}

	public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final String name) {
		return setup(entry, new ResourceLocation(Main.MODID, name));
	}

	public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final ResourceLocation registryName) {
		entry.setRegistryName(registryName);
		return entry;
	}

}
