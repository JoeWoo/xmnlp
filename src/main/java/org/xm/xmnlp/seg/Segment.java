package org.xm.xmnlp.seg;


import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.collection.trie.DoubleArrayTrie;
import org.xm.xmnlp.collection.trie.bintrie.BaseNode;
import org.xm.xmnlp.corpus.tag.Nature;
import org.xm.xmnlp.dictionary.CoreDictionary;
import org.xm.xmnlp.dictionary.CustomDictionary;
import org.xm.xmnlp.dictionary.other.CharTable;
import org.xm.xmnlp.dictionary.other.CharType;
import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.seg.domain.Vertex;
import org.xm.xmnlp.seg.domain.WordNet;
import org.xm.xmnlp.seg.nshort.path.AtomNode;
import org.xm.xmnlp.util.Predefine;
import org.xm.xmnlp.util.SentencesUtil;
import org.xm.xmnlp.util.TextUtil;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static org.xm.xmnlp.util.Predefine.logger;

/**
 * 分词器（分词服务）<br>
 * 是所有分词器的基类（Abstract）<br>
 * 分词器的分词方法是线程安全的，但配置方法则不保证
 * <p/>
 * Created by xuming on 2016/7/22.
 */
public abstract class Segment {
    /**
     * 分词器配置
     */
    protected Config config;

    /**
     * 构造一个分词器
     */
    public Segment() {
        config = new Config();
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
        if (Xmnlp.Config.Normalization) {
            CharTable.normalization(text);
        }
        return segSentence(text);
    }

    /**
     * 分词断句 输出句子形式
     *
     * @param text 待分词句子
     * @return 句子列表，每个句子由一个单词列表组成
     */
    public List<List<Term>> seg2sentence(String text) {
        List<List<Term>> resultList = new LinkedList<List<Term>>();
        for (String sentence : SentencesUtil.toSentenceList(text)) {
            resultList.add(segSentence(sentence.toCharArray()));
        }

        return resultList;
    }

    /**
     * 快速原子分词，希望用这个方法替换掉原来缓慢的方法
     *
     * @param charArray
     * @param start
     * @param end
     * @return
     */
    protected static List<AtomNode> quickAtomSegment(char[] charArray, int start, int end) {
        List<AtomNode> atomNodeList = new LinkedList<AtomNode>();
        int offsetAtom = start;
        int preType = CharType.get(charArray[offsetAtom]);
        int curType;
        while (++offsetAtom < end) {
            curType = CharType.get(charArray[offsetAtom]);
            if (curType != preType) {
                // 浮点数识别
                if (charArray[offsetAtom] == '.' && preType == CharType.CT_NUM) {
                    while (++offsetAtom < end) {
                        curType = CharType.get(charArray[offsetAtom]);
                        if (curType != CharType.CT_NUM) break;
                    }
                }
                atomNodeList.add(new AtomNode(new String(charArray, start, offsetAtom - start), preType));
                start = offsetAtom;
            }
            preType = curType;
        }
        if (offsetAtom == end)
            atomNodeList.add(new AtomNode(new String(charArray, start, offsetAtom - start), preType));

        return atomNodeList;
    }

    /**
     * 使用用户词典合并粗分结果
     *
     * @param vertexList 粗分结果
     * @return 合并后的结果
     */
    protected static List<Vertex> combineByCustomDictionary(List<Vertex> vertexList) {
        Vertex[] wordNet = new Vertex[vertexList.size()];
        vertexList.toArray(wordNet);
        // DAT合并
        DoubleArrayTrie<CoreDictionary.Attribute> dat = CustomDictionary.dat;
        for (int i = 0; i < wordNet.length; ++i) {
            int state = 1;
            state = dat.transition(wordNet[i].realWord, state);
            if (state > 0) {
                int start = i;
                int to = i + 1;
                int end = to;
                CoreDictionary.Attribute value = dat.output(state);
                for (; to < wordNet.length; ++to) {
                    state = dat.transition(wordNet[to].realWord, state);
                    if (state < 0) break;
                    CoreDictionary.Attribute output = dat.output(state);
                    if (output != null) {
                        value = output;
                        end = to + 1;
                    }
                }
                if (value != null) {
                    StringBuilder sbTerm = new StringBuilder();
                    for (int j = start; j < end; ++j) {
                        sbTerm.append(wordNet[j]);
                        wordNet[j] = null;
                    }
                    wordNet[i] = new Vertex(sbTerm.toString(), value);
                    i = end - 1;
                }
            }
        }
        // BinTrie合并
        if (CustomDictionary.trie != null) {
            for (int i = 0; i < wordNet.length; ++i) {
                if (wordNet[i] == null) continue;
                BaseNode<CoreDictionary.Attribute> state = CustomDictionary.trie.transition(wordNet[i].realWord.toCharArray(), 0);
                if (state != null) {
                    int start = i;
                    int to = i + 1;
                    int end = to;
                    CoreDictionary.Attribute value = state.getValue();
                    for (; to < wordNet.length; ++to) {
                        if (wordNet[to] == null) continue;
                        state = state.transition(wordNet[to].realWord.toCharArray(), 0);
                        if (state == null) break;
                        if (state.getValue() != null) {
                            value = state.getValue();
                            end = to + 1;
                        }
                    }
                    if (value != null) {
                        StringBuilder sbTerm = new StringBuilder();
                        for (int j = start; j < end; ++j) {
                            if (wordNet[j] == null) continue;
                            sbTerm.append(wordNet[j]);
                            wordNet[j] = null;
                        }
                        wordNet[i] = new Vertex(sbTerm.toString(), value);
                        i = end - 1;
                    }
                }
            }
        }
        vertexList.clear();
        for (Vertex vertex : wordNet) {
            if (vertex != null) vertexList.add(vertex);
        }
        return vertexList;
    }

