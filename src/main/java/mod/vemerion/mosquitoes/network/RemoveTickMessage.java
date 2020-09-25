package mod.vemerion.mosquitoes.network;

import java.util.function.Supplier;

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
		context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, ClientOnlyMethods::removeTick));
	}
}
