package icu.cykuta.ejobs.utils;

import icu.cykuta.ejobs.Main;
import icu.cykuta.ejobs.counters.CounterType;
import icu.cykuta.ejobs.data.Data;
import icu.cykuta.ejobs.jobs.Job;
import icu.cykuta.ejobs.jobs.Requirement;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerAdapter {
    private final Player player;
    private Job job = null;
    private int jobLevel = 0;

    public PlayerAdapter(Player player) {
        this.player = player;
    }

    /**
     * Set list of job permissions to player.
     */
    public void setJobPermissions(){
        if (job == null) return;

        for (int level = 1; level <= jobLevel; level++){
            for (String permission : job.getPermissions(level)){
                player.addAttachment(Main.getPlugin(), permission, true);
            }
        }
    }

    /**
     * Unset list of job permissions to player.
     */
    public void unsetJobPermissions(){
        if (job == null) return;

        for (int level = 1; level <= jobLevel; level++){
            for (String permission : job.getPermissions(level)){
                player.addAttachment(Main.getPlugin(), permission, false);
                player.addAttachment(Main.getPlugin()).unsetPermission(permission);
            }
        }
    }

    /**
     * Function to set player level if it
     * is not higher than max level.
     */
    public void levelUp(){
        if (job == null) return;
        if (job.getMaxLevel() == jobLevel) return;
        jobLevel++;
        setJobPermissions();
    }

    /**
     * Function to verify if player can level up.
     */
    public boolean canLevelUp() {
        if (job == null) return false;
        return job.getMaxLevel() > jobLevel;
    }

    /**
     * Function to verify if player has all requirements completed.
     */
    public boolean verifyRequirements(int level){
        ArrayList<Requirement> requirements = job.getRequirements().get(level);

        for (Requirement requirement : requirements){
            CounterType type = requirement.getType();
            String object = requirement.getObject();
            int current = Data.getCounter(player, type, object);

            if (current < requirement.getAmount()) return false;
        }

        return true;
    }

    /**
     * Function to send message to player with colors.
     */
    public void sendMessage(String message){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    /* Getters and Setters */

    public void setJobLevel(int jobLevel) {
        this.jobLevel = jobLevel;
    }

    public int getJobLevel() {
        return jobLevel;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public void unsetJob() {
        job = null;
        jobLevel = 0;
    }

    public Job getJob() {
        return job;
    }

    public Player getPlayer() {
        return player;
    }

}
