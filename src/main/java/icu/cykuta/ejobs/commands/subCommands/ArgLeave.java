package icu.cykuta.ejobs.commands.subCommands;

import icu.cykuta.ejobs.data.Data;
import icu.cykuta.ejobs.jobs.Job;
import icu.cykuta.ejobs.utils.Lang;
import icu.cykuta.ejobs.utils.PlayerAdapter;
import org.jetbrains.annotations.NotNull;

public class ArgLeave implements CommandArgument {

    @Override
    public @NotNull String getName() {
        return "leave";
    }

    @Override
    public @NotNull String getPermission() {
        return "ejobs.leave";
    }

    @Override
    public @NotNull String getDescription() {
        return "Leave a job.";
    }

    @Override
    public @NotNull String getUsage() {
        return "/job leave";
    }

    @Override
    public boolean execute(PlayerAdapter playerAdapter, String[] args) {
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
}
