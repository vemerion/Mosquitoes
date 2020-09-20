package mod.vemerion.mosquitoes.potion;

import mod.vemerion.mosquitoes.Main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class MalariaCureEffect extends Effect {

	public MalariaCureEffect(EffectType typeIn, int liquidColorIn) {
		super(typeIn, liquidColorIn);
	}
	
	@Override
	public void affectEntity(Entity source, Entity indirectSource, LivingEntity entityLivingBaseIn, int amplifier,
			double health) {
		entityLivingBaseIn.removePotionEffect(Main.MALARIA_EFFECT);
	}

	@Override
	public boolean isInstant() {
		return true;
	}
	
	public static class MalariaCurePotionRecipe extends BrewingRecipe {

		public MalariaCurePotionRecipe(Ingredient input, Ingredient ingredient, ItemStack output) {
			super(input, ingredient, output);
		}

		@Override
		public boolean isInput(ItemStack input) {
			return super.isInput(input) && PotionUtils.getPotionFromItem(input) == Potions.AWKWARD;
		}
	}
}
