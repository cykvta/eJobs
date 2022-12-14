package icu.cykuta.ejobs.jobs;

import icu.cykuta.ejobs.Main;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Job {
    private final String name;
    private final int maxLevel;
    private final String description;
    private final Map<Integer, List<String>> permissions;
    private final Map<Integer, ArrayList<Requirement> > requirements;
    private final Map<Integer, Integer> levelCost;
    private final String permission;

    public Job(String jobName, int maxLevel, String description, String permission,
               Map<Integer, List<String>> permissions, Map<Integer, ArrayList<Requirement>> requirements,
               Map<Integer, Integer> levelCost) {
        this.name = jobName;
        this.maxLevel = maxLevel;
        this.description = description;
        this.permission = permission;
        this.levelCost = levelCost;
        this.permissions = permissions;
        this.requirements = requirements;
    }

    public List<String> getPermissions(int level){
        return permissions.get(level);
    }

    public String getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public String getDescription() {
        return description;
    }

    public Map<Integer, ArrayList<Requirement> > getRequirements() {
        return requirements;
    }

    public String getPermission() {
        return permission;
    }

    public int getLevelCost(int level){
        return levelCost.get(level);
    }

    /**
     * Get job from list of jobs by name.
     * @param name
     * @return Job
     */
    @Nullable
    public static Job getJobFromName(String name){
        for (Job job : Main.getPlugin().getJobLoader().getJobs()){
            if (job.getName().equalsIgnoreCase(name)) return job;
        }
        return null;
    }
}
