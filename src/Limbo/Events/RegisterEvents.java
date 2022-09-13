package Limbo.Events;

import org.bukkit.plugin.PluginManager;

import Limbo.SimpleShop;

public class RegisterEvents {
	private final SimpleShop main;
	public RegisterEvents() {
		main = SimpleShop.getIntance();
		PluginManager manager = main.getServer().getPluginManager();
		
		manager.registerEvents(new DisableTrade(), main);
		manager.registerEvents(main.getSell(), main);
		manager.registerEvents(main.getShop(), main);
		manager.registerEvents(main.getTrade(), main);
	}
}
