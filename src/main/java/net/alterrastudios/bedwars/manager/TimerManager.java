package net.alterrastudios.bedwars.manager;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

import static net.alterrastudios.bedwars.BedWars.format;
import static net.alterrastudios.bedwars.config.Language.getString;

public class TimerManager {

    private final GameManager gameManager;
    private BukkitTask countdown;

    public TimerManager(GameManager gameManager){
        this.gameManager = gameManager;
    }

    public void startCountdown(){
        if(countdown != null || gameManager.getGameState() != GameState.STARTING) return;

        List<Player> players = gameManager.getPlayersInLobby();
        if(players.size() < 2){
            gameManager.getPlugin().getLogger().info("Waiting for players...");
            return;
        }

        int time = 30;

        countdown = Bukkit.getScheduler().runTaskTimer(gameManager.getPlugin(), new Runnable() {
            int timeLeft = time;

            @Override
            public void run() {
                if(gameManager.getPlayersInLobby().size() < 2){
                    broadcast(format(getString("messages.countdown-stopped-missing-players")));
                    cancelCountdown();
                    return;
                }

                if(timeLeft == 0){
                    broadcast(format(getString("messages.match-started")));
                    gameManager.setGameState(GameState.ACTIVE);
                    for(Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
                    cancelCountdown();
                    return;
                }

                if(timeLeft == 30 || timeLeft == 20 || timeLeft == 10 || timeLeft <= 5){
                    if(timeLeft == 30 || timeLeft == 20 || timeLeft == 10) for(Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    else for(Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);

                    broadcast(format(getString("messages.countdown-started")
                            .replaceAll("%seconds%", String.valueOf(timeLeft))));
                }

                timeLeft--;
            }
        }, 0L, 20L);
    }

    public void cancelCountdown(){
        if(countdown != null){
            countdown.cancel();
            countdown = null;
        }
    }

    public void broadcast(String message){
        for(org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) p.sendMessage(message);
    }

}
