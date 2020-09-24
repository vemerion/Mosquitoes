package mod.vemerion.mosquitoes;

import mod.vemerion.mosquitoes.mosquito.Mosquitoes;
import mod.vemerion.mosquitoes.mosquito.MosquitoesProvider;
import mod.vemerion.mosquitoes.network.Network;
import mod.vemerion.mosquitoes.network.SynchMosquitoesMessage;
import mod.vemerion.mosquitoes.tick.Ticks;
import mod.vemerion.mosquitoes.tick.TicksProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.PacketDistributor;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.FORGE)
public class ForgeEventSubscriber {
	public static final ResourceLocation MOSQUITOES_CAP = new ResourceLocation(Main.MODID, "mosquitoes");
	public static final ResourceLocation TICKS_CAP = new ResourceLocation(Main.MODID, "ticks");

	@SubscribeEvent
	public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		event.addCapability(MOSQUITOES_CAP, new MosquitoesProvider());
		event.addCapability(TICKS_CAP, new TicksProvider());
	}

	@SubscribeEvent
	public static void synchCapabilitiesWithClient(PlayerLoggedInEvent event) {
		PlayerEntity player = event.getPlayer();
		Mosquitoes mosquitoes = Mosquitoes.getMosquitoes(player);
		Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
				new SynchMosquitoesMessage(mosquitoes.save()));
		
		Ticks.getTicks(player).sendSyncMessage(player);
	}
	
	@SubscribeEvent
	public static void synchCapabilitiesWithClient(PlayerChangedDimensionEvent event) {
		PlayerEntity player = event.getPlayer();
		Mosquitoes mosquitoes = Mosquitoes.getMosquitoes(player);
		Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
				new SynchMosquitoesMessage(mosquitoes.save()));

		Ticks.getTicks(player).sendSyncMessage(player);
	}
	
	@SubscribeEvent
	public static void synchTicksWithClient(PlayerRespawnEvent event) {
		PlayerEntity player = event.getPlayer();
		Ticks.getTicks(player).sendSyncMessage(player);
	}
	
	@SubscribeEvent
	public static void retainTickAfterDeath(PlayerEvent.Clone event) {
		Ticks.getTicks(event.getPlayer()).load(Ticks.getTicks(event.getOriginal()).save());
	}

	@SubscribeEvent
	public static void tickCapabilities(PlayerTickEvent event) {
		if (event.phase == Phase.START) {
			PlayerEntity player = event.player;
			Mosquitoes.getMosquitoes(player).tick(player);
			Ticks.getTicks(player).tick(player);
		}
	}
}
