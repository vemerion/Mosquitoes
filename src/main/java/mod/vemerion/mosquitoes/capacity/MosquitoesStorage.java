package mod.vemerion.mosquitoes.capacity;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class MosquitoesStorage implements IStorage<Mosquitoes> {

	@Override
	public INBT writeNBT(Capability<Mosquitoes> capability, Mosquitoes instance, Direction side) {
		return IntNBT.valueOf(instance.count());
		
	}

	@Override
	public void readNBT(Capability<Mosquitoes> capability, Mosquitoes instance, Direction side, INBT nbt) {
		instance.mosquitoArrives(((IntNBT)nbt).getInt()); 
	}
}
