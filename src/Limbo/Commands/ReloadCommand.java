package Limbo.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Limbo.SimpleShop;
import Limbo.Message.Message;

public class ReloadCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
		if(sender instanceof Player)
			if(!sender.hasPermission("shop.admin")) {
				SimpleShop.sendMessage(sender, Message.HASNT_PERM);
				return false;
			}
		SimpleShop.getIntance().reload();
		SimpleShop.sendMessage(sender, Message.RELOAD);
		return false;
	}

}
