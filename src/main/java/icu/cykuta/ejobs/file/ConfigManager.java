package icu.cykuta.ejobs.file;

import icu.cykuta.ejobs.file.config.MainConfig;
import icu.cykuta.ejobs.file.data.DataConfig;
import icu.cykuta.ejobs.file.jobs.JobsConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;

public class ConfigManager {
    private final MainConfig mainConfig = new MainConfig();
    private final JobsConfig jobsConfigList = new JobsConfig();
    private final DataConfig dataConfig = new DataConfig();

    public FileConfiguration getMainConfig(){
        return mainConfig.getConfig();
    }

    public ArrayList<YamlConfiguration> getJobsConfig(){
        return this.jobsConfigList.getJobsList();
    }

    public DataConfig getDataConfig(){
        return dataConfig;
    }
}
