package com.wingsoffireserver.wingsoffire.Util;

import com.wingsoffireserver.wingsoffire.Config;
import com.wingsoffireserver.wingsoffire.Inventory.WoFInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryAnimus {

    public static void openTalismanEnchantingInventory(Player player, Inventory inv){
        int animusLevel = Config.getAnimusStudyLevel(player) + 1;
        WoFInventory woFInventory = new WoFInventory(inv.getSize()/9, "Animus Item Enchanting");
        woFInventory.outline();
        woFInventory.placeBackArrow(36);

        woFInventory.addItem(ItemCreator.createEnchantItem(Material.PLAYER_HEAD, ChatColor.AQUA + "Running Talisman",
                "10 per lvl", "LapisBlock", 4, true, "Putting this talisman in", "an active talisman slot will", "grant you extra walkspeed!"));
        woFInventory.addItem(ItemCreator.createEnchantItem(Material.PLAYER_HEAD, ChatColor.DARK_GRAY + "Shield Talisman",
                "10 per lvl", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE1Yzg2ODY0MTFkNDQ2YzkwYzE5MWM5M2Y4MGI5ZmZiMWNkMjQ3YWExMmEyMjZmODk3OTk4MWFkNDM4OGJlZSJ9fX0="
                , 5, "Putting this talisman in", "an active talisman slot will", "grant you extra armor points!"));
        if (animusLevel >= Levels.LVL_TWO.getValue()){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.PLAYER_HEAD, ChatColor.DARK_RED + "Strength Talisman",
                    "10 per lvl", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2IwMzNiMmMyZDNjZjU4OTEzM2FiNzMwZWQxYThiNDQzMTNkNjI5OTU0ODBjM2EwZGFjMzI4ZDUzN2UyN2Q3ZiJ9fX0=",
                    3,"Putting this talisman in", "an active talisman slot will", "grant you extra attack boost!"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.PLAYER_HEAD, ChatColor.WHITE + "Gliding Talisman",
                    "5 Mana", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzZmMzNiMjk3YTk4MWI1MjdiZWMzOTMxNjg0MDFkOGEyZWNhZGViOWYxNjAzYmE1ZTI3NmY0MmQ2NDQ3NTExNiJ9fX0=",
                    1, "Putting this talisman in", "an active talisman slot will", "grant you permanent slow-falling!"));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.RED_STAINED_GLASS_PANE, ChatColor.DARK_RED + "Strength Talisman",
                    "10 per lvl", Levels.LVL_TWO.getValue(),
                    "Putting this talisman in", "an active talisman slot will", "grant you extra attack boost!"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.RED_STAINED_GLASS_PANE, ChatColor.WHITE + "Gliding Talisman",
                    "5 Mana", Levels.LVL_TWO.getValue(),
                    "Putting this talisman in", "an active talisman slot will", "grant you permanent slow-falling!"));
        }

        if (animusLevel >= Levels.LVL_THREE.getValue()){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.PLAYER_HEAD, ChatColor.DARK_GRAY + "Stealth Talisman",
                    "10 Mana", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2IxZTAxN2I1ODQxYjk4NTc3YTJiOGVkOWJmMDIzZDNiZjE0OWQ3ZWY2Y2RkY2VmY2FkZjdiNGIyN2MzMWIzMSJ9fX0=",
                    1, "Putting this talisman in", "an active talisman slot will", "grant you permanent invisibility!"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.PLAYER_HEAD, ChatColor.YELLOW + "Glowing Talisman",
                    "5 Mana", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Q5ZDE5NWYwOTJlNDM1MDViNTQ5OWU3MzJkY2RiOWU4NTIwNjlkNWFkMzVjMTE0MzJjOTkwYWZjZmU2NDAzNyJ9fX0=",
                    1, "Putting this talisman in", "an active talisman slot will", "grant you the glowing effect!"));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.RED_STAINED_GLASS_PANE, ChatColor.DARK_GRAY + "Stealth Talisman",
                    "10 Mana", Levels.LVL_THREE.getValue(),
                    "Putting this talisman in", "an active talisman slot will", "grant you permanent invisibility!"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.RED_STAINED_GLASS_PANE, ChatColor.YELLOW + "Glowing Talisman",
                    "5 Mana", Levels.LVL_THREE.getValue(),
                    "Putting this talisman in", "an active talisman slot will", "grant you the glowing effect!"));
        }

        if (animusLevel >= Levels.LVL_FOUR.getValue()){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.ENCHANTED_BOOK, ChatColor.AQUA + "Sharpness VI",
                    "15 Mana", true, "Adds Sharpness 6 to a", "sword or enchanted", "book!"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.ENCHANTED_BOOK, ChatColor.AQUA + "Protection V",
                    "15 Mana", true, "Adds Protection 5 to a", "armor piece or enchanted", "book!"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.NETHERITE_AXE, ChatColor.RED + "Axe of Flames",
                    "10 per lvl", true, 2, "Adds fire aspect to an", "axe!"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.NETHERITE_CHESTPLATE, ChatColor.BLUE + "Heart Scales",
                    "10 per lvl", true, 2, "Adds the Heart Scales enchant", "to any armor",
                    "", "&9Heart Scales I", "  Adds +1 health point", "  whenever your equip this", "  armor piece!"));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.RED_STAINED_GLASS_PANE, ChatColor.AQUA + "Sharpness VI",
                    "15 Mana", Levels.LVL_FOUR.getValue(), "Adds Sharpness 6 to a", "sword or enchanted", "book!"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.RED_STAINED_GLASS_PANE, ChatColor.AQUA + "Protection V",
                    "15 Mana", Levels.LVL_FOUR.getValue(), "Adds Protection 5 to a", "armor piece or enchanted", "book!"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Axe of Flames",
                    "10 per lvl", Levels.LVL_FOUR.getValue(), "Adds fire aspect to an", "axe!"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.RED_STAINED_GLASS_PANE, ChatColor.BLUE + "Heart Scales",
                    "10 per lvl", Levels.LVL_FOUR.getValue(), "Adds the Heart Scales enchant", "to any armor",
                    "", "&9Heart Scales I", "  Adds +1 health point", "  whenever your equip this", "  armor piece!"));
        }

        player.openInventory(woFInventory);
    }

    public static void openWeatherEnchantingGui(Player player, Inventory inv){
        int animusLevel = Config.getAnimusStudyLevel(player) + 1;
        WoFInventory woFInventory = new WoFInventory(inv.getSize()/9, "Animus Weather Enchanting");
        woFInventory.outline();
        woFInventory.placeBackArrow(36);

        if (animusLevel >= Levels.LVL_FOUR.getValue()){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.SUNFLOWER, ChatColor.GOLD + "Part Clouds",
                    "5 Mana", false, "Clears the current weather", "and makes it a sunny day!"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.WATER_BUCKET, ChatColor.AQUA + "Rain",
                    "5 Mana", false, "Makes the current weather", "into rain!"));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.SUNFLOWER, ChatColor.GOLD + "Part Clouds",
                    "5 Mana", Levels.LVL_FOUR.getValue(), "Clears the current weather", "and makes it a sunny day!"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.WATER_BUCKET, ChatColor.BLUE + "Rain",
                    "5 Mana", Levels.LVL_FOUR.getValue(), "Makes the current weather", "into rain!"));
        }

        if (animusLevel >= Levels.LVL_FIVE.getValue()){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.COD_BUCKET, ChatColor.BLUE + "Thunderstorm",
                    "5 Mana", false, "Makes the current weather", "into a thunder storm!"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.BLAZE_ROD, ChatColor.YELLOW + "Lightning",
                    "15 Mana", false, "Summons a lightning bolt", "in front of you!"));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.COD_BUCKET, ChatColor.BLUE + "Thunderstorm",
                    "5 Mana", Levels.LVL_FIVE.getValue(), "Makes the current weather", "into a thunder storm!"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.BLAZE_ROD, ChatColor.YELLOW + "Lightning",
                    "15 Mana", Levels.LVL_FIVE.getValue(), "Summons a lightning bolt", "in front of you!"));
        }

        player.openInventory(woFInventory);
    }

    public static void openPlayerEnchantsGui(Player player, Inventory inv){
        int animusLevel = Config.getAnimusStudyLevel(player) + 1;
        WoFInventory woFInventory = new WoFInventory(inv.getSize()/9, "Animus Player Enchanting");
        woFInventory.outline();
        woFInventory.placeBackArrow(36);
        woFInventory.addItem(ItemCreator.createEnchantItem(Material.GOLDEN_APPLE, ChatColor.RED + "Health Boost",
                "2 per lvl", false, 4, "Grants the named player", "+X hearts!"));
        woFInventory.addItem(ItemCreator.createEnchantItem(Material.FEATHER, ChatColor.WHITE + "Swift Talons",
                "10 per lvl", false, 5, "Grants the named player", "speed. Also gives haste", "at lvl 4+!"));

        if (animusLevel >= Levels.LVL_TWO.getValue()){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.COBBLESTONE, ChatColor.DARK_GRAY + "Stone Scales",
                    "10 per lvl", false, 4, "Grants the named player", "resistance and slowness!"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.RABBIT_FOOT, ChatColor.GREEN + "Leaping Talons",
                    "2 per lvl", false, 3, "Grants the named player", "jump boost!"));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.COBBLESTONE, ChatColor.DARK_GRAY + "Stone Scales",
                    "10 per lvl", Levels.LVL_TWO.getValue(), "Grants the named player", "resistance and slowness!"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.RABBIT_FOOT, ChatColor.GREEN + "Leaping Talons",
                    "2 per lvl", Levels.LVL_TWO.getValue(), "Grants the named player", "jump boost!"));
        }

        if (animusLevel >= Levels.LVL_THREE.getValue()){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.MAGMA_BLOCK, ChatColor.GOLD + "Magma Scales",
                    "10 per lvl", false, 2, "Grants the named player", "fire resistance and slowness.",
                    "Also gives permanent fire", "aspect to named player's", "items at lvl 2+!"));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.MAGMA_BLOCK, ChatColor.GREEN + "Magma Scales",
                    "10 per lvl", Levels.LVL_THREE.getValue(), "Grants the named player", "fire resistance and slowness.",
                    "Also gives permanent fire", "aspect to named player's", "items at lvl 2+!"));
        }

        if (animusLevel >= Levels.LVL_FOUR.getValue()){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.GOLDEN_CARROT, ChatColor.RED + "Rejuvenation",
                    "5 per lvl", false, 2, "Grants the named player instant health!"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.MILK_BUCKET, ChatColor.YELLOW + "Cure Disease",
                    "10 Mana", false, "Removes Wither, Poison", "and Hunger from the", "named player!"));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.GOLDEN_CARROT, ChatColor.RED + "Rejuvenation",
                    "5 per lvl", Levels.LVL_FOUR.getValue(), "Grants the named player instant health!"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.MILK_BUCKET, ChatColor.YELLOW + "Cure Disease",
                    "10 Mana", Levels.LVL_FOUR.getValue(), "Removes Wither, Poison", "and Hunger from the", "named player!"));
        }

        if (animusLevel >= Levels.LVL_FIVE.getValue()){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.DIAMOND_ORE, ChatColor.AQUA + "Gentle Talons",
                    "20 Mana", false, "Gives the named player", "infinite Silk Touch."));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.SLIME_SPAWN_EGG, ChatColor.GREEN + "Species Shift",
                    "15 Mana", false, "Change your skin to", "another tribe!"));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.DIAMOND_ORE, ChatColor.AQUA + "Gentle Talons",
                    "20 Mana", Levels.LVL_FIVE.getValue(), "Gives the named player", "infinite Silk Touch."));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.SLIME_SPAWN_EGG, ChatColor.GREEN + "Species Shift",
                    "15 Mana", Levels.LVL_FIVE.getValue(), "Change your skin to", "another tribe!"));
        }

        player.openInventory(woFInventory);
    }

    public static void openAnimusCursingGui(Player player, Inventory inv){
        int animusLevel = Config.getAnimusStudyLevel(player) + 1;
        WoFInventory woFInventory = new WoFInventory(inv.getSize()/9, "Animus Curse Enchanting");
        woFInventory.outline();
        woFInventory.placeBackArrow(36);

        if (animusLevel >= Levels.LVL_FOUR.getValue()){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.ENCHANTED_BOOK, ChatColor.RED + "Curse of Binding",
                    "10 Mana", true, "Adds Curse of Binding to an", "item."));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.ENCHANTED_BOOK, ChatColor.RED + "Curse of Vanishing",
                    "10 Mana", true, "Adds Curse of Vanishing to an", "item."));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.ENCHANTED_BOOK, ChatColor.DARK_GRAY + "Curse of Sickness",
                    "15 Mana", true, "The named player get poison", "for 10 seconds."));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.ENCHANTED_BOOK, ChatColor.RED + "Curse of Binding",
                    "10 Mana", Levels.LVL_FOUR.getValue(), "Adds Curse of Binding to an", "item."));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.ENCHANTED_BOOK, ChatColor.RED + "Curse of Vanishing",
                    "10 Mana", Levels.LVL_FOUR.getValue(), "Adds Curse of Vanishing to an", "item."));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.ENCHANTED_BOOK, ChatColor.DARK_GRAY + "Curse of Sickness",
                    "15 Mana", Levels.LVL_FOUR.getValue(), "The named player get poison", "for 10 seconds."));
        }

        if (animusLevel >= Levels.LVL_FIVE.getValue()){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.SPIDER_EYE, ChatColor.GRAY + "Curse of Degradatioin",
                    "40 Mana", false, "The named player gets permanent", "-3 hearts"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.ROTTEN_FLESH, ChatColor.GRAY + "Curse of Undead",
                    "30 Mana", false, "The named player catches fire", "while under sunlight"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.BLACK_DYE, ChatColor.GRAY + "Curse of Blindness",
                    "30 Mana", false, "The named player gets the", "blindness effect."));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.SPIDER_EYE, ChatColor.GRAY + "Curse of Degradatioin",
                    "40 Mana", Levels.LVL_FIVE.getValue(), "The named player gets permanent", "-3 hearts"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.ROTTEN_FLESH, ChatColor.GRAY + "Curse of Undead",
                    "30 Mana", Levels.LVL_FIVE.getValue(), "The named player catches fire", "while under sunlight"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.BLACK_DYE, ChatColor.GRAY + "Curse of Blindness",
                    "30 Mana", Levels.LVL_FIVE.getValue(), "The named player gets the", "blindness effect."));
        }

        player.openInventory(woFInventory);
    }

}