    /**
     * 合并数字
     *
     * @param termList
     */
    protected void mergeNumberQuantifier(List<Vertex> termList, WordNet wordNetAll, Config config) {
        if (termList.size() < 4) return;
        StringBuilder sbQuantifier = new StringBuilder();
        ListIterator<Vertex> iterator = termList.listIterator();
        iterator.next();
        int line = 1;
        while (iterator.hasNext()) {
            Vertex pre = iterator.next();
            if (pre.hasNature(Nature.m)) {
                sbQuantifier.append(pre.realWord);
                Vertex cur = null;
                while (iterator.hasNext() && (cur = iterator.next()).hasNature(Nature.m)) {
                    sbQuantifier.append(cur.realWord);
                    iterator.remove();
                    removeFromWordNet(cur, wordNetAll, line, sbQuantifier.length());
                }
                if (cur != null) {
                    if ((cur.hasNature(Nature.q) || cur.hasNature(Nature.qv) || cur.hasNature(Nature.qt))) {
                        if (config.indexMode) {
                            wordNetAll.add(line, new Vertex(sbQuantifier.toString(), new CoreDictionary.Attribute(Nature.m)));
                        }
                        sbQuantifier.append(cur.realWord);
                        iterator.remove();
                        removeFromWordNet(cur, wordNetAll, line, sbQuantifier.length());
                    } else {
                        line += cur.realWord.length();   // (cur = iterator.next()).hasNature(Nature.m) 最后一个next可能不含q词性
                    }
                }
                if (sbQuantifier.length() != pre.realWord.length()) {
                    pre.realWord = sbQuantifier.toString();
                    pre.word = Predefine.TAG_NUMBER;
                    pre.attribute = new CoreDictionary.Attribute(Nature.mq);
                    pre.wordID = CoreDictionary.M_WORD_ID;
                    sbQuantifier.setLength(0);
                }
            }
            sbQuantifier.setLength(0);
            line += pre.realWord.length();
        }
        if (Xmnlp.Config.DEBUG) {
            System.out.println(wordNetAll);
        }
    }

    /**
     * 将一个词语从词网中彻底抹除
     *
     * @param cur        词语
     * @param wordNetAll 词网
     * @param line       当前扫描的行数
     * @param length     当前缓冲区的长度
     */
    private static void removeFromWordNet(Vertex cur, WordNet wordNetAll, int line, int length) {
        LinkedList<Vertex>[] vertexes = wordNetAll.getVertexes();
        // 将其从wordNet中删除
        for (Vertex vertex : vertexes[line + length]) {
            if (vertex.from == cur)
                vertex.from = null;
        }
        ListIterator<Vertex> iterator = vertexes[line + length - cur.realWord.length()].listIterator();
        while (iterator.hasNext()) {
            Vertex vertex = iterator.next();
            if (vertex == cur) iterator.remove();
        }
    }

