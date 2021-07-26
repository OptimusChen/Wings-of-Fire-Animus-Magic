package com.wingsoffireserver.wingsoffire.Commands;

import com.mysql.fabric.xmlrpc.base.Array;
import com.wingsoffireserver.wingsoffire.Config;
import com.wingsoffireserver.wingsoffire.Main;
import javafx.scene.control.Tab;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.*;

public class AdminCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.isOp()){
                if (args.length == 3){
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (target == null){
                        player.sendMessage(ChatColor.RED + "An error occurred! This player isn't online!");
                        return false;
                    }

                    int i;

                    switch (args[0].toLowerCase()){
                        case "animus":
                            if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")) {
                                try {
                                    Config.setAnimus(target, Boolean.parseBoolean(args[2].toLowerCase()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                player.sendMessage(ChatColor.RED + "Value must be true/false!");
                            }
                            player.sendMessage(ChatColor.GREEN + "Successfully set " + target.getName() + "'s Animus boolean to: " + Boolean.valueOf(args[2]));
                            break;
                        case "lvl":
                            i = 0;
                            try {
                                i = Integer.parseInt(args[2]);
                            }catch (NumberFormatException e){
                                player.sendMessage(ChatColor.RED + "This isn't a valid number!");
                                return false;
                            }
                            try {
                                Config.setAnimusStudy(target, i-1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            player.sendMessage(ChatColor.GREEN + "Successfully set " + target.getName() + "'s Animus Level to: " + i);
                            break;
                        case "mana":
                            i = 0;
                            try {
                                i = Integer.parseInt(args[2]);
                            }catch (NumberFormatException e){
                                player.sendMessage(ChatColor.RED + "This isn't a valid number!");
                                return false;
                            }
                            try {
                                Config.setMana(target, i);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            player.sendMessage(ChatColor.GREEN + "Successfully set " + target.getName() + "'s mana to: " + i);
                            break;
                    }
                }else{
                    player.sendMessage(ChatColor.RED + "Invalid Arguments!");
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getAliases().contains("animusadmin")){
            if (args.length == 1){
                return Arrays.asList("animus", "lvl", "mana");
            }else if (args.length == 2){
                List<String> list;
                list = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()){
                    list.add(player.getName());
                }
                return list;
            }else if (args.length == 3){
                switch (args[0].toLowerCase()){
                    case "animus":
                        return Arrays.asList("true", "false");
                }
                return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }
}
