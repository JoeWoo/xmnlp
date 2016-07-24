package org.xm.xmnlp.dictionary.pinyin;

/**
 * 将整型转为拼音
 */
public class Integer2PinyinConverter {
    public static final Pinyin[] pinyins = Pinyin.values();

    public static Pinyin getPinyin(int ordinal) {
        return pinyins[ordinal];
    }
}
