package icu.cykuta.ejobs.data;

import icu.cykuta.ejobs.Main;
import icu.cykuta.ejobs.file.counters.Counter;
import icu.cykuta.ejobs.file.counters.CounterType;
import icu.cykuta.ejobs.jobs.Job;
import icu.cykuta.ejobs.utils.Log;
import icu.cykuta.ejobs.utils.PlayerAdapter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class DataEvents implements Listener {

    /**
     * When a player joins the server
     * this event load job and save in player adapter.
     */
    @EventHandler
    public void joinPlayerJob(PlayerJoinEvent e) {
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
     * this event remove player adapter from list.
     */
    @EventHandler
    public void removePlayerAdapter(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        for (PlayerAdapter playerAdapter : Data.playerHolder) {
            if (playerAdapter.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                Data.playerHolder.remove(playerAdapter);
                break;
            }
        }
    }

    /**
     * When a player enters the server
     * this event load counter from data.yml and save in map.
     */
    @EventHandler
    public void joinStoreCounter(PlayerJoinEvent e){
        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();
        FileConfiguration data = Main.getPlugin().getCfg().getDataConfig().getData();

        for (CounterType type : CounterType.values()) {
            ConfigurationSection counters = data.getConfigurationSection(uuid + ".counters." + type.getValue());
            if (counters == null) continue;

            for (String material : counters.getKeys(false)) {
                Data.setCounter(player, type, material, counters.getInt(material));
            }
        }
    }

    /**
     * When a player leaves the server
     * this event save counter from map to data.yml.
     */
    @EventHandler
    public void leaveStoreCounter(PlayerQuitEvent e){
        Player player = e.getPlayer();

        for (CounterType type : CounterType.values()) {
            ArrayList<Counter> counters = Data.counters.get(player);
            if (counters == null) continue;
            for (Counter counter : counters) {
                String material = counter.getMaterial();
                int value = Data.getCounter(player, material, type);
                Data.setCounterToFile(player, type, material, value);
                Data.counters.remove(player);
            }
        }
    }
}
