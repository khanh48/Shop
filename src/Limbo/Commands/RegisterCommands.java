package Limbo.Commands;

import Limbo.SimpleShop;

public class RegisterCommands {

	public RegisterCommands() {
		SimpleShop main = SimpleShop.getIntance();

		Commands commands = new Commands();
		TransferCommand transferCommand = new TransferCommand();
		main.getCommand("simpleshop").setExecutor(commands);
		main.getCommand("simpleshop").setTabCompleter(commands);
		main.getCommand("sreload").setExecutor(new ReloadCommand());
		main.getCommand("transfer").setExecutor(transferCommand);
		main.getCommand("transfer").setTabCompleter(transferCommand);
	}
}
