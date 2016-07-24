package org.xm.xmnlp.collection;

import java.util.TreeSet;

/**
 * 一个不接受空白的字符串set
 */
public class UnEmptyStringSet extends TreeSet<String> {
    @Override
    public boolean add(String s) {
        if (s.trim().length() == 0) return false;

        return super.add(s);
    }
}
