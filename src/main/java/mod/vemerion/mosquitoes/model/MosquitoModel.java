package mod.vemerion.mosquitoes.model;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import mod.vemerion.mosquitoes.mosquito.Mosquito;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

/**
 * Created using Tabula 8.0.0
 */
public class MosquitoModel extends Model {
	public ModelRenderer bodyLower;
	public ModelRenderer bodyUpper;
	public ModelRenderer head;
	public ModelRenderer tail1;
	public ModelRenderer leg21;
	public ModelRenderer leg31;
	public ModelRenderer leg11;
	public ModelRenderer leg41;
	public ModelRenderer leg51;
	public ModelRenderer leg61;
	public ModelRenderer leftWing;
	public ModelRenderer rightWing;
	public ModelRenderer straw;
	public ModelRenderer strawExtended;
	public ModelRenderer leftTooth;
	public ModelRenderer rightTooth;
	public ModelRenderer tail2;
	public ModelRenderer tail3;
	public ModelRenderer tail4;
	public ModelRenderer tail5;
	public ModelRenderer tail6;
	public ModelRenderer leg22;
	public ModelRenderer leg23;
	public ModelRenderer leg32;
	public ModelRenderer leg33;
	public ModelRenderer leg12;
	public ModelRenderer leg13;
	public ModelRenderer leg42;
	public ModelRenderer leg43;
	public ModelRenderer leg52;
	public ModelRenderer leg53;
	public ModelRenderer leg62;
	public ModelRenderer leg63;

	private float[] legStartRotation;

