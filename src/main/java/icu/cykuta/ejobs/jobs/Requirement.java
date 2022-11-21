package icu.cykuta.ejobs.jobs;

import icu.cykuta.ejobs.counters.CounterType;
import org.bukkit.Material;

public class Requirement {
    private final CounterType counterType;
    private final Material material;
    private final int amount;

    public Requirement(CounterType counterType, Material material, int amount) {
        this.counterType = counterType;
        this.material = material;
        this.amount = amount;
    }

    public CounterType getCounterType() {
        return counterType;
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }
}
