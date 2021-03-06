package mod.vemerion.mosquitoes;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import mod.vemerion.mosquitoes.item.TweezersItem;
import mod.vemerion.mosquitoes.model.MosquitoModel;
import mod.vemerion.mosquitoes.model.TickModel;
import mod.vemerion.mosquitoes.mosquito.Mosquito;
import mod.vemerion.mosquitoes.mosquito.Mosquitoes;
import mod.vemerion.mosquitoes.tick.Tick;
import mod.vemerion.mosquitoes.tick.Ticks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickEmpty;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEventSubscriber {

	private static final ResourceLocation MOSQUITO_TEXTURE = new ResourceLocation(Main.MODID,
			"textures/entity/mosquito.png");
	private static final ResourceLocation TICK_TEXTURE = new ResourceLocation(Main.MODID, "textures/entity/tick.png");

	@SubscribeEvent
	public static void renderMosquito(RenderHandEvent event) {
		MosquitoModel model = new MosquitoModel();
		PlayerEntity player = Minecraft.getInstance().player;
		Mosquitoes mosquitoes = player.getCapability(Main.MOSQUITOES_CAP).orElse(new Mosquitoes());
		for (int i = 0; i < mosquitoes.count(); i++) {
			Mosquito mosquito = mosquitoes.get(i);
			IVertexBuilder ivertexbuilder = event.getBuffers().getBuffer(model.getRenderType(MOSQUITO_TEXTURE));

			model.animate(mosquito, event.getPartialTicks());
			model.renderMosquito(new MatrixStack(), ivertexbuilder, event.getLight(), OverlayTexture.NO_OVERLAY,
					mosquito, event.getPartialTicks());
		}
	}

	@SubscribeEvent
	public static void renderSwatter(RenderHandEvent event) {
		AbstractClientPlayerEntity player = Minecraft.getInstance().player;
		ItemStack itemStack = event.getItemStack();
		Item item = itemStack.getItem();
		float partialTicks = event.getPartialTicks();
		if (item.equals(Main.SWATTER_ITEM) && player.getActiveItemStack().equals(itemStack)) {
			event.setCanceled(true);
			int maxDuration = itemStack.getUseDuration();
			float duration = (float) maxDuration - ((float) player.getItemInUseCount() - partialTicks + 1.0f);
			MatrixStack matrix = event.getMatrixStack();
			matrix.push();
			matrix.translate(0, -0.2, -0.3);
			matrix.translate(0, -1, 0);
			matrix.rotate(
					new Quaternion(Math.abs(MathHelper.cos((duration / 14) * (float) Math.PI * 2)) * -30, 90, 0, true));
			matrix.translate(0, 1, 0);
			Minecraft.getInstance().getItemRenderer().renderItem(itemStack, TransformType.FIRST_PERSON_RIGHT_HAND,
					event.getLight(), OverlayTexture.NO_OVERLAY, matrix, event.getBuffers());
			matrix.pop();
		}
	}

	@SubscribeEvent
	public static void renderTweezers(RenderHandEvent event) {
		AbstractClientPlayerEntity player = Minecraft.getInstance().player;
		ItemStack itemStack = event.getItemStack();
		Item item = itemStack.getItem();
		float partialTicks = event.getPartialTicks();
		if (item.equals(Main.TWEEZERS_ITEM) && player.getActiveItemStack().equals(itemStack)) {
			event.setCanceled(true);
			int maxDuration = itemStack.getUseDuration();
			float duration = (float) maxDuration - ((float) player.getItemInUseCount() - partialTicks + 1.0f);
			MatrixStack matrix = event.getMatrixStack();
			matrix.push();
			Vector2f prevPosition = TweezersItem.getPosition((int) duration);
			Vector2f position = TweezersItem.getPosition((int) duration);
			float x = MathHelper.lerp(partialTicks, prevPosition.x, position.x);
			float y = MathHelper.lerp(partialTicks, prevPosition.y, position.y);
			matrix.translate(x, y, -0.085);
			matrix.scale(0.1f, 0.1f, 0.1f);
			matrix.translate(0, -1, 0);
			matrix.rotate(new Quaternion(0, 45, 0, true));
			matrix.translate(0, 1, 0);
			Minecraft.getInstance().getItemRenderer().renderItem(itemStack, TransformType.FIRST_PERSON_RIGHT_HAND,
					event.getLight(), OverlayTexture.NO_OVERLAY, matrix, event.getBuffers());
			matrix.pop();
		}
	}

	@SubscribeEvent
	public static void renderWaving(RenderHandEvent event) {
		AbstractClientPlayerEntity player = Minecraft.getInstance().player;
		float swingProgress = event.getSwingProgress();
		if (player.getHeldItemMainhand().isEmpty() && player.getHeldItemOffhand().isEmpty() && swingProgress > 0
				&& hasMosquitoes(player)) {
			event.setCanceled(true);
			renderWavingHands(player, event.getMatrixStack(), event.getBuffers(), event.getLight(), swingProgress);
		}
	}

	@SubscribeEvent
	public static void renderTick(RenderHandEvent event) {
		TickModel model = new TickModel();
		PlayerEntity player = Minecraft.getInstance().player;
		Ticks ticks = Ticks.getTicks(player);
		if (ticks.hasTick()) {
			Tick tick = ticks.getTick();
			IVertexBuilder ivertexbuilder = event.getBuffers().getBuffer(model.getRenderType(TICK_TEXTURE));
			model.animate(tick, event.getPartialTicks());
			model.renderTick(tick, new MatrixStack(), ivertexbuilder, event.getLight(), OverlayTexture.NO_OVERLAY,
					event.getPartialTicks());
		}
	}

	private static void renderWavingHands(AbstractClientPlayerEntity player, MatrixStack matrix,
			IRenderTypeBuffer buffer, int light, float swing) {
		PlayerModel<AbstractClientPlayerEntity> playermodel = new PlayerModel<>(0, false);
		playermodel.setRotationAngles(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		renderWavingHand(player, matrix, buffer, light, swing, HandSide.RIGHT, playermodel.bipedRightArm,
				playermodel.bipedRightArmwear);
		renderWavingHand(player, matrix, buffer, light, swing, HandSide.LEFT, playermodel.bipedLeftArm,
				playermodel.bipedLeftArmwear);
	}

	private static void renderWavingHand(AbstractClientPlayerEntity player, MatrixStack matrix,
			IRenderTypeBuffer buffer, int light, float swing, HandSide handSide, ModelRenderer arm,
			ModelRenderer armwear) {
		float progress = MathHelper.sqrt(swing);
		boolean left = handSide == HandSide.LEFT;
		float offset = !left ? 1 : -1;
		swing = left ? swing * 0.9f : swing;
		matrix.push();
		matrix.translate(MathHelper.lerp(progress, 1.6, 0.8) * offset, -0.65 + swing * 0.1 + (left ? -0.1 : 0), -1);
		matrix.rotate(new Quaternion(-25 + swing * 6, 0, swing * 50 * offset, true));
		arm.render(matrix, buffer.getBuffer(RenderType.getEntitySolid(player.getLocationSkin())), light,
				OverlayTexture.NO_OVERLAY);
		armwear.render(matrix, buffer.getBuffer(RenderType.getEntityTranslucent(player.getLocationSkin())), light,
				OverlayTexture.NO_OVERLAY);
		matrix.pop();
	}

	@SubscribeEvent
	public static void chaseAwayMosquitoes(LeftClickEmpty event) {
		if (hasMosquitoes(event.getPlayer())) {
			Mosquitoes.getMosquitoes(event.getPlayer()).waveHands();
		}
	}

	private static boolean hasMosquitoes(PlayerEntity player) {
		return Mosquitoes.getMosquitoes(player).count() > 0;
	}

}
