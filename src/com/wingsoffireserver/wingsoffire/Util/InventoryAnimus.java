package com.wingsoffireserver.wingsoffire.Util;

import com.wingsoffireserver.wingsoffire.Config;
import com.wingsoffireserver.wingsoffire.Inventory.WoFInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryAnimus {

    private static int firstLvl = 1;
    private static int secondLvl = 2;
    private static int thirdLvl = 3;
    private static int fourthLvl = 4;
    private static int fifthLvl = 5;


    public static void openTalismanEnchantingInventory(Player player, Inventory inv){
        int animusLevel = Config.getAnimusStudyLevel(player) + 1;
        WoFInventory woFInventory = new WoFInventory(inv.getSize()/9, "Animus Item Enchanting");
        woFInventory.outline();
        woFInventory.placeBackArrow(36);

        woFInventory.addItem(ItemCreator.createEnchantItem(Material.PLAYER_HEAD, ChatColor.AQUA + "Running Talisman",
                "10 per lvl", "LapisBlock", true, "Putting this talisman in", "an active talisman slot will", "grant you extra walkspeed!"));
        woFInventory.addItem(ItemCreator.createEnchantItem(Material.PLAYER_HEAD, ChatColor.DARK_GRAY + "Shield Talisman",
                "10 per lvl", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE1Yzg2ODY0MTFkNDQ2YzkwYzE5MWM5M2Y4MGI5ZmZiMWNkMjQ3YWExMmEyMjZmODk3OTk4MWFkNDM4OGJlZSJ9fX0=",
                "Putting this talisman in", "an active talisman slot will", "grant you extra armor points!"));
        if (animusLevel >= secondLvl){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.PLAYER_HEAD, ChatColor.DARK_RED + "Strength Talisman",
                    "10 per lvl", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2IwMzNiMmMyZDNjZjU4OTEzM2FiNzMwZWQxYThiNDQzMTNkNjI5OTU0ODBjM2EwZGFjMzI4ZDUzN2UyN2Q3ZiJ9fX0=",
                    "Putting this talisman in", "an active talisman slot will", "grant you extra attack boost!"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.PLAYER_HEAD, ChatColor.WHITE + "Gliding Talisman",
                    "5 Mana", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzZmMzNiMjk3YTk4MWI1MjdiZWMzOTMxNjg0MDFkOGEyZWNhZGViOWYxNjAzYmE1ZTI3NmY0MmQ2NDQ3NTExNiJ9fX0=",
                    "Putting this talisman in", "an active talisman slot will", "grant you permanent slow-falling!"));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "[NOT UNLOCKED] Strength Talisman",
                    "10 per lvl", secondLvl,
                    "Putting this talisman in", "an active talisman slot will", "grant you extra attack boost!"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "[NOT UNLOCKED] Gliding Talisman",
                    "5 Mana", secondLvl,
                    "Putting this talisman in", "an active talisman slot will", "grant you permanent slow-falling!"));
        }

        if (animusLevel >= thirdLvl){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.PLAYER_HEAD, ChatColor.DARK_GRAY + "Stealth Talisman",
                    "10 Mana", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2IxZTAxN2I1ODQxYjk4NTc3YTJiOGVkOWJmMDIzZDNiZjE0OWQ3ZWY2Y2RkY2VmY2FkZjdiNGIyN2MzMWIzMSJ9fX0=",
                    "Putting this talisman in", "an active talisman slot will", "grant you permanent invisibility!"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.PLAYER_HEAD, ChatColor.YELLOW + "Glowing Talisman",
                    "5 Mana", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Q5ZDE5NWYwOTJlNDM1MDViNTQ5OWU3MzJkY2RiOWU4NTIwNjlkNWFkMzVjMTE0MzJjOTkwYWZjZmU2NDAzNyJ9fX0=",
                    "Putting this talisman in", "an active talisman slot will", "grant you the glowing effect!"));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "[NOT UNLOCKED] Stealth Talisman",
                    "10 Mana", thirdLvl,
                    "Putting this talisman in", "an active talisman slot will", "grant you permanent invisibility!"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "[NOT UNLOCKED] Glowing Talisman",
                    "5 Mana", thirdLvl,
                    "Putting this talisman in", "an active talisman slot will", "grant you the glowing effect!"));
        }

        if (animusLevel >= fourthLvl){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.ENCHANTED_BOOK, ChatColor.AQUA + "Sharpness VI",
                    "15 Mana", true, "Adds Sharpness 6 to a", "sword or enchanted", "book!"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.ENCHANTED_BOOK, ChatColor.AQUA + "Protection V",
                    "15 Mana", true, "Adds Protection 5 to a", "armor piece or enchanted", "book!"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.NETHERITE_AXE, ChatColor.RED + "Axe of Flames",
                    "10 per lvl", true, "Adds fire aspect to an", "axe!"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.NETHERITE_CHESTPLATE, ChatColor.BLUE + "Heart Scales",
                    "10 per lvl", true, "Adds the Heart Scales enchant", "to any armor",
                    "", "&1Heart Scales I", "  Adds +1 health point", "whenever your equip this", "armor piece!"));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "[NOT UNLOCKED] Sharpness VI",
                    "15 Mana", fourthLvl, "Adds Sharpness 6 to a", "sword or enchanted", "book!"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "[NOT UNLOCKED] Protection V",
                    "15 Mana", fourthLvl, "Adds Protection 5 to a", "armor piece or enchanted", "book!"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "[NOT UNLOCKED] Axe of Flames",
                    "10 per lvl", fourthLvl, "Adds fire aspect to an", "axe!"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "[NOT UNLOCKED] Heart Scales",
                    "10 per lvl", fourthLvl, "Adds the Heart Scales enchant", "to any armor",
                    "", "&1Heart Scales I", "  Adds +1 health point", "whenever your equip this", "armor piece!"));
        }

        player.openInventory(woFInventory);
    }

    public static void openWeatherEnchantingGui(Player player, Inventory inv){
        int animusLevel = Config.getAnimusStudyLevel(player) + 1;
        WoFInventory woFInventory = new WoFInventory(inv.getSize()/9, "Animus Weather Enchanting");
        woFInventory.outline();
        woFInventory.placeBackArrow(36);

        if (animusLevel >= fourthLvl){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.SUNFLOWER, ChatColor.GOLD + "Part Clouds",
                    "5 Mana", false, "Clears the current weather", "and makes it a sunny day!"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.WATER_BUCKET, ChatColor.AQUA + "Rain",
                    "5 Mana", false, "Makes the current weather", "into rain!"));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.SUNFLOWER, ChatColor.GOLD + "Part Clouds",
                    "5 Mana", fourthLvl, "Clears the current weather", "and makes it a sunny day!"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.WATER_BUCKET, ChatColor.BLUE + "Rain",
                    "5 Mana", fourthLvl, "Makes the current weather", "into rain!"));
        }

        if (animusLevel >= fifthLvl){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.COD_BUCKET, ChatColor.BLUE + "Thunderstorm",
                    "5 Mana", false, "Makes the current weather", "into a thunder storm!"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.BLAZE_ROD, ChatColor.YELLOW + "Lightning",
                    "15 Mana", false, "Summons a lightning bolt", "in front of you!"));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.COD_BUCKET, ChatColor.BLUE + "Thunderstorm",
                    "5 Mana", fifthLvl, "Makes the current weather", "into a thunder storm!"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.BLAZE_ROD, ChatColor.YELLOW + "Lightning",
                    "15 Mana", fifthLvl, "Summons a lightning bolt", "in front of you!"));
        }

        player.openInventory(woFInventory);
    }

    public static void openPlayerEnchantsGui(Player player, Inventory inv){
        int animusLevel = Config.getAnimusStudyLevel(player) + 1;
        WoFInventory woFInventory = new WoFInventory(inv.getSize()/9, "Animus Player Enchanting");
        woFInventory.outline();
        woFInventory.placeBackArrow(36);
        woFInventory.addItem(ItemCreator.createEnchantItem(Material.GOLDEN_APPLE, ChatColor.RED + "Health Boost",
                "2 per lvl", false, "Grants the named player", "+X hearts!"));
        woFInventory.addItem(ItemCreator.createEnchantItem(Material.FEATHER, ChatColor.WHITE + "Swift Talons",
                "10 per lvl", false, "Grants the named player speed. Also gives haste", "at lvl 4+!"));

        if (animusLevel >= secondLvl){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.COBBLESTONE, ChatColor.DARK_GRAY + "Stone Scales",
                    "10 per lvl", false, "Grants the named player", "resistance and slowness!"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.RABBIT_FOOT, ChatColor.GREEN + "Leaping Talons",
                    "2 per lvl", false, "Grants the named player", "jump boost!"));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.COBBLESTONE, ChatColor.DARK_GRAY + "Stone Scales",
                    "10 per lvl", secondLvl, "Grants the named player", "resistance and slowness!"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.RABBIT_FOOT, ChatColor.GREEN + "Leaping Talons",
                    "2 per lvl", secondLvl, "Grants the named player", "jump boost!"));
        }

        if (animusLevel >= thirdLvl){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.MAGMA_BLOCK, ChatColor.GOLD + "Magma Scales",
                    "10 per lvl", false, "Grants the named player", "fire resistance and slowness.",
                    "Also gives permanent fire", "aspect to named player's", "items at lvl 2+!"));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.MAGMA_BLOCK, ChatColor.GREEN + "Magma Scales",
                    "10 per lvl", thirdLvl, "Grants the named player", "fire resistance and slowness.",
                    "Also gives permanent fire", "aspect to named player's", "items at lvl 2+!"));
        }

        if (animusLevel >= fourthLvl){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.GOLDEN_CARROT, ChatColor.RED + "Rejuvenation",
                    "5 per lvl", false, "Grants the named player instant health!"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.MILK_BUCKET, ChatColor.YELLOW + "Cure Disease",
                    "10 Mana", false, "Removes Wither, Poison", "and Hunger from the", "named player!"));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.GOLDEN_CARROT, ChatColor.RED + "Rejuvenation",
                    "5 per lvl", fourthLvl, "Grants the named player instant health!"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.MILK_BUCKET, ChatColor.YELLOW + "Cure Disease",
                    "10 Mana", fourthLvl, "Removes Wither, Poison", "and Hunger from the", "named player!"));
        }

        if (animusLevel >= fifthLvl){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.DIAMOND_ORE, ChatColor.AQUA + "Gentle Talons",
                    "20 Mana", false, "Gives the named player", "infinite Silk Touch."));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.SLIME_SPAWN_EGG, ChatColor.GREEN + "Species Shift",
                    "15 Mana", false, "Change your skin to", "another tribe!"));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.DIAMOND_ORE, ChatColor.AQUA + "Gentle Talons",
                    "20 Mana", fifthLvl, "Gives the named player", "infinite Silk Touch."));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.SLIME_SPAWN_EGG, ChatColor.GREEN + "Species Shift",
                    "15 Mana", fifthLvl, "Change your skin to", "another tribe!"));
        }

        player.openInventory(woFInventory);
    }

    public static void openAnimusCursingGui(Player player, Inventory inv){
        int animusLevel = Config.getAnimusStudyLevel(player) + 1;
        WoFInventory woFInventory = new WoFInventory(inv.getSize()/9, "Animus Curse Enchanting");
        woFInventory.outline();
        woFInventory.placeBackArrow(36);

        if (animusLevel >= fourthLvl){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.ENCHANTED_BOOK, ChatColor.RED + "Curse of Binding",
                    "10 Mana", true, "Adds Curse of Binding to an", "item."));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.ENCHANTED_BOOK, ChatColor.RED + "Curse of Vanishing",
                    "10 Mana", true, "Adds Curse of Vanishing to an", "item."));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.ENCHANTED_BOOK, ChatColor.DARK_GRAY + "Curse of Sickness",
                    "15 Mana", true, "The named player get poison", "for 10 seconds."));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.ENCHANTED_BOOK, ChatColor.RED + "Curse of Binding",
                    "10 Mana", fourthLvl, "Adds Curse of Binding to an", "item."));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.ENCHANTED_BOOK, ChatColor.RED + "Curse of Vanishing",
                    "10 Mana", fourthLvl, "Adds Curse of Vanishing to an", "item."));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.ENCHANTED_BOOK, ChatColor.DARK_GRAY + "Curse of Sickness",
                    "15 Mana", fourthLvl, "The named player get poison", "for 10 seconds."));
        }

        if (animusLevel >= fifthLvl){
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.SPIDER_EYE, ChatColor.GRAY + "Curse of Degradatioin",
                    "40 Mana", false, "The named player gets permanent", "-3 hearts"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.ROTTEN_FLESH, ChatColor.GRAY + "Curse of Undead",
                    "30 Mana", false, "The named player catches fire", "while under sunlight"));
            woFInventory.addItem(ItemCreator.createEnchantItem(Material.BLACK_DYE, ChatColor.GRAY + "Curse of Blindness",
                    "30 Mana", false, "The named player gets the", "blindness effect."));
        }else{
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.SPIDER_EYE, ChatColor.GRAY + "Curse of Degradatioin",
                    "40 Mana", fifthLvl, "The named player gets permanent", "-3 hearts"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.ROTTEN_FLESH, ChatColor.GRAY + "Curse of Undead",
                    "30 Mana", fifthLvl, "The named player catches fire", "while under sunlight"));
            woFInventory.addItem(ItemCreator.createNotUnlockedItem(Material.BLACK_DYE, ChatColor.GRAY + "Curse of Blindness",
                    "30 Mana", fifthLvl, "The named player gets the", "blindness effect."));
        }

        player.openInventory(woFInventory);
    }

}
