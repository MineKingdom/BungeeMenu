package net.minekingdom.bungeemenu;

import org.bukkit.plugin.java.JavaPlugin;

public class BungeeMenu extends JavaPlugin {
	
	private static BungeeMenu instance;

	public void onEnable() {
		instance = this;
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getPluginManager().registerEvents(new BungeeMenuListener(this), this);
		this.saveDefaultConfig();
	}
	
	public void onDisable() {
		
	}
	
	public static BungeeMenu getInstance() {
		return instance;
	}
}
