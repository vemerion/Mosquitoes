package mod.vemerion.mosquitoes.item;

import mod.vemerion.mosquitoes.Main;
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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector2f;
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

	public static Vector2f getPosition(int progress) {
		float x = progress / 15 * 0.05f - 0.1f;
		float y = progress % 15 * 0.2f / 15 - 0.1f;
		return new Vector2f(x, y);
	}

	private static float distance(Vector2f p1, Vector2f p2) {
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
				Vector2f pos = getPosition(progress);
				Vector2f tickPos = new Vector2f(tick.getX(0.5f), tick.getY(0.5f));
				if (distance(pos, tickPos) < 0.001) {
					worldIn.playSound(null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), Main.TEAR_SOUND, SoundCategory.PLAYERS, 1, 1);
					ticks.sendRemoveMessage(player);
					stack.damageItem(1, player, (p) -> {
						p.sendBreakAnimation(EquipmentSlotType.MAINHAND);
					});
				}
			}
		}
	}
}
