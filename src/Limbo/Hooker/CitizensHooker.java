package Limbo.Hooker;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import Limbo.SimpleShop;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.CitizensEnableEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;

public class CitizensHooker implements Listener{
	NPC npc;
	List<Integer> listNpc;
	SimpleShop main;
	public CitizensHooker(){
		main = SimpleShop.getIntance();
		if(Bukkit.getPluginManager().getPlugin("Citizens") == null) {
			SimpleShop.sendMessage(main.getServer().getConsoleSender(), "Can't hook into Citizens");
			return;
		}
		loadNPC();
	}
	
	@EventHandler
	public void enableCitizens(CitizensEnableEvent e) {
		npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, SimpleShop.format("&6&lSimpleShop"));
	}
	
	@EventHandler
	public void onClickNPC(NPCRightClickEvent e) {
		if(listNpc == null) return;
		for (Integer integer : listNpc) {
			if(e.getNPC().getId() == integer) {
				e.getClicker().openInventory(main.getShop().getInv());
			}
		}
	}
	
	public void loadNPC() {
		if(listNpc != null)
			listNpc.clear();
		if(main.dataConfig.getConfig().getIntegerList("npc") == null) return;
		listNpc = main.dataConfig.getConfig().getIntegerList("npc");
	}
	
	public void create(Player p) {
		npc.spawn(p.getLocation());
		listNpc.add(npc.getId());
		main.dataConfig.getConfig().set("npc", listNpc);
		main.dataConfig.saveConfig();;
	}
	
	public NPC getNPC() {
		return this.npc;
	}
}
