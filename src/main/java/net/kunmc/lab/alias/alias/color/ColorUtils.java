package net.kunmc.lab.alias.alias.color;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;

public class ColorUtils {
    public static ChatColor TextColorToChatColor(TextColor textColor) {
        ChatColor chatColor = null;
        switch (textColor.toString()) {
            case "black":
                chatColor = ChatColor.BLACK;
                break;
            case "auqa":
                chatColor = ChatColor.AQUA;
                break;
            case "blue":
                chatColor = ChatColor.BLUE;
                break;
            case "dark_aqua":
                chatColor = ChatColor.DARK_AQUA;
                break;
            case "dark_blue":
                chatColor = ChatColor.DARK_BLUE;
                break;
            case "dark_gray":
                chatColor = ChatColor.DARK_GRAY;
                break;
            case "dark_green":
                chatColor = ChatColor.DARK_GREEN;
                break;
            case "dark_purple":
                chatColor = ChatColor.DARK_PURPLE;
                break;
            case "dark_red":
                chatColor = ChatColor.DARK_RED;
                break;
            case "gold":
                chatColor = ChatColor.GOLD;
                break;
            case "green":
                chatColor = ChatColor.GREEN;
                break;
            case "yellow":
                chatColor = ChatColor.YELLOW;
                break;
            case "gary":
                chatColor = ChatColor.GRAY;
                break;
            case "light_purple":
                chatColor = ChatColor.LIGHT_PURPLE;
                break;
            default:
                chatColor = ChatColor.WHITE;
                break;
        }

        return chatColor;
    }
}
