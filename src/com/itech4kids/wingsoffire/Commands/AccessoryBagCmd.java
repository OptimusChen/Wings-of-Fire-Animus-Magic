package com.itech4kids.wingsoffire.Commands;

import com.itech4kids.wingsoffire.ActivePlayer;
import com.itech4kids.wingsoffire.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AccessoryBagCmd implements CommandExecutor {
    Main main;

    public AccessoryBagCmd(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            ActivePlayer activePlayer = main.getActivePlayer(player.getName());
            player.openInventory(activePlayer.accessoryBag);
        }
        return false;
    }
}
