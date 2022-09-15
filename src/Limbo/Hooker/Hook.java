package Limbo.Hooker;

import org.bukkit.Bukkit;

import Limbo.SimpleShop;

public class Hook {
	private VaultHooker vault;
	private CitizensHooker citizens;

	public Hook() {
		SimpleShop shop = SimpleShop.getIntance();
		if(Bukkit.getPluginManager().getPlugin("ItemsAdder") == null) {
			shop.itemsAdder = false;
		}
		vault = new VaultHooker();
		citizens = new CitizensHooker();
	}
	
	public VaultHooker getVault() {
		return vault;
	}
	
	public CitizensHooker getCitizens() {
		return citizens;
	}
}
