package org.xm.xmnlp.seg.domain;


import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.corpus.tag.Nature;
import org.xm.xmnlp.util.LexiconUtil;

/**
 * Created by xuming on 2016/7/22.
 */
public class Term {
    /**
     * 词语
     */
    public String word;
    /**
     * 词性
     */
    public Nature nature;

    /**
     * 在文本中的起始位置（需开启分词器的offset选项）
     */
    public int offset;

    /**
     * 构造一个单词
     *
     * @param word   词语
     * @param nature 词性
     */
    public Term(String word, Nature nature) {
        this.word = word;
        this.setNature(nature);
    }

    @Override
    public String toString() {
        if (Xmnlp.Config.ShowTermNature)
            return word + "/" + getNature();
        return word;
    }

    /**
     * 长度
     */
    public int length() {
        return word.length();
    }

    /**
     * 获取本词语在HanLP词库中的频次
     *
     * @return 频次，0代表这是个OOV
     */
    public int getFrequency() {
        return LexiconUtil.getFrequency(word);
    }

    /**
     * 词性
     */
    public Nature getNature() {
        return nature;
    }

    public void setNature(Nature nature) {
        this.nature = nature;
    }
}
