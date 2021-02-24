package com.github.hammynl.restarter.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.hammynl.restarter.Restarter;

public class ClickEvent implements Listener {
	
	private Restarter plugin = Restarter.getPlugin(Restarter.class);
	
	@EventHandler
	public void clickCheck(InventoryClickEvent event) {
		try {
			
			String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
			String menuTitle = event.getView().getTitle();
			
			if(menuTitle.equals(null)) {
				return;
			} 
			else if(menuTitle.equals(plugin.getConfigString("gui.title"))) {
				
				event.setCancelled(true);
				for(int count = 0; plugin.getConfig().getString("gui." + count) != null; count++) {
					/*
					 * Creating some shortcuts for cleaner code and easier editing
					 */
					String path = "gui." + count;
					String buttonType = plugin.getConfigString(path + ".button.type");
					String buttonValue = path + ".button.value";
					
					if(itemName.equals(plugin.getConfigString(path + ".name"))) {
						/*
						 * Going through all button types. And performing the action with the defined value
						 */
						if(buttonType.equalsIgnoreCase("removal")) {
							plugin.timer -= plugin.getConfigInteger(buttonValue);
						} 
						
						else if(buttonType.equalsIgnoreCase("addition")) {
							plugin.timer += plugin.getConfigInteger(buttonValue);
						} 
						
						else if(buttonType.equalsIgnoreCase("timestamp")) {
							plugin.timer = plugin.getConfigInteger(buttonValue);
						}
						
						else if(buttonType.equalsIgnoreCase("shutdown")) {
							plugin.restartServer();
						}
					}
					
				}
			}
		} catch(NullPointerException ex) {}
	}
}
