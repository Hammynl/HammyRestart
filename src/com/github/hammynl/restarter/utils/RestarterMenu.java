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

public class RestarterMenu implements Listener {
	
	private Restarter plugin = Restarter.getPlugin(Restarter.class);
	
    public ItemStack createItem(Material material, int amount, String nameInput, ArrayList<String> lore) {
	    ItemStack item = new ItemStack(material, amount);
	    ItemMeta itemmeta = item.getItemMeta();
	    itemmeta.setDisplayName(nameInput);
	    itemmeta.setLore(lore);
	    item.setItemMeta(itemmeta);
	    return item;
	}
	
	
	public void openMenu(Player p) {
		/*
		 * Creating some variables to pull from the config such as titles and amount of slots
		 */
		int menuSlots = 9 * plugin.getConfigInteger("gui.rows");
		String menuTitle = plugin.getConfigString("gui.title");
		ItemStack fillItem = createItem(Material.valueOf(plugin.getConfigStringRaw("gui.fill-item")), 1, "§6", null);
		/*
		 * Creating the actual inventory, And filling it up with the fill items.
		 */
		Inventory menu = Bukkit.createInventory(null, menuSlots, menuTitle);
		
		for(int currentSlot = 0; currentSlot < menuSlots; currentSlot++) {
			menu.setItem(currentSlot, fillItem);
		}
		
		
		
		
		for(int count = 0; plugin.getConfigStringRaw("gui." + count) != null; count++) {
			
			String path = "gui." + count;
			/*
			 * Creating lots of variables for the menu. For organisation purposes.
			 */
			int itemSlot = plugin.getConfigInteger(path + ".slot");
			int itemAmount = plugin.getConfigInteger(path + ".amount");
			String itemName = plugin.getConfigString(path + ".name");
			Material itemMaterial = Material.valueOf(plugin.getConfigStringRaw(path + ".item"));
			
			/*
			 * Creating the list for the lore of an item.
			 */
			ArrayList<String> itemLore = new ArrayList<String>();
			for(String line : plugin.getConfig().getStringList(path + ".lore")) {
				itemLore.add(ChatColor.translateAlternateColorCodes('&', line));
			}
			/*
			 * Setting the actual item and adding 1 to the count to continue with the next item in the list.
			 */
			menu.setItem(itemSlot, createItem(itemMaterial, itemAmount, itemName, itemLore));
		}
		// Finally. Opening the menu for the player.
		p.openInventory(menu);
	}
}
