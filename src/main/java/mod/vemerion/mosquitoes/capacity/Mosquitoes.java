package mod.vemerion.mosquitoes.capacity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mod.vemerion.mosquitoes.Main;
import mod.vemerion.mosquitoes.network.AttackMosquitoMessage;
import mod.vemerion.mosquitoes.network.Network;
import mod.vemerion.mosquitoes.network.SpawnMosquitoesMessage;
import mod.vemerion.mosquitoes.network.WavingMessage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.network.PacketDistributor;

public class Mosquitoes {
	private List<Mosquito> mosquitoes;
	private Random rand;
	private int timer;
	private int waves;
	private int idCounter;
	private int waveHandsCooldown;

	public Mosquitoes() {
		this.init();
	}

	private void init() {
		rand = new Random();
		mosquitoes = new ArrayList<>();
		timer = 20 * 10;
		waves = rand.nextInt(2) + 2;
		idCounter = 0;
		waveHandsCooldown = 0;
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
		waveHandsCooldown--;

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
				int count = rand.nextInt(4) + 3;

				if (!player.isPotionActive(Main.CITRONELLA_EFFECT)) {
					mosquitoArrives(count);
					Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
							new SpawnMosquitoesMessage(count));
				}

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

	// The server picks a random mosquito to kill
	public boolean killRandomMosquito(PlayerEntity player) {
		Mosquito removed = null;
		if (!mosquitoes.isEmpty()) {
			removed = mosquitoes.remove(rand.nextInt(count()));
		}

		if (removed == null)
			return false;

		if (rand.nextDouble() < 0.1) {
			ItemEntity wing = new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(),
					new ItemStack(Main.MOSQUITO_WING_ITEM));
			player.world.addEntity(wing);
		}

		Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
				new AttackMosquitoMessage(removed.getId(), true));
		return true;
	}

	// The server picks a random mosquito to chase away, if conditions are met
	public boolean chaseAwayRandomMosquito(PlayerEntity player) {
		if (waveHandsCooldown < 0) {
			waveHandsCooldown = 20;
			if (!mosquitoes.isEmpty() && rand.nextDouble() < 0.5) {
				Mosquito chasedAway = mosquitoes.get(rand.nextInt(count()));
				Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
						new AttackMosquitoMessage(chasedAway.getId(), false));
				return true;
			}
		}
		return false;
	}

	// Client method for removing a mosquito
	public boolean killMosquitoClient(int id) {
		for (int i = 0; i < count(); i++) {
			if (get(i).getId() == id) {
				mosquitoes.remove(i);
				return true;
			}
		}
		return false;
	}

	// Client method for chasing away a mosquito
	public boolean chaseAwayMosquitoClient(int id) {
		for (int i = 0; i < count(); i++) {
			if (get(i).getId() == id) {
				get(i).chaseAway();
				return true;
			}
		}
		return false;
	}

	// The client sends a wave hands message to the server
	public void waveHands() {
		if (waveHandsCooldown < 0) {
			waveHandsCooldown = 20;
			Network.INSTANCE.send(PacketDistributor.SERVER.noArg(), new WavingMessage());
		}
	}

	public void mosquitoArrives(int count) {
		for (int i = 0; i < count; i++) {
			mosquitoes.add(new Mosquito(rand, idCounter++));
		}
	}

	public int count() {
		return mosquitoes.size();
	}

	public Mosquito get(int i) {
		return mosquitoes.get(i);
	}

	public void load(CompoundNBT compound) {
		init();
		idCounter = compound.getInt("idCounter");
		int[] ticksExisted = compound.getIntArray("ticksExisted");
		int[] ids = compound.getIntArray("ids");
		for (int i = 0; i < ids.length; i++) {
			mosquitoes.add(new Mosquito(rand, ids[i], ticksExisted[i]));
		}
	}

	public CompoundNBT save() {
		CompoundNBT compound = new CompoundNBT();
		int[] ticksExisted = new int[count()];
		int[] ids = new int[count()];
		for (int i = 0; i < ticksExisted.length; i++) {
			ticksExisted[i] = get(i).ticksExisted();
			ids[i] = get(i).getId();
		}
		compound.putIntArray("ticksExisted", ticksExisted);
		compound.putIntArray("ids", ids);
		compound.putInt("idCounter", idCounter);
		return compound;
	}

	public static Mosquitoes getMosquitoes(LivingEntity entity) {
		return entity.getCapability(Main.MOSQUITOES_CAP).orElse(new Mosquitoes());
	}
}
