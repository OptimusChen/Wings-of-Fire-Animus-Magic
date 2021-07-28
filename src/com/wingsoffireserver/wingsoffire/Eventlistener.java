package com.wingsoffireserver.wingsoffire;

import com.wingsoffireserver.wingsoffire.Armor.ArmorEquipEvent;
import com.wingsoffireserver.wingsoffire.Armor.ArmorType;
import com.wingsoffireserver.wingsoffire.ArmorEvent.PlayerArmorEquipEvent;
import com.wingsoffireserver.wingsoffire.ArmorEvent.PlayerArmorUnequipEvent;
import com.wingsoffireserver.wingsoffire.Inventory.WoFInventory;
import com.wingsoffireserver.wingsoffire.NPC.NPC;
import com.wingsoffireserver.wingsoffire.NPC.NpcClickEvent;
import com.wingsoffireserver.wingsoffire.NPC.PacketReader;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.security.KeyStore;
import java.util.*;

import static org.bukkit.Material.*;

public class Eventlistener implements Listener {

    private Main main;

    public Eventlistener(Main headHunt) {
        this.main = headHunt;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws IOException {
        Player player = e.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run () {
                try {
                    Config.createPlayer(player.getName());

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    ActivePlayer activePlayer = new ActivePlayer(player);
                    main.players.put(player.getName(), activePlayer);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                ActivePlayer activePlayer1 = main.getActivePlayer(player.getName());
                for (ItemStack itemStack : Config.getActiveTalismans(player)){
                    List<String> list = Arrays.asList(itemStack.getItemMeta().getDisplayName().toLowerCase().split(" "));
                    switch (ChatColor.stripColor(list.get(0))) {
                        case "strength":
                            activePlayer1.strengthMultiplier = Integer.parseInt(list.get(2)) * 5;
                            break;
                        case "running":
                            activePlayer1.speedMultiplier = Integer.parseInt(list.get(2)) * 10;
                            player.setWalkSpeed((0.2F * (activePlayer1.speedMultiplier / 100F + 1)));
                            break;
                        case "shield":
                            player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(player.getAttribute(Attribute.GENERIC_ARMOR).getValue() + Integer.parseInt(list.get(2)));
                            break;
                    }
                }

                if (!Config.getShapeshift(player).equals("Default")){
                    main.setSkin(player, Config.getShapeshift(player));
                }

                if (activePlayer1.upgrade){
                    player.sendMessage(ChatColor.GREEN + "Your previous study session was cancelled due to you logging off!");
                    activePlayer1.upgrade = false;
                }
                main.showMana(player);

                for (ItemStack item : player.getInventory().getArmorContents()){
                    if (item != null){
                        Bukkit.getPluginManager().callEvent(new PlayerArmorUnequipEvent(player, item));

                    }
                }
            }
        }.runTaskLater(main, 1);


        /*
        new BukkitRunnable() {
            @Override
            public void run () {
                player.setWalkSpeed((0.2F * (activePlayer1.speedMultiplier/10+1)));
            }
        }.runTaskLater(main, 1);

         */
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        ActivePlayer activePlayer = main.getActivePlayer(e.getPlayer().getName());
        if (activePlayer.isSelectingNumber){
            e.setCancelled(true);

            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        if (Integer.parseInt(e.getMessage()) > activePlayer.maxLevel){
                            activePlayer.player.playSound(activePlayer.player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                            activePlayer.player.sendMessage(ChatColor.GRAY + "This value is too large for this enchantment! Please try again!\nThe max level for this enchantment is: " + main.IntegerToRomanNumeral(activePlayer.maxLevel));
                        }else{
                            activePlayer.player.performCommand(activePlayer.pendingCmd + Integer.parseInt(e.getMessage()));
                            activePlayer.needsToSelectNumber = false;
                            activePlayer.isSelectingNumber = false;
                        }
                    }catch (NumberFormatException n){
                        if (e.getMessage().equalsIgnoreCase("cancel")){
                            e.getPlayer().sendMessage(ChatColor.GRAY + "Successfully cancelled enchantment!");
                            activePlayer.needsToSelectNumber = false;
                            activePlayer.isSelectingNumber = false;
                            e.setCancelled(true);
                        }else{
                            activePlayer.player.playSound(activePlayer.player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                            activePlayer.player.sendMessage(ChatColor.RED + "This is an invalid number! Please try again or type" + ChatColor.BOLD + " cancel" + ChatColor.RESET + ChatColor.RED + " to cancel!");
                        }
                    }
                }
            }.runTaskLater(main, 1);
        }
    }

//    @EventHandler
//    public void onClick(InventoryClickEvent e) throws IOException {
//        Player player = (Player) e.getWhoClicked();
//        ActivePlayer activePlayer = main.getActivePlayer(player.getName());
//        if (e.getView().getTitle().equals("Accessories")) {
//            if (e.getCurrentItem() != null) {
//                if ((e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(" ") || e.getClick().isKeyboardClick() || e.getClick().isShiftClick())) {
//                    e.setCancelled(true);
//                } else if (e.getCurrentItem() != null) {
//                    if (e.getSlot() < 9 && !player.getItemOnCursor().getType().equals(Material.AIR) && e.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "Active Accessory slot") && e.getClickedInventory().equals(activePlayer.accessoryBag)) {
//                        if (CraftItemStack.asNMSCopy(e.getCursor()).getItem() instanceof ItemArmor || CraftItemStack.asNMSCopy(e.getCursor()).getItem() instanceof ItemSkullPlayer || e.getCursor().getType().equals(ELYTRA)) {
//                            e.setCancelled(true);
//                            Config.addActiveAccessories(player, player.getItemOnCursor());
//
//                            Bukkit.getPluginManager().callEvent(new PlayerArmorEquipEvent(player, e.getCursor()));
//
//                            activePlayer.accessoryBag.setItem(e.getSlot(), player.getItemOnCursor());
//                            player.setItemOnCursor(null);
//                            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10, 10);
//                        } else {
//                            e.setCancelled(true);
//                            player.sendMessage(ChatColor.RED + "You may only put accessories/armor items in here!");
//                            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 10);
//                        }
//                    } else if (e.getSlot() < 9 && player.getItemOnCursor().getType().equals(Material.AIR) && !e.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "Active Accessory slot") && e.getClickedInventory().equals(activePlayer.accessoryBag)) {
//                        e.setCancelled(true);
//                        Config.removeActiveAccessories(player, e.getCurrentItem());
//
//                        Bukkit.getPluginManager().callEvent(new PlayerArmorUnequipEvent(player, e.getCurrentItem()));
//
//                        player.getInventory().addItem(e.getCurrentItem());
//
//                        activePlayer.accessoryBag.setItem(e.getSlot(), activePlayer.getItem(e.getSlot() + 1, "Active Accessory"));
//                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10, 10);
//                    }else if (e.getSlot() < 9 && player.getItemOnCursor().getType().equals(Material.AIR) && e.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "Active Accessory slot") && e.getClickedInventory().equals(activePlayer.accessoryBag)) {
//                        e.setCancelled(true);
//                    }else if (e.getSlot() < 36 && e.getSlot() > 17 && e.getClickedInventory().equals(activePlayer.accessoryBag)) {
//                        if (player.getItemOnCursor().getType().equals(Material.AIR)) {
//                            Config.removeAccessories(player, e.getCurrentItem());
//                            player.getInventory().addItem(e.getCurrentItem());
//                            activePlayer.accessoryBag.setItem(e.getSlot(), new ItemStack(Material.AIR));
//                            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10, 10);
//                            e.setCancelled(true);
//                        }
//                    } else if ((e.getSlot() > 44 && e.getSlot() < 49) && e.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "Active Talisman slot") && e.getClickedInventory().equals(activePlayer.accessoryBag)) {
//                        if (e.getCursor() != null) {
//                            e.setCancelled(true);
//                            if (ChatColor.stripColor(e.getCursor().getItemMeta().getDisplayName()).toLowerCase().contains("talisman") && !ChatColor.stripColor(e.getCursor().getItemMeta().getDisplayName()).toLowerCase().contains("talisman slot")) {
//                                e.setCancelled(true);
//                                activePlayer.accessoryBag.setItem(e.getSlot(), player.getItemOnCursor());
//                                Config.addActiveTalismans(player, player.getItemOnCursor());
//                                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10, 10);
//                                List<String> list = Arrays.asList(e.getCursor().getItemMeta().getDisplayName().toLowerCase().split(" "));
//                                switch (ChatColor.stripColor(list.get(0))) {
//                                    case "running":
//                                        activePlayer.speedMultiplier = Integer.parseInt(list.get(2)) * 10;
//                                        player.setWalkSpeed((0.2F * (activePlayer.speedMultiplier / 100F + 1)));
//                                        break;
//                                    case "shield":
//                                        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(player.getAttribute(Attribute.GENERIC_ARMOR).getValue() + Integer.parseInt(list.get(2)));
//                                        break;
//                                    case "strength":
//                                        activePlayer.strengthMultiplier = Integer.parseInt(list.get(2)) * 5;
//                                        break;
//                                    case "gliding":
//                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 0));
//                                        break;
//                                    case "stealth":
//                                        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
//                                        break;
//                                    case "glowing":
//                                        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0));
//                                        break;
//                                }
//                                player.setItemOnCursor(null);
//                            } else {
//                                e.setCancelled(true);
//                                player.sendMessage(ChatColor.RED + "You may only put talisman items in here!");
//                                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 10);
//                            }
//                        } else {
//                            e.setCancelled(true);
//                        }
//                    } else if (e.getSlot() > 44 && e.getSlot() < 49 && !e.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "Active Talisman slot") && e.getClickedInventory().equals(activePlayer.accessoryBag)) {
//                        if (e.getCursor() != null){
//                            e.setCancelled(true);
//                            List<String> list = Arrays.asList(e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase().split(" "));
//                            switch (ChatColor.stripColor(list.get(0))) {
//                                case "running":
//                                    if (player.getWalkSpeed() <= 0.1){
//                                        player.setWalkSpeed(0.1F);
//                                    }else {
//                                        activePlayer.speedMultiplier = activePlayer.speedMultiplier - Integer.parseInt(list.get(2)) * 10;
//                                        player.setWalkSpeed((0.2F * (activePlayer.speedMultiplier / 100F + 1)));
//                                    }
//                                    break;
//                                case "shield":
//                                    player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(player.getAttribute(Attribute.GENERIC_ARMOR).getValue() - Integer.parseInt(list.get(2)));
//                                    break;
//                                case "strength":
//                                    activePlayer.strengthMultiplier = activePlayer.strengthMultiplier - Integer.parseInt(list.get(2)) *5;
//                                    break;
//                                case "gliding":
//                                    player.removePotionEffect(PotionEffectType.SLOW_FALLING);
//                                    break;
//                                case "stealth":
//                                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
//                                    break;
//                                case "glowing":
//                                    player.removePotionEffect(PotionEffectType.GLOWING);
//                                    break;
//                            }
//                            Config.removeActiveTalismans(player, e.getCurrentItem());
//                            player.getInventory().addItem(e.getCurrentItem());
//                            activePlayer.accessoryBag.setItem(e.getSlot(), activePlayer.getItem(e.getSlot() - 44, "Active Talisman"));
//                            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10, 10);
//                        }else{
//                            e.setCancelled(true);
//                        }
//                    }else if (e.getSlot() > 44 && e.getSlot() < 49 && e.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "Active Talisman slot") && e.getClickedInventory().equals(activePlayer.accessoryBag)) {
//                        if (e.getCursor() == null){
//                            e.setCancelled(true);
//                        }
//                    }else if (e.getSlot() > 44 && e.getSlot() < 49 && e.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "Active Talisman slot") && e.getClickedInventory().equals(activePlayer.accessoryBag)){
//                        if (e.getCursor() == null) {
//                            e.setCancelled(true);
//                        }
//                    }
//                }
//            }else {
//                if (!e.getCursor().getType().equals(Material.AIR)) {
//                    if (CraftItemStack.asNMSCopy(e.getCursor()).getItem() instanceof ItemArmor || CraftItemStack.asNMSCopy(e.getCursor()).getItem() instanceof ItemSkullPlayer || e.getCursor().getType().equals(ELYTRA) || e.getCursor().getItemMeta().getDisplayName().contains("Talisman")) {
//                        Config.addAccessories(player, e.getCursor());
//                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10, 10);
//                    } else {
//                        e.setCancelled(true);
//                        player.sendMessage(ChatColor.RED + "You may only put accessories/armor items in here!");
//                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 10);
//                    }
//                }else {
//                    e.setCancelled(true);
//                    player.sendMessage(ChatColor.RED + "Active and stored compatibility coming soon....I think...");
//                }
//            }
//        }
//    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if (e.getItemInHand().getItemMeta().getDisplayName().contains("Talisman") || e.getItemInHand().getItemMeta().getDisplayName().contains("Accessory Bag")){
            if (e.getItemInHand().getType().equals(PLAYER_HEAD)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        if (e.getSlot() >= 36 && e.getSlot() <= 39){
            if (e.getCurrentItem() != null){
                if (e.getClick().equals(ClickType.NUMBER_KEY)){
                    for (String s : e.getCurrentItem().getItemMeta().getLore()) {
                        if (s.startsWith(ChatColor.GRAY + "Heart Scales ")) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                }
            }
            for (String s : e.getCurrentItem().getItemMeta().getLore()) {
                if (s.startsWith(ChatColor.GRAY + "Heart Scales ")) {
                    Bukkit.getPluginManager().callEvent(new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.PICK_DROP, ArmorType.matchType(e.getCurrentItem()), e.getCurrentItem(), e.getCursor()));
                }
            }
        }
    }

