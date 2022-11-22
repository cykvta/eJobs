package icu.cykuta.ejobs.file;

import icu.cykuta.ejobs.file.files.ConfigFile;
import icu.cykuta.ejobs.file.files.DataFile;
import icu.cykuta.ejobs.file.files.JobsFile;
import icu.cykuta.ejobs.file.files.LangFile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;

public class ConfigManager {
    private final ConfigFile configFile = new ConfigFile();
    private final JobsFile jobsFileList = new JobsFile();
    private final DataFile dataFile = new DataFile();
    private final LangFile langFile = new LangFile();

    public FileConfiguration getMainConfig(){
        return configFile.getConfig();
    }

    public ArrayList<YamlConfiguration> getJobsConfig(){
        return this.jobsFileList.getJobsList();
    }

    public DataFile getDataConfig(){
        return dataFile;
    }

    public LangFile getLangFile(){
        return langFile;
    }
}
