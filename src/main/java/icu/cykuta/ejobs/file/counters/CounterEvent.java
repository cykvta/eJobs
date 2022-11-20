package icu.cykuta.ejobs.file.counters;

import icu.cykuta.ejobs.data.Data;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;

import java.util.EventListener;

public class CounterEvent implements EventListener, Listener {

    /**
     * TODO:
     * smelt
     * farm
     * kill
     * Brew
     */

    /**
     * Player break block counter
     */
    @EventHandler
    public void playerBreakBlock(BlockBreakEvent e){
        Player player = e.getPlayer();
        Material blockType = e.getBlock().getType();
        CounterType counterType = CounterType.BREAK;

        int counter = Data.getCounter(player, blockType.toString(), counterType);
        Data.setCounter(player, counterType, blockType.toString(), counter+1);
    }

    /**
     * Player place block counter
     */
    @EventHandler
    public void playerPlaceBlock(BlockPlaceEvent e){
        Player player = e.getPlayer();
        Material blockType = e.getBlock().getType();
        CounterType counterType = CounterType.PLACE;

        int counter = Data.getCounter(player, blockType.toString(), counterType);
        Data.setCounter(player, counterType, blockType.toString(), counter+1);
    }

    /**
     * Player craft item counter
     */
    @EventHandler
    public void playerCraftItem(CraftItemEvent e){
        Player player = (Player) e.getWhoClicked();
        Material itemType = e.getRecipe().getResult().getType();
        CounterType counterType = CounterType.CRAFT;

        int counter = Data.getCounter(player, itemType.toString(), counterType);
        Data.setCounter(player, counterType, itemType.toString(), counter+1);
    }

    /**
     * Player enchant item counter
     */
    @EventHandler
    public void playerEnchantItem(EnchantItemEvent e){
        Player player = e.getEnchanter();
        Material itemType = e.getItem().getType();
        CounterType counterType = CounterType.ENCHANT;

        int counter = Data.getCounter(player, itemType.toString(), counterType);
        Data.setCounter(player, counterType, itemType.toString(), counter+1);
    }



}
