package icu.cykuta.ejobs;

import icu.cykuta.ejobs.commands.CommandLoader;
import icu.cykuta.ejobs.data.Data;
import icu.cykuta.ejobs.data.DataEvents;
import icu.cykuta.ejobs.counters.CounterEvent;
import icu.cykuta.ejobs.file.ConfigManager;
import icu.cykuta.ejobs.jobs.JobLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin {
    private ConfigManager cfg;
    private JobLoader jobLoader;

    @Override
    public void onEnable() {
        cfg = new ConfigManager();
        jobLoader = new JobLoader();
        CommandLoader.loadCommands(this);
        loadEvents();
    }

    @Override
    public void onDisable() {
        Data.saveAllPlayerData();
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

    public static Main getPlugin() {
        return JavaPlugin.getPlugin(Main.class);
    }
}
