package Limbo.Shop;

import java.util.HashMap;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

public class Enchantments {
	static HashMap<String, Enchantment> enc;
	
	public Enchantments() {
		enc = new HashMap<>();
		loadKey();
	}
	
	private void loadKey() {
		for (Enchantment e : Enchantment.values()) {
			enc.put(e.getKey().toString(), e);
		}
	}
	
	private String getKey(String key) {
		return NamespacedKey.MINECRAFT + ":" + key;
	}
	
	public Enchantment getEnchantment(String key) {
		return enc.get(getKey(key));
	}
}
