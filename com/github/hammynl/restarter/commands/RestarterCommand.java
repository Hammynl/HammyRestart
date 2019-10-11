package com.github.hammynl.restarter.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.hammynl.restarter.Restarter;
import com.github.hammynl.restarter.utils.RestarterGUI;

public class RestarterCommand implements CommandExecutor {
	
	private Restarter plugin = Restarter.getPlugin(Restarter.class);
	
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		 if(args.length == 0 && sender.hasPermission("restarter.info")) {
			for(String s : plugin.getConfig().getStringList("help-page")) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s)); 
			return true;
		} else if(args.length == 1 && args[0].equalsIgnoreCase("gui") && sender.hasPermission("restarter.gui")) {
			if(!(sender instanceof Player)) { sender.sendMessage("You cannot open a GUI via console!"); }
			Player p = (Player) sender;
			new RestarterGUI().restarterGUI(p);

		} else if(args.length == 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("restarter.reload")) {
			plugin.reloadConfig();
			sender.sendMessage(plugin.prefix + plugin.getConfString("reload-command"));

		} else if(!sender.hasPermission("restarter.gui") || !sender.hasPermission("restarter.reload") || !sender.hasPermission("restarter.info")) {
			sender.sendMessage(plugin.prefix + plugin.getConfString("no-permission"));
			return true;
		} 
		return true;
	}
}
