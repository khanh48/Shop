package Limbo.Config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import Limbo.SimpleShop;

public class ConfigManager{
	private FileConfiguration customConfig;
	private File customConfigFile;
	private String name;
	private final SimpleShop main;
	
	public ConfigManager(String name) {
		main = SimpleShop.getIntance();
		this.name = name;
		createConfig();
	}
	
	public FileConfiguration getConfig() {
		if(customConfig == null)
			reloadConfig();
		return this.customConfig;
	}
	
	public void reloadConfig() {
		this.customConfig = (FileConfiguration)YamlConfiguration.loadConfiguration(this.customConfigFile);
		InputStream defConfigStream = main.getResource(name + ".yml");
		if (defConfigStream == null)
			return;
		this.customConfig.setDefaults((Configuration)YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
	}
	
	public void createConfig() {
		customConfigFile = new File(main.getDataFolder(), name + ".yml");
		if(!customConfigFile.exists()) {
			customConfigFile.getParentFile().mkdirs();
			main.saveResource(name + ".yml", false);
		}
		
		customConfig = new YamlConfiguration();
		try {
			customConfig.load(customConfigFile);
		}catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public void saveConfig() {
	    try {
	      this.getConfig().save(this.customConfigFile);
	    } catch (IOException ex) {
	    } 
	}
}
