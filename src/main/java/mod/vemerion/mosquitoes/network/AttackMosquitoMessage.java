package mod.vemerion.mosquitoes.network;

import java.util.Random;
import java.util.function.Supplier;

import mod.vemerion.mosquitoes.Main;
import mod.vemerion.mosquitoes.mosquito.Mosquitoes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
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
		context.enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			ClientPlayerEntity player = Minecraft.getInstance().player;
			Random rand = player.getRNG();
			Mosquitoes mosquitoes = Mosquitoes.getMosquitoes(player);

			if (msg.kill && mosquitoes.killMosquitoClient(msg.id)) {
				player.playSound(Main.SPLASH_SOUND, 1f, 0.8f + rand.nextFloat() * 0.4f);
				Vec3d direction = Vec3d.fromPitchYaw(player.getPitchYaw());
				Vec3d right = direction.rotateYaw(-90);
				Vec3d center = player.getPositionVec().add(direction.x * 0.6, 1.6 + direction.y * 0.6,
						direction.z * 0.6);
				for (int i = 0; i < 15; i++) {
					Vec3d position = center.add(right.x * (rand.nextDouble() * 1.5 - 0.75),
							rand.nextDouble() * 1.5 - 0.75, right.z * (rand.nextDouble() * 1.5 - 0.75));
					player.world.addParticle(Main.MOSQUITO_PARTICLE_TYPE, position.x, position.y, position.z, 0, 0, 0);
				}
			} else {
				mosquitoes.chaseAwayMosquitoClient(msg.id);
			}
		}));
	}
}
