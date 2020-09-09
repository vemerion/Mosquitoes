package mod.vemerion.mosquitoes;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import mod.vemerion.mosquitoes.capacity.Mosquito;
import mod.vemerion.mosquitoes.capacity.Mosquitoes;
import mod.vemerion.mosquitoes.model.MosquitoModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@SuppressWarnings("deprecation")
@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEventSubscriber {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Main.MODID, "textures/entity/mosquito.png");

	@SubscribeEvent
	public static void renderMosquito(RenderHandEvent event) {
		MosquitoModel model = new MosquitoModel();
		PlayerEntity player = Minecraft.getInstance().player;
		Mosquitoes mosquitoes = player.getCapability(Main.MOSQUITOES_CAP).orElse(new Mosquitoes());
		for (int i = 0; i < mosquitoes.count(); i++) {
			Mosquito mosquito = mosquitoes.get(i);
			IVertexBuilder ivertexbuilder = event.getBuffers().getBuffer(model.getRenderType(TEXTURE));

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
			matrix.rotate(new Quaternion(Math.abs(MathHelper.cos((duration / 14) * (float) Math.PI * 2)) * -30, 90, 0, true));
			matrix.translate(0, 1, 0);
			Minecraft.getInstance().getItemRenderer().renderItem(itemStack, TransformType.FIRST_PERSON_RIGHT_HAND, event.getLight(), OverlayTexture.NO_OVERLAY, matrix, event.getBuffers());
			matrix.pop();
		}
	}

}
