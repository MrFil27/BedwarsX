package net.alterrastudios.bedwars.worlds;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import net.alterrastudios.bedwars.worlds.generators.Generator;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Island {

    private GameWorld gameWorld;
    private final TeamColor teamColor;

    private final List<Player> players = new ArrayList<>();

    private Location protectedCornerOne = null;
    private Location protectedCornerTwo = null;

    private Location upgradesEntityLocation = null;
    private Location shopEntityLocation = null;
    private Location bedLocation = null;

    private Location spawnLocation = null;

    private List<Generator> generatorList = new ArrayList<>();

    public Island(GameWorld gameWorld, TeamColor teamColor) {
        this.gameWorld = gameWorld;
        this.teamColor = teamColor;
    }

    public Location getProtectedCornerOne() {
        return protectedCornerOne;
    }

    public Location getProtectedCornerTwo() {
        return protectedCornerTwo;
    }

    public Location getShopEntityLocation() {
        return shopEntityLocation;
    }

    public Location getBedLocation() {
        return bedLocation;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public GameWorld getGameWorld(){
        return gameWorld;
    }

    public Location getUpgradeEntityLocation(){
        return upgradesEntityLocation;
    }

    public List<Generator> getGenerators(){
        return generatorList;
    }

    public void setProtectedCornerOne(Location protectedCornerOne) {
        this.protectedCornerOne = protectedCornerOne;
    }

    public void setProtectedCornerTwo(Location protectedCornerTwo) {
        this.protectedCornerTwo = protectedCornerTwo;
    }

    public void setShopEntityLocation(Location shopEntityLocation) {
        this.shopEntityLocation = shopEntityLocation;
    }

    public void setBedLocation(Location bedLocation) {
        this.bedLocation = bedLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void setUpgradesEntityLocation(Location upgradesEntityLocation) {
        this.upgradesEntityLocation = upgradesEntityLocation;
    }

    public void setGenerators(List<Generator> generators){
        this.generatorList = generators;
    }

    public void addGenerator(Generator generator){
        generatorList.add(generator);
    }

    public boolean isBlockWithinProtectedZone(Block block){
        Location blockLocation = block.getLocation();

        BlockVector3 one = BlockVector3.at(protectedCornerOne.getX(), protectedCornerOne.getY(), protectedCornerOne.getBlockZ());
        BlockVector3 two = BlockVector3.at(protectedCornerTwo.getX(), protectedCornerTwo.getY(), protectedCornerTwo.getBlockZ());
        CuboidRegion region = new CuboidRegion(one, two);

        return region.contains(BlockVector3.at(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ()));
    }

    public TeamColor getColor() {
        return teamColor;
    }

    public int alivePlayerCount(){
        return players.stream().filter(player -> player.getGameMode() != GameMode.SPECTATOR).toArray().length;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isMember(Player player){
        return players.contains(player);
    }

    public boolean isBedPlaced(){
        return !getBedLocation().getBlock().getType().toString().contains("BED");
    }

}
