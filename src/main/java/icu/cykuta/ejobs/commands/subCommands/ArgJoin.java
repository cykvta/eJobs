package icu.cykuta.ejobs.commands.subCommands;

import icu.cykuta.ejobs.data.Data;
import icu.cykuta.ejobs.jobs.Job;
import icu.cykuta.ejobs.utils.Lang;
import icu.cykuta.ejobs.utils.PlayerAdapter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ArgJoin implements CommandArgument {

    @Override
    public @NotNull String getName() {
        return "join";
    }

    @Override
    public @NotNull String getPermission() {
        return "ejobs.join";
    }

    @Override
    public @NotNull String getDescription() {
        return "Join a job.";
    }

    @Override
    public @NotNull String getUsage() {
        return "/job join <job>";
    }

    @Override
    public boolean execute(PlayerAdapter playerAdapter, String[] args) {
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

}
