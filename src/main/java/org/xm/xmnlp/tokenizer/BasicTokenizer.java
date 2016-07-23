//package org.xm.xmnlp.tokenizer;
//
//
//import org.xm.xmnlp.Xmnlp;
//import org.xm.xmnlp.seg.Segment;
//import org.xm.xmnlp.seg.domain.Term;
//
//import java.util.List;
//
///**
// * 基础分词器，只做基本NGram分词，不识别命名实体，不使用用户词典
// */
//public class BasicTokenizer
//{
//    /**
//     * 预置分词器
//     */
//    public static final Segment SEGMENT = Xmnlp.newSegment().enableAllNamedEntityRecognize(false).enableCustomDictionary(false);
//
//    /**
//     * 分词
//     * @param text 文本
//     * @return 分词结果
//     */
//    public static List<Term> segment(String text)
//    {
//        return SEGMENT.seg(text.toCharArray());
//    }
//
//    /**
//     * 分词
//     * @param text 文本
//     * @return 分词结果
//     */
//    public static List<Term> segment(char[] text)
//    {
//        return SEGMENT.seg(text);
//    }
//
//    /**
//     * 切分为句子形式
//     * @param text 文本
//     * @return 句子列表
//     */
//    public static List<List<Term>> seg2sentence(String text)
//    {
//        return SEGMENT.seg2sentence(text);
//    }
//}
