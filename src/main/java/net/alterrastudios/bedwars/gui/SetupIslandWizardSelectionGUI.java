package net.alterrastudios.bedwars.gui;

import net.alterrastudios.bedwars.ItemBuilder;
import net.alterrastudios.bedwars.manager.GameManager;
import net.alterrastudios.bedwars.worlds.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import static net.alterrastudios.bedwars.BedWars.format;
import static net.alterrastudios.bedwars.config.Language.getString;

public class SetupIslandWizardSelectionGUI implements GUI{

    private final GameManager gameManager;
    private Inventory inventory;

    public SetupIslandWizardSelectionGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.inventory = Bukkit.createInventory(null, 27, format(getString("menus.select-island")));

        for(TeamColor teamColor : TeamColor.values()){
            inventory.addItem(
                    new ItemBuilder(teamColor.woolMaterial()).setName(format(teamColor.formattedName())).toItemStack()
            );
        }

        inventory.addItem(new ItemBuilder(Material.BARRIER).setName(format(getString("menus.exit-item"))).toItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public String getName() {
        return format(getString("menus.select-island"));
    }

    @Override
    public GUI handleClick(Player player, ItemStack itemStack, InventoryView view) {
        if(!gameManager.getSetupWizardManager().isInWizard(player)) return null;

        assert itemStack.getItemMeta() != null;
        TeamColor clickedColor = null;
        String itemName = ChatColor.stripColor(itemStack.getItemMeta().getDisplayName());

        for(TeamColor teamColor : TeamColor.values()){
            if(itemName.equalsIgnoreCase(teamColor.formattedName())){
                clickedColor = teamColor;
                break;
            }
        }

        if(clickedColor != null) gameManager.getSetupWizardManager().teamSetupWizard(player, clickedColor);
        else gameManager.getSetupWizardManager().worldSetupWizard(player, gameManager.getSetupWizardManager().getWorld(player));

        return null;
    }

    @Override
    public boolean isInventory(InventoryView view) {
        return view.getTitle().equals(getName());
    }
}
