package Limbo.Shop;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import Limbo.SimpleShop;

public class SellLoader {
	String name;
	ItemStack itemStack;
	double price;
	SimpleShop m;
	HashMap<String, SellLoader> item;
	
	public SellLoader() 
	{
		this.m = SimpleShop.getIntance();
		item = new HashMap<>();
		loadConfig();
	}
	
	public SellLoader(String name, ItemStack itemStack, double price) {
		this.name = name;
		this.itemStack = itemStack;
		this.price = price;
	}
	
	protected void loadConfig() {
		List<String> type = m.shopConfig.getConfig().getStringList("sell");
		if(type == null) return;
		for (String s : type) {
			String []sp = s.split("-");
			ItemStack is = null;
			try {
				is = new ItemStack(Material.getMaterial(sp[0].toUpperCase()));
			}catch (Exception e) {
				SimpleShop.sendMessage(Bukkit.getConsoleSender(), "&cCan't load the item "+ sp[0] + " recheck material in shop.yml!");
			}
			
			item.put(sp[0].toUpperCase(), new SellLoader(sp[0].toUpperCase(), is, Double.parseDouble(sp[1])));
		}
	}
	
	public void reload() {
		item.clear();
		loadConfig();
	}
	
	public String getName() {
		return this.name;
	}
	
	public ItemStack getItem() {
		return itemStack;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public HashMap<String, SellLoader> getMap() {
		return this.item;
	}
}
