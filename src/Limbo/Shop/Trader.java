package Limbo.Shop;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

public class Trader {
	HashMap<Integer, ItemStack> listItem = new HashMap<>();
	UUID  uuid;
	String name;
	int slot, maxLen;
	ItemStack item;
	double price;
	
	public Trader(String name, UUID uuid, double price, ItemStack item, int slot) {
		this.name = name;
		this.uuid = uuid;
		this.price = price;
		this.slot = slot;
		this.item = item;
	}
	
	public void addItem(ItemStack item, int slot) {
		this.listItem.put(slot, item);
	}
	
	public void setLen(int maxLen) {
		this.maxLen = maxLen;
	}
	
	public String getName() {
		return this.name;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public ItemStack getItem() {
		return this.item;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public int getSlot() {
		return this.slot;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
}
