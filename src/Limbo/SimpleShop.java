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
		metrics.addCustomChart(new Metrics.SimplePie("disable_village", () -> getConfig().getString("disable_village_trade")));
	}
	
	public static SimpleShop getIntance() {
		return intance;
	}
	
	public static String format(String msg) {
		return ChatColor.translateAlternateColorCodes('&', "&3&l[SimpleShop]&r " + msg);
	}
	
	public static String nonFormat(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
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
	
	public static void sendMessage(CommandSender sender, Message message, double value) {
		String tmp = "";
		tmp = message.replace("%money%", String.valueOf(value));
		sender.sendMessage(format(tmp));
	}
	
	public static void sendMessage(CommandSender sender, Message message, int value) {
		String tmp = "";
		tmp = message.replace("%limit%", String.valueOf(value));
		sender.sendMessage(format(tmp));
	}
	
	public static void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(format(message));
	}
	
	public static void sendMessage(CommandSender sender, Message message) {
		sender.sendMessage(format(message.getMessage));
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
		
		Player toPlayer = Bukkit.getPlayer(name);
		if(toPlayer == null)
			sendMessage(player, "&cPlayer " + name + " not found!");
		if(intance.getEco().getEconomy().getBalance(player) >= money) {
			if(toPlayer.isOnline()) {
				intance.getEco().getEconomy().withdrawPlayer(player, money);
				intance.getEco().getEconomy().depositPlayer(toPlayer, money);
				sendMessage(player, Message.BALANCE, money);
				sendMessage(toPlayer, Message.TAKE_MONEY, money);
				return true;
			}
		}
		return false;
	}
	
	public void reload() {
		reloadConfig();
		dataConfig.reloadConfig();
		message.reloadConfig();
		shopConfig.reloadConfig();
		shop.reload();
		sell.reload();
		hook.getCitizens().reload();
	}
	
}
