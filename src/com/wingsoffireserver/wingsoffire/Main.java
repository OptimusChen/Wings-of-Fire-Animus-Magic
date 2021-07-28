package com.wingsoffireserver.wingsoffire;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wingsoffireserver.wingsoffire.Armor.ArmorListener;
import com.wingsoffireserver.wingsoffire.Armor.DispenserArmorListener;
import com.wingsoffireserver.wingsoffire.ArmorEvent.EventAnalyser;
import com.wingsoffireserver.wingsoffire.Commands.*;

import com.wingsoffireserver.wingsoffire.Commands.*;
import com.wingsoffireserver.wingsoffire.Inventory.WoFInventory;
import com.wingsoffireserver.wingsoffire.NPC.NPC;
import com.wingsoffireserver.wingsoffire.NPC.NPCListener;
import com.wingsoffireserver.wingsoffire.NPC.PacketReader;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import com.wingsoffireserver.wingsoffire.Util.InventoryAnimus;
import com.wingsoffireserver.wingsoffire.Util.ItemCreator;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.Base64;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class Main extends JavaPlugin {

    public Inventory animusInventory;
    public Inventory enchantingInventory;
    public Inventory simpleEnchantingInventory;
    public Inventory playerEnchantingInventory;
    public Inventory weatherEnchantingInventory;
    public Inventory cursesEnchantingInventory;
    public Inventory selectNumber;
    public Inventory onlinePlayers;
    public Inventory shapeshiftInventory;
    public Inventory studyInventory;
    public static Main main;
    public HashMap<String, ActivePlayer> players;
    public NamespacedKey key;
    public Glow glow;

    @Override
    public void onEnable(){
        this.key = new NamespacedKey(this, getDescription().getName());
        this.glow = new Glow(key);
        players = new HashMap<>();
        new Config(this);
        animusInventory = Bukkit.createInventory(null, 45, "Animus Magic GUI");
        enchantingInventory = Bukkit.createInventory(null, 45, "Animus Enchanting");
        simpleEnchantingInventory = Bukkit.createInventory(null, 45, "Animus Item Enchanting");
        playerEnchantingInventory = Bukkit.createInventory(null, 45, "Animus Player Enchanting");
        weatherEnchantingInventory = Bukkit.createInventory(null, 45, "Animus Weather Enchanting");
        cursesEnchantingInventory = Bukkit.createInventory(null, 45, "Animus Curse Enchanting");
        studyInventory = Bukkit.createInventory(null, 27, "Animus Studying");
        selectNumber = Bukkit.createInventory(null, 9, "Select Number");
        onlinePlayers = Bukkit.createInventory(null, 54, "Select Player");
        shapeshiftInventory = Bukkit.createInventory(null, 9, "Select Tribe");
        this.main = this;
        getServer().getPluginManager().registerEvents(new Eventlistener(this), this);
        getServer().getPluginManager().registerEvents(new NPCListener(), this);
        getServer().getPluginManager().registerEvents(new AccessoryBagListener(this), this);
        getServer().getPluginManager().registerEvents(new ArmorListener(), this);
        getServer().getPluginManager().registerEvents(new DispenserArmorListener(), this);
        getServer().getPluginManager().registerEvents(new EventAnalyser(), this);
        this.getCommand("ab").setExecutor(new AccessoryBagCmd(this));
        this.getCommand("activeplayer").setExecutor(new ActivePlayerAddCmd(this));
        this.getCommand("animus").setExecutor(new AnimusCmd(this));
        this.getCommand("customItems").setExecutor(new CustomItemsCmd(this));
        this.getCommand("spawnstudynpc").setExecutor(new SpawnNpcCmd(this));
        this.getCommand("aadmin").setExecutor(new AdminCommand());
        loadRecipe();
        registerGlow();
        if (getConfig().contains("data")) {
            loadNpc();
        }
        if (!Bukkit.getOnlinePlayers().isEmpty()){
            for (Player player : Bukkit.getOnlinePlayers()) {
                PacketReader reader = new PacketReader();
                reader.inject(player);
            }
        }
    }

    @Override
    public void onDisable(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            PacketReader reader = new PacketReader();
            reader.unInject(player);

            for (EntityPlayer npc : com.wingsoffireserver.wingsoffire.NPC.NPC.getNpcs()){
                com.wingsoffireserver.wingsoffire.NPC.NPC.removeNpc(player, npc);
            }
        }
    }

    public static Main getInstance(){
        return main;
    }

    public static void registerEntity(String name, int id, Class<? extends EntityInsentient> customClass) {
        try {
            List<Map<?, ?>> dataMaps = new ArrayList<Map<?, ?>>();
            for (Field f : EntityTypes.class.getDeclaredFields()) {
                if (f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                    f.setAccessible(true);
                    dataMaps.add((Map<?, ?>) f.get(null));
                }
            }
            ((Map<Class<? extends EntityInsentient>, String>) dataMaps.get(1)).put(customClass, name);
            ((Map<Class<? extends EntityInsentient>, Integer>) dataMaps.get(3)).put(customClass, id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ActivePlayer getActivePlayer(String name){
        return players.get(name);
    }

    public void setMaxStackSize(Item item, int i){
        try {

            Field field = Item.class.getDeclaredField("maxStackSize");
            field.setAccessible(true);
            field.setInt(item, i);

        } catch (Exception e) {
        }
    }

    public int convertFromRoman(String roman) {
        Map<String, Integer> v = new HashMap<String, Integer>();
        v.put("IV", 4);
        v.put("IX", 9);
        v.put("XL", 40);
        v.put("CD", 400);
        v.put("CM", 900);
        v.put("C", 100);
        v.put("M", 1000);
        v.put("I", 1);
        v.put("V", 5);
        v.put("X", 10);
        v.put("L", 50);
        v.put("D", 500);
        int result = 0;
        for (String s : v.keySet()) {
            result += countOccurrences(roman, s) * v.get(s);
            roman = roman.replaceAll(s, "");
        }

        return result;
    }

    public int countOccurrences(String main, String sub) {
        return (main.length() - main.replace(sub, "").length()) / sub.length();
    }

    public String IntegerToRomanNumeral(int input) {
        if (input < 1 || input > 3999)
            return "Invalid Roman Number Value";
        String s = "";
        while (input >= 1000) {
            s += "M";
            input -= 1000;        }
        while (input >= 900) {
            s += "CM";
            input -= 900;
        }
        while (input >= 500) {
            s += "D";
            input -= 500;
        }
        while (input >= 400) {
            s += "CD";
            input -= 400;
        }
        while (input >= 100) {
            s += "C";
            input -= 100;
        }
        while (input >= 90) {
            s += "XC";
            input -= 90;
        }
        while (input >= 50) {
            s += "L";
            input -= 50;
        }
        while (input >= 40) {
            s += "XL";
            input -= 40;
        }
        while (input >= 10) {
            s += "X";
            input -= 10;
        }
        while (input >= 9) {
            s += "IX";
            input -= 9;
        }
        while (input >= 5) {
            s += "V";
            input -= 5;
        }
        while (input >= 4) {
            s += "IV";
            input -= 4;
        }
        while (input >= 1) {
            s += "I";
            input -= 1;
        }
        return s;
    }

    public ItemStack IDtoSkull(ItemStack head, String id) {
        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(new String(Base64.decodeBase64(id))).getAsJsonObject();
        String skinUrl = o.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(("{textures:{SKIN:{url:\"" + skinUrl + "\"}}}").getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    public void openStudyGui(Player player){
        studyInventory.clear();
        ItemStack space1 = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = space1.getItemMeta();
        itemMeta.setDisplayName(" ");
        space1.setItemMeta(itemMeta);

        for (int i = 0; i < 27; ++i){
            studyInventory.setItem(i, space1);
        }

        ItemStack study = new ItemStack(Material.BOOK);
        ItemMeta meta = study.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.AQUA + "Start Study");
        lore.add(ChatColor.GRAY + "Click to start your");
        lore.add(ChatColor.GRAY + "study session!");
        lore.add(ChatColor.GRAY + " ");
        lore.add(ChatColor.YELLOW + "Click to start!");
        meta.setLore(lore);
        study.setItemMeta(meta);
        studyInventory.setItem(13, study);

        player.openInventory(studyInventory);
    }

    public void openShapeShiftGui(Player player){
        shapeshiftInventory.clear();
        ItemStack space1 = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = space1.getItemMeta();
        itemMeta.setDisplayName(" ");

        ItemStack item = new ItemStack(Material.OBSIDIAN);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.DARK_GRAY + "NightWing");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Click to select!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        shapeshiftInventory.addItem(item);

        lore.clear();
        item.setType(Material.PACKED_ICE);
        meta.setDisplayName(ChatColor.WHITE + "IceWing");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Click to select!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        shapeshiftInventory.addItem(item);

        lore.clear();
        item.setType(Material.BROWN_CONCRETE_POWDER);
        meta.setDisplayName(ChatColor.RED + "MudWing");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Click to select!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        shapeshiftInventory.addItem(item);

        lore.clear();
        item.setType(Material.OAK_LEAVES);
        meta.setDisplayName(ChatColor.GREEN + "RainWing");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Click to select!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        shapeshiftInventory.addItem(item);

        lore.clear();
        item.setType(Material.PRISMARINE);
        meta.setDisplayName(ChatColor.AQUA + "SeaWing");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Click to select!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        shapeshiftInventory.addItem(item);

        lore.clear();
        item.setType(Material.SAND);
        meta.setDisplayName(ChatColor.GOLD + "SandWing");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Click to select!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        shapeshiftInventory.addItem(item);

        lore.clear();
        item.setType(Material.NETHERRACK);
        meta.setDisplayName(ChatColor.DARK_RED + "SkyWing");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Click to select!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        shapeshiftInventory.addItem(item);

        lore.clear();
        item.setType(Material.PLAYER_HEAD);
        meta.setDisplayName(ChatColor.GRAY + "Default");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Click to select!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        shapeshiftInventory.addItem(item);

        shapeshiftInventory.setItem(8, space1);

        player.openInventory(shapeshiftInventory);
    }

    public void openOnlinePlayers(Player player){
        onlinePlayers.clear();
        for (Player player1 : Bukkit.getOnlinePlayers()){
            ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
            List<String> lore = new ArrayList<>();
            itemMeta.setOwner(player1.getName());
            itemMeta.setDisplayName(ChatColor.GRAY + player1.getName());
            lore.add(ChatColor.YELLOW + " ");
            lore.add(ChatColor.YELLOW + "Click to select player!");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            onlinePlayers.addItem(itemStack);
        }
        player.openInventory(onlinePlayers);
    }

    public void openSelectNumber(Player player, int maxLevel){
        ActivePlayer activePlayer = getActivePlayer(player.getName());
        activePlayer.isSelectingNumber = true;
        activePlayer.maxLevel = maxLevel;
        player.sendMessage(ChatColor.GRAY + "Please enter your query in chat or type " + ChatColor.BOLD + "cancel " +
                ChatColor.RESET + "" + ChatColor.GRAY + "to cancel.\nThe max level for this enchantment is: " + ChatColor.BOLD +
                IntegerToRomanNumeral(maxLevel));
        player.closeInventory();
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 10, 1);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    if (activePlayer.isSelectingNumber){
                        player.sendMessage(ChatColor.GRAY + "Cancelled enchantment! You took too long!");
                        activePlayer.needsToSelectNumber = false;
                        activePlayer.isSelectingNumber = false;
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskLater(this, 30*20);
    }

    public void openAnimusGui(Player player){
        animusInventory.clear();
        ItemStack space1 = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = space1.getItemMeta();
        itemMeta.setDisplayName(" ");
        space1.setItemMeta(itemMeta);

        ItemStack space2 = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta2 = space2.getItemMeta();
        itemMeta2.setDisplayName(" ");
        space2.setItemMeta(itemMeta2);

        ItemStack study = new ItemStack(Material.BOOK, 1);
        ItemMeta studyMeta = study.getItemMeta();
        List<String> studyLore = new ArrayList<>();
        studyMeta.setDisplayName(ChatColor.AQUA + "Animus Studying");
        studyLore.add(ChatColor.GRAY + "The more you study, the");
        studyLore.add(ChatColor.GRAY + "less Mana your enchantments");
        studyLore.add(ChatColor.GRAY + "cost!");
        studyLore.add(ChatColor.GRAY + " ");
        studyLore.add(ChatColor.YELLOW + "Click to begin!");
        studyMeta.setLore(studyLore);
        study.setItemMeta(studyMeta);

        ItemStack enchant = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
        ItemMeta enchantMeta = enchant.getItemMeta();
        List<String> enchantLore = new ArrayList<>();
        enchantMeta.setDisplayName(ChatColor.YELLOW + "Animus Enchanting");
        enchantLore.add(ChatColor.GRAY + "As an animus, you can enchant");
        enchantLore.add(ChatColor.GRAY + "Talismans, or even Players!");
        enchantLore.add(ChatColor.GRAY + " ");
        enchantLore.add(ChatColor.YELLOW + "Click to enchant!");
        enchantMeta.setLore(enchantLore);
        enchant.setItemMeta(enchantMeta);

        for (int i = 0; i < 45; ++i){
            animusInventory.setItem(i, space1);
        }

        for (int i = 0; i < 9; ++i){
            animusInventory.setItem(i, space2);
        }
        animusInventory.setItem(9, space2);
        animusInventory.setItem(17, space2);
        animusInventory.setItem(18, space2);
        animusInventory.setItem(26, space2);
        animusInventory.setItem(27, space2);
        animusInventory.setItem(35, space2);

        for (int i = 36; i < 45; ++i){
            animusInventory.setItem(i, space2);
        }

        animusInventory.setItem(20, enchant);
        animusInventory.setItem(24, study);
        player.openInventory(animusInventory);
    }

    public void openAnimusEnchantingGUI(Player player){
        enchantingInventory.clear();
        ItemStack space1 = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = space1.getItemMeta();
        itemMeta.setDisplayName(" ");
        space1.setItemMeta(itemMeta);

        ItemStack space2 = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta2 = space2.getItemMeta();
        itemMeta2.setDisplayName(" ");
        space2.setItemMeta(itemMeta2);

        ItemStack talismans = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta talismansMeta = (SkullMeta) talismans.getItemMeta();
        talismansMeta.setOwner("stone");
        List<String> talismansLore = new ArrayList<>();
        talismansMeta.setDisplayName(ChatColor.YELLOW + "Item Enchanting");
        talismansLore.add(ChatColor.GRAY + "As an animus, you can enchant");
        talismansLore.add(ChatColor.GRAY + "item to give the owner");
        talismansLore.add(ChatColor.GRAY + "special buffs!");
        talismansLore.add(ChatColor.GRAY + " ");
        talismansLore.add(ChatColor.YELLOW + "Click to enchant!");
        talismansMeta.setLore(talismansLore);
        talismans.setItemMeta(talismansMeta);

        ItemStack buffs = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta buffsMeta = buffs.getItemMeta();
        List<String> buffsLore = new ArrayList<>();
        buffsMeta.setDisplayName(ChatColor.YELLOW + "Player Enchanting");
        buffsLore.add(ChatColor.GRAY + "As an animus, you can enchant");
        buffsLore.add(ChatColor.GRAY + "players to give them special");
        buffsLore.add(ChatColor.GRAY + "buffs/debuffs!");
        buffsLore.add(ChatColor.GRAY + " ");
        buffsLore.add(ChatColor.YELLOW + "Click to enchant!");
        buffsMeta.setLore(buffsLore);
        buffs.setItemMeta(buffsMeta);

        ItemStack curses = new ItemStack(Material.PUFFERFISH);
        ItemMeta cursesMeta = curses.getItemMeta();
        List<String> cursesLore = new ArrayList<>();
        cursesMeta.setDisplayName(ChatColor.YELLOW + "Animus Curses");
        cursesLore.add(ChatColor.GRAY + "As an animus, you can enchant");
        cursesLore.add(ChatColor.GRAY + "items/players to give them special");
        cursesLore.add(ChatColor.GRAY + "debuffs!");
        cursesLore.add(ChatColor.GRAY + " ");
        cursesLore.add(ChatColor.YELLOW + "Click to enchant!");
        cursesMeta.setLore(cursesLore);
        curses.setItemMeta(cursesMeta);

        ItemStack weather = new ItemStack(Material.WATER_BUCKET);
        ItemMeta weatherMeta = weather.getItemMeta();
        List<String> weatherLore = new ArrayList<>();
        weatherMeta.setDisplayName(ChatColor.YELLOW + "Enchant Weather");
        weatherLore.add(ChatColor.GRAY + "As an animus, you can enchant");
        weatherLore.add(ChatColor.GRAY + "the weather to change the weather");
        weatherLore.add(ChatColor.GRAY + "to your liking!");
        weatherLore.add(ChatColor.GRAY + " ");
        weatherLore.add(ChatColor.YELLOW + "Click to enchant!");
        weatherMeta.setLore(weatherLore);
        weather.setItemMeta(weatherMeta);

        ItemStack extreme = new ItemStack(Material.FIRE_CHARGE);
        ItemMeta extremeMeta = extreme.getItemMeta();
        List<String> extremeLore = new ArrayList<>();
        extremeMeta.setDisplayName(ChatColor.YELLOW + "Extreme Enchanting");
        extremeLore.add(ChatColor.GRAY + "I...wouldn't...try");
        extremeLore.add(ChatColor.GRAY + " ");
        extremeLore.add(ChatColor.RED + "This is your last warning!");
        extremeMeta.setLore(extremeLore);
        extreme.setItemMeta(extremeMeta);

        for (int i = 0; i < 45; ++i){
            enchantingInventory.setItem(i, space1);
        }

        for (int i = 0; i < 9; ++i){
            enchantingInventory.setItem(i, space2);
        }
        enchantingInventory.setItem(9, space2);
        enchantingInventory.setItem(17, space2);
        enchantingInventory.setItem(18, space2);
        enchantingInventory.setItem(26, space2);
        enchantingInventory.setItem(27, space2);
        enchantingInventory.setItem(35, space2);

        for (int i = 36; i < 45; ++i){
            enchantingInventory.setItem(i, space2);
        }

        enchantingInventory.setItem(13, extreme);
        enchantingInventory.setItem(19, weather);
        enchantingInventory.setItem(21, buffs);
        enchantingInventory.setItem(23, talismans);
        enchantingInventory.setItem(25, curses);

        player.openInventory(enchantingInventory);
    }

    public void startStudy(Player player){
        if (!getActivePlayer(player.getName()).upgrade){
            if (Config.getAnimusStudyLevel(player) < 4) {
                player.sendMessage(ChatColor.GREEN + "You have successfully started your study session! It will be completed in: " + (Config.getAnimusStudyLevel(player) + 1) * 20 + " minutes.");
                getActivePlayer(player.getName()).upgrade = true;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.isOnline()) {
                            if (Config.getAnimusStudyLevel(player) + 1 == 4) {
                                player.sendMessage(ChatColor.AQUA + "--------------------");
                                player.sendMessage(ChatColor.AQUA + "   Animus Level up  ");
                                player.sendMessage(ChatColor.AQUA + "   Level " + (Config.getAnimusStudyLevel(player) + 1) + " -> MAX LEVEL");
                                player.sendMessage(ChatColor.AQUA + "--------------------");
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                                getActivePlayer(player.getName()).upgrade = false;
                                Config.setAnimusStudy(player, Config.getAnimusStudyLevel(player) + 1);
                            } else {
                                player.sendMessage(ChatColor.AQUA + "--------------------");
                                player.sendMessage(ChatColor.AQUA + "   Animus Level up  ");
                                player.sendMessage(ChatColor.AQUA + "   Level " + (Config.getAnimusStudyLevel(player) + 1) + " -> " + (Config.getAnimusStudyLevel(player) + 2));
                                player.sendMessage(ChatColor.AQUA + "--------------------");
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                                getActivePlayer(player.getName()).upgrade = false;
                                Config.setAnimusStudy(player, Config.getAnimusStudyLevel(player) + 1);
                            }
                        } else {
                            cancel();
                        }
                    }
                }.runTaskLater(this, (((Config.getAnimusStudyLevel(player)+1)*20)*60)*20);
            }else{
                player.sendMessage(ChatColor.GREEN + "You are already at the max level!");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 10, 1);
            }
        }else{
            player.sendMessage(ChatColor.RED + "You have already started an upgrade!");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
        }
    }

    public void setSkin(Player player, String string) {
        GameProfile gameProfile = ((CraftPlayer) player).getHandle().getProfile();
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        for (Player player2 : Bukkit.getOnlinePlayers()) {
            PlayerConnection connection2 = ((CraftPlayer) player2).getHandle().playerConnection;
            connection2.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) player).getHandle()));
        }

        gameProfile.getProperties().removeAll("textures");

        PropertyMap map = gameProfile.getProperties();

        if (string.equalsIgnoreCase("NightWing")){
            try { Config.setShapeshift(player, "NightWing"); }catch (Exception e){ e.printStackTrace(); }
            player.sendMessage(ChatColor.GREEN + "You have successfully changed your skin to: Night_Wing_1");
            map.put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxNjgwNDAxMDg2MCwKICAicHJvZmlsZUlkIiA6ICJjMGYzYjI3YTUwMDE0YzVhYjIxZDc5ZGRlMTAxZGZlMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJDVUNGTDEzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzg4MDkwZDA3NjExNDI2ZmZkYWE3NjRjNmYwNGE1YzJjNWQ0NmMzMWM3YmU1ODBiY2UxMzg3ZTQwZjcyNmI2NzUiCiAgICB9CiAgfQp9", "Tps7dCx/j6s4DObuqDcvaCIZJi+aXduWyY9Jl6CaqS0pZp7KEHW8b2YfNCctzshuEdRDe2j7W6XCr6kCYht6P5wmhjw371zKVwS0yuBnqoHFRr2T2LWNalg8eqZ5XbN9BQdEwF9ZVuQ3X7J5XO3L8bYd2eS1kaFKoiadr1N4l6bD+QkThFeEcoliT4wpGgFYbRV70qH/5+i2YpIwx9pMOcUP//NAjW+/lULMcW/ysAEC5B2ulJo5EcfMcxMUOBhZdATRPJWNw5q45uE0f5byLYt7sh++dPqn3tFrxlffiLPZ8aac/Km8mF2c4ra966V5wTcFZVZkqxOxqGE1h+eZ1PrhYAF838DPoqpSgS/Fh1ECFlp3ofJAp3u9jl7csUyan1HYLqQTswg5yHqyEn1IBnCMpKvGSLqzuINgzmTuEkWbLlmgrX0AOQH1mMoRLaNjbDRxKEPyDG29YkZrBRkQWdTmrjY+FXkBps6iIsmPe3PyUu7C+T0MTFtTy99t+SB5L4bU5beOav1PtRApn2x9lM4SpANUoJdtVrxQfVp1WjsF+f+gYRdqL2f0qFqk40D8w+osdZOylbDu5oipBrjkwCkoJFbkcK8mhWjfDX+Hoe9eYddv4YIGp+7DvpSxE9EpWTty2/QYAlk539ThSUGi7D+71unhW1vOFbp5rFVKQ8I="));
        }else if (string.equalsIgnoreCase("SkyWing")){
            try { Config.setShapeshift(player, "SkyWing"); }catch (Exception e){ e.printStackTrace(); }
            player.sendMessage(ChatColor.GREEN + "You have successfully changed your skin to: Sky_Wing_1");
            map.put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxMTc1MDI1MzUyOSwKICAicHJvZmlsZUlkIiA6ICI0ZWQ4MjMzNzFhMmU0YmI3YTVlYWJmY2ZmZGE4NDk1NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJGaXJlYnlyZDg4IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2JkMWNjMTdjOGQ4NzM1NjdjYTE4YzgzM2JiM2E4ZDg3ZjBkNWI4NWM0ODVlZDJmYTQ5MjUwM2U0NDg4OWM5NWQiCiAgICB9CiAgfQp9", "VScWE6quw9kwqJAk+HCQkz/smwYQA1wxQbAsvTeyRpWlvKig8d50uyOsLmN/m76JVce1Hsff/p4T0pj9veEYWcoP7qS7JJGLWgCZhvja641JiRBwDb0LCiOwUAW1Sj41PjP5SWvdp+BbAy7WX9JdehhSLIxfXcSpHYpN/Pj6U7/R18gp0vpwz3mvdMF7r+OvIZe8fEc53z3ALe0m/ppjwa+Hw3Su+6QDM0k3qDGp1ef3FpuAqo30Asn5Sf4OQrOI11OE+81nB+inzI+YJfOBrUkDIxtEQCE6Upv/9kHNu7yvZsTBo7j/tOgG+EKThwPGE5jQrQp3AwaqarTlFSThnsDoayawPugwpv6RMNH8AS1zynnn6yBDYu2lcLhYCumKnDiII1I9atDN3HBiM9z/QRE8AxcOkikAHPOFolZTv2OdG9dDOYAObQ4yX6yWEzL6tkN5U7UsSkU0SS6CsOthAoiihdIG76GJb6S0oihdQScVjXRrhfQTM6wLYfkLjmza/ZOnMe6MTJTj2hIo5b932lFOTb0jQo9VhD0gqTV9sfYfKlP8fb/fST5G8ReiHx/dLECZtc4Ix0uVNN6KSBTY3iud0GTi6xO3m4nV7yblOXGuMgh4ZJyhXi4HcDDZ9oYnZfKcpEUlMJxsSK9Y0tQ25VhZ4oR1LSbU4IPk+NSwyyU="));
        }else if (string.equalsIgnoreCase("RainWing")){
            try { Config.setShapeshift(player, "RainWing"); }catch (Exception e){ e.printStackTrace(); }
            player.sendMessage(ChatColor.GREEN + "You have successfully changed your skin to: Rain_Wing_1");
            map.put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxNjgwNDAxODg2MSwKICAicHJvZmlsZUlkIiA6ICIwMGM2Yjk0YTY5YmU0MzY3OTkwOTQxNjFjMjAxOWI3ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJLQUVWRVJZIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzRlNmNiMGE4YmU1NWVhMjZhZWJkMjExOWFkZjYwOTljZDY3YmMzMmRkZmI3YTFlYjVjZjMxOThkNTQ5ODBiMGUiCiAgICB9CiAgfQp9", "a6gOD5TSPr45hcj9NhjLuHvqF9RBdd0PcTtDLdmMrVUhshIrDJD7+xZ982unpv63HbWdjLBUFfV7e3J4WQV03RHmXPZM+Aqa+7WR019lnaiC/N9/kZodFHeloC4FoT2ZJxA2mJkm5JzD/bHOsDmjJkfIvd7VK3Un2h2Z/NQFdN7xaGzxbU14+3CSDwHODDlNM4SO5xadebwpXmS9Ql6p6N+xtuHMl2APtOjo/JHDYfHawLl3T+nlyackkbDJoTcuYyVe9q4P2R7qdImwFxtzTYidL4gLXRdES7WUMTxOMcRypRN3rSbTTP/seZ8/l4hLqhXOQJTAQy8nc8C7cUFgZ0ev/Lj6296nzUX2kf/Zqcn3kPA7eTq7tjQqjnl5bTwgze+D63QXTMqBt9HTOt+H/Fq2//qIUf1TfFyMgsRqULbgzZc5FE3joqL5WdTbAW+MJJo9m8dGGUHuucnaIncXMP54uWUw73Hylscbg7FQWNJuLSj5tawkV293/N5TDvAUGnTsEdrqE6YaFTe/d4ETPBLdXCL/Cfhv3ZOQVrF+NsExVVeZE9slhe3b5PZEFG20iyalTv2dHYXRues/8lXjv8u0qDU7efpP7WXBA00rN1hXOEoS/teIJuELdL9IRvw0CU/dhkJgDpmrw/32dl/0s/FWBh9qAEgNMT2tpfNj0bE="));
        }else if (string.equalsIgnoreCase("SandWing")){
            try { Config.setShapeshift(player, "SandWing"); }catch (Exception e){ e.printStackTrace(); }
            player.sendMessage(ChatColor.GREEN + "You have successfully changed your skin to: Sand_Wing_1");
            map.put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxNjgwNDAyODYxNCwKICAicHJvZmlsZUlkIiA6ICI0NWY3YTJlNjE3ODE0YjJjODAwODM5MmRmN2IzNWY0ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJfSnVzdERvSXQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzFmOGQ3NWNiZDQxZDczMTQzNjBlOGRkZTZiYWI5OWNmZDE5NGM4NWU2Y2M0ODZlMDA0M2I5ODcyZjJhMGVjZCIKICAgIH0KICB9Cn0=", "GAfmsoztbCiI64S4U3oLA/hlSSz5yO5NSEEXfVmBk/NLCAMjAdNEt+KLWv975d3AB26UkfRd7XUgaCq/pYzRj/98ylA9Us86htgdIOi9VwaDDXVAkMWvmue28LIY+rTm1gOONfnXNWBw64vEu9ly7+Zr2i295X8VUZLvJ0LtxIKFUiVg75t0Fk2NiuTWlxgcrkJrqnWFerkS6PFXqG6bEsOKMYMBEwkf901rmQ2s4WjhRxoyMcLkU59oepoMoBpDyC672uwL3JPoOfemrgHhBb5GEfiAlqjZqb0skwSBj72RBkTjtQ4aHMMKBV0vojtt+Gbzfais39cQB7jBvkyspWKt/TKHjmgSjPRls9rTYeW50979oWcYbD0i+28Eq/8jE0fFTavm8IbWe6hlpSJJw8mT3atN1fZek0qqsnWabJgDXPs/T66/CXVdVJW+rnZ56xtazSPSui2veW7F+T/qpQ6mOiDvdhm20zDqiLHOrDl2K2Au+JG+o+ds7aD0wya4vmBGgkD4AGXijnJM75YqvglEBWkkXKpowMk41Gp3cNQWE59veMvcFRQcMV7c4CPneAIEau5/4yHGrGql6K1sgJiJRy6HTkFOQ6ANTcakVB7LrdPp0C32qgoXPhSCJihBJxxwzwM2X/GlfVlw8rQVdgOp5lrNiCMTDX0LFJINdzI="));
        }else if (string.equalsIgnoreCase("SeaWing")){
            player.sendMessage(ChatColor.GREEN + "You have successfully changed your skin to: Sea_Wing_1");
            try { Config.setShapeshift(player, "SeaWing"); }catch (Exception e){ e.printStackTrace(); }
            map.put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxMTc1MDExNDQ1MSwKICAicHJvZmlsZUlkIiA6ICI2MTI4MTA4MjU5M2Q0OGQ2OWIzMmI3YjlkMzIxMGUxMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJEYXJrUmVhcGVyMTMxNSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kOTNkMDEyNTI5ZGE0MmY0ZWQ3Yjg0YmYxMTFlMTY1NjgwYWYzYmYyZjQxMWEwNGZjZThmMTIwZThhM2U2OTA3IgogICAgfQogIH0KfQ==", "WZiZx3Y0+fB2SFuPZ42GWRRcEuSk10iWgqtFmPPb2qzcOj4n0BLL7JIIfob6n31Ii41S9mvFU6RcSt9ICzp4hFxAeGv8zxNNIn6YnZQyxF8R8VqZNP50R5k6r0GXQcMlu+2Ze1Y6ZuMJkhMq1yAZCV7IbJ+yMDYJPAZZGgB6ex8PXt4h9hUrqckj929p5LGDuXqXr6IcLc02ZkOCLjXW61ATohwBSpd757BH+qFu3ep1ShtJ3K6aEpibIngchX1kfghF9grnQOBa73oNEKDucjnYj0Qx9vbRl9vs7sVF95Q4uSbaADLGWSE9s9tBfsZ6+sRAZRyzAzZX4/Mcc3nq/7jtMeTatgsO4+zHoXjkLlHdmJweTwaT2ck6lA2dvRLgh9NJB58cEPo+avWFp2Z9ufRU4vcOorEjdsFlIyEL/047LYJQ4EErEUUCIh1OPhWU2YoZJnNffVbsBFf/lI92eQJyxv00M2XfhIjw0nPe5eo4f3pMVddD0UvwCvA9a1n1a+TsGDrlxu/BqGurY/et4GYvCDvaHsvcptTb/HCS0rSRHdRwbzegxuOtm8CPta1jMg6DG7A+AemHjcKAE7ObfA2dF+2hRx/mKAfEsZq0WaL9JUGPsNNTkMDMx7fdh1/JBvY9UJlc3HY9ZXa/FxLXrPHDVsFqKp6tbSKtbuybm2w="));
        }else if (string.equalsIgnoreCase("MudWing")){
            player.sendMessage(ChatColor.GREEN + "You have successfully changed your skin to: Mud_Wing_1");
            try { Config.setShapeshift(player, "MudWing"); }catch (Exception e){ e.printStackTrace(); }
            map.put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxMTc1MDAxNTU1NCwKICAicHJvZmlsZUlkIiA6ICJiMGQ3MzJmZTAwZjc0MDdlOWU3Zjc0NjMwMWNkOThjYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJPUHBscyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kNjkxMWE5YTBiYTQzZDg4NjBjMzM0NzM3MThiMzNlOTE1ODc0OTY4NjYwOGYxN2U5MGI2YzI0OGVjYTI2YSIKICAgIH0KICB9Cn0=", "mhwCvSO1YSPgVuB5hKkwABihUdahwfSv0BKrl2qjbmNeXp0JEbh8bTX2jGPuSLcfbCDbn2xWRehJPaiUjzLdQ3RYONgogzPr7jLUHBTmT7T8sBfEZGAP1yQRn66RFURCZyXE11Fy960dQKyhSLACjIqII3G2GncQQfRRsddxDVALePPyQZeywwLf9XntwjbB5s8RTM7ecXBQCx6hV7rpeY8eQtNh2Ovqxt5Ir4NCG3xV7uN8A29E3UVKwFl+hMs+d/dQBLyCsrZRmi9K07sOE+t5gg/bYpzKazLDieQkQtOolggB+vke/p21eMkhKNKiYb/fSexHM/c/txbb6u8gu8uBCWqMtuiV2QPeljzggZSx3lgcEBJZbXuk1vECwoLBVmFjVW/HjkRvqvM4VZsxtHFmmA2LjJYDoEukefknRgrWNprvbOEVgoVzhh8JZaFzZfdEWdtQu1RovB7ftf47RZ7d2SVPPskfGZ0oa0Ekiw6aVS6ojFwoROOhhYfa504/Eew3ShE1TJ220osJ5kW1fd0msunh8sO2mFEyytH7qhqwTAowggGhK86ELT49nCB3r5I+ozJhiE3UCEPXfCEifvessKpQXDk3nbgr1IeXuc2t4tejrXxRUSbBQV9yRHEiHwt+G+V+Eu1ggaRyfcOnX7288kmBLw6BM7zExIYtYHw="));
        }else if (string.equalsIgnoreCase("IceWing")){
            player.sendMessage(ChatColor.GREEN + "You have successfully changed your skin to: Ice_Wing_1");
            try { Config.setShapeshift(player, "IceWing"); }catch (Exception e){ e.printStackTrace(); }
            map.put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxNjgwMzk5NDk5OSwKICAicHJvZmlsZUlkIiA6ICI5MzZmMTA3MTEzOGM0YjMyYTg0OGY2NmE5Nzc2NDJhMiIsCiAgInByb2ZpbGVOYW1lIiA6ICIwMDAwMDAwMDAwMDAwMDB4IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzY3OTUxNzdiZGM2ZTI3NGUzNDk1M2I5YzQ2MjM3ZjFjM2FlZTIwMzY0YjUwNTc3YWZlNDdhZGMzNjUxYmJjYjIiCiAgICB9CiAgfQp9", "D8huU+rQIEUF7CdILGMOZTk+53nUQ8hSIAZHZK1Ih2yyTHDKbAxa4Wdja5W8bajoXgaPsN75jIWaL0ve2mbo8MHv6XouSEKxO8DEdm1N+1jooCNwnGJMtW16RvluCwkzu6QL9I4WMxxj6NeUh5U0bpojv5+O9MQnLH2psAAlDrXHTwuQYgtme7T9Au9RpGYM6S7QNHSE1CAR7rG2/FgZyw5Lldx7ovE+0Y2AJUfs3X1Y5vP2SgBnSobdIp4C6Lv504vM+CfIZEo8dp/ruSPQg8r+HGkMGYx7ZykGvUDAOnaSDXR4RTpAgWCZeXdwi9PPAfzfu2nk3C3rKViV82PIa7b/GcoqhCPxo4rOmvoGbel461uVaLB4UQwJIfM2z8WURI8gvKyXba7B03Mu6bO3wvh9UeBgKvKYwYgPIqWGCdALHbMRgBaJpG6CXG/r7kFG02CT8ca4Eu2tG2Ofb3nU3mN/JA7NTz/M6lGdZ5KC+PiBiEy8LFlGPIkB/JJRdJa3rmU9hijAyp0O7l6z5JeN7OpC/YFQRxFNV/YP/4n5w9KMIiHB7215aqxfvOMVb+PDrbJCDRDnSLfzF5ZXX6MkH58LoBTgnZQvVkrhIk11DnUFGW78Kts5XWyqlec2NKJ/qPiAfj61LImGWeY64i3EQphYZOnUwm46we8VkuPIvf0="));
        }else if (string.equalsIgnoreCase("default")){
            player.sendMessage(ChatColor.GREEN + "You have successfully changed your skin to: " + player.getName());
            Player player1 = Bukkit.getPlayer(player.getName());
            CraftPlayer craftPlayer = (CraftPlayer) player1;
            map = craftPlayer.getHandle().getProfile().getProperties();

        }
        for (Player player1 : Bukkit.getOnlinePlayers()) {
            PlayerConnection connection1 = ((CraftPlayer) player1).getHandle().playerConnection;
            connection1.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) player).getHandle()));
        }

        ((CraftPlayer) player).getHandle().server.getPlayerList().moveToWorld(((CraftPlayer) player).getHandle(), ((CraftPlayer) player).getHandle().getWorldServer(), false, player.getLocation(), true);
    }

    public void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            NamespacedKey key = new NamespacedKey(this, getDescription().getName());

            Glow glow = new Glow(key);
            org.bukkit.enchantments.Enchantment.registerEnchantment(glow);
        }
        catch (IllegalArgumentException e){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public void showMana(Player player){
        Random rand = new Random();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                }else if (player.isOnline()){
                    if (Config.isAnimus(player)){
                        int i = rand.nextInt(15);
                        ActivePlayer activePlayer = getActivePlayer(player.getName());
                        activePlayer.animusManaBar.setTitle(ChatColor.BLUE + "Animus Mana: " + Config.getMana(player));
                        if (i == 1 && Config.getMana(player) <= 95){
                            Config.setMana(player, Config.getMana(player) + 5);
                        }

                    }
                }
            }
        }.runTaskTimer(this, 5L, 5L);
    }


    public void loadRecipe() {
        ItemStack talisman = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) talisman.getItemMeta();
        List<String> talismanLore = new ArrayList<>();
        talismanLore.add(ChatColor.GRAY + "An animus dragon can");
        talismanLore.add(ChatColor.GRAY + "enchant generic");
        talismanLore.add(ChatColor.GRAY + "talismans to give");
        talismanLore.add(ChatColor.GRAY + "special buffs!");
        skullMeta.setLore(talismanLore);
        skullMeta.setDisplayName(ChatColor.DARK_GRAY + "Generic Talisman");
        skullMeta.setOwner("stone");
        talisman.setItemMeta(skullMeta);
        setMaxStackSize(CraftItemStack.asNMSCopy(talisman).getItem(), 1);

        ShapedRecipe recipe = new ShapedRecipe(talisman);
        recipe.shape("^%^", "%&%", "^%^");
        recipe.setIngredient('^', Material.STONE);
        recipe.setIngredient('%', Material.IRON_INGOT);
        recipe.setIngredient('&', Material.DIAMOND);

        this.getServer().addRecipe(recipe);

        ItemStack accessoryBag = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta accessoryBagMeta = (SkullMeta) accessoryBag.getItemMeta();
        List<String> accessoryBagLore = new ArrayList<>();
        accessoryBagLore.add(ChatColor.GRAY + "Allows you to access");
        accessoryBagLore.add(ChatColor.GRAY + "you accessory bag using");
        accessoryBagLore.add(ChatColor.GRAY + "just a click!");
        accessoryBagLore.add(ChatColor.GRAY + "");
        accessoryBagLore.add(ChatColor.YELLOW + "Right Click to open!");
        accessoryBagMeta.setLore(accessoryBagLore);
        accessoryBagMeta.setDisplayName(ChatColor.YELLOW + "Accessory Bag");
        accessoryBag.setItemMeta(accessoryBagMeta);
        accessoryBag = IDtoSkull(accessoryBag, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNiM2FjZGMxMWNhNzQ3YmY3MTBlNTlmNGM4ZTliM2Q5NDlmZGQzNjRjNjg2OTgzMWNhODc4ZjA3NjNkMTc4NyJ9fX0=");
        setMaxStackSize(CraftItemStack.asNMSCopy(accessoryBag).getItem(), 1);

        ShapedRecipe recipe2 = new ShapedRecipe(accessoryBag);
        recipe2.shape("^%^", "^&^", "^%^");
        recipe2.setIngredient('^', Material.LEATHER);
        recipe2.setIngredient('%', Material.IRON_INGOT);
        recipe2.setIngredient('&', Material.DIAMOND);

        this.getServer().addRecipe(recipe2);
    }


    /*
    public void playRandomSound(Player sender, Player target, boolean passThrough){
        this.sender = sender;
        this.target = target;
        this.passThrough = passThrough;
        this.soundPlayTicks = 100;

        soundPlayerCountDown = new Countdown(this, this.soundPlayTicks, null, null,
                (t) -> {
                    playSoundTask(sender, target, passThrough);
                });
    }

    private void playSoundTask(Player sender, Player target, boolean passThrough){
        if (soundPlayerCountDown.getTicksPassed() == 0){

        }else if (soundPlayerCountDown.getTicksPassed() == 4 || soundPlayerCountDown.getTicksPassed() == 12){

        }else if (soundPlayerCountDown.getTicksPassed() == 8){

        }
    }
     */


    public void playRandomSound(Player player, Player player2, boolean b){
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 0);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 0);
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                new BukkitRunnable() {
                                                    @Override
                                                    public void run() {
                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);
                                                        new BukkitRunnable() {
                                                            @Override
                                                            public void run() {
                                                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                                new BukkitRunnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 0);
                                                                        new BukkitRunnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                                                new BukkitRunnable() {
                                                                                    @Override
                                                                                    public void run() {
                                                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);
                                                                                        new BukkitRunnable() {
                                                                                            @Override
                                                                                            public void run() {
                                                                                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                                                                new BukkitRunnable() {
                                                                                                    @Override
                                                                                                    public void run() {
                                                                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 0);
                                                                                                        new BukkitRunnable() {
                                                                                                            @Override
                                                                                                            public void run() {
                                                                                                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                                                                                new BukkitRunnable() {
                                                                                                                    @Override
                                                                                                                    public void run() {
                                                                                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);
                                                                                                                        new BukkitRunnable() {
                                                                                                                            @Override
                                                                                                                            public void run() {
                                                                                                                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                                                                                                new BukkitRunnable() {
                                                                                                                                    @Override
                                                                                                                                    public void run() {
                                                                                                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 0);
                                                                                                                                        new BukkitRunnable() {
                                                                                                                                            @Override
                                                                                                                                            public void run() {
                                                                                                                                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                                                                                                                new BukkitRunnable() {
                                                                                                                                                    @Override
                                                                                                                                                    public void run() {
                                                                                                                                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);
                                                                                                                                                        new BukkitRunnable() {
                                                                                                                                                            @Override
                                                                                                                                                            public void run() {
                                                                                                                                                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                                                                                                                                                new BukkitRunnable() {
                                                                                                                                                                    @Override
                                                                                                                                                                    public void run() {
                                                                                                                                                                        if (b){
                                                                                                                                                                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                                                                                                                                                                            player2.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                                                                                                                                                                            player2.sendTitle(ChatColor.AQUA + "" + ChatColor.BOLD + "You're an Animus!", ChatColor.GOLD + player.getName() + " made you an Animus!", 20, 40, 20);
                                                                                                                                                                        }else{
                                                                                                                                                                            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
                                                                                                                                                                            player2.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
                                                                                                                                                                            player.sendMessage(ChatColor.RED + "Unsuccessful");
                                                                                                                                                                            player2.sendMessage(ChatColor.RED + "Unsuccessful");
                                                                                                                                                                        }
                                                                                                                                                                    }
                                                                                                                                                                }.runTaskLater(Main.getInstance(), 24);
                                                                                                                                                            }
                                                                                                                                                        }.runTaskLater(Main.getInstance(), 8);
                                                                                                                                                    }
                                                                                                                                                }.runTaskLater(Main.getInstance(), 8);
                                                                                                                                            }
                                                                                                                                        }.runTaskLater(Main.getInstance(), 8);
                                                                                                                                    }
                                                                                                                                }.runTaskLater(Main.getInstance(), 8);
                                                                                                                            }
                                                                                                                        }.runTaskLater(Main.getInstance(), 8);
                                                                                                                    }
                                                                                                                }.runTaskLater(Main.getInstance(), 8);
                                                                                                            }
                                                                                                        }.runTaskLater(Main.getInstance(), 8);
                                                                                                    }
                                                                                                }.runTaskLater(Main.getInstance(), 8);
                                                                                            }
                                                                                        }.runTaskLater(Main.getInstance(), 8);
                                                                                    }
                                                                                }.runTaskLater(Main.getInstance(), 8);
                                                                            }
                                                                        }.runTaskLater(Main.getInstance(), 8);
                                                                    }
                                                                }.runTaskLater(Main.getInstance(), 6);
                                                            }
                                                        }.runTaskLater(Main.getInstance(), 4);
                                                    }
                                                }.runTaskLater(Main.getInstance(), 4);
                                            }
                                        }.runTaskLater(Main.getInstance(), 4);
                                    }
                                }.runTaskLater(Main.getInstance(), 4);
                            }
                        }.runTaskLater(Main.getInstance(), 4);
                    }
                }.runTaskLater(Main.getInstance(), 4);
            }
        }.runTaskLater(this, 4);
    }

    public void loadNpc(){
        FileConfiguration file = getConfig();
        getConfig().getConfigurationSection("data").getKeys(false).forEach(npc ->{
            Location location = new Location(Bukkit.getWorld(file.getString("data." + npc + ".world"))
                    , file.getDouble("data." + npc + ".x")
                    , file.getDouble("data." + npc + ".y")
                    , file.getDouble("data." + npc + ".z"));
            location.setPitch((float) file.getDouble("data." + npc + ".p"));
            location.setYaw((float) file.getDouble("data." + npc + ".yaw"));

            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), ChatColor.YELLOW + "" + ChatColor.BOLD + "Click");

            gameProfile.getProperties().removeAll("textures");

            PropertyMap map = gameProfile.getProperties();

            map.put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxNzI5OTA4MDc0MCwKICAicHJvZmlsZUlkIiA6ICJiN2ZkYmU2N2NkMDA0NjgzYjlmYTllM2UxNzczODI1NCIsCiAgInByb2ZpbGVOYW1lIiA6ICJDVUNGTDE0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2EwNmU2NzNjMzk0MzA5N2RjYzg4ZTE3MDNhYmIxZWZlZTcyOTZiYzBjYzFhOTU0N2QyMDBkN2YzOGM5YzU5NDAiCiAgICB9CiAgfQp9", "eoH/M+M49Hew7JhBiEGJDXxmFJNqmU8yUIFgtJBcZZdyfVpjZnnR59YGPcyJZg/FBlvKBnV02red1O2OS7JGteKTow7cmmCDlyAyTyCxjqstyRKl9ZsPbVSbxboghTWAVrl4dLZTRrFe0/rHcMVXngkdU0z1talRVBg2ylr0faO7j8vx76MF+Lclq8W0ZCmLDagGZ5VADeXu3hmMkAXIFmzZKglJAH4i7ncxOP2Z1kwtYu+pD13XJBjDI1UDCSnm758fEFMNiUmg7JZ8RrVeeiyOFwdYE/U32g2scplC5eq7WbV6vVpXCArI4V4ZSUPELccO/aVgu8eD1SGnuLvYygq1TmDe+7nwDMTJXvihy2k9MDpbuS7SmpHWNFUfJPLeLX11uGdGM5/M+1QPxvvQ3wwPVVzJ2fCDUm8NnuQ4EpGB1Rn2uNkSsK1oQ5pMj4SIp9dHAs26QtlXr631QBLkru5QVVjfg7mGdPCFBRFWRWn3o9JOIdsWtHjr1Zi+K9QOwnjLZm3pQZLlVhAOW4vzsaMdnlqqTPFYycaJC9+HqU7ghlDlvU7VfiEmcYRP6MrKBzqEfeqILASiHadCLq6icCMFGUd63j2ohyCm6ZSqMYrvvcedhH0PhcV9Sn90+3x1F6IlSToCDz3aFlKPE9+APhW8HChfd+d7hSa3WT851/k="));


            NPC.loadNpc(location, gameProfile);
        });
    }

    public void openTalismanEnchantingInventory(Player player){
        InventoryAnimus.openTalismanEnchantingInventory(player, simpleEnchantingInventory);
    }

    public void openWeatherEnchantingGui(Player player){
        InventoryAnimus.openWeatherEnchantingGui(player, weatherEnchantingInventory);
    }

    public void openPlayerEnchantsGui(Player player){
        InventoryAnimus.openPlayerEnchantsGui(player, playerEnchantingInventory);
    }

    public void openAnimusCursingGui(Player player){
        InventoryAnimus.openAnimusCursingGui(player, cursesEnchantingInventory);
    }


