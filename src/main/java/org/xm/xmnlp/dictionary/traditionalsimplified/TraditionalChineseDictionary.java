package org.xm.xmnlp.dictionary.traditionalsimplified;


import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.collection.ahocorasick.AhoCorasickDoubleArrayTrie;
import static org.xm.xmnlp.util.Predefine.logger;

/**
 * 繁简词典，提供简繁转换
 */
public class TraditionalChineseDictionary extends BaseChineseDictionary {
    /**
     * 繁体=简体
     */
    public static AhoCorasickDoubleArrayTrie<String> trie = new AhoCorasickDoubleArrayTrie<String>();

    static {
        long start = System.currentTimeMillis();
        if (!load(Xmnlp.Config.TraditionalChineseDictionaryPath, trie, false)) {
            throw new IllegalArgumentException("繁简词典" + Xmnlp.Config.TraditionalChineseDictionaryPath + "加载失败");
        }

        logger.info("繁简词典" + Xmnlp.Config.TraditionalChineseDictionaryPath + "加载成功，耗时" + (System.currentTimeMillis() - start) + "ms");
    }

    public static String convertToSimplifiedChinese(String traditionalChineseString) {
        return segLongest(traditionalChineseString.toCharArray(), trie);
    }

    public static String convertToSimplifiedChinese(char[] traditionalChinese) {
        return segLongest(traditionalChinese, trie);
    }

}
