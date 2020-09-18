package mod.vemerion.mosquitoes.capacity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mod.vemerion.mosquitoes.Main;
import mod.vemerion.mosquitoes.network.MosquitoesMessage;
import mod.vemerion.mosquitoes.network.Network;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.network.PacketDistributor;

public class Mosquitoes {
	private List<Mosquito> mosquitoes;
	private Random rand;
	private int timer;
	private int waves;

	public Mosquitoes() {
		rand = new Random();
		this.mosquitoes = new ArrayList<>();
		timer = 20 * 10;
		waves = rand.nextInt(2) + 2;
	}

	public void tick(PlayerEntity player) {
		int sucking = 0;
		boolean someoneFlying = false;
		for (Mosquito m : mosquitoes) {
			m.tick(player);
			if (m.isSucking())
				sucking++;
			if (m.isFlying())
				someoneFlying = true;
		}

		if (!player.world.isRemote) {

			if (player.ticksExisted % 10 == 0)
				player.attackEntityFrom(DamageSource.GENERIC, sucking);

			if (timer-- < 0) {
				if (waves == 0) {
					timer = 20 * 60 * 5 + rand.nextInt(20 * 60);
					waves = rand.nextInt(2) + 2;
				} else {
					timer = 20 * 10 + rand.nextInt(20 * 10);

					waves--;
				}
				int count = rand.nextInt(2) + 1;

				mosquitoArrives(count);
				Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
						new MosquitoesMessage(count));

			}
		} else {
			if (player.ticksExisted % 15 == 0) {
				if (someoneFlying)
					player.playSound(Main.FLYING_MOSQUITO_SOUND, 1f, 1);
				if (sucking > 0)
					player.playSound(Main.GULP_SOUND, 1f, 0.8f + rand.nextFloat() * 0.4f);
			}
		}

		for (int i = count() - 1; i >= 0; i--) {
			if (mosquitoes.get(i).hasLeft()) {
				mosquitoes.remove(i);
			}
		}
	}

	public boolean killMosquitoServer(PlayerEntity player) {
		Mosquito removed = null;
		if (!mosquitoes.isEmpty()) {
			removed = mosquitoes.remove(0);
		}

		if (removed == null)
			return false;

		Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
				new MosquitoesMessage(-1));
		return true;
	}
	
	public boolean killMosquitoClient() {
		Mosquito removed = null;
		if (!mosquitoes.isEmpty()) {
			removed = mosquitoes.remove(0);
		}
		return removed != null;
	}

	public void mosquitoArrives(int count) {
		for (int i = 0; i < count; i++) {
			mosquitoes.add(new Mosquito(rand));
		}
	}

	public int count() {
		return mosquitoes.size();
	}

	public Mosquito get(int i) {
		return mosquitoes.get(i);
	}
	
	public static Mosquitoes getMosquitoes(PlayerEntity player) {
		return player.getCapability(Main.MOSQUITOES_CAP).orElse(new Mosquitoes());
	}
}
