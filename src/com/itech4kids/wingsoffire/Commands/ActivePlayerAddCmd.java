package com.itech4kids.wingsoffire.Commands;

import com.itech4kids.wingsoffire.ActivePlayer;
import com.itech4kids.wingsoffire.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class ActivePlayerAddCmd implements CommandExecutor {
    Main main;

    public ActivePlayerAddCmd(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            try {
                ActivePlayer activePlayer = new ActivePlayer((Player) sender);
                main.players.put(sender.getName(), activePlayer);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
