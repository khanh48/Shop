package Limbo.Events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import Limbo.SimpleShop;
import Limbo.Message.Message;

public class DisableTrade implements Listener{

	@EventHandler
	public void onTrade(PlayerInteractEntityEvent e) {
		if(!e.getRightClicked().getType().equals(EntityType.VILLAGER)) return;
		Villager villager = (Villager) e.getRightClicked();
		if(villager.getProfession().equals(Villager.Profession.NONE)) return;
		if(!SimpleShop.getIntance().getConfig().getBoolean("disable-village-trade")) return;
		if(e.getPlayer().hasPermission("shop.bypass")) return;
		if(villager.hasAI() && villager.hasGravity()) {
			e.setCancelled(true);
			SimpleShop.sendMessage(e.getPlayer(), Message.TRADE_VILLAGE_MSG);
		}
	}
}
