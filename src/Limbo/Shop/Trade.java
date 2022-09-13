package Limbo.Shop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Limbo.SimpleShop;
import Limbo.Message.Message;


public class Trade implements Listener{
	SimpleShop m;
	Inventory inv;
	ItemStack stack;
	Trader tmp;
	HashMap<Integer, Trader> listTrade = new HashMap<>();
	boolean input = false, cancel = false;
	
	public Trade() {
		this.m = SimpleShop.getIntance();
		inv = m.getServer().createInventory(null, 54, "TRADE");
	}

	@EventHandler
	public void onClickTrade(InventoryClickEvent e) {
		if(!e.getInventory().equals(inv)) return;
		if(e.getClickedInventory() == null) return;
		if(!e.getClickedInventory().equals(e.getView().getTopInventory())) return;
		Player p = (Player) e.getWhoClicked();
			
		if(e.getAction().equals(InventoryAction.PICKUP_ALL) && !cancel) {
			Trader temp = listTrade.get(e.getRawSlot());
			if(temp == null ) { 
				e.setCancelled(true);
				return;
			}
			if(m.getEco().getEconomy().getBalance((OfflinePlayer) p) >= temp.getPrice()) {
				e.getCurrentItem().setItemMeta(removeInfo(temp.getItem().getItemMeta()));
				OfflinePlayer nhan = m.getServer().getOfflinePlayer(temp.getUUID());
				m.getEco().getEconomy().depositPlayer(nhan, temp.getPrice());
				if(nhan.isOnline()) {
					SimpleShop.sendMessage(nhan.getPlayer(), Message.TAKE_MONEY, temp.getPrice());
				}
				m.getEco().getEconomy().withdrawPlayer((OfflinePlayer) p, temp.getPrice());
				listTrade.remove(e.getRawSlot());
				save();
				SimpleShop.sendMessage(p, Message.BALANCE, m.getEco().getEconomy().getBalance((OfflinePlayer) p));
			}
			else {
				if(!p.getUniqueId().equals(temp.getUUID())) {
					SimpleShop.sendMessage(p, Message.CANT_BUY);
					e.setCancelled(true);
				}else 
				{
					e.getCurrentItem().setItemMeta(removeInfo(temp.getItem().getItemMeta()));
					listTrade.remove(e.getRawSlot());
					save();
				}
			}
		}
		else if(e.getAction().equals(InventoryAction.PLACE_ALL)) {
			cancel = false;
			tmp = new Trader(p.getName(), p.getUniqueId(), 0 , e.getCursor().clone(), e.getRawSlot());
			input = true;
			e.setCancelled(true);
			SimpleShop.sendMessage(p, Message.ENTER_MONEY);
			p.closeInventory();
		}else
			e.setCancelled(true);
	}

	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		if(!e.getInventory().equals(inv)) return;
		e.setCancelled(true);
	}
	
	private ItemMeta removeInfo(ItemMeta itemMeta) {
		List<String> rmInfo = itemMeta.getLore();
		rmInfo.remove(rmInfo.size() - 1);
		rmInfo.remove(rmInfo.size() - 1);
		itemMeta.setLore(rmInfo);
		return itemMeta;
	}
	
	public Inventory getInv() {
		return this.inv;
	}
	
	void removeFromStack(InventoryView it, ItemStack is, int num) {
		int temp = 0;
		HashMap<Integer, ? extends ItemStack> sameItem = it.getBottomInventory().all(is.getType());
		for (ItemStack itemStack : sameItem.values()) {
			if(itemStack.isSimilar(is)) {
				temp += itemStack.getAmount();
				it.getBottomInventory().remove(itemStack);
			}
		}
		is.setAmount(temp - num);
		it.getBottomInventory().addItem(is);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(input) {
			if(!e.getPlayer().getUniqueId().equals(tmp.getUUID())) return;
			input = false;
			e.setCancelled(true);
			
			Bukkit.getScheduler().runTask(m, () -> {
				try {
					tmp.setPrice(Double.parseDouble(e.getMessage()));
				}catch (NumberFormatException ex) {
					cancel = true;
					SimpleShop.sendMessage(e.getPlayer(), Message.TRADE_ERROR);
					e.getPlayer().openInventory(inv);
					return;
				}
				e.getPlayer().openInventory(inv);
				removeFromStack(e.getPlayer().getOpenInventory(), tmp.getItem().clone(), tmp.getItem().getAmount());
				ItemMeta im = tmp.getItem().getItemMeta();
				List<String> info = Arrays.asList("Price $" + String.valueOf(tmp.getPrice()),"Sell by " + tmp.getName());
				List<String> lore = im.getLore() == null? new ArrayList<String>(): im.getLore();
				lore.addAll(info);
				im.setLore(lore);
				tmp.getItem().setItemMeta(im);
				inv.setItem(tmp.getSlot(), tmp.getItem());
				listTrade.put(tmp.getSlot(), tmp);
				save();
			});
		}
	}
	
	public void load() {
		int slot;
		double price;
		String name;
		UUID uuid;
		ItemStack is;
		ConfigurationSection cfg = m.dataConfig.getConfig().getConfigurationSection("trade");
		if(cfg == null) return;
		for(String s :cfg.getKeys(false)) {
			name = m.dataConfig.getConfig().getString("trade." + s + ".name");
			slot =  m.dataConfig.getConfig().getInt("trade." + s + ".slot");
			uuid = UUID.fromString(m.dataConfig.getConfig().getString("trade." + s + ".uuid"));
			price = m.dataConfig.getConfig().getDouble("trade." + s + ".price");
			is = (ItemStack) m.dataConfig.getConfig().get("trade." + s + ".item");
			listTrade.put(slot, new Trader(name, uuid, price, is, slot));
		}
		
		for (Trader sl : listTrade.values()) {
			inv.setItem(sl.getSlot(), sl.getItem());
		}
		m.dataConfig.saveConfig(); 
	}
	
	public void save() {
		m.dataConfig.getConfig().set("trade", null);
		m.dataConfig.saveConfig();
		for (Trader sl : listTrade.values()) {
			m.dataConfig.getConfig().set("trade." + String.valueOf(sl.getSlot()) + ".name", sl.getName());
			m.dataConfig.getConfig().set("trade." + String.valueOf(sl.getSlot()) + ".slot", sl.getSlot());
			m.dataConfig.getConfig().set("trade." + String.valueOf(sl.getSlot()) + ".uuid", sl.getUUID().toString());
			m.dataConfig.getConfig().set("trade." + String.valueOf(sl.getSlot()) + ".price", sl.getPrice());
			m.dataConfig.getConfig().set("trade." + String.valueOf(sl.getSlot()) + ".item", sl.getItem());
		}
		m.dataConfig.saveConfig();
	}
}
