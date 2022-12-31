package icu.cykuta.ejobs.utils;

import org.bukkit.ChatColor;

public class Color {
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
