package org.xm.tokenizer;

import org.xm.Xmnlp;
import org.xm.domain.Term;

import org.xm.seg.Segment;
import java.util.List;

/**
 * Created by xuming on 2016/7/22.
 */
public class StandardTokenizer {

    /**
     * 预置分词器
     */
    public static final Segment SEGMENT = Xmnlp.newSegment();

    /**
     * 分词
     * @param text 文本
     * @return 分词结果
     */
    public static List<Term> segment(char[] text)
    {
        return SEGMENT.seg(text);
    }
}
