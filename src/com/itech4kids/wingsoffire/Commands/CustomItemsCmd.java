package com.itech4kids.wingsoffire.Commands;

import com.itech4kids.wingsoffire.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftItem;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class CustomItemsCmd implements CommandExecutor {
    Main main;

    public CustomItemsCmd(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            ItemStack talisman = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) talisman.getItemMeta();
            List<String> talismanLore = new ArrayList<>();
            talismanLore.add(ChatColor.GRAY + "An animus dragon can");
            talismanLore.add(ChatColor.GRAY + "enchant generic");
            talismanLore.add(ChatColor.GRAY + "talismans to give");
            talismanLore.add(ChatColor.GRAY + "special buffs (or debuffs)!");
            skullMeta.setLore(talismanLore);
            skullMeta.setDisplayName(ChatColor.DARK_GRAY + "Generic Talisman");
            skullMeta.setOwner("stone");
            talisman.setItemMeta(skullMeta);
            main.setMaxStackSize(CraftItemStack.asNMSCopy(talisman).getItem(), 1);
            player.getInventory().addItem(talisman);
        }
        return false;
    }
}
