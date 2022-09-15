package Limbo.Shop;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Limbo.SimpleShop;
import Limbo.Message.Message;

public class Sell implements Listener{
	static int INVSLOT;
	static int SELLBUTTON;
	static int LISTBUTTON;
	static int SLOTSELL;
	SimpleShop m;
	Inventory invlist;
	SellLoader loader;
	double price = 0;
	static Map<Player, Inventory> inv;
	
	public Sell() {
		this.m = SimpleShop.getIntance();
		inv = new HashMap<Player, Inventory>();
		int con = m.getConfig().getInt("slot-sell");
		INVSLOT = con > 54? 54 : con < 18? 18 : con;
		SELLBUTTON = INVSLOT - 5;
		LISTBUTTON = INVSLOT - 1;
		SLOTSELL = INVSLOT - 9;
		loader = new SellLoader();
		invlist = Bukkit.createInventory(null, INVSLOT, "List Item");
	}

	@EventHandler
	public void onOpenInv(InventoryOpenEvent e) {
		createInv((Player) e.getPlayer());
		if(e.getInventory().equals(invlist)) {
			loader = new SellLoader();
			loadInv();
		}
	}
	
	public void createInv(Player p) {
		if(!inv.containsKey(p)) {
			Inventory temp = Bukkit.createInventory(p, INVSLOT, "SELL");
			temp.setItem(SELLBUTTON, createGuiItem(Material.EMERALD, true, SimpleShop.nonFormat("&r&lTotal $0"), 1));
			temp.setItem(LISTBUTTON, createGuiItem(Material.BOOK, true, "Item can sell", 1,"List of item you can sell here"));
			temp.setItem(SLOTSELL, createGuiItem(Material.BOOK, true, "Shop", 1));
			for(int i = SLOTSELL; i < INVSLOT; i++) {
				if(temp.getItem(i) == null) {
					temp.setItem(i, createGuiItem(Material.GRAY_STAINED_GLASS_PANE, true, " ", 1));
				}
			}
			inv.put(p, temp);
		}
	}

	private void updatePrice(InventoryClickEvent e) {
		price = 0;
		ItemStack it[] = e.getInventory().getContents();
		for (int i = 0; i < SLOTSELL; i++) {
			if(it[i] == null) continue;
			ItemStack cur = it[i];
			SellLoader sell = loader.getMap().get(cur.getType().toString());
			if(sell == null) continue;
			price += cur.getAmount() * sell.getPrice();
			e.getClickedInventory().clear(i);
		}
		
		ItemMeta im = e.getInventory().getItem(SELLBUTTON).getItemMeta();
		im.setDisplayName(SimpleShop.format("&r&lTotal $", String.valueOf(price)));
		e.getInventory().getItem(SELLBUTTON).setItemMeta(im);
	}
	
	
	public void loadInv() {
		int id = 0;

		invlist.setItem(SLOTSELL, createGuiItem(Material.BOOK, true, "Back", 1," "));
		for(SellLoader sell : loader.getMap().values()) {
			invlist.setItem(id, createGuiItem(sell.getItem(), true, "", 1, "Price $"+ String.valueOf(sell.getPrice())));
			id++;
		}
	}
	

	private ItemStack createGuiItem(ItemStack itemStack, boolean setMeta, String name, int amount, String... lore) {
		ItemStack item = itemStack;
		if(setMeta) {
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(name);
			meta.setLore(Arrays.asList(lore));
			item.setItemMeta(meta);
		}
		return item;
	}
	
	public static ItemStack createGuiItem(Material material, boolean setMeta, String name, int amount, String... lore) {
		ItemStack item = new ItemStack(material, amount);
		if(setMeta) {
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(name);
			meta.setLore(Arrays.asList(lore));
			item.setItemMeta(meta);
		}
		return item;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		ItemStack item = e.getCurrentItem();
		Player p = (Player) e.getWhoClicked();
		if(item == null || item.getType().isAir()) return;
		if(e.getInventory().equals(inv.get(p))) {
			Bukkit.getScheduler().runTaskLater(m, () -> {
				updatePrice(e);
			}, 10);
			if(e.getRawSlot() >= SLOTSELL && e.getRawSlot() < INVSLOT) {
				e.setCancelled(true);
			}
			if(item.equals(e.getInventory().getItem(SELLBUTTON))) {
				giveMoney(price, p);
				if(price > 0) SimpleShop.sendMessage(p, Message.TAKE_MONEY, price);
			}
			else if(item.equals(e.getInventory().getItem(LISTBUTTON))) {
				p.openInventory(invlist);
			}
			else if(item.equals(e.getInventory().getItem(SLOTSELL))) {
				p.openInventory(m.getShop().getInv());
			}
		}
		else if(e.getInventory().equals(invlist)) {
			e.setCancelled(true);
			if(item.equals(e.getInventory().getItem(SLOTSELL))) {
				p.openInventory(inv.get(p));
			}
		}
	}
	
	public void giveMoney(double value, Player player) {
		m.getEco().getEconomy().depositPlayer((OfflinePlayer) player, value);
	}
	
	public static Inventory getInv(Player p) {
		return inv.get(p);
	}

	public SellLoader getSell() {
		return this.loader;
	}

}
