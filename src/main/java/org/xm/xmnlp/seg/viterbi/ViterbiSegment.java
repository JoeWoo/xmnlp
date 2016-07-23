package org.xm.xmnlp.seg.viterbi;


import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.recognition.person.JapanesePersonRecognition;
import org.xm.xmnlp.recognition.person.PersonRecognition;
import org.xm.xmnlp.recognition.person.TranslatedPersonRecognition;
import org.xm.xmnlp.seg.WordBasedModelSegment;
import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.seg.domain.Vertex;
import org.xm.xmnlp.seg.domain.WordNet;

import java.util.LinkedList;
import java.util.List;

/**
 * Viterbi分词器<br>
 * 也是最短路分词，最短路求解采用Viterbi算法
 * Created by mingzai on 2016/7/23.
 */
public class ViterbiSegment extends WordBasedModelSegment {
    @Override
    protected List<Term> segSentence(char[] sentence) {
        WordNet wordNetAll = new WordNet(sentence);
        GenerateWordNet(wordNetAll);//生成词网
        if (Xmnlp.Config.DEBUG) {
            System.out.printf("粗粉词网：\n%s \n", wordNetAll);
        }
        List<Vertex> vertexList = viterbi(wordNetAll);
        if (config.useCustomDictionary) {
            combineByCustomDictionary(vertexList);
        }

        if (Xmnlp.Config.DEBUG) {
            System.out.println("粗分结果" + convert(vertexList, false));
        }
        // 数字识别
        if (config.numberQuantifierRecognize) {
            mergeNumberQuantifier(vertexList, wordNetAll, config);
        }

        // 实体命名识别
        if (config.ner) {
            WordNet wordNetOptimum = new WordNet(sentence, vertexList);
            int preSize = wordNetOptimum.size();
            if (config.nameRecognize) {
                PersonRecognition.recognition(vertexList, wordNetOptimum, wordNetAll);
            }
            if (config.translatedNameRecognize) {
                TranslatedPersonRecognition.Recognition(vertexList, wordNetOptimum, wordNetAll);
            }
            if (config.japaneseNameRecognize) {
                JapanesePersonRecognition.Recognition(vertexList, wordNetOptimum, wordNetAll);
            }
            /*if (config.placeRecognize) {
                PlaceRecognition.Recognition(vertexList, wordNetOptimum, wordNetAll);
            }
            if (config.organizationRecognize) {
                // 层叠隐马模型——生成输出作为下一级隐马输入
                vertexList = viterbi(wordNetOptimum);
                wordNetOptimum.clear();
                wordNetOptimum.addAll(vertexList);
                preSize = wordNetOptimum.size();
                OrganizationRecognition.Recognition(vertexList, wordNetOptimum, wordNetAll);
            }*/
            if (wordNetOptimum.size() != preSize) {
                vertexList = viterbi(wordNetOptimum);
                if (Xmnlp.Config.DEBUG) {
                    System.out.printf("细分词网：\n%s\n", wordNetOptimum);
                }
            }
        }
/*
        // 如果是索引模式则全切分
        if (config.indexMode) {
            return decorateResultForIndexMode(vertexList, wordNetAll);
        }

        // 是否标注词性
        if (config.speechTagging) {
            speechTagging(vertexList);
        }*/

        return convert(vertexList, config.offset);
    }

    private static List<Vertex> viterbi(WordNet wordNet) {
        // 避免生成对象，优化速度
        LinkedList<Vertex> nodes[] = wordNet.getVertexes();
        LinkedList<Vertex> vertexList = new LinkedList<Vertex>();
        for (Vertex node : nodes[1]) {
            node.updateFrom(nodes[0].getFirst());
        }
        for (int i = 1; i < nodes.length - 1; ++i) {
            LinkedList<Vertex> nodeArray = nodes[i];
            if (nodeArray == null) continue;
            for (Vertex node : nodeArray) {
                if (node.from == null) continue;
                for (Vertex to : nodes[i + node.realWord.length()]) {
                    to.updateFrom(node);
                }
            }
        }
        Vertex from = nodes[nodes.length - 1].getFirst();
        while (from != null) {
            vertexList.addFirst(from);
            from = from.from;
        }
        return vertexList;
    }


}
