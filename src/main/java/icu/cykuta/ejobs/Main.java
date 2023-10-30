package icu.cykuta.ejobs;

import icu.cykuta.ejobs.commands.BaseCommand;
import icu.cykuta.ejobs.data.Data;
import icu.cykuta.ejobs.data.DataEvents;
import icu.cykuta.ejobs.counters.CounterEvent;
import icu.cykuta.ejobs.file.ConfigManager;
import icu.cykuta.ejobs.jobs.JobLoader;
import icu.cykuta.ejobs.utils.PapiConnector;
import icu.cykuta.ejobs.utils.VaultConnector;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin {
    private ConfigManager cfg;
    private JobLoader jobLoader;
    private static Economy econ = null;
    private static Plugin itemsAdder = null;
    private static Plugin placeholderAPI = null;

    @Override
    public void onEnable() {
        cfg = new ConfigManager();
        jobLoader = new JobLoader();

        // Setup soft dependencies
        econ = VaultConnector.setupEconomy();
        itemsAdder = getServer().getPluginManager().getPlugin("ItemsAdder");
        placeholderAPI = getServer().getPluginManager().getPlugin("PlaceholderAPI");

        loadEvents();
        CommandMap commandMap = getCommandMap();
        commandMap.register("ejobs", new BaseCommand());
    }

    private CommandMap getCommandMap() {
        try {
            return (CommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onDisable() {
        Data.saveAllPlayerData();
        Data.saveAllPlayerCounters();
    }

    private void loadEvents(){
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new DataEvents(), this);
        pm.registerEvents(new CounterEvent(), this);

        // Register PAPI placeholders
        if (placeholderAPI != null) {
            new PapiConnector().register();
        }
    }

    public ConfigManager getCfg() {
        return cfg;
    }

    public JobLoader getJobLoader() {
        return jobLoader;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static boolean isItemsAdderLoaded() {
        return itemsAdder != null;
    }

    public static Main getPlugin() {
        return JavaPlugin.getPlugin(Main.class);
    }
}
