package icu.cykuta.ejobs.commands;

import icu.cykuta.ejobs.commands.subCommands.*;
import icu.cykuta.ejobs.data.Data;
import icu.cykuta.ejobs.utils.Lang;
import icu.cykuta.ejobs.utils.PlayerAdapter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BaseCommand extends Command {
    private static final List<CommandArgument> subCommands = new ArrayList<>();

    public BaseCommand() {
        super("job");
        addSubCommand(new ArgJoin());
        addSubCommand(new ArgLeave());
        addSubCommand(new ArgLevelup());
        addSubCommand(new ArgRequirements());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        PlayerAdapter playerAdapter = Data.getPlayerAdapter(player);

        // Command help message triggered when no arguments are provided
        if (args.length == 0) {
            playerAdapter.sendMessage(Lang.PREFIX.value() + "&6&leJobs commands:");
            playerAdapter.sendMessage(Lang.PREFIX.value() + "&7developed by cykuta");

            subCommands.forEach(subCommand -> {
                playerAdapter.sendMessage(Lang.PREFIX.value() + "&6" + subCommand.getUsage() + " &7- &f" + subCommand.getDescription());
            });

            return false;
        }

        // Execute selected sub command.
        for (CommandArgument subCommand : subCommands) {
            if (!subCommand.getName().equalsIgnoreCase(args[0])) continue;

            if (!sender.hasPermission(subCommand.getPermission())) {
                sender.sendMessage(Lang.NO_COMMAND_PERMISSION.value());
                return true;
            }

            return subCommand.execute(playerAdapter, args);

        }

        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        final List<String> list = new ArrayList<>();

        if (args.length == 1) {
            for (CommandArgument subCommand : subCommands) {
                if (!sender.hasPermission(subCommand.getPermission())) continue;
                list.add(subCommand.getName());
            }

        }

        return list;
    }

    public static void addSubCommand(CommandArgument subCommand) {
        subCommands.add(subCommand);
    }
}
