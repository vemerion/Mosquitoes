package mod.vemerion.mosquitoes.mosquito;

import java.util.Random;

import mod.vemerion.mosquitoes.Main;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class Mosquito {
	private float x, y, prevX, prevY, targetX, targetY, rotation, legRotation, prevLegRotation;
	private int ticksExisted;
	private Random rand;
	private int id;

	public Mosquito(Random rand, int id, int ticksExisted) {
		this.rand = rand;
		this.ticksExisted = ticksExisted;
		this.id = id;
		rotation = rand.nextFloat() * 360;
		initPos();
		prevX = x;
		prevY = y;
	}

	private void initPos() {
		targetX = randomXPos();
		targetY = randomYPos();
		if (ticksExisted < arrivingDuration() * 0.4f) {
			x = randomXPosOutside();
			y = randomYPosOutside();
		} else {
			x = targetX;
			y = targetY;
		}

		if (ticksExisted > startsLeavingAfter()) {
			targetX = randomXPosOutside();
			targetY = randomYPosOutside();

		}
	}

	public Mosquito(Random rand, int id) {
		this(rand, id, 0);
	}

	public void tick(PlayerEntity player) {
		ticksExisted++;

		if (player.world.isRemote) {
			prevX = x;
			prevY = y;
			prevLegRotation = legRotation;

			if (isArriving()) {
				Vector3d direction = direction();
				x += direction.x;
				y += direction.y;

				if (rand.nextDouble() < 0.05) {
					targetX = randomXPos();
					targetY = randomYPos();
				}
			} else if (isLeaving()) {
				Vector3d direction = direction();
				x += direction.x;
				y += direction.y;

				if (ticksExisted() - 1 == startsLeavingAfter()) {
					targetX = randomXPosOutside();
					targetY = randomYPosOutside();
				}
			}

			if (prevX < x) {
				legRotation = (float) MathHelper.lerp(0.15, legRotation, 20);
			} else {
				legRotation = (float) MathHelper.lerp(0.15, legRotation, -20);
			}

		} else {
			if (isSucking() && hasMalaria()) {
				if (rand.nextDouble() < 0.003 && !player.isPotionActive(Main.MALARIA_EFFECT)) {
					player.addPotionEffect(new EffectInstance(Main.MALARIA_EFFECT, 20 * 60 * 50));
				}
			}
		}
	}

	private boolean hasMalaria() {
		return id % 10 == 0;
	}

	private Vector3d direction() {
		Vector3d pos = new Vector3d(x, y, 0);
		Vector3d target = new Vector3d(targetX, targetY, 0);
		double distance = target.squareDistanceTo(pos);
		double speed = distance < 0.0001 ? 0 : distance < 0.17 ? 0.02 * (distance / 0.17) + 0.005 : 0.025;
		return target.subtract(pos).normalize().scale(speed);
	}

	private float randomYPos() {
		return rand.nextFloat() * 0.28f - 0.14f;
	}

	private float randomXPos() {
		if (rand.nextBoolean()) {
			return rand.nextFloat() * 0.05f - 0.025f - 0.15f;
		} else {
			return rand.nextFloat() * 0.05f - 0.025f + 0.15f;
		}
	}

	private float randomXPosOutside() {
		return MathHelper.cos((float) Math.toRadians(rotation)) * 1.5f;
	}

	private float randomYPosOutside() {
		return MathHelper.sin((float) Math.toRadians(rotation)) * 1.5f;
	}

	public int ticksExisted() {
		return ticksExisted;
	}

	public int getId() {
		return id;
	}

	public void chaseAway() {
		if (ticksExisted < startsLeavingAfter())
			ticksExisted = startsLeavingAfter();
	}

	public float getX(float partialTicks) {
		return MathHelper.lerp(partialTicks, prevX, x);
	}

	public float getY(float partialTicks) {
		return MathHelper.lerp(partialTicks, prevY, y);
	}

	public float getLegRotation(float partialTicks) {
		return MathHelper.lerp(partialTicks, prevLegRotation, legRotation);
	}

	public float getZ(float partialTicks) {
		float ageInTicks = ticksExisted() + partialTicks;
		double z = isArriving() ? -0.2 - MathHelper.clampedLerp(0.2, 0, ageInTicks / arrivingDuration())
				: isLeaving() ? -0.2 : -0.2;
		return (float) z;
	}

	public float getScale(float partialTicks) {
		return 0.06f;
	}

	public float getRotationX() {
		return isArriving() ? -180 : isLeaving() ? -180 : 90;
	}

	public float getRotationY() {
		return isArriving() ? 0 : isLeaving() ? 0 : rotation;
	}

	public int startsSuckingAfter() {
		return 160;
	}

	public int startsLeavingAfter() {
		return 260;
	}

	public int arrivingDuration() {
		return 120;
	}

	public int leavingDuration() {
		return 30;
	}

	public boolean hasArrived() {
		return ticksExisted() >= arrivingDuration();
	}

	public boolean isArriving() {
		return ticksExisted() < arrivingDuration();
	}

	public boolean isLeaving() {
		return ticksExisted() > startsLeavingAfter();
	}

	public boolean hasLeft() {
		return ticksExisted() >= startsLeavingAfter() + leavingDuration();
	}

	public boolean isSucking() {
		return ticksExisted() > startsSuckingAfter() && ticksExisted() < startsLeavingAfter();
	}

	public boolean isFlying() {
		return isArriving() || isLeaving();
	}
}
