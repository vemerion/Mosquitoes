package mod.vemerion.mosquitoes.network;

import java.util.function.Supplier;

import mod.vemerion.mosquitoes.Main;
import mod.vemerion.mosquitoes.tick.Ticks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class RemoveTickMessage {

	public static void encode(final RemoveTickMessage msg, final PacketBuffer buffer) {
	}

	public static RemoveTickMessage decode(final PacketBuffer buffer) {
		return new RemoveTickMessage();
	}

	public static void handle(final RemoveTickMessage msg, final Supplier<NetworkEvent.Context> supplier) {
		final NetworkEvent.Context context = supplier.get();
		context.setPacketHandled(true);
		context.enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			ClientPlayerEntity player = Minecraft.getInstance().player;
			player.getCapability(Main.TICKS_CAP).orElse(new Ticks()).remove();
		}));
	}
}
