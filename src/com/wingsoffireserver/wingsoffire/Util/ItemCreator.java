package com.wingsoffireserver.wingsoffire.Util;

import com.wingsoffireserver.wingsoffire.Config;
import com.wingsoffireserver.wingsoffire.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemCreator {

    public static ItemStack createEnchantCategoryItem(Material mat, String name, String owner, String... lore){
        ItemStack item = new ItemStack(mat);
        if (owner.equalsIgnoreCase("")) {
            ItemMeta meta = item.getItemMeta();
            List<String> itemLore = new ArrayList<>(Arrays.asList(lore));
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            for (String s : lore){
                itemLore.add(ChatColor.translateAlternateColorCodes('&', "&7" + s));
            }
            itemLore.add("");
            itemLore.add(ChatColor.YELLOW + "Click to enchant!");
            meta.setLore(itemLore);
            item.setItemMeta(meta);
        }else{
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            List<String> itemLore = new ArrayList<>(Arrays.asList(lore));
            meta.setOwner(owner);
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            for (String s : lore){
                itemLore.add(ChatColor.translateAlternateColorCodes('&', "&7" + s));
            }
            itemLore.add("");
            itemLore.add(ChatColor.YELLOW + "Click to enchant!");
            meta.setLore(itemLore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createEnchantItem(Material mat, String name, String manaCost, String id, int maxLvl, String...lore){
        ItemStack item = new ItemStack(mat);
        if (id.equalsIgnoreCase("")){
            ItemMeta meta = item.getItemMeta();
            List<String> itemLore = new ArrayList<>();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            for (String s : lore){
                itemLore.add(ChatColor.translateAlternateColorCodes('&', "&7" + s));
            }
            itemLore.add(" ");
            itemLore.add(ChatColor.AQUA + "Mana Cost: " + ChatColor.GRAY + manaCost);
            itemLore.add(ChatColor.YELLOW + "Click to Enchant!");
            meta.setLore(itemLore);
            item.setItemMeta(meta);
        }else{
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            List<String> itemLore = new ArrayList<>();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            for (String s : lore){
                itemLore.add(ChatColor.translateAlternateColorCodes('&', "&7" + s));
            }
            itemLore.add(" ");
            itemLore.add(ChatColor.AQUA + "Mana Cost: " + ChatColor.GRAY + manaCost);
            itemLore.add(ChatColor.AQUA + "Max Level: " + ChatColor.GRAY + Main.getInstance().IntegerToRomanNumeral(maxLvl));
            itemLore.add(" ");
            itemLore.add(ChatColor.YELLOW + "Click to Enchant!");
            meta.setLore(itemLore);
            item.setItemMeta(meta);
            item = Main.getInstance().IDtoSkull(item, id);
        }
        return item;
    }

    public static ItemStack createEnchantItem(Material mat, String name, String manaCost, String owner, int maxLvl, boolean b, String...lore){
        ItemStack item = new ItemStack(mat);
        if (owner.equalsIgnoreCase("")){
            ItemMeta meta = item.getItemMeta();
            List<String> itemLore = new ArrayList<>();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            for (String s : lore){
                itemLore.add(ChatColor.translateAlternateColorCodes('&', "&7" + s));
            }
            itemLore.add(" ");
            itemLore.add(ChatColor.AQUA + "Mana Cost: " + ChatColor.GRAY + manaCost);
            itemLore.add(ChatColor.AQUA + "Max Level: " + ChatColor.GRAY + Main.getInstance().IntegerToRomanNumeral(maxLvl));
            itemLore.add(" ");
            itemLore.add(ChatColor.YELLOW + "Click to Enchant!");
            meta.setLore(itemLore);
            item.setItemMeta(meta);
        }else{
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            List<String> itemLore = new ArrayList<>();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            meta.setOwner(owner);
            for (String s : lore){
                itemLore.add(ChatColor.translateAlternateColorCodes('&', "&7" + s));
            }
            itemLore.add(" ");
            itemLore.add(ChatColor.AQUA + "Mana Cost: " + ChatColor.GRAY + manaCost);
            itemLore.add(ChatColor.AQUA + "Max Level: " + ChatColor.GRAY + Main.getInstance().IntegerToRomanNumeral(maxLvl));
            itemLore.add(" ");
            itemLore.add(ChatColor.YELLOW + "Click to Enchant!");
            meta.setLore(itemLore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createEnchantItem(Material mat, String name, String manaCost, boolean glow, int maxLvl, String...lore){
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        List<String> itemLore = new ArrayList<>();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        for (String s : lore){
            itemLore.add(ChatColor.translateAlternateColorCodes('&', "&7" + s));
        }
        itemLore.add(" ");
        itemLore.add(ChatColor.AQUA + "Mana Cost: " + ChatColor.GRAY + manaCost);
        itemLore.add(ChatColor.AQUA + "Max Level: " + ChatColor.GRAY + Main.getInstance().IntegerToRomanNumeral(maxLvl));
        itemLore.add(" ");
        itemLore.add(ChatColor.YELLOW + "Click to Enchant!");

        if (glow){
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
        }

        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.setLore(itemLore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createEnchantItem(Material mat, String name, String manaCost, boolean glow, String...lore){
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        List<String> itemLore = new ArrayList<>();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        for (String s : lore){
            itemLore.add(ChatColor.translateAlternateColorCodes('&', "&7" + s));
        }
        itemLore.add(" ");
        itemLore.add(ChatColor.AQUA + "Mana Cost: " + ChatColor.GRAY + manaCost);
        itemLore.add(ChatColor.YELLOW + "Click to Enchant!");

        if (glow){
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
        }

        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.setLore(itemLore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createNotUnlockedItem(Material mat, String name, String manaCost, int unlockLvl, String...lore){
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        List<String> itemLore = new ArrayList<>();
        meta.setDisplayName(ChatColor.RED + "[NOT UNLOCKED] " + name);
        for (String s : lore){
            itemLore.add(ChatColor.translateAlternateColorCodes('&', "&7" + s));
        }
        itemLore.add(" ");
        itemLore.add(ChatColor.RED + "Unlock at: " + ChatColor.GRAY + "Lvl " + unlockLvl);

        meta.setLore(itemLore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material mat, String name, String manaCost, boolean glow, int requiredLvl, Player player, String...lore){
        int level = Config.getAnimusStudyLevel(player) + 1;

        ItemStack item;

        if (level >= requiredLvl){
            item = createEnchantItem(mat, name, manaCost, glow, lore);
        }else{
            item = createNotUnlockedItem(mat, name, manaCost, requiredLvl, lore);
        }

        return item;
    }

    public static ItemStack goBack(){
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Go Back");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack extiBarrier(){
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Close");
        item.setItemMeta(meta);
        return item;
    }
}
