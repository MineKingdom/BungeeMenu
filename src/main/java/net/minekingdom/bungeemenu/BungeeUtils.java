package net.minekingdom.bungeemenu;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.entity.Player;

public final class BungeeUtils {
	private BungeeUtils() {
	}
	
	public static void connect(Player player, String server) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		 
		try {
		    out.writeUTF("Connect");
		    out.writeUTF(server);
		} catch (IOException e) {
		    // Can never happen
		}
		player.sendPluginMessage(BungeeMenu.getInstance(), "BungeeCord", b.toByteArray());
	}
}
