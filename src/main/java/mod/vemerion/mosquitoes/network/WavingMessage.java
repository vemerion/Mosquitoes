package mod.vemerion.mosquitoes.network;

import java.util.function.Supplier;

import mod.vemerion.mosquitoes.capacity.Mosquitoes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class WavingMessage {

	public static void encode(final WavingMessage msg, final PacketBuffer buffer) {
	}

	public static WavingMessage decode(final PacketBuffer buffer) {
		return new WavingMessage();
	}

	public static void handle(final WavingMessage msg, final Supplier<NetworkEvent.Context> supplier) {
		final NetworkEvent.Context context = supplier.get();
		context.setPacketHandled(true);
		context.enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			PlayerEntity player = context.getSender();
			Mosquitoes.getMosquitoes(player).killMosquitoServer(player);
		}));
	}
}
