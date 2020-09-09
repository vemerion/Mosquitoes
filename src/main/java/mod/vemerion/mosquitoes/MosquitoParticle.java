package mod.vemerion.mosquitoes;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.world.World;

public class MosquitoParticle extends SpriteTexturedParticle {

	protected MosquitoParticle(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		super(world, x, y, z, xSpeed, ySpeed, zSpeed);
		this.maxAge = 20;
		this.particleGravity = 1;
	}

	@Override
	public int getBrightnessForRender(float partialTick) {
		return 15728880;
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_LIT;
	}

	public static class Factory implements IParticleFactory<BasicParticleType> {
		private final IAnimatedSprite sprites;

		public Factory(IAnimatedSprite sprite) {
			this.sprites = sprite;
		}

		public Particle makeParticle(BasicParticleType typeIn, World worldIn, double x, double y, double z,
				double xSpeed, double ySpeed, double zSpeed) {
			MosquitoParticle particle = new MosquitoParticle(worldIn, x, y, z, ySpeed, zSpeed, xSpeed);
			particle.selectSpriteRandomly(sprites);
			return particle;
		}
	}
}
