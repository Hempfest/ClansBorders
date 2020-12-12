package com.youtube.hempfest.borders.api;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CubeObject {

	private final int xMax;
	private final int xMin;
	private final int yMax;
	private final int yMin;
	private final int zMax;
	private final int zMin;

	private Player p;

	public CubeObject(int xMax, int xMin, int yMax, int yMin, int zMax, int zMin) {
		this.xMax = xMax;
		this.zMax = zMax;
		this.yMax = yMax;
		this.xMin = xMin;
		this.zMin = zMin;
		this.yMin = yMin;
	}

	public void loadTarget(Player target) {
		this.p = target;
	}

	public enum ParticleColor {
		WHITE(Color.fromRGB(255, 255, 255)),
		GREEN(Color.fromRGB(66, 245, 102)),
		RED(Color.fromRGB(255, 10, 10)),
		YELLOW(Color.fromRGB(207, 183, 4));

		private final Color color;

		ParticleColor(Color color) {
			this.color = color;
		}

		public Color toColor() {
			return color;
		}
	}

	public void sendCube(ParticleColor color) {
		Particle.DustOptions dustOptions = new Particle.DustOptions(color.toColor(), 2);
		for (int i = xMin; i < xMax; i++) {
			for (int j = yMin; j < yMax; j++) {
				p.spawnParticle(Particle.REDSTONE, i, j, zMin, 1, dustOptions);
				p.spawnParticle(Particle.REDSTONE, i, j + 2, zMin, 1, dustOptions);
				p.spawnParticle(Particle.SPELL, i, j + 1, zMin, 1);
			}
		}
		for (int i = xMin; i < xMax; i++) {
			for (int j = yMin; j < yMax; j++) {
				p.spawnParticle(Particle.REDSTONE, i, j, zMax, 1, dustOptions);
				p.spawnParticle(Particle.REDSTONE, i, j + 2, zMax, 1, dustOptions);
				p.spawnParticle(Particle.SPELL, i, j + 1, zMax, 1);
			}
		}
		for (int i = zMin; i < zMax; i++) {
			for (int j = yMin; j < yMax; j++) {
				p.spawnParticle(Particle.REDSTONE, xMin, j, i, 1, dustOptions);
				p.spawnParticle(Particle.REDSTONE, xMin, j + 2, i, 1, dustOptions);
				p.spawnParticle(Particle.SPELL, xMin, j + 1, i, 1);
			}
		}
		for (int i = zMin; i < zMax; i++) {
			for (int j = yMin; j < yMax; j++) {
				p.spawnParticle(Particle.REDSTONE, xMax, j, i, 1, dustOptions);
				p.spawnParticle(Particle.REDSTONE, xMax, j + 2, i, 1, dustOptions);
				p.spawnParticle(Particle.SPELL, xMax, j + 1, i, 1);
			}
		}
	}

	public void sendFriendly(ParticleColor color) {
		Particle.DustOptions dustOptions = new Particle.DustOptions(color.toColor(), 2);
		for (int i = xMin; i < xMax; i++) {
			for (int j = yMin; j < yMax; j++) {
				p.spawnParticle(Particle.REDSTONE, i, j, zMin, 1, dustOptions);
				p.spawnParticle(Particle.REDSTONE, i, j + 2, zMin, 1, dustOptions);
				p.spawnParticle(Particle.HEART, i, j + 1, zMin, 1);
			}
		}
		for (int i = xMin; i < xMax; i++) {
			for (int j = yMin; j < yMax; j++) {
				p.spawnParticle(Particle.REDSTONE, i, j, zMax, 1, dustOptions);
				p.spawnParticle(Particle.REDSTONE, i, j + 2, zMax, 1, dustOptions);
				p.spawnParticle(Particle.HEART, i, j + 1, zMax, 1);
			}
		}
		for (int i = zMin; i < zMax; i++) {
			for (int j = yMin; j < yMax; j++) {
				p.spawnParticle(Particle.REDSTONE, xMin, j, i, 1, dustOptions);
				p.spawnParticle(Particle.REDSTONE, xMin, j + 2, i, 1, dustOptions);
				p.spawnParticle(Particle.HEART, xMin, j + 1, i, 1);
			}
		}
		for (int i = zMin; i < zMax; i++) {
			for (int j = yMin; j < yMax; j++) {
				p.spawnParticle(Particle.REDSTONE, xMax, j, i, 1, dustOptions);
				p.spawnParticle(Particle.REDSTONE, xMax, j + 2, i, 1, dustOptions);
				p.spawnParticle(Particle.HEART, xMax, j + 1, i, 1);
			}
		}
	}

	public enum FlagType {

		BASE, SPAWN, PLAYER

	}

	public void sendFlag(FlagType type, Location loc) {
		Location origin = p.getEyeLocation();  // location where it starts
		Vector target = loc.toVector();  // location where it ends (for the direction only)
		origin.setDirection(target.subtract(origin.toVector()));  // setting direction bc of above information
		Vector increase = origin.getDirection().multiply(1.3); // getting what to increase by
		if (!loc.getChunk().equals(p.getLocation().getChunk())) {
			for (int counter = 0; counter < 10; counter++) { // 5 == blocks to travel
				Location location = origin.add(increase);
				double x = location.getX();
				double y = location.getY() + 0.5;
				double z = location.getZ();
				switch (type) {
					case BASE:
						location.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, x, y, z, 10, 0, 0, 0, 0);
						location.getWorld().spawnParticle(Particle.SWEEP_ATTACK, x, y, z, 10, 0, 0, 0, 0);
						break;
					case SPAWN:
						location.getWorld().spawnParticle(Particle.FLAME, x, y, z, 10, 0, 0, 0, 0);
						location.getWorld().spawnParticle(Particle.SWEEP_ATTACK, x, y, z, 10, 0, 0, 0, 0);
						break;
					case PLAYER:
						location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, x, y, z, 10, 0, 0, 0, 0);
						location.getWorld().spawnParticle(Particle.SWEEP_ATTACK, x, y, z, 10, 0, 0, 0, 0);
						break;
				}

			}
		}
	}


}
