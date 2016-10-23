package de.devcubehd.survivalgames.events;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import de.devcubehd.gamelib.GameLib;
import de.devcubehd.gamelib.features.lobbyFeature.GameEndEvent;
import de.devcubehd.gamelib.features.lobbyFeature.LobbyListener;
import de.devcubehd.survivalgames.SurvivalGames;
import de.devcubehd.survivalgames.game.ItemTool;

public class GameListener implements Listener {
		
	private HashMap<Location, ItemStack[]> chests = new HashMap<>();
	
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent e) {
		
		if(e.getInventory().getType().equals(InventoryType.CHEST)) {
						
			if(!chests.containsKey(e.getInventory().getLocation())) {
				
				ItemTool.setRandomItems(e.getInventory());
				chests.put(e.getInventory().getLocation(), e.getInventory().getContents());
				
			} else {
				
				e.getInventory().setContents(chests.get(e.getInventory().getLocation()));
				
			}
			
			
		}
		
	}
	
	@EventHandler
	public void onInventoryOpen(InventoryCloseEvent e) {
		
		if(e.getInventory().getType().equals(InventoryType.CHEST) && chests.containsKey(e.getInventory().getLocation())) {
			
			chests.put(e.getInventory().getLocation(), e.getInventory().getContents());
			
		}
		
	}
	
	@EventHandler
    public void onJoin(PlayerJoinEvent e) {
		
		Player p = e.getPlayer();
		
		e.setJoinMessage("");
		
		p.setGameMode(GameMode.SPECTATOR);
		p.playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 100);
		p.teleport(LobbyListener.getSpawn());
		p.sendMessage(GameLib.getGamePrefix() + "Das Spiel läuft bereits. Du schaust jetzt zu.");
		
	}
	
	@EventHandler
    public void onQuit(PlayerQuitEvent e) {
		e.setQuitMessage("");
		removePlayer(e.getPlayer());
	}
	
	@EventHandler
    public void onDeath(PlayerDeathEvent e) {
		
		if(e.getEntityType().equals(EntityType.PLAYER)) {
						
			e.setDeathMessage("");
			e.getEntity().spigot().respawn();
			
		}
		
	}
	
	@EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
		
		Player p = e.getPlayer();
		
		e.setRespawnLocation(p.getLocation());
		SurvivalGames.getLobbyFeature().getManager().getListener().sendTitle(p, 0, 60, 40, "§c" + "Gestorben!", "§7" + "Du schaust jetzt zu.");
		SurvivalGames.getLobbyFeature().getManager().getListener().resetPlayer(p);
		p.setGameMode(GameMode.SPECTATOR);
		removePlayer(p);
		
	}
	
	private void removePlayer(Player p) {
		
		GameStartListener.getPlayers().remove(p);
		
		if(GameStartListener.getPlayers().size() <= 1) {
			
			GameStartListener.getPlayers().get(0).setGameMode(GameMode.SPECTATOR);
			SurvivalGames.getLobbyFeature().getManager().getListener().sendTitle(GameStartListener.getPlayers().get(0), 0, 60, 40, "§2Gewonnen", "§7Du bist der letzte überlebende.");
			Bukkit.getPluginManager().callEvent(new GameEndEvent());
			disable();
			
		} else {
			
			Bukkit.broadcastMessage(GameLib.getGamePrefix() + "Der Spieler §8" + p.getName() + "§7 ist gestorben. §8" + GameStartListener.getPlayers().size() + "§7 Spieler verbleibend.");
			
		}
		
	}
	
	public void disable() {
        HandlerList.unregisterAll(this);
    }

}
