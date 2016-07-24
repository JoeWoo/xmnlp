package org.xm.xmnlp.dictionary.traditionalsimplified;


import org.xm.xmnlp.collection.ahocorasick.AhoCorasickDoubleArrayTrie;
import org.xm.xmnlp.collection.trie.DoubleArrayTrie;
import org.xm.xmnlp.corpus.dictionary.StringDictionary;
import org.xm.xmnlp.corpus.io.ByteArray;
import org.xm.xmnlp.dictionary.BaseSearcher;
import org.xm.xmnlp.util.Predefine;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import static org.xm.xmnlp.util.Predefine.logger;

/**
 * 基础词典
 */
public class BaseChineseDictionary {
    /**
     * 将path的内容载入trie中
     *
     * @param path
     * @param trie
     * @return
     */
    static boolean load(String path, AhoCorasickDoubleArrayTrie<String> trie) {
        return load(path, trie, false);
    }

    /**
     * 读取词典
     *
     * @param path
     * @param trie
     * @param reverse 是否将其翻转
     * @return
     */
    static boolean load(String path, AhoCorasickDoubleArrayTrie<String> trie, boolean reverse) {
        String datPath = path;
        if (reverse) {
            datPath += Predefine.REVERSE_EXT;
        }
        if (loadDat(datPath, trie)) return true;
        // 从文本中载入并且尝试生成dat
        StringDictionary dictionary = new StringDictionary("=");
        if (!dictionary.load(path)) return false;
        if (reverse) dictionary = dictionary.reverse();
        Set<Map.Entry<String, String>> entrySet = dictionary.entrySet();
        TreeMap<String, String> map = new TreeMap<String, String>();
        for (Map.Entry<String, String> entry : entrySet) {
            map.put(entry.getKey(), entry.getValue());
        }
        logger.info("正在构建AhoCorasickDoubleArrayTrie，来源：" + path);
        trie.build(map);
        logger.info("正在缓存双数组" + datPath);
        saveDat(datPath, trie, entrySet);
        return true;
    }

    static boolean loadDat(String path, AhoCorasickDoubleArrayTrie<String> trie) {
        ByteArray byteArray = ByteArray.createByteArray(path + Predefine.BIN_EXT);
        if (byteArray == null) return false;
        int size = byteArray.nextInt();
        String[] valueArray = new String[size];
        for (int i = 0; i < valueArray.length; ++i) {
            valueArray[i] = byteArray.nextString();
        }
        trie.load(byteArray, valueArray);
        return true;
    }

    static boolean saveDat(String path, AhoCorasickDoubleArrayTrie<String> trie, Set<Map.Entry<String, String>> entrySet) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(path + Predefine.BIN_EXT));
            out.writeInt(entrySet.size());
            for (Map.Entry<String, String> entry : entrySet) {
                char[] charArray = entry.getValue().toCharArray();
                out.writeInt(charArray.length);
                for (char c : charArray) {
                    out.writeChar(c);
                }
            }
            trie.save(out);
            out.close();
        } catch (Exception e) {
            logger.warning("缓存值dat" + path + "失败");
            return false;
        }

        return true;
    }

    public static BaseSearcher getSearcher(char[] charArray, DoubleArrayTrie<String> trie) {
        return new Searcher(charArray, trie);
    }

    protected static String segLongest(char[] charArray, DoubleArrayTrie<String> trie) {
        StringBuilder sb = new StringBuilder(charArray.length);
        BaseSearcher searcher = getSearcher(charArray, trie);
        Map.Entry<String, String> entry;
        int p = 0;  // 当前处理到什么位置
        int offset;
        while ((entry = searcher.next()) != null) {
            offset = searcher.getOffset();
            // 补足没查到的词
            while (p < offset) {
                sb.append(charArray[p]);
                ++p;
            }
            sb.append(entry.getValue());
            p = offset + entry.getKey().length();
        }
        // 补足没查到的词
        while (p < charArray.length) {
            sb.append(charArray[p]);
            ++p;
        }
        return sb.toString();
    }

    protected static String segLongest(char[] charArray, AhoCorasickDoubleArrayTrie<String> trie) {
        final String[] wordNet = new String[charArray.length];
        final int[] lengthNet = new int[charArray.length];
        trie.parseText(charArray, new AhoCorasickDoubleArrayTrie.IHit<String>() {
            @Override
            public void hit(int begin, int end, String value) {
                int length = end - begin;
                if (length > lengthNet[begin]) {
                    wordNet[begin] = value;
                    lengthNet[begin] = length;
                }
            }
        });
        StringBuilder sb = new StringBuilder(charArray.length);
        for (int offset = 0; offset < wordNet.length; ) {
            if (wordNet[offset] == null) {
                sb.append(charArray[offset]);
                ++offset;
                continue;
            }
            sb.append(wordNet[offset]);
            offset += lengthNet[offset];
        }
        return sb.toString();
    }

    /**
     * 最长分词
     */
    public static class Searcher extends BaseSearcher<String> {
        /**
         * 分词从何处开始，这是一个状态
         */
        int begin;

        DoubleArrayTrie<String> trie;

        protected Searcher(char[] c, DoubleArrayTrie<String> trie) {
            super(c);
            this.trie = trie;
        }

        protected Searcher(String text, DoubleArrayTrie<String> trie) {
            super(text);
            this.trie = trie;
        }

        @Override
        public Map.Entry<String, String> next() {
            // 保证首次调用找到一个词语
            Map.Entry<String, String> result = null;
            while (begin < c.length) {
                LinkedList<Map.Entry<String, String>> entryList = trie.commonPrefixSearchWithValue(c, begin);
                if (entryList.size() == 0) {
                    ++begin;
                } else {
                    result = entryList.getLast();
                    offset = begin;
                    begin += result.getKey().length();
                    break;
                }
            }
            if (result == null) {
                return null;
            }
            return result;
        }
    }
}
