package net.alterrastudios.bedwars.events;

import net.alterrastudios.bedwars.BedWars;
import net.alterrastudios.bedwars.manager.GameManager;
import net.alterrastudios.bedwars.manager.GameState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

import static net.alterrastudios.bedwars.BedWars.format;
import static net.alterrastudios.bedwars.config.Language.getString;

public class PlayerJoinListeners implements Listener {

    private final BedWars plugin;
    private final GameManager gameManager;

    public PlayerJoinListeners(BedWars plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent e){
        if(gameManager.getGameState() == GameState.RESET){
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, format(getString("messages.kick-to-resetting")));
            return;
        }

        UUID uuid = e.getUniqueId();
        OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
        if(!p.isOp() && gameManager.getGameState() == GameState.PRE_LOBBY) e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, format(getString("messages.pre-lobby-kicked-not-op")));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        e.setJoinMessage(null);

        if(gameManager.getGameState() == GameState.STARTING) Bukkit.getScheduler().runTaskLater(plugin, () -> gameManager.getTimerManager().startCountdown(), 20L);

        e.getPlayer().setScoreboard(gameManager.getScoreBoard());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        e.setQuitMessage(null);

        if(gameManager.getGameState() == GameState.STARTING && gameManager.getPlayersInLobby().size() < 2){
            gameManager.getTimerManager().cancelCountdown();
            gameManager.getTimerManager().broadcast(format(getString("messages.countdown-stopped-missing-players")));
        }
    }

    @EventHandler
    public void onToggleGameMode(PlayerGameModeChangeEvent e){
        gameManager.setSpectator(e.getPlayer(), e.getNewGameMode() == GameMode.SPECTATOR);
    }

}