//    public void openTalismanEnchantingInventory(Player player){
//        simpleEnchantingInventory.clear();
//        ItemStack space1 = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
//        ItemMeta itemMeta = space1.getItemMeta();
//        itemMeta.setDisplayName(" ");
//        space1.setItemMeta(itemMeta);
//
//        ItemStack space2 = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
//        ItemMeta itemMeta2 = space2.getItemMeta();
//        itemMeta2.setDisplayName(" ");
//        space2.setItemMeta(itemMeta2);
//
//        for (int i = 0; i < 9; ++i){
//            simpleEnchantingInventory.setItem(i, space2);
//        }
//
//        simpleEnchantingInventory.setItem(9, space2);
//        simpleEnchantingInventory.setItem(17, space2);
//        simpleEnchantingInventory.setItem(18, space2);
//        simpleEnchantingInventory.setItem(26, space2);
//        simpleEnchantingInventory.setItem(27, space2);
//        simpleEnchantingInventory.setItem(35, space2);
//
//        for (int i = 36; i < 45; ++i){
//            simpleEnchantingInventory.setItem(i, space2);
//        }
//
//        ItemStack hand = new ItemStack(Material.PLAYER_HEAD);
//        List<String> lore = new ArrayList<>();
//        SkullMeta handItemMeta = (SkullMeta) hand.getItemMeta();
//        handItemMeta.setOwner("LapisBlock");
//        handItemMeta.setDisplayName(ChatColor.AQUA + "Running Talisman" + ChatColor.AQUA + " (10 Mana per lvl)");
//        lore.add(ChatColor.GRAY + "Putting this talisman in");
//        lore.add(ChatColor.GRAY + "an active talisman slot will");
//        lore.add(ChatColor.GRAY + "grant you +X% speed!");
//        lore.add(ChatColor.GRAY + " ");
//        lore.add(ChatColor.YELLOW + "Click to Enchant!");
//        handItemMeta.setLore(lore);
//        hand.setItemMeta(handItemMeta);
//        simpleEnchantingInventory.addItem(hand);
//
//        lore.clear();
//        lore.add(ChatColor.GRAY + "Putting this talisman in");
//        lore.add(ChatColor.GRAY + "an active talisman slot will");
//        lore.add(ChatColor.GRAY + "grant you +X armor points!");
//        lore.add(ChatColor.GRAY + " ");
//        lore.add(ChatColor.YELLOW + "Click to Enchant!");
//        handItemMeta.setLore(lore);
//        handItemMeta.setDisplayName(ChatColor.DARK_GRAY + "Shield Talisman" + ChatColor.AQUA + " (10 Mana per lvl)");
//        hand.setItemMeta(handItemMeta);
//        hand = IDtoSkull(hand, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE1Yzg2ODY0MTFkNDQ2YzkwYzE5MWM5M2Y4MGI5ZmZiMWNkMjQ3YWExMmEyMjZmODk3OTk4MWFkNDM4OGJlZSJ9fX0=");
//        simpleEnchantingInventory.addItem(hand);
//
//        lore.clear();
//        lore.add(ChatColor.GRAY + "Putting this talisman in");
//        lore.add(ChatColor.GRAY + "an active talisman slot will");
//        lore.add(ChatColor.GRAY + "grant you +X% attack boost!");
//        lore.add(ChatColor.GRAY + " ");
//        lore.add(ChatColor.YELLOW + "Click to Enchant!");
//        handItemMeta.setLore(lore);
//        handItemMeta.setDisplayName(ChatColor.DARK_RED + "Strength Talisman" + ChatColor.AQUA + " (10 Mana per lvl)");
//        hand.setItemMeta(handItemMeta);
//        hand = IDtoSkull(hand, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2IwMzNiMmMyZDNjZjU4OTEzM2FiNzMwZWQxYThiNDQzMTNkNjI5OTU0ODBjM2EwZGFjMzI4ZDUzN2UyN2Q3ZiJ9fX0=");
//        simpleEnchantingInventory.addItem(hand);
//
//        lore.clear();
//        lore.add(ChatColor.GRAY + "Putting this talisman in");
//        lore.add(ChatColor.GRAY + "an active talisman slot will");
//        lore.add(ChatColor.GRAY + "grant you permanent slow-falling!");
//        lore.add(ChatColor.GRAY + " ");
//        lore.add(ChatColor.YELLOW + "Click to Enchant!");
//        handItemMeta.setLore(lore);
//        handItemMeta.setDisplayName(ChatColor.WHITE + "Gliding Talisman" + ChatColor.AQUA + " (5 Mana)");
//        hand.setItemMeta(handItemMeta);
//        hand = IDtoSkull(hand, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzZmMzNiMjk3YTk4MWI1MjdiZWMzOTMxNjg0MDFkOGEyZWNhZGViOWYxNjAzYmE1ZTI3NmY0MmQ2NDQ3NTExNiJ9fX0=");
//        simpleEnchantingInventory.addItem(hand);
//
//        lore.clear();
//        lore.add(ChatColor.GRAY + "Putting this talisman in");
//        lore.add(ChatColor.GRAY + "an active talisman slot will");
//        lore.add(ChatColor.GRAY + "grant you permanent invisibility!");
//        lore.add(ChatColor.GRAY + " ");
//        lore.add(ChatColor.YELLOW + "Click to Enchant!");
//        handItemMeta.setLore(lore);
//        handItemMeta.setDisplayName(ChatColor.DARK_GRAY + "Stealth Talisman" + ChatColor.AQUA + " (10 Mana)");
//        hand.setItemMeta(handItemMeta);
//        hand = IDtoSkull(hand, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2IxZTAxN2I1ODQxYjk4NTc3YTJiOGVkOWJmMDIzZDNiZjE0OWQ3ZWY2Y2RkY2VmY2FkZjdiNGIyN2MzMWIzMSJ9fX0=");
//        simpleEnchantingInventory.addItem(hand);
//
//        lore.clear();
//        lore.add(ChatColor.GRAY + "Putting this talisman in");
//        lore.add(ChatColor.GRAY + "an active talisman slot will");
//        lore.add(ChatColor.GRAY + "grant you permanent glowing effect!");
//        lore.add(ChatColor.GRAY + " ");
//        lore.add(ChatColor.YELLOW + "Click to Enchant!");
//        handItemMeta.setLore(lore);
//        handItemMeta.setDisplayName(ChatColor.YELLOW + "Glowing Talisman" + ChatColor.AQUA + " (5 Mana)");
//        hand.setItemMeta(handItemMeta);
//        hand = IDtoSkull(hand, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Q5ZDE5NWYwOTJlNDM1MDViNTQ5OWU3MzJkY2RiOWU4NTIwNjlkNWFkMzVjMTE0MzJjOTkwYWZjZmU2NDAzNyJ9fX0=");
//        simpleEnchantingInventory.addItem(hand);
//
//        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
//        ItemMeta bookMeta = book.getItemMeta();
//        List<String> bookLore = new ArrayList<>();
//        bookMeta.setDisplayName(ChatColor.AQUA + "Sharpness VI" + ChatColor.AQUA + " (15 Mana)");
//        bookLore.add(ChatColor.GRAY + "Adds Sharpness 6 to a");
//        bookLore.add(ChatColor.GRAY + "sword or enchanted");
//        bookLore.add(ChatColor.GRAY + "book!");
//        bookLore.add(ChatColor.GRAY + " ");
//        bookLore.add(ChatColor.YELLOW + "Click to Enchant!");
//        bookMeta.setLore(bookLore);
//        book.setItemMeta(bookMeta);
//        simpleEnchantingInventory.addItem(book);
//
//        ItemStack protBook = new ItemStack(Material.ENCHANTED_BOOK);
//        ItemMeta protBookMeta = protBook.getItemMeta();
//        List<String> protBookLore = new ArrayList<>();
//        protBookMeta.setDisplayName(ChatColor.AQUA + "Protection V" + ChatColor.AQUA + " (15 Mana)");
//        protBookLore.add(ChatColor.GRAY + "Adds Protection 5 to a");
//        protBookLore.add(ChatColor.GRAY + "armor piece or enchanted book!");
//        protBookLore.add(ChatColor.GRAY + " ");
//        protBookLore.add(ChatColor.YELLOW + "Click to Enchant!");
//        protBookMeta.setLore(protBookLore);
//        protBook.setItemMeta(protBookMeta);
//        simpleEnchantingInventory.addItem(protBook);
//
//        ItemStack axeOfFlames = new ItemStack(Material.FIRE_CHARGE);
//        ItemMeta axeOfFlamesMeta = axeOfFlames.getItemMeta();
//        List<String> axeOfFlamesLore = new ArrayList<>();
//        axeOfFlamesMeta.setDisplayName(ChatColor.RED + "Axe of Flames" + ChatColor.AQUA + " (10 Mana per lvl)");
//        axeOfFlamesLore.add(ChatColor.GRAY + "Adds fire aspect to an");
//        axeOfFlamesLore.add(ChatColor.GRAY + "axe!");
//        axeOfFlamesLore.add(ChatColor.GRAY + " ");
//        axeOfFlamesLore.add(ChatColor.YELLOW + "Click to Enchant!");
//        axeOfFlamesMeta.setLore(axeOfFlamesLore);
//        axeOfFlames.setItemMeta(axeOfFlamesMeta);
//        simpleEnchantingInventory.addItem(axeOfFlames);
//
//        ItemStack heartScale = new ItemStack(Material.NETHERITE_CHESTPLATE);
//        ItemMeta heartScaleMeta = heartScale.getItemMeta();
//        List<String> heartScaleLore = new ArrayList<>();
//        heartScaleMeta.setDisplayName(ChatColor.BLUE + "Heart Scales" + ChatColor.AQUA + " (10 Mana per lvl)");
//        heartScaleLore.add(ChatColor.GRAY + "Adds the Heart Scales enchant");
//        heartScaleLore.add(ChatColor.GRAY + "to any armor");
//        heartScaleLore.add(ChatColor.GRAY + " ");
//        heartScaleLore.add(ChatColor.YELLOW + "Click to Enchant!");
//        heartScaleMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
//        heartScaleMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
//        heartScaleMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
//        heartScaleMeta.setLore(heartScaleLore);
//        heartScale.setItemMeta(heartScaleMeta);
//        simpleEnchantingInventory.addItem(heartScale);
//        simpleEnchantingInventory.setItem(36, ItemCreator.goBack());
//
//        player.openInventory(simpleEnchantingInventory);
//    }
//
//    public void openWeatherEnchantingGui(Player player){
//        weatherEnchantingInventory.clear();
//        ItemStack space1 = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
//        ItemMeta itemMeta1 = space1.getItemMeta();
//        itemMeta1.setDisplayName(" ");
//        space1.setItemMeta(itemMeta1);
//
//        ItemStack space2 = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
//        ItemMeta itemMeta2 = space2.getItemMeta();
//        itemMeta2.setDisplayName(" ");
//        space2.setItemMeta(itemMeta2);
//
//        for (int i = 0; i < 9; ++i){
//            weatherEnchantingInventory.setItem(i, space2);
//        }
//
//        weatherEnchantingInventory.setItem(9, space2);
//        weatherEnchantingInventory.setItem(17, space2);
//        weatherEnchantingInventory.setItem(18, space2);
//        weatherEnchantingInventory.setItem(26, space2);
//        weatherEnchantingInventory.setItem(27, space2);
//        weatherEnchantingInventory.setItem(35, space2);
//
//        for (int i = 36; i < 45; ++i){
//            weatherEnchantingInventory.setItem(i, space2);
//        }
//
//        ItemStack item = new ItemStack(Material.SUNFLOWER);
//        ItemMeta itemMeta = item.getItemMeta();
//        List<String> lore = new ArrayList<>();
//        itemMeta.setDisplayName(ChatColor.GOLD + "Part Clouds" + ChatColor.AQUA + " (5 Mana)");
//        lore.add(ChatColor.GRAY + "Clears the current weather");
//        lore.add(ChatColor.GRAY + "and makes it a sunny day!");
//        lore.add(ChatColor.GRAY + " ");
//        lore.add(ChatColor.YELLOW + "Click to enchant!");
//        itemMeta.setLore(lore);
//        item.setItemMeta(itemMeta);
//        weatherEnchantingInventory.addItem(item);
//
//        item = new ItemStack(Material.WATER_BUCKET);
//        itemMeta.setDisplayName(ChatColor.AQUA + "Rain" + ChatColor.AQUA + " (5 Mana)");
//        lore.clear();
//        lore.add(ChatColor.GRAY + "Makes the current weather");
//        lore.add(ChatColor.GRAY + "into rain!");
//        lore.add(ChatColor.GRAY + " ");
//        lore.add(ChatColor.YELLOW + "Click to enchant!");
//        itemMeta.setLore(lore);
//        item.setItemMeta(itemMeta);
//        weatherEnchantingInventory.addItem(item);
//
//        item = new ItemStack(Material.COD_BUCKET);
//        itemMeta.setDisplayName(ChatColor.BLUE + "Thunder Storm" + ChatColor.AQUA + " (5 Mana)");
//        lore.clear();
//        lore.add(ChatColor.GRAY + "Makes the current weather");
//        lore.add(ChatColor.GRAY + "into a thunder storm!");
//        lore.add(ChatColor.GRAY + " ");
//        lore.add(ChatColor.YELLOW + "Click to enchant!");
//        itemMeta.setLore(lore);
//        item.setItemMeta(itemMeta);
//        weatherEnchantingInventory.addItem(item);
//
//        item = new ItemStack(Material.BLAZE_ROD);
//        itemMeta.setDisplayName(ChatColor.YELLOW + "Lightning" + ChatColor.AQUA + " (15 Mana)");
//        lore.clear();
//        lore.add(ChatColor.GRAY + "Summons a lightning bolt");
//        lore.add(ChatColor.GRAY + "in front of you!");
//        lore.add(ChatColor.GRAY + " ");
//        lore.add(ChatColor.YELLOW + "Click to enchant!");
//        itemMeta.setLore(lore);
//        item.setItemMeta(itemMeta);
//        weatherEnchantingInventory.addItem(item);
//
//        item.setType(Material.WATER_BUCKET);
//        weatherEnchantingInventory.setItem(36, ItemCreator.goBack());
//
//        player.openInventory(weatherEnchantingInventory);
//    }
//
//    public void openAnimusCursingGui(Player player){
//        cursesEnchantingInventory.clear();
//        ItemStack space1 = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
//        ItemMeta itemMeta1 = space1.getItemMeta();
//        itemMeta1.setDisplayName(" ");
//        space1.setItemMeta(itemMeta1);
//
//        ItemStack space2 = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
//        ItemMeta itemMeta2 = space2.getItemMeta();
//        itemMeta2.setDisplayName(" ");
//        space2.setItemMeta(itemMeta2);
//
//        for (int i = 0; i < 9; ++i){
//            cursesEnchantingInventory.setItem(i, space2);
//        }
//
//        cursesEnchantingInventory.setItem(9, space2);
//        cursesEnchantingInventory.setItem(17, space2);
//        cursesEnchantingInventory.setItem(18, space2);
//        cursesEnchantingInventory.setItem(26, space2);
//        cursesEnchantingInventory.setItem(27, space2);
//        cursesEnchantingInventory.setItem(35, space2);
//
//        for (int i = 36; i < 45; ++i){
//            cursesEnchantingInventory.setItem(i, space2);
//        }
//
//        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
//        ItemMeta itemMeta = item.getItemMeta();
//        List<String> lore = new ArrayList<>();
//        itemMeta.setDisplayName(ChatColor.RED + "Curse of Binding" + ChatColor.AQUA + " (10 Mana)");
//        lore.add(ChatColor.GRAY + "Adds Curse of Binding to an");
//        lore.add(ChatColor.GRAY + "item.");
//        lore.add(ChatColor.GRAY + " ");
//        lore.add(ChatColor.YELLOW + "Click to enchant!");
//        itemMeta.setLore(lore);
//        item.setItemMeta(itemMeta);
//        cursesEnchantingInventory.addItem(item);
//
//        lore.clear();
//        itemMeta.setDisplayName(ChatColor.RED + "Curse of Vanishing" + ChatColor.AQUA + " (10 Mana)");
//        lore.add(ChatColor.GRAY + "Adds Curse of Vanishing to an");
//        lore.add(ChatColor.GRAY + "item.");
//        lore.add(ChatColor.GRAY + " ");
//        lore.add(ChatColor.YELLOW + "Click to enchant!");
//        itemMeta.setLore(lore);
//        item.setItemMeta(itemMeta);
//        cursesEnchantingInventory.addItem(item);
//
//        item = new ItemStack(Material.PUFFERFISH);
//        itemMeta = item.getItemMeta();
//        lore = new ArrayList<>();
//        itemMeta.setDisplayName(ChatColor.DARK_GRAY + "Curse of Sickness" + ChatColor.AQUA + " (15 Mana)");
//        lore.add(ChatColor.GRAY + "The named player gets poison");
//        lore.add(ChatColor.GRAY + "for 5 seconds");
//        lore.add(ChatColor.GRAY + " ");
//        lore.add(ChatColor.YELLOW + "Click to Enchant!");
//        itemMeta.setLore(lore);
//        item.setItemMeta(itemMeta);
//        cursesEnchantingInventory.addItem(item);
//
//        lore.clear();
//        item.setType(Material.SPIDER_EYE);
//        itemMeta.setDisplayName(ChatColor.GRAY + "Curse of Degradation" + ChatColor.AQUA + " (40 Mana)");
//        lore.add(ChatColor.GRAY + "The named player gets permanent");
//        lore.add(ChatColor.GRAY + "-3 hearts");
//        lore.add(ChatColor.GRAY + " ");
//        lore.add(ChatColor.YELLOW + "Click to Enchant!");
//        itemMeta.setLore(lore);
//        item.setItemMeta(itemMeta);
//        cursesEnchantingInventory.addItem(item);
//
//        lore.clear();
//        item.setType(Material.ROTTEN_FLESH);
//        itemMeta.setDisplayName(ChatColor.GRAY + "Curse of Undead" + ChatColor.AQUA + " (30 Mana)");
//        lore.add(ChatColor.GRAY + "The named player catches fire");
//        lore.add(ChatColor.GRAY + "while under sunlight.");
//        lore.add(ChatColor.GRAY + " ");
//        lore.add(ChatColor.YELLOW + "Click to Enchant!");
//        itemMeta.setLore(lore);
//        item.setItemMeta(itemMeta);
//        cursesEnchantingInventory.addItem(item);
//
//        lore.clear();
//        item.setType(Material.GRAY_STAINED_GLASS);
//        itemMeta.setDisplayName(ChatColor.GRAY + "Curse of Blindness" + ChatColor.AQUA + " (15 Mana)");
//        lore.add(ChatColor.GRAY + "The named player gets the");
//        lore.add(ChatColor.GRAY + "blindness effect.");
//        lore.add(ChatColor.GRAY + " ");
//        lore.add(ChatColor.YELLOW + "Click to Enchant!");
//        itemMeta.setLore(lore);
//        item.setItemMeta(itemMeta);
//        cursesEnchantingInventory.addItem(item);
//        cursesEnchantingInventory.setItem(36, ItemCreator.goBack());
//
//        player.openInventory(cursesEnchantingInventory);
//    }
//
//    public void openPlayerEnchantsGui(Player player){
//        playerEnchantingInventory.clear();
//        ItemStack space1 = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
//        ItemMeta itemMeta2 = space1.getItemMeta();
//        itemMeta2.setDisplayName(" ");
//        space1.setItemMeta(itemMeta2);
//
//        ItemStack space2 = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
//        ItemMeta itemMeta3 = space2.getItemMeta();
//        itemMeta3.setDisplayName(" ");
//        space2.setItemMeta(itemMeta3);
//
//        for (int i = 0; i < 9; ++i){
//            playerEnchantingInventory.setItem(i, space2);
//        }
//
//        playerEnchantingInventory.setItem(9, space2);
//        playerEnchantingInventory.setItem(17, space2);
//        playerEnchantingInventory.setItem(18, space2);
//        playerEnchantingInventory.setItem(26, space2);
//        playerEnchantingInventory.setItem(27, space2);
//        playerEnchantingInventory.setItem(35, space2);
//
//        for (int i = 36; i < 45; ++i){
//            playerEnchantingInventory.setItem(i, space2);
//        }
//
//        ItemStack item = new ItemStack(Material.GOLDEN_APPLE);
//        ItemMeta itemMeta = item.getItemMeta();
//        List<String> itemLore = new ArrayList<>();
//        itemMeta.setDisplayName(ChatColor.RED + "Health Boost" + ChatColor.AQUA + " (2 Mana per lvl)");
//        itemLore.add(ChatColor.GRAY + "Grants the named player");
//        itemLore.add(ChatColor.GRAY + "+X hearts!");
//        itemLore.add(ChatColor.GRAY + " ");
//        itemLore.add(ChatColor.YELLOW + "Click to Enchant!");
//        itemMeta.setLore(itemLore);
//        item.setItemMeta(itemMeta);
//        playerEnchantingInventory.addItem(item);
//
//        itemLore.clear();
//        item.setType(Material.FEATHER);
//        itemMeta.setDisplayName(ChatColor.WHITE + "Swift Talons" + ChatColor.AQUA + " (10 Mana per lvl)");
//        itemLore.add(ChatColor.GRAY + "Grants the named player");
//        itemLore.add(ChatColor.GRAY + "speed and haste at lvl 4+!");
//        itemLore.add(ChatColor.GRAY + " ");
//        itemLore.add(ChatColor.YELLOW + "Click to Enchant!");
//        itemMeta.setLore(itemLore);
//        item.setItemMeta(itemMeta);
//        playerEnchantingInventory.addItem(item);
//
//        itemLore.clear();
//        item.setType(Material.COBBLESTONE);
//        itemMeta.setDisplayName(ChatColor.DARK_GRAY + "Stone Scales" + ChatColor.AQUA + " (10 Mana per lvl)");
//        itemLore.add(ChatColor.GRAY + "Grants the named player");
//        itemLore.add(ChatColor.GRAY + "resistance..at a cost.");
//        itemLore.add(ChatColor.GRAY + " ");
//        itemLore.add(ChatColor.YELLOW + "Click to Enchant!");
//        itemMeta.setLore(itemLore);
//        item.setItemMeta(itemMeta);
//        playerEnchantingInventory.addItem(item);
//
//        itemLore.clear();
//        item.setType(Material.RABBIT_FOOT);
//        itemMeta.setDisplayName(ChatColor.GREEN + "Leaping Talons" + ChatColor.AQUA + " (2 Mana per lvl)");
//        itemLore.add(ChatColor.GRAY + "Grants the named player");
//        itemLore.add(ChatColor.GRAY + "jump boost!");
//        itemLore.add(ChatColor.GRAY + " ");
//        itemLore.add(ChatColor.YELLOW + "Click to Enchant!");
//        itemMeta.setLore(itemLore);
//        item.setItemMeta(itemMeta);
//        playerEnchantingInventory.addItem(item);
//
//        itemLore.clear();
//        item.setType(Material.LAVA_BUCKET);
//        itemMeta.setDisplayName(ChatColor.GOLD + "Magma Scales" + ChatColor.AQUA + " (10 Mana per lvl)");
//        itemLore.add(ChatColor.GRAY + "Grants the named player");
//        itemLore.add(ChatColor.GRAY + "fire resistance..at a cost.");
//        itemLore.add(ChatColor.GRAY + "Also gives permanent Fire");
//        itemLore.add(ChatColor.GRAY + "Aspect to named player's");
//        itemLore.add(ChatColor.GRAY + "items at lvl 2+.");
//        itemLore.add(ChatColor.GRAY + " ");
//        itemLore.add(ChatColor.YELLOW + "Click to Enchant!");
//        itemMeta.setLore(itemLore);
//        item.setItemMeta(itemMeta);
//        playerEnchantingInventory.addItem(item);
//
//        itemLore.clear();
//        item.setType(Material.GOLDEN_CARROT);
//        itemMeta.setDisplayName(ChatColor.RED + "Rejuvenation" + ChatColor.AQUA + " (5 Mana per lvl)");
//        itemLore.add(ChatColor.GRAY + "Grants the named player");
//        itemLore.add(ChatColor.GRAY + "instant health!");
//        itemLore.add(ChatColor.GRAY + " ");
//        itemLore.add(ChatColor.YELLOW + "Click to Enchant!");
//        itemMeta.setLore(itemLore);
//        item.setItemMeta(itemMeta);
//        playerEnchantingInventory.addItem(item);
//
//        itemLore.clear();
//        item.setType(Material.MILK_BUCKET);
//        itemMeta.setDisplayName(ChatColor.YELLOW + "Cure Disease" + ChatColor.AQUA + " (10 Mana)");
//        itemLore.add(ChatColor.GRAY + "Removes Wither, Poison");
//        itemLore.add(ChatColor.GRAY + "and Hunger from the");
//        itemLore.add(ChatColor.GRAY + "named player!");
//        itemLore.add(ChatColor.GRAY + " ");
//        itemLore.add(ChatColor.YELLOW + "Click to Enchant!");
//        itemMeta.setLore(itemLore);
//        item.setItemMeta(itemMeta);
//        playerEnchantingInventory.addItem(item);
//
//        itemLore.clear();
//        item.setType(Material.DIAMOND_ORE);
//        itemMeta.setDisplayName(ChatColor.AQUA + "Gentle Talons" + ChatColor.AQUA + " (20 Mana)");
//        itemLore.add(ChatColor.GRAY + "Gives the named player");
//        itemLore.add(ChatColor.GRAY + "infinite Silk Touch.");
//        itemLore.add(ChatColor.GRAY + " ");
//        itemLore.add(ChatColor.YELLOW + "Click to Enchant!");
//        itemMeta.setLore(itemLore);
//        item.setItemMeta(itemMeta);
//        playerEnchantingInventory.addItem(item);
//
//        itemLore.clear();
//        item.setType(Material.SLIME_SPAWN_EGG);
//        itemMeta.setDisplayName(ChatColor.GREEN + "Species Shift" + ChatColor.AQUA + " (15 Mana)");
//        itemLore.add(ChatColor.GRAY + "Change your skin to");
//        itemLore.add(ChatColor.GRAY + "another tribe!");
//        itemLore.add(ChatColor.GRAY + " ");
//        itemLore.add(ChatColor.YELLOW + "Click to Enchant!");
//        itemMeta.setLore(itemLore);
//        item.setItemMeta(itemMeta);
//        playerEnchantingInventory.addItem(item);
//
//        itemLore.clear();
//        item.setType(Material.FIRE_CHARGE);
//        itemMeta.setDisplayName(ChatColor.GOLD + ""  + ChatColor.MAGIC + "111 " + ChatColor.RED + "Animus Magic" + ChatColor.GOLD + ""  + ChatColor.MAGIC + " 111" + ChatColor.AQUA + " (80 Mana)");
//        itemLore.add(ChatColor.GRAY + "Enchant player to be an animus.");
//        itemLore.add(ChatColor.GRAY + "0.1% chance of actually");
//        itemLore.add(ChatColor.GRAY + "working!");
//        itemLore.add(ChatColor.GRAY + " ");
//        itemLore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "I want to run away.");
//        itemLore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "I want to hide forever.");
//        itemLore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "But he had no choice.");
//        itemLore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Anemone was his responsibility.");
//        itemLore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Anemone was his biggest mistake.");
//        itemLore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + " ");
//        itemLore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "-Talons of Power, Page 239");
//        itemLore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + " ");
//        itemLore.add(ChatColor.YELLOW + "Click to Enchant!");
//        itemMeta.setLore(itemLore);
//        item.setItemMeta(itemMeta);
//        playerEnchantingInventory.addItem(item);
//
//        playerEnchantingInventory.setItem(36, ItemCreator.goBack());
//
//        player.openInventory(playerEnchantingInventory);
//    }





    /*

        player.sendMessage("Started");
        ActivePlayer activePlayer = getActivePlayer(player.getName());
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        GameProfile gameProfile = entityPlayer.getProfile();
        activePlayer.gameProfile = gameProfile;
        PropertyMap map = gameProfile.getProperties();

        PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;

        playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) player).getHandle()));
        gameProfile.getProperties().removeAll("textures");

        if (string.equalsIgnoreCase("NightWing")){
            player.sendMessage("Night");
            map.put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxNjQ2NDEwNDk1NCwKICAicHJvZmlsZUlkIiA6ICJiZDQ5ODdkY2NjODc0OGY1OGY4NDJmODZkZjNjNWJiMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJMaWdodF9EcmFnb25lciIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jNDBlYzY3MzIyNTI3MGFmYTUwMzBiZWJjOTczZGEwNDY1MGJiNGQ1OTA3MTQ1NWVlNzljODFjNmQyZWQ0YzBhIgogICAgfQogIH0KfQ==", "TNwq56mPCIZAVDmQ7AnkBh+vGdgcYTAC+Uvz6UfpTHZWhgP8eBqm8rF5KaE1eD1cfqaUcZ1CPu/TdtfsYt7aIO5CIkG/SS05mGZbM/n1POxifEvjXTbwJLNdPehQ6mVElWLkaaZhLuepthiH6jrXcMUQgC2INNQnybgliU/G+sKXvnOzu1IbCQLYBLpaf2/8fb4Q34SndL3vzUz08IGPbcA7tVvz3EfeSj7oWxr3e2/7kJgCNQePb1480dyV0V21hhCTNAWnNnqVLllRXjc/zWxF50uuu03cQwSuVoYz6OQEdKPWXZN5CRPg7qZB4Hi/eYmfIa6zxOsZ58mkLo09zzyWf/aq32FPtDR3RsNT5ePZFR9hFrVgEjYuUPByi09YkK76w70V7gjg2vjB1xAMC+5lmNG7M51+wDw1K4aV/P77Qy9D94RLSeVXmH9A7OjAA28Gx0b3zcpA4vKZWmol79ZKly300Gur2icdB3qLJo/5j22+D1m+4hyc1KDFXOSEy/Pd7WGN6PAVrSzqC2Lzroy3e+idulRb/LiRSJsD8A2HCzLiHN9BuzwPTb3LDvO3D2Fu7QLZ4thT3skbyfwze2uDw8seuzZNNBa5ai9UV5gYGdWP/GIf5hs8ZqZoCO7bPcSQxO1ywki2yiq93YcRThpnEe09OZ9+gNoexuWdtGw="));
        }else if (string.equalsIgnoreCase("SkyWing")){
            player.sendMessage("Sky");
            map.put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxNjQ1Nzg0NjQ5MSwKICAicHJvZmlsZUlkIiA6ICIzZjM4YmViZGYwMWQ0MjNkYWI4MjczZjUwNGFiNGEyNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJjazM0Nzk0MjM1NzUzNzMxIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzg1ZDU1ZTAyZDIyYmI3ZmYxY2Q4MzhmOWRkNGI3MmEzMDljMmYwNzQzYWM1NDAzZTNhNmUxZGUyNDhmMzE4NjciCiAgICB9CiAgfQp9", "fIPP58lUhIH8PQSWgp2UKyVLqeOYLWPVmhej+rbc/CBdiWBqsKMJRdbGnaU7v96KvXBuZIMeod2rDeVsMkaeNOqrYoWftLC1qqPFSokvM9EBPpAZLw/Azv1VsIMCqxww3bJY6rQzje0k3GVCnaPEGJf0q7uJfmHv+VhucycmUE08YnxeuoqtY/BElGh3HF1hyzIJeMvPV/7uJrEuxB5q8eQYfAIwBAZ8J2plLEo5PqQhcexCTpvnY7auwFxsBDUkKe9+dl5yUujO4j1NtWTn60LDHpZMlfJe9wztvQx8vm7p5//91iGltyw9Qwk9UM3D1GDgWBdNwKQWxwjt6JIIOwkRZCrZnsAU/soZMLaKTfY+huk+pbDbScizaMu7xoGLrjMLE2BUD5n+bBFDKnrBlWJ2J/PWfRiwEqdw0cXXhX2mnTysN1lDIrdbSjg5pNKTQO8qeZQnoIOgFRf26WY4BXUAL/BjHjji0MKYml19ewLSTxnkSh9g6jArB4BRAQnAyhMG0AADttQqIxiAxkHZ+zo+ZbxFphumGZonArapzZXblMQRlqegIoncRBbTAGsVdficIyNUwbJZT/dMEPXg4sb69Mvjd/Rq7eXXEu8lrUCXkw0ITmWYL7Py3W6euJe0OeP3Aro0XFDHlTo3VVjkHOoqL/ty8MczIQsiSDY562g="));
        }else if (string.equalsIgnoreCase("RainWing")){
            player.sendMessage("Rain");
            map.put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxMjA4ODcyMDc5MSwKICAicHJvZmlsZUlkIiA6ICJjZGM5MzQ0NDAzODM0ZDdkYmRmOWUyMmVjZmM5MzBiZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJSYXdMb2JzdGVycyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kYWM4MGU5MmQ4MDE3MzA3YjU2ZGRkOWNjMjEzYzA1YTBiYTk3ODM5MTc3ZDg0NDNlZDQ2NGFmMmRiOTIxMDRiIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=", "KSmOePYhIsSZwa8F6noLP7VGMDk5flND5c2E2YolBkeeEXjbiLnfHch8Wupz00/9xiKld2m8D9t8wP/ZT8T/Ywasr/aXIMTicXV4vEkmnTumPGTf00dBYifzCxMjimZ1TIQaeU+gsY1oZpR+lLxYG0HAql2J988yLBjg8uqZS/SgthpiP61OR7vE/0nI4FgNj4ekvRbMySqcFGlcOSzeDYyyMdaoi2Y4HIT2PlNnSP0+WZdqhDQBbTJF4zppFoaAj+27jBCA3tgmSDw+KD8jBAH1MIF9Av+ZFjnwkGrAX1tgOxGG1StPfYXn5NzxQVcvCNXZqM3tCPsekZvygSIETwVR+kcqOzdtSa3mF48SkDmjTCsBk83f1q9rgQdzdxC0rbuj1QWmqVgM1retYkt8RdKbdJL0dFtH1DrGHH2UtPYOqUbzdp8i77iUeij8Pz17hqSuZYO1hzB39FmnzrTTWqzaV3Zwea9rVc2Ou2EJ2jmQeMNHsx/0AHpYWlwxGUK2D1GtSmseAoKMGmp41kCpQ3rUxCuvq5FwpUI2pvg0EW6TtB21vVHfr39FUU8RqXzqfX4VXnRceFtpsFDyuVwz4V636bdbSJY0NsrrEZ7DKen/gIu1ofSO31+SqzDMmGPhbYzsW1pj6gl7f5EwWKCJpwMRTOW1roz+HpAQTZlJ7v4="));
        }else if (string.equalsIgnoreCase("SandWing")){
            player.sendMessage("Sand");
            map.put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxNjQ2MzYzODYzMiwKICAicHJvZmlsZUlkIiA6ICI5MWZlMTk2ODdjOTA0NjU2YWExZmMwNTk4NmRkM2ZlNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJoaGphYnJpcyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hZmM0NWFhMzhlODVhOTQxNzhlOGQ3YTNlZjIzOTlhYzVkNjcxYzIxZGU3YmI2MjFkYzYxZTNhZGE4NGMwZjc0IgogICAgfQogIH0KfQ==", "evdKA+Gjf2a6030pf5jOud7H2ICQ6drAdrDJMcb0gjOkEXUTSiU5fU+AHt4UpMmZq+7Xlgm+d/x39xh4DX4GuHMKw3TdaorFrQKeU6oM8FqtaGeDI36Iawpt1HO1lrc/vn9WJZQamc28ejuuMGpPseQzAyOwpvRoIQv48l3cusKrvxzuShQdS0Y3o/vk2WTAyGGe87o4DU9fAOei49ZG+NTFXleFNHD5qidP8IXyykwOiFNz1V9M5uHPO3hBOwTHiwtf2KueUgd4ur8axLv2s+vDefPIiHR9RHs/0lXUNL4en3IWcWi0K+Dd/c7PpYBO6dq4iK8iqK9hUxLvxCBMqsmK9kt5F8Y7cg03rMAc9nNaVW8v8X/euLaCCO+vZdyd9DNuN6xYCYVzBlLa/20BqwldkZmHm/Dgd4oOvet8RYifwuDnpAD+rr8clP3KmzSqFFuJlZXU4YDII0WS6c+XGLOqlBo8ic9gG6v1uFrlk/OBduGVMZOp6pulZE5Y65kU0/JHpX/JUgRT9R1GChnMrZw4vdQl64qB7Rp8rRCqAvgrn3CkcooKEltdlVilpbKGBaxorGQKjBaP1lbCIKZG/H/4Z/afbZeaCMzuOoPmGnnJgpTcivMo219SnAdlizvYJU/FLmls3xbb3oTWbJY+RetPj4ddVfqGVOSXEnBkqeA="));
        }else if (string.equalsIgnoreCase("SeaWing")){
            player.sendMessage("Sea");
            map.put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxNjQ1ODUyNDAyMSwKICAicHJvZmlsZUlkIiA6ICI4MmM2MDZjNWM2NTI0Yjc5OGI5MWExMmQzYTYxNjk3NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJOb3ROb3RvcmlvdXNOZW1vIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2JhYzRmYjU1ZGEwYTNhNzE3MjYwMjliNGVhMDZkMzIzNzU4M2E1YTRiMGEyNzRlZDc3MWRjYTQ1N2ExMjkwNmMiCiAgICB9CiAgfQp9", "fbgrz9U7L7iofK2CGR6fmPrEZu26WCBAnpNey8o51UjHKEwt9f8FKHSZyHPk4dik27QsPZmxlu+SydJhHv4QIlnCfrl1GpWmGS0N9lD5DIFFeafaObzOx88YnIu2PKtOfGrmq5j11o7+E5KwW2ok4T3RxO+17uAjOzWEu7FvN50vLPk4PtxLv7nT9QDkTocukZSzn6Q4cOM0ifiqXJc8+iJ5Pq9EejuXgWyPmdbnjkszNKhuz1pHoWGfLH9/kt9Mg+jzqE+bT2zwgfhPMZQIkczaxv2kK1QmBVhscNer7clTTVJwMTT4Y6/vmX08kuxMUsf+uR4FktWcgVsFeMlZ3KPaXO+TM1l07vWgPIfCeCD+/bkJfo4yTZyhl6LfKQqt94AXAr4ANCwTo/G34qzy/IBoZdYFlaUBlaf22hwItpywcD7t03pBRSYG3DczychIQ8Upn2rAm+FI87AfrWwMuH2/t6wMfjSvms5nNOnddwW1dE93IRSg+bGX3fAy+6P9wLhuS5nM6zjb/UovMwnM1S1WLbTLOQ+Lr5zIhiqEUR36z22rbvtUBNIAs3FrjC7S3/x2GoriT+cfycvtwBvOse4XThXEu5aXCo2Nd7MyPAkNrR7ZbTbAMW6yMU+FMs4pR0ZbogqTFjjVXJo2H2dVWNMiQGXYHANm1/U/gpQ0z2M="));
        }else if (string.equalsIgnoreCase("MudWing")){
            player.sendMessage("Mud");
            map.put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxNjQ1ODcwNDQxMywKICAicHJvZmlsZUlkIiA6ICI5MzZmMTA3MTEzOGM0YjMyYTg0OGY2NmE5Nzc2NDJhMiIsCiAgInByb2ZpbGVOYW1lIiA6ICIwMDAwMDAwMDAwMDAwMDB4IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2M4NDU4NzU4ODg4OTZhYjZiNmU1ZjI5ZDY2YWM5ZWY2YjQxZjIyN2UxMjY4NWRlNGNiMTM0OWUzMGMwZTM1YzgiCiAgICB9CiAgfQp9", "JWb9PKWKhfF0yt3DHt3ZT9QEo37jYijrjeUOmygF/ppNFh4jnJVXMDLNWviQdZNcmk6zzjUgSbFUrA6bi3lqsTpvwf9ZScpTj7e8w+AGBfLYVcyyoORwi/evUSKIFdvHrr7oPfAe7G+FDaIiez4wwvBLpjGQOW23xsmiyHTXg9cJPhSbJRDO7cAYq+Ut+To/FbwaGek1ZYuDmxV70PJlPPjv3pExnBYaDQCX6O8qwubyqU7989cgEUtOakSg0yNogtt8RaW55q2dyRyErT5H+Oif9ziSCoJcu+SnvixPfeMxhaLSchKiepzHqve6R8CJMipAVKAmsPKF75hlDmekoyKl0vpoairoxr6thjELzobj5xXcgybiX/OoE9hUY8WsXG27Nu/i3BbLy5A9umhy3fmpUch+PSShU5Dz2rY6ZYGxbpFfe41ycTTO3s63Hz+zsydPhdytvEno3EDSOh3kqO5rCY6kW9ueLu4LrgVbrgkAYNpYKtvDkUCLP3JL+WG6IeX9benj3cFxo4gn0s0azCycWcx8xfduy2hOLPSa+qhPO7Hd133Npvu0gw64G8DrgZtuvLLtaRhrrBZGfwri+0K0/lycwTN69a0AeQVvNw72kPBj2FCrqrp3uXQL7+zPXPKzYqct2qmRNIgB+GrkRHszoRjIKjNkwXXUwiRLNuk="));
        }else if (string.equalsIgnoreCase("IceWing")){
            player.sendMessage("Ice");
            map.put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxNjQ2NDEwNDk1NCwKICAicHJvZmlsZUlkIiA6ICJiZDQ5ODdkY2NjODc0OGY1OGY4NDJmODZkZjNjNWJiMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJMaWdodF9EcmFnb25lciIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jNDBlYzY3MzIyNTI3MGFmYTUwMzBiZWJjOTczZGEwNDY1MGJiNGQ1OTA3MTQ1NWVlNzljODFjNmQyZWQ0YzBhIgogICAgfQogIH0KfQ==", "TNwq56mPCIZAVDmQ7AnkBh+vGdgcYTAC+Uvz6UfpTHZWhgP8eBqm8rF5KaE1eD1cfqaUcZ1CPu/TdtfsYt7aIO5CIkG/SS05mGZbM/n1POxifEvjXTbwJLNdPehQ6mVElWLkaaZhLuepthiH6jrXcMUQgC2INNQnybgliU/G+sKXvnOzu1IbCQLYBLpaf2/8fb4Q34SndL3vzUz08IGPbcA7tVvz3EfeSj7oWxr3e2/7kJgCNQePb1480dyV0V21hhCTNAWnNnqVLllRXjc/zWxF50uuu03cQwSuVoYz6OQEdKPWXZN5CRPg7qZB4Hi/eYmfIa6zxOsZ58mkLo09zzyWf/aq32FPtDR3RsNT5ePZFR9hFrVgEjYuUPByi09YkK76w70V7gjg2vjB1xAMC+5lmNG7M51+wDw1K4aV/P77Qy9D94RLSeVXmH9A7OjAA28Gx0b3zcpA4vKZWmol79ZKly300Gur2icdB3qLJo/5j22+D1m+4hyc1KDFXOSEy/Pd7WGN6PAVrSzqC2Lzroy3e+idulRb/LiRSJsD8A2HCzLiHN9BuzwPTb3LDvO3D2Fu7QLZ4thT3skbyfwze2uDw8seuzZNNBa5ai9UV5gYGdWP/GIf5hs8ZqZoCO7bPcSQxO1ywki2yiq93YcRThpnEe09OZ9+gNoexuWdtGw="));
        }

        playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) player).getHandle()));
        player.sendMessage("Finished");




    public ItemStack IDtoSkull(ItemStack skull, String url){
        if (url == null || url.isEmpty())
            return skull;
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        profileField.setAccessible(true);
        try {
            profileField.set(skullMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        skull.setItemMeta(skullMeta);
        return skull;
    }
    */

}
