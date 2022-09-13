package Limbo.Hooker;

import org.bukkit.Bukkit;

import Limbo.SimpleShop;

public class Hook {

	public Hook() {
		SimpleShop shop = SimpleShop.getIntance();
		if(Bukkit.getPluginManager().getPlugin("ItemsAdder") == null) {
			shop.getConfig().set("itemsadder.enable", false);
			shop.saveConfig();
		}
	}
}
