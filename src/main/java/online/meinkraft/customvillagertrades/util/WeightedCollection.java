package online.meinkraft.customvillagertrades.util;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;
import java.util.Map.Entry;

public class WeightedCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
    private final Random random;
    private double total = 0;

    public WeightedCollection() {
        this(new Random());
    }

    public WeightedCollection(Random random) {
        this.random = random;
    }

    public WeightedCollection<E> add(double weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, result);
        return this;
    }

    public E next() {
        double value = random.nextDouble() * total;
        Entry<Double, E> result = map.higherEntry(value);
        if(result == null) return null;
        return map.higherEntry(value).getValue();
    }

}