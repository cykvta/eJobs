package icu.cykuta.ejobs.jobs;

import icu.cykuta.ejobs.counters.CounterType;

public class Requirement {
    private final CounterType type;
    private final int amount;
    private final String object;

    public Requirement(CounterType type, String object, int amount) {
        this.type = type;
        this.amount = amount;
        this.object = object;
    }

    public CounterType getType() {
        return type;
    }

    public String getObject() {
        return object;
    }

    public int getAmount() {
        return amount;
    }
}
