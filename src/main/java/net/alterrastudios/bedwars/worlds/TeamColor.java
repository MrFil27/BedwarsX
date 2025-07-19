package net.alterrastudios.bedwars.worlds;

import org.bukkit.Material;

public enum TeamColor {

    RED,
    BLUE,
    GREEN,
    YELLOW,
    AQUA,
    WHITE,
    PINK,
    GRAY;

    public String formattedName(){
        String caps = this.toString();
        return String.valueOf(caps.charAt(0)).toUpperCase() + caps.substring(1).toLowerCase();
    }

    public Material woolMaterial(){
        return switch(this){
            case RED -> Material.RED_WOOL;
            case BLUE -> Material.BLUE_WOOL;
            case YELLOW -> Material.YELLOW_WOOL;
            case GREEN -> Material.GREEN_WOOL;
            case AQUA -> Material.MAGENTA_WOOL;
            case GRAY -> Material.GRAY_WOOL;
            case PINK -> Material.PINK_WOOL;
            case WHITE -> Material.WHITE_WOOL;
        };
    }

}
