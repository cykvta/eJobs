package icu.cykuta.ejobs.commands;

import icu.cykuta.ejobs.data.Data;
import icu.cykuta.ejobs.jobs.Job;
import icu.cykuta.ejobs.utils.Log;
import icu.cykuta.ejobs.utils.PlayerAdapter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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

        // Arg Join logic
        if (args[0].equalsIgnoreCase("join")) {
            if (args.length != 2) return false;

            Job job = Job.getJobFromName(args[1]);
            if (job == null) {
                playerAdapter.sendMessage("&cNo job with this name.");
                return true;
            }

            if (playerAdapter.getJob() != null) {
                playerAdapter.sendMessage("&cYou already have a job.");
                return true;
            }

            Data.setPlayerJob(playerAdapter, job, 1);
            playerAdapter.sendMessage("&aYou joined the job " + job.getName() + ".");
            return true;
        }

        // Arg Leave logic
        if (args[0].equalsIgnoreCase("leave")) {
            if (args.length != 1) return false;

            Job job = playerAdapter.getJob();

            if (job == null) {
                playerAdapter.sendMessage("&cYou are not in a job.");
                return true;
            }

            Data.removePlayerJob(playerAdapter);
            playerAdapter.sendMessage("&cYou left the job " + job.getName() + ".");
            return true;
        }

        // Arg Levelup Logic
        if (args[0].equalsIgnoreCase("levelup")) {
            if (args.length != 1) return false;

            Job job = playerAdapter.getJob();

            if (job == null) {
                playerAdapter.sendMessage("&cYou are not in a job.");
                return true;
            }
            if (!playerAdapter.canLevelUp()) {
                playerAdapter.sendMessage("&cYour job level is already max.");
                return true;
            }

            if (!playerAdapter.verifyRequirements()) {
                playerAdapter.sendMessage("&cYou don't have the requirements to level up.");
                return true;
            }

            playerAdapter.levelUp();
            Data.savePlayerData(playerAdapter);
            playerAdapter.sendMessage("&aYou have leveled up!");
            return true;
        }

        return true;
    }
}
