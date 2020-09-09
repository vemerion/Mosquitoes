package mod.vemerion.mosquitoes;

import mod.vemerion.mosquitoes.capacity.Mosquitoes;
import mod.vemerion.mosquitoes.capacity.MosquitoesMessage;
import mod.vemerion.mosquitoes.capacity.MosquitoesProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.PacketDistributor;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.FORGE)
public class ForgeEventSubscriber {
	public static final ResourceLocation MOSQUITOES_CAP = new ResourceLocation(Main.MODID, "mosquitoes");

	@SubscribeEvent
	public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		event.addCapability(MOSQUITOES_CAP, new MosquitoesProvider());
	}

	@SubscribeEvent
	public static void synchMosquitoesWithClient(PlayerLoggedInEvent event) {
		PlayerEntity player = event.getPlayer();
		Mosquitoes mosquitoes = player.getCapability(Main.MOSQUITOES_CAP).orElse(new Mosquitoes());
		MosquitoesMessage.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
				new MosquitoesMessage(mosquitoes.count()));
	}

	@SubscribeEvent
	public static void tickMosquitoes(PlayerTickEvent event) {
		if (event.phase == Phase.START) {
			PlayerEntity player = event.player;
			Mosquitoes mosquitoes = player.getCapability(Main.MOSQUITOES_CAP).orElse(new Mosquitoes());
			mosquitoes.tick(player);
		}
	}
}
