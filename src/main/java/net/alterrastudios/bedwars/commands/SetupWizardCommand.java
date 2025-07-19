package net.alterrastudios.bedwars.commands;

import net.alterrastudios.bedwars.manager.GameManager;
import net.alterrastudios.bedwars.worlds.GameWorld;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.alterrastudios.bedwars.BedWars.format;
import static net.alterrastudios.bedwars.config.Language.getString;

public class SetupWizardCommand implements CommandExecutor {

    private final GameManager gameManager;

    public SetupWizardCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player p)){
            sender.sendMessage(format(getString("messages.instance-denied")));
            return true;
        }

        if(!p.hasPermission("bedwars.admin")){
            p.sendMessage(format(getString("messages.no-permission")));
            return true;
        }

        if(args.length < 1){
            p.sendMessage(format(getString("messages.setup-syntax-error")));
            return true;
        }

        String mapName = args[0];

        if(mapName.equalsIgnoreCase("exit")){
            gameManager.getSetupWizardManager().removeFromWizard(p);
            return true;
        }

        p.sendMessage(format(getString("messages.wait-loading-world")));

        GameWorld world = new GameWorld(mapName);

        world.loadWorld(gameManager, false, () -> gameManager.getSetupWizardManager().activateSetupWizard(p, world));

        return true;
    }
}
