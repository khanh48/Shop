package Limbo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import Limbo.Commands.Commands;
import Limbo.Commands.RegisterCommands;
import Limbo.Commands.ReloadCommand;
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
	private VaultHooker vault;
	private CitizensHooker citizens;
	private RegisterCommands commands;
	private RegisterEvents events;
	public ConfigManager shopConfig, dataConfig, message;
	
	public SimpleShop() {
	}
	
	@Override
	public void onEnable() {
		intance = this;
		saveDefaultConfig();
		shopConfig = new ConfigManager("shop");
		dataConfig = new ConfigManager("data");
		message = new ConfigManager("message");
		vault = new VaultHooker();
		citizens = new CitizensHooker();
		shop = new Shop();
		sell = new Sell();
		trade = new Trade();
		events = new RegisterEvents();
		Commands cmd = new Commands();
		getCommand("shop").setExecutor(cmd);
		getCommand("sreload").setExecutor(new ReloadCommand());
		Metrics metrics = new Metrics(this, 16429);
		metrics.addCustomChart(new Metrics.SimplePie("use_ia", new Callable<String>() {
			@Override
			public String call() throws Exception {
				return getConfig().getString("itemsadder.enable");
			}
		}));
		metrics.addCustomChart(new Metrics.SimplePie("disable_village", new Callable<String>() {
			@Override
			public String call() throws Exception {
				return getConfig().getString("disable_village_trade");
			}
		}));
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
		if(message.getMsg.contains("%money%")) {
			tmp = message.getMsg.replace("%money%", String.valueOf(value));
		}
		if(message.getMsg.contains("%limit%")) {
			tmp = message.getMsg.replace("%limit%", String.valueOf((int)value));
		}
		sender.sendMessage(format(tmp));
	}
	
	public static void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(format(message));
	}
	
	public static void sendMessage(CommandSender sender, Message message) {
		sender.sendMessage(format(message.getMsg));
	}
	
	public VaultHooker getEco() {
		return vault;
	}
	
	public CitizensHooker getCitizens() {
		return this.citizens;
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
	
	public void reload() {
		reloadConfig();
		dataConfig.reloadConfig();
		message.reloadConfig();
		shopConfig.reloadConfig();
	}
	
	public RegisterCommands getRegisterCommands() {
		return commands;
	}
	
	public RegisterEvents getRegisterEvents() {
		return events;
	}
	
	public static boolean giveMoney(Player player, Player toPlayer, double money) {
		if(intance.getEco().getEconomy().getBalance(player) >= money) {
			if(toPlayer.isOnline()) {
				intance.getEco().getEconomy().withdrawPlayer(player, money);
				intance.getEco().getEconomy().depositPlayer(toPlayer, money);
				sendMessage(player, Message.BALANCE);
				sendMessage(toPlayer, Message.TAKE_MONEY);
				return true;
			}
			sendMessage(player, "Can't find player " + toPlayer.getName());
		}
		return false;
	}
	
	public boolean itemsAdderIsEnable() {
		return getConfig().getBoolean("itemsadder.enable");
	}
}
