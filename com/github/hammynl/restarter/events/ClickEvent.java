package com.github.hammynl.restarter.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.hammynl.restarter.Restarter;

public class ClickEvent implements Listener {
	
	private Restarter plugin = Restarter.getPlugin(Restarter.class);
	
	@EventHandler
	public void clickCheck(InventoryClickEvent e) {
		try {
			String Meta = e.getCurrentItem().getItemMeta().getDisplayName();
			if(e.getView().getTitle().equals(null)) {
				return;
			} else if(e.getView().getTitle().equals(plugin.getConfString("gui.title"))) {
				e.setCancelled(true);
				int count = 0;
				while(plugin.getConfig().getString("gui." + count) != null) {
					if(Meta.equals(plugin.getConfString("gui." + count + ".name"))) {
						plugin.timer = plugin.timer - plugin.getConfInt("gui." + count + ".remove");
						plugin.timer = plugin.timer + plugin.getConfInt("gui." + count + ".add");
						if(plugin.getConfBool("gui." + count + ".shutdown")) {
							for(Player all : Bukkit.getOnlinePlayers()) all.kickPlayer(plugin.prefix + plugin.getConfString("kick-message")); 
							if(plugin.getConfBool("use-restart-script")) {
								Bukkit.spigot().restart();
							} else {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
							}
						}
					}
					count++;
				}
			} 
		} catch(NullPointerException ex) {}
	}
}
