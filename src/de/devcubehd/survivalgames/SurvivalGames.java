package de.devcubehd.survivalgames;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import de.devcubehd.gamelib.GameLib;
import de.devcubehd.gamelib.features.disallowFeature.DisallowFeature;
import de.devcubehd.gamelib.features.disallowFeature.DisallowSettingsBuilder;
import de.devcubehd.gamelib.features.lobbyFeature.LobbyFeature;
import de.devcubehd.gamelib.features.lobbyFeature.LobbySettings;
import de.devcubehd.gamelib.features.mapFeature.MapFeature;
import de.devcubehd.gamelib.features.sqlFeature.SqlFeature;
import de.devcubehd.gamelib.features.sqlFeature.SqlSettings;
import de.devcubehd.survivalgames.events.GameEndListener;
import de.devcubehd.survivalgames.events.GameStartListener;
import de.devcubehd.survivalgames.game.ItemTool;
import de.devcubehd.survivalgames.setup.SetupCommand;

public class SurvivalGames extends JavaPlugin {
	
	private static SqlFeature sqlFeature;	
	private static MapFeature mapFeature;
	private static DisallowFeature disallowFeature;
	private static LobbyFeature lobbyFeature;
	
	public static enum GameState {
		LOBBY, FULL, INGAME, RESTART, SETUP
	}
	
	private static GameState gameState;
	
	public void onEnable() {
		
		GameLib.init("SurvivalGames", "");
		
		sqlFeature = GameLib.getFeatureManager().getFeature(SqlFeature.class);
		sqlFeature.setSettings(new SqlSettings("5.189.148.138", 3306, "SurvivalGames", "SurvivalGames", "PmCgqQwMX2cgCI4g", "Spawns"));
		sqlFeature.enable();
		
		mapFeature = GameLib.getFeatureManager().getFeature(MapFeature.class);
		mapFeature.enable();
				
		disallowFeature = GameLib.getFeatureManager().getFeature(DisallowFeature.class);
		disallowFeature.setSettings(DisallowSettingsBuilder.defaultDisallowBuilder().allowMove().build());
		disallowFeature.enable();
		
		lobbyFeature = GameLib.getFeatureManager().getFeature(LobbyFeature.class);
        lobbyFeature.setSettings(new LobbySettings(3, mapFeature.getManager().getSpawns().size() > 0 ? mapFeature.getManager().getSpawns().size() : 1, 20, "lobby", new Location(Bukkit.getWorld("lobby"), 702.5, 32, 704.5, 180, 0)));
        lobbyFeature.enable();
        
        setGameState(GameState.LOBBY);
        
        getInstance().getCommand("setup").setExecutor(new SetupCommand());
        
        Bukkit.getPluginManager().registerEvents(new GameStartListener(), SurvivalGames.getInstance());
        Bukkit.getPluginManager().registerEvents(new GameEndListener(), SurvivalGames.getInstance());
        
        ItemTool.init();
        
	}
	
	public void onDisable() {
		sqlFeature.disable();
	}
	
	public static SurvivalGames getInstance() {
        return getPlugin(SurvivalGames.class);
    }
	
	public static SqlFeature getSqlFeature() {
		return sqlFeature;
	}

	public static MapFeature getMapFeature() {
		return mapFeature;
	}
	
	public static DisallowFeature getDisallowFeature() {
		return disallowFeature;
	}
	
	public static LobbyFeature getLobbyFeature() {
		return lobbyFeature;
	}

	public static GameState getGameState() {
		return gameState;
	}

	public static void setGameState(GameState gameState) {
		SurvivalGames.gameState = gameState;
	}
	
}
