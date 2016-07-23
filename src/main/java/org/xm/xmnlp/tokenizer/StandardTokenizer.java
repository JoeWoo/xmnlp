package org.xm.xmnlp.tokenizer;


import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.seg.Segment;
import org.xm.xmnlp.seg.domain.Term;

import java.util.List;

/**
 * 标准分词器
 * Created by xuming on 2016/7/22.
 */
public class StandardTokenizer {

    /**
     * 预置分词器
     */
    public static final Segment SEGMENT = Xmnlp.newSegment();

    /**
     * 分词
     *
     * @param text 文本
     * @return 分词结果
     */
    public static List<Term> segment(String text) {
        return SEGMENT.seg(text.toCharArray());
    }

    /**
     * 分词
     *
     * @param text 文本
     * @return 分词结果
     */
    public static List<Term> segment(char[] text) {
        return SEGMENT.seg(text);
    }

    /**
     * 切分为句子形式
     *
     * @param text 文本
     * @return 句子列表
     */
    public static List<List<Term>> seg2sentence(String text) {
        return SEGMENT.seg2sentence(text);
    }

}
