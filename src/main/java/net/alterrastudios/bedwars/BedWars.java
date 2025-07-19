package net.alterrastudios.bedwars;

import net.alterrastudios.bedwars.commands.CommandManager;
import net.alterrastudios.bedwars.commands.SetupWizardCommand;
import net.alterrastudios.bedwars.commands.StartCommand;
import net.alterrastudios.bedwars.config.Language;
import net.alterrastudios.bedwars.events.BlockUpdateListener;
import net.alterrastudios.bedwars.events.InventoryClickListener;
import net.alterrastudios.bedwars.events.PlayerInteractListeners;
import net.alterrastudios.bedwars.events.PlayerJoinListeners;
import net.alterrastudios.bedwars.manager.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class BedWars extends JavaPlugin {

    // --- https://www.youtube.com/watch?v=RMCv_YOGbmw --- 6:10:13

    @Override
    public void onEnable() {
        GameManager gameManager = new GameManager(this);

        Language.setup();

        for(Player player : Bukkit.getOnlinePlayers()) player.setScoreboard(gameManager.getScoreBoard());

        CommandManager cmdManager = new CommandManager();
        cmdManager.registerCommand("setup-bedwars", new SetupWizardCommand(gameManager));
        cmdManager.registerCommand("start-bedwars", new StartCommand(gameManager));

        getServer().getPluginManager().registerEvents(new PlayerJoinListeners(this, gameManager), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListeners(gameManager), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(gameManager), this);
        getServer().getPluginManager().registerEvents(new BlockUpdateListener(gameManager), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static String format(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static BedWars getInstance(){
        return BedWars.getPlugin(BedWars.class);
    }
}
