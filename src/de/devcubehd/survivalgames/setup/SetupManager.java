package de.devcubehd.survivalgames.setup;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.devcubehd.gamelib.GameLib;
import de.devcubehd.survivalgames.SurvivalGames;
import de.devcubehd.survivalgames.SurvivalGames.GameState;

public class SetupManager {
	
	private static List<Player> players = new ArrayList<>();
	
	private static SetupListener setup;
		
	public static void enable() {
		
		SurvivalGames.setGameState(GameState.SETUP);
		setup = new SetupListener();
		Bukkit.getPluginManager().registerEvents(setup, SurvivalGames.getInstance());
		SurvivalGames.getDisallowFeature().disable();
		SurvivalGames.getLobbyFeature().disable();
		
	}
	
	public static void disable() {
		
		Bukkit.getOnlinePlayers().forEach(p->p.kickPlayer("§7Server startet wegen einem §8Setup §7neu."));
		Bukkit.shutdown();
		
	}
	
	public static void change(Player player) {
		
		if(players.contains(player)) {
			
			player.sendMessage(GameLib.getGamePrefix() + "You §8left §7the Setup-Mode.");
			players.remove(player);
			player.setGameMode(GameMode.ADVENTURE);
			
		} else {
			
			player.sendMessage(GameLib.getGamePrefix() + "You §8joined §7the Setup-Mode.");
			players.add(player);
			player.setGameMode(GameMode.CREATIVE);
			player.getInventory().clear();
			
			ItemStack item = new ItemStack(Material.SIGN);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§8» §7World - Creator");
			item.setItemMeta(meta);
			
			player.getInventory().setItem(0, item);
			
			item = new ItemStack(Material.SIGN); 
			meta = item.getItemMeta();
			meta.setDisplayName("§8» §7World - Teleporter");
			item.setItemMeta(meta);
			
			player.getInventory().setItem(1, item);
			
			item = new ItemStack(Material.ARMOR_STAND); 
			meta = item.getItemMeta();
			meta.setDisplayName("§8» §7Spawn - Creator");
			item.setItemMeta(meta);
			
			player.getInventory().setItem(3, item);
			
		}
		
		if(players.size() == 1) enable();
		if(players.size() == 0) disable();
		
	}
	
	public static List<Player> getPlayers() {
		return players;
	}

}
