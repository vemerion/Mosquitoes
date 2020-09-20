package mod.vemerion.mosquitoes.potion;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;

public class MalariaEffect extends Effect {
	
	private static final Effect[] EFFECTS = { Effects.BLINDNESS, Effects.NAUSEA, Effects.POISON, Effects.SLOWNESS };

	public MalariaEffect(EffectType typeIn, int liquidColorIn) {
		super(typeIn, liquidColorIn);
	}
	
	@Override
	public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
		entityLivingBaseIn.addPotionEffect(new EffectInstance(EFFECTS[entityLivingBaseIn.getRNG().nextInt(EFFECTS.length)], 20 * 60, amplifier));
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return duration % (20 * 60 * 6) == 0;
	}
	
	@Override
	public List<ItemStack> getCurativeItems() {
		return new ArrayList<>();
	}
}
