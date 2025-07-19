package net.alterrastudios.bedwars.config;

import net.alterrastudios.bedwars.manager.GameManager;
import net.alterrastudios.bedwars.worlds.TeamColor;
import net.alterrastudios.bedwars.worlds.GameWorld;
import net.alterrastudios.bedwars.worlds.Island;
import net.alterrastudios.bedwars.worlds.generators.Generator;
import net.alterrastudios.bedwars.worlds.generators.GeneratorType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ConfigurationManager {

    private final GameManager gameManager;
    private final ConfigurationSection mapsConfiguration;

    public ConfigurationManager(GameManager gameManager) {
        this.gameManager = gameManager;
        FileConfiguration fileConfiguration = gameManager.getPlugin().getConfig();

        if(!fileConfiguration.isConfigurationSection("maps")) mapsConfiguration = fileConfiguration.createSection("maps");
        else mapsConfiguration = fileConfiguration.getConfigurationSection("maps");

        gameManager.getPlugin().saveConfig();
    }

    public String randomMapName(){
        String[] mapNames = mapsConfiguration.getKeys(false).toArray(new String[0]);
        if(mapNames.length == 0){
            Bukkit.getLogger().warning("[BedwarsX] No maps found in the configuration.");
            return null;
        }

        return mapNames[ThreadLocalRandom.current().nextInt(mapNames.length)];
    }

    public ConfigurationSection getMapSection(String mapName){
        if(!mapsConfiguration.isConfigurationSection(mapName)) mapsConfiguration.createSection(mapName);

        return mapsConfiguration.getConfigurationSection(mapName);
    }

    public void loadWorld(String mapName, Consumer<GameWorld> consumer){
        GameWorld gameWorld = new GameWorld(mapName);
        gameWorld.loadWorld(gameManager, true, () -> {
            ConfigurationSection mapSection = getMapSection(mapName);
            for(String key : mapSection.getKeys(false)){
                if(Arrays.stream(TeamColor.values()).anyMatch(t -> t.name().equals(key))){
                    Island island = loadIsland(gameWorld, mapSection.getConfigurationSection(key));
                    gameWorld.addIsland(island);
                }
            }

            gameWorld.setLobbyPosition(from(gameWorld.getWorld(), mapSection.getConfigurationSection("lobbySpawn")));

            consumer.accept(gameWorld);
        });
    }

    public Island loadIsland(GameWorld world, ConfigurationSection section){
        TeamColor teamColor = TeamColor.valueOf(section.getName());
        Island island = new Island(world, teamColor);

        try{
            island.setBedLocation(from(world.getWorld(), section.getConfigurationSection("bed")));
            island.setUpgradesEntityLocation(from(world.getWorld(), section.getConfigurationSection("upgradeLocation")));
            island.setShopEntityLocation(from(world.getWorld(), section.getConfigurationSection("shopEntity")));
            island.setSpawnLocation(from(world.getWorld(), section.getConfigurationSection("spawn")));
            island.setProtectedCornerOne(from(world.getWorld(), section.getConfigurationSection("protectedCornerOne")));
            island.setProtectedCornerTwo(from(world.getWorld(), section.getConfigurationSection("protectedCornerTwo")));

            island.setGenerators(loadGenerators(world, section.getConfigurationSection("generators")));
        }catch(Exception e){
            Bukkit.getServer().getLogger().severe("Invalid " + teamColor.formattedName() + " island in " + world.getConfigName());
        }

        return island;
    }

    public List<Generator> loadGenerators(GameWorld world, ConfigurationSection section){
        return section.getKeys(false).stream().map((key) -> {
            Location location = from(world.getWorld(), section.getConfigurationSection(key));
            String typeString = section.getString("type");

            if(Arrays.stream(GeneratorType.values()).noneMatch(t -> t.name().equals(typeString))) return null;
            GeneratorType type = GeneratorType.valueOf(typeString);
            return new Generator(location, type);
        }).collect(Collectors.toList());
    }

    public void saveWorld(GameWorld gameWorld){
        ConfigurationSection mapSection = getMapSection(gameWorld.getConfigName());
        ConfigurationSection lobbySection;
        if(mapSection.isConfigurationSection("lobbySpawn")) lobbySection = mapSection.getConfigurationSection("lobbySpawn");
        else lobbySection = mapSection.createSection("lobbySpawn");

        writeLocation(gameWorld.getLobbyPosition(), lobbySection);

        gameManager.getPlugin().saveConfig();
    }

    public void saveUnownedGenerator(String worldConfigName, Generator generator){
        ConfigurationSection mapSection = getMapSection(worldConfigName);

        ConfigurationSection generatorSection;
        if(mapSection.isConfigurationSection("generators")) generatorSection = mapSection.getConfigurationSection("generators");
        else generatorSection = mapSection.createSection("generators");

        ConfigurationSection section = generatorSection.createSection(String.valueOf(UUID.randomUUID().toString()));
        section.set("type", generator.getType().toString());
        writeLocation(generator.getLocation(), section.createSection("location"));

        gameManager.getPlugin().saveConfig();
    }

    public void saveIsland(Island island){
        ConfigurationSection mapSection = getMapSection(island.getGameWorld().getConfigName());

        ConfigurationSection colorSection;
        if(mapSection.isConfigurationSection(island.getColor().toString())) colorSection = mapSection.getConfigurationSection(island.getColor().toString());
        else colorSection = mapSection.createSection(island.getColor().toString());

        Map<String, Location> locationsToWrite = new HashMap<>();
        locationsToWrite.put("upgradeLocation", island.getUpgradeEntityLocation());
        locationsToWrite.put("bed", island.getBedLocation());
        locationsToWrite.put("shopEntity", island.getShopEntityLocation());
        locationsToWrite.put("spawn", island.getSpawnLocation());
        locationsToWrite.put("protectedCornerOne", island.getProtectedCornerOne());
        locationsToWrite.put("protectedCornerTwo", island.getProtectedCornerTwo());

        for(Map.Entry<String, Location> entry : locationsToWrite.entrySet()){
            ConfigurationSection section;

            if(!colorSection.isConfigurationSection(entry.getKey())) section = colorSection.createSection(entry.getKey());
            else section = colorSection.getConfigurationSection(entry.getKey());

            if(entry.getValue() != null) writeLocation(entry.getValue(), section);
        }

        colorSection.set("generators", null);
        ConfigurationSection generatorSection = colorSection.createSection("generators");

        for(Generator generator : island.getGenerators()){
            ConfigurationSection section = generatorSection.createSection(UUID.randomUUID().toString());
            section.set("type", generator.getType().toString());
            writeLocation(generator.getLocation(), section.createSection("location"));
        }

        gameManager.getPlugin().saveConfig();
    }

    public void writeLocation(Location location, ConfigurationSection section){
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
        section.set("yaw", location.getYaw());
        section.set("pitch", location.getPitch());
    }

    public Location from(World world, ConfigurationSection section){
        return new Location(world, section.getDouble("x"), section.getDouble("y"), section.getDouble("z"), (float) section.getDouble("yaw"), (float) section.getDouble("pitch"));
    }
}
