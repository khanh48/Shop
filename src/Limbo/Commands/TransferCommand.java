package Limbo.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Limbo.SimpleShop;

public class TransferCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
		if(arg.length > 1) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(player.hasPermission("shop.transfer")) {
					SimpleShop.giveMoney(player, Bukkit.getPlayer(arg[0]), Double.parseDouble(arg[1]));
				}
			}
		}
		return false;
	}

}
