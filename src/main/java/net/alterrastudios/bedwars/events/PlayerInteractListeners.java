package net.alterrastudios.bedwars.events;

import net.alterrastudios.bedwars.gui.SetupIslandWizardSelectionGUI;
import net.alterrastudios.bedwars.manager.GameManager;
import net.alterrastudios.bedwars.worlds.GameWorld;
import net.alterrastudios.bedwars.worlds.Island;
import net.alterrastudios.bedwars.worlds.generators.Generator;
import net.alterrastudios.bedwars.worlds.generators.GeneratorType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import static net.alterrastudios.bedwars.BedWars.format;
import static net.alterrastudios.bedwars.config.Language.getString;

public class PlayerInteractListeners implements Listener {

    private final GameManager gameManager;

    public PlayerInteractListeners(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(!e.hasItem()) return;
        Player p = e.getPlayer();
        if(!gameManager.getSetupWizardManager().isInWizard(p)) return;
        if(e.getItem() == null) return;
        if(!e.getItem().hasItemMeta()) return;
        if(e.getItem().getItemMeta() == null) return;

        String itemName = e.getItem().getItemMeta().getDisplayName();
        itemName = ChatColor.stripColor(itemName);

        Location current = p.getLocation();

        Location clicked = null;
        if(e.getClickedBlock() != null) clicked = e.getClickedBlock().getLocation();

        Island island = gameManager.getSetupWizardManager().getIsland(p);

        switch(itemName.toLowerCase()){
            case "set diamond generator":
                Generator diamondGenerator = new Generator(current, GeneratorType.DIAMOND);
                gameManager.getConfigurationManager().saveUnownedGenerator(p.getWorld().getName(), diamondGenerator);
                p.sendMessage(format(getString("messages.diamond-set")));
                break;
            case "set emerald generator":
                Generator emeraldGenerator = new Generator(current, GeneratorType.EMERALD);
                gameManager.getConfigurationManager().saveUnownedGenerator(p.getWorld().getName(), emeraldGenerator);
                p.sendMessage(format(getString("messages.emerald-set")));
                break;
            case "change island":
                SetupIslandWizardSelectionGUI gui = new SetupIslandWizardSelectionGUI(gameManager);
                gameManager.getGuiManager().setGUI(p, gui);
                p.sendMessage(format(getString("messages.island-changed")));
                break;
            case "first corner stick":
                if(clicked != null) island.setProtectedCornerOne(clicked);
                p.sendMessage(format(getString("messages.first-corner-set")));
                break;
            case "second corner stick":
                if(clicked != null) island.setProtectedCornerTwo(clicked);
                p.sendMessage(format(getString("messages.first-second-set")));
                break;
            case "set shop location":
                island.setShopEntityLocation(current);
                p.sendMessage(format(getString("messages.shop-set")));
                break;
            case "set generator location":
                {
                    Generator islandGenerator = new Generator(current, GeneratorType.IRON);
                    island.addGenerator(islandGenerator);
                }
                {
                    Generator islandGenerator = new Generator(current, GeneratorType.GOLD);
                    island.addGenerator(islandGenerator);
                }
                {
                    Generator islandGenerator = new Generator(current, GeneratorType.EMERALD);
                    island.addGenerator(islandGenerator);
                }
                p.sendMessage(format(getString("messages.island-generator-set")));
                break;
            case "set team upgrade location":
                island.setUpgradesEntityLocation(current);
                p.sendMessage(format(getString("messages.team-upgrade-set")));
                break;
            case "set spawn location":
                island.setSpawnLocation(current);
                p.sendMessage(format(getString("messages.spawn-location-set")));
                break;
            case "set bed location":
                if(clicked != null) island.setBedLocation(current);
                p.sendMessage(format(getString("messages.bed-location-set")));
                break;
            case "set lobby spawn":
                GameWorld gameWorld = gameManager.getSetupWizardManager().getWorld(p);
                gameWorld.setLobbyPosition(current);
                gameManager.getConfigurationManager().saveWorld(gameWorld);

                p.sendMessage(format(getString("messages.lobby-spawn-location-set")));
                break;
            case "save island":
                gameManager.getConfigurationManager().saveIsland(island);
                Bukkit.getServer().getScheduler().runTaskLater(gameManager.getPlugin(), bukkitTask -> gameManager.getSetupWizardManager().worldSetupWizard(p, island.getGameWorld()), 4);
        }

        e.setCancelled(true);
    }

}
