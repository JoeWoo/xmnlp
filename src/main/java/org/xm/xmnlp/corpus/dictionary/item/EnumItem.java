package org.xm.xmnlp.corpus.dictionary.item;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 标签-频次的封装
 * Created by mingzai on 2016/7/23.
 */
public class EnumItem<E extends Enum<E>> {
    public Map<E, Integer> labelMap;

    public EnumItem() {
        labelMap = new TreeMap<E, Integer>();
    }

    /**
     * 一个标签的项
     *
     * @param label
     * @param freq
     */
    public EnumItem(E label, Integer freq) {
        this();
        labelMap.put(label, freq);
    }

    /**
     * 新建一个标签的频次都是1的项
     *
     * @param labels
     */
    public EnumItem(E... labels) {
        this();
        for (E label : labels) {
            labelMap.put(label, 1);
        }
    }

    public void addLabel(E label, Integer freq) {
        Integer innerFreq = labelMap.get(label);
        if (innerFreq == null) innerFreq = freq;
        else innerFreq += freq;

        labelMap.put(label, innerFreq);
    }

    public boolean containsLabel(E label) {
        return labelMap.containsKey(label);
    }

    public int getFrequency(E label) {
        Integer freq = labelMap.get(label);
        if (freq == null) return 0;
        return freq;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        ArrayList<Map.Entry<E, Integer>> entries = new ArrayList<Map.Entry<E, Integer>>(labelMap.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<E, Integer>>() {
            @Override
            public int compare(Map.Entry<E, Integer> o1, Map.Entry<E, Integer> o2) {
                return -o1.getValue().compareTo(o2.getValue());
            }
        });
        for (Map.Entry<E, Integer> entry : entries) {
            sb.append(entry.getKey());
            sb.append(' ');
            sb.append(entry.getValue());
            sb.append(' ');
        }
        return sb.toString();
    }

    public static Map.Entry<String, Map.Entry<String, Integer>[]> create(String param) {
        if (param == null) return null;
        String[] array = param.split(" ");
        return create(array);
    }

    /**
     * 实现遍历新建项
     *
     * @param array
     * @return
     */
    private static Map.Entry<String, Map.Entry<String, Integer>[]> create(String[] array) {
        if (array.length % 2 == 0) return null;
        int natureCount = (array.length - 1) / 2;
        Map.Entry<String, Integer>[] entries = (Map.Entry<String, Integer>[]) Array.newInstance(Map.Entry.class, natureCount);
        for (int i = 0; i < natureCount; ++i) {
            entries[i] = new AbstractMap.SimpleEntry<String, Integer>(array[1 + 2 * i], Integer.parseInt(array[2 + 2 * i]));
        }
        return new AbstractMap.SimpleEntry<String, Map.Entry<String, Integer>[]>(array[0], entries);
    }

}