	public MosquitoModel() {
		super(RenderType::getEntityTranslucent);
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.leg53 = new ModelRenderer(this, 53, 8);
		this.leg53.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.leg53.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(leg53, 0.0F, 0.0F, -0.7428121536172364F);
		this.leftTooth = new ModelRenderer(this, 34, 0);
		this.leftTooth.setRotationPoint(1.2F, 1.0F, -1.0F);
		this.leftTooth.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(leftTooth, -0.5082398928281348F, 0.0F, 0.0F);
		this.leg12 = new ModelRenderer(this, 38, 17);
		this.leg12.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.leg12.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(leg12, 0.0F, 0.0F, 2.242398948393804F);
		this.head = new ModelRenderer(this, 24, 0);
		this.head.setRotationPoint(0.0F, 1.0F, -4.0F);
		this.head.addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(head, 0.19547687289441354F, 0.0F, 0.0F);
		this.leg22 = new ModelRenderer(this, 38, 17);
		this.leg22.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.leg22.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(leg22, 0.0F, 0.0F, 2.242398948393804F);
		this.leg42 = new ModelRenderer(this, 38, 17);
		this.leg42.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.leg42.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(leg42, 0.0F, 0.0F, 2.242398948393804F);
		this.leg23 = new ModelRenderer(this, 53, 8);
		this.leg23.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.leg23.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(leg23, 0.0F, 0.0F, -0.7428121536172364F);
		this.leg61 = new ModelRenderer(this, 59, 3);
		this.leg61.setRotationPoint(-2.0F, 1.0F, 1.0F);
		this.leg61.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(leg61, 2.3460715485728447F, 0.7819074915776542F, -1.13376586611655F);
		this.rightTooth = new ModelRenderer(this, 34, 0);
		this.rightTooth.setRotationPoint(-1.2F, 1.0F, -1.0F);
		this.rightTooth.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(rightTooth, -0.5082398928281348F, 0.0F, 0.0F);
		this.leg21 = new ModelRenderer(this, 59, 3);
		this.leg21.setRotationPoint(2.0F, 1.0F, 0.0F);
		this.leg21.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(leg21, 0.35185837453889574F, -0.19547687289441354F, -2.3458971115214444F);
		this.tail3 = new ModelRenderer(this, 0, 10);
		this.tail3.setRotationPoint(0.0F, 0.0F, 6.0F);
		this.tail3.addBox(-3.5F, -3.5F, -1.5F, 7.0F, 7.0F, 3.0F, 0.0F, 0.0F, 0.0F);
		this.leg52 = new ModelRenderer(this, 38, 17);
		this.leg52.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.leg52.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(leg52, 0.0F, 0.0F, 2.242398948393804F);
		this.leg33 = new ModelRenderer(this, 53, 8);
		this.leg33.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.leg33.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(leg33, 0.0F, 0.0F, -0.7428121536172364F);
		this.tail1 = new ModelRenderer(this, 38, 0);
		this.tail1.setRotationPoint(0.0F, -3.0F, 4.0F);
		this.tail1.addBox(-2.5F, -2.5F, -1.5F, 5.0F, 5.0F, 3.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(tail1, -0.19547687289441354F, 0.0F, 0.0F);
		this.tail5 = new ModelRenderer(this, 51, 15);
		this.tail5.setRotationPoint(0.0F, 0.0F, 11.5F);
		this.tail5.addBox(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, 0.0F, 0.0F, 0.0F);
		this.leg41 = new ModelRenderer(this, 59, 3);
		this.leg41.setRotationPoint(-2.0F, 1.0F, -1.0F);
		this.leg41.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(leg41, 3.0634019776689576F, -0.3504621124688808F, -0.5473352640780661F);
		this.leg43 = new ModelRenderer(this, 53, 8);
		this.leg43.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.leg43.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(leg43, 0.0F, 0.0F, -0.7330382858376184F);
		this.tail2 = new ModelRenderer(this, 35, 8);
		this.tail2.setRotationPoint(0.0F, 0.0F, 3.0F);
		this.tail2.addBox(-3.0F, -3.0F, -1.5F, 6.0F, 6.0F, 3.0F, 0.0F, 0.0F, 0.0F);
		this.tail4 = new ModelRenderer(this, 20, 14);
		this.tail4.setRotationPoint(0.0F, 0.0F, 9.0F);
		this.tail4.addBox(-3.0F, -3.0F, -1.5F, 6.0F, 6.0F, 3.0F, 0.0F, 0.0F, 0.0F);
		this.leftWing = new ModelRenderer(this, -16, 20);
		this.leftWing.setRotationPoint(2.0F, -1.0F, 4.0F);
		this.leftWing.addBox(-2.5F, 0.0F, 0.0F, 5.0F, 0.0F, 16.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(leftWing, -0.3909537457888271F, 0.2F, 0.0F);
		this.leg13 = new ModelRenderer(this, 53, 8);
		this.leg13.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.leg13.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(leg13, 0.0F, 0.0F, -0.7330382858376184F);
		this.leg51 = new ModelRenderer(this, 59, 3);
		this.leg51.setRotationPoint(-2.0F, 1.0F, 0.0F);
		this.leg51.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(leg51, 2.907020359511178F, 0.12077678556958815F, -0.8210028961170991F);
		this.tail6 = new ModelRenderer(this, 56, 0);
		this.tail6.setRotationPoint(0.0F, 0.0F, 13.0F);
		this.tail6.addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.rightWing = new ModelRenderer(this, -6, 20);
		this.rightWing.setRotationPoint(-2.0F, -1.0F, 4.0F);
		this.rightWing.addBox(-2.5F, 0.0F, 0.0F, 5.0F, 0.0F, 16.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(rightWing, -0.3909537457888271F, -0.2F, 0.0F);
		this.bodyUpper = new ModelRenderer(this, 7, 0);
		this.bodyUpper.setRotationPoint(0.0F, -3.0F, -2.0F);
		this.bodyUpper.addBox(-2.5F, -1.5F, -2.5F, 5.0F, 3.0F, 7.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(bodyUpper, 0.5473352640780661F, 0.0F, 0.0F);
		this.leg31 = new ModelRenderer(this, 59, 3);
		this.leg31.setRotationPoint(2.0F, 1.0F, 1.0F);
		this.leg31.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(leg31, 0.8991936386169619F, 0.6255260065779288F, -1.9940386704035213F);
		this.leg62 = new ModelRenderer(this, 38, 17);
		this.leg62.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.leg62.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(leg62, 0.0F, 0.0F, 2.242398948393804F);
		this.straw = new ModelRenderer(this, 34, 0);
		this.straw.setRotationPoint(0.0F, 1.0F, -1.0F);
		this.straw.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 10.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.bodyLower = new ModelRenderer(this, 0, 0);
		this.bodyLower.setRotationPoint(0.0F, 0.0F, -2.0F);
		this.bodyLower.addBox(-2.0F, -2.0F, -1.5F, 4.0F, 4.0F, 3.0F, 0.0F, 0.0F, 0.0F);
		this.leg32 = new ModelRenderer(this, 38, 17);
		this.leg32.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.leg32.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(leg32, 0.0F, 0.0F, 2.242398948393804F);
		this.leg63 = new ModelRenderer(this, 53, 8);
		this.leg63.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.leg63.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(leg63, 0.0F, 0.0F, -0.7428121536172364F);
		this.leg11 = new ModelRenderer(this, 59, 3);
		this.leg11.setRotationPoint(2.0F, 1.0F, -1.0F);
		this.leg11.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(leg11, -0.3127630032889644F, -0.6646214111173737F, -2.3068016404029725F);
		this.strawExtended = new ModelRenderer(this, 34, 0);
		this.strawExtended.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.strawExtended.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 10.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.leg52.addChild(this.leg53);
		this.head.addChild(this.leftTooth);
		this.leg11.addChild(this.leg12);
		this.bodyLower.addChild(this.head);
		this.leg21.addChild(this.leg22);
		this.leg41.addChild(this.leg42);
		this.leg22.addChild(this.leg23);
		this.bodyLower.addChild(this.leg61);
		this.head.addChild(this.rightTooth);
		this.bodyLower.addChild(this.leg21);
		this.leg51.addChild(this.leg52);
		this.leg32.addChild(this.leg33);
		this.bodyLower.addChild(this.leg41);
		this.leg42.addChild(this.leg43);
		this.bodyUpper.addChild(this.leftWing);
		this.leg12.addChild(this.leg13);
		this.bodyLower.addChild(this.leg51);
		this.bodyUpper.addChild(this.rightWing);
		this.bodyLower.addChild(this.bodyUpper);
		this.bodyLower.addChild(this.leg31);
		this.leg61.addChild(this.leg62);
		this.head.addChild(this.straw);
		this.leg31.addChild(this.leg32);
		this.leg62.addChild(this.leg63);
		this.bodyLower.addChild(this.leg11);
		this.straw.addChild(this.strawExtended);

		legStartRotation = new float[] { leg11.rotateAngleZ, leg21.rotateAngleZ, leg31.rotateAngleZ, leg41.rotateAngleZ,
				leg51.rotateAngleZ, leg61.rotateAngleZ };
	}

	public void renderMosquito(MatrixStack matrix, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn,
			Mosquito mosquito, float partialTicks) {
		float ageInTicks = mosquito.ticksExisted() + partialTicks;

		matrix.push();
		matrix.translate(mosquito.getX(partialTicks), mosquito.getY(partialTicks), mosquito.getZ(partialTicks));
		matrix.rotate(new Quaternion(mosquito.getRotationX(), mosquito.getRotationY(), 0, true));

		float scale = mosquito.getScale(partialTicks);
		matrix.scale(scale, scale, scale);

		float period = 10;
		float size = 0.22f;
		float offset = 3;
		float suckingDuration = ageInTicks - mosquito.startsSuckingAfter();

		bodyLower.render(matrix, bufferIn, packedLightIn, packedOverlayIn);
		matrix.push();
		bodyLower.translateRotate(matrix);
		List<ModelRenderer> tails = ImmutableList.of(tail1, tail2, tail3, tail4, tail5, tail6);
		for (int i = 0; i < tails.size(); i++) {
			matrix.push();
			if (suckingDuration >= offset * i)
				matrix.scale(1 + MathHelper.abs(MathHelper.sin((suckingDuration - offset * i) / period)) * size,
						1 + MathHelper.abs(MathHelper.sin((suckingDuration - offset) / period)) * size, 1);
			tails.get(i).render(matrix, bufferIn, packedLightIn, packedOverlayIn);
			matrix.pop();
			if (i == 0)
				tail1.translateRotate(matrix);
		}
		matrix.pop();
		matrix.pop();
	}

	public void animate(Mosquito mosquito, float partialTicks) {
		float ageInTicks = mosquito.ticksExisted() + partialTicks;


		// Extended straw
		strawExtended.showModel = !mosquito.isFlying();
		if (!mosquito.isFlying()) {
			strawExtended.rotationPointY = MathHelper
					.clamp((ageInTicks - mosquito.arrivingDuration()) / 2, 0, 10);
		}

		// Wiggle tail
		tail1.rotateAngleX = (float) (MathHelper.cos((ageInTicks / 60) * (float) Math.PI * 2) * Math.toRadians(4)
				+ Math.toRadians(-20));

		// Teeth
		if (!mosquito.isFlying()) {
			leftTooth.rotateAngleZ = (float) (MathHelper.cos((ageInTicks / 15) * (float) Math.PI * 2)
					* Math.toRadians(10));
			rightTooth.rotateAngleZ = (float) -(MathHelper.cos((ageInTicks / 15) * (float) Math.PI * 2)
					* Math.toRadians(10));
		}

		// wings
		if (mosquito.isFlying()) {
			leftWing.rotateAngleX = (float) (MathHelper.cos((ageInTicks / 2) * (float) Math.PI * 2) * Math.toRadians(35)
					- Math.toRadians(20));
			rightWing.rotateAngleX = (float) (MathHelper.cos(((ageInTicks + 0.2f) / 2) * (float) Math.PI * 2)
					* Math.toRadians(35) - Math.toRadians(20));
		} else {
			leftWing.rotateAngleX = (float) -Math.toRadians(20);
			rightWing.rotateAngleX = (float) -Math.toRadians(20);
		}

		// Leg animation
		List<ModelRenderer> outerLegs = ImmutableList.of(leg13, leg23, leg33, leg43, leg53, leg63);
		List<ModelRenderer> innerLegs = ImmutableList.of(leg11, leg21, leg31, leg41, leg51, leg61);
		if (mosquito.isFlying()) {
			outerLegs.forEach((leg) -> leg.rotateAngleZ = (float) Math.toRadians(30));

			for (int i = 0; i < legStartRotation.length; i++) {
				innerLegs.get(i).rotateAngleZ = legStartRotation[i]
						+ (float) Math.toRadians(mosquito.getLegRotation(partialTicks));
			}
		} else {
			outerLegs.forEach((leg) -> leg.rotateAngleZ = (float) Math.toRadians(-40));
			for (int i = 0; i < legStartRotation.length; i++) {
				innerLegs.get(i).rotateAngleZ = legStartRotation[i];
			}
		}
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn,
			float red, float green, float blue, float alpha) {

	}
}
