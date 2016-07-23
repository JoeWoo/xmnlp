package org.xm.xmnlp.collection.trie.bintrie;

public class EmptyValueArray<V> extends ValueArray<V> {
    public EmptyValueArray() {
    }

    @Override
    public V nextValue() {
        return null;
    }
}
