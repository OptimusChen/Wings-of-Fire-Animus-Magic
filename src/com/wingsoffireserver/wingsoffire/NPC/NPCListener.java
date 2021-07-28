package com.wingsoffireserver.wingsoffire.NPC;

import com.wingsoffireserver.wingsoffire.Main;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class NPCListener implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (EntityPlayer npc : NPC.getNpcs()) {
                    NPC.addNpcPacket(npc);
                }
            }
        }.runTaskLater(Main.getInstance(), 1);
    }

    @EventHandler
    public void onNpcJoin(PlayerJoinEvent e){
        PacketReader reader = new PacketReader();
        reader.inject(e.getPlayer());
        if (NPC.getNpcs() == null){
            return;
        }else if (NPC.getNpcs().isEmpty()){
            return;
        }else{
            new BukkitRunnable() {
                @Override
                public void run() {
                    NPC.addJoinPacket(e.getPlayer());
                }
            }.runTaskLater(Main.getInstance(), 1);
        }
    }

    @EventHandler
    public void onClick(NpcClickEvent e){
        if (e.getNpc().getName().equalsIgnoreCase(ChatColor.YELLOW + "" + ChatColor.BOLD + "Click")) {
            Main.getInstance().openStudyGui(e.getPlayer());
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerArmorStandManipulateEvent e){
        if (e.getRightClicked().getName().equals("Starflight")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDmg(EntityDamageByEntityEvent e){
        if (e.getEntity().getName().equals(ChatColor.YELLOW + "" + ChatColor.BOLD + "Click")){
            e.setCancelled(true);
        }
    }

}
