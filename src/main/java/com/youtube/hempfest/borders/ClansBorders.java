package com.youtube.hempfest.borders;

import com.github.sanctum.labyrinth.formatting.component.Text_R2;
import com.github.sanctum.labyrinth.library.Message;
import com.github.sanctum.labyrinth.library.TextLib;
import com.youtube.hempfest.borders.task.BorderTask;
import com.youtube.hempfest.clans.util.events.CommandHelpEvent;
import com.youtube.hempfest.clans.util.events.SubCommandEvent;
import com.youtube.hempfest.clans.util.events.TabInsertEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class ClansBorders extends JavaPlugin implements Listener {

	private static ClansBorders instance;

	public HashMap<UUID, Boolean> toggled = new HashMap<>();

	public List<UUID> baseLocate = new ArrayList<>();

	public List<UUID> playerLocate = new ArrayList<>();

	public List<UUID> spawnLocate = new ArrayList<>();

	@Override
	public void onEnable() {
		instance = this;
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		toggled.clear();
	}

	public static ClansBorders getInstance() {
		return instance;
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		toggled.remove(p.getUniqueId());
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity().getPlayer();
		toggled.remove(p.getUniqueId());
	}

	private TextComponent coToggle(String body, String highlight, String highlightMsg, String command) {
		return TextLib.getInstance().textRunnable(body, highlight, highlightMsg, command);
	}

	@EventHandler
	public void onHelp(CommandHelpEvent e) {
		e.insert("&7|&e) &6/clan &fterritory &7| &8optional:&f-f &7<&8flag&7>");
		e.insert("&7|&e) &6/clan &fflags");
	}

	@EventHandler
	public void onTab(TabInsertEvent e) {
		List<String> tab = new ArrayList<>(Arrays.asList("flags", "territory"));
		List<String> tab2 = new ArrayList<>(Arrays.asList("-f"));
		List<String> tab3 = new ArrayList<>(Arrays.asList("base", "player", "spawn"));
		String[] args = e.getCommandArgs();
			for (String t : tab) {
				if (!e.getArgs(1).contains(t)) {
					e.add(1, t);
				}
			}

		if (args[0].equalsIgnoreCase("territory")) {
			for (String t : tab2) {
				if (!e.getArgs(2).contains(t)) {
					e.add(2, t);
				}
			}

			for (String t : tab3) {
				if (args.length == 3) {
					if (args[1].equalsIgnoreCase("-f")) {
						if (!e.getArgs(3).contains(t)) {
							e.add(3, t);
						}
					}
				}
			}
		}

	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onShow(SubCommandEvent e) {
		Player p = e.getSender();
		int length = e.getArgs().length;
		String[] args = e.getArgs();
		String prefix = "&f[&6&lX&f]&r";
		Message msg = new Message(p, prefix);
		if (length == 1) {
			if (args[0].equalsIgnoreCase("flags")) {
				msg.send("&f&oCurrent flags: [ &6base&f, &6player&f, &6spawn &f]");
				e.setReturn(true);
				return;
			}
			if (args[0].equalsIgnoreCase("territory")) {
				if (!toggled.containsKey(p.getUniqueId())) {
					msg.build(coToggle(e.stringLibrary().getPrefix().replace("&6", "&#eb9534&l") + " &#3ee67c&lChunk borders on &#ffffff| &#3ee6ddClick to toggle ", prefix.replace("&6", "&#eb9534"), "Click me to toggle", "c territory"));
					BorderTask task = new BorderTask(p);
					task.runTaskTimer(ClansBorders.getInstance(), 1, 40);
					toggled.put(p.getUniqueId(), true);
					e.setReturn(true);
					return;
				}
				if (toggled.get(p.getUniqueId())) {
					msg.build(coToggle(e.stringLibrary().getPrefix().replace("&6", "&#eb9534&l") + " &#eb4034&lChunk borders off &#ffffff| &#3ee6ddClick to toggle ", prefix.replace("&6", "&#eb9534"), "Click me to toggle", "c territory"));
					toggled.remove(p.getUniqueId());
					baseLocate.remove(p.getUniqueId());
					spawnLocate.remove(p.getUniqueId());
					playerLocate.remove(p.getUniqueId());
					e.setReturn(true);
					return;
				}

				e.setReturn(true);
			}
		}

		if (length == 3) {
			if (args[0].equalsIgnoreCase("territory")) {
				if (args[1].equalsIgnoreCase("-f")) {
					if (args[2].equalsIgnoreCase("base")) {
						if (e.getUtil().getClan(p) != null) {
							if (!baseLocate.contains(p.getUniqueId())) {
								baseLocate.add(p.getUniqueId());
								spawnLocate.remove(p.getUniqueId());
								playerLocate.remove(p.getUniqueId());
								if (!toggled.containsKey(p.getUniqueId())) {
									msg.build(coToggle(e.stringLibrary().getPrefix().replace("&6", "&#eb9534&l") + " &#3ee67c&lChunk borders on &#ffffff| &#3ee6ddClick to toggle ", prefix.replace("&6", "&#eb9534"), "Click me to toggle", "c territory"));
									BorderTask task = new BorderTask(p);
									task.runTaskTimer(ClansBorders.getInstance(), 1, 40);
									toggled.put(p.getUniqueId(), true);
								}
								msg.send("&6&oBase flag has been enabled.");
								msg.send("&7You are now locating your clans base location.");
							} else {
								baseLocate.remove(p.getUniqueId());
								msg.send("&c&oBase flag has been disabled.");
							}
						} else {
							e.stringLibrary().sendMessage(p, e.stringLibrary().notInClan());
						}
						e.setReturn(true);
					}
					if (args[2].equalsIgnoreCase("player")) {
						if (!playerLocate.contains(p.getUniqueId())) {
							playerLocate.add(p.getUniqueId());
							spawnLocate.remove(p.getUniqueId());
							baseLocate.remove(p.getUniqueId());
							if (!toggled.containsKey(p.getUniqueId())) {
								msg.build(coToggle(e.stringLibrary().getPrefix().replace("&6", "&#eb9534&l") + " &#3ee67c&lChunk borders on &#ffffff| &#3ee6ddClick to toggle ", prefix.replace("&6", "&#eb9534"), "Click me to toggle", "c territory"));
								BorderTask task = new BorderTask(p);
								task.runTaskTimer(ClansBorders.getInstance(), 1, 40);
								toggled.put(p.getUniqueId(), true);
							}
							msg.send("&6&oPlayer flag has been enabled.");
							msg.send("&7You are now locating any player within 500 blocks");
						} else {
							playerLocate.remove(p.getUniqueId());
							msg.send("&c&oPlayer flag has been disabled.");
						}
						e.setReturn(true);
					}
					if (args[2].equalsIgnoreCase("spawn")) {
						if (!spawnLocate.contains(p.getUniqueId())) {
							spawnLocate.add(p.getUniqueId());
							playerLocate.remove(p.getUniqueId());
							baseLocate.remove(p.getUniqueId());
							if (!toggled.containsKey(p.getUniqueId())) {
								msg.build(coToggle(e.stringLibrary().getPrefix().replace("&6", "&#eb9534&l") + " &#3ee67c&lChunk borders on &#ffffff| &#3ee6ddClick to toggle ", prefix.replace("&6", "&#eb9534"), "Click me to toggle", "c territory"));
								BorderTask task = new BorderTask(p);
								task.runTaskTimer(ClansBorders.getInstance(), 1, 40);
								toggled.put(p.getUniqueId(), true);
							}
							msg.send("&6&oSpawn flag has been enabled.");
							msg.send("&7You are now locating the spawn location.");
						} else {
							spawnLocate.remove(p.getUniqueId());
							msg.send("&c&oSpawn flag has been disabled.");
						}
						e.setReturn(true);
					}
				}
			}
		}

	}


}
