package com.wingsoffireserver.wingsoffire.Commands;

import com.wingsoffireserver.wingsoffire.ActivePlayer;
import com.wingsoffireserver.wingsoffire.Main;
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
