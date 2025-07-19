package net.alterrastudios.bedwars.config;

import net.alterrastudios.bedwars.BedWars;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static net.alterrastudios.bedwars.BedWars.format;

public class Language {

    private static final BedWars plugin = BedWars.getInstance();
    private static FileConfiguration config;
    private static File file;

    public static void setup(){
        File folder = plugin.getDataFolder();
        if(!folder.exists() && folder.mkdirs()) Bukkit.getConsoleSender().sendMessage(format("&2Plugin folder created."));

        file = new File(plugin.getDataFolder(), "language.yml");

        try{
            if(!file.exists() && file.createNewFile()){
                Bukkit.getConsoleSender().sendMessage(format("&2language.yml created."));
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        config = YamlConfiguration.loadConfiguration(file);
        if(file.length() == 0) setData();
    }

    public static File getFile(){
        return file;
    }

    public static FileConfiguration get(){
        if(config == null){
            setup();
        }
        return config;
    }

    public static void save(){
        try{
            get().save(file);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void reload(){
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static String getString(String path){
        return get().getString(path);
    }

    private static void setData(){
        //Errors
        get().set("messages.instance-denied", "&cYou can't execute that command from this instance.");
        get().set("messages.no-permission", "&cYou don't have permission to do that.");
        get().set("messages.setup-syntax-error", "&cSyntax error, please use &7/setup-bedwars <map name>");
        get().set("messages.pre-lobby-kicked-not-op", "&cThe match hasn't started yet.");
        get().set("messages.countdown-stopped-missing-players", "&cCountdown cancelled while waiting for more players.");

        //Successes
        get().set("messages.wait-loading-world", "&c[BedwarsX] &eLoading world, please one moment...");
        get().set("messages.diamond-set", "&aYou have been set diamond generator into your location.");
        get().set("messages.emerald-set", "&aYou have been set emerald generator into your location.");
        get().set("messages.island-changed", "&aYou have been changed island.");
        get().set("messages.first-corner-set", "&aYou have been set first corner.");
        get().set("messages.first-second-set", "&aYou have been set second corner.");
        get().set("messages.shop-set", "&cYou have been set shop location.");
        get().set("messages.island-generator-set", "&aYou have been set island generator location.");
        get().set("messages.team-upgrade-set", "&aYou have been set team upgrade location.");
        get().set("messages.spawn-location-set", "&aYou have been set spawn location.");
        get().set("messages.bed-location-set", "&aYou have been set bed location.");
        get().set("messages.lobby-spawn-location-set", "&aYou have been set lobby spawn location.");
        get().set("messages.kick-to-restarting", "&cRestarting game...");
        get().set("messages.kick-to-resetting", "&cResetting game...");
        get().set("messages.match-started", "&eThe game has begun!");

        get().set("messages.waiting-players", "&eWaiting for more players...");
        get().set("messages.countdown-started", "&eThe match will start in &c%seconds% &eseconds.");

        //Menus
        get().set("menus.select-island", "&2Select island");
        get().set("menus.exit-item", "&cExit");

        save();
    }

}
