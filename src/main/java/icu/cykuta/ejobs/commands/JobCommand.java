package icu.cykuta.ejobs.commands;

import icu.cykuta.ejobs.counters.CounterType;
import icu.cykuta.ejobs.data.Data;
import icu.cykuta.ejobs.jobs.Job;
import icu.cykuta.ejobs.jobs.Requirement;
import icu.cykuta.ejobs.utils.Log;
import icu.cykuta.ejobs.utils.PlayerAdapter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class JobCommand implements CommandExecutor {

    /**
     * TODO:
     * Rewrite the command function. (too dirty)
     * Make it more readable.
     * Make it more efficient.
     */

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            Log.error("This command can only be executed by a player.");
            return false;
        }

        Player player = (Player) sender;
        PlayerAdapter playerAdapter = Data.getPlayerAdapter(player);

        if (playerAdapter == null) playerAdapter = new PlayerAdapter(player);

        if (args.length < 1) return false;

        switch (args[0]) {
            case "join":
                join(args, playerAdapter);
                break;
            case "leave":
                leave(args, playerAdapter);
                break;
            case "levelup":
                levelup(args, playerAdapter);
                break;
            default:
                return false;
        }

        return true;
    }

    public void join(String[] args, PlayerAdapter playerAdapter) {
        if (args.length != 2) return;

        Player player = playerAdapter.getPlayer();
        Job job = Job.getJobFromName(args[1]);

        if (job == null) {
            playerAdapter.sendMessage("&cNo job with this name.");
            return;
        }

        if (playerAdapter.getJob() != null) {
            playerAdapter.sendMessage("&cYou already have a job.");
            return;
        }

        if (!player.hasPermission(job.getPermission())) {
            playerAdapter.sendMessage("&cYou not have permission to join this job.");
            return;
        }

        Data.setPlayerJob(playerAdapter, job, 1);
        playerAdapter.sendMessage("&aYou joined the job " + job.getName() + ".");
    }

    public void leave(String[] args, PlayerAdapter playerAdapter){
        if (args.length != 1) return;

        Job job = playerAdapter.getJob();

        if (job == null) {
            playerAdapter.sendMessage("&cYou are not in a job.");
            return;
        }

        Data.removePlayerJob(playerAdapter);
        playerAdapter.sendMessage("&cYou left the job " + job.getName() + ".");
    }

    public void levelup(String[] args, PlayerAdapter playerAdapter){
        if (args.length != 1) return;

        Player player = playerAdapter.getPlayer();
        Job job = playerAdapter.getJob();
        int level = playerAdapter.getJobLevel();
        int nextLevel = level + 1;

        if (job == null) {
            playerAdapter.sendMessage("&cYou are not in a job.");
            return;
        }
        if (!playerAdapter.canLevelUp()) {
            playerAdapter.sendMessage("&cYour job level is already max.");
            return;
        }

        ArrayList<Requirement> requirements = playerAdapter.getJob().getRequirements().get(nextLevel);
        playerAdapter.sendMessage("&7----------------------------------------");
        if (!playerAdapter.verifyRequirements(nextLevel)) {
            for (Requirement requirement : requirements) {
                CounterType type = requirement.getType();
                int amount = requirement.getAmount();
                String object = requirement.getObject();

                int current_amount = Data.getCounter(player, type, object);
                playerAdapter.sendMessage("&cYou need " + type + " " + current_amount + "/" + amount + " " + object + " to level up.");
            }
            return;
        }

        for (Requirement requirement : requirements) {
            CounterType type = requirement.getType();
            int amount = requirement.getAmount();
            String object = requirement.getObject();
            int current_amount = Data.getCounter(player, type, object);
            Data.setCounter(player, type, object, current_amount - amount);
        }

        playerAdapter.levelUp();
        Data.savePlayerData(playerAdapter);
        playerAdapter.sendMessage("&aYou have leveled up!");
    }

}