    @EventHandler
    public void onArmor(PlayerArmorEquipEvent e){
        Player player = e.getPlayer();
        if (e.getItemStack() != null) {
            if (e.getItemStack().getItemMeta().getLore() != null) {
                for (String s : e.getItemStack().getItemMeta().getLore()) {
                    if (s.startsWith(ChatColor.GRAY + "Heart Scales ")) {
                        List<String> list = Arrays.asList(s.toLowerCase().split(" "));
                        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + Integer.parseInt(list.get(2)));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onArmor(PlayerArmorUnequipEvent e){
        Player player = e.getPlayer();
        if (e.getItemStack() != null){
            if (e.getItemStack().getItemMeta().getLore() != null){
                for (String s : e.getItemStack().getItemMeta().getLore()) {
                    if (s.startsWith(ChatColor.GRAY + "Heart Scales ")) {
                        List<String> list = Arrays.asList(s.toLowerCase().split(" "));
                        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - Integer.parseInt(list.get(2)));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player){
            Player player = (Player) e.getDamager();
            ActivePlayer activePlayer = main.getActivePlayer(player.getName());
            if (activePlayer.strengthMultiplier != 0) {
                e.setDamage(e.getFinalDamage() * ((activePlayer.strengthMultiplier / 10F) + 1));
            }
            //player.sendMessage(activePlayer.speedMultiplier + " speeeeeed");
            //player.sendMessage(player.getWalkSpeed() + " walkspeeeeeed");
            //player.sendMessage(activePlayer.strengthMultiplier + " damaage");
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player player = e.getPlayer();
        if (Config.isUndead(player)){
            if (player.getLocation().getBlockY() + 1 > player.getWorld().getHighestBlockYAt(player.getLocation()) && !player.getWorld().hasStorm() && player.getInventory().getHelmet() == null) {
                player.setFireTicks(60);
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player player = e.getPlayer();

        main.players.remove(e.getPlayer().getName());

        PacketReader reader = new PacketReader();
        reader.unInject(e.getPlayer());

        for (ItemStack itemStack : Config.getActiveTalismans(e.getPlayer())){
            List<String> list = Arrays.asList(itemStack.getItemMeta().getDisplayName().toLowerCase().split(" "));
            switch (ChatColor.stripColor(list.get(0))){
                case "shield":
                    player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(player.getAttribute(Attribute.GENERIC_ARMOR).getValue() - Integer.parseInt(list.get(2)));
                    break;
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if (e.getPlayer().getInventory().getItemInMainHand() != null){
            if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()){
                if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().startsWith(ChatColor.YELLOW + "Accessory Bag")){
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 10, 1);
                    e.getPlayer().performCommand("ab");
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Player player = e.getPlayer();
        if (Config.isGentleTalons(player)){
            if (!(e.getBlock().getType().equals(SPAWNER) || e.getBlock().getType().equals(BARRIER) || e.getBlock().getType().equals(BEDROCK) || e.getBlock().getType().equals(COMMAND_BLOCK) || e.getBlock().getType().equals(CAKE)))
            e.setDropItems(false);
            player.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(e.getBlock().getType()));
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run () {
                for (ItemStack itemStack : Config.getActiveTalismans(player)){
                    if (itemStack != null){
                        List<String> list = Arrays.asList(itemStack.getItemMeta().getDisplayName().toLowerCase().split(" "));
                        switch (ChatColor.stripColor(list.get(0))) {
                            case "shield":
                                player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(player.getAttribute(Attribute.GENERIC_ARMOR).getValue() + Integer.parseInt(list.get(2)));
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
                    }
                }
            }
        }.runTaskLater(main, 1);
    }

    @EventHandler
    public void onAnimusGUIClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        ActivePlayer activePlayer = main.getActivePlayer(player.getName());

        if (e.getView().getTitle().toLowerCase().contains("animus")){
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getItemMeta().hasDisplayName()) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "go back")){
                        player.performCommand("animus");
                        return;
                    }
                }
            }
        }

        if (e.getView().getTitle().equalsIgnoreCase("Animus Magic GUI")){
            e.setCancelled(true);
            switch (e.getCurrentItem().getType()){
                case BOOK:
                    player.performCommand("animus study");
                    break;
                case EXPERIENCE_BOTTLE:
                    main.openAnimusEnchantingGUI(player);
                    break;
            }
        }else if (e.getView().getTitle().equalsIgnoreCase("Animus Enchanting")){
            e.setCancelled(true);
            switch (e.getCurrentItem().getType()){
                case WATER_BUCKET:
                    main.openWeatherEnchantingGui(player);
                    break;
                case GOLDEN_APPLE:
                    main.openPlayerEnchantsGui(player);
                    break;
                case PLAYER_HEAD:
                    main.openTalismanEnchantingInventory(player);
                    break;
                case PUFFERFISH:
                    main.openAnimusCursingGui(player);
                    break;
                case FIRE_CHARGE:
                    player.performCommand("animus enchant extreme");
                    break;
        }
    }else if (e.getView().getTitle().equalsIgnoreCase("Animus Item Enchanting")){
            e.setCancelled(true);
            List<String> list = Arrays.asList(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase()).split(" "));
            switch (list.get(0)){
                case "stealth":
                    player.performCommand("animus enchant simple stealth 1");
                    player.closeInventory();
                    break;
                case "running":
                    activePlayer.pendingCmd = "animus enchant simple running ";
                    main.openSelectNumber(player, 4);
                    break;
                case "strength":
                    activePlayer.pendingCmd = "animus enchant simple strength ";
                    main.openSelectNumber(player, 4);
                    break;
                case "shield":
                    activePlayer.pendingCmd = "animus enchant simple shield ";
                    main.openSelectNumber(player, 5);
                    break;
                case "gliding":
                    player.performCommand("animus enchant simple gliding 1");
                    player.closeInventory();
                    break;
                case "glowing":
                    player.performCommand("animus enchant simple glowing 1");
                    player.closeInventory();
                    break;
                case "sharpness":
                    player.performCommand("animus enchant simple extra_sharpness");
                    player.closeInventory();
                    break;
                case "protection":
                    player.performCommand("animus enchant simple extra_protection");
                    player.closeInventory();
                    break;
                case "axe":
                    activePlayer.pendingCmd = "animus enchant simple axe_of_flames ";
                    main.openSelectNumber(player, 2);
                    break;
                case "heart":
                    activePlayer.pendingCmd = "animus enchant simple heart_scale_armor ";
                    main.openSelectNumber(player, 2);
                    break;
            }
        }else if (e.getView().getTitle().equalsIgnoreCase("Select Number")){
            e.setCancelled(true);
            List<String> list = Arrays.asList(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase()).split(" "));
            if (e.getCurrentItem().getType().equals(GREEN_CONCRETE)){
                player.performCommand(activePlayer.pendingCmd + list.get(1));
                player.closeInventory();
            }
        }else if (e.getView().getTitle().equalsIgnoreCase("Animus Player Enchanting")){
            e.setCancelled(true);
            List<String> list = Arrays.asList(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase()).split(" "));
            switch (list.get(0)){
                case "health":
                    activePlayer.pendingPlayerCmd1 = "animus enchant playerbuff ";
                    activePlayer.pendingPlayerCmd2 = " health_boost ";
                    activePlayer.needsToSelectNumber = true;
                    activePlayer.maxLevel = 4;
                    main.openOnlinePlayers(player);
                    break;
                case "swift":
                    activePlayer.pendingPlayerCmd1 = "animus enchant playerbuff ";
                    activePlayer.pendingPlayerCmd2 = " swift_talons ";
                    activePlayer.needsToSelectNumber = true;
                    activePlayer.maxLevel = 5;
                    main.openOnlinePlayers(player);
                    break;
                case "stone":
                    activePlayer.pendingPlayerCmd1 = "animus enchant playerbuff ";
                    activePlayer.pendingPlayerCmd2 = " stone_scales ";
                    activePlayer.needsToSelectNumber = true;
                    activePlayer.maxLevel = 4;
                    main.openOnlinePlayers(player);
                    break;
                case "leaping":
                    activePlayer.pendingPlayerCmd1 = "animus enchant playerbuff ";
                    activePlayer.pendingPlayerCmd2 = " leaping_talons ";
                    activePlayer.needsToSelectNumber = true;
                    activePlayer.maxLevel = 3;
                    main.openOnlinePlayers(player);
                    break;
                case "magma":
                    activePlayer.pendingPlayerCmd1 = "animus enchant playerbuff ";
                    activePlayer.pendingPlayerCmd2 = " magma_scales ";
                    activePlayer.needsToSelectNumber = true;
                    activePlayer.maxLevel = 2;
                    main.openOnlinePlayers(player);
                    break;
                case "rejuvenation":
                    activePlayer.pendingPlayerCmd1 = "animus enchant playerbuff ";
                    activePlayer.pendingPlayerCmd2 = " rejuvenation ";
                    activePlayer.needsToSelectNumber = true;
                    activePlayer.maxLevel = 2;
                    main.openOnlinePlayers(player);
                    break;
                case "cure":
                    activePlayer.pendingPlayerCmd1 = "animus enchant playerbuff ";
                    activePlayer.pendingPlayerCmd2 = " cure_disease ";
                    activePlayer.needsToSelectNumber = false;
                    main.openOnlinePlayers(player);
                    break;
                case "gentle":
                    activePlayer.pendingPlayerCmd1 = "animus enchant playerbuff ";
                    activePlayer.pendingPlayerCmd2 = " gentle_talons ";
                    activePlayer.needsToSelectNumber = false;
                    main.openOnlinePlayers(player);
                    break;
                case "species":
                    activePlayer.pendingPlayerCmd1 = "animus enchant playerbuff ";
                    activePlayer.pendingPlayerCmd2 = " shapeshift ";
                    activePlayer.needsToSelectNumber = false;
                    activePlayer.needsToSelectTribe = true;
                    main.openOnlinePlayers(player);
                    break;
                case "111":
                      player.sendMessage(ChatColor.RED + "Cant be implemented");
//                    activePlayer.pendingPlayerCmd1 = "animus enchant playerbuff ";
//                    activePlayer.pendingPlayerCmd2 = " animus ";
//                    activePlayer.needsToSelectNumber = false;
//                    main.openOnlinePlayers(player);
                    break;
            }
        }else if (e.getView().getTitle().equalsIgnoreCase("Select Player")){
            if (e.getCurrentItem().getType().equals(PLAYER_HEAD)){
                if (e.getClickedInventory().equals(main.onlinePlayers)){
                    if (e.getCurrentItem() != null){
                        activePlayer.pendingPlayerName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                        activePlayer.pendingCmd = activePlayer.pendingPlayerCmd1 + activePlayer.pendingPlayerName + activePlayer.pendingPlayerCmd2;
                        if (activePlayer.needsToSelectNumber){
                            main.openSelectNumber(player, activePlayer.maxLevel);
                        }else{
                            if (!activePlayer.needsToSelectTribe) {
                                player.performCommand(activePlayer.pendingCmd);
                                e.setCancelled(true);
                                player.closeInventory();
                            }else{
                                main.openShapeShiftGui(player);
                                activePlayer.needsToSelectTribe = false;
                            }
                        }
                    }
                }
            }
        }else if (e.getView().getTitle().equalsIgnoreCase("Animus Weather Enchanting")){
            e.setCancelled(true);
            List<String> list = Arrays.asList(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase()).split(" "));
            switch (list.get(0)){
                case "part":
                    player.performCommand("animus enchant weather part_clouds");
                    player.closeInventory();
                    break;
                case "rain":
                case "thunder":
                    player.performCommand("animus enchant weather rain");
                    player.closeInventory();
                    break;
                case "lightning":
                    player.performCommand("animus enchant weather thunder");
                    player.closeInventory();
                    break;
            }
        } else if (e.getView().getTitle().equalsIgnoreCase("Animus Curse Enchanting")) {
            e.setCancelled(true);
            List<String> list = Arrays.asList(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase()).split(" "));
            switch (list.get(2)){
                case "binding":
                    player.performCommand("animus enchant curses binding");
                    player.closeInventory();
                    break;
                case "vanishing":
                    player.performCommand("animus enchant curses vanishing");
                    player.closeInventory();
                    break;
                case "sickness":
                    activePlayer.pendingPlayerCmd1 = "animus enchant playercurses ";
                    activePlayer.pendingPlayerCmd2 = " sickness";
                    activePlayer.needsToSelectNumber = false;
                    main.openOnlinePlayers(player);
                    break;
                case "degradation":
                    activePlayer.pendingPlayerCmd1 = "animus enchant playercurses ";
                    activePlayer.pendingPlayerCmd2 = " degradation";
                    activePlayer.needsToSelectNumber = false;
                    main.openOnlinePlayers(player);
                    break;
                case "undead":
                    activePlayer.pendingPlayerCmd1 = "animus enchant playercurses ";
                    activePlayer.pendingPlayerCmd2 = " undead";
                    activePlayer.needsToSelectNumber = false;
                    main.openOnlinePlayers(player);
                    break;
                case "blindness":
                    activePlayer.pendingPlayerCmd1 = "animus enchant playercurses ";
                    activePlayer.pendingPlayerCmd2 = " blindness";
                    activePlayer.needsToSelectNumber = false;
                    main.openOnlinePlayers(player);
                    break;
            }
        }else if (e.getView().getTitle().equalsIgnoreCase("Animus Studying")){
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getType().equals(Material.BOOK)) {
                    main.startStudy(player);
                    player.closeInventory();
                }
            }
        }else if (e.getView().getTitle().equalsIgnoreCase("Select Tribe")){
            e.setCancelled(true);
            switch (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase())){
                case "nightwing":
                    activePlayer.pendingCmd = activePlayer.pendingCmd + "NightWing";
                    player.performCommand(activePlayer.pendingCmd);
                    break;
                case "icewing":
                    activePlayer.pendingCmd = activePlayer.pendingCmd + "IceWing";
                    player.performCommand(activePlayer.pendingCmd);
                    break;
                case "mudwing":
                    activePlayer.pendingCmd = activePlayer.pendingCmd + "MudWing";
                    player.performCommand(activePlayer.pendingCmd);
                    break;
                case "rainwing":
                    activePlayer.pendingCmd = activePlayer.pendingCmd + "RainWing";
                    player.performCommand(activePlayer.pendingCmd);
                    break;
                case "skywing":
                    activePlayer.pendingCmd = activePlayer.pendingCmd + "SkyWing";
                    player.performCommand(activePlayer.pendingCmd);
                    break;
                case "seawing":
                    activePlayer.pendingCmd = activePlayer.pendingCmd + "SeaWing";
                    player.performCommand(activePlayer.pendingCmd);
                    break;
                case "sandwing":
                    activePlayer.pendingCmd = activePlayer.pendingCmd + "SandWing";
                    player.performCommand(activePlayer.pendingCmd);
                    break;
                case "default":
                    activePlayer.pendingCmd = activePlayer.pendingCmd + "Default";
                    player.performCommand(activePlayer.pendingCmd);
                    break;
            }
        }else if (e.getView().getTitle().equalsIgnoreCase("Animus Level")){
            e.setCancelled(true);
        }
    }

    public boolean day() {
        Server server = main.getServer();
        long time = server.getWorld("world").getTime();

        if(time > 0 && time < 12300) {
            return true;
        } else {
            return false;
        }
    }

}





