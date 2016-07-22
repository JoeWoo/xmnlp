package org.xm.seg;

import org.xm.Xmnlp;
import org.xm.domain.Term;

import java.util.List;

/**
 * Created by xuming on 2016/7/22.
 */
public abstract class Segment {
    /**
     * 分词器配置
     */
    protected Xmnlp.Config config;

    /**
     * 构造一个分词器
     */
    public Segment()
    {
        config = new Xmnlp.Config();
    }

    /**
     * 给一个句子分词
     *
     * @param sentence 待分词句子
     * @return 单词列表
     */
    protected abstract List<Term> segSentence(char[] sentence);

    /**
     * 分词
     *
     * @param text 待分词文本
     * @return 单词列表
     */
    public List<Term> seg(char[] text) {
        assert text != null;
        /*if (HanLP.Config.Normalization) {
            CharTable.normalization(text);
        }*/
        return segSentence(text);
    }


}
