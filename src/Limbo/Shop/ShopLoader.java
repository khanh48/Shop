package Limbo.Shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import Limbo.SimpleShop;

public class ShopLoader {
	final SimpleShop m;
	String name, type, type_enchant, tag;
	List<String> lore, id;
	int limit, amount;
	double price = 0;
	HashMap<Integer, ShopLoader> map;
	
	public ShopLoader() {
		this.m = SimpleShop.getIntance();
		map = new HashMap<>();
		loadConfig();
	}
	
	public ShopLoader(String tag, String name, String type, int amount, double price, int limit, String type_enchant, List<String> id, List<String> lore) {
		this.m = SimpleShop.getIntance();
		this.tag = tag;
		this.name = name;
		this.amount = amount;
		this.type = type;
		this.price = price;
		this.limit = limit;
		this.type_enchant = type_enchant;
		this.id = id;
		this.lore = lore;
	}
	
	public void loadConfig() {
		ConfigurationSection listBuy = m.shopConfig.getConfig().getConfigurationSection("items");
		if(listBuy == null) return;
		int i = 0;
		for(String lb: listBuy.getKeys(false)) {
			//tag
			tag = lb;
			//name
			name = m.shopConfig.getConfig().getString("items."+ lb + ".name");
			//type
			type = m.shopConfig.getConfig().getString("items."+ lb + ".type");
			
			amount = m.shopConfig.getConfig().getInt("items."+ lb + ".amount");
			
			//price
			price = m.shopConfig.getConfig().getDouble("items."+ lb + ".price");
			//limit
			limit = m.shopConfig.getConfig().getInt("items."+ lb + ".limit");
			//enchants
			//type-enchant
			type_enchant = m.shopConfig.getConfig().getString("items."+ lb + ".enchants.type");
			lore = m.shopConfig.getConfig().getStringList("items."+ lb + ".lore");
			//id-enchant
			id = m.shopConfig.getConfig().getStringList("items."+ lb + ".enchants.id");
			//lore
			map.put(i, new ShopLoader(tag, name, type, amount, price, limit, type_enchant, id, lore));
			i++;
		}
	}
	public void reload() {
		map.clear();
		loadConfig();
	}
	
	public String getTag() {
		return this.tag;
	}
	
	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type.toUpperCase();
	}
	
	public boolean isItemsAdder() {
		List<String> namespace = m.shopConfig.getConfig().getStringList("itemsadder.namespace");
		if(namespace != null)
			for (String string : namespace)
				if(getType().contains(string.toLowerCase()))
					return true;
		return false;
	}
	
	public Integer getAmount() {
		return this.amount;
	}

	public double getPrice() {
		return this.price;
	}
	
	public int getLimit() {
		return this.limit;
	}

	public String getTypeEnchant() {
		return this.type_enchant;
	}

	public ArrayList<String> getID() {
		ArrayList<String> tmp = new ArrayList<>();
		for (String s : id) {
			tmp.add(s.split("-")[0].toLowerCase());
		}
		return tmp;
	}
	
	public ArrayList<Integer> getLevel() {
		ArrayList<Integer> tmp = new ArrayList<>();
		for (String s : id) {
			tmp.add(Integer.parseInt(s.split("-")[1]));
		}
		return tmp;
	}

	public List<String> getLore() {
		return this.lore;
	}

	public HashMap<Integer, ShopLoader> getMap() {
		return this.map;
	}
}
