package net.kunmc.lab.alias.alias.color;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;

import java.util.Arrays;

public class ColorUtils {
    public static ChatColor TextColorToChatColor(TextColor textColor) {
        return Arrays.stream(ChatColor.values()).filter(color -> color.name().equalsIgnoreCase(textColor.toString())).findFirst().orElse(ChatColor.WHITE);
    }
}
