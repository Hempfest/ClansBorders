package com.youtube.hempfest.borders.event;

import com.youtube.hempfest.borders.ClansBorders;
import com.youtube.hempfest.borders.api.CubeObject;
import com.youtube.hempfest.centerspawn.util.Spawn;
import com.youtube.hempfest.centerspawn.util.SpawnManager;
import com.youtube.hempfest.clans.HempfestClans;
import com.youtube.hempfest.clans.util.StringLibrary;
import com.youtube.hempfest.clans.util.construct.Claim;
import com.youtube.hempfest.clans.util.construct.Clan;
import com.youtube.hempfest.clans.util.construct.ClanUtil;
import com.youtube.hempfest.clans.util.listener.ClanEventBuilder;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class BorderTaskEvent extends ClanEventBuilder implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private final Player p;

	public BorderTaskEvent(Player p) {
		this.p = p;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public ClanUtil getUtil() {
		return Clan.clanUtil;
	}

	@Override
	public StringLibrary stringLibrary() {
		return Clan.clanUtil;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Spawn getSpawn() {
		SpawnManager manager = new SpawnManager(p);
		return new Spawn(manager);
	}

	public boolean isInClaim() {
		return Claim.claimUtil.isInClaim(p.getLocation());
	}

	public Claim getClaim() {
		return new Claim(Claim.claimUtil.getClaimID(p.getLocation()));
	}

	public Player getUser() {
		return p;
	}

	public void perform() {
		// send borders
		if (Claim.claimUtil.isInClaim(p.getLocation())) {
			Claim claim = getClaim();
			int cy1 = p.getLocation().getBlockY() + 5;
			int cy2 = p.getLocation().getBlockY() - 5;
			int cx1 = claim.getChunk().getX()*16;
			int cz1 = claim.getChunk().getZ()*16;
			int cx2 = claim.getChunk().getX()*16+16;
			int cz2 = claim.getChunk().getZ()*16+16;
			CubeObject cube = new CubeObject(cx2, cx1, cy1, cy2, cz2, cz1);
			cube.loadTarget(p);
			if (Clan.clanUtil.getClan(p) != null) {

				if (claim.getOwner().equals(Clan.clanUtil.getClan(p))) {
					int i = 0;
					i++;
					p.getWorld().spawnParticle(Particle.HEART, p.getLocation().getX(), p.getEyeLocation().getY() + 0.5, p.getLocation().getZ(), 1);
					cube.sendFriendly(CubeObject.ParticleColor.YELLOW);
					double add = p.getHealth() + i;
					int addF = p.getFoodLevel() + i;
					if (addF <= 20) {
						p.setFoodLevel(addF);
						i = 0;
					}
					if (add <= 20) {
						p.setHealth(add);
						i = 0;
					}
					return;
				}
				if (Clan.clanUtil.isNeutral(Clan.clanUtil.getClan(p), claim.getOwner())) {
					cube.sendCube(CubeObject.ParticleColor.WHITE);
				} else {
					if (Clan.clanUtil.getAllies(Clan.clanUtil.getClan(p)).contains(claim.getOwner())) {
						cube.sendFriendly(CubeObject.ParticleColor.GREEN);
					}
					if (Clan.clanUtil.getEnemies(Clan.clanUtil.getClan(p)).contains(claim.getOwner())) {
						cube.sendCube(CubeObject.ParticleColor.RED);
					}
				}
			} else {
				cube.sendCube(CubeObject.ParticleColor.WHITE);
			}
		} else {
			int cy1 = p.getLocation().getBlockY() + 5;
			int cy2 = p.getLocation().getBlockY() - 5;
			int cx1 = p.getLocation().getChunk().getX()*16;
			int cz1 = p.getLocation().getChunk().getZ()*16;
			int cx2 = p.getLocation().getChunk().getX()*16+16;
			int cz2 = p.getLocation().getChunk().getZ()*16+16;
			CubeObject cube = new CubeObject(cx2, cx1, cy1, cy2, cz2, cz1);
			cube.loadTarget(p);
			cube.sendCube(CubeObject.ParticleColor.YELLOW);
			if (Clan.clanUtil.getClan(p) != null) {
				Clan clan = HempfestClans.clanManager(p);
				Location base = clan.getBase();
				if (base != null) {
					if (ClansBorders.getInstance().baseLocate.contains(p.getUniqueId())) {
						cube.sendFlag(CubeObject.FlagType.BASE, base);
					}
				}
			}
			if (ClansBorders.getInstance().spawnLocate.contains(p.getUniqueId())) {
				cube.sendFlag(CubeObject.FlagType.SPAWN, getSpawn().getLocation());
			}
			if (ClansBorders.getInstance().playerLocate.contains(p.getUniqueId())) {
				for (Entity e : p.getNearbyEntities(1000, 10, 1000)) {
					if (e instanceof Player) {
						Player target = (Player) e;
						cube.sendFlag(CubeObject.FlagType.PLAYER, target.getLocation());
					}
				}

			}
		}
	}


	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
