package org.xm.domain;

import org.xm.Xmnlp;
import org.xm.corpus.tag.Nature;

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
     * @param word 词语
     * @param nature 词性
     */
    public Term(String word, Nature nature)
    {
        this.word = word;
        this.nature = nature;
    }

    @Override
    public String toString()
    {
        if (Xmnlp.Config.ShowTermNature)
            return word + "/" + nature;
        return word;
    }

    /**
     * 长度
     * @return
     */
    public int length()
    {
        return word.length();
    }



}
