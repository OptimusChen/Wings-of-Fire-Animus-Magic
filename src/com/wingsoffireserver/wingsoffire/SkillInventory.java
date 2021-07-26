package com.wingsoffireserver.wingsoffire;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryCustom;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class SkillInventory {

    Inventory inv;

    public SkillInventory(Player player) {
        inv = Bukkit.createInventory(null, 54, "Animus Level");

        ItemStack space1 = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = space1.getItemMeta();
        itemMeta.setDisplayName(" ");
        space1.setItemMeta(itemMeta);

        ItemStack item = new ItemStack(Material.DIAMOND);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "You Animus Level: " + ChatColor.GOLD + (Config.getAnimusStudyLevel(player) + 1));
        item.setItemMeta(meta);


        for (int i = 0; i < 54; i++){
            inv.setItem(i, space1);
        }

        inv.setItem(9, createRedSquare(1));
        inv.setItem(11, createRedSquare(2));
        inv.setItem(13, createRedSquare(3));
        inv.setItem(15, createRedSquare(4));
        inv.setItem(17, createRedSquare(5));

        inv.setItem(18, talismanItem(1));
        inv.setItem(27, spellItem(1));
        inv.setItem(36, cursesItem(1));
        inv.setItem(45, weatherItem(1));

        inv.setItem(20, talismanItem(2));
        inv.setItem(29, spellItem(2));
        inv.setItem(38, cursesItem(2));
        inv.setItem(47, weatherItem(2));

        inv.setItem(22, talismanItem(3));
        inv.setItem(31, spellItem(3));
        inv.setItem(40, cursesItem(3));
        inv.setItem(49, weatherItem(3));

        inv.setItem(24, talismanItem(4));
        inv.setItem(33, spellItem(4));
        inv.setItem(42, cursesItem(4));
        inv.setItem(51, weatherItem(4));

        inv.setItem(26, talismanItem(5));
        inv.setItem(35, spellItem(5));
        inv.setItem(44, cursesItem(5));
        inv.setItem(53, weatherItem(5));

        inv.setItem(4, item);

        for (int i = 0; i < 5; i++){
            switch (i + 1){
                case 1:
                    inv.setItem(9, createGreenSquare(i + 1));
                    break;
                case 2:
                    inv.setItem(11, createGreenSquare(i + 1));
                    break;
                case 3:
                    inv.setItem(13, createGreenSquare(i + 1));
                    break;
                case 4:
                    inv.setItem(15, createGreenSquare(i + 1));
                    break;
                case 5:
                    inv.setItem(17, createGreenSquare(i + 1));
                    break;
            }
            if (Config.getAnimusStudyLevel(player) == i){
                break;
            }
        }

    }

    public ItemStack talismanItem(int i){
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.GRAY + "Talisman Rewards " + i);
        meta.setOwner("stone");

        for (String s : getTalismanLvlLore(i)){
            lore.add(s);
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack weatherItem(int i){
        ItemStack item = new ItemStack(Material.WATER_BUCKET);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.YELLOW + "Weather Rewards " + i);

        for (String s : getWeatherLvlLore(i)){
            lore.add(s);
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack cursesItem(int i){
        ItemStack item = new ItemStack(Material.PUFFERFISH);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.GRAY + "Curse Rewards " + i);

        for (String s : getCursesLvlLore(i)){
            lore.add(s);
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack spellItem(int i){
        ItemStack item = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.AQUA + "Player Spell Rewards " + i);

        for (String s : getSpellLvlLore(i)){
            lore.add(s);
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;

    }

    public ItemStack createRedSquare(int i){
        ItemStack lvl = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta lvlMeta = lvl.getItemMeta();
        List<String> lore = new ArrayList<>();
        lvlMeta.setDisplayName(ChatColor.RED + "Animus Level " + i);
        lvl.setItemMeta(lvlMeta);

        lore.add(" ");
        lore.add(ChatColor.RED + "NOT UNLOCKED");
        lvlMeta.setLore(lore);
        lvl.setItemMeta(lvlMeta);

        return lvl;
    }

    public ItemStack createGreenSquare(int i){
        ItemStack lvl = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
        ItemMeta lvlMeta = lvl.getItemMeta();
        List<String> lore = new ArrayList<>();
        lvlMeta.setDisplayName(ChatColor.GREEN + "Animus Level " + i);
        lvl.setItemMeta(lvlMeta);

        lore.add(" ");
        lore.add(ChatColor.GREEN + "UNLOCKED");
        lvlMeta.setLore(lore);
        lvl.setItemMeta(lvlMeta);

        return lvl;
    }

    public List<String> getCursesLvlLore(int i) {
        List<String> lore = new ArrayList<>();
        switch (i){
            case 1:
                lore.add(ChatColor.GRAY + "Rewards:");
                lore.add(ChatColor.RED + " None!");
                break;
            case 2:
                lore.add(ChatColor.GRAY + "Rewards:");
                lore.add(ChatColor.RED + " None!");
                break;
            case 3:
                lore.add(ChatColor.GRAY + "Rewards:");
                lore.add(ChatColor.RED + " None!");
                break;
            case 4:
                lore.add(ChatColor.GRAY + "Rewards:");
                lore.add(ChatColor.GRAY + " Curses:");
                lore.add(ChatColor.RED + "  Curse of Binding");
                lore.add(ChatColor.RED + "  Curse of Vanishing");
                lore.add(ChatColor.GRAY + "  Curse of Sickness");
                break;
            case 5:
                lore.add(ChatColor.GRAY + "Rewards:");
                lore.add(ChatColor.GRAY + " Curses:");
                lore.add(ChatColor.RED + "  Curse of Undead");
                lore.add(ChatColor.GRAY + "  Curse of Blindness");
                lore.add(ChatColor.RED + "  Curse of Degredation");
                break;
        }
        return lore;
    }

    public List<String> getWeatherLvlLore(int i) {
        List<String> lore = new ArrayList<>();
        switch (i){
            case 1:
                lore.add(ChatColor.GRAY + "Rewards:");
                lore.add(ChatColor.RED + " None!");
                break;
            case 2:
                lore.add(ChatColor.GRAY + "Rewards:");
                lore.add(ChatColor.RED + " None!");
                break;
            case 3:
                lore.add(ChatColor.GRAY + "Rewards:");
                lore.add(ChatColor.RED + " None!");
                break;
            case 4:
                lore.add(ChatColor.GRAY + "Rewards:");
                lore.add(ChatColor.GRAY + " Weather:");
                lore.add(ChatColor.YELLOW + "  Part Clouds");
                lore.add(ChatColor.AQUA + "  Rain");
                break;
            case 5:
                lore.add(ChatColor.GRAY + "Rewards:");
                lore.add(ChatColor.GRAY + " Weather:");
                lore.add(ChatColor.YELLOW + "  Lightning Bolt");
                lore.add(ChatColor.AQUA + "  Thunderstorm");
                break;
        }
        return lore;
    }

    public List<String> getSpellLvlLore(int i) {
        List<String> lore = new ArrayList<>();
        switch (i){
            case 1:
                lore.add(ChatColor.GRAY + "Rewards:");
                lore.add(ChatColor.GRAY + " Spells:");
                lore.add(ChatColor.DARK_RED + "  Health Boost I");
                lore.add(ChatColor.RED + "  Heal Injury I");
                lore.add(ChatColor.WHITE + "  Swift Talons I");
                break;
            case 2:
                lore.add(ChatColor.GRAY + "Rewards:");
                lore.add(ChatColor.GRAY + " Spells:");
                lore.add(ChatColor.DARK_RED + "  Health Boost II");
                lore.add(ChatColor.RED + "  Heal Injury II");
                lore.add(ChatColor.WHITE + "  Swift Talons II");
                lore.add(ChatColor.GRAY + "  Stone Scales I");
                lore.add(ChatColor.GREEN + "  Leaping Talons I");
                break;
            case 3:
                lore.add(ChatColor.GRAY + "Rewards:");
                lore.add(ChatColor.GRAY + " Spells:");
                lore.add(ChatColor.DARK_RED + "  Health Boost III");
                lore.add(ChatColor.RED + "  Heal Injury III");
                lore.add(ChatColor.WHITE + "  Swift Talons III");
                lore.add(ChatColor.GRAY + "  Stone Scales II");
                lore.add(ChatColor.GREEN + "  Leaping Talons II");
                lore.add(ChatColor.RED + "  Magma Scales I");
                break;
            case 4:
                lore.add(ChatColor.GRAY + "Rewards:");
                lore.add(ChatColor.GRAY + " Spells:");
                lore.add(ChatColor.DARK_RED + "  Health Boost IV");
                lore.add(ChatColor.RED + "  Heal Injury IV");
                lore.add(ChatColor.WHITE + "  Swift Talons IV");
                lore.add(ChatColor.GRAY + "  Stone Scales III");
                lore.add(ChatColor.GREEN + "  Leaping Talons III");
                lore.add(ChatColor.RED + "  Rejuvenation I");
                lore.add(ChatColor.GRAY + "  Cure Disease");
                lore.add(ChatColor.RED + "  Magma Scales I");
                break;
            case 5:
                lore.add(ChatColor.GRAY + "Rewards:");
                lore.add(ChatColor.GRAY + " Spells:");
                lore.add(ChatColor.RED + "  Heal Injury V");
                lore.add(ChatColor.WHITE + "  Swift Talons V");
                lore.add(ChatColor.GRAY + "  Stone Scales IV");
                lore.add(ChatColor.RED + "  Rejuvenation II");
                lore.add(ChatColor.RED + "  Magma Scales II");
                lore.add(ChatColor.GREEN + "  Species Shift");
                lore.add(ChatColor.AQUA + "  Gentile Talons");
                break;
        }
        return lore;
    }

        public List<String> getTalismanLvlLore(int i){
        List<String> lore = new ArrayList<>();
        switch (i){
            case 1:
                lore.add(ChatColor.GRAY + "Rewards:");
                lore.add(ChatColor.GRAY + " Talismans:");
                lore.add(ChatColor.BLUE + "  Running Talisman I");
                lore.add(ChatColor.GRAY + "  Shield Talisman I");
                break;
            case 2:
                lore.add(ChatColor.GRAY + "Rewards:");
                lore.add(ChatColor.GRAY + " Talismans:");
                lore.add(ChatColor.BLUE + "  Running Talisman II");
                lore.add(ChatColor.GRAY + "  Shield Talisman II");
                lore.add(ChatColor.DARK_RED + "  Strength Talisman I");
                lore.add(ChatColor.WHITE + "  Gliding Talisman I");
                break;
            case 3:
                lore.add(ChatColor.GRAY + "Rewards:");
                lore.add(ChatColor.GRAY + " Talismans:");
                lore.add(ChatColor.BLUE + "  Running Talisman III");
                lore.add(ChatColor.GRAY + "  Shield Talisman III");
                lore.add(ChatColor.DARK_RED + "  Strength Talisman II");
                lore.add(ChatColor.WHITE + "  Stealth Talisman I");
                lore.add(ChatColor.YELLOW + "  Glowing Talisman I");
                break;
            case 4:
                lore.add(ChatColor.GRAY + "Rewards:");
                lore.add(ChatColor.GRAY + " Talismans:");
                lore.add(ChatColor.BLUE + "  Running Talisman IV");
                lore.add(ChatColor.GRAY + "  Shield Talisman IV");
                lore.add(ChatColor.DARK_RED + "  Strength Talisman III");
                lore.add(ChatColor.AQUA + "  Sharpness VI");
                lore.add(ChatColor.AQUA + "  Protection V");
                lore.add(ChatColor.RED + "  Axe of Flames I");
                lore.add(ChatColor.AQUA + "  Heart Scales I");
                break;
            case 5:
                lore.add(ChatColor.GRAY + "Rewards:");
                lore.add(ChatColor.GRAY + " Talismans:");
                lore.add(ChatColor.BLUE + "  Running Talisman V");
                lore.add(ChatColor.GRAY + "  Shield Talisman V");
                lore.add(ChatColor.DARK_RED + "  Strength Talisman IV");
                lore.add(ChatColor.RED + "  Axe of Flames II");
                lore.add(ChatColor.AQUA + "  Heart Scales II");
                break;
        }
        return lore;
    }

    public Inventory getInv(){
        return inv;
    }
}
