package icu.cykuta.ejobs.file.files;

import icu.cykuta.ejobs.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class DataFile {
    private final File file;
    private FileConfiguration config;

    public DataFile() {
        this.file = new File(Main.getPlugin().getDataFolder(), "data.yml");
        if (!file.exists()) create();
        load();
    }

    /**
     * This function create a copy of file called data.yml in the plugin folder.
     */
    private void create(){
        file.getParentFile().mkdirs();
        Main.getPlugin().saveResource("data.yml", false);
    }

    /**
     * This function transform File to YamlConfiguration and load it.
     * @Return YamlConfiguration
     */
    private FileConfiguration load() {
        config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    /**
     * This function save the YamlConfiguration in the file.
     */
    public void saveData(){
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * With this function you can get the data.yml file. If the file doesn't exist, it will be created.
     * @Return YamlConfiguration
     */
    public FileConfiguration getData() {
        return config;
    }
}
