package icu.cykuta.ejobs.commands.subCommands;

import icu.cykuta.ejobs.counters.CounterType;
import icu.cykuta.ejobs.data.Data;
import icu.cykuta.ejobs.jobs.Requirement;
import icu.cykuta.ejobs.utils.Lang;
import icu.cykuta.ejobs.utils.PlayerAdapter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ArgRequirements implements CommandArgument {
    @Override
    public @NotNull String getName() {
        return "requirements";
    }

    @Override
    public @NotNull String getPermission() {
        return "ejobs.requirements";
    }

    @Override
    public @NotNull String getDescription() {
        return "Show the requirements of a job.";
    }

    @Override
    public @NotNull String getUsage() {
        return "/job requirements";
    }

    @Override
    public boolean execute(PlayerAdapter playerAdapter, String[] args) {
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
