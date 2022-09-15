package Limbo.Commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import Limbo.SimpleShop;
import Limbo.Message.Message;

public class TransferCommand implements CommandExecutor, TabExecutor{
List<String> list;
	public TransferCommand() {
		list = Arrays.asList("1000", "10000", "100000");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
		if(arg.length > 1) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(player.hasPermission("shop.transfer")) {
					try {
						SimpleShop.giveMoney(player, arg[0], Double.parseDouble(arg[1]));
					}catch (Exception e) {
						SimpleShop.sendMessage(sender, Message.TRADE_ERROR);
					}
				}
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if(!command.getName().equalsIgnoreCase("transfer"))
			return null;
		if(args.length == 2)
			return list;
		return null;
	}

}
