package icu.cykuta.ejobs;

import icu.cykuta.ejobs.commands.CommandLoader;
import icu.cykuta.ejobs.data.Data;
import icu.cykuta.ejobs.data.DataEvents;
import icu.cykuta.ejobs.counters.CounterEvent;
import icu.cykuta.ejobs.file.ConfigManager;
import icu.cykuta.ejobs.jobs.JobLoader;
import icu.cykuta.ejobs.utils.VaultConnector;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin {
    private ConfigManager cfg;
    private JobLoader jobLoader;
    private static Economy econ = null;
    private static Plugin itemsAdder = null;

    @Override
    public void onEnable() {
        cfg = new ConfigManager();
        jobLoader = new JobLoader();

        // Setup soft dependencies
        econ = VaultConnector.setupEconomy();
        itemsAdder = getServer().getPluginManager().getPlugin("ItemsAdder");

        CommandLoader.loadCommands(this);
        loadEvents();
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
