package Limbo.Shop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import dev.lone.itemsadder.api.CustomStack;
import Limbo.SimpleShop;
import Limbo.Message.Message;

public class Shop implements Listener{
	static int INVSLOT;
	static int SELLBUTTON;
	int day, year;
	Inventory inv;
	ShopLoader shopLoader;
	SimpleShop m;
	Calendar date;
	private static HashMap<Integer, Double> mapPrice;
	HashMap<String, Integer> listLimit;
	private static Enchantments enchant;
	
	public Shop() {
		this.m = SimpleShop.getIntance();
		date = Calendar.getInstance();
		enchant = new Enchantments();
		listLimit = new HashMap<>();
		mapPrice = new HashMap<>();
		shopLoader = new ShopLoader();
		int con = m.getConfig().getInt("slot-shop");
		INVSLOT = con > 54? 54 : con < 18? 18 : con;
		SELLBUTTON = INVSLOT - 1;
		inv = Bukkit.createInventory(null, INVSLOT, "SHOP");
		inv.setItem(SELLBUTTON, Sell.createGuiItem(Material.BOOK, true, "SELL SHOP", 1));
	}
	
	void loadInv() {
		int count = 0;
		mapPrice.clear();

		for(ShopLoader map : shopLoader.getMap().values()) {
			ItemStack is = null;
			if(map.isItemsAdder() && m.itemsAdder) {
				CustomStack ia = CustomStack.getInstance(map.getType().toLowerCase());
				if(ia != null)
					is = ia.getItemStack();	
			}
			else 
				is = new ItemStack(Material.getMaterial(map.getType()), 1);
			if(is == null) continue;
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(SimpleShop.nonFormat(map.getName()));
			map.getLore().add(0 ,"Price $" + String.valueOf(map.getPrice()));
			im.setLore(SimpleShop.format(map.getLore()));
			map.getLore().remove(0);
			try {
				for (int en = 0; en < map.getID().size(); en++) {
					Enchantment enc = enchant.getEnchantment(map.getID().get(en));
					if(enc == null) continue;
					if(map.getTypeEnchant().equalsIgnoreCase("book")) {
						EnchantmentStorageMeta enchantBook = (EnchantmentStorageMeta) im;
						enchantBook.addStoredEnchant(enc, map.getLevel().get(en), true);
						im = (ItemMeta)enchantBook;
					}
					else if(map.getTypeEnchant().equalsIgnoreCase("normal")){
						im.addEnchant(enc, map.getLevel().get(en), true);
					}
				}
			}catch (Exception e) {
			}
			is.setItemMeta(im);
			
			mapPrice.put(count, map.getPrice());
			is.setAmount(map.getAmount() / 1 > 0? map.getAmount() : 1);
			inv.setItem(count, is);
			count++;
		}
		
		for(int i = 0; i < INVSLOT; i++) {
			if(inv.getItem(i) == null) {
				inv.setItem(i, Sell.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, true, " ", 1));
			}
		}
	}
	
	public void loadConfig(Player p) {
		day = m.dataConfig.getConfig().getInt("limit." + p.getName() + ".day");
		year = m.dataConfig.getConfig().getInt("limit." + p.getName() + ".year");
		List<String> tmpL = m.dataConfig.getConfig().getStringList("limit." + p.getName() + ".amount");
		if(tmpL == null) return;
		for (String l : tmpL) {
			listLimit.put(l.split("-")[0], Integer.parseInt(l.split("-")[1]));
		}
	}
	
	void saveList(HashMap<String, Integer> sl, Player p) {
		ArrayList<String> tem = new ArrayList<>();
		for (ShopLoader buy : shopLoader.getMap().values()) {
			if(sl.get(buy.getTag()) == null) {
				tem.add(buy.getTag() + "-" + "0");
				continue;
			}
			tem.add(buy.getTag() + "-" + String.valueOf(sl.get(buy.getTag())));
		}
		m.dataConfig.getConfig().set("limit." + p.getName() + ".amount", tem);
		m.dataConfig.saveConfig();
	}
	
	public boolean canBuy(Player p, String tag, int limit) {
		loadConfig(p);
		if(limit == 0) {
			int tmp = 0; 
			if(listLimit.containsKey(tag))
				tmp = listLimit.get(tag);
			listLimit.put(tag, tmp + 1);
			saveList(listLimit, p);
			return true;
		}
		if(date.get(Calendar.DAY_OF_YEAR) > day ||  date.get(Calendar.YEAR) > year) {
			listLimit.clear();
			m.dataConfig.getConfig().set("limit." + p.getName() + ".amount", null);
			m.dataConfig.getConfig().set("limit." + p.getName() + ".day", date.get(Calendar.DAY_OF_YEAR));
			m.dataConfig.getConfig().set("limit." + p.getName() + ".year", date.get(Calendar.YEAR));
			m.dataConfig.saveConfig();
		}

		if(listLimit.get(tag) == null) {
			listLimit.put(tag, 0);
		}
		if(listLimit.get(tag) >= limit)
			return false;
		listLimit.put(tag, listLimit.get(tag) + 1);
		saveList(listLimit, p);
		return true;
	}
	

	@EventHandler
	public void onOpenS(InventoryOpenEvent e) {
		if(e.getInventory().equals(inv)) {
			this.loadInv();
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		loadConfig((Player) e.getPlayer());
	}

	@EventHandler
	public void onClickBuyShop(InventoryClickEvent e) {
		ItemStack it = e.getCurrentItem();
		Player p = (Player) e.getWhoClicked();
		if(!e.getInventory().equals(inv)) return;
		if(it == null || it.getType().isAir()) return;
		if(e.getClickedInventory().equals(e.getView().getTopInventory())) {
			e.setCancelled(true);
			if(it.equals(e.getInventory().getItem(SELLBUTTON))) {
				p.openInventory(Sell.getInv(p));
				return;
			}
			if(e.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE) return;
			
			double tmp;
			tmp = mapPrice.get(e.getRawSlot());
			if(m.getEco().getEconomy().getBalance((OfflinePlayer) p) >= tmp) {
				if(canBuy(p, shopLoader.getMap().get(e.getRawSlot()).getTag(), shopLoader.getMap().get(e.getRawSlot()).getLimit())) {
					p.getInventory().addItem(it);
					m.getEco().getEconomy().withdrawPlayer((OfflinePlayer) p, tmp);
					SimpleShop.sendMessage(p, Message.BALANCE, SimpleShop.getBalance(p));
					
				}else
					SimpleShop.sendMessage(p, Message.LIMIT, shopLoader.getMap().get(e.getRawSlot()).getLimit());
			}
			else {
				SimpleShop.sendMessage(p, Message.CANT_BUY);
			}
		}
	}
	
	public Inventory getInv() {
		return this.inv;
	}

	public ShopLoader getBuy() {
		return this.shopLoader;
	}
	
	public HashMap<String, Integer> getLimit() {
		return this.listLimit;
	}
	
	public void reload() {
		listLimit.clear();
		shopLoader.reload();
	}
}
