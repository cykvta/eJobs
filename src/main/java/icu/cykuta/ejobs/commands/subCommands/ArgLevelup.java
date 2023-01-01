package icu.cykuta.ejobs.commands.subCommands;

import icu.cykuta.ejobs.Main;
import icu.cykuta.ejobs.counters.CounterType;
import icu.cykuta.ejobs.data.Data;
import icu.cykuta.ejobs.jobs.Job;
import icu.cykuta.ejobs.jobs.Requirement;
import icu.cykuta.ejobs.utils.Lang;
import icu.cykuta.ejobs.utils.PlayerAdapter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ArgLevelup implements CommandArgument {
    @Override
    public @NotNull String getName() {
        return "levelup";
    }

    @Override
    public @NotNull String getPermission() {
        return "ejobs.levelup";
    }

    @Override
    public @NotNull String getDescription() {
        return "Level up a job.";
    }

    @Override
    public @NotNull String getUsage() {
        return "/job levelup";
    }

    @Override
    public boolean execute(PlayerAdapter playerAdapter, String[] args) {
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
}
