package org.xm.xmnlp.summary;

import org.xm.xmnlp.math.MaxHeap;
import org.xm.xmnlp.seg.domain.Term;

import java.util.*;

/**
 * 关键字提取，单文档用
 * Created by xuming on 2016/7/26.
 */
public class TextRankKeyword extends KeywordExtractor {
    /**
     * 提取关键字的个数
     */
    int nKeyword = 10;
    /**
     * 阻尼系数，一般取值0.85
     */
    final static float d = 0.85f;
    /**
     * 最大迭代次数
     */
    final static int MAX_ITER = 200;

    final static float MIN_DIFF = 0.001f;

    /**
     * 提取关键词
     * @param sentence 文档内容
     * @param size 希望提取几个关键词
     * @return 结果列表
     */
    public static List<String> getKeywordList(String sentence, int size) {
        TextRankKeyword textRankKeyword = new TextRankKeyword();
        textRankKeyword.nKeyword = size;
        return textRankKeyword.getKeyword(sentence);
    }

    private List<String> getKeyword(String sentence) {
        Set<Map.Entry<String, Float>> entrySet = getTermAndRank(sentence, nKeyword).entrySet();
        List<String> result = new ArrayList<String>(entrySet.size());
        for (Map.Entry<String, Float> entry : entrySet) {
            result.add(entry.getKey());
        }
        return result;
    }

    /**
     * 返回全部分词结果和对应的rank
     *
     * @param sentence
     * @return Map
     */
    public Map<String, Float> getTermAndRank(String sentence) {
        assert sentence != null;
        List<Term> termList = defaultSegment.seg(sentence);
        return getRank(termList);
    }

    /**
     * 返回分数最高的前size个分词结果和对应的排名
     *
     * @param sentence 待分词句子
     * @param size     个数
     * @return Map
     */
    private Map getTermAndRank(String sentence, int size) {
        Map<String, Float> map = getTermAndRank(sentence);
        Map<String, Float> result = new LinkedHashMap<String, Float>();
        for (Map.Entry<String, Float> entry : new MaxHeap<Map.Entry<String, Float>>(size, new Comparator<Map.Entry<String, Float>>() {
            @Override
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        }).addAll(map.entrySet()).toList()) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * 使用已经分好的词来计算排名
     *
     * @param termList 分好的词
     * @return Map
     */
    private Map getRank(List<Term> termList) {
        List<String> wordList = new ArrayList<String>(termList.size());
        for (Term t : termList) {
            if (shouldInclude(t)) {
                wordList.add(t.word);
            }
        }
        Map<String, Set<String>> words = new TreeMap<String, Set<String>>();
        Queue<String> queue = new LinkedList<String>();
        for (String w : wordList) {
            if (!words.containsKey(w)) {
                words.put(w, new TreeSet<String>());
            }
            queue.offer(w);
            if (queue.size() > 5) {
                queue.poll();
            }
            for (String w1 : queue) {
                for (String w2 : queue) {
                    if (w1.equals(w2)) {
                        continue;
                    }
                    words.get(w1).add(w2);
                    words.get(w2).add(w1);
                }
            }
        }
        Map<String, Float> score = new HashMap<String, Float>();
        for (int i = 0; i < MAX_ITER; ++i) {
            Map<String, Float> m = new HashMap<String, Float>();
            float max_diff = 0;
            for (Map.Entry<String, Set<String>> entry : words.entrySet()) {
                String key = entry.getKey();
                Set<String> value = entry.getValue();
                m.put(key, 1 - d);
                for (String element : value) {
                    int size = words.get(element).size();
                    if (key.equals(element) || size == 0) continue;
                    m.put(key, m.get(key) + d / size * (score.get(element) == null ? 0 : score.get(element)));

                }
                max_diff = Math.max(max_diff, Math.abs(m.get(key) - (score.get(key) == null ? 0 : score.get(key))));
            }
            score = m;
            if (max_diff <= MIN_DIFF) break;
        }
        return score;
    }
}
