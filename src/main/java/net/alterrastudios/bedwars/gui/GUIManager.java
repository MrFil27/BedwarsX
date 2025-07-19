package net.alterrastudios.bedwars.gui;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GUIManager {

    private final Map<Player, GUI> playerGUIMap = new HashMap<>();

    public GUI getOpenedGUI(Player player){
        return playerGUIMap.get(player);
    }

    public void setGUI(Player player, GUI gui){
        if(gui == null){
            player.closeInventory();
            return;
        }

        playerGUIMap.put(player, gui);
        player.closeInventory();
        player.openInventory(gui.getInventory());
    }

    public void clear(Player player){
        playerGUIMap.put(player, null);
    }

}
