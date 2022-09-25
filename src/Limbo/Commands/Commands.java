package Limbo.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import Limbo.SimpleShop;
import Limbo.Message.Message;
import Limbo.Shop.Sell;

public class Commands implements CommandExecutor,TabCompleter{
	SimpleShop shop;
	List<String> queue;
	
	public Commands() {
		shop = SimpleShop.getIntance();
		queue = new ArrayList<>();
		queue.add("trade");
		queue.add("create");
		queue.add("sell");
		queue.add("reload");
		queue.add("help");
		queue.sort((a, b) -> a.compareTo(b));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
		boolean isPlayer = false;
		if(sender instanceof Player) {
			Player player = (Player) sender;
			isPlayer = true;
			if(arg.length == 0 || arg[0] == null) {
				if(player.hasPermission("shop.shop")) {
					player.openInventory(shop.getShop().getInv());
					return true;
				}
				else {
					SimpleShop.sendMessage(player, Message.HASNT_PERM);
					return false;
				}
			}
			
			else if(arg[0].equalsIgnoreCase("sell")) {
				
					if(player.hasPermission("shop.sell")) {
						player.openInventory(Sell.getInv(player));
						return true;
					}
					else {
						SimpleShop.sendMessage(player, Message.HASNT_PERM);
						return false;
					}
			}
			else if(arg[0].equalsIgnoreCase("trade")) {
					if(player.hasPermission("shop.trade")) {
						player.openInventory(shop.getTrade().getInv());
						return true;
					}
					else {
						SimpleShop.sendMessage(player, Message.HASNT_PERM);
						return false;
					}
				}
			else if(arg[0].equalsIgnoreCase("create")) {
					if(player.hasPermission("shop.admin")) {
						shop.getCitizens().create(player);
						return true;
					}
					else {
						SimpleShop.sendMessage(player, Message.HASNT_PERM);
						return false;
					}
			}
		}
		if(arg.length > 0)
			if(arg[0].equalsIgnoreCase("reload")) {
				if(!sender.hasPermission("shop.admin")) {
					SimpleShop.sendMessage(sender, Message.HASNT_PERM);
					return false;
				}
				shop.reload();
				SimpleShop.sendMessage(sender, Message.RELOAD);
				return true;
			}
		if(!isPlayer) {
			SimpleShop.sendMessage(sender, Message.CONSOLE);
			return false;
		}
		help(sender);
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if(!command.getName().equalsIgnoreCase("simpleshop"))
			return null;
		if(args.length == 1)
			return queue;
		return null;
	}

	void help(CommandSender player) {
		player.sendMessage(SimpleShop.nonFormat("&3&l>>> Simple Shop <<<"));
		player.sendMessage(SimpleShop.nonFormat("&c&l> Aliases"));
		player.sendMessage(SimpleShop.nonFormat("&e[simpleshop, ss, shop]"));
		player.sendMessage(SimpleShop.nonFormat("&e[transfer, tf, bank, givemoney]"));
		player.sendMessage(SimpleShop.nonFormat("&b&l> Commands"));
		player.sendMessage(SimpleShop.nonFormat("&a/shop: &rOpen shop GUI"));
		player.sendMessage(SimpleShop.nonFormat("&a/shop create: &rCreate NPC (need Citizens)"));
		player.sendMessage(SimpleShop.nonFormat("&a/shop sell: &rOpen shop sell GUI"));
		player.sendMessage(SimpleShop.nonFormat("&a/shop trade: &rOpen trade GUI"));
		player.sendMessage(SimpleShop.nonFormat("&a/shop reload: &rReload config"));
		player.sendMessage(SimpleShop.nonFormat("&a/sreload: &rReload config"));
		player.sendMessage(SimpleShop.nonFormat("&a/transfer <player> <amounts>: &rGive money to other player"));
	}
	
}
