package mod.vemerion.mosquitoes.capacity;


import mod.vemerion.mosquitoes.Main;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class MosquitoesProvider implements ICapabilitySerializable<INBT>{

	private LazyOptional<Mosquitoes> instance = LazyOptional.of(Main.MOSQUITOES_CAP::getDefaultInstance);

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return Main.MOSQUITOES_CAP.orEmpty(cap, instance);
	}

	@Override
	public INBT serializeNBT() {
		return Main.MOSQUITOES_CAP.getStorage().writeNBT(Main.MOSQUITOES_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}

	@Override
	public void deserializeNBT(INBT nbt) {
		Main.MOSQUITOES_CAP.getStorage().readNBT(Main.MOSQUITOES_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
	}
}
