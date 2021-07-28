package com.wingsoffireserver.wingsoffire.Commands;

import com.wingsoffireserver.wingsoffire.ActivePlayer;
import com.wingsoffireserver.wingsoffire.Config;
import com.wingsoffireserver.wingsoffire.Main;
import com.wingsoffireserver.wingsoffire.SkillInventory;
import net.minecraft.server.v1_16_R3.ItemArmor;
import net.minecraft.server.v1_16_R3.ItemAxe;
import net.minecraft.server.v1_16_R3.ItemSword;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

public class AnimusCmd implements CommandExecutor {
    Main main;

    public AnimusCmd(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            ActivePlayer activePlayer = main.getActivePlayer(player.getName());
            if (Config.isAnimus(player)) {
                if (args.length == 0){
                    main.openAnimusGui(player);
                }else{
                    switch (args[0].toLowerCase()){
                        case "help":
                            player.sendMessage(ChatColor.GOLD + "-----Animus Help-----");
                            player.sendMessage(ChatColor.GOLD + "/animus help");
                            player.sendMessage(ChatColor.GOLD + "/animus enchant help");
                            player.sendMessage(ChatColor.GOLD + "/animus study");
                            break;
                        case "enchant":
                            try{

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            switch (args[1].toLowerCase()){
                                case "simple":
                                    ItemStack hand = player.getInventory().getItemInMainHand();
                                    List<String> lore = new ArrayList<>();
                                    if (hand.getType().equals(Material.AIR)){
                                        player.sendMessage(ChatColor.RED + "You need to be holding something!");
                                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 10, 1);
                                        player.closeInventory();
                                        break;
                                    }
                                    switch (args[2].toLowerCase()){
                                        case "running":
                                            SkullMeta handItemMeta = (SkullMeta) hand.getItemMeta();
                                            if (handItemMeta.getDisplayName().equals(ChatColor.DARK_GRAY + "Generic Talisman") || handItemMeta.getDisplayName().startsWith(ChatColor.AQUA + "Running Talisman ")){
                                                if ((Integer.parseInt(args[3]) <= 4) && (Integer.parseInt(args[3]) - 1  <= Config.getAnimusStudyLevel(player))){
                                                    if (Config.getMana(player) >= Integer.parseInt(args[3])*10) {
                                                        Config.setMana(player, Config.getMana(player) - Integer.parseInt(args[3])*10);
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                        player.sendMessage(ChatColor.GREEN + "You have successfully enchanted your talisman to a Running Talisman!");
                                                        handItemMeta.setOwner("LapisBlock");
                                                        lore.add(ChatColor.GRAY + "Putting this talisman in");
                                                        lore.add(ChatColor.GRAY + "an active talisman slot will");
                                                        lore.add(ChatColor.GRAY + "grant you +" + Integer.parseInt(args[3]) * 10 + "% speed!");
                                                        handItemMeta.setLore(lore);
                                                        handItemMeta.setDisplayName(ChatColor.AQUA + "Running Talisman " + Integer.parseInt(args[3]));
                                                        hand.setItemMeta(handItemMeta);
                                                    }else{
                                                        player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                    }
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "Value too large/Further study is needed!");
                                                }
                                            }else{
                                                player.sendMessage(ChatColor.RED + "You must hold a generic talisman to do this!");
                                                break;
                                            }
                                            break;
                                        case "shield":
                                            SkullMeta handItemMeta2 = (SkullMeta) hand.getItemMeta();
                                            if (handItemMeta2.getDisplayName().equals(ChatColor.DARK_GRAY + "Generic Talisman") || handItemMeta2.getDisplayName().startsWith(ChatColor.DARK_GRAY + "Shield Talisman " )){
                                                if (Integer.parseInt(args[3]) <= 5 && (Integer.parseInt(args[3]) - 1 <= Config.getAnimusStudyLevel(player))){
                                                    if (Config.getMana(player) >= Integer.parseInt(args[3])*10) {
                                                        Config.setMana(player, Config.getMana(player) - Integer.parseInt(args[3])*10);
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                        player.sendMessage(ChatColor.GREEN + "You have successfully enchanted your talisman to a Shield Talisman!");
                                                        lore.add(ChatColor.GRAY + "Putting this talisman in");
                                                        lore.add(ChatColor.GRAY + "an active talisman slot will");
                                                        lore.add(ChatColor.GRAY + "grant you +" + Integer.parseInt(args[3]) + " armor points!");
                                                        handItemMeta2.setLore(lore);
                                                        handItemMeta2.setDisplayName(ChatColor.DARK_GRAY + "Shield Talisman " + Integer.parseInt(args[3]));
                                                        hand.setItemMeta(handItemMeta2);
                                                        hand = main.IDtoSkull(hand, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE1Yzg2ODY0MTFkNDQ2YzkwYzE5MWM5M2Y4MGI5ZmZiMWNkMjQ3YWExMmEyMjZmODk3OTk4MWFkNDM4OGJlZSJ9fX0=");
                                                    }else{
                                                        player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                    }
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "Value too large/Further study is needed!");
                                                }
                                            }else{
                                                player.sendMessage(ChatColor.RED + "You must hold a generic talisman to do this!");
                                                break;
                                            }
                                            break;
                                        case "strength":
                                            SkullMeta handItemMeta3 = (SkullMeta) hand.getItemMeta();
                                            if (handItemMeta3.getDisplayName().equals(ChatColor.DARK_GRAY + "Generic Talisman") || handItemMeta3.getDisplayName().startsWith(ChatColor.DARK_RED + "Strength Talisman ")){
                                                if (Integer.parseInt(args[3]) <= 5 && (Integer.parseInt(args[3]) - 1 <= Config.getAnimusStudyLevel(player))){
                                                    if (Config.getMana(player) >= Integer.parseInt(args[3])*10) {
                                                        Config.setMana(player, Config.getMana(player) - Integer.parseInt(args[3])*10);
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                        player.sendMessage(ChatColor.GREEN + "You have successfully enchanted your talisman to a Strength Talisman!");
                                                        lore.add(ChatColor.GRAY + "Putting this talisman in");
                                                        lore.add(ChatColor.GRAY + "an active talisman slot will");
                                                        lore.add(ChatColor.GRAY + "grant you +" + Integer.parseInt(args[3]) * 5 + "% attack boost!");
                                                        handItemMeta3.setLore(lore);
                                                        handItemMeta3.setDisplayName(ChatColor.DARK_RED + "Strength Talisman " + Integer.parseInt(args[3]));
                                                        hand.setItemMeta(handItemMeta3);
                                                        hand = main.IDtoSkull(hand, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2IwMzNiMmMyZDNjZjU4OTEzM2FiNzMwZWQxYThiNDQzMTNkNjI5OTU0ODBjM2EwZGFjMzI4ZDUzN2UyN2Q3ZiJ9fX0=");
                                                    }else{
                                                        player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                    }
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "Value too large/Further study is needed!");
                                                }
                                            }else{
                                                player.sendMessage(ChatColor.RED + "You must hold a generic talisman to do this!");
                                                break;
                                            }
                                            break;
                                        case "gliding":
                                            SkullMeta handItemMeta4 = (SkullMeta) hand.getItemMeta();
                                            if (handItemMeta4.getDisplayName().equals(ChatColor.DARK_GRAY + "Generic Talisman")){
                                                if (Config.getMana(player) >= 5) {
                                                    Config.setMana(player, Config.getMana(player) - 5);
                                                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                    player.sendMessage(ChatColor.GREEN + "You have successfully enchanted your talisman to a Gliding Talisman!");
                                                    lore.add(ChatColor.GRAY + "Putting this talisman in");
                                                    lore.add(ChatColor.GRAY + "an active talisman slot will");
                                                    lore.add(ChatColor.GRAY + "grant you permanent slow-falling!");
                                                    handItemMeta4.setLore(lore);
                                                    handItemMeta4.setDisplayName(ChatColor.WHITE + "Gliding Talisman 1");
                                                    hand.setItemMeta(handItemMeta4);
                                                    hand = main.IDtoSkull(hand, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzZmMzNiMjk3YTk4MWI1MjdiZWMzOTMxNjg0MDFkOGEyZWNhZGViOWYxNjAzYmE1ZTI3NmY0MmQ2NDQ3NTExNiJ9fX0=");
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                }
                                            }else{
                                                player.sendMessage(ChatColor.RED + "You must hold a generic talisman to do this!");
                                                break;
                                            }
                                            break;
                                        case "stealth":
                                            SkullMeta handItemMeta5 = (SkullMeta) hand.getItemMeta();
                                            if (handItemMeta5.getDisplayName().equals(ChatColor.DARK_GRAY + "Generic Talisman")) {
                                                if (Config.getAnimusStudyLevel(player) >= 1) {
                                                    if (Config.getMana(player) >= 10) {
                                                        Config.setMana(player, Config.getMana(player) - 10);
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                        player.sendMessage(ChatColor.GREEN + "You have successfully enchanted your talisman to a Stealth Talisman!");
                                                        lore.add(ChatColor.GRAY + "Putting this talisman in");
                                                        lore.add(ChatColor.GRAY + "an active talisman slot will");
                                                        lore.add(ChatColor.GRAY + "grant you permanent invisibility!");
                                                        handItemMeta5.setLore(lore);
                                                        handItemMeta5.setDisplayName(ChatColor.DARK_GRAY + "Stealth Talisman 1");
                                                        hand.setItemMeta(handItemMeta5);
                                                        hand = main.IDtoSkull(hand, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2IxZTAxN2I1ODQxYjk4NTc3YTJiOGVkOWJmMDIzZDNiZjE0OWQ3ZWY2Y2RkY2VmY2FkZjdiNGIyN2MzMWIzMSJ9fX0=");
                                                    }else{
                                                        player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                    }
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "Continue the research..");
                                                }
                                            }else{
                                                player.sendMessage(ChatColor.RED + "You must hold a generic talisman to do this!");
                                                break;
                                            }
                                            break;
                                        case "glowing":
                                            SkullMeta handItemMeta6 = (SkullMeta) hand.getItemMeta();
                                            if (handItemMeta6.getDisplayName().equals(ChatColor.DARK_GRAY + "Generic Talisman")) {
                                                if (Config.getAnimusStudyLevel(player) >= 1) {
                                                    if (Config.getMana(player) >= 5) {
                                                        Config.setMana(player, Config.getMana(player) - 5);
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                        player.sendMessage(ChatColor.GREEN + "You have successfully enchanted your talisman to a Glowing Talisman!");
                                                        lore.add(ChatColor.GRAY + "Putting this talisman in");
                                                        lore.add(ChatColor.GRAY + "an active talisman slot will");
                                                        lore.add(ChatColor.GRAY + "grant you permanent glowing effect!");
                                                        handItemMeta6.setLore(lore);
                                                        handItemMeta6.setDisplayName(ChatColor.YELLOW + "Glowing Talisman 1");
                                                        hand.setItemMeta(handItemMeta6);
                                                        hand = main.IDtoSkull(hand, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Q5ZDE5NWYwOTJlNDM1MDViNTQ5OWU3MzJkY2RiOWU4NTIwNjlkNWFkMzVjMTE0MzJjOTkwYWZjZmU2NDAzNyJ9fX0=");
                                                    }else{
                                                        player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                    }
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "Continue the research..");
                                                }
                                            }else{
                                                player.sendMessage(ChatColor.RED + "You must hold a generic talisman to do this!");
                                                break;
                                            }
                                            break;
                                        case "extra_sharpness":
                                            if (Config.getAnimusStudyLevel(player) >= 2) {
                                                if (Config.getMana(player) >= 15) {
                                                    Config.setMana(player, Config.getMana(player) - 15);
                                                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                                    if (itemStack.getType().equals(Material.BOOK)) {
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                        itemStack.setType(Material.ENCHANTED_BOOK);
                                                        itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 6, true);
                                                    } else if (itemStack.getType().equals(Material.ENCHANTED_BOOK)) {
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                        itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 6, true);
                                                    } else if (CraftItemStack.asNMSCopy(itemStack).getItem() instanceof ItemSword) {
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                        itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 6, true);
                                                    }else{
                                                        player.sendMessage(ChatColor.RED + "You must hold specific item to do this!");
                                                        break;
                                                    }
                                                    itemStack.setItemMeta(itemMeta);
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                }
                                            }else{
                                                player.sendMessage(ChatColor.RED + "Continue the research..");
                                            }
                                            break;
                                        case "extra_protection":
                                            if (Config.getAnimusStudyLevel(player) >= 3) {
                                                if (Config.getMana(player) >= 15) {
                                                    Config.setMana(player, Config.getMana(player) - 15);
                                                    ItemStack armororbook = player.getInventory().getItemInMainHand();
                                                    ItemMeta armororbookMeta = armororbook.getItemMeta();
                                                    if (armororbook.getType().equals(Material.BOOK)) {
                                                        armororbook.setType(Material.ENCHANTED_BOOK);
                                                        armororbookMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5, true);
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                    } else if (armororbook.getType().equals(Material.ENCHANTED_BOOK)) {
                                                        armororbookMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5, true);
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                    } else if (CraftItemStack.asNMSCopy(armororbook).getItem() instanceof ItemArmor) {
                                                        armororbookMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5, true);
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                    }else{
                                                        player.sendMessage(ChatColor.RED + "You must hold specific item to do this!");
                                                        break;
                                                    }
                                                    armororbook.setItemMeta(armororbookMeta);
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                }
                                            }else{
                                                player.sendMessage(ChatColor.RED + "Continue the research..");
                                            }
                                            break;
                                        case "axe_of_flames":
                                            if (Integer.parseInt(args[3]) <= 2 && ((Integer.parseInt(args[3]) == 1 && Config.getAnimusStudyLevel(player) >= 2) || (Integer.parseInt(args[3]) == 2 && Config.getAnimusStudyLevel(player) >= 3))){
                                                if (Config.getMana(player) >= Integer.parseInt(args[3])*10) {
                                                    Config.setMana(player, Config.getMana(player) - Integer.parseInt(args[3])*10);
                                                    ItemStack itemStack2 = player.getInventory().getItemInMainHand();
                                                    ItemMeta itemMeta2 = itemStack2.getItemMeta();
                                                    if (CraftItemStack.asNMSCopy(itemStack2).getItem() instanceof ItemAxe) {
                                                        itemMeta2.addEnchant(Enchantment.FIRE_ASPECT, Integer.parseInt(args[3]), true);
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                    }else{
                                                        player.sendMessage(ChatColor.RED + "You must hold specific item to do this!");
                                                        break;
                                                    }
                                                    itemStack2.setItemMeta(itemMeta2);
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                }
                                            }else{
                                                player.sendMessage(ChatColor.RED + "Value too large/Further study is needed!");
                                            }
                                            break;
                                        case "heart_scale_armor":
                                            if (Integer.parseInt(args[3]) <= 2 && ((Integer.parseInt(args[3]) == 1 && Config.getAnimusStudyLevel(player) >= 2) || (Integer.parseInt(args[3]) == 2 && Config.getAnimusStudyLevel(player) >= 3))){
                                                if (Config.getMana(player) >= Integer.parseInt(args[3])*10) {
                                                    Config.setMana(player, Config.getMana(player) - Integer.parseInt(args[3])*10);
                                                    ItemStack itemStack3 = player.getInventory().getItemInMainHand();
                                                    ItemMeta itemMeta3 = itemStack3.getItemMeta();
                                                    List<String> lore3 = new ArrayList<>();
                                                    if (CraftItemStack.asNMSCopy(itemStack3).getItem() instanceof ItemArmor) {
                                                        lore3.add(ChatColor.GRAY + "Heart Scales " + Integer.parseInt(args[3]));
                                                        itemMeta3.addEnchant(main.glow, 1, true);
                                                    }else{
                                                        player.sendMessage(ChatColor.RED + "You must hold specific item to do this!");
                                                        break;
                                                    }
                                                    if (!lore3.isEmpty()) {
                                                        itemMeta3.setLore(lore3);
                                                        itemStack3.setItemMeta(itemMeta3);
                                                    }
                                                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                }
                                            }else{
                                                player.sendMessage(ChatColor.RED + "Value too large/Further study is needed!");
                                            }
                                            break;
                                        case "help":
                                            player.sendMessage(ChatColor.GOLD + "-----Animus Simple Enchanting Help-----");
                                            player.sendMessage(ChatColor.GOLD + "/animus enchant simple running <lvl>");
                                            player.sendMessage(ChatColor.GOLD + "/animus enchant simple shield <lvl>");
                                            player.sendMessage(ChatColor.GOLD + "/animus enchant simple strength <lvl>");
                                            player.sendMessage(ChatColor.GOLD + "/animus enchant simple glowing 1");
                                            player.sendMessage(ChatColor.GOLD + "/animus enchant simple gliding 1");
                                            player.sendMessage(ChatColor.GOLD + "/animus enchant simple stealth 1");
                                            player.sendMessage(ChatColor.GOLD + "/animus enchant simple extra_sharpness");
                                            player.sendMessage(ChatColor.GOLD + "/animus enchant simple extra_protection");
                                            player.sendMessage(ChatColor.GOLD + "/animus enchant simple axe_of_flames <lvl>");
                                            player.sendMessage(ChatColor.GOLD + "/animus enchant simple heart_scale_armor <lvl>");
                                            break;
                                    }
                                    main.setMaxStackSize(CraftItemStack.asNMSCopy(hand).getItem(), 1);
                                    break;
                                case "weather":
                                    switch (args[2].toLowerCase()){
                                        case "part_clouds":
                                            if (Config.getAnimusStudyLevel(player) >= 2) {
                                                if (Config.getMana(player) >= 5) {
                                                    Config.setMana(player, Config.getMana(player) - 5);
                                                    player.getWorld().setClearWeatherDuration(10000);
                                                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                }
                                            }else{
                                                player.sendMessage(ChatColor.RED + "Further study is required..");
                                            }
                                            break;
                                        case "rain":
                                            if (Config.getAnimusStudyLevel(player) >= 2) {
                                                if (Config.getMana(player) >= 5) {
                                                    Config.setMana(player, Config.getMana(player) - 5);
                                                    player.getWorld().setClearWeatherDuration(0);
                                                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                }
                                            }else{
                                                player.sendMessage(ChatColor.RED + "Further study is required..");
                                            }
                                            break;
                                        case "thunder":
                                            if (Config.getAnimusStudyLevel(player) >= 3) {
                                                if (Config.getMana(player) >= 15) {
                                                    Config.setMana(player, Config.getMana(player) - 15);
                                                    for (int i = 0; i < 11; ++i) {
                                                        Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(8)).toLocation(player.getWorld(),
                                                                player.getLocation().getYaw(),
                                                                player.getLocation().getPitch());
                                                        player.getWorld().strikeLightning(loc);
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                    }
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                }
                                            }else{
                                                player.sendMessage(ChatColor.RED + "Further study is required..");
                                            }
                                            break;
                                        case "help":
                                            player.sendMessage(ChatColor.GOLD + "-----Animus Weather Enchanting Help-----");
                                            player.sendMessage(ChatColor.GOLD + "/animus enchant weather part_clouds - Clears the weather");
                                            player.sendMessage(ChatColor.GOLD + "/animus enchant weather rain - Makes it rain");
                                            player.sendMessage(ChatColor.GOLD + "/animus enchant weather thunder - Summons thunder");
                                            break;
                                    }
                                    break;
                                case "curses":
                                    switch (args[2].toLowerCase()){
                                        case "binding":
                                            if (Config.getAnimusStudyLevel(player) >= 2) {
                                                if (Config.getMana(player) >= 10) {
                                                    Config.setMana(player, Config.getMana(player) - 10);
                                                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                                    itemMeta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
                                                    itemStack.setItemMeta(itemMeta);
                                                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                }
                                            }else{
                                                player.sendMessage(ChatColor.RED + "Further study is required..");
                                            }
                                            break;
                                        case "vanishing":
                                            if (Config.getAnimusStudyLevel(player) >= 2) {
                                                if (Config.getMana(player) >= 10) {
                                                    Config.setMana(player, Config.getMana(player) - 10);
                                                    ItemStack itemStack2 = player.getInventory().getItemInMainHand();
                                                    ItemMeta itemMeta2 = itemStack2.getItemMeta();
                                                    itemMeta2.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
                                                    itemStack2.setItemMeta(itemMeta2);
                                                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                }
                                            }else{
                                                player.sendMessage(ChatColor.RED + "Further study is required..");
                                            }
                                            break;
                                        case "help":
                                            player.sendMessage(ChatColor.GOLD + "-----Animus Curses Enchanting Help-----");
                                            player.sendMessage(ChatColor.GOLD + "/animus enchant curses binding - give held item binding curse");
                                            player.sendMessage(ChatColor.GOLD + "/animus enchant simple vanishing - give held item vanishing curse");
                                            break;
                                    }
                                    break;
                                case "playercurses":
                                    if (args.length == 2){
                                        player.sendMessage(ChatColor.RED + "Please state a player name!");
                                }else{
                                    Player otherPlayer = Bukkit.getPlayer(args[2]);
                                    switch (args[3].toLowerCase()){
                                        case "sickness":
                                            if (Config.getAnimusStudyLevel(player) >= 2) {
                                                if (Config.getMana(player) >= 15) {
                                                    Config.setMana(player, Config.getMana(player) - 15);
                                                    otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 0));
                                                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                }
                                            }else{
                                                player.sendMessage(ChatColor.RED + "You can't handle this..");
                                            }
                                            break;
                                        case "degradation":
                                            if (Config.getAnimusStudyLevel(player) >= 3) {
                                                if (Config.getMana(player) >= 40) {
                                                    Config.setMana(player, Config.getMana(player) - 40);
                                                    otherPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(otherPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - 3);
                                                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                }
                                            }else{
                                                player.sendMessage(ChatColor.RED + "You must study more!");
                                            }
                                            break;
                                        case "undead":
                                            if (Config.getAnimusStudyLevel(player) >= 2) {
                                                if (Config.getMana(player) >= 30) {
                                                    Config.setMana(player, Config.getMana(player) - 30);
                                                    Config.setUndead(otherPlayer, true);
                                                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                }
                                            }else{
                                                player.sendMessage(ChatColor.RED + "You must study more!");
                                            }
                                            break;
                                        case "blindness":
                                            if (Config.getAnimusStudyLevel(player) >= 2) {
                                                if (Config.getMana(player) >= 15) {
                                                    Config.setMana(player, Config.getMana(player) - 15);
                                                    otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0));
                                                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                }
                                            }else{
                                                player.sendMessage(ChatColor.RED + "You must study more!");
                                            }
                                            break;
                                        case "help":
                                            player.sendMessage(ChatColor.GOLD + "-----Animus Curses Enchanting Help-----");
                                            player.sendMessage(ChatColor.GOLD + "/animus enchant playercurses sickness - give poison for 5 seconds");
                                            player.sendMessage(ChatColor.GOLD + "/animus enchant playercurses degradation - give -3 hp");
                                            player.sendMessage(ChatColor.GOLD + "/animus enchant playercurses undead - makes player burn in sunlight");
                                            player.sendMessage(ChatColor.GOLD + "/animus enchant playercurses blindness - gives blindness");
                                            break;
                                    }
                                }
                                    break;
                                case "extreme":
                                    player.setHealth(0);
                                    new BukkitRunnable() {
                                        @Override
                                        public void run () {
                                            Bukkit.getPluginManager().callEvent(new PlayerDeathEvent(player, new ArrayList<>(), 0, player.getLevel(), player.getName() + " couldn't handle extreme animus magic!"));
                                        }
                                    }.runTaskLater(Main.getInstance(), 5);
                                    break;
                                case "playerbuff":
                                    if (args.length == 2){
                                        player.sendMessage(ChatColor.RED + "Please state a player name!");
                                    }else{
                                        Player otherPlayer = Bukkit.getPlayer(args[2]);
                                        switch (args[3].toLowerCase()){
                                            case "health_boost":
                                                if (Integer.parseInt(args[4]) <= 4 && (Integer.parseInt(args[4]) - 1 > Config.getAnimusStudyLevel(player))) {
                                                    if (Config.getMana(player) >= Integer.parseInt(args[4])*2) {
                                                        Config.setMana(player, Config.getMana(player) - Integer.parseInt(args[4])*2);
                                                        player.setMaxHealth(player.getMaxHealth() + 4 + (Integer.parseInt(args[4]) - 1));
                                                        player.setHealth(player.getHealth() + 4 + (Integer.parseInt(args[4]) - 1));
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                    }else{
                                                        player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                    }
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "Value too large/Further study is needed!");
                                                }
                                                break;
                                            case "swift_talons":
                                                if (Integer.parseInt(args[4]) <= 5 && (Integer.parseInt(args[4]) - 1 > Config.getAnimusStudyLevel(player))) {
                                                    if (Config.getMana(player) >= Integer.parseInt(args[4])*10) {
                                                        Config.setMana(player, Config.getMana(player) - Integer.parseInt(args[4])*10);
                                                        if (Integer.parseInt(args[4]) < 3){
                                                            otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, Integer.parseInt(args[4]) - 1));
                                                        }else{
                                                            otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, Integer.parseInt(args[4]) - 1));
                                                            otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, Integer.parseInt(args[4]) - 4));
                                                        }
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                    }else{
                                                        player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                    }
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "Value too large/Further study is needed!");
                                                }
                                                break;
                                            case "heal_injury":
                                                if (Integer.parseInt(args[4]) <= 5 && (Integer.parseInt(args[4]) - 1 <= Config.getAnimusStudyLevel(player))) {
                                                    if (Config.getMana(player) >= Integer.parseInt(args[4])*5) {
                                                        Config.setMana(player, Config.getMana(player) - Integer.parseInt(args[4])*5);
                                                        otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60 + (Integer.parseInt(args[4]) - 1), Integer.parseInt(args[4]) - 1));
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                    }else{
                                                        player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                    }
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "Value too large/Further study is needed!");
                                                }
                                                break;
                                            case "stone_scales":
                                                if (Integer.parseInt(args[4]) <= 4 && (Integer.parseInt(args[4]) - 1 <= Config.getAnimusStudyLevel(player))) {
                                                    if (Config.getMana(player) >= Integer.parseInt(args[4])*10) {
                                                        Config.setMana(player, Config.getMana(player) - Integer.parseInt(args[4])*10);
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                        if (Integer.parseInt(args[4]) == 3) {
                                                            otherPlayer.removePotionEffect(PotionEffectType.SLOW);
                                                            otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 0));
                                                            otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, Integer.parseInt(args[4]) - 1));
                                                        } else if (Integer.parseInt(args[4]) >= 4) {
                                                            otherPlayer.removePotionEffect(PotionEffectType.SLOW);
                                                            otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0));
                                                            otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 1));
                                                            otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, Integer.parseInt(args[4]) - 1));
                                                        } else {
                                                            otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 3));
                                                            otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, Integer.parseInt(args[4]) - 1));
                                                        }
                                                    }else{
                                                        player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                    }
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "Value too large/Further study is needed!");
                                                }
                                                break;
                                            case "leaping_talons":
                                                if (Integer.parseInt(args[4]) <= 3 && (Integer.parseInt(args[4]) - 1 <= Config.getAnimusStudyLevel(player))) {
                                                    if (Config.getMana(player) >= Integer.parseInt(args[4])*2) {
                                                        Config.setMana(player, Config.getMana(player) - Integer.parseInt(args[4])*2);
                                                        otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 320, Integer.parseInt(args[4]) - 1));
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                    }else{
                                                        player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                    }
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "Value too large/Further study is needed!");
                                                }
                                                break;
                                            case "magma_scales":
                                                if (Integer.parseInt(args[4]) <= 2 && (Integer.parseInt(args[4]) - 1 <= Config.getAnimusStudyLevel(player))) {
                                                    if (Config.getMana(player) >= Integer.parseInt(args[4])*10) {
                                                        Config.setMana(player, Config.getMana(player) - Integer.parseInt(args[4])*10);
                                                        otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 3));
                                                        otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                    }else{
                                                        player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                    }
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "Value too large/Further study is needed!");
                                                }
                                                break;
                                            case "rejuvenation":
                                                if (Integer.parseInt(args[4]) <= 2 && (Integer.parseInt(args[4]) - 1 <= Config.getAnimusStudyLevel(player))) {
                                                    if (Config.getMana(player) >= Integer.parseInt(args[4])*5) {
                                                        Config.setMana(player, Config.getMana(player) - Integer.parseInt(args[4])*5);
                                                        otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, Integer.parseInt(args[4])));
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                    }else{
                                                        player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                    }
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "Value too large/Further study is needed!");
                                                }
                                                break;
                                            case "cure_disease":
                                                if (Config.getAnimusStudyLevel(player) >= 2) {
                                                    if (Config.getMana(player) >= 10) {
                                                        Config.setMana(player, Config.getMana(player) - 10);
                                                        otherPlayer.removePotionEffect(PotionEffectType.WITHER);
                                                        otherPlayer.removePotionEffect(PotionEffectType.HUNGER);
                                                        otherPlayer.removePotionEffect(PotionEffectType.POISON);
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                    }else{
                                                        player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                    }
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "Further study is needed..");
                                                }
                                                break;
                                            case "gentle_talons":
                                                if (Config.getAnimusStudyLevel(player) >= 3) {
                                                    if (Config.getMana(player) >= 20) {
                                                        Config.setMana(player, Config.getMana(player) - 20);
                                                        Config.setGentleTalons(otherPlayer, true);
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                    }else{
                                                        player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                    }
                                                }else{
                                                    player.sendMessage(ChatColor.RED + "Further study is needed..");
                                                }
                                                break;
                                            case "shapeshift":
                                                if (Config.getAnimusStudyLevel(player) >= 3) {
                                                    if (Config.getMana(player) >= 20) {
                                                        Config.setMana(player, Config.getMana(player) - 20);
                                                        main.setSkin(otherPlayer, args[4]);
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                        Config.setShapeshift(player, args[4]);
                                                    }else{
                                                        player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                    }
                                                }else {
                                                    player.sendMessage(ChatColor.RED + "Further study is needed..");
                                                }
                                                break;
                                            case "animus":
                                                if (Config.getAnimusStudyLevel(player) >= 4) {
                                                    if (Config.getMana(player) >= 80) {
                                                        Config.setMana(player, Config.getMana(player) - 80);
                                                        Random rand = new Random();
                                                        int i = rand.nextInt(2);
                                                        if (i == 1){
                                                            main.playRandomSound(player, otherPlayer, true);
                                                            Config.setAnimus(otherPlayer, true);
                                                        }else{
                                                            main.playRandomSound(player, otherPlayer, false);
                                                        }
                                                    }else{
                                                        player.sendMessage(ChatColor.RED + "You don't have enough Mana!");
                                                    }
                                                }else {
                                                    player.sendMessage(ChatColor.RED + "Further study is needed..");
                                                }
                                                break;
                                            case "help":
                                                player.sendMessage(ChatColor.GOLD + "-----Animus PlayerBuffs Enchanting Help-----");
                                                player.sendMessage(ChatColor.GOLD + "/animus enchant playerbuff <Player Name> health_boost <lvl> - Heals you <lvl> amount of hearts");
                                                player.sendMessage(ChatColor.GOLD + " ");
                                                player.sendMessage(ChatColor.GOLD + "/animus enchant playerbuff <Player Name> swift_talons <lvl> - Makes you faster...");
                                                player.sendMessage(ChatColor.GOLD + " ");
                                                player.sendMessage(ChatColor.GOLD + "/animus enchant playerbuff <Player Name> heal_injury <lvl> - Give regeneration <lvl>");
                                                player.sendMessage(ChatColor.GOLD + " ");
                                                player.sendMessage(ChatColor.GOLD + "/animus enchant playerbuff <Player Name> stone_scales <lvl> - Gives resistance <lvl> at a cost...");
                                                player.sendMessage(ChatColor.GOLD + " ");
                                                player.sendMessage(ChatColor.GOLD + "/animus enchant playerbuff <Player Name> leaping_talons <lvl> - Gives you jump boost <lvl>");
                                                player.sendMessage(ChatColor.GOLD + " ");
                                                player.sendMessage(ChatColor.GOLD + "/animus enchant playerbuff <Player Name> magma_scales <lvl> - Give fire resistance at a cost...");
                                                player.sendMessage(ChatColor.GOLD + " ");
                                                player.sendMessage(ChatColor.GOLD + "/animus enchant playerbuff <Player Name> rejuvenation <lvl> - Gives instant health <lvl>");
                                                player.sendMessage(ChatColor.GOLD + " ");
                                                player.sendMessage(ChatColor.GOLD + "/animus enchant playerbuff <Player Name> cure_disease - We desperately need this IRL...");
                                                player.sendMessage(ChatColor.GOLD + " ");
                                                player.sendMessage(ChatColor.GOLD + "/animus enchant playerbuff <Player Name> gentle_talons - Infinite Silk Touch");
                                                break;
                                        }
                                    }
                                    break;
                                case "help":
                                    player.sendMessage(ChatColor.GOLD + "-----Animus Enchanting Help-----");
                                    player.sendMessage(ChatColor.GOLD + "/animus enchant simple help - Enchant a simple talisman");
                                    player.sendMessage(ChatColor.GOLD + "/animus enchant extreme help - I...wouldn't");
                                    player.sendMessage(ChatColor.GOLD + "/animus enchant curses help - Give a player a curse");
                                    player.sendMessage(ChatColor.GOLD + "/animus enchant playerbuff help - Give a player a buff");
                                    player.sendMessage(ChatColor.GOLD + "/animus enchant weather help - Change the weather");
                                    break;
                            }
                            break;
                        case "study":
                            player.openInventory(new SkillInventory(player).getInv());
//                            player.sendMessage(ChatColor.GREEN + "Go to " + ChatColor.BOLD + "Starflight" + ChatColor.RESET + "" + ChatColor.GREEN + " at the Jade Mountain Library to start studying!");
//                            player.sendMessage(ChatColor.GREEN + "Your current Animus level is: " + (Config.getAnimusStudyLevel(player) + 1));
//                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 10, 1);
                            break;
                        }
                    }
                }else{
                    player.sendMessage(ChatColor.RED + "Wait....you're not an Animus!");
                }
            }
            return false;
        }
    }
