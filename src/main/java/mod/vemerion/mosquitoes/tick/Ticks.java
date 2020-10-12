package mod.vemerion.mosquitoes.tick;

import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import mod.vemerion.mosquitoes.Main;
import mod.vemerion.mosquitoes.network.Network;
import mod.vemerion.mosquitoes.network.RemoveTickMessage;
import mod.vemerion.mosquitoes.network.SynchTicksMessage;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.PacketDistributor;

public class Ticks {

	private static final Set<Block> SPAWN_POSITIONS = ImmutableSet.of(Blocks.TALL_GRASS, Blocks.GRASS);

	private static final int MAX_SPAWN_TIMER = 20 * 60 * 15;

	private Optional<Tick> tick;
	private int spawnTimer;

	public Ticks() {
		tick = Optional.empty();
		spawnTimer = MAX_SPAWN_TIMER;
	}

	public void tick(PlayerEntity player) {
		tick.ifPresent((t) -> t.tick(player));

		if (!player.world.isRemote) {
			if (inGrass(player) && !tick.isPresent()) {
				spawnTimer--;

				if (spawnTimer < 0) {
					spawnTimer = MAX_SPAWN_TIMER;
					createTick(player);
				}
			}
		}
	}

	public boolean hasTick() {
		return tick.isPresent();
	}

	public Tick getTick() {
		return tick.get();
	}

	public CompoundNBT save() {
		CompoundNBT compound = new CompoundNBT();
		tick.ifPresent((t) -> t.save(compound));
		compound.putBoolean("hasTick", tick.isPresent());
		compound.putInt("spawnTimer", spawnTimer);
		return compound;
	}

	public void load(CompoundNBT compound) {
		if (compound.getBoolean("hasTick")) {
			tick = Optional.of(new Tick());
			tick.get().load(compound);
		}
		spawnTimer = compound.getInt("spawnTimer");
	}

	private void createTick(PlayerEntity player) {
		tick = Optional.of(new Tick());
		sendSyncMessage(player);
	}

	public void sendSyncMessage(PlayerEntity player) {
		if (tick.isPresent()) {
			Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
					new SynchTicksMessage(save()));
		}
	}
	
	public void sendRemoveMessage(PlayerEntity player) {
		if (tick.isPresent()) {
			tick = Optional.empty();
			Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
					new RemoveTickMessage());
		}
	}

	private boolean inGrass(PlayerEntity player) {
		BlockPos pos = new BlockPos(player.getPositionVec());
		return SPAWN_POSITIONS.contains(player.world.getBlockState(pos).getBlock())
				|| SPAWN_POSITIONS.contains(player.world.getBlockState(pos.down()).getBlock());
	}

	public static Ticks getTicks(PlayerEntity player) {
		return player.getCapability(Main.TICKS_CAP).orElse(new Ticks());
	}

	public void remove() {
		tick = Optional.empty();
	}
}
