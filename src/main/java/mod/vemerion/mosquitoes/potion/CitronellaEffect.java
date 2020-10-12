package mod.vemerion.mosquitoes.potion;

import mod.vemerion.mosquitoes.mosquito.Mosquitoes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class CitronellaEffect extends Effect {

	public CitronellaEffect(EffectType typeIn, int liquidColorIn) {
		super(typeIn, liquidColorIn);
	}

	@Override
	public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
		if (!entityLivingBaseIn.world.isRemote) {
			if (entityLivingBaseIn instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) entityLivingBaseIn;
				Mosquitoes.getMosquitoes(entityLivingBaseIn).citronellaEffect(player);
			}
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return duration % Math.max(20, 50 >> amplifier) == 0;
	}
}
