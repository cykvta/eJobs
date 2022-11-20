package icu.cykuta.ejobs.file.counters;

public enum CounterType {
    BREAK("break"),
    PLACE("place"),
    CRAFT("craft"),
    ENCHANT("enchant"),
    SMELT("smelt"),
    FARM("farm"),
    KILL("kill"),
    POTION("potion");

    private final String value;

    CounterType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
