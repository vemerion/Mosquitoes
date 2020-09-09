package mod.vemerion.mosquitoes;

import mod.vemerion.mosquitoes.capacity.Mosquitoes;
import net.minecraft.item.Item;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod(Main.MODID)
public class Main {
	public static final String MODID = "mosquitoes";

	@ObjectHolder(Main.MODID + ":mosquito_particle_type")
	public static final BasicParticleType MOSQUITO_PARTICLE_TYPE = null;

	@ObjectHolder(Main.MODID + ":swatter_item")
	public static final Item SWATTER_ITEM = null;

	@ObjectHolder(Main.MODID + ":flying_mosquito_sound")
	public static final SoundEvent FLYING_MOSQUITO_SOUND = null;

	@ObjectHolder(Main.MODID + ":gulp_sound")
	public static final SoundEvent GULP_SOUND = null;

	@ObjectHolder(Main.MODID + ":smack_sound")
	public static final SoundEvent SMACK_SOUND = null;

	@ObjectHolder(Main.MODID + ":splash_sound")
	public static final SoundEvent SPLASH_SOUND = null;     
	
	@CapabilityInject(Mosquitoes.class)
	public static final Capability<Mosquitoes> MOSQUITOES_CAP = null;
}
