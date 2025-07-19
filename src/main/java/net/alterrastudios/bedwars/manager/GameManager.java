package net.alterrastudios.bedwars.manager;

import net.alterrastudios.bedwars.BedWars;
import net.alterrastudios.bedwars.config.ConfigurationManager;
import net.alterrastudios.bedwars.gui.GUIManager;
import net.alterrastudios.bedwars.setup.SetupWizardManager;
import net.alterrastudios.bedwars.worlds.GameWorld;
import net.alterrastudios.bedwars.worlds.Island;
import net.alterrastudios.bedwars.worlds.generators.GeneratorManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static net.alterrastudios.bedwars.BedWars.format;
import static net.alterrastudios.bedwars.config.Language.getString;

public class GameManager {

    private final BedWars plugin;
    private final Scoreboard scoreboard;
    private final Objective objective;

    private final SetupWizardManager setupWizardManager;
    private final ConfigurationManager configurationManager;
    private final GUIManager guiManager;

    private GameWorld gameWorld;

    private GameState gameState;

    private final TimerManager timerManager;
    private final GeneratorManager generatorManager;

    public GameManager(BedWars plugin) {
        this.plugin = plugin;

        this.setupWizardManager = new SetupWizardManager(this);
        this.configurationManager = new ConfigurationManager(this);
        this.guiManager = new GUIManager();

        this.configurationManager.loadWorld(this.configurationManager.randomMapName(), gameWorld -> {
            this.gameWorld = gameWorld;
            setGameState(GameState.LOBBY);
        });

        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("bedwars", "dummy", "BedWars");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.timerManager = new TimerManager(this);
        this.generatorManager = new GeneratorManager(plugin);
    }

    public void setGameState(GameState gameState){
        this.gameState = gameState;

        switch(gameState){
            case STARTING:
                for(Player p : Bukkit.getOnlinePlayers()) p.teleport(gameWorld.getLobbyPosition());

                timerManager.startCountdown();

                break;
            case ACTIVE:
                assignPlayersToTeams();
                generatorManager.startGenerators();
                break;
            case WON:
                // announce winner
                break;
            case RESET:
                for(Player player : Bukkit.getOnlinePlayers()) player.kickPlayer(format(getString("messages.kick-to-restarting")));

                Bukkit.getServer().getScheduler().runTaskLater(plugin, bukkitTask -> {
                    gameWorld.resetWorld();
                    Bukkit.getServer().shutdown();
                }, 20);
                break;
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public Scoreboard getScoreBoard(){
        return scoreboard;
    }

    public void setSpectator(Player player, boolean isSpectator){
        if(isSpectator){
            Score score = objective.getScore("Morto");
            score.setScore(0);
        }else scoreboard.resetScores("Morto");

        player.setScoreboard(scoreboard);
    }

    public BedWars getPlugin(){
        return plugin;
    }

    public SetupWizardManager getSetupWizardManager() {
        return setupWizardManager;
    }

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public GUIManager getGuiManager() {
        return guiManager;
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public TimerManager getTimerManager() {
        return timerManager;
    }

    public GeneratorManager getGeneratorManager() {
        return generatorManager;
    }

    public List<Player> getPlayersInLobby(){
        Location lobby = gameWorld.getLobbyPosition();
        World world = lobby.getWorld();

        return Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.getWorld().equals(world))
                .collect(Collectors.toList());
    }

    public void assignPlayersToTeams(){
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        List<Island> islands = gameWorld.getIslands();

        islands.forEach(island -> island.getPlayers().clear());

        Collections.shuffle(islands);

        int maxTeams = islands.size();
        int playerCount = players.size();

        for(int i = 0; i < playerCount; i++){
            Player player = players.get(i);
            Island island = islands.get(i % maxTeams);

            island.getPlayers().add(player);

            Location spawn = island.getSpawnLocation();
            if(spawn != null) player.teleport(spawn);
            else player.sendMessage(format("&cIsland spawn not configured."));
        }
    }

}
