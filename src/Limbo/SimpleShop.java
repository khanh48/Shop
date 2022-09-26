package Limbo;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import Limbo.Commands.RegisterCommands;
import Limbo.Config.ConfigManager;
import Limbo.Events.RegisterEvents;
import Limbo.Hooker.CitizensHooker;
import Limbo.Hooker.Hook;
import Limbo.Hooker.VaultHooker;
import Limbo.Message.Message;
import Limbo.Metrics.Metrics;
import Limbo.Shop.Sell;
import Limbo.Shop.Shop;
import Limbo.Shop.Trade;

public class SimpleShop extends JavaPlugin{
	private static SimpleShop intance;
	private Shop shop;
	private Trade trade;
	private Sell sell;
	private RegisterCommands commands;
	private RegisterEvents events;
	private Hook hook;
	public ConfigManager shopConfig, dataConfig, message;
	public boolean itemsAdder, citizens;
	
	public SimpleShop() {
		itemsAdder = true;
		citizens = true;
		intance = this;
	}
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		shopConfig = new ConfigManager("shop");
		dataConfig = new ConfigManager("data");
		message = new ConfigManager("message");
		hook = new Hook();
		shop = new Shop();
		trade = new Trade();
		sell = new Sell();
		events = new RegisterEvents();
		commands = new RegisterCommands();
		Metrics metrics = new Metrics(this, 16429);
		metrics.addCustomChart(new Metrics.SimplePie("disable_village", () -> getConfig().getString("disable-village-trade")));
		metrics.addCustomChart(new Metrics.SimplePie("costs", () -> getConfig().getString("costs")));
		metrics.addCustomChart(new Metrics.SimplePie("auto_remove", () -> getConfig().getString("auto-remove-items-in-trade.enable")));
	}
	
	public static SimpleShop getIntance() {
		return intance;
	}
	
	public static String format(String msg) {
		return ChatColor.translateAlternateColorCodes('&', "&3&l[SimpleShop]&r " + msg);
	}
	
	public static String format(String... msg) {
		String tmp = "";
		for (String string : msg)
			tmp += string;
		return ChatColor.translateAlternateColorCodes('&', tmp);
	}
	
	public static List<String> format(List<String> msg) {
		List<String> tmp = new ArrayList<>();
		for (String string : msg)
			tmp.add(nonFormat(string));
		return tmp;
	}
	
	public static String nonFormat(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public static void sendMessage(CommandSender sender, String message, double value) {
		String tmp = "";
		tmp = Message.replace(message, "%money%", String.valueOf(value));
		sender.sendMessage(format(tmp));
	}
	
	public static void sendMessage(CommandSender sender, String message, double money, String name) {
		String tmp = "";
		tmp = Message.replace(Message.replace(message, "%money%", String.valueOf(money)), "%player%", name);
		sender.sendMessage(format(tmp));
	}

	public static void sendMessage(CommandSender sender, String message, double money, String item, String player) {
		String tmp = "";
		tmp = Message.replace(Message.replace(Message.replace(message, "%money%", String.valueOf(money)), "%player%", player), "%item%", item);
		sender.sendMessage(format(tmp));
	}
	
	public static void sendMessage(CommandSender sender, String message, int value) {
		String tmp = "";
		tmp = Message.replace(message, "%limit%", String.valueOf(value));
		sender.sendMessage(format(tmp));
	}
	
	public static void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(format(message));
	}
	
	
	public VaultHooker getEco() {
		return this.hook.getVault();
	}
	
	public CitizensHooker getCitizens() {
		return this.hook.getCitizens();
	}
	
	public Shop getShop() {
		return this.shop;
	}
	
	public Trade getTrade() {
		return trade;
	}
	
	public Sell getSell() {
		return sell;
	}
	
	public RegisterCommands getRegisterCommands() {
		return commands;
	}
	
	public RegisterEvents getRegisterEvents() {
		return events;
	}
	
	public static boolean giveMoney(Player player, String name, double money) {
		
		Player toPlayer = Bukkit.getPlayerExact(name);
		if(toPlayer == null) {
			sendMessage(player, "&cPlayer &l" + name + "&r&c not found!");
			return false;
		}
		if(getBalance(player) >= money) {
			if(toPlayer.isOnline()) {
				double tmp = 0;
				if(intance.getConfig().getDouble("costs") > 0) 
					tmp = money * intance.getConfig().getDouble("costs");
				intance.getEco().getEconomy().withdrawPlayer(player, money);
				intance.getEco().getEconomy().depositPlayer(toPlayer, money - tmp);
				sendMessage(player, Message.BALANCE, getBalance(player));
				sendMessage(toPlayer, Message.TAKE_FROM, money - tmp, player.getName());
				return true;
			}else {
				sendMessage(player, "&cPlayer &l" + name + "&r&c is offline!");
				return false;
			}
		}else
			sendMessage(player, Message.CANT_BUY);
		return false;
	}
	
	public static double getBalance(Player player) {
		return intance.getEco().getEconomy().getBalance(player);
	}
	
	public void reload() {
		reloadConfig();
		saveDefaultConfig();
		dataConfig.reloadConfig();
		message.reloadConfig();
		shopConfig.reloadConfig();
		shop.reload();
		sell.reload();
		hook.getCitizens().reload();
		Message.reload();
	}
	
	public boolean autoRemove() {
		return getConfig().getBoolean("auto-remove-items-in-trade.enable");
	}
}
