package com.wingsoffireserver.wingsoffire;

import com.wingsoffireserver.wingsoffire.ArmorEvent.PlayerArmorEquipEvent;
import com.wingsoffireserver.wingsoffire.ArmorEvent.PlayerArmorUnequipEvent;
import net.minecraft.server.v1_16_R3.ItemArmor;
import net.minecraft.server.v1_16_R3.ItemSkullPlayer;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.bukkit.Material.*;

public class AccessoryBagListener implements Listener {

    private Main main;
    private List<Integer> accessorySlots;
    private List<Integer> talismanSlots;

    public AccessoryBagListener(Main main){
        this.main = main;

        initSlots();
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ActivePlayer activePlayer = main.getActivePlayer(player.getName());
        Inventory inv = e.getClickedInventory();
        ItemStack cursor = e.getCursor();
        ItemStack clicked = e.getCurrentItem();
        if (Objects.equals(e.getClickedInventory(), activePlayer.accessoryBag)) {
            if (e.getClick().isShiftClick() || e.getClick().isKeyboardClick()){
                e.setCancelled(true);
                return;
            }
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getItemMeta().hasDisplayName()) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(" ")) {
                        e.setCancelled(true);
                    } else {
                        if (e.getCurrentItem().getItemMeta().hasDisplayName()) {
                            String itemName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase());
                            if (accessorySlots.contains(e.getSlot())) {
                                e.setCancelled(true);
                                if (itemName.startsWith("active accessory slot")) {
                                    if (e.getCursor() != null && !e.getCursor().getType().equals(AIR)) {
                                        if ((CraftItemStack.asNMSCopy(cursor).getItem() instanceof ItemSkullPlayer) || (CraftItemStack.asNMSCopy(cursor).getItem() instanceof ItemArmor)) {
                                            buttonSound(player);
                                            Config.addActiveAccessories(player, cursor);
                                            inv.setItem(e.getSlot(), cursor);
                                            sendMessage(player, "&aSuccessfully added " + cursor.getItemMeta().getDisplayName() + " &ato your active accessories!");
                                            player.setItemOnCursor(null);
                                        } else {
                                            if (cursor == null || cursor.getType().equals(AIR)){
                                                playErrorSound(player);
                                                sendMessage(player, "&cYou need to be holding something!");
                                            }else{
                                                playErrorSound(player);
                                                sendMessage(player, "&cYou can only put &laccessory and armor&r&c items in this slot!");
                                            }
                                        }
                                    } else {
                                        e.setCancelled(true);
                                    }
                                }else{
                                    if (e.getCursor() == null || e.getCursor().getType().equals(AIR)){
                                        buttonSound(player);
                                        Config.removeActiveAccessories(player, clicked);
                                        player.setItemOnCursor(clicked);
                                        inv.setItem(e.getSlot(), getActiveAccessorySlot(e));
                                        sendMessage(player, "&aSuccessfully removed " + cursor.getItemMeta().getDisplayName() + " &afrom your active accessories!");
                                    }else{
                                        playErrorSound(player);
                                        sendMessage(player, "&cYou cannot have an item in your cursor!");
                                    }
                                }
                            } else if (talismanSlots.contains(e.getSlot())) {
                                e.setCancelled(true);
                                if (itemName.startsWith("active talisman slot")) {
                                    if (e.getCursor() != null && !e.getCursor().getType().equals(AIR)) {
                                        if ((CraftItemStack.asNMSCopy(cursor).getItem() instanceof ItemSkullPlayer) && (ChatColor.stripColor(cursor.getItemMeta().getDisplayName().toLowerCase()).contains("talisman"))) {
                                            buttonSound(player);
                                            Config.addActiveTalismans(player, cursor);
                                            inv.setItem(e.getSlot(), cursor);
                                            addTalismanEffects(e, false);
                                            sendMessage(player, "&aSuccessfully added " + cursor.getItemMeta().getDisplayName() + " &ato your active talismans!");
                                            player.setItemOnCursor(null);
                                        } else {
                                            if (cursor == null || cursor.getType().equals(AIR)){
                                                playErrorSound(player);
                                                sendMessage(player, "&cYou need to be holding something!");
                                            }else {
                                                playErrorSound(player);
                                                sendMessage(player, "&cYou can only put &ltalisman&r&c items in this slot!");
                                            }
                                        }
                                    } else {
                                        e.setCancelled(true);
                                    }
                                }else{
                                    if (e.getCursor() == null || e.getCursor().getType().equals(AIR)){
                                        buttonSound(player);
                                        Config.removeActiveTalismans(player, clicked);
                                        player.setItemOnCursor(clicked);
                                        inv.setItem(e.getSlot(), getActiveTalismanSlot(e));
                                        addTalismanEffects(e, true);
                                        sendMessage(player, "&aSuccessfully removed " + cursor.getItemMeta().getDisplayName() + " &afrom your active talismans!");
                                    }else{
                                        playErrorSound(player);
                                        sendMessage(player, "&cYou cannot have an item in your cursor!");
                                    }
                                }
                            }else{
                                if (e.getCursor() == null && e.getCursor().getType().equals(AIR)){
                                    player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_ELYTRA, 10, 2);
                                    Config.removeAccessories(player, clicked);
                                    player.setItemOnCursor(clicked);
                                    inv.setItem(e.getSlot(), null);
                                }else{
                                    playErrorSound(player);
                                    sendMessage(player, "&cYou cannot have an item in your cursor!");
                                }
                            }
                        }else{
                            if (e.getCursor() == null && e.getCursor().getType().equals(AIR)){
                                player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_ELYTRA, 10, 2);
                                Config.removeAccessories(player, clicked);
                                player.setItemOnCursor(clicked);
                                inv.setItem(e.getSlot(), null);
                            }else{
                                playErrorSound(player);
                                sendMessage(player, "&cYou cannot have an item in your cursor!");
                            }
                        }
                    }
                }else{
                    String name = WordUtils.capitalize(clicked.getType().name().toLowerCase().replaceAll("_", " "));
                    if (accessorySlots.contains(e.getSlot())){
                        e.setCancelled(true);
                        if (e.getCursor() == null || e.getCursor().getType().equals(AIR)){
                            buttonSound(player);
                            Config.removeActiveAccessories(player, clicked);
                            player.setItemOnCursor(clicked);
                            inv.setItem(e.getSlot(), getActiveAccessorySlot(e));
                            sendMessage(player, "&aSuccessfully removed " + name + " &afrom your active accessories!");
                        }else{
                            playErrorSound(player);
                            sendMessage(player, "&cYou cannot have an item in your cursor!");
                        }
                    }else if (talismanSlots.contains(e.getSlot())){
                        e.setCancelled(true);
                        if (e.getCursor() == null || e.getCursor().getType().equals(AIR)){
                            buttonSound(player);
                            Config.removeActiveTalismans(player, clicked);
                            player.setItemOnCursor(clicked);
                            inv.setItem(e.getSlot(), getActiveTalismanSlot(e));
                            addTalismanEffects(e, true);
                            sendMessage(player, "&aSuccessfully removed " + name + " &afrom your active talismans!");
                        }else{
                            playErrorSound(player);
                            sendMessage(player, "&cYou cannot have an item in your cursor!");
                        }
                    }else{
                        if (e.getCursor() == null || e.getCursor().getType().equals(AIR)){
                            e.setCancelled(true);
                            player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_ELYTRA, 10, 2);
                            Config.removeAccessories(player, clicked);
                            player.setItemOnCursor(clicked);
                            inv.setItem(e.getSlot(), null);
                        }else{
                            playErrorSound(player);
                            sendMessage(player, "&cYou cannot have an item in your cursor!");
                        }
                    }
                }
            } else {
                if (cursor != null || !cursor.getType().equals(AIR)){
                    e.setCancelled(true);
                    Config.addAccessories(player, cursor);
                    inv.setItem(e.getSlot(), new ItemStack(cursor));
                    player.setItemOnCursor(new ItemStack(AIR));
                    player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_ELYTRA, 10, 2);
                }
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e){
        Player player = (Player) e.getWhoClicked();
        ActivePlayer activePlayer = main.getActivePlayer(player.getName());
        Inventory inv = e.getInventory();
        ItemStack cursor = e.getCursor();
        ItemStack clicked = e.getOldCursor();
        if (Objects.equals(e.getInventory(), activePlayer.accessoryBag)) {
            e.setCancelled(true);
            playErrorSound(player);
            sendMessage(player, "&cError saving your accessory bag! Please try again!");
        }
    }

    private void addTalismanEffects(InventoryClickEvent e, boolean subtract){
        Player player = (Player) e.getWhoClicked();
        ActivePlayer activePlayer = main.getActivePlayer(player.getName());
        List<String> list = Arrays.asList(e.getCursor().getItemMeta().getDisplayName().toLowerCase().split(" "));
        if (!subtract) {
            switch (ChatColor.stripColor(list.get(0))) {
                case "running":
                    activePlayer.speedMultiplier = Integer.parseInt(list.get(2)) * 10;
                    player.setWalkSpeed((0.2F * (activePlayer.speedMultiplier / 100F + 1)));
                    break;
                case "shield":
                    player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(player.getAttribute(Attribute.GENERIC_ARMOR).getValue() + Integer.parseInt(list.get(2)));
                    break;
                case "strength":
                    activePlayer.strengthMultiplier = Integer.parseInt(list.get(2)) * 5;
                    break;
                case "gliding":
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 0));
                    break;
                case "stealth":
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
                    break;
                case "glowing":
                    player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0));
                    break;
            }
        }else{
            switch (ChatColor.stripColor(list.get(0))) {
                case "running":
                    if (player.getWalkSpeed() <= 0.1){
                        player.setWalkSpeed(0.1F);
                    }else {
                        activePlayer.speedMultiplier = activePlayer.speedMultiplier - Integer.parseInt(list.get(2)) * 10;
                        player.setWalkSpeed((0.2F * (activePlayer.speedMultiplier / 100F + 1)));
                    }
                    break;
                case "shield":
                    player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(player.getAttribute(Attribute.GENERIC_ARMOR).getValue() - Integer.parseInt(list.get(2)));
                    break;
                case "strength":
                    activePlayer.strengthMultiplier = activePlayer.strengthMultiplier - Integer.parseInt(list.get(2)) *5;
                    break;
                case "gliding":
                    player.removePotionEffect(PotionEffectType.SLOW_FALLING);
                    break;
                case "stealth":
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                    break;
                case "glowing":
                    player.removePotionEffect(PotionEffectType.GLOWING);
                    break;
            }
        }
    }

    private ItemStack getActiveTalismanSlot(InventoryClickEvent e){
        ItemStack item = new ItemStack(WHITE_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Active Talisman slot #" + (e.getSlot() - 44));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getActiveAccessorySlot(InventoryClickEvent e){
        ItemStack item = new ItemStack(WHITE_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Active Accessory slot #" + (e.getSlot() + 1));
        item.setItemMeta(meta);
        return item;
    }

    private void sendMessage(Player player, String message){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private void buttonSound(Player player){
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10, 1);
    }

    private void playErrorSound(Player player){
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 0);
    }

    public void initSlots(){
        accessorySlots = new ArrayList<>();
        talismanSlots = new ArrayList<>();

        for (int i = 0; i < 9; i++){
            accessorySlots.add(i);
        }

        talismanSlots.add(45);
        talismanSlots.add(46);
        talismanSlots.add(47);
        talismanSlots.add(48);
    }

}
