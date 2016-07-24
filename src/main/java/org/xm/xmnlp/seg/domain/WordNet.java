package org.xm.xmnlp.seg.domain;


import org.xm.xmnlp.corpus.tag.Nature;
import org.xm.xmnlp.dictionary.CoreDictionary;
import org.xm.xmnlp.math.MathTools;
import org.xm.xmnlp.seg.nshort.path.AtomNode;
import org.xm.xmnlp.util.Predefine;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 词网
 * Created by mingzai on 2016/7/23.
 */
public class WordNet {
    private LinkedList<Vertex> vertexes[];
    int size;
    public String sentence;

    /**
     * source sentence to array
     */
    public char[] charArray;

    public WordNet(String sentence) {
        this(sentence.toCharArray());
    }


    public WordNet(char[] charArray) {
        this.charArray = charArray;
        vertexes = new LinkedList[charArray.length + 2];
        for (int i = 0; i < vertexes.length; ++i) {
            vertexes[i] = new LinkedList<Vertex>();
        }
        vertexes[0].add(Vertex.newB());
        vertexes[vertexes.length - 1].add(Vertex.newE());
        size = 2;
    }

    public WordNet(char[] charArray, List<Vertex> vertexList) {
        this.charArray = charArray;
        vertexes = new LinkedList[charArray.length + 2];
        for (int i = 0; i < vertexes.length; ++i) {
            vertexes[i] = new LinkedList<Vertex>();
        }
        int i = 0;
        for (Vertex vertex : vertexList) {
            vertexes[i].add(vertex);
            ++size;
            i += vertex.realWord.length();
        }
    }

    /**
     * 添加顶点
     *
     * @param line   行号
     * @param vertex 顶点
     */
    public void add(int line, Vertex vertex) {
        for (Vertex oldVertex : vertexes[line]) {
            // 保证唯一性
            if (oldVertex.realWord.length() == vertex.realWord.length()) return;
        }
        vertexes[line].add(vertex);
        ++size;
    }

    /**
     * 强行添加，替换已有的顶点
     *
     * @param line
     * @param vertex
     */
    public void push(int line, Vertex vertex) {
        Iterator<Vertex> iterator = vertexes[line].iterator();
        while (iterator.hasNext()) {
            if (iterator.next().realWord.length() == vertex.realWord.length()) {
                iterator.remove();
                --size;
                break;
            }
        }
        vertexes[line].add(vertex);
        ++size;
    }

    /**
     * 添加顶点，同时检查此顶点是否悬孤，如果悬孤则自动补全
     *
     * @param line
     * @param vertex
     * @param wordNetAll 这是一个完全的词图
     */
    public void insert(int line, Vertex vertex, WordNet wordNetAll) {
        for (Vertex oldVertex : vertexes[line]) {
            // 保证唯一性
            if (oldVertex.realWord.length() == vertex.realWord.length()) return;
        }
        vertexes[line].add(vertex);
        ++size;
        // 保证连接
        for (int l = line - 1; l > 1; --l) {
            if (get(l, 1) == null) {
                Vertex first = wordNetAll.getFirst(l);
                if (first == null) break;
                vertexes[l].add(first);
                ++size;
                if (vertexes[l].size() > 1) break;
            } else {
                break;
            }
        }
        // 首先保证这个词语可直达
        int l = line + vertex.realWord.length();
        if (get(l).size() == 0) {
            List<Vertex> targetLine = wordNetAll.get(l);
            if (targetLine == null || targetLine.size() == 0) return;
            vertexes[l].addAll(targetLine);
            size += targetLine.size();
        }
        // 直达之后一直往后
        for (++l; l < vertexes.length; ++l) {
            if (get(l).size() == 0) {
                Vertex first = wordNetAll.getFirst(l);
                if (first == null) break;
                vertexes[l].add(first);
                ++size;
                if (vertexes[l].size() > 1) break;
            } else {
                break;
            }
        }
    }

