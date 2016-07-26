package org.xm.xmnlp.summary;

import org.xm.xmnlp.dictionary.stopword.CoreStopWordDictionary;
import org.xm.xmnlp.seg.Segment;
import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.tokenizer.StandardTokenizer;

/**
 * 提取关键词基础类
 * Created by xuming on 2016/7/26.
 */
public class KeywordExtractor {
    Segment defaultSegment = StandardTokenizer.SEGMENT;

    /**
     * 是否应当将这个term纳入计算，词性属于名词、动词、副词、形容词
     * @param term Term
     * @return 是否应当
     */
    public boolean shouldInclude(Term term) {
        //去掉停用词
        if (term.nature == null) return false;
        String nature = term.nature.toString();
        char firstChar = nature.charAt(0);
        switch (firstChar) {
            case 'm':
            case 'b':
            case 'c':
            case 'e':
            case 'o':
            case 'p':
            case 'q':
            case 'u':
            case 'y':
            case 'z':
            case 'r':
            case 'w': {
                return false;
            }
            default: {
                if (term.word.trim().length() > 1 && !CoreStopWordDictionary.contains(term.word)) {
                    return true;
                }
            }
            break;
        }

        return false;
    }

    /**
     * 设置关键词提取器使用的分词器
     *
     * @param segment 任何开启了词性标注的分词器
     * @return 自己
     */
    public KeywordExtractor setSegment(Segment segment) {
        defaultSegment = segment;
        return this;
    }
}
