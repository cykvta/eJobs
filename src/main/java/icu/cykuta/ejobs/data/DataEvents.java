package icu.cykuta.ejobs.data;

import icu.cykuta.ejobs.Main;
import icu.cykuta.ejobs.jobs.Job;
import icu.cykuta.ejobs.utils.PlayerAdapter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DataEvents implements Listener {

    /**
     * When a player joins the server
     * this event will be called and load the player data.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PlayerAdapter playerAdapter = new PlayerAdapter(player);
        String uid = player.getUniqueId().toString();

        FileConfiguration data = Main.getPlugin().getCfg().getDataConfig().getData();
        int job_level = data.getInt(uid + ".job-level");
        Job job = Job.getJobFromName( data.getString(uid + ".job") );

        if (job == null) {
            Data.removePlayerJob(playerAdapter);
            return;
        }

        Data.setPlayerJob(playerAdapter, job, job_level);
    }

    /**
     * When a player leaves the server
     * this event will be called and save the player data.
     */
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        for (PlayerAdapter playerAdapter : Data.playerHolder) {

            if (playerAdapter.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                Data.playerHolder.remove(playerAdapter);
                break;
            }

        }
    }
}
