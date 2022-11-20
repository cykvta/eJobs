package icu.cykuta.ejobs.commands;

import icu.cykuta.ejobs.Main;

public class CommandLoader {
    public static void loadCommands(Main plugin) {
        plugin.getCommand("job").setExecutor(new JobCommand());
    }
}
