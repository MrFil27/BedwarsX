package net.alterrastudios.bedwars.events;

import net.alterrastudios.bedwars.gui.GUI;
import net.alterrastudios.bedwars.manager.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryClickListener implements Listener {

    private final GameManager gameManager;

    public InventoryClickListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e.getCurrentItem() == null) return;
        if(!e.getCurrentItem().hasItemMeta()) return;

        Player p = (Player) e.getWhoClicked();

        GUI gui = gameManager.getGuiManager().getOpenedGUI(p);
        if(gui == null) return;

        //e.setCancelled(true);

        GUI newGui = gui.handleClick(p, e.getCurrentItem(), e.getView());

        e.getView().close();

        gameManager.getGuiManager().setGUI(p, newGui);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e){
        Player p = (Player) e.getPlayer();

        //gameManager.getGuiManager().clear(p);
    }

}
