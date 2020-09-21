package mod.vemerion.mosquitoes;

import java.awt.Color;

import mod.vemerion.mosquitoes.block.CitronellaBlock;
import mod.vemerion.mosquitoes.capacity.Mosquitoes;
import mod.vemerion.mosquitoes.capacity.MosquitoesStorage;
import mod.vemerion.mosquitoes.item.MosquitoWingItem;
import mod.vemerion.mosquitoes.item.SwatterItem;
import mod.vemerion.mosquitoes.network.AttackMosquitoMessage;
import mod.vemerion.mosquitoes.network.Network;
import mod.vemerion.mosquitoes.network.SpawnMosquitoesMessage;
import mod.vemerion.mosquitoes.network.SynchMosquitoesMessage;
import mod.vemerion.mosquitoes.network.WavingMessage;
import mod.vemerion.mosquitoes.potion.CitronellaEffect;
import mod.vemerion.mosquitoes.potion.MalariaCureEffect;
import mod.vemerion.mosquitoes.potion.MalariaEffect;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventSubscriber {

	@SubscribeEvent
	public static void onRegisterBlock(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(setup(new CitronellaBlock(Block.Properties.create(Material.PLANTS)
				.doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT)), "citronella_block"));
	}

	@SubscribeEvent
	public static void onRegisterItem(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(setup(new SwatterItem(), "swatter_item"));
		event.getRegistry().register(setup(new MosquitoWingItem(), "mosquito_wing_item"));

		event.getRegistry().register(
				setup(new BlockItem(Main.CITRONELLA_BLOCK, new Item.Properties().group(ItemGroup.DECORATIONS)),
						"citronella_block_item"));
	}

	@SubscribeEvent
	public static void onRegisterEffect(RegistryEvent.Register<Effect> event) {
		event.getRegistry().register(setup(new MalariaEffect(EffectType.HARMFUL, 0), "malaria_effect"));
		event.getRegistry().register(setup(
				new MalariaCureEffect(EffectType.BENEFICIAL, new Color(0, 255, 172).getRGB()), "malaria_cure_effect"));
		event.getRegistry().register(setup(
				new CitronellaEffect(EffectType.BENEFICIAL, new Color(197, 166, 70).getRGB()), "citronella_effect"));

	}

	@SubscribeEvent
	public static void onRegisterPotion(RegistryEvent.Register<Potion> event) {
		event.getRegistry()
				.register(setup(new Potion(new EffectInstance(Main.MALARIA_CURE_EFFECT, 1)), "malaria_cure_potion"));
		event.getRegistry().register(
				setup(new Potion(new EffectInstance(Main.CITRONELLA_EFFECT, 20 * 60 * 20)), "citronella_potion"));
		event.getRegistry().register(setup(new Potion("citronella_potion", new EffectInstance(Main.CITRONELLA_EFFECT, 20 * 60 * 20 * 2)),
				"long_citronella_potion"));

	}

	@SubscribeEvent
	public static void setup(FMLCommonSetupEvent event) {
		CapabilityManager.INSTANCE.register(Mosquitoes.class, new MosquitoesStorage(), Mosquitoes::new);

		Network.INSTANCE.registerMessage(0, SpawnMosquitoesMessage.class, SpawnMosquitoesMessage::encode,
				SpawnMosquitoesMessage::decode, SpawnMosquitoesMessage::handle);
		Network.INSTANCE.registerMessage(1, WavingMessage.class, WavingMessage::encode, WavingMessage::decode,
				WavingMessage::handle);
		Network.INSTANCE.registerMessage(2, AttackMosquitoMessage.class, AttackMosquitoMessage::encode,
				AttackMosquitoMessage::decode, AttackMosquitoMessage::handle);
		Network.INSTANCE.registerMessage(3, SynchMosquitoesMessage.class, SynchMosquitoesMessage::encode,
				SynchMosquitoesMessage::decode, SynchMosquitoesMessage::handle);

		DeferredWorkQueue.runLater(() -> addPotionRecipes());
		DeferredWorkQueue.runLater(() -> CitronellaBlock.addFlowerGen());
	}

	private static void addPotionRecipes() {
		BrewingRecipeRegistry.addRecipe(new MalariaCureEffect.ModBrewingRecipe(
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.AWKWARD),
				Ingredient.fromItems(Main.MOSQUITO_WING_ITEM),
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Main.MALARIA_CURE_POTION)));

		BrewingRecipeRegistry.addRecipe(new MalariaCureEffect.ModBrewingRecipe(
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.AWKWARD),
				Ingredient.fromItems(Main.CITRONELLA_BLOCK_ITEM),
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Main.CITRONELLA_POTION)));

		BrewingRecipeRegistry.addRecipe(new MalariaCureEffect.ModBrewingRecipe(
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Main.CITRONELLA_POTION),
				Ingredient.fromItems(Items.REDSTONE),
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Main.LONG_CITRONELLA_POTION)));
	}

	@SubscribeEvent
	public static void onRegisterSound(RegistryEvent.Register<SoundEvent> event) {
		SoundEvent flying_mosquito_sound = new SoundEvent(new ResourceLocation(Main.MODID, "flying_mosquito_sound"));
		event.getRegistry().register(setup(flying_mosquito_sound, "flying_mosquito_sound"));
		SoundEvent gulp_sound = new SoundEvent(new ResourceLocation(Main.MODID, "gulp_sound"));
		event.getRegistry().register(setup(gulp_sound, "gulp_sound"));
		SoundEvent smack_sound = new SoundEvent(new ResourceLocation(Main.MODID, "smack_sound"));
		event.getRegistry().register(setup(smack_sound, "smack_sound"));
		SoundEvent splash_sound = new SoundEvent(new ResourceLocation(Main.MODID, "splash_sound"));
		event.getRegistry().register(setup(splash_sound, "splash_sound"));
	}

	@SubscribeEvent
	public static void onIParticleTypeRegistration(RegistryEvent.Register<ParticleType<?>> event) {
		event.getRegistry().register(setup(new BasicParticleType(true), "mosquito_particle_type"));
	}

	public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final String name) {
		return setup(entry, new ResourceLocation(Main.MODID, name));
	}

	public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final ResourceLocation registryName) {
		entry.setRegistryName(registryName);
		return entry;
	}

}
