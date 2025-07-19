package net.alterrastudios.bedwars.worlds.generators;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class Generator {

    private final Location location;
    private final GeneratorType type;

    private boolean activated = false;

    public Generator(Location location, GeneratorType type) {
        this.location = location;
        this.type = type;
    }

    public Location getLocation(){
        return location;
    }

    public GeneratorType getType() {
        return type;
    }

    public boolean getActivated(){
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public void spawn(){
        if(!activated) return;
        Item item = (Item) location.getWorld().spawnEntity(location, EntityType.DROPPED_ITEM);

        switch(type){
            case IRON:
                item.setItemStack(new ItemStack(Material.IRON_INGOT));
                break;
            case GOLD:
                item.setItemStack(new ItemStack(Material.GOLD_INGOT));
            case DIAMOND:
                item.setItemStack(new ItemStack(Material.DIAMOND));
                break;
            case EMERALD:
                item.setItemStack(new ItemStack(Material.EMERALD));
                break;
        }
    }

}
