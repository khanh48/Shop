package Limbo.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import Limbo.SimpleShop;
import Limbo.Message.Message;

public class ReloadCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		SimpleShop.getIntance().reload();
		SimpleShop.sendMessage(arg0, Message.RELOAD);
		return false;
	}

}
