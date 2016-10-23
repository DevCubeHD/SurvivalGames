package de.devcubehd.survivalgames.setup;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import de.devcubehd.gamelib.GameLib;
import de.devcubehd.survivalgames.SurvivalGames;

public class SetupListener implements Listener {
	
	@EventHandler
	public void onReady(SignChangeEvent e) {	
		
		e.getBlock().setType(Material.AIR);
		
		if(!(e.getLine(0).isEmpty() && e.getLine(1).isEmpty() && e.getLine(2).isEmpty() && e.getLine(3).isEmpty())) {
			
			if(e.getLine(3).equals("CREATE")) {
				
				e.getPlayer().sendMessage(GameLib.getGamePrefix() + "Erstelle neue Karte...");
				
				try {
					
					SurvivalGames.getSqlFeature().getManager().getTable("Maps").insertEntry(new String[]{"Name", "Builder", "Folder"}, new Object[]{e.getLine(0), e.getLine(1), e.getLine(2)});
			
					World w = Bukkit.getServer().createWorld(new WorldCreator(e.getLine(2)));
										
					SurvivalGames.getMapFeature().getManager().resetWorld(w);
					
					e.getPlayer().teleport(w.getSpawnLocation());
					
				} catch (SQLException e1) {
				
					e1.printStackTrace();
					
				}
				
				e.getPlayer().sendMessage(GameLib.getGamePrefix() + "Erstellt! " + e.getLine(0) + " von " + e.getLine(1) + " findest du im Ordner " + e.getLine(2) + ".");
				
			} 
			
			if(e.getLine(3).equals("TELEPORT")) {
				
				World w = Bukkit.getServer().createWorld(new WorldCreator(e.getLine(0)));
								
				SurvivalGames.getMapFeature().getManager().resetWorld(w);
				
				try {
					
					for(Object loc : SurvivalGames.getSqlFeature().getManager().getTable("Spawns").getSortedList("Location", "ID")) {
						
						Entity armorstand = w.spawnEntity(SurvivalGames.getMapFeature().getManager().decodeLoc((String) loc), EntityType.ARMOR_STAND);
						ArmorStand entity = (ArmorStand) armorstand;
						entity.setArms(true);
						entity.setBasePlate(false);
						entity.setHelmet(new ItemStack(Material.SKULL_ITEM));
						
						entity.setCustomName("Spawn-" + SurvivalGames.getSqlFeature().getManager().getTable("Spawns").getInt("ID", "Location", loc));
						entity.setCustomNameVisible(true);
						
					}
					
				} catch (SQLException e1) {
					
					e1.printStackTrace();
					
				}
				
				e.getPlayer().teleport(w.getSpawnLocation());
				
			}
						
		}
				
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onStart(BlockPlaceEvent e) {
		
		Player p = e.getPlayer();
		
		if((e.getBlock().getType().equals(Material.SIGN_POST) || e.getBlock().getType().equals(Material.WALL_SIGN)) && SetupManager.getPlayers().contains(p)) {
			
			for(int i = 0; i <= 20; i++) p.sendMessage("");
			
			if(p.getItemInHand().getItemMeta().getDisplayName().contains("Teleporter")) {
				
				p.sendMessage(GameLib.getGamePrefix() + "§8So teleportierst du dich:");
				p.sendMessage(GameLib.getGamePrefix() + "§8Line 1:§7 Folder Name");
				p.sendMessage(GameLib.getGamePrefix() + "§8Line 4:§7 \"TELEPORT\"");
				
			} else {
				
				p.sendMessage(GameLib.getGamePrefix() + "§8So erstellst du eine neue Karte:");
				p.sendMessage(GameLib.getGamePrefix() + "§8Line 1:§7 Map Name");
				p.sendMessage(GameLib.getGamePrefix() + "§8Line 2:§7 Map Builder");
				p.sendMessage(GameLib.getGamePrefix() + "§8Line 3:§7 Folder Name");
				p.sendMessage(GameLib.getGamePrefix() + "§8Line 4:§7 \"CREATE\"");
				
			}
			
		}
		
	}
	
	@EventHandler
	public void onSpawn(EntitySpawnEvent e) {
				
		if(e.getEntityType().equals(EntityType.ARMOR_STAND) && !e.getEntity().isGlowing()) {
			
			ArmorStand entity = (ArmorStand) e.getEntity();
			entity.setArms(true);
			entity.setBasePlate(false);
			entity.setHelmet(new ItemStack(Material.SKULL_ITEM));
			entity.setGlowing(true);
			
			try {
				
				SurvivalGames.getSqlFeature().getManager().getTable("Spawns").insertEntry(new String[]{"Location"}, new Object[]{ SurvivalGames.getMapFeature().getManager().encodeLoc(e.getLocation().subtract(0.00, 1.00, 0.00).getBlock().getType().toString().endsWith("_PLATE") ? entity.getLocation().subtract(0.00, 1.00, 0.00) : entity.getLocation()) });
				entity.setCustomName("Spawn-" + SurvivalGames.getSqlFeature().getManager().getTable("Spawns").getLastInsertID());
				entity.setCustomNameVisible(true);
				
			} catch (SQLException e1) {
				
				e1.printStackTrace();
				
			}
			
		} else {
			
			e.getEntity().setGlowing(true);
			
		}
		
	}
	
	@EventHandler
    public void onArmorStandDestroy(EntityDamageByEntityEvent e){
      
		if(e.getEntity().getType().equals(EntityType.ARMOR_STAND)) {
    	   			
			try {
				
				SurvivalGames.getSqlFeature().getManager().getTable("Spawns").delete("ID", e.getEntity().getCustomName().split("-")[1]);
				
			} catch (SQLException e1) {
				
				e1.printStackTrace();
				
			}
    	   
		}
       
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		
		if(SetupManager.getPlayers().contains(e.getPlayer())) {
			
			SetupManager.getPlayers().remove(e.getPlayer());
			
		}
		
	}
	
	public void disable() {
        HandlerList.unregisterAll(this);
    }

}
