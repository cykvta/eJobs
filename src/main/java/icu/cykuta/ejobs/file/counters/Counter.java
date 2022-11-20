package icu.cykuta.ejobs.file.counters;

public class Counter {
    private final CounterType type;
    private final String material;
    private int value;

    public Counter(CounterType type, String material, int value) {
        this.type = type;
        this.material = material;
        this.value = value;
    }

    public CounterType getType() {
        return type;
    }

    public String getMaterial() {
        return material;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
