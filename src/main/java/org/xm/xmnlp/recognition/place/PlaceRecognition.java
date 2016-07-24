package org.xm.xmnlp.recognition.place;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.corpus.dictionary.item.EnumItem;
import org.xm.xmnlp.corpus.tag.NS;
import org.xm.xmnlp.corpus.tag.Nature;
import org.xm.xmnlp.dictionary.place.PlaceDictionary;
import org.xm.xmnlp.math.Viterbi;
import org.xm.xmnlp.seg.domain.Vertex;
import org.xm.xmnlp.seg.domain.WordNet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * 地名识别类
 * Created by mingzai on 2016/7/24.
 */
public class PlaceRecognition {
    public static boolean recognition(List<Vertex> words, WordNet wordNetOptimum, WordNet wordNetAll) {
        List<EnumItem<NS>> roleTagList = getRoleTags(words);
        if (Xmnlp.Config.DEBUG) {
            StringBuffer sbLog = new StringBuffer();
            Iterator<Vertex> iterator = words.iterator();
            for (EnumItem<NS> nsEnumItem : roleTagList) {
                sbLog.append('[');
                sbLog.append(iterator.next().realWord);
                sbLog.append(' ');
                sbLog.append(nsEnumItem);
                sbLog.append(']');
            }
            System.out.printf("地名角色观察：%s\n", sbLog.toString());
        }
        List<NS> nsList = viterbiExCompute(roleTagList);
        if (Xmnlp.Config.DEBUG) {
            StringBuilder sbLog = new StringBuilder();
            Iterator<Vertex> iterator = words.iterator();
            sbLog.append('[');
            for (NS ns : nsList) {
                sbLog.append(iterator.next().realWord);
                sbLog.append('/');
                sbLog.append(ns);
                sbLog.append(" ,");
            }
            if (sbLog.length() > 1) sbLog.delete(sbLog.length() - 2, sbLog.length());
            sbLog.append(']');
            System.out.printf("地名角色标注：%s\n", sbLog.toString());
        }

        PlaceDictionary.parsePattern(nsList, words, wordNetOptimum, wordNetAll);
        return true;
    }

    public static List<EnumItem<NS>> getRoleTags(List<Vertex> vertexList) {
        List<EnumItem<NS>> tagList = new LinkedList<EnumItem<NS>>();
        ListIterator<Vertex> listIterator = vertexList.listIterator();
        while (listIterator.hasNext()) {
            Vertex vertex = listIterator.next();
            if (Nature.ns == vertex.getNature() && vertex.getAttribute().totalFrequency <= 1000) {
                if (vertex.realWord.length() < 3)               // 二字地名，认为其可以再接一个后缀或前缀
                    tagList.add(new EnumItem<NS>(NS.H, NS.G));
                else
                    tagList.add(new EnumItem<NS>(NS.G));        // 否则只可以再加后缀
                continue;
            }
            EnumItem<NS> NSEnumItem = PlaceDictionary.dictionary.get(vertex.word);  // 此处用等效词，更加精准
            if (NSEnumItem == null) {
                NSEnumItem = new EnumItem<NS>(NS.Z, PlaceDictionary.transformMatrixDictionary.getTotalFrequency(NS.Z));
            }
            tagList.add(NSEnumItem);
        }
        return tagList;
    }

    private static void insert(ListIterator<Vertex> listIterator, List<EnumItem<NS>> tagList, WordNet wordNetAll, int line, NS ns) {
        Vertex vertex = wordNetAll.getFirst(line);
        assert vertex != null : "全词网居然有空白行！";
        listIterator.add(vertex);
        tagList.add(new EnumItem<NS>(ns, 1000));
    }

    /**
     * 维特比算法求解最优标签
     *
     * @param roleTagList
     * @return
     */
    public static List<NS> viterbiExCompute(List<EnumItem<NS>> roleTagList) {
        return Viterbi.computeEnum(roleTagList, PlaceDictionary.transformMatrixDictionary);
    }

}
