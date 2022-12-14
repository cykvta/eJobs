package icu.cykuta.ejobs.commands;

import icu.cykuta.ejobs.Main;
import icu.cykuta.ejobs.counters.CounterType;
import icu.cykuta.ejobs.data.Data;
import icu.cykuta.ejobs.jobs.Job;
import icu.cykuta.ejobs.jobs.Requirement;
import icu.cykuta.ejobs.utils.Lang;
import icu.cykuta.ejobs.utils.Log;
import icu.cykuta.ejobs.utils.PlayerAdapter;
import net.milkbowl.vault.economy.Economy;
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
            Log.error(Lang.PLAYER_COMMAND_ERROR.value());
            return false;
        }

        Player player = (Player) sender;
        PlayerAdapter playerAdapter = Data.getPlayerAdapter(player);

        if (args.length < 1) return false;

        switch (args[0]) {
            case "join": return join(args, playerAdapter);
            case "leave": return leave(args, playerAdapter);
            case "levelup": return levelUp(args, playerAdapter);
            default: return false;
        }
    }

    public boolean join(String[] args, PlayerAdapter playerAdapter) {
        if (args.length != 2) return false;

        Player player = playerAdapter.getPlayer();
        Job job = Job.getJobFromName(args[1]);

        if (job == null) {
            playerAdapter.sendMessage(Lang.PREFIX.value() + Lang.JOB_NAME_NOT_FOUND.value());
            return true;
        }

        if (playerAdapter.getJob() != null) {
            playerAdapter.sendMessage(Lang.PREFIX.value() + Lang.ALREADY_HAVE_JOB.value());
            return true;
        }

        if (!player.hasPermission(job.getPermission())) {
            playerAdapter.sendMessage(Lang.PREFIX.value() + Lang.NO_PERMISSION_JOB.value());
            return true;
        }

        Data.setPlayerJob(playerAdapter, job, 1);
        playerAdapter.sendMessage(Lang.PREFIX.value() + Lang.JOB_JOIN.value().replace("%job%", job.getName()));
        return true;
    }

    public boolean leave(String[] args, PlayerAdapter playerAdapter){
        if (args.length != 1) return false;

        Job job = playerAdapter.getJob();

        if (job == null) {
            playerAdapter.sendMessage(Lang.PREFIX.value() + Lang.NOT_HAVE_JOB.value());
            return true;
        }

        Data.removePlayerJob(playerAdapter);
        Data.removeCounters(playerAdapter);
        Data.clearDataFile(playerAdapter);

        playerAdapter.sendMessage(Lang.PREFIX.value() + Lang.JOB_LEAVE.value().replace("%job%", job.getName()));
        return true;
    }

    public boolean levelUp(String[] args, PlayerAdapter playerAdapter){
        if (args.length != 1) return false;

        Player player = playerAdapter.getPlayer();
        Job job = playerAdapter.getJob();
        int level = playerAdapter.getJobLevel();
        int nextLevel = level + 1;

        // Check if player has a job.
        if (job == null) {
            playerAdapter.sendMessage(Lang.PREFIX.value() + Lang.NOT_HAVE_JOB.value());
            return true;
        }

        // Check if the player has reached the maximum level.
        if (!playerAdapter.canLevelUp()) {
            playerAdapter.sendMessage(Lang.PREFIX.value() + Lang.MAX_LEVEL_REACHED.value());
            return true;
        }

        // Check if the player has enough money to level up.
        if (Main.getEconomy() != null) {
            Economy econ = Main.getEconomy();
            double Player_balance = econ.getBalance(player);
            double cost = job.getLevelCost(nextLevel);

            if (Player_balance < cost) {
                playerAdapter.sendMessage(Lang.PREFIX.value() + Lang.NOT_ENOUGH_MONEY.value());
                return true;
            }
        }

        // Check if the player has enough requirements to level up.
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
            return true;
        }

        // Level up the player.
        for (Requirement requirement : requirements) {
            CounterType type = requirement.getType();
            int amount = requirement.getAmount();
            String object = requirement.getObject();
            int current_amount = Data.getCounter(player, type, object);
            Data.setCounter(player, type, object, current_amount - amount);
        }

        playerAdapter.levelUp();
        Data.savePlayerData(playerAdapter);
        playerAdapter.sendMessage(Lang.PREFIX.value() + Lang.JOB_LEVEL_UP.value());
        return true;
    }

}
