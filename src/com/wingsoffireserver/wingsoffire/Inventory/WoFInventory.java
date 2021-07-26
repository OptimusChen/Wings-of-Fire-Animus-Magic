package com.wingsoffireserver.wingsoffire.Inventory;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryCustom;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WoFInventory extends CraftInventoryCustom {

    private static int rowSize = 9;

    public WoFInventory(int rows, String title) {
        super(null, rows*rowSize, title);
    }

    public void fill(){
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        fill(item);
    }

    public void fill(ItemStack item){
        for (int i = 0; i < getSize(); i++){
            setItem(i, item);
        }
    }

    public void outline(ItemStack item){
        int rows = getSize()/rowSize;

        /*
        Set the perimeter of a gui to a certain item
         */
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < rows; j++) {
                if ((i == 0) ||
                    (j == 0) ||
                    (i == (rowSize-1)) ||
                    (j == (rows-1))){
                    setItem(j*rowSize+i, item);
                }
            }
        }
    }

    public void outline(){
        ItemStack item = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        outline(item);
    }
}
