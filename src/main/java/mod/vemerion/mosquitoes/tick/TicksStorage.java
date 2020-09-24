package mod.vemerion.mosquitoes.tick;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class TicksStorage implements IStorage<Ticks> {

	@Override
	public INBT writeNBT(Capability<Ticks> capability, Ticks instance, Direction side) {
		return instance.save();

	}

	@Override
	public void readNBT(Capability<Ticks> capability, Ticks instance, Direction side, INBT nbt) {
		if (nbt instanceof CompoundNBT)
			instance.load((CompoundNBT) nbt);
	}
}
