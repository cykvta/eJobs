package icu.cykuta.ejobs.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Log {
    public static void info(String message){
        Bukkit.getServer().getConsoleSender().sendMessage(
                ChatColor.translateAlternateColorCodes('&', "&6[eJobs Info] &b" + message));
    }

    public static void error(String message){
        Bukkit.getServer().getConsoleSender().sendMessage(
                ChatColor.translateAlternateColorCodes('&', "&4[eJobs Error] &c" + message));
    }
}
