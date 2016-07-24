package org.xm.xmnlp.recognition.person;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.corpus.dictionary.item.EnumItem;
import org.xm.xmnlp.corpus.tag.NR;
import org.xm.xmnlp.dictionary.person.PersonDictionary;
import org.xm.xmnlp.math.Viterbi;
import org.xm.xmnlp.seg.domain.Vertex;
import org.xm.xmnlp.seg.domain.WordNet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 人名识别
 * Created by mingzai on 2016/7/23.
 */
public class PersonRecognition {
    public static boolean recognition(List<Vertex> words, WordNet wordNetOptimum, WordNet wordNetAll) {
        List<EnumItem<NR>> roleTagList = getRoleTags(words);
        if (Xmnlp.Config.DEBUG) {
            StringBuffer sbLog = new StringBuffer();
            Iterator<Vertex> iterator = words.iterator();
            for (EnumItem<NR> nrEnumItem : roleTagList) {
                sbLog.append('[');
                sbLog.append(iterator.next().realWord);
                sbLog.append(' ');
                sbLog.append(nrEnumItem);
                sbLog.append(']');
            }
            System.out.printf("人名角色观察：%s\n", sbLog.toString());
        }
        List<NR> nrList = viterbiComputerSimply(roleTagList);
        if (Xmnlp.Config.DEBUG) {
            StringBuilder sbLog = new StringBuilder();
            Iterator<Vertex> iterator = words.iterator();
            sbLog.append('[');
            for (NR nr : nrList) {
                sbLog.append(iterator.next().realWord);
                sbLog.append('/');
                sbLog.append(nr);
                sbLog.append(" ,");
            }
            if (sbLog.length() > 1) sbLog.delete(sbLog.length() - 2, sbLog.length());
            sbLog.append(']');
            System.out.printf("人名角色标注：%s\n", sbLog.toString());
        }

        PersonDictionary.parsePattern(nrList, words, wordNetOptimum, wordNetAll);
        return true;
    }

    /**
     * 角色观察(从模型中加载所有词语对应的所有角色,允许进行一些规则补充)
     *
     * @param wordSegResult 粗分结果
     * @return
     */
    public static List<EnumItem<NR>> getRoleTags(List<Vertex> wordSegResult) {
        List<EnumItem<NR>> tagList = new LinkedList<EnumItem<NR>>();
        for (Vertex vertex : wordSegResult) {
            EnumItem<NR> nrEnumItem = PersonDictionary.dictionary.get(vertex.realWord);
            if (nrEnumItem == null) {
                switch (vertex.guessNature()) {
                    case nr: {
                        // 有些双名实际上可以构成更长的三名
                        if (vertex.getAttribute().totalFrequency <= 1000 && vertex.realWord.length() == 2) {
                            nrEnumItem = new EnumItem<NR>(NR.X, NR.G);
                        } else
                            nrEnumItem = new EnumItem<NR>(NR.A, PersonDictionary.transformMatrixDictionary.getTotalFrequency(NR.A));
                    }
                    break;
                    case nnt: {
                        // 姓+职位
                        nrEnumItem = new EnumItem<NR>(NR.G, NR.K);
                    }
                    break;
                    default: {
                        nrEnumItem = new EnumItem<NR>(NR.A, PersonDictionary.transformMatrixDictionary.getTotalFrequency(NR.A));
                    }
                    break;
                }
            }
            tagList.add(nrEnumItem);
        }
        return tagList;
    }

    /**
     * 简化的"维特比算法"求解最优标签
     *
     * @param roleTagList
     * @return
     */
    private static List<NR> viterbiComputerSimply(List<EnumItem<NR>> roleTagList) {
        return Viterbi.computeEnumSimply(roleTagList, PersonDictionary.transformMatrixDictionary);
    }

    /**
     * 维特比算法求解最优标签
     *
     * @param roleTagList
     * @return
     */
    public static List<NR> viterbiCompute(List<EnumItem<NR>> roleTagList) {
        return Viterbi.computeEnum(roleTagList, PersonDictionary.transformMatrixDictionary);
    }
}
