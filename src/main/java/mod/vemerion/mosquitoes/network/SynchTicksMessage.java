package mod.vemerion.mosquitoes.network;

import java.util.function.Supplier;

import mod.vemerion.mosquitoes.Main;
import mod.vemerion.mosquitoes.tick.Ticks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class SynchTicksMessage {
	private CompoundNBT compound;
	
	public SynchTicksMessage(CompoundNBT compound) {
		this.compound = compound;
	}

	public static void encode(final SynchTicksMessage msg, final PacketBuffer buffer) {
		buffer.writeCompoundTag(msg.compound);
	}

	public static SynchTicksMessage decode(final PacketBuffer buffer) {
		return new SynchTicksMessage(buffer.readCompoundTag());
	}

	public static void handle(final SynchTicksMessage msg, final Supplier<NetworkEvent.Context> supplier) {
		final NetworkEvent.Context context = supplier.get();
		context.setPacketHandled(true);
		context.enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			ClientPlayerEntity player = Minecraft.getInstance().player;
			Ticks ticks = player.getCapability(Main.TICKS_CAP).orElse(new Ticks());
			ticks.load(msg.compound);
		}));
	}
}
