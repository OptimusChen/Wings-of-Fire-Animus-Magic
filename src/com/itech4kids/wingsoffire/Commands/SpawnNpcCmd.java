package com.itech4kids.wingsoffire.Commands;

import com.itech4kids.wingsoffire.Config;
import com.itech4kids.wingsoffire.Main;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.EntityVillager;
import net.minecraft.server.v1_16_R3.EntityVillagerTrader;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftWanderingTrader;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.inventory.ItemStack;

public class SpawnNpcCmd implements CommandExecutor {
    Main main;

    public SpawnNpcCmd(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            WanderingTrader villager = player.getWorld().spawn(player.getLocation(), WanderingTrader.class);
            villager.setCustomNameVisible(true);
            //villager.setRemoveWhenFarAway(false);
            //villager.setAware(false);
            villager.setInvulnerable(true);
            villager.setRemoveWhenFarAway(false);
            villager.setAI(false);
            villager.setCustomName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Click");
            ArmorStand armorStand = player.getWorld().spawn(new Location(villager.getLocation().getWorld(), villager.getLocation().getX(), villager.getLocation().getY() + 0.4, villager.getLocation().getZ()), ArmorStand.class);
            armorStand.setCustomName("Jade Mountain Librarian");
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setInvulnerable(true);
            armorStand.setCustomNameVisible(true);
        }
        return false;
    }
}
