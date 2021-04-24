package com.youtube.hempfest.borders.task;

import com.youtube.hempfest.borders.ClansBorders;
import com.youtube.hempfest.borders.event.BorderTaskEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BorderTask extends BukkitRunnable {

	private final Player p;

	private boolean paused;

	public BorderTask(Player p) {
		this.p = p;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean b) {
		this.paused = b;
	}

	@Override
	public void run() {
		if (!isPaused()) {
			if (ClansBorders.getInstance().toggled.containsKey(p.getUniqueId()) && ClansBorders.getInstance().toggled.get(p.getUniqueId())) {
				BorderTaskEvent event = new BorderTaskEvent(p);
				Bukkit.getPluginManager().callEvent(event);
				if (!event.isCancelled()) {
					event.perform();
				}
			} else {
				setPaused(true);
			}
		} else {
			this.cancel();
			try {
				this.finalize();
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
		}
	}
}
