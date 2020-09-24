package mod.vemerion.mosquitoes.mosquito;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class MosquitoesStorage implements IStorage<Mosquitoes> {

	@Override
	public INBT writeNBT(Capability<Mosquitoes> capability, Mosquitoes instance, Direction side) {
		return instance.save();

	}

	@Override
	public void readNBT(Capability<Mosquitoes> capability, Mosquitoes instance, Direction side, INBT nbt) {
		if (nbt instanceof CompoundNBT)
			instance.load((CompoundNBT) nbt);
	}
}
