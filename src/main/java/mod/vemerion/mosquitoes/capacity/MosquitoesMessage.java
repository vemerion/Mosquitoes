package mod.vemerion.mosquitoes.capacity;

import java.util.Random;
import java.util.function.Supplier;

import mod.vemerion.mosquitoes.Main;
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

public class MosquitoesMessage {
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(Main.MODID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals);

	private int count;

	public MosquitoesMessage(int count) {
		this.count = count;
	}

	public static void encode(final MosquitoesMessage msg, final PacketBuffer buffer) {
		buffer.writeInt(msg.count);
	}

	public static MosquitoesMessage decode(final PacketBuffer buffer) {
		return new MosquitoesMessage(buffer.readInt());
	}

	// This turned into kind of a mess because of DistExecutor silliness
	public static void handle(final MosquitoesMessage msg, final Supplier<NetworkEvent.Context> supplier) {
		final NetworkEvent.Context context = supplier.get();
		context.setPacketHandled(true);
		context.enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			ClientPlayerEntity player = Minecraft.getInstance().player;
			Random rand = player.getRNG();
			Mosquitoes mosquitoes = player.getCapability(Main.MOSQUITOES_CAP)
					.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"));
			if (msg.count > 0) {
				mosquitoes.mosquitoArrives(msg.count);
			} else {
				if (mosquitoes.killMosquitoClient()) {
					player.playSound(Main.SPLASH_SOUND, 1f, 0.8f + rand.nextFloat() * 0.4f);
					Vec3d direction = Vec3d.fromPitchYaw(player.getPitchYaw());
					Vec3d right = direction.rotateYaw(-90);
					Vec3d center = player.getPositionVec().add(direction.x * 0.6, 1.6 + direction.y * 0.6,
							direction.z * 0.6);
					for (int i = 0; i < 15; i++) {
						Vec3d position = center.add(right.x * (rand.nextDouble() * 1.5 - 0.75),
								rand.nextDouble() * 1.5 - 0.75, right.z * (rand.nextDouble() * 1.5 - 0.75));
						player.world.addParticle(Main.MOSQUITO_PARTICLE_TYPE, position.x, position.y, position.z, 0, 0,
								0);
					}
				}
			}
		}));
	}
}
