package icu.cykuta.ejobs.file.files;

import icu.cykuta.ejobs.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;

public class JobsFile {
    private final File dir = new File(Main.getPlugin().getDataFolder() + "/jobs");
    private final ArrayList<YamlConfiguration> jobsList = new ArrayList<>();

    public JobsFile(){
        this.load();
    }

    /**
     * This function load all files in the jobs' folder.
     */
    private void load(){
        File[] fileList = dir.listFiles();

        if (fileList == null) {
            this.create();
            fileList = dir.listFiles(); // This refresh the fileList variable.
        }

        for (File file : fileList) {
            YamlConfiguration config = new YamlConfiguration();

            try {
                config.load(file);
                this.jobsList.add(config);

            } catch (Exception e) {
                continue;
            }
        }

    }

    /**
     * This function create a copy of file called
     * template.yml in the plugin/jobs folder.
     */
    private void create(){
        this.dir.mkdirs();
        Main.getPlugin().saveResource("jobs/template.yml", false);
    }

    /**
     * This function return all jobs files.
     *
     * @return ArrayList<YamlConfiguration>
     */
    public ArrayList<YamlConfiguration> getJobsList(){
        return this.jobsList;
    }
}
