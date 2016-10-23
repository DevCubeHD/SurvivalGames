package de.devcubehd.survivalgames.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemTool {
		
	private static List<ItemStack> items;	
	
	public static void init() {
		
		items = new ArrayList<>();
		
		addItemToList(new ItemStack(Material.WOOD_AXE, 1)				, 3);
		addItemToList(new ItemStack(Material.APPLE, 3)					, 3);
		addItemToList(new ItemStack(Material.STICK, 2)					, 2);
		addItemToList(new ItemStack(Material.BAKED_POTATO, 2)			, 2);
		addItemToList(new ItemStack(Material.DIAMOND, 1)				, 1);
		addItemToList(new ItemStack(Material.WOOD_SWORD, 1)				, 4);
		addItemToList(new ItemStack(Material.STONE_SWORD, 1)			, 2);
		addItemToList(new ItemStack(Material.IRON_INGOT, 2)				, 1);
		addItemToList(new ItemStack(Material.FLINT, 1)					, 2);
		addItemToList(new ItemStack(Material.FLINT_AND_STEEL, 1)		, 1);
		addItemToList(new ItemStack(Material.WEB, 1)					, 2);
		addItemToList(new ItemStack(Material.EXP_BOTTLE, 3)				, 1);
		addItemToList(new ItemStack(Material.GOLD_INGOT, 2)				, 2);
		addItemToList(new ItemStack(Material.LEATHER_HELMET, 1)			, 3);
		addItemToList(new ItemStack(Material.LEATHER_CHESTPLATE, 1)		, 3);
		addItemToList(new ItemStack(Material.LEATHER_LEGGINGS, 1)		, 3);
		addItemToList(new ItemStack(Material.LEATHER_BOOTS, 1)			, 3);
		addItemToList(new ItemStack(Material.GOLD_HELMET, 1)			, 2);
		addItemToList(new ItemStack(Material.GOLD_CHESTPLATE, 1)		, 2);
		addItemToList(new ItemStack(Material.GOLD_LEGGINGS, 1)			, 2);
		addItemToList(new ItemStack(Material.GOLD_BOOTS, 1)				, 2);
		addItemToList(new ItemStack(Material.CHAINMAIL_HELMET, 1)		, 2);
		addItemToList(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1)	, 1);
		addItemToList(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1)		, 1);
		addItemToList(new ItemStack(Material.CHAINMAIL_BOOTS, 1)		, 2);
		addItemToList(new ItemStack(Material.IRON_HELMET, 1)			, 1);
		addItemToList(new ItemStack(Material.IRON_CHESTPLATE, 1)		, 1);
		addItemToList(new ItemStack(Material.IRON_LEGGINGS, 1)			, 1);
		addItemToList(new ItemStack(Material.IRON_BOOTS, 1)				, 1);
		addItemToList(new ItemStack(Material.RAW_BEEF, 3)				, 2);
		addItemToList(new ItemStack(Material.RAW_CHICKEN, 2)			, 2);
		addItemToList(new ItemStack(Material.RAW_FISH, 2)				, 2);
		addItemToList(new ItemStack(Material.BOW, 1)					, 1);
		addItemToList(new ItemStack(Material.ARROW, 6)					, 3);
		addItemToList(new ItemStack(Material.GOLDEN_APPLE, 1)			, 1);
		addItemToList(new ItemStack(Material.CARROT_ITEM, 3)			, 2);
		addItemToList(new ItemStack(Material.BREAD, 2)					, 2);
		addItemToList(new ItemStack(Material.WHEAT, 5)					, 2);
		
		Collections.shuffle(items);
		
	}
	
	public static void addItemToList(ItemStack item, int amount) {
		
		for(int i = 0; i < amount; i++) {
			items.add(item);
		}
		
	}
	
	public static List<ItemStack> getItemList() {
		return items;		
	}
	
	private static ItemStack getRandomItem() {
		Random rdm = new Random();
		int randomNum = rdm.nextInt(items.size());
	    return items.get(randomNum);
	}
	
	public static void setRandomItems(Inventory inv) {
		
		inv.clear();
		
		Random rdm = new Random();
		
		int amount = 2;
		
		amount += rdm.nextInt(3);
		
		while(amount != 0) {
			
			int slot = rdm.nextInt(inv.getSize());
			
			if(inv.getItem(slot) == null || inv.getItem(slot).getType().equals(Material.AIR)) {
				
				ItemStack item = getRandomItem();
				int items = rdm.nextInt(item.getAmount()+1);
				item.setAmount(items < 1 ? 1 : items);
				inv.setItem(slot, item);
				
				amount--;
				
			}
			
		}
		
	}

}
