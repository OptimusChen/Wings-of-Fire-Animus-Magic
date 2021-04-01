package com.itech4kids.wingsoffire;

import net.minecraft.server.v1_16_R3.EntityVillager;
import net.minecraft.server.v1_16_R3.EntityVillagerTrader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Config {

    private static Main main;

    public Config(Main m){
        main = m;
        main.getConfig().options().copyDefaults();
        main.saveDefaultConfig();
    }

    public static ArrayList<ItemStack> getAccessories(Player player){
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> list = (ArrayList<ItemStack>) config.getList("stored-accessories");
        return list;
    }

    public static void addActiveAccessories(Player player, ItemStack i) throws IOException {
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> accessories = (ArrayList<ItemStack>) config.getList("stored-active");
        accessories.add(i);
        config.set("stored-active", accessories);
        config.save(file);
    }

    public static void removeAccessories(Player player, ItemStack i) throws IOException {
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> list = (ArrayList<ItemStack>) config.getList("stored-accessories");
        list.remove(i);
        config.set("stored-accessories", list);
        config.save(file);
    }

    public static ArrayList<ItemStack> getActiveAccessories(Player player){
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> list = (ArrayList<ItemStack>) config.getList("stored-active");
        return list;
    }

    public static void addAccessories(Player player, ItemStack i) throws IOException {
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> accessories = (ArrayList<ItemStack>) config.getList("stored-accessories");
        accessories.add(i);
        config.set("stored-accessories", accessories);
        config.save(file);
    }

    public static ArrayList<ItemStack> getActiveTalismans(Player player){
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> list = (ArrayList<ItemStack>) config.getList("stored-talismans");
        return list;
    }

    public static void removeActiveAccessories(Player player, ItemStack i) throws IOException {
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> list = (ArrayList<ItemStack>) config.getList("stored-active");
        list.remove(i);
        config.set("stored-active", list);
        config.save(file);
    }

    public static void addActiveTalismans(Player player, ItemStack i) throws IOException {
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> accessories = (ArrayList<ItemStack>) config.getList("stored-talismans");
        accessories.add(i);
        config.set("stored-talismans", accessories);
        config.save(file);
    }

    public static void removeActiveTalismans(Player player, ItemStack i) throws IOException {
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> list = (ArrayList<ItemStack>) config.getList("stored-talismans");
        list.remove(i);
        config.set("stored-talismans", list);
        config.save(file);
    }

    public static void setAnimus(Player player, boolean b) throws IOException {
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("animus", b);
        config.save(file);
    }

    public static boolean isAnimus(Player player){
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        return config.getBoolean("animus");
    }

    public static void setUndead(Player player, boolean b) throws IOException {
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("undead", b);
        config.save(file);
    }

    public static boolean isUndead(Player player){
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        return config.getBoolean("undead");
    }

    /*
    public static void setNpc(EntityVillagerTrader villager){
        main.getConfig().set("JMA-Npc", villager);
        main.saveConfig();
        main.reloadConfig();
        main.saveDefaultConfig();
    }

    public static EntityVillagerTrader getNpc(){
        return (EntityVillagerTrader) main.getConfig().get("JMA-Npc");
    }
     */

    public static void setGentleTalons(Player player, boolean b) throws IOException {
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("gentle-talons", b);
        config.save(file);
    }

    public static boolean isGentleTalons(Player player){
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        return config.getBoolean("gentle-talons");
    }

    public static void setAnimusStudy(Player player, int i) throws IOException {
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("animus-study-level", i);
        config.save(file);
    }

    public static int getAnimusStudyLevel(Player player){
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        return config.getInt("animus-study-level");
    }

    public static void setMana(Player player, int i) throws IOException {
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("mana", i);
        config.save(file);
    }

    public static int getMana(Player player){
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        return config.getInt("mana");
    }

    public static void createPlayer(String name) throws IOException {
        File folder = new File(main.getDataFolder() + File.separator + "Players");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File playerFile = new File(main.getDataFolder()+File.separator+"Players"+File.separator+Bukkit.getPlayer(name).getUniqueId()+".yml");
        if (!playerFile.exists()) {
            playerFile.createNewFile();
            Random rand = new Random();
            int animus = rand.nextInt(1);
            FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
            ArrayList<ItemStack> storedAccessories = new ArrayList<>();
            ArrayList<ItemStack> accessories = new ArrayList<>();
            ArrayList<ItemStack> activeTalismans = new ArrayList<>();

            config.set("stored-accessories", storedAccessories);
            config.set("stored-active", accessories);
            config.set("stored-talismans", activeTalismans);
            config.set("gentle-talons", false);
            config.set("undead", false);
            config.set("animus-study-level", 0);
            config.set("mana", 100);

            if (animus == 0){
                config.set("animus", true);
                Bukkit.getPlayer(name).sendTitle(ChatColor.AQUA + "" + ChatColor.BOLD + "You're an Animus! ", ChatColor.YELLOW + "Use /animus to learn to use your powers!", 20, 60, 20);
                Bukkit.getPlayer(name).playSound(Bukkit.getPlayer(name).getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 10);
            }else{
                config.set("animus", false);
            }
            config.save(playerFile);
        }
    }






    public static ItemStack getItem(int i, String str){
        ItemStack itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + str + " slot #" + i);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }














    public static void addHelmet(Player player, ItemStack i) throws IOException {
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> accessories = (ArrayList<ItemStack>) config.getList("stored-helmets");
        accessories.add(i);
        config.set("stored-accessories", accessories);
        config.save(file);
    }

    public static ArrayList<ItemStack> getHelmets(Player player){
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> list = (ArrayList<ItemStack>) config.getList("stored-helmets");
        return list;
    }

    public static void removeHelmet(Player player, ItemStack i) throws IOException {
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> list = (ArrayList<ItemStack>) config.getList("stored-helmets");
        list.remove(i);
        config.save(file);
    }

    public static void addChestplate(Player player, ItemStack i) throws IOException {
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> accessories = (ArrayList<ItemStack>) config.getList("stored-chestplates");
        accessories.add(i);
        config.set("stored-accessories", accessories);
        config.save(file);
    }

    public static ArrayList<ItemStack> getChestplates(Player player){
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> list = (ArrayList<ItemStack>) config.getList("stored-chestplates");
        return list;
    }

    public static void removeChestplates(Player player, ItemStack i) throws IOException {
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> list = (ArrayList<ItemStack>) config.getList("stored-chestplates");
        list.remove(i);
        config.save(file);
    }

    public static void addLeggings(Player player, ItemStack i) throws IOException {
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> accessories = (ArrayList<ItemStack>) config.getList("stored-leggings");
        accessories.add(i);
        config.set("stored-accessories", accessories);
        config.save(file);
    }

    public static ArrayList<ItemStack> getLeggings(Player player){
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> list = (ArrayList<ItemStack>) config.getList("stored-leggings");
        return list;
    }

    public static void removeLeggings(Player player, ItemStack i) throws IOException {
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> list = (ArrayList<ItemStack>) config.getList("stored-leggings");
        list.remove(i);
        config.save(file);
    }

    public static void addBoots(Player player, ItemStack i) throws IOException {
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> accessories = (ArrayList<ItemStack>) config.getList("stored-boots");
        accessories.add(i);
        config.set("stored-accessories", accessories);
        config.save(file);
    }

    public static ArrayList<ItemStack> getBoots(Player player){
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> list = (ArrayList<ItemStack>) config.getList("stored-boots");
        return list;
    }

    public static void removeBoots(Player player, ItemStack i) throws IOException {
        File file = new File(main.getDataFolder() + File.separator + "Players" + File.separator + player.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> list = (ArrayList<ItemStack>) config.getList("stored-boots");
        list.remove(i);
        config.save(file);
    }

    public static void addMinerals(Player player, ItemStack i) throws IOException {
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> accessories = (ArrayList<ItemStack>) config.getList("stored-minerals");
        accessories.add(i);
        config.set("stored-accessories", accessories);
        config.save(file);
    }

    public static ArrayList<ItemStack> getMinerals(Player player){
        File file = new File(main.getDataFolder()+File.separator+"Players"+File.separator+player.getUniqueId()+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> list = (ArrayList<ItemStack>) config.getList("stored-minerals");
        return list;
    }

    public static void removeMinerals(Player player, ItemStack i) throws IOException {
        File file = new File(main.getDataFolder() + File.separator + "Players" + File.separator + player.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> list = (ArrayList<ItemStack>) config.getList("stored-minerals");
        list.remove(i);
        config.save(file);
    }

}
