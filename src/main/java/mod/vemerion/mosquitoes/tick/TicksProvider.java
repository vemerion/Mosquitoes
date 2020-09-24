package mod.vemerion.mosquitoes.tick;


import mod.vemerion.mosquitoes.Main;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class TicksProvider implements ICapabilitySerializable<INBT>{

	private LazyOptional<Ticks> instance = LazyOptional.of(Main.TICKS_CAP::getDefaultInstance);

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return Main.TICKS_CAP.orEmpty(cap, instance);
	}

	@Override
	public INBT serializeNBT() {
		return Main.TICKS_CAP.getStorage().writeNBT(Main.TICKS_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}

	@Override
	public void deserializeNBT(INBT nbt) {
		Main.TICKS_CAP.getStorage().readNBT(Main.TICKS_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
	}
}
