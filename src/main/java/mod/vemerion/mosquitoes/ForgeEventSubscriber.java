package mod.vemerion.mosquitoes;

import mod.vemerion.mosquitoes.mosquito.Mosquitoes;
import mod.vemerion.mosquitoes.mosquito.MosquitoesProvider;
import mod.vemerion.mosquitoes.network.Network;
import mod.vemerion.mosquitoes.network.SynchMosquitoesMessage;
import mod.vemerion.mosquitoes.tick.Ticks;
import mod.vemerion.mosquitoes.tick.TicksProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.PacketDistributor;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.FORGE)
public class ForgeEventSubscriber {
	public static final ResourceLocation MOSQUITOES_CAP = new ResourceLocation(Main.MODID, "mosquitoes");
	public static final ResourceLocation TICKS_CAP = new ResourceLocation(Main.MODID, "ticks");

	@SubscribeEvent
	public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		event.addCapability(MOSQUITOES_CAP, new MosquitoesProvider());
		event.addCapability(TICKS_CAP, new TicksProvider());
	}
	
	@SubscribeEvent
	public static void onBiomeLoad(BiomeLoadingEvent event) {
		BlockClusterFeatureConfig citronellaConfig = (new BlockClusterFeatureConfig.Builder(
				(new WeightedBlockStateProvider()).addWeightedBlockstate(Main.CITRONELLA_BLOCK.getDefaultState(), 1),
				new SimpleBlockPlacer())).tries(32).build();
			if (event.getCategory() == Biome.Category.SAVANNA) {
				event.getGeneration().func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION,
						Feature.FLOWER.withConfiguration(citronellaConfig)
								.withPlacement(Features.Placements.field_244000_k)
								.withPlacement(Features.Placements.field_244001_l).func_242731_b(1));
			}
	}

	@SubscribeEvent
	public static void synchCapabilitiesWithClient(PlayerLoggedInEvent event) {
		PlayerEntity player = event.getPlayer();
		Mosquitoes mosquitoes = Mosquitoes.getMosquitoes(player);
		Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
				new SynchMosquitoesMessage(mosquitoes.save()));
		
		Ticks.getTicks(player).sendSyncMessage(player);
	}
	
	@SubscribeEvent
	public static void synchCapabilitiesWithClient(PlayerChangedDimensionEvent event) {
		PlayerEntity player = event.getPlayer();
		Mosquitoes mosquitoes = Mosquitoes.getMosquitoes(player);
		Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
				new SynchMosquitoesMessage(mosquitoes.save()));

		Ticks.getTicks(player).sendSyncMessage(player);
	}
	
	@SubscribeEvent
	public static void synchTicksWithClient(PlayerRespawnEvent event) {
		PlayerEntity player = event.getPlayer();
		Ticks.getTicks(player).sendSyncMessage(player);
	}
	
	@SubscribeEvent
	public static void retainTickAfterDeath(PlayerEvent.Clone event) {
		Ticks.getTicks(event.getPlayer()).load(Ticks.getTicks(event.getOriginal()).save());
	}

	@SubscribeEvent
	public static void tickCapabilities(PlayerTickEvent event) {
		if (event.phase == Phase.START) {
			PlayerEntity player = event.player;
			Mosquitoes.getMosquitoes(player).tick(player);
			Ticks.getTicks(player).tick(player);
		}
	}
}
