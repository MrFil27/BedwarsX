package net.alterrastudios.bedwars.commands;

import net.alterrastudios.bedwars.manager.GameManager;
import net.alterrastudios.bedwars.manager.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static net.alterrastudios.bedwars.BedWars.format;
import static net.alterrastudios.bedwars.config.Language.getString;

public class StartCommand implements CommandExecutor {

    private final GameManager gameManager;

    public StartCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.hasPermission("bedwars.admin")) gameManager.setGameState(GameState.STARTING);
        else commandSender.sendMessage(format(getString("messages.no-permission")));

        return true;
    }
}
