package net.alterrastudios.bedwars.worlds;

import net.alterrastudios.bedwars.manager.GameManager;
import net.alterrastudios.bedwars.worlds.generators.Generator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.alterrastudios.bedwars.BedWars.format;

public class GameWorld {

    private final String name;
    private World world;

    private Location lobbyPosition;

    private final List<Island> islands = new ArrayList<>();
    private final List<Generator> generators = new ArrayList<>();

    public GameWorld(String name) {
        this.name = name;
    }

    public void loadWorld(GameManager gameManager, boolean loadIntoPlaying, Runnable runnable){
        File sourceWorldFolder = null;
        try{
            sourceWorldFolder = new File(
                    gameManager.getPlugin().getDataFolder().getCanonicalFile() + File.separator + ".." + File.separator + ".." + File.separator + name
            );
        }catch(IOException e){
            e.printStackTrace();
        }
        File destinationWorldFolder = new File(sourceWorldFolder.getPath() + (loadIntoPlaying ? "_playing" : ""));

        try{
            copyFolder(sourceWorldFolder, destinationWorldFolder);
        }catch(IOException e){
            e.printStackTrace();
        }

        if(name == null) return;

        WorldCreator worldCreator = new WorldCreator(name + (loadIntoPlaying ? "_playing" : ""));
        world = worldCreator.createWorld();

        runnable.run();
    }

    public void copyFolder(File src, File dest) throws IOException {
        if(src.isDirectory()){
            if(!dest.exists() && dest.mkdir()) Bukkit.getConsoleSender().sendMessage("Dir copied from " + src + " to " + dest + ".");

            String[] files = src.list();

            for(String file : files){
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);

                copyFolder(srcFile, destFile);
            }
        }else{
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;

            while((length = in.read(buffer)) > 0) out.write(buffer, 0, length);

            in.close();
            out.close();
        }
    }

    public void resetWorld(){
        if(world == null) return;

        String worldName = world.getName();

        Bukkit.unloadWorld(world, false);
        File file = new File(Bukkit.getServer().getWorldContainer().getAbsolutePath().replace(".", "") + world.getName());

        if(delete(file)) Bukkit.getConsoleSender().sendMessage(format("&cDeleted " + worldName));
        else Bukkit.getConsoleSender().sendMessage(format("&cFailed to delete " + worldName));
    }

    private boolean delete(File toDelete){
        File[] allContents = toDelete.listFiles();

        if(allContents != null){
            for(File file : allContents) delete(file);
        }

        return toDelete.delete();
    }

    public String getConfigName(){
        return name;
    }

    public World getWorld(){
        return world;
    }

    public Location getLobbyPosition(){
        return lobbyPosition;
    }

    public Location getSpawnForTeamColor(TeamColor teamColor){
        Optional<Island> optionalIsland = islands.stream().filter(island -> island.getColor() == teamColor).findFirst();

        return optionalIsland.map(Island::getSpawnLocation).orElse(null);
    }

    public Island getIslandForBedLocation(Location location){
        Optional<Island> islandOptional = islands.stream().filter(island -> {
            if(island.getBedLocation() == location) return true;

            Location oneExtraZ = location.add(0, 0, 1);
            if(island.getBedLocation() == oneExtraZ) return true;

            Location oneLessZ = location.add(0, 0, -1);
            if(island.getBedLocation() == oneLessZ) return true;


            Location oneExtraX = location.add(1, 0, 0);
            if(island.getBedLocation() == oneExtraX) return true;

            Location oneLessX = location.add(-1, 0, 0);
            return island.getBedLocation() == oneLessX;
        }).findFirst();

        return islandOptional.orElse(null);
    }

    public void setLobbyPosition(Location lobbyPosition){
        this.lobbyPosition = lobbyPosition;
    }

    public List<Generator> getGenerators(){
        return generators;
    }

    public void addIsland(Island island){
        islands.add(island);
    }

    public List<Island> getAliveTeams(){
        return islands.stream().filter(island -> island.isBedPlaced() && island.alivePlayerCount() != 0).collect(Collectors.toList());
    }

    public List<Island> getIslands() {
        return islands;
    }
}
