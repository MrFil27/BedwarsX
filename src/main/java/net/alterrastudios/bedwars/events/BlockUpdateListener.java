package net.alterrastudios.bedwars.events;

import net.alterrastudios.bedwars.manager.GameManager;
import net.alterrastudios.bedwars.manager.GameState;
import net.alterrastudios.bedwars.worlds.Island;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockUpdateListener implements Listener {

    private final GameManager gameManager;

    public BlockUpdateListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        if(e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        if(gameManager.getGameState() != GameState.ACTIVE && gameManager.getGameState() != GameState.WON) return;

        if(e.getBlock().getLocation().getBlockY() > 110){
            e.setCancelled(true);
            return;
        }

        Player p = e.getPlayer();
        Material type = e.getBlock().getType();

        if(type.toString().contains("BED")){
            Location loc = e.getBlock().getLocation();
            Island island = gameManager.getGameWorld().getIslandForBedLocation(loc);

            if(island != null && island.isMember(p)) e.getBlock().setType(Material.AIR);

            e.setCancelled(true);
            return;
        }

        for(Island island : gameManager.getGameWorld().getIslands()){
            if(island.isBlockWithinProtectedZone(e.getBlock())) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        if(e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        if(gameManager.getGameState() != GameState.ACTIVE && gameManager.getGameState() != GameState.WON) return;

        if(e.getBlock().getLocation().getBlockY() > 110){
            e.setCancelled(true);
            return;
        }

        for(Island island : gameManager.getGameWorld().getIslands()){
            if(island.isBlockWithinProtectedZone(e.getBlock())) e.setCancelled(true);
        }
    }

}
