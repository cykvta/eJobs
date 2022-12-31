package icu.cykuta.ejobs.commands;

import icu.cykuta.ejobs.jobs.Job;
import icu.cykuta.ejobs.jobs.JobLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class JobTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final List<String> subcommands = new ArrayList<>();
        if (args.length == 1) {
            subcommands.add("join");
            subcommands.add("leave");
            subcommands.add("levelup");
            subcommands.add("requirements");
        }

        // Complete job names
        if (args.length == 2 && args[0].equals("join")) {
            for (Job job : JobLoader.getJobs()) {
                subcommands.add(job.getName());
            }
        }

        return subcommands;
    }
}
