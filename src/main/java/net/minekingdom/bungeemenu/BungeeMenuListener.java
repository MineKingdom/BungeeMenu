package net.minekingdom.bungeemenu;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class BungeeMenuListener implements Listener {
	
	private Map<Material,String> servers;
	private Location chestLocation;
	
	public BungeeMenuListener(BungeeMenu plugin) {
		this.servers = new HashMap<Material,String>();
		ConfigurationSection section = plugin.getConfig().getConfigurationSection("servers");
		for (String key : section.getKeys(false)) {
			try {
				Material material = Material.matchMaterial(key);
				String server = section.getString(key);
				if (material == null || server == null) {
					throw new Exception();
				}
				this.servers.put(material,server);
			} catch (Exception e) {
				
			}
		}
		
		Vector v = plugin.getConfig().getVector("chest-vector", new Vector(0,0,0));
		World world = plugin.getServer().getWorld(plugin.getConfig().getString("chest-world", "world"));
		if (world != null) {
			this.chestLocation = new Location(world,v.getBlockX(),v.getBlockY(),v.getBlockZ());
		}
	}
	
	@EventHandler
    public void onPlayerConnect(PlayerJoinEvent join) {
            if (chestLocation == null) {
            	return;
            }
            Player player = join.getPlayer();
            player.getInventory().clear();
            Block chestBlock = chestLocation.getBlock();
            if (chestBlock.getType().equals(Material.CHEST)) {
                    Chest chestState = (Chest) chestBlock.getState();
                    Inventory inventory = chestState.getInventory();
                    ItemStack[] contents = inventory.getContents();
                    for (int i = 0; i < 9; ++i) {
                        player.getInventory().setItem(i,contents[i]);
                    }
                    player.getInventory().setHelmet(inventory.getItem(9));
                    player.getInventory().setChestplate(inventory.getItem(10));
                    player.getInventory().setLeggings(inventory.getItem(11));
                    player.getInventory().setBoots(inventory.getItem(12));
            }
    }
	
	
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		ItemStack itemStack = event.getPlayer().getItemInHand();
		
		if (itemStack != null && !event.getAction().equals(Action.PHYSICAL)) {
			String server = this.servers.get(itemStack.getType());
			if (server != null) {
				BungeeUtils.connect(event.getPlayer(), server);
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onClickInventory(InventoryClickEvent event) {
		if (!event.getWhoClicked().hasPermission("bungeemenu.bypass")) {
			event.setCancelled(true);
		}
	}
}
