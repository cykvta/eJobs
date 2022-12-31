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
            case "requirements": return requirements(args, playerAdapter);
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
        if (job.getMaxLevel() <= level) {
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
        if (playerAdapter.verifyRequirements(level)) {
            playerAdapter.sendMessage(Lang.PREFIX.value() + Lang.NOT_ENOUGH_REQUIREMENTS.value());
            return true;
        }

        // Take away requirements of the player.
        for (Requirement requirement : requirements) {
            CounterType type = requirement.getType();
            int amount = requirement.getAmount();
            String object = requirement.getObject();
            int current_amount = Data.getCounter(player, type, object);
            Data.setCounter(player, type, object, current_amount - amount);
        }

        // Level up the player.
        playerAdapter.levelUp();
        Data.savePlayerData(playerAdapter);
        playerAdapter.sendMessage(Lang.PREFIX.value() + Lang.JOB_LEVEL_UP.value());
        return true;
    }

    public boolean requirements(String[] args, PlayerAdapter playerAdapter) {
        if (args.length != 1) return false;

        if (playerAdapter.getJob() == null) {
            playerAdapter.sendMessage(Lang.PREFIX.value() + Lang.NOT_HAVE_JOB.value());
            return true;
        }

        Player player = playerAdapter.getPlayer();
        int level = playerAdapter.getJobLevel();
        int nextLevel = level + 1;
        ArrayList<Requirement> requirements = playerAdapter.getJob().getRequirements().get(nextLevel);

        if (!playerAdapter.verifyRequirements(nextLevel)) {
            for (Requirement requirement : requirements) {
                CounterType type = requirement.getType();
                int amount = requirement.getAmount();
                String object = requirement.getObject();

                int current_amount = Data.getCounter(player, type, object);
                String msg = Lang.JOB_REQUIREMENTS.value()
                        .replace("%type%", type.toString())
                        .replace("%amount%", String.valueOf(amount))
                        .replace("%object%", object)
                        .replace("%current_amount%", String.valueOf(current_amount));

                String status = current_amount >= amount ?
                        Lang.JOB_REQUIREMENTS_STATUS_OK.value() : Lang.JOB_REQUIREMENTS_STATUS_NOT_OK.value();

                playerAdapter.sendMessage(Lang.PREFIX.value() + status + msg);
            }
            return true;
        }

        return true;
    }

}
