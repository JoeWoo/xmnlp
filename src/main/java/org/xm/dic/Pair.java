package org.xm.dic;

public class Pair<K> {
    public K key;
    public Double freq = 0.0;
    public String nature = "";

    public Pair(K key, double freq, String nature) {
        this.key = key;
        this.freq = freq;
        this.nature = nature;
    }

    @Override
    public String toString() {
        return "Candidate [key=" + key + ", freq=" + freq + ", nature=" + nature+ "]";
    }

}
