package com.apptogo.roperace.manager;

import com.apptogo.roperace.game.ParticleEffectActor;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ParticlesManager {

	private static ParticlesManager INSTANCE;
	private ParticleEffectActor starParticle;
	private ParticleEffectActor hoopParticle;

	private ParticleEffectActor bubbleParticle;
	private final static String PARTICLES_DIR = "particle/";

	public static void create() {
		INSTANCE = new ParticlesManager();
	}

	public static void destroy() {
		INSTANCE = null;
	}

	public static ParticlesManager getInstance() {
		return INSTANCE;
	}

	/*---------PARTICLES-----------*/
	public void initParticles(TextureAtlas atlas) {
		starParticle = new ParticleEffectActor(PARTICLES_DIR + "star.p", 1, 4, 1, UnitConverter.toBox2dUnits(1), atlas);
		hoopParticle = new ParticleEffectActor(PARTICLES_DIR + "hoop.p", 1, 4, 1, UnitConverter.toBox2dUnits(1), atlas);
		bubbleParticle = new ParticleEffectActor(PARTICLES_DIR + "hoop.p", 1, 4, 1, UnitConverter.toBox2dUnits(1), atlas);

	}

	public static void changeColor(Color color, PooledEffect effect) {
		float temp[] = effect.getEmitters().first().getTint().getColors();
		temp[0] = color.r;
		temp[1] = color.g;
		temp[2] = color.b;
	}

	/*---------POOLS---------*/
	public ParticleEffectActor getStarParticle() {
		return starParticle;
	}

	public ParticleEffectActor getHoopParticle() {
		return hoopParticle;
	}

	public ParticleEffectActor getBubbleParticle() { return bubbleParticle; }
}
