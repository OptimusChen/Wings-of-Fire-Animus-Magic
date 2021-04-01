package com.itech4kids.wingsoffire;

import com.mysql.fabric.xmlrpc.base.Array;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftBlockInventoryHolder;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventorySmithing;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import sun.nio.cs.ext.MacRoman;

import java.io.IOException;
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
                            activePlayer1.strengthMultiplier = Integer.valueOf(list.get(2))*5;
                            break;
                        case "running":
                            activePlayer1.speedMultiplier = Integer.valueOf(list.get(2))*10;
                            player.setWalkSpeed((0.2F * (activePlayer1.speedMultiplier/10+1)));
                            break;
                    }
                }

                if (activePlayer1.upgrade){
                    player.sendMessage(ChatColor.GREEN + "Your previous study session was cancelled due to you logging off!");
                    activePlayer1.upgrade = false;
                }
                main.showMana(player);
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
    public void onLeave(PlayerQuitEvent e){
        main.players.remove(e.getPlayer().getName());
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) throws IOException {
        Player player = (Player) e.getWhoClicked();
        ActivePlayer activePlayer = main.getActivePlayer(player.getName());
        if (e.getView().getTitle().equals("Accessories")) {
            if (e.getCurrentItem() != null) {
                if ((e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(" ") && e.getCurrentItem().getType().equals(GRAY_STAINED_GLASS_PANE)) || e.getClick().isKeyboardClick() || e.getClick().isShiftClick()) {
                    e.setCancelled(true);
                } else if (e.getCurrentItem() != null) {
                    if (e.getSlot() < 9 && !player.getItemOnCursor().getType().equals(Material.AIR) && e.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "Active Accessory slot") && e.getClickedInventory().equals(activePlayer.accessoryBag)) {
                        if (CraftItemStack.asNMSCopy(e.getCursor()).getItem() instanceof ItemArmor || CraftItemStack.asNMSCopy(e.getCursor()).getItem() instanceof ItemSkullPlayer || e.getCursor().getType().equals(ELYTRA)) {
                            e.setCancelled(true);
                            activePlayer.accessoryBag.setItem(e.getSlot(), player.getItemOnCursor());
                            Config.addActiveAccessories(player, player.getItemOnCursor());
                            player.setItemOnCursor(null);
                            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10, 10);
                        } else {
                            e.setCancelled(true);
                            player.sendMessage(ChatColor.RED + "You may only put accessories/armor items in here!");
                            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 10);
                        }
                    } else if (e.getSlot() < 9 && player.getItemOnCursor().getType().equals(Material.AIR) && !e.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "Active Accessory slot") && e.getClickedInventory().equals(activePlayer.accessoryBag)) {
                        e.setCancelled(true);
                        Config.removeActiveAccessories(player, e.getCurrentItem());
                        player.getInventory().addItem(e.getCurrentItem());
                        activePlayer.accessoryBag.setItem(e.getSlot(), activePlayer.getItem(e.getSlot() + 1, "Active Accessory"));
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10, 10);
                    } else if (e.getSlot() < 36 && e.getSlot() > 17 && e.getClickedInventory().equals(activePlayer.accessoryBag)) {
                        if (player.getItemOnCursor().getType().equals(Material.AIR)) {
                            Config.removeAccessories(player, e.getCurrentItem());
                            player.getInventory().addItem(e.getCurrentItem());
                            activePlayer.accessoryBag.setItem(e.getSlot(), new ItemStack(Material.AIR));
                            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10, 10);
                        }
                    } else if (e.getSlot() > 44 && e.getSlot() < 49 && e.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "Active Talisman slot") && e.getClickedInventory().equals(activePlayer.accessoryBag)) {
                        if (e.getCursor() != null && e.getCursor().getItemMeta().getDisplayName().contains("Talisman")) {
                            e.setCancelled(true);
                            activePlayer.accessoryBag.setItem(e.getSlot(), player.getItemOnCursor());
                            Config.addActiveTalismans(player, player.getItemOnCursor());
                            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10, 10);
                            List<String> list = Arrays.asList(e.getCursor().getItemMeta().getDisplayName().toLowerCase().split(" "));
                            switch (ChatColor.stripColor(list.get(0))) {
                                case "running":
                                    activePlayer.speedMultiplier = Integer.valueOf(list.get(2)) *10;
                                    player.setWalkSpeed((0.2F * (activePlayer.speedMultiplier/10+1)));
                                    break;
                                case "shield":
                                    player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(player.getAttribute(Attribute.GENERIC_ARMOR).getValue() + Integer.valueOf(list.get(2)));
                                    break;
                                case "strength":
                                    activePlayer.strengthMultiplier = Integer.valueOf(list.get(2)) *5;
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
                            player.setItemOnCursor(null);
                        } else {
                            e.setCancelled(true);
                            player.sendMessage(ChatColor.RED + "You may only put talisman items in here!");
                            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 10);
                        }
                    } else if (e.getSlot() > 44 && e.getSlot() < 49 && !e.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "Active Talisman slot") && e.getClickedInventory().equals(activePlayer.accessoryBag)) {
                        if (e.getCursor() != null){
                            e.setCancelled(true);
                            List<String> list = Arrays.asList(e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase().split(" "));
                            switch (ChatColor.stripColor(list.get(0))) {
                                case "running":
                                    activePlayer.speedMultiplier = activePlayer.speedMultiplier - Integer.parseInt(list.get(2)) *10;
                                    player.setWalkSpeed((0.2F * (activePlayer.speedMultiplier/10+1)));
                                    break;
                                case "shield":
                                    player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(player.getAttribute(Attribute.GENERIC_ARMOR).getValue() - Integer.valueOf(list.get(2)));
                                    break;
                                case "strength":
                                    activePlayer.strengthMultiplier = activePlayer.strengthMultiplier - Integer.valueOf(list.get(2)) *5;
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
                            Config.removeActiveTalismans(player, e.getCurrentItem());
                            player.getInventory().addItem(e.getCurrentItem());
                            activePlayer.accessoryBag.setItem(e.getSlot(), activePlayer.getItem(e.getSlot() - 44, "Active Talisman"));
                            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10, 10);
                        }else{
                            e.setCancelled(true);
                        }
                    }
                } else {
                    if (!Config.getActiveAccessories(player).contains(e.getCursor())) {
                        if (!e.getCursor().getType().equals(Material.AIR)) {
                            if (CraftItemStack.asNMSCopy(e.getCursor()).getItem() instanceof ItemArmor || CraftItemStack.asNMSCopy(e.getCursor()).getItem() instanceof ItemSkullPlayer || e.getCursor().getType().equals(ELYTRA) || e.getCursor().getItemMeta().getDisplayName().contains("Talisman")) {
                                Config.addAccessories(player, e.getCursor());
                                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10, 10);
                            } else {
                                e.setCancelled(true);
                                player.sendMessage(ChatColor.RED + "You may only put accessories/armor items in here!");
                                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 10);
                            }
                        }
                    } else {
                        e.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "Active and stored compatibility coming soon....I think...");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if (e.getItemInHand().getItemMeta().getDisplayName().contains("Talisman")){
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player player = e.getPlayer();
        ItemStack main = player.getInventory().getItemInMainHand();
        ItemStack off = player.getInventory().getItemInOffHand();
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (CraftItemStack.asNMSCopy(main).getItem() instanceof ItemArmor || CraftItemStack.asNMSCopy(off).getItem() instanceof ItemArmor){
                for (String string : main.getItemMeta().getLore()){
                    if (string.startsWith(ChatColor.GRAY + "Heart Scales ")){
                        List<String> list = Arrays.asList(string.toLowerCase().split(" "));
                        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + Integer.valueOf(list.get(2)));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPotion(EntityPotionEffectEvent e){
        if (e.getEntity().getName().equals(ChatColor.YELLOW + "" + ChatColor.BOLD + "Click")){
            e.setCancelled(true);
            WanderingTrader wanderingTrader = (WanderingTrader) e.getEntity();
            for (int i = 0; i < wanderingTrader.getInventory().getSize(); ++i){
                wanderingTrader.getInventory().setItem(i, new ItemStack(Material.BOOK));
            }
        }
    }

    @EventHandler
    public void onArmorClick(InventoryClickEvent e){
        if (CraftItemStack.asNMSCopy(e.getCursor()).getItem() instanceof ItemArmor){
            if (e.getClickedInventory().equals(e.getWhoClicked().getInventory())){
                if (e.getSlot() >= 36 && e.getSlot() <= 39) {
                    ItemStack main = e.getCursor();
                    ItemStack off = e.getCurrentItem();
                    Player player = (Player) e.getWhoClicked();
                    if (e.getCurrentItem() == null) {
                        for (String string : main.getItemMeta().getLore()){
                            if (string.startsWith(ChatColor.GRAY + "Heart Scales ")){
                                if (e.getCursor() != null){
                                    List<String> list = Arrays.asList(string.toLowerCase().split(" "));
                                    player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + Integer.valueOf(list.get(2)));
                                }
                            }
                        }
                    }else{
                        player.sendMessage(e.getCursor() + " Cursor");
                        player.sendMessage(e.getCurrentItem() + " Item");
                        player.sendMessage("worked");
                        for (String string : off.getItemMeta().getLore()){
                            player.sendMessage("worked2");
                            if (string.startsWith(ChatColor.GRAY + "Heart Scales ")){
                                player.sendMessage("worked3");
                                if (off != null){
                                    player.sendMessage("worked4");
                                    List<String> list = Arrays.asList(string.toLowerCase().split(" "));
                                    player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - Integer.valueOf(list.get(2)));
                                }
                            }
                        }
                    }
                }
            }
        }
    }




    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        if (e.getDeathMessage().endsWith("died")) {
            e.setDeathMessage(e.getEntity().getName() + " couldn't handle extreme Animus Magic");
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player){
            Player player = (Player) e.getDamager();
            ActivePlayer activePlayer = main.getActivePlayer(player.getName());
            e.setDamage(e.getFinalDamage()*((activePlayer.strengthMultiplier/10)+1));
            //player.sendMessage(activePlayer.speedMultiplier + " speeeeeed");
            //player.sendMessage(player.getWalkSpeed() + " walkspeeeeeed");
            //player.sendMessage(activePlayer.strengthMultiplier + " damaage");
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player player = e.getPlayer();
        if (Config.isUndead(player)){
            if (player.getLocation().getBlockY() + 1 > player.getWorld().getHighestBlockYAt(player.getLocation()) && this.day() && !player.getWorld().hasStorm() && player.getInventory().getHelmet() == null && !player.getGameMode().equals(GameMode.CREATIVE) && !player.getGameMode().equals(GameMode.SPECTATOR) && player.getFireTicks() == 0) {
                player.setFireTicks(60);
            }
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent e){
        Player player = e.getPlayer();
        if (e.getRightClicked().getName().equals(ChatColor.YELLOW + "" + ChatColor.BOLD + "Click")){
            e.setCancelled(true);
            main.openStudyGui(player);
        }else if (e.getRightClicked().getName().equals("Jade Mountain Librarian")){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDmg(EntityDamageByEntityEvent e){
        if (e.getEntity().getName().equals(ChatColor.YELLOW + "" + ChatColor.BOLD + "Click")){
            e.setCancelled(true);
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
                                player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(player.getAttribute(Attribute.GENERIC_ARMOR).getValue() + Integer.valueOf(list.get(2)));
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
        if (e.getView().getTitle().equalsIgnoreCase("Animus Magic GUI")){
            e.setCancelled(true);
            switch (e.getCurrentItem().getType()){
                case BOOK:
                    player.performCommand("animus study");
                    player.closeInventory();
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
                    break;
                case "running":
                    activePlayer.pendingCmd = "animus enchant simple running ";
                    main.openSelectNumber(player);
                    break;
                case "strength":
                    activePlayer.pendingCmd = "animus enchant simple strength ";
                    main.openSelectNumber(player);
                    break;
                case "shield":
                    activePlayer.pendingCmd = "animus enchant simple shield ";
                    main.openSelectNumber(player);
                    break;
                case "gliding":
                    player.performCommand("animus enchant simple gliding 1");
                    break;
                case "glowing":
                    player.performCommand("animus enchant simple glowing 1");
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
                    main.openSelectNumber(player);
                    break;
                case "heart":
                    activePlayer.pendingCmd = "animus enchant simple heart_scale_armor ";
                    main.openSelectNumber(player);
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
                    main.openOnlinePlayers(player);
                    break;
                case "swift":
                    activePlayer.pendingPlayerCmd1 = "animus enchant playerbuff ";
                    activePlayer.pendingPlayerCmd2 = " swift_talons ";
                    activePlayer.needsToSelectNumber = true;
                    main.openOnlinePlayers(player);
                    break;
                case "stone":
                    activePlayer.pendingPlayerCmd1 = "animus enchant playerbuff ";
                    activePlayer.pendingPlayerCmd2 = " stone_scales ";
                    activePlayer.needsToSelectNumber = true;
                    main.openOnlinePlayers(player);
                    break;
                case "leaping":
                    activePlayer.pendingPlayerCmd1 = "animus enchant playerbuff ";
                    activePlayer.pendingPlayerCmd2 = " leaping_talons ";
                    activePlayer.needsToSelectNumber = true;
                    main.openOnlinePlayers(player);
                    break;
                case "magma":
                    activePlayer.pendingPlayerCmd1 = "animus enchant playerbuff ";
                    activePlayer.pendingPlayerCmd2 = " magma_scales ";
                    activePlayer.needsToSelectNumber = true;
                    main.openOnlinePlayers(player);
                    break;
                case "rejuvenation":
                    activePlayer.pendingPlayerCmd1 = "animus enchant playerbuff ";
                    activePlayer.pendingPlayerCmd2 = " rejuvenation ";
                    activePlayer.needsToSelectNumber = true;
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
                case "111":
                    activePlayer.pendingPlayerCmd1 = "animus enchant playerbuff ";
                    activePlayer.pendingPlayerCmd2 = " animus ";
                    activePlayer.needsToSelectNumber = false;
                    main.openOnlinePlayers(player);
                    break;
            }
        }else if (e.getView().getTitle().equalsIgnoreCase("Select Player")){
            if (e.getCurrentItem().getType().equals(PLAYER_HEAD)){
                if (e.getClickedInventory().equals(main.onlinePlayers)){
                    if (e.getCurrentItem() != null){
                        activePlayer.pendingPlayerName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                        activePlayer.pendingCmd = activePlayer.pendingPlayerCmd1 + activePlayer.pendingPlayerName + activePlayer.pendingPlayerCmd2;
                        if (activePlayer.needsToSelectNumber){
                            main.openSelectNumber(player);
                        }else{
                            player.performCommand(activePlayer.pendingCmd);
                            e.setCancelled(true);
                            player.closeInventory();
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
            if (e.getCurrentItem().getType().equals(Material.BOOK)){
                main.startStudy(player);
                player.closeInventory();
            }
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





