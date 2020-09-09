package mod.vemerion.mosquitoes.capacity;

import java.util.Random;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Mosquito {
	private float x, y, prevX, prevY, targetX, targetY, rotation, legRotation, prevLegRotation;
	private int ticksExisted;
	private Random rand;

	public Mosquito(Random rand) {
		this.rand = rand;
		targetX = randomXPos();
		targetY = randomYPos();
		rotation = rand.nextFloat() * 360;
		x = randomXPosOutside();
		y = randomYPosOutside();
		prevX = x;
		prevY = y;
	}

	public void tick(PlayerEntity player) {
		ticksExisted++;

		if (player.world.isRemote) {
			prevX = x;
			prevY = y;
			prevLegRotation = legRotation;

			if (isArriving()) {
				Vec3d direction = direction();
				x += direction.x;
				y += direction.y;

				if (rand.nextDouble() < 0.3) {
					targetX = randomXPos();
					targetY = randomYPos();
				}
			} else if (isLeaving()) {
				Vec3d direction = direction();
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

		}
	}

	private Vec3d direction() {
		Vec3d pos = new Vec3d(x, y, 0);
		Vec3d target = new Vec3d(targetX, targetY, 0);
		double distance = target.squareDistanceTo(pos);
		double speed = distance < 0.01 ? 0 : distance < 0.17 ? 0.025 * (distance / 0.17) : 0.025;
		return target.subtract(pos).normalize().scale(speed);
	}

	private float randomYPos() {
		return rand.nextFloat() * 0.35f - 0.175f;
	}

	private float randomXPos() {
		return rand.nextFloat() * 0.6f - 0.3f;
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
		return 0.1f;
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
