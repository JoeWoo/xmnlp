package org.xm.xmnlp.collection.trie.bintrie;

/**
 * 对值数组的包装，可以方便地取下一个
 *
 */
public class ValueArray<V> {
    V[] value;
    int offset;

    public ValueArray(V[] value) {
        this.value = value;
    }

    public V nextValue() {
        return value[offset++];
    }

    /**
     * 仅仅给子类用，不要用
     */
    protected ValueArray() {
    }

    public ValueArray setValue(V[] value) {
        this.value = value;
        return this;
    }
}
