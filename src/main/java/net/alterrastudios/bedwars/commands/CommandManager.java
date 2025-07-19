package net.alterrastudios.bedwars.commands;

import net.alterrastudios.bedwars.BedWars;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private final BedWars plugin = BedWars.getInstance();
    private final Map<String, CommandExecutor> commands;

    public CommandManager(){
        this.commands = new HashMap<>();
    }

    public void registerCommand(String commandName, CommandExecutor executor){
        registerCommand(commandName, executor, null);
    }

    public void registerCommand(String commandName, CommandExecutor executor, TabCompleter tabCompleter){
        try{
            PluginCommand command = plugin.getCommand(commandName);
            if(command == null){
                plugin.getLogger().warning("Command '" + commandName + "' not defined in plugin.yml.");
                return;
            }

            command.setExecutor(executor);
            if(tabCompleter != null) command.setTabCompleter(tabCompleter);

            commands.put(commandName, executor);
        }catch(Exception e){
            plugin.getLogger().warning("Error registering command: " + commandName + " -> " + e.getMessage());
            e.printStackTrace();
        }
    }

    public CommandExecutor getCommandExecutor(String commandName){
        return commands.get(commandName);
    }

}
