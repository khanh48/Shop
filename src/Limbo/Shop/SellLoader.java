package Limbo.Shop;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import Limbo.SimpleShop;
import dev.lone.itemsadder.api.CustomStack;

public class SellLoader {
	String name;
	ItemStack itemStack;
	double price;
	SimpleShop m;
	List<SellLoader> item;
	
	public SellLoader() 
	{
		this.m = SimpleShop.getIntance();
		item = new ArrayList<>();
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
			ItemStack is;
			if(isItemsAdder(sp[0]) && m.itemsAdderIsEnable()) {
				CustomStack customStack = CustomStack.getInstance(sp[0]);
				is = customStack.getItemStack();
			}
			else {
				is = new ItemStack(Material.getMaterial(sp[0].toUpperCase()));
			}
			
			item.add(new SellLoader(sp[0].toUpperCase(), is, Double.parseDouble(sp[1])));
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
	
	public boolean isItemsAdder() {
		List<String> namespace = m.shopConfig.getConfig().getStringList("itemsadder.namespace");
		if(namespace != null)
			for (String string : namespace)
				if(getName().contains(string.toLowerCase()))
					return true;
		return false;
	}

	public boolean isItemsAdder(String type) {
		List<String> namespace = m.shopConfig.getConfig().getStringList("itemsadder.namespace");
		if(namespace != null)
			for (String string : namespace)
				if(type.toLowerCase().contains(string.toLowerCase()))
					return true;
		return false;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public List<SellLoader> getMap() {
		return this.item;
	}
}
