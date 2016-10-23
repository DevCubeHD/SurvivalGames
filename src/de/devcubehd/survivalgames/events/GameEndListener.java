package de.devcubehd.survivalgames.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import de.devcubehd.gamelib.features.disallowFeature.DisallowSettingsBuilder;
import de.devcubehd.gamelib.features.lobbyFeature.GameEndEvent;
import de.devcubehd.survivalgames.SurvivalGames;

public class GameEndListener implements Listener {
	
	@EventHandler
	public void onGameEnd(GameEndEvent e) {
				
		SurvivalGames.getDisallowFeature().setSettings(DisallowSettingsBuilder.defaultDisallowBuilder().allowMove().build());
		
		new BukkitRunnable() {

			public void run() {
				
				Bukkit.getOnlinePlayers().forEach(p-> {
					SurvivalGames.getLobbyFeature().getManager().getListener().resetPlayer(p);
				});
				
				Bukkit.broadcastMessage("§cDer Server startet in 10 Sekunden neu...");
				
			}			
			
		}.runTaskLater(SurvivalGames.getInstance(), 20L);
		
		
		
		new BukkitRunnable() {

			public void run() {
				
				Bukkit.getOnlinePlayers().forEach(p->p.kickPlayer("§cDer Server wird neugestartet."));
				Bukkit.shutdown();
				
			}			
			
		}.runTaskLater(SurvivalGames.getInstance(), 10*20L);
		
	}

}
