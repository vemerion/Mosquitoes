package mod.vemerion.mosquitoes.network;

import java.util.Random;

import mod.vemerion.mosquitoes.Main;
import mod.vemerion.mosquitoes.mosquito.Mosquitoes;
import mod.vemerion.mosquitoes.tick.Ticks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.DistExecutor;

public class ClientOnlyMethods {

	public static DistExecutor.SafeRunnable attackMosquito(int id, boolean kill) {
		return new DistExecutor.SafeRunnable() {
			private static final long serialVersionUID = 1L;

			@Override
			public void run() {
				ClientPlayerEntity player = Minecraft.getInstance().player;
				Random rand = player.getRNG();
				Mosquitoes mosquitoes = player.getCapability(Main.MOSQUITOES_CAP).orElse(new Mosquitoes());

				if (kill && mosquitoes.killMosquitoClient(id)) {
					player.playSound(Main.SPLASH_SOUND, 1f, 0.8f + rand.nextFloat() * 0.4f);
					Vector3d direction = Vector3d.fromPitchYaw(player.getPitchYaw());
					Vector3d right = direction.rotateYaw(-90);
					Vector3d center = player.getPositionVec().add(direction.x * 0.6, 1.6 + direction.y * 0.6,
							direction.z * 0.6);
					for (int i = 0; i < 15; i++) {
						Vector3d position = center.add(right.x * (rand.nextDouble() * 1.5 - 0.75),
								rand.nextDouble() * 1.5 - 0.75, right.z * (rand.nextDouble() * 1.5 - 0.75));
						player.world.addParticle(Main.MOSQUITO_PARTICLE_TYPE, position.x, position.y, position.z, 0, 0,
								0);
					}
				} else {
					mosquitoes.chaseAwayMosquitoClient(id);
				}
			}
		};
	}

	public static DistExecutor.SafeRunnable removeTick() {
		return new DistExecutor.SafeRunnable() {
			private static final long serialVersionUID = 1L;

			@Override
			public void run() {
				ClientPlayerEntity player = Minecraft.getInstance().player;
				player.getCapability(Main.TICKS_CAP).orElse(new Ticks()).remove();
			}
		};
	}

	public static DistExecutor.SafeRunnable spawnMosquitoes(int count) {
		return new DistExecutor.SafeRunnable() {
			private static final long serialVersionUID = 1L;

			@Override
			public void run() {
				ClientPlayerEntity player = Minecraft.getInstance().player;
				Mosquitoes mosquitoes = player.getCapability(Main.MOSQUITOES_CAP)
						.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"));
				if (count > 0) {
					mosquitoes.mosquitoArrives(count);
				}
			}
		};
	}

	public static DistExecutor.SafeRunnable synchMosquitoes(CompoundNBT compound) {
		return new DistExecutor.SafeRunnable() {
			private static final long serialVersionUID = 1L;

			@Override
			public void run() {
				ClientPlayerEntity player = Minecraft.getInstance().player;
				Mosquitoes mosquitoes = player.getCapability(Main.MOSQUITOES_CAP).orElse(new Mosquitoes());
				mosquitoes.load(compound);
			}
		};
	}

	public static DistExecutor.SafeRunnable synchTicks(CompoundNBT compound) {
		return new DistExecutor.SafeRunnable() {
			private static final long serialVersionUID = 1L;

			@Override
			public void run() {
				ClientPlayerEntity player = Minecraft.getInstance().player;
				Ticks ticks = player.getCapability(Main.TICKS_CAP).orElse(new Ticks());
				ticks.load(compound);
			}
		};
	}
}
