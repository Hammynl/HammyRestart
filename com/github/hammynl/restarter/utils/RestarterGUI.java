package com.github.hammynl.restarter.utils;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.hammynl.restarter.Restarter;

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
}
