package org.xm.xmnlp.dictionary.traditionalsimplified;


import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.collection.ahocorasick.AhoCorasickDoubleArrayTrie;
import org.xm.xmnlp.util.Predefine;
import static org.xm.xmnlp.util.Predefine.logger;

/**
 * 简体=繁体词典
 */
public class SimplifiedChineseDictionary extends BaseChineseDictionary {
    /**
     * 简体=繁体
     */
    static AhoCorasickDoubleArrayTrie<String> trie = new AhoCorasickDoubleArrayTrie<String>();

    static {
        long start = System.currentTimeMillis();
        if (!load(Xmnlp.Config.TraditionalChineseDictionaryPath, trie, true)) {
            throw new IllegalArgumentException("简繁词典" + Xmnlp.Config.TraditionalChineseDictionaryPath + Predefine.REVERSE_EXT + "加载失败");
        }

        logger.info("简繁词典" + Xmnlp.Config.TraditionalChineseDictionaryPath + Predefine.REVERSE_EXT + "加载成功，耗时" + (System.currentTimeMillis() - start) + "ms");
    }

    public static String convertToTraditionalChinese(String simplifiedChineseString) {
        return segLongest(simplifiedChineseString.toCharArray(), trie);
    }

    public static String convertToTraditionalChinese(char[] simplifiedChinese) {
        return segLongest(simplifiedChinese, trie);
    }

    public static String getTraditionalChinese(String simplifiedChinese) {
        return trie.get(simplifiedChinese);
    }
}
