package com.wingsoffireserver.wingsoffire.ArmorEvent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public final class PlayerArmorUnequipEvent extends Event
{
    private static final HandlerList HANDLER_LIST;
    private final Player player;
    private final ItemStack itemStack;

    public PlayerArmorUnequipEvent(final Player player, final ItemStack itemStack) {
        this.player = player;
        this.itemStack = itemStack;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public HandlerList getHandlers() {
        return PlayerArmorUnequipEvent.HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return PlayerArmorUnequipEvent.HANDLER_LIST;
    }

    static {
        HANDLER_LIST = new HandlerList();
    }
}