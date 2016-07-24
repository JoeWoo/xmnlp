package org.xm.xmnlp.dic;

public class DictPair<K> {
    public K key;
    public Double freq = 0.0;
    public String nature = "";

    public DictPair(K key, double freq, String nature) {
        this.key = key;
        this.freq = freq;
        this.nature = nature;
    }

    @Override
    public String toString() {
        return "Candidate [key=" + key + ", freq=" + freq + ", nature=" + nature+ "]";
    }

}
