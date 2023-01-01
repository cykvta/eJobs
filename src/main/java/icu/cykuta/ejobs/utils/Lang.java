package icu.cykuta.ejobs.utils;

import icu.cykuta.ejobs.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public enum Lang {
    //GLOBAL
    PREFIX("global", "prefix"),

    //ERRORS
    PLAYER_COMMAND_ERROR("error", "player-command"),
    JOB_NAME_NOT_FOUND("error", "job-name-not-found"),
    ALREADY_HAVE_JOB("error", "already-have-job"),
    NO_PERMISSION_JOB("error", "no-permission-job"),
    NOT_HAVE_JOB("error", "not-have-job"),
    MAX_LEVEL_REACHED("error", "max-level-reached"),
    NOT_ENOUGH_MONEY("error", "not-enough-money"),
    NOT_ENOUGH_REQUIREMENTS("error", "not-enough-requirements"),
    NO_COMMAND_PERMISSION("error", "no-command-permission"),

    // INFO
    JOB_REQUIREMENTS("info", "job-requirements"),
    JOB_REQUIREMENTS_STATUS_OK("info", "job-requirements-status-ok"),
    JOB_REQUIREMENTS_STATUS_NOT_OK("info", "job-requirements-status-not-ok"),

    //SUCCESS
    JOB_JOIN("success", "job-join"),
    JOB_LEAVE("success", "job-leave"),
    JOB_LEVEL_UP("success", "job-level-up"),

    ;

    private final String path;
    private final String type;
    private final FileConfiguration cfg = Main.getPlugin().getCfg().getLangFile().getConfig();

    Lang(String type, String path){
        this.path = path;
        this.type = type;
    }

    public String value() {
        String full_path = type + "." + path;
        String value = cfg.getString(full_path);
        if (value == null) return "No value found for " + path;

        return ChatColor.translateAlternateColorCodes('&', value);
    }
}
