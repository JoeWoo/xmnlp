package org.xm.xmnlp.dictionary.person;


import org.xm.xmnlp.dictionary.CoreDictionary;
import org.xm.xmnlp.util.Predefine;

/**
 * 人名识别中常用的一些常量
 */
public class NRConstant {
    /**
     * 本词典专注的词的ID
     */
    public static final int WORD_ID = CoreDictionary.getWordID(Predefine.TAG_PEOPLE);
    /**
     * 本词典专注的词的属性
     */
    public static final CoreDictionary.Attribute ATTRIBUTE = CoreDictionary.get(WORD_ID);
}
