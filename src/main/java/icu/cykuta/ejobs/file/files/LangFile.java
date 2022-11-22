package icu.cykuta.ejobs.file.files;

import icu.cykuta.ejobs.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LangFile {
    private final File file;

    public LangFile(){
        this.file = new File(Main.getPlugin().getDataFolder(), "lang.yml");
        if (!file.exists()) create();
    }

    /**
     * This function create a copy of file called lang.yml in the plugin folder.
     */
    private void create(){
        file.getParentFile().mkdirs();
        Main.getPlugin().saveResource("lang.yml", false);
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
     * With this function you can get the lang.yml file. If the file doesn't exist, it will be created.
     * @Return YamlConfiguration
     */
    public FileConfiguration getConfig() {
        return load();
    }
}
