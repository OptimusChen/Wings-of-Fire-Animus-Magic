package com.wingsoffireserver.wingsoffire.Commands;

import com.wingsoffireserver.wingsoffire.Main;
import com.wingsoffireserver.wingsoffire.NPC.NPC;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class SpawnNpcCmd implements CommandExecutor {
    Main main;

    public SpawnNpcCmd(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.isOp()) {
                NPC.spawnNPC(player.getLocation());
                ArmorStand armorStand = player.getWorld().spawn(new Location(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY() + 0.2, player.getLocation().getZ()), ArmorStand.class);
                armorStand.setCustomName("Starflight");
                armorStand.setVisible(false);
                armorStand.setGravity(false);
                armorStand.setInvulnerable(true);
                armorStand.setCustomNameVisible(true);
            }
        }
        return false;
    }
}
