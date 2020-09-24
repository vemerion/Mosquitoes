package mod.vemerion.mosquitoes.item;

import mod.vemerion.mosquitoes.Main;
import mod.vemerion.mosquitoes.mosquito.Mosquitoes;
import mod.vemerion.mosquitoes.tick.Tick;
import mod.vemerion.mosquitoes.tick.Ticks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;

public class TweezersItem extends Item {
	private static final int DURATION = 15 * 5;

	public TweezersItem() {
		super(new Item.Properties().group(ItemGroup.TOOLS).maxDamage(16));
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return DURATION;
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

	public static Vec2f getPosition(int progress) {
		float x = progress / 15 * 0.05f - 0.1f;
		float y = progress % 15 * 0.2f / 15 - 0.1f;
		return new Vec2f(x, y);
	}

	private static float distance(Vec2f p1, Vec2f p2) {
		float x = p1.x - p2.x;
		float y = p1.y - p2.y;
		return x * x + y * y;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		if (!worldIn.isRemote && entityLiving instanceof PlayerEntity) {
			int progress = getUseDuration(stack) - timeLeft;
			PlayerEntity player = (PlayerEntity) entityLiving;
			Ticks ticks = Ticks.getTicks(player);
			if (ticks.hasTick()) {
				Tick tick = ticks.getTick();
				Vec2f pos = getPosition(progress);
				Vec2f tickPos = new Vec2f(tick.getX(0.5f), tick.getY(0.5f));
				if (distance(pos, tickPos) < 0.001) {
					System.out.println("REMOVE TICK");
					stack.damageItem(1, player, (p) -> {
						p.sendBreakAnimation(EquipmentSlotType.MAINHAND);
					});
				}
			}
		}
	}
}
