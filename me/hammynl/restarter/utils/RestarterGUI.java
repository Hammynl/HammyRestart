package me.hammynl.restarter.utils;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.hammynl.restarter.Restarter;

public class RestarterGUI implements Listener {
	
	private Restarter plugin = Restarter.getPlugin(Restarter.class);
	
    public ItemStack createItem(Material material, int amount, String nameInput, ArrayList<String> lore) {
	    ItemStack item = new ItemStack(material, amount);
	    ItemMeta itemmeta = item.getItemMeta();
	    itemmeta.setDisplayName(nameInput);
	    itemmeta.setLore(lore);
	    item.setItemMeta(itemmeta);
	    return item;
	}
	
	
	public void restarterGUI(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9 * plugin.getConfInt("gui.rows"), plugin.getConfString("gui.title"));
		for(int i = 0; i < 9 * plugin.getConfInt("gui.rows"); i++) inv.setItem(i, createItem(Material.valueOf(plugin.getConfString("gui.fill-item")), 1, "§6", null));
		int count = 0;

		while(plugin.getConfig().getString("gui." + count) != null) {
			String path = "gui." + count;
			ArrayList<String> list = new ArrayList<String>();
			for(String line : plugin.getConfig().getStringList(path + ".lore")) {
				list.add(ChatColor.translateAlternateColorCodes('&', line));
			}
			inv.setItem(plugin.getConfInt(path + ".slot"), createItem(Material.valueOf(plugin.getConfString(path + ".item")), plugin.getConfInt(path + ".amount"), plugin.getConfString(path + ".name"), list));
			count++;
		}
		p.openInventory(inv);
	}
	
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
							Bukkit.spigot().restart();
						}
					}
					count++;
				}
			} 
		} catch(NullPointerException ex) {}
	}
}
