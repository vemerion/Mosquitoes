package mod.vemerion.mosquitoes.network;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class AttackMosquitoMessage {
	private int id;
	private boolean kill;

	public AttackMosquitoMessage(int id, boolean kill) {
		this.id = id;
		this.kill = kill;
	}

	public static void encode(final AttackMosquitoMessage msg, final PacketBuffer buffer) {
		buffer.writeInt(msg.id);
		buffer.writeBoolean(msg.kill);
	}

	public static AttackMosquitoMessage decode(final PacketBuffer buffer) {
		return new AttackMosquitoMessage(buffer.readInt(), buffer.readBoolean());
	}

	public static void handle(final AttackMosquitoMessage msg, final Supplier<NetworkEvent.Context> supplier) {
		final NetworkEvent.Context context = supplier.get();
		context.setPacketHandled(true);
		context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () ->  ClientOnlyMethods.attackMosquito(msg.id, msg.kill)));
	}
}
