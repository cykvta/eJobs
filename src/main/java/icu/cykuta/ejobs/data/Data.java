package icu.cykuta.ejobs.data;

import icu.cykuta.ejobs.Main;
import icu.cykuta.ejobs.file.counters.Counter;
import icu.cykuta.ejobs.file.counters.CounterType;
import icu.cykuta.ejobs.file.data.DataConfig;
import icu.cykuta.ejobs.jobs.Job;
import icu.cykuta.ejobs.utils.PlayerAdapter;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Data {
    public static ArrayList<PlayerAdapter> playerHolder = new ArrayList<>();
    public static Map<Player, ArrayList<Counter> > counters = new HashMap<>();

    /**
     * Set value to a player's counter.
     * @param player Player
     * @param counterType Counter type
     * @param material material string
     * @param value int
     */
    public static void setCounter(Player player, CounterType counterType, String material, int value){
        ArrayList<Counter> counterList = counters.computeIfAbsent(player, k -> new ArrayList<>());

        for (Counter counter : counters.get(player)) {
            if ( (counter.getType() == counterType) && (counter.getMaterial() == material) ) {
                counter.setValue(value);
                return;
            }
        }

        counterList.add(new Counter(counterType, material, value));
    }

    /**
     * Get value from a player's counter.
     * @param player player
     * @param material material string
     * @param counterType counter type
     * @return int
     */
    public static int getCounter(Player player, String material, CounterType counterType){
        ArrayList<Counter> countersList = counters.get(player);

        if (countersList != null){
            for (Counter counter : countersList) {
                if (counter.getType() == counterType && counter.getMaterial() == material) {
                    return counter.getValue();
                }
            }
        }

        return 0;
    }

    /**
     * Set player's job and save on map and data.yml
     * @param player PlayerAdapter
     * @param job Job
     * @param level int
     */
    public static void setPlayerJob(PlayerAdapter player, Job job, Integer level) {
        player.setJob(job);
        player.setJobLevel(level);
        player.setJobPermissions();
        savePlayerData(player);

        if (playerHolder.contains(player)) {
            playerHolder.remove(player);
            return;
        }

        playerHolder.add(player);
    }

    /**
     * Unset player's job and save on map and data.yml
     * @param player PlayerAdapter
     */
    public static void removePlayerJob(PlayerAdapter player) {
        player.unsetJobPermissions();
        player.unsetJob();
        String uuid = player.getPlayer().getUniqueId().toString();
        DataConfig dataFile = Main.getPlugin().getCfg().getDataConfig();

        dataFile.getData().set(uuid + ".job", null);
        dataFile.getData().set(uuid + ".job-level", null);
        playerHolder.remove(player);
        dataFile.saveData();
    }

    /**
     * Save player's job and level on data.yml
     * @param player PlayerAdapter
     */
    public static void savePlayerData(PlayerAdapter player) {
        String uuid = player.getPlayer().getUniqueId().toString();
        DataConfig dataFile = Main.getPlugin().getCfg().getDataConfig();

        dataFile.getData().set(uuid + ".job", player.getJob().getName());
        dataFile.getData().set(uuid + ".job-level", player.getJobLevel());
        dataFile.saveData();
    }

    /**
     * Save player counter on data.yml.
     * @param player Player
     * @param counterType CounterType
     * @param value int
     */
    public static void setCounterToFile(Player player, CounterType counterType, String material, int value) {
        String uuid = player.getPlayer().getUniqueId().toString();
        DataConfig dataFile = Main.getPlugin().getCfg().getDataConfig();

        dataFile.getData().set(uuid + ".counters." + counterType.toString() + "." + material, value);
        dataFile.saveData();
    }

    /**
     * Get player counter on data.yml.
     * @param player player
     * @param counter CounterType
     * @return counter value or null
     */
    public static int getCounterFromFile(Player player, CounterType counter, String materialString) {
        String uuid = player.getPlayer().getUniqueId().toString();
        DataConfig dataFile = Main.getPlugin().getCfg().getDataConfig();

        return dataFile.getData().getInt(uuid + ".counters." + counter + "." + materialString);
    }

    /**
     * Get PlayerAdapter from player.
     * @param player Player
     */
    @Nullable
    public static PlayerAdapter getPlayerAdapter(Player player) {
        for (PlayerAdapter playerAdapter : playerHolder) {
            if (playerAdapter.getPlayer().equals(player)) return playerAdapter;
        }

        return null;
    }

    /**
     * Save all player's data on data.yml
     */
    public static void saveAllPlayerData() {
        for (PlayerAdapter player : Data.playerHolder) {
            Data.savePlayerData(player);
        }
    }
}
