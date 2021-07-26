package com.wingsoffireserver.wingsoffire.ArmorEvent;

import com.wingsoffireserver.wingsoffire.Main;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.Location;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.Bukkit;
import com.google.common.collect.Maps;
import org.bukkit.inventory.ItemStack;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import org.bukkit.event.Listener;

public final class EventAnalyser implements Listener
{
    private final ConcurrentMap<UUID, ItemStack[]> contents;

    public EventAnalyser() {
        this.contents = Maps.newConcurrentMap();
        Bukkit.getOnlinePlayers().stream().forEach(player -> this.getContents().putIfAbsent(player.getUniqueId(), player.getEquipment().getArmorContents()));
    }

    @EventHandler
    public final void onEvent(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        final Inventory inventory = event.getClickedInventory();
        if (inventory != null && (inventory.getType() == InventoryType.CRAFTING || inventory.getType() == InventoryType.PLAYER) && (event.getSlotType() == InventoryType.SlotType.ARMOR || event.isShiftClick())) {
            this.check((Player)event.getWhoClicked());
        }
    }

    @EventHandler
    public final void onEvent(final PlayerInteractEvent event) {
        final Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            final ItemStack item = event.getItem();
            if (item == null) {
                return;
            }
            final String name = item.getType().name();
            if (name.contains("_HELMET") || name.contains("_CHESTPLATE") || name.contains("_LEGGINGS") || name.contains("_BOOTS")) {
                this.check(event.getPlayer());
            }
        }
    }

    @EventHandler
    public final void onEvent(final PlayerDeathEvent event) {
        this.check(event.getEntity());
    }

    @EventHandler
    public final void onEvent(final PlayerJoinEvent event) {
        this.check(event.getPlayer());
    }

    @EventHandler
    public final void onEvent(final PlayerQuitEvent event) {
        if (this.getContents().containsKey(event.getPlayer().getUniqueId())) {
            this.getContents().remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public final void onEvent(final BlockDispenseEvent event) {
        final ItemStack item = event.getItem();
        final Location location = event.getBlock().getLocation();
        if (item != null) {
            location.getWorld().getNearbyEntities(location, 6.0, 6.0, 6.0).stream().filter(e -> e instanceof Player).map(e -> e).forEach(player -> this.check((Player) player));
        }
    }

    @EventHandler
    public final void onEvent(final PlayerItemBreakEvent event) {
        this.check(event.getPlayer());
    }

    private void check(final Player player) {
        new BukkitRunnable() {
            public void run() {
                final ItemStack[] now = player.getEquipment().getArmorContents();
                final ItemStack[] saved = (ItemStack[])EventAnalyser.this.getContents().get(player.getUniqueId());
                for (int i = 0; i < now.length; ++i) {
                    if (now[i] == null && saved != null && saved[i] != null) {
                        Bukkit.getPluginManager().callEvent((Event)new PlayerArmorUnequipEvent(player, saved[i]));
                    }
                    else if (now[i] != null && (saved == null || saved[i] == null)) {
                        Bukkit.getPluginManager().callEvent((Event)new PlayerArmorEquipEvent(player, now[i]));
                    }
                    else if (saved != null && now[i] != null && saved[i] != null && !now[i].toString().equalsIgnoreCase(saved[i].toString())) {
                        Bukkit.getPluginManager().callEvent((Event)new PlayerArmorUnequipEvent(player, saved[i]));
                        Bukkit.getPluginManager().callEvent((Event)new PlayerArmorEquipEvent(player, now[i]));
                    }
                }
                EventAnalyser.this.getContents().put(player.getUniqueId(), now);
            }
        }.runTaskLater((Plugin)JavaPlugin.getPlugin((Class) Main.class), 1L);
    }

    private ConcurrentMap<UUID, ItemStack[]> getContents() {
        return this.contents;
    }
}
