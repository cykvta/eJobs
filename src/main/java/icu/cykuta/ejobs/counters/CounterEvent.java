package icu.cykuta.ejobs.counters;

import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.ItemsAdder;
import icu.cykuta.ejobs.Main;
import icu.cykuta.ejobs.data.Data;
import icu.cykuta.ejobs.utils.PlayerAdapter;
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

public class CounterEvent implements Listener {

    /**
     * Player break block counter
     */
    @EventHandler
    public void playerBreakBlock(BlockBreakEvent e){
        Player player = e.getPlayer();
        PlayerAdapter adapter = Data.getPlayerAdapter(player);

        if (adapter.getJob() == null) return;

        String blockType = e.getBlock().getType().toString();
        CounterType counterType = CounterType.BREAK;

        // If the block is a custom block from ItemsAdder, get the custom block name.
        if (Main.isItemsAdderLoaded()) {
            ItemStack cBlock = ItemsAdder.getCustomBlock(e.getBlock());
            if (cBlock == null || ItemsAdder.isCustomItem(cBlock)) {
                blockType = CustomStack.byItemStack(cBlock).getId();
            }
        }

        int counter = Data.getCounter(player, counterType, blockType.toString());
        Data.setCounter(player, counterType, blockType, counter+1);
    }

    /**
     * Player place block counter
     */
    @EventHandler
    public void playerPlaceBlock(BlockPlaceEvent e){
        Player player = e.getPlayer();
        PlayerAdapter adapter = Data.getPlayerAdapter(player);

        if (adapter.getJob() == null) return;

        String blockType = e.getBlock().getType().toString();
        CounterType counterType = CounterType.PLACE;

        int counter = Data.getCounter(player, counterType, blockType);
        Data.setCounter(player, counterType, blockType.toString(), counter+1);
    }

    /**
     * Player craft item counter
     */
    @EventHandler
    public void playerCraftItem(InventoryClickEvent e){
        if (!(e.getInventory() instanceof CraftingInventory)) return;
        if (e.getSlotType() != InventoryType.SlotType.RESULT) return;

        CraftingInventory craftingInventory = (CraftingInventory) e.getInventory();
        Player player = (Player) e.getWhoClicked();
        PlayerAdapter adapter = Data.getPlayerAdapter(player);

        if (adapter.getJob() == null) return;

        ItemStack item = craftingInventory.getResult();

        if (item == null) return;

        String itemType = item.getType().toString();
        CounterType counterType = CounterType.CRAFT;

        // If the item is a custom item from ItemsAdder, get the custom item name.
        if (Main.isItemsAdderLoaded()) {
            CustomStack cItem = CustomStack.byItemStack(item);
            if (cItem != null) {
                itemType = cItem.getId();
            }
        }

        int counter = Data.getCounter(player, counterType, itemType);
        Data.setCounter(player, counterType, itemType, counter+1);
    }

    /**
     * Player enchant item counter
     */
    @EventHandler
    public void playerEnchantItem(EnchantItemEvent e){
        Player player = e.getEnchanter();
        PlayerAdapter adapter = Data.getPlayerAdapter(player);

        if (adapter.getJob() == null) return;

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
        PlayerAdapter adapter = Data.getPlayerAdapter(player);

        if (adapter.getJob() == null) return;

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
        if (e.getSlotType() != InventoryType.SlotType.RESULT) return;

        Player player = (Player) e.getWhoClicked();
        PlayerAdapter adapter = Data.getPlayerAdapter(player);

        if (adapter.getJob() == null) return;

        FurnaceInventory inventory = (FurnaceInventory) e.getInventory();
        ItemStack item = inventory.getResult();

        if (item == null) return;
        String itemType = item.getType().toString();
        CounterType counterType = CounterType.SMELT;
        int amount = item.getAmount();

        // If the item is a custom item from ItemsAdder, get the custom item name.
        if (Main.isItemsAdderLoaded()) {
            CustomStack cItem = CustomStack.byItemStack(item);
            if (cItem != null) {
                itemType = cItem.getId();
            }
        }

        int counter = Data.getCounter(player, counterType, itemType);
        Data.setCounter(player, counterType, itemType, counter + amount);
    }

    /**
     * Player farm counter
     */
    @EventHandler
    public void playerFarmEvent(BlockBreakEvent e){
        Block block = e.getBlock();
        Player player = e.getPlayer();
        PlayerAdapter adapter = Data.getPlayerAdapter(player);

        if (adapter.getJob() == null) return;
        if (!(block.getBlockData() instanceof Ageable)) return;

        Ageable bData = (Ageable) block.getBlockData();
        if (bData.getAge() != bData.getMaximumAge()) return;

        String result = block.getType().toString();
        CounterType counterType = CounterType.FARM;

        int counter = Data.getCounter(player, counterType, result);
        Data.setCounter(player, counterType, result, counter + 1);
    }
}