    /**
     * 全自动添加顶点
     *
     * @param vertexList
     */
    public void addAll(List<Vertex> vertexList) {
        int i = 0;
        for (Vertex vertex : vertexList) {
            add(i, vertex);
            i += vertex.realWord.length();
        }
    }

    /**
     * 获取某一行的所有节点
     *
     * @param line 行号
     * @return 一个数组
     */
    public List<Vertex> get(int line) {
        return vertexes[line];
    }

    /**
     * 获取某一行的第一个节点
     *
     * @param line
     * @return
     */
    public Vertex getFirst(int line) {
        Iterator<Vertex> iterator = vertexes[line].iterator();
        if (iterator.hasNext()) return iterator.next();
        return null;
    }

    /**
     * 获取某一行长度为length的节点
     *
     * @param line
     * @param length
     * @return
     */
    public Vertex get(int line, int length) {
        for (Vertex vertex : vertexes[line]) {
            if (vertex.realWord.length() == length) {
                return vertex;
            }
        }
        return null;
    }

    /**
     * 添加顶点，由原子分词顶点添加
     *
     * @param line
     * @param atomSegment
     */
    public void add(int line, List<AtomNode> atomSegment) {
        // 将原子部分存入m_segGraph
        int offset = 0;
        for (AtomNode atomNode : atomSegment)//Init the cost array
        {
            String sWord = atomNode.sWord;//initUserDic the word
            Nature nature = Nature.n;
            int id = -1;
            switch (atomNode.nPOS) {
                case Predefine.CT_CHINESE:
                    break;
                case Predefine.CT_INDEX:
                case Predefine.CT_NUM:
                    nature = Nature.m;
                    sWord = "未##数";
                    id = CoreDictionary.M_WORD_ID;
                    break;
                case Predefine.CT_DELIMITER:
                case Predefine.CT_OTHER:
                    nature = Nature.w;
                    break;
                case Predefine.CT_SINGLE://12021-2129-3121
                    nature = Nature.nx;
                    sWord = "未##串";
                    id = CoreDictionary.X_WORD_ID;
                    break;
                default:
                    break;
            }
            // 这些通用符的量级都在10万左右
            add(line + offset, new Vertex(sWord, atomNode.sWord, new CoreDictionary.Attribute(nature, 10000), id));
            offset += atomNode.sWord.length();
        }
    }

    public int size() {
        return size;
    }


    /**
     * 获取顶点数组
     *
     * @return Vertex[] 按行优先列次之的顺序构造的顶点数组
     */
    private Vertex[] getVertexesLineFirst() {
        Vertex[] vertexes = new Vertex[size];
        int i = 0;
        for (List<Vertex> vertexList : this.vertexes) {
            for (Vertex v : vertexList) {
                v.index = i;    // 设置id
                vertexes[i++] = v;
            }
        }

        return vertexes;
    }


    /**
     * 词网转词图
     *
     * @return 词图
     */
    public Graph toGraph() {
        Graph graph = new Graph(getVertexesLineFirst());

        for (int row = 0; row < vertexes.length - 1; ++row) {
            List<Vertex> vertexListFrom = vertexes[row];
            for (Vertex from : vertexListFrom) {
                assert from.realWord.length() > 0 : "空节点会导致死循环！";
                int toIndex = row + from.realWord.length();
                for (Vertex to : vertexes[toIndex]) {
                    graph.connect(from.index, to.index, MathTools.calculateWeight(from, to));
                }
            }
        }
        return graph;
    }

    /**
     * 清空词图
     */
    public void clear() {
        for (List<Vertex> vertexList : vertexes) {
            vertexList.clear();
        }
        size = 0;
    }

    /**
     * 获取内部顶点表格，谨慎操作！
     *
     * @return
     */
    public LinkedList<Vertex>[] getVertexes() {
        return vertexes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int line = 0;
        for (List<Vertex> vertexList : vertexes) {
            sb.append(String.valueOf(line++) + ':' + vertexList.toString()).append("\n");
        }
        return sb.toString();
    }
}
