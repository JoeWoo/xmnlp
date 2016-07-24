package org.xm.xmnlp.recognition.organ;


import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.corpus.dictionary.item.EnumItem;
import org.xm.xmnlp.corpus.tag.NT;
import org.xm.xmnlp.corpus.tag.Nature;
import org.xm.xmnlp.dictionary.organ.OrganizationDictionary;
import org.xm.xmnlp.math.Viterbi;
import org.xm.xmnlp.seg.domain.Vertex;
import org.xm.xmnlp.seg.domain.WordNet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 地址识别
 */
public class OrganizationRecognition {
    public static boolean recognition(List<Vertex> pWordSegResult, WordNet wordNetOptimum, WordNet wordNetAll) {
        List<EnumItem<NT>> roleTagList = getRoleTags(pWordSegResult);
        if (Xmnlp.Config.DEBUG) {
            StringBuilder sbLog = new StringBuilder();
            Iterator<Vertex> iterator = pWordSegResult.iterator();
            for (EnumItem<NT> NTEnumItem : roleTagList) {
                sbLog.append('[');
                sbLog.append(iterator.next().realWord);
                sbLog.append(' ');
                sbLog.append(NTEnumItem);
                sbLog.append(']');
            }
            System.out.printf("机构名角色观察：%s\n", sbLog.toString());
        }
        List<NT> NTList = viterbiExCompute(roleTagList);
        if (Xmnlp.Config.DEBUG) {
            StringBuilder sbLog = new StringBuilder();
            Iterator<Vertex> iterator = pWordSegResult.iterator();
            sbLog.append('[');
            for (NT NT : NTList) {
                sbLog.append(iterator.next().realWord);
                sbLog.append('/');
                sbLog.append(NT);
                sbLog.append(" ,");
            }
            if (sbLog.length() > 1) sbLog.delete(sbLog.length() - 2, sbLog.length());
            sbLog.append(']');
            System.out.printf("机构名角色标注：%s\n", sbLog.toString());
        }

        OrganizationDictionary.parsePattern(NTList, pWordSegResult, wordNetOptimum, wordNetAll);
        return true;
    }

    public static List<EnumItem<NT>> getRoleTags(List<Vertex> vertexList) {
        List<EnumItem<NT>> tagList = new LinkedList<EnumItem<NT>>();
        for (Vertex vertex : vertexList) {
            // 构成更长的
            Nature nature = vertex.guessNature();
            switch (nature) {
                case nz: {
                    if (vertex.getAttribute().totalFrequency <= 1000) {
                        tagList.add(new EnumItem<NT>(NT.F, 1000));
                    } else break;
                }
                continue;
                case ni:
                case nic:
                case nis:
                case nit: {
                    EnumItem<NT> ntEnumItem = new EnumItem<NT>(NT.K, 1000);
                    ntEnumItem.addLabel(NT.D, 1000);
                    tagList.add(ntEnumItem);
                }
                continue;
                case m: {
                    EnumItem<NT> ntEnumItem = new EnumItem<NT>(NT.M, 1000);
                    tagList.add(ntEnumItem);
                }
                continue;
            }

            EnumItem<NT> NTEnumItem = OrganizationDictionary.dictionary.get(vertex.word);  // 此处用等效词，更加精准
            if (NTEnumItem == null) {
                NTEnumItem = new EnumItem<NT>(NT.Z, OrganizationDictionary.transformMatrixDictionary.getTotalFrequency(NT.Z));
            }
            tagList.add(NTEnumItem);
        }
        return tagList;
    }

    /**
     * 维特比算法求解最优标签
     *
     * @param roleTagList
     * @return
     */
    public static List<NT> viterbiExCompute(List<EnumItem<NT>> roleTagList) {
        return Viterbi.computeEnum(roleTagList, OrganizationDictionary.transformMatrixDictionary);
    }
}
