package com.github.hammynl.restarter.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.hammynl.restarter.Restarter;
import com.github.hammynl.restarter.utils.RestarterMenu;

public class RestarterCommand implements CommandExecutor {
	
	private Restarter plugin = Restarter.getPlugin(Restarter.class);
	
	RestarterMenu RestarterMenu = new RestarterMenu();
	
	String[] helpPage = {
			"&6&l>>", 
			"&6&l>> &e&lRestarter &7| &eMade with <3 by Hammynl",
			"&6&l>>",
			"&6&l>> &e&lCommands",
			"&6&l>> &6/restarter [help] &7| &6Shows this help page",
			"&6&l>> &6/restarter reload &7| &6Reloads the plugin",
			"&6&l>> &6/restarter gui &7| &6Opens the built-in menu",
			"&6&l>> &6/restarter time &7| &6Shows the time left before restart!",
			"&6&l>>"
			};
	

	
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(args.length == 0 && sender.hasPermission("restarter.help")) {
			for(String lines : helpPage) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lines));
			}
		} 
		
		else if(args.length == 1 && args[0].equalsIgnoreCase("gui") && sender.hasPermission("restarter.gui")) {
			Player player = (Player) sender;
			RestarterMenu.openMenu(player);
		} 
		
		else if(args.length == 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("restarter.reload")) {
			plugin.reloadConfig();
			sender.sendMessage(plugin.prefix + " &aPlugin reloaded succesfully!");
		} 
		
		else if(args.length == 1 && args[0].equalsIgnoreCase("time") && sender.hasPermission("restarter.time")) {
			
			sender.sendMessage(plugin.prefix + plugin.getConfigString("time-message")
			.replace("%seconds", plugin.timer + "")
			.replace("%minutes", plugin.timer/60 + "")
			.replace("%hours",  plugin.timer/3600 + ""));
		} 
		 
		else {
			sender.sendMessage(plugin.prefix + plugin.getConfigString("no-permission"));
		} 
		
		return true;
	}
}
