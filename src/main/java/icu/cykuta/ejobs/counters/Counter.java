package icu.cykuta.ejobs.counters;

public class Counter {
    private final CounterType type;
    private final String object;
    private int value;

    public Counter(CounterType type, String object, int value) {
        this.type = type;
        this.object = object;
        this.value = value;
    }

    public CounterType getType() {
        return type;
    }

    public String getObject() {
        return object;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
