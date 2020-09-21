package mod.vemerion.mosquitoes.block;

import mod.vemerion.mosquitoes.Main;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

public class CitronellaBlock extends BushBlock {
	private static final VoxelShape SHAPE = Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 15.0D, 11.0D);

	public CitronellaBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		Vec3d vec3d = state.getOffset(worldIn, pos);
		return SHAPE.withOffset(vec3d.x, vec3d.y, vec3d.z);
	}

	@Override
	public Block.OffsetType getOffsetType() {
		return Block.OffsetType.XZ;
	}

	public static void addFlowerGen() {
		BlockClusterFeatureConfig citronellaConfig = (new BlockClusterFeatureConfig.Builder(
				(new WeightedBlockStateProvider()).addWeightedBlockstate(Main.CITRONELLA_BLOCK.getDefaultState(), 1),
				new SimpleBlockPlacer())).tries(32).build();
		for (Biome biome : ForgeRegistries.BIOMES) {
			if (BiomeDictionary.getTypes(biome).contains(BiomeDictionary.Type.SAVANNA)) {
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
						Feature.FLOWER.withConfiguration(citronellaConfig)
								.withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(1))));
			}
		}
	}
}
