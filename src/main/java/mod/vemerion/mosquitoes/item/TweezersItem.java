package mod.vemerion.mosquitoes.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class TweezersItem extends Item {
	private static final int LONG_TIME = 20 * 60 * 60;

	public TweezersItem() {
		super(new Item.Properties().group(ItemGroup.TOOLS).maxDamage(16));
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return LONG_TIME;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		return ActionResult.resultSuccess(itemstack);
	}

	@Override
	public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {		
		if (!worldIn.isRemote && livingEntityIn instanceof PlayerEntity) {
			
		}
	}
}
