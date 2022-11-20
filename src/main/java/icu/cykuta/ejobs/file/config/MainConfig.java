package icu.cykuta.ejobs.file.config;

import icu.cykuta.ejobs.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MainConfig {
    private final File file;

    public MainConfig(){
        this.file = new File(Main.getPlugin().getDataFolder(), "config.yml");
        if (!file.exists()) create();
    }

    /**
     * This function create a copy of file called config.yml in the plugin folder.
     */
    private void create(){
        file.getParentFile().mkdirs();
        Main.getPlugin().saveResource("config.yml", false);
    }

    /**
     * This function transform File to YamlConfiguration and load it.
     * @Return YamlConfiguration
     */
    private FileConfiguration load() {
        FileConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    /**
     * With this function you can get the config.yml file. If the file doesn't exist, it will be created.
     * @Return YamlConfiguration
     */
    public FileConfiguration getConfig() {
        return load();
    }
}
