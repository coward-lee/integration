package org.lee;

import java.util.*;

public class RandomizedSet {

    Map<Integer, Integer> indices = new HashMap<>();
    ArrayList<Integer> values = new ArrayList<>();
    Random random = new Random();

    public RandomizedSet() {
    }

    public boolean insert(int val) {
        if (indices.containsKey(val)) {
            return false;
        }
        boolean add = values.add(val);
        indices.put(val, values.size() - 1);
        return true;
    }


    public boolean remove(int val) {
        Integer index = indices.get(val);
        if (index == null) {
            return false;
        }
        int lastValue = values.get(values.size() - 1);
        indices.put(lastValue, index);
        values.set(index, lastValue);

        values.remove(values.size() - 1);
        indices.remove(val);
        return true;
    }

    public int getRandom() {
        return values.get(random.nextInt(0, values.size()));
    }

    public static void main(String[] args) {
        RandomizedSet set = new RandomizedSet();
        set.insert(0);
        set.remove(0);
        System.out.println(set.insert(0));
    }
}