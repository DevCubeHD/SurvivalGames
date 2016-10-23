package de.devcubehd.survivalgames.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.devcubehd.gamelib.features.disallowFeature.DisallowSettingsBuilder;
import de.devcubehd.gamelib.features.lobbyFeature.GameStartEvent;
import de.devcubehd.survivalgames.SurvivalGames;
import de.devcubehd.survivalgames.SurvivalGames.GameState;

public class GameStartListener implements Listener {
	
	private static List<Player> players = new ArrayList<>();
	
	@EventHandler
	public void onGameStart(GameStartEvent e) {
		
		SurvivalGames.setGameState(GameState.INGAME);
		
		Bukkit.getPluginManager().registerEvents(new GameListener(), SurvivalGames.getInstance());
		
		List<Location> spawns = new ArrayList<>(SurvivalGames.getMapFeature().getManager().getSpawns());
		
		spawns.get(0).getWorld().getEntities().forEach(en->en.remove());
		
		Bukkit.getOnlinePlayers().forEach(pl-> {
			
			SurvivalGames.getLobbyFeature().getManager().getListener().sendTitle(pl, 10, 20, 10, "§2" + SurvivalGames.getMapFeature().getManager().getMapName(), "§7by " + SurvivalGames.getMapFeature().getManager().getMapBuilder());
			pl.teleport(spawns.get(0));
			spawns.remove(0);
			players.add(pl);
			
		});
		
		SurvivalGames.getDisallowFeature().setSettings(DisallowSettingsBuilder.defaultAllowBuilder().disallowMobSpawn().disallowPortalUse().disableTeleportOnStuck().build());
				
	}
	
	public static List<Player> getPlayers() {
		return players;
	}
	
}
