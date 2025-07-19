package net.alterrastudios.bedwars.worlds.generators;

import net.alterrastudios.bedwars.BedWars;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class GeneratorManager {

    private final List<Generator> generators = new ArrayList<>();
    private final BedWars plugin;

    public GeneratorManager(BedWars plugin) {
        this.plugin = plugin;
    }

    public void addGenerator(Generator generator){
        generators.add(generator);
    }

    public void startGenerators(){
        for(Generator g : generators) g.setActivated(true);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for(Generator g : generators) g.spawn();
        }, 0L, 20L);
    }

    public void clearGenerators(){
        generators.clear();
    }

    public List<Generator> getGenerators(){
        return generators;
    }

}
