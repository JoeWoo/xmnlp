package org.xm.xmnlp.dictionary.stopword;


import org.xm.xmnlp.seg.domain.Term;

/**
 * 停用词词典过滤器
 */
public interface Filter {
    /**
     * 是否应当将这个term纳入计算
     *
     * @param term
     * @return 是否应当
     */
    boolean shouldInclude(Term term);
}
