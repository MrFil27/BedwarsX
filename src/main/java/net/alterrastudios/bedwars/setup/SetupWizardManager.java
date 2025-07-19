package net.alterrastudios.bedwars.setup;

import net.alterrastudios.bedwars.ItemBuilder;
import net.alterrastudios.bedwars.manager.GameManager;
import net.alterrastudios.bedwars.worlds.TeamColor;
import net.alterrastudios.bedwars.worlds.GameWorld;
import net.alterrastudios.bedwars.worlds.Island;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static net.alterrastudios.bedwars.BedWars.format;

public class SetupWizardManager {

    public Map<Player, Island> playerToIslandMap = new HashMap<>();
    public Map<Player, GameWorld> playerToGameWorldMap = new HashMap<>();

    private final GameManager gameManager;

    public SetupWizardManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean isInWizard(Player player){
        return playerToGameWorldMap.containsKey(player);
    }

    public void activateSetupWizard(Player player, GameWorld gameWorld){
        player.getInventory().clear();
        player.setGameMode(GameMode.CREATIVE);
        player.teleport(new Location(gameWorld.getWorld(), 0, 69, 0));

        worldSetupWizard(player, gameWorld);
    }

    public void worldSetupWizard(Player player, GameWorld gameWorld){
        player.getInventory().clear();

        player.getInventory().addItem(
                new ItemBuilder(Material.DIAMOND).setName(format("&aSet Diamond Generator")).toItemStack()
        );
        player.getInventory().addItem(
                new ItemBuilder(Material.EMERALD).setName(format("&aSet Emerald Generator")).toItemStack()
        );
        player.getInventory().addItem(
                new ItemBuilder(Material.BLAZE_POWDER).setName(format("&aSet Lobby Spawn")).toItemStack()
        );
        player.getInventory().addItem(
                new ItemBuilder(Material.STICK).setName(format("&aChange Island")).toItemStack()
        );

        playerToGameWorldMap.put(player, gameWorld);
    }

    public void teamSetupWizard(Player player, TeamColor teamColor){
        player.getInventory().clear();
        player.getInventory().addItem(
                new ItemBuilder(Material.STICK).setName(format("&aFirst Corner Stick")).toItemStack()
        );
        player.getInventory().addItem(
                new ItemBuilder(Material.BLAZE_ROD).setName(format("&aSecond Corner Stick")).toItemStack()
        );
        player.getInventory().addItem(
                new ItemBuilder(Material.EGG).setName(format("&aSet Shop Location")).toItemStack()
        );
        player.getInventory().addItem(
                new ItemBuilder(Material.IRON_INGOT).setName(format("&aSet Generator Location")).toItemStack()
        );
        player.getInventory().addItem(
                new ItemBuilder(Material.DIAMOND_SWORD).setName(format("&aSet Team Upgrade Location")).toItemStack()
        );
        player.getInventory().addItem(
                new ItemBuilder(Material.BOWL).setName(format("&aSet Spawn Location")).toItemStack()
        );
        player.getInventory().addItem(
                new ItemBuilder(Material.MAGMA_CREAM).setName(format("&aSet Bed Location")).toItemStack()
        );
        player.getInventory().addItem(
                new ItemBuilder(teamColor.woolMaterial()).setName(format("&aChange island")).toItemStack()
        );
        player.getInventory().addItem(
                new ItemBuilder(Material.RED_MUSHROOM).setName(format("&aSave island")).toItemStack()
        );

        Island island = new Island(getWorld(player), teamColor);
        playerToIslandMap.put(player, island);
    }

    public GameWorld getWorld(Player player){
        return playerToGameWorldMap.get(player);
    }

    public Island getIsland(Player p){
        return playerToIslandMap.get(p);
    }

    public void removeFromWizard(Player player){
        player.teleport(new Location(Bukkit.getWorld("world"), 0, 70, 0));
    }
}
