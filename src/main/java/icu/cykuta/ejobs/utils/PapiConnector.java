package icu.cykuta.ejobs.utils;

import icu.cykuta.ejobs.Main;
import icu.cykuta.ejobs.data.Data;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;


public class PapiConnector extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return Main.getPlugin().getDescription().getName();
    }

    @Override
    public @NotNull String getAuthor() {
        return Main.getPlugin().getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return Main.getPlugin().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (player == null) return null;

        PlayerAdapter playerAdapter = Data.getPlayerAdapter(player.getPlayer());

        if (params.equalsIgnoreCase("job_name")){
            return playerAdapter.getJob().getName();
        }

        if (params.equalsIgnoreCase("job_level")){
            return String.valueOf(playerAdapter.getJobLevel());
        }

        if (params.equalsIgnoreCase("job_description")){
            return playerAdapter.getJob().getDescription();
        }

        return null;
    }

}
