package mod.vemerion.mosquitoes.tick;

import java.util.Random;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.MathHelper;

public class Tick {
	private static final float SPEED = 0.00015f;
	private static final int MAX_DISEASE_TIMER = 20 * 60 * 3;
	
	private float x, y, targetX, targetY, direction, bellySize, prevBellySize, prevX, prevY;
	private int ticksExisted;
	private int diseaseTimer;
	Random rand;
	
	public Tick() {
		this.rand = new Random();
		this.x = randomXPosOutside();
		this.y = randomYPosOutside();
		this.prevX = x;
		this.prevY = y;
		this.targetX = randomXPos();
		this.targetY = randomYPos();
		this.direction = (float) MathHelper.atan2(targetY - y, targetX - x);
		this.bellySize = 1;
		this.prevBellySize = bellySize;
		this.diseaseTimer = MAX_DISEASE_TIMER;
	}

	public void load(CompoundNBT compound) {
		x = compound.getFloat("x");
		y = compound.getFloat("y");
		this.prevX = x;
		this.prevY = y;
		targetX = compound.getFloat("targetX");
		targetY = compound.getFloat("targetY");
		direction = compound.getFloat("direction");
		bellySize = compound.getFloat("bellySize");
		prevBellySize = bellySize;
		ticksExisted = compound.getInt("ticksExisted");
	}

	public void save(CompoundNBT compound) {
		compound.putFloat("x", x);
		compound.putFloat("y", y);
		compound.putFloat("targetX", targetX);
		compound.putFloat("targetY", targetY);
		compound.putFloat("direction", direction);
		compound.putFloat("bellySize", bellySize);
		compound.putInt("ticksExisted", ticksExisted);
	}
	
	public int ticksExisted() {
		return ticksExisted;
	}

	// The tick is ticking
	public void tick(PlayerEntity player) {
		ticksExisted++;
		prevX = x;
		prevY = y;
		prevBellySize = bellySize;
		
		if (isMoving()) {
			x += MathHelper.cos(direction) * SPEED;
			y += MathHelper.sin(direction) * SPEED;
		} else {
			bellySize += 0.0001;
			
			if (!player.world.isRemote) {
				if (diseaseTimer-- < 0) {
					diseaseTimer = MAX_DISEASE_TIMER;
					player.addPotionEffect(new EffectInstance(Effects.HUNGER, 20 * 40));
				}
			}
		}
	}
	
	public float getX(float partialTicks) {
		return MathHelper.lerp(partialTicks, prevX, x);
	}
	
	public float getY(float partialTicks) {
		return MathHelper.lerp(partialTicks, prevY, y);
	}
	
	public float getBellySize(float partialTicks) {
		return MathHelper.lerp(partialTicks, prevBellySize, bellySize);
	}
	
	public float getDirection() {
		return (float) Math.toDegrees(direction) - 90;
	}
	
	private float randomYPos() {
		return rand.nextFloat() * 0.05f - 0.025f;
	}

	private float randomXPos() {
		return rand.nextFloat() * 0.1f - 0.05f;
	}

	private float randomXPosOutside() {
		return rand.nextBoolean() ? -0.15f : 0.15f;
	}

	private float randomYPosOutside() {
		return rand.nextFloat() * 0.2f - 0.1f;
	}

	public boolean isMoving() {
		return distanceToTarget() > 0.0001f;
	}

	private float distanceToTarget() {
		float xDistance = x - targetX;
		float yDistance = y - targetY;
		return xDistance * xDistance + yDistance * yDistance;
	}
}
