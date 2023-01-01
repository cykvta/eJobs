package icu.cykuta.ejobs.commands.subCommands;
import icu.cykuta.ejobs.utils.PlayerAdapter;
import org.jetbrains.annotations.NotNull;

public interface CommandArgument {
    @NotNull
    String getName();

    @NotNull
    String getPermission();

    @NotNull
    String getDescription();

    @NotNull
    String getUsage();

    boolean execute(PlayerAdapter player, String[] args);
}
