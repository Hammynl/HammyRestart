package me.hammynl.restarter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.hammynl.restarter.commands.RestarterCommand;
import me.hammynl.restarter.utils.RestarterGUI;

public class Restarter extends JavaPlugin {
	public int timer;
	public String prefix = getConfString("prefix");
	
	public void onEnable() {
		@SuppressWarnings("unused")
		MetricsLite metrics = new MetricsLite(this);
		this.getCommand("restarter").setExecutor(new RestarterCommand());
		Bukkit.getPluginManager().registerEvents(new RestarterGUI(), this);
		saveDefaultConfig();
		timer = getConfig().getInt("time");
		runnable();
	}
	
	public String getConfString(String s) {	
		return ChatColor.translateAlternateColorCodes('&', getConfig().getString(s)); 
	}
	
	public int getConfInt(String s) { 
		return getConfig().getInt(s); 
	}
	
	public boolean getConfBool(String s) { 
		return getConfig().getBoolean(s); 
	}
	
	public void runnable() {
		new BukkitRunnable() {
			@Override
			public void run() {
				timer--;
				for(String s : getConfig().getStringList("executables")) {
					String[] split = s.split(":");
					int sec = Integer.valueOf(split[0]);
					String usage = split[1];
					String text = split[2].replace('&', '§');
					if(timer == sec) {
						if(usage.equals("MESSAGE")) for(Player all : Bukkit.getOnlinePlayers()) all.sendMessage(ChatColor.translateAlternateColorCodes('&', text));
						if(usage.equals("COMMAND")) for(Player p : Bukkit.getOnlinePlayers()) if(!p.hasPermission("restarter.executables.bypass")) Bukkit.dispatchCommand(p, text); 
						if(usage.equals("CONSOLE")) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), text);	
					}
				}
				if(timer < 0) {
					for(Player all : Bukkit.getOnlinePlayers()) all.kickPlayer(prefix + getConfString("kick-message")); 
					Bukkit.spigot().restart();
				}
			}
		}.runTaskTimer(this, 0, 20 * 1); 	
	}
}
