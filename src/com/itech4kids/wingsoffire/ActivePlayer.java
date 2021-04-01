package com.itech4kids.wingsoffire;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class ActivePlayer {

    public Inventory accessoryBag;
    public Player player;
    public ArrayList<ItemStack> activeAccessories;
    public int speedMultiplier;
    public int strengthMultiplier;
    public String pendingCmd;
    public String pendingPlayerCmd1;
    public String pendingPlayerCmd2;
    public String pendingPlayerName;
    public boolean needsToSelectNumber;
    public GameProfile gameProfile;
    public boolean upgrade;

    public ActivePlayer(Player player) throws IOException {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        this.player = player;
        activeAccessories = new ArrayList<>();
        speedMultiplier = 0;
        strengthMultiplier = 0;
        pendingCmd = " ";
        pendingPlayerCmd1 = " ";
        pendingPlayerCmd2 = " ";
        pendingPlayerName = "";
        needsToSelectNumber = true;
        gameProfile = entityPlayer.getProfile();
        upgrade = false;

        accessoryBag = Bukkit.createInventory(null, 54, "Accessories");
        createAccessoryBag();
        for (ItemStack itemStack : activeAccessories) {
            Config.addActiveAccessories(player, itemStack);
        }
    }

    public void createAccessoryBag() {
        ItemStack space1 = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = space1.getItemMeta();
        itemMeta.setDisplayName(" ");
        space1.setItemMeta(itemMeta);

        accessoryBag.setItem(44, space1);
        accessoryBag.setItem(43, space1);
        accessoryBag.setItem(42, space1);
        accessoryBag.setItem(41, space1);
        accessoryBag.setItem(40, space1);
        accessoryBag.setItem(39, space1);
        accessoryBag.setItem(38, space1);
        accessoryBag.setItem(37, space1);
        accessoryBag.setItem(36, space1);

        accessoryBag.setItem(9, space1);
        accessoryBag.setItem(10, space1);
        accessoryBag.setItem(11, space1);
        accessoryBag.setItem(12, space1);
        accessoryBag.setItem(13, space1);
        accessoryBag.setItem(14, space1);
        accessoryBag.setItem(15, space1);
        accessoryBag.setItem(16, space1);
        accessoryBag.setItem(17, space1);

        accessoryBag.setItem(49, space1);
        accessoryBag.setItem(50, space1);
        accessoryBag.setItem(51, space1);
        accessoryBag.setItem(52, space1);
        accessoryBag.setItem(53, space1);

        accessoryBag.setItem(0, getItem(1, "Active Accessory"));
        accessoryBag.setItem(1, getItem(2, "Active Accessory"));
        accessoryBag.setItem(2, getItem(3, "Active Accessory"));
        accessoryBag.setItem(3, getItem(4, "Active Accessory"));
        accessoryBag.setItem(4, getItem(5, "Active Accessory"));
        accessoryBag.setItem(5, getItem(6, "Active Accessory"));
        accessoryBag.setItem(6, getItem(7, "Active Accessory"));
        accessoryBag.setItem(7, getItem(8, "Active Accessory"));
        accessoryBag.setItem(8, getItem(9, "Active Accessory"));

        accessoryBag.setItem(45, getItem(1, "Active Talisman"));
        accessoryBag.setItem(46, getItem(2, "Active Talisman"));
        accessoryBag.setItem(47, getItem(3, "Active Talisman"));
        accessoryBag.setItem(48, getItem(4, "Active Talisman"));

        for (int i = 0; i < 9; ++i) {
            if (Config.getActiveAccessories(player).size() == 1){
                accessoryBag.setItem(0, Config.getActiveAccessories(player).get(0));
            }else if (Config.getActiveAccessories(player).size() == 2){
                accessoryBag.setItem(0, Config.getActiveAccessories(player).get(0));
                accessoryBag.setItem(1, Config.getActiveAccessories(player).get(1));
            }else if (Config.getActiveAccessories(player).size() == 3){
                accessoryBag.setItem(0, Config.getActiveAccessories(player).get(0));
                accessoryBag.setItem(1, Config.getActiveAccessories(player).get(1));
                accessoryBag.setItem(2, Config.getActiveAccessories(player).get(2));
            }else if (Config.getActiveAccessories(player).size() == 4){
                accessoryBag.setItem(0, Config.getActiveAccessories(player).get(0));
                accessoryBag.setItem(1, Config.getActiveAccessories(player).get(1));
                accessoryBag.setItem(2, Config.getActiveAccessories(player).get(2));
                accessoryBag.setItem(3, Config.getActiveAccessories(player).get(3));
            }else if (Config.getActiveAccessories(player).size() == 5){
                accessoryBag.setItem(0, Config.getActiveAccessories(player).get(0));
                accessoryBag.setItem(1, Config.getActiveAccessories(player).get(1));
                accessoryBag.setItem(2, Config.getActiveAccessories(player).get(2));
                accessoryBag.setItem(3, Config.getActiveAccessories(player).get(3));
                accessoryBag.setItem(4, Config.getActiveAccessories(player).get(4));
            }else if (Config.getActiveAccessories(player).size() == 6){
                accessoryBag.setItem(0, Config.getActiveAccessories(player).get(0));
                accessoryBag.setItem(1, Config.getActiveAccessories(player).get(1));
                accessoryBag.setItem(2, Config.getActiveAccessories(player).get(2));
                accessoryBag.setItem(3, Config.getActiveAccessories(player).get(3));
                accessoryBag.setItem(4, Config.getActiveAccessories(player).get(4));
                accessoryBag.setItem(5, Config.getActiveAccessories(player).get(5));
            }else if (Config.getActiveAccessories(player).size() == 7){
                accessoryBag.setItem(0, Config.getActiveAccessories(player).get(0));
                accessoryBag.setItem(1, Config.getActiveAccessories(player).get(1));
                accessoryBag.setItem(2, Config.getActiveAccessories(player).get(2));
                accessoryBag.setItem(3, Config.getActiveAccessories(player).get(3));
                accessoryBag.setItem(4, Config.getActiveAccessories(player).get(4));
                accessoryBag.setItem(5, Config.getActiveAccessories(player).get(5));
                accessoryBag.setItem(6, Config.getActiveAccessories(player).get(6));
            }else if (Config.getActiveAccessories(player).size() == 8){
                accessoryBag.setItem(0, Config.getActiveAccessories(player).get(0));
                accessoryBag.setItem(1, Config.getActiveAccessories(player).get(1));
                accessoryBag.setItem(2, Config.getActiveAccessories(player).get(2));
                accessoryBag.setItem(3, Config.getActiveAccessories(player).get(3));
                accessoryBag.setItem(4, Config.getActiveAccessories(player).get(4));
                accessoryBag.setItem(5, Config.getActiveAccessories(player).get(5));
                accessoryBag.setItem(6, Config.getActiveAccessories(player).get(6));
                accessoryBag.setItem(7, Config.getActiveAccessories(player).get(7));
            }else if (Config.getActiveAccessories(player).size() == 9){
                accessoryBag.setItem(0, Config.getActiveAccessories(player).get(0));
                accessoryBag.setItem(1, Config.getActiveAccessories(player).get(1));
                accessoryBag.setItem(2, Config.getActiveAccessories(player).get(2));
                accessoryBag.setItem(3, Config.getActiveAccessories(player).get(3));
                accessoryBag.setItem(4, Config.getActiveAccessories(player).get(4));
                accessoryBag.setItem(5, Config.getActiveAccessories(player).get(5));
                accessoryBag.setItem(6, Config.getActiveAccessories(player).get(6));
                accessoryBag.setItem(7, Config.getActiveAccessories(player).get(7));
                accessoryBag.setItem(8, Config.getActiveAccessories(player).get(8));
            }
        }

        for (int i = 45; i < 49; ++i) {
            if (Config.getActiveTalismans(player).size() == 1){
                accessoryBag.setItem(45, Config.getActiveTalismans(player).get(0));
            }else if (Config.getActiveTalismans(player).size() == 2){
                accessoryBag.setItem(45, Config.getActiveTalismans(player).get(0));
                accessoryBag.setItem(46, Config.getActiveTalismans(player).get(1));
            }else if (Config.getActiveTalismans(player).size() == 3){
                accessoryBag.setItem(45, Config.getActiveTalismans(player).get(0));
                accessoryBag.setItem(46, Config.getActiveTalismans(player).get(1));
                accessoryBag.setItem(47, Config.getActiveTalismans(player).get(2));
            }else if (Config.getActiveTalismans(player).size() == 4){
                accessoryBag.setItem(45, Config.getActiveTalismans(player).get(0));
                accessoryBag.setItem(46, Config.getActiveTalismans(player).get(1));
                accessoryBag.setItem(47, Config.getActiveTalismans(player).get(2));
                accessoryBag.setItem(48, Config.getActiveTalismans(player).get(3));
            }
        }


        for (ItemStack itemStack : Config.getAccessories(player)){
            accessoryBag.addItem(itemStack);
        }
    }
    public ItemStack getItem(int i, String str){
        ItemStack itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + str + " slot #" + i);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
        /*
        accessoryBag.setItem(35, space1);
        accessoryBag.setItem(34, space1);
        accessoryBag.setItem(33, space1);
        accessoryBag.setItem(32, space1);
        accessoryBag.setItem(31, space1);
        accessoryBag.setItem(30, space1);
        accessoryBag.setItem(29, space1);
        accessoryBag.setItem(28, space1);
        accessoryBag.setItem(27, space1);


        accessoryBag.setItem(17, space1);
        accessoryBag.setItem(16, space1);
        accessoryBag.setItem(15, space1);
        accessoryBag.setItem(14, space1);
        accessoryBag.setItem(13, space1);
        accessoryBag.setItem(12, space1);
        accessoryBag.setItem(11, space1);
        accessoryBag.setItem(10, space1);
        accessoryBag.setItem(9, space1);
        accessoryBag.setItem(8, space1);

        if (Config.getMinerals(player).size() == 0){
            accessoryBag.setItem(18, getItem(1, "Mineral"));
            accessoryBag.setItem(19, getItem(2, "Mineral"));
            accessoryBag.setItem(20, getItem(3, "Mineral"));
            accessoryBag.setItem(21, getItem(4, "Mineral"));
            accessoryBag.setItem(22, getItem(5, "Mineral"));
            accessoryBag.setItem(23, getItem(6, "Mineral"));
            accessoryBag.setItem(24, getItem(7, "Mineral"));
            accessoryBag.setItem(25, getItem(8, "Mineral"));
            accessoryBag.setItem(26, getItem(9, "Mineral"));
        }else if (Config.getMinerals(player).size() == 1){
            accessoryBag.setItem(18, Config.getMinerals(player).get(0));
            accessoryBag.setItem(19, getItem(2, "Mineral"));
            accessoryBag.setItem(20, getItem(3, "Mineral"));
            accessoryBag.setItem(21, getItem(4, "Mineral"));
            accessoryBag.setItem(22, getItem(5, "Mineral"));
            accessoryBag.setItem(23, getItem(6, "Mineral"));
            accessoryBag.setItem(24, getItem(7, "Mineral"));
            accessoryBag.setItem(25, getItem(8, "Mineral"));
            accessoryBag.setItem(26, getItem(9, "Mineral"));
        }else if (Config.getMinerals(player).size() == 2){
            accessoryBag.setItem(18, Config.getMinerals(player).get(0));
            accessoryBag.setItem(19, Config.getMinerals(player).get(1));
            accessoryBag.setItem(20, getItem(3, "Mineral"));
            accessoryBag.setItem(21, getItem(4, "Mineral"));
            accessoryBag.setItem(22, getItem(5, "Mineral"));
            accessoryBag.setItem(23, getItem(6, "Mineral"));
            accessoryBag.setItem(24, getItem(7, "Mineral"));
            accessoryBag.setItem(25, getItem(8, "Mineral"));
            accessoryBag.setItem(26, getItem(9, "Mineral"));
        }else if (Config.getMinerals(player).size() == 3){
            accessoryBag.setItem(18, Config.getMinerals(player).get(0));
            accessoryBag.setItem(19, Config.getMinerals(player).get(1));
            accessoryBag.setItem(20, Config.getMinerals(player).get(2));
            accessoryBag.setItem(21, getItem(4, "Mineral"));
            accessoryBag.setItem(22, getItem(5, "Mineral"));
            accessoryBag.setItem(23, getItem(6, "Mineral"));
            accessoryBag.setItem(24, getItem(7, "Mineral"));
            accessoryBag.setItem(25, getItem(8, "Mineral"));
            accessoryBag.setItem(26, getItem(9, "Mineral"));
        }else if (Config.getMinerals(player).size() == 4){
            accessoryBag.setItem(18, Config.getMinerals(player).get(0));
            accessoryBag.setItem(19, Config.getMinerals(player).get(1));
            accessoryBag.setItem(20, Config.getMinerals(player).get(2));
            accessoryBag.setItem(21, Config.getMinerals(player).get(3));
            accessoryBag.setItem(22, getItem(5, "Mineral"));
            accessoryBag.setItem(23, getItem(6, "Mineral"));
            accessoryBag.setItem(24, getItem(7, "Mineral"));
            accessoryBag.setItem(25, getItem(8, "Mineral"));
            accessoryBag.setItem(26, getItem(9, "Mineral"));
        }else if (Config.getMinerals(player).size() == 5){
            accessoryBag.setItem(18, Config.getMinerals(player).get(0));
            accessoryBag.setItem(19, Config.getMinerals(player).get(1));
            accessoryBag.setItem(20, Config.getMinerals(player).get(2));
            accessoryBag.setItem(21, Config.getMinerals(player).get(3));
            accessoryBag.setItem(22, Config.getMinerals(player).get(4));
            accessoryBag.setItem(23, getItem(6, "Mineral"));
            accessoryBag.setItem(24, getItem(7, "Mineral"));
            accessoryBag.setItem(25, getItem(8, "Mineral"));
            accessoryBag.setItem(26, getItem(9, "Mineral"));
        }else if (Config.getMinerals(player).size() == 6){
            accessoryBag.setItem(18, Config.getMinerals(player).get(0));
            accessoryBag.setItem(19, Config.getMinerals(player).get(1));
            accessoryBag.setItem(20, Config.getMinerals(player).get(2));
            accessoryBag.setItem(21, Config.getMinerals(player).get(3));
            accessoryBag.setItem(22, Config.getMinerals(player).get(4));
            accessoryBag.setItem(23, Config.getMinerals(player).get(5));
            accessoryBag.setItem(24, getItem(7, "Mineral"));
            accessoryBag.setItem(25, getItem(8, "Mineral"));
            accessoryBag.setItem(26, getItem(9, "Mineral"));
        }else if (Config.getMinerals(player).size() == 7){
            accessoryBag.setItem(18, Config.getMinerals(player).get(0));
            accessoryBag.setItem(19, Config.getMinerals(player).get(1));
            accessoryBag.setItem(20, Config.getMinerals(player).get(2));
            accessoryBag.setItem(21, Config.getMinerals(player).get(3));
            accessoryBag.setItem(22, Config.getMinerals(player).get(4));
            accessoryBag.setItem(23, Config.getMinerals(player).get(5));
            accessoryBag.setItem(24, Config.getMinerals(player).get(6));
            accessoryBag.setItem(25, getItem(8, "Mineral"));
            accessoryBag.setItem(26, getItem(9, "Mineral"));
        }else if (Config.getMinerals(player).size() == 8){
            accessoryBag.setItem(18, Config.getMinerals(player).get(0));
            accessoryBag.setItem(19, Config.getMinerals(player).get(1));
            accessoryBag.setItem(20, Config.getMinerals(player).get(2));
            accessoryBag.setItem(21, Config.getMinerals(player).get(3));
            accessoryBag.setItem(22, Config.getMinerals(player).get(4));
            accessoryBag.setItem(23, Config.getMinerals(player).get(5));
            accessoryBag.setItem(24, Config.getMinerals(player).get(6));
            accessoryBag.setItem(25, Config.getMinerals(player).get(7));
            accessoryBag.setItem(26, getItem(9, "Mineral"));
        }else if (Config.getMinerals(player).size() == 9){
            accessoryBag.setItem(18, Config.getMinerals(player).get(0));
            accessoryBag.setItem(19, Config.getMinerals(player).get(1));
            accessoryBag.setItem(20, Config.getMinerals(player).get(2));
            accessoryBag.setItem(21, Config.getMinerals(player).get(3));
            accessoryBag.setItem(22, Config.getMinerals(player).get(4));
            accessoryBag.setItem(23, Config.getMinerals(player).get(5));
            accessoryBag.setItem(24, Config.getMinerals(player).get(6));
            accessoryBag.setItem(25, Config.getMinerals(player).get(7));
            accessoryBag.setItem(26, Config.getMinerals(player).get(8));
        }
        if (Config.getHelmets(player).size() == 0){
            accessoryBag.setItem(0, getItem(1, "Helmet"));
            accessoryBag.setItem(5, getItem(2, "Helmet"));
        }else if (Config.getHelmets(player).size() == 1){
            accessoryBag.setItem(0, Config.getHelmets(player).get(0));
            accessoryBag.setItem(5, getItem(2, "Helmet"));
        }else if (Config.getHelmets(player).size() == 2){
            accessoryBag.setItem(0, Config.getHelmets(player).get(0));
            accessoryBag.setItem(5, Config.getHelmets(player).get(1));
        }

        if (Config.getChestplates(player).size() == 0){
            accessoryBag.setItem(1, getItem(1, "Chest"));
            accessoryBag.setItem(6, getItem(2, "Chest"));
        }else if (Config.getChestplates(player).size() == 1){
            accessoryBag.setItem(1, Config.getChestplates(player).get(0));
            accessoryBag.setItem(6, getItem(2, "Chest"));
        }else if (Config.getChestplates(player).size() == 2){
            accessoryBag.setItem(1, Config.getChestplates(player).get(0));
            accessoryBag.setItem(6, Config.getChestplates(player).get(1));
        }

        if (Config.getLeggings(player).size() == 0){
            accessoryBag.setItem(2, getItem(1, "Leggings"));
            accessoryBag.setItem(7, getItem(2, "Leggings"));
        }else if (Config.getLeggings(player).size() == 1){
            accessoryBag.setItem(2, Config.getLeggings(player).get(0));
            accessoryBag.setItem(7, getItem(2, "Leggings"));
        }else if (Config.getLeggings(player).size() == 2){
            accessoryBag.setItem(2, Config.getLeggings(player).get(0));
            accessoryBag.setItem(7, Config.getLeggings(player).get(1));
        }

        if (Config.getBoots(player).size() == 0){
            accessoryBag.setItem(3, getItem(1, "Boots"));
            accessoryBag.setItem(8, getItem(2, "Boots"));
        }else if (Config.getBoots(player).size() == 1){
            accessoryBag.setItem(3, Config.getBoots(player).get(0));
            accessoryBag.setItem(8, getItem(2, "Boots"));
        }else if (Config.getBoots(player).size() == 2){
            accessoryBag.setItem(3, Config.getBoots(player).get(0));
            accessoryBag.setItem(8, Config.getBoots(player).get(1));
        }
    }
         */
}
