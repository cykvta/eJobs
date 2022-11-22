package icu.cykuta.ejobs.jobs;

import icu.cykuta.ejobs.Main;
import icu.cykuta.ejobs.counters.CounterType;
import icu.cykuta.ejobs.file.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobLoader {

    private final ArrayList<Job> jobs = new ArrayList<>();

    /**
     * Load all jobs from config file and store
     * them in a list of Jobs.
     */
    public JobLoader(){
        // Get the config
        ConfigManager config = Main.getPlugin().getCfg();

        // Get the jobs data from file.
        for (FileConfiguration JobFile : config.getJobsConfig()){
            String jobName     = JobFile.getString("name");
            int maxLevel       = JobFile.getInt("max-level");
            String description = JobFile.getString("description");
            String permission = JobFile.getString("permission");

            Map<Integer, List<String> > jobPermissions = new HashMap<>();
            Map<Integer, ArrayList<Requirement> > jobRequirements = new HashMap<>();

            // Grab all data in levels from the jobs file.
            for (String key : JobFile.getConfigurationSection("levels").getKeys(false)){
                int level = Integer.parseInt(key);

                // Get the permissions list for the level.
                List<String> permissions = JobFile.getStringList("levels." + level + ".permissions");
                jobPermissions.put(level, permissions);

                // Get the requirements for the level.
                ArrayList<Requirement> levelRequirements = new ArrayList<>();

                // Get the requirements list for the level.
                List<String> requirements = JobFile.getStringList("levels." + level + ".requirements");
                for (String val : requirements) {
                    String[] req = val.split(":");
                    String rawType   = req[0];
                    String rawObject = req[1];
                    String rawValue  = req[2];

                    CounterType reqType = CounterType.valueOf(rawType.toUpperCase());
                    int reqValue = Integer.parseInt(rawValue);
                    levelRequirements.add(new Requirement(reqType, rawObject, reqValue));
                }

                jobRequirements.put(level, levelRequirements);
            }

            // Create a new Job object and add it to the jobs list.
            jobs.add( new Job(jobName, maxLevel, description, permission, jobPermissions, jobRequirements) );
        }
    }

    /**
     * Get the list of jobs.
     * @return ArrayList<Job>
     */
    public ArrayList<Job> getJobs(){
        return jobs;
    }
}
