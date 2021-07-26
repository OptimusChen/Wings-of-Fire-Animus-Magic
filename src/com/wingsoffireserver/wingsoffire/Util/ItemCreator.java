package com.wingsoffireserver.wingsoffire.Util;

import com.wingsoffireserver.wingsoffire.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemCreator {

    public ItemStack createEnchantCategoryItem(Material mat, String name, String owner, String... lore){
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

    public ItemStack createEnchantItem(Material mat, String name, String manaCost, String id, String...lore){
        ItemStack item = new ItemStack(mat);
        if (id.equalsIgnoreCase("")){
            ItemMeta meta = item.getItemMeta();
            List<String> itemLore = new ArrayList<>();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name) + " " + ChatColor.AQUA + manaCost);
            for (String s : lore){
                itemLore.add(ChatColor.translateAlternateColorCodes('&', "&7" + s));
            }
            itemLore.add(" ");
            itemLore.add(ChatColor.YELLOW + "Click to Enchant!");
            meta.setLore(itemLore);
            item.setItemMeta(meta);
        }else{
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            List<String> itemLore = new ArrayList<>();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name) + " " + ChatColor.AQUA + manaCost);
            for (String s : lore){
                itemLore.add(ChatColor.translateAlternateColorCodes('&', "&7" + s));
            }
            itemLore.add(" ");
            itemLore.add(ChatColor.YELLOW + "Click to Enchant!");
            meta.setLore(itemLore);
            item.setItemMeta(meta);
            item = Main.getInstance().IDtoSkull(item, id);
        }
        return item;
    }

    public ItemStack createEnchantItem(Material mat, String name, String manaCost, String owner, boolean b, String...lore){
        ItemStack item = new ItemStack(mat);
        if (owner.equalsIgnoreCase("")){
            ItemMeta meta = item.getItemMeta();
            List<String> itemLore = new ArrayList<>();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name) + " " + ChatColor.AQUA + manaCost);
            for (String s : lore){
                itemLore.add(ChatColor.translateAlternateColorCodes('&', "&7" + s));
            }
            itemLore.add(" ");
            itemLore.add(ChatColor.YELLOW + "Click to Enchant!");
            meta.setLore(itemLore);
            item.setItemMeta(meta);
        }else{
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            List<String> itemLore = new ArrayList<>();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name) + " " + ChatColor.AQUA + manaCost);
            meta.setOwner(owner);
            for (String s : lore){
                itemLore.add(ChatColor.translateAlternateColorCodes('&', "&7" + s));
            }
            itemLore.add(" ");
            itemLore.add(ChatColor.YELLOW + "Click to Enchant!");
            meta.setLore(itemLore);
            item.setItemMeta(meta);
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
