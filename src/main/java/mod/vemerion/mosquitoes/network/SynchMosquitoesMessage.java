package mod.vemerion.mosquitoes.network;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class SynchMosquitoesMessage {
	private CompoundNBT compound;
	
	public SynchMosquitoesMessage(CompoundNBT compound) {
		this.compound = compound;
	}

	public static void encode(final SynchMosquitoesMessage msg, final PacketBuffer buffer) {
		buffer.writeCompoundTag(msg.compound);
	}

	public static SynchMosquitoesMessage decode(final PacketBuffer buffer) {
		return new SynchMosquitoesMessage(buffer.readCompoundTag());
	}

	public static void handle(final SynchMosquitoesMessage msg, final Supplier<NetworkEvent.Context> supplier) {
		final NetworkEvent.Context context = supplier.get();
		context.setPacketHandled(true);
		context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientOnlyMethods.synchMosquitoes(msg.compound)));
	}
}
