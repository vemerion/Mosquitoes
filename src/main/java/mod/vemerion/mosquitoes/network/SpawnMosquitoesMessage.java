package mod.vemerion.mosquitoes.network;

import java.util.Random;
import java.util.function.Supplier;

import mod.vemerion.mosquitoes.Main;
import mod.vemerion.mosquitoes.mosquito.Mosquitoes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

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
		context.enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			ClientPlayerEntity player = Minecraft.getInstance().player;
			Mosquitoes mosquitoes = player.getCapability(Main.MOSQUITOES_CAP)
					.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"));
			if (msg.count > 0) {
				mosquitoes.mosquitoArrives(msg.count);
			}
		}));
	}
}
