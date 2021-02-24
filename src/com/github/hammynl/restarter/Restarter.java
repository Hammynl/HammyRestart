package com.github.hammynl.restarter;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.hammynl.restarter.commands.RestarterCommand;
import com.github.hammynl.restarter.events.ClickEvent;

import net.md_5.bungee.api.ChatColor;

public class Restarter extends JavaPlugin {
	
	/*
	 * Declaring some global variables
	 */
	public int timer;
	public String prefix = getConfigString("prefix");
	
	public void onEnable() {
		@SuppressWarnings("unused")
		
		Metrics metrics = new Metrics(this);
		this.getCommand("restarter").setExecutor(new RestarterCommand());
		Bukkit.getPluginManager().registerEvents(new ClickEvent(), this);
		timer = getConfigInteger("time");
		
		saveDefaultConfig();
		startTimer();
	}
	
	public String getConfigString(String s) {	
		return ChatColor.translateAlternateColorCodes('&', getConfig().getString(s)); 
	}
	
	public String getConfigStringRaw(String s) {	
		return getConfig().getString(s); 
	}
	
	public int getConfigInteger(String s) { 
		return getConfig().getInt(s); 
	}
	
	public boolean getConfigBoolean(String s) { 
		return getConfig().getBoolean(s);
	}
	/*
	 * Method to restart the server so it can be globally used.
	 */
	public void restartServer() {
		
		for(Player players : Bukkit.getOnlinePlayers()) {
			players.kickPlayer(prefix + getConfigString("kick-message")); 
		}
		
		if(getConfigBoolean("use-restart-script")) {
			Bukkit.spigot().restart();
		} 
		else {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
		}
	}
	
	public void startTimer() {
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				/*
				 * Declaring a count for the executables in the runnable. So that the counter automatically resets to 0.
				 * Simultaneously removing 1 second from the global timer integer.
				 */
				int executableCount = 0;
				timer--;
				
				while(getConfigStringRaw("executables." + executableCount) != null) {
					
					/*
					 * Creating paths to further use in statements
					 */
					String executableInput = getConfigString("executables." + executableCount + ".input");
					String executableType = getConfigString("executables." + executableCount + ".type");
					int executableTime = getConfigInteger("executables." + executableCount + ".time");
					
					if(timer == executableTime) {
						
						/*
						 *  Checking is the executableType is either a message, console command or player command
						 */
						if(executableType.equalsIgnoreCase("message")) {
							for(Player player : Bukkit.getOnlinePlayers()) {
								player.sendMessage(prefix + executableInput);
							}
						} 
						
						else if(executableType.equalsIgnoreCase("players")) {
							for(Player player : Bukkit.getOnlinePlayers()) {
								Bukkit.dispatchCommand(player, executableInput);
							}
						} 
						
						else if(executableType.equalsIgnoreCase("console")) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), executableInput);
						}
					}
					/*
					 * Adding another to the executableCount to check all of the executables
					 */
					executableCount++;
				}
				
				/*
				 * When the timer hits 0 or under 0. 
				 */
				if(timer < 0) {
					restartServer();
				}
			}
		}.runTaskTimer(this, 0, 20 * 1); 	
	}
}
