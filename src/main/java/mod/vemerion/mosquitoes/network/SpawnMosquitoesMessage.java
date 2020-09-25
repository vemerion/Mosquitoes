package mod.vemerion.mosquitoes.network;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class SpawnMosquitoesMessage {
	private int count;

	public SpawnMosquitoesMessage(int count) {
		this.count = count;
	}

	public static void encode(final SpawnMosquitoesMessage msg, final PacketBuffer buffer) {
		buffer.writeInt(msg.count);
	}

	public static SpawnMosquitoesMessage decode(final PacketBuffer buffer) {
		return new SpawnMosquitoesMessage(buffer.readInt());
	}

	public static void handle(final SpawnMosquitoesMessage msg, final Supplier<NetworkEvent.Context> supplier) {
		final NetworkEvent.Context context = supplier.get();
		context.setPacketHandled(true);
		context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientOnlyMethods.spawnMosquitoes(msg.count)));
	}
}
