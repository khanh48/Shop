package Limbo.Commands;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
	TreeSet<String> queue;
	
	public Commands() {
		shop = SimpleShop.getIntance();
		queue = new TreeSet<>((a, b) -> a.compareTo(b));
		queue.add("trade");
		queue.add("transfer");
		queue.add("create");
		queue.add("sell");
		queue.add("reload");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
		boolean isPlayer = false;
		Player player = (Player) sender;
		if(sender instanceof Player) isPlayer = true;
		if(arg.length == 0 || arg[0] == null) {
			if(isPlayer && player.hasPermission("shop.shop")) 
				player.openInventory(shop.getShop().getInv());
			else
				SimpleShop.sendMessage(sender, Message.HASNT_PERM);
			return false;
		}
		
		else if(arg[0].equalsIgnoreCase("sell")) {
			
			if(isPlayer)
				if(player.hasPermission("shop.sell"))
					player.openInventory(Sell.getInv(player));
				else
					SimpleShop.sendMessage(player, Message.HASNT_PERM);
			else
				SimpleShop.sendMessage(sender, Message.CONSOLE);
			return true;
		}
		else if(arg[0].equalsIgnoreCase("trade")) {
			if(isPlayer)
				if(player.hasPermission("shop.trade"))
					player.openInventory(shop.getTrade().getInv());
				else
					SimpleShop.sendMessage(player, Message.HASNT_PERM);
			else
				SimpleShop.sendMessage(sender, Message.CONSOLE);
			return false;
			}
		else if(arg[0].equalsIgnoreCase("create")) {
			if(isPlayer)
				if(player.hasPermission("shop.admin"))
					shop.getCitizens().create(player);
				else
					SimpleShop.sendMessage(player, Message.HASNT_PERM);
			else
				SimpleShop.sendMessage(sender, Message.CONSOLE);
			return false;
		}
		if(isPlayer && !player.hasPermission("shop.admin")) {
			SimpleShop.sendMessage(player, Message.HASNT_PERM);
			return false;
		}
		shop.reload();
		SimpleShop.sendMessage(sender, Message.RELOAD);
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if(!command.getName().equalsIgnoreCase("shop"))
			return null;
		if(args.length == 1)
			return queue.stream().collect(Collectors.toList());
		return null;
	}


	
	

}