    /**
     * 分词<br>
     * 此方法是线程安全的
     *
     * @param text 待分词文本
     * @return 单词列表
     */
    public List<Term> seg(String text) {
        char[] charArray = text.toCharArray();
        if (Xmnlp.Config.Normalization) {
            CharTable.normalization(charArray);
        }
        if (config.threadNumber > 1 && charArray.length > 10000) { // 小文本多线程没意义，反而变慢了
            List<String> sentenceList = SentencesUtil.toSentenceList(charArray);
            String[] sentenceArray = new String[sentenceList.size()];
            sentenceList.toArray(sentenceArray);
            //noinspection unchecked
            List<Term>[] termListArray = new List[sentenceArray.length];
            final int per = sentenceArray.length / config.threadNumber;
            WorkThread[] threadArray = new WorkThread[config.threadNumber];
            for (int i = 0; i < config.threadNumber - 1; ++i) {
                int from = i * per;
                threadArray[i] = new WorkThread(sentenceArray, termListArray, from, from + per);
                threadArray[i].start();
            }
            threadArray[config.threadNumber - 1] = new WorkThread(sentenceArray, termListArray, (config.threadNumber - 1) * per, sentenceArray.length);
            threadArray[config.threadNumber - 1].start();
            try {
                for (WorkThread thread : threadArray) {
                    thread.join();  // 主线程需要用到子线程的结果，主线程等子线程都执行完毕
                }
            } catch (InterruptedException e) {
                logger.severe("线程同步异常：" + TextUtil.exceptionToString(e));
                return Collections.emptyList();
            }
            List<Term> termList = new LinkedList<Term>();
            if (config.offset || config.indexMode) { // 由于分割了句子，所以需要重新校正offset
                int sentenceOffset = 0;
                for (int i = 0; i < sentenceArray.length; ++i) {
                    for (Term term : termListArray[i]) {
                        term.offset += sentenceOffset;
                        termList.add(term);
                    }
                    sentenceOffset += sentenceArray[i].length();
                }
            } else {
                for (List<Term> list : termListArray) {
                    termList.addAll(list);
                }
            }

            return termList;
        }
        return segSentence(charArray);
    }

    private class WorkThread extends Thread {
        String[] sentenceArray;
        List<Term>[] termListArray;
        int from;
        int to;

        public WorkThread(String[] sentenceArray, List<Term>[] termListArray, int from, int to) {
            this.sentenceArray = sentenceArray;
            this.termListArray = termListArray;
            this.from = from;
            this.to = to;
        }

        @Override
        public void run() {
            for (int i = from; i < to; ++i) {
                termListArray[i] = segSentence(sentenceArray[i].toCharArray());
            }
        }
    }

    /**
     * 开启多线程
     *
     * @param enable true表示开启4个线程，false表示单线程
     * @return
     */
    public Segment enableMultithreading(boolean enable) {
        if (enable) config.threadNumber = 4;
        else config.threadNumber = 1;
        return this;
    }

    /**
     * 开启多线程
     *
     * @param threadNumber 线程数量
     * @return
     */
    public Segment enableMultithreading(int threadNumber) {
        config.threadNumber = threadNumber;
        return this;
    }

    /**
     * 是否开启人名识别
     *
     * @param enable
     * @return
     */
    public Segment enableNameRecognize(boolean enable) {
        config.nameRecognize = enable;
        config.updateNerConfig();
        return this;
    }

    /**
     * 是否启用音译人名识别
     *
     * @param enable
     */
    public Segment enableTranslatedNameRecognize(boolean enable) {
        config.translatedNameRecognize = enable;
        config.updateNerConfig();
        return this;
    }

    /**
     * 是否启用日本人名识别
     *
     * @param enable
     */
    public Segment enableJapaneseNameRecognize(boolean enable) {
        config.japaneseNameRecognize = enable;
        config.updateNerConfig();
        return this;
    }

    /**
     * 开启地名识别
     *
     * @param enable
     * @return
     */
    public Segment enablePlaceRecognize(boolean enable) {
        config.placeRecognize = enable;
        config.updateNerConfig();
        return this;
    }

    /**
     * 开启机构名识别
     *
     * @param enable
     * @return
     */
    public Segment enableOrganizationRecognize(boolean enable) {
        config.organizationRecognize = enable;
        config.updateNerConfig();
        return this;
    }

    /**
     * 是否启用用户词典
     *
     * @param enable
     */
    public Segment enableCustomDictionary(boolean enable) {
        config.useCustomDictionary = enable;
        return this;
    }


    /**
     * 是否启用偏移量计算（开启后Term.offset才会被计算）
     *
     * @param enable
     * @return
     */
    public Segment enableOffset(boolean enable) {
        config.offset = enable;
        return this;
    }

    /**
     * 是否启用数词和数量词识别<br>
     * 即[二, 十, 一] => [二十一]，[十, 九, 元] => [十九元]
     *
     * @param enable
     * @return
     */
    public Segment enableNumberQuantifierRecognize(boolean enable) {
        config.numberQuantifierRecognize = enable;
        return this;
    }

    /**
     * 是否启用所有的命名实体识别
     *
     * @param enable
     * @return
     */
    public Segment enableAllNamedEntityRecognize(boolean enable) {
        config.nameRecognize = enable;
        config.japaneseNameRecognize = enable;
        config.translatedNameRecognize = enable;
        config.placeRecognize = enable;
        config.organizationRecognize = enable;
        config.updateNerConfig();
        return this;
    }

    /**
     * 设为索引模式
     *
     * @return
     */
    public Segment enableIndexMode(boolean enable) {
        config.indexMode = enable;
        return this;
    }

    /**
     * 开启词性标注
     *
     * @param enable
     * @return
     */
    public Segment enablePartOfSpeechTagging(boolean enable) {
        config.speechTagging = enable;
        return this;
    }

}
