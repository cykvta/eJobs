package icu.cykuta.ejobs.counters;

import icu.cykuta.ejobs.data.Data;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

public class CounterEvent implements Listener {

    /**
     * Player break block counter
     */
    @EventHandler
    public void playerBreakBlock(BlockBreakEvent e){
        Player player = e.getPlayer();
        Material blockType = e.getBlock().getType();
        CounterType counterType = CounterType.BREAK;

        int counter = Data.getCounter(player, counterType, blockType.toString());
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

        int counter = Data.getCounter(player, counterType, blockType.toString());
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

        int counter = Data.getCounter(player, counterType, itemType.toString());
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

        int counter = Data.getCounter(player, counterType, itemType.toString());
        Data.setCounter(player, counterType, itemType.toString(), counter+1);
    }

    /**
     * Player kill entity counter
     */
    @EventHandler
    public void playerKillEntity(EntityDeathEvent e){
        if (e.getEntity().getKiller() == null) return;
        Player player = e.getEntity().getKiller();
        Entity entity = e.getEntity();
        CounterType counterType = CounterType.KILL;

        int counter = Data.getCounter(player, counterType, entity.getType().toString());
        Data.setCounter(player, counterType, entity.getType().toString(), counter+1);
    }

    /**
     * Player smell item counter
     */
    @EventHandler
    public void playerSmeltEvent(InventoryClickEvent e){
        if (!(e.getInventory() instanceof FurnaceInventory)) return;
        FurnaceInventory inventory = (FurnaceInventory) e.getInventory();
        ItemStack itemResult = inventory.getResult();

        if (itemResult == null) return;
        Player player = (Player) e.getWhoClicked();
        String result = itemResult.getType().toString();
        CounterType counterType = CounterType.SMELT;
        int amount = itemResult.getAmount();

        int counter = Data.getCounter(player, counterType, result);
        Data.setCounter(player, counterType, result, counter + amount);
    }

    /**
     * Player farm counter
     */
    @EventHandler
    public void playerFarmEvent(BlockBreakEvent e){
        Block block = e.getBlock();

        if (!(block.getBlockData() instanceof Ageable)) return;

        Ageable bData = (Ageable) block.getBlockData();
        if (bData.getAge() != bData.getMaximumAge()) return;

        Player player = e.getPlayer();
        String result = block.getType().toString();
        CounterType counterType = CounterType.FARM;

        int counter = Data.getCounter(player, counterType, result);
        Data.setCounter(player, counterType, result, counter + 1);
    }
}
