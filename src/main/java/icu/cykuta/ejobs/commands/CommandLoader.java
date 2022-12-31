package icu.cykuta.ejobs.commands;

import icu.cykuta.ejobs.Main;
import org.bukkit.command.PluginCommand;

public class CommandLoader {
    public static void loadCommands(Main plugin) {
        PluginCommand jobCommand = plugin.getCommand("job");
        if (jobCommand != null) {
            jobCommand.setExecutor(new JobCommand());
            jobCommand.setTabCompleter(new JobTabCompleter());
        }
    }
}
