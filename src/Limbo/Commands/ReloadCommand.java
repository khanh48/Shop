package Limbo.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import Limbo.SimpleShop;
import Limbo.Message.Message;

public class ReloadCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
		SimpleShop.getIntance().reload();
		SimpleShop.sendMessage(sender, Message.RELOAD);
		return false;
	}

}
