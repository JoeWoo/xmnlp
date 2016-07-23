package org.xm.xmnlp.dictionary;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.util.Predefine;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import static org.xm.xmnlp.util.Predefine.logger;

/**
 * 核心词典的二元接续词典，采用整型储存，高性能
 * Created by mingzai on 2016/7/23.
 */
public class CoreBiGramTableDictionary {
    /**
     * 描述了词在pair中的范围，具体说来<br>
     * 给定一个词idA，从pair[start[idA]]开始的start[idA + 1] - start[idA]描述了一些接续的频次
     */
    static int start[];
    /**
     * pair[偶数n]表示key，pair[n+1]表示frequency
     */
    static int pair[];

    public final static String path = Xmnlp.Config.BiGramDictionaryPath;
    private final static String datPath = Xmnlp.Config.BiGramDictionaryPath + ".table" + Predefine.BIN_EXT;

    static {
        logger.info("开始加载二元词典" + path + ".table");
        long start = System.currentTimeMillis();
        if (!load(path)) {
            logger.severe("二元词典加载失败！");
            System.exit(-1);
        } else {
            logger.info(path + ".table 加载成功，耗时：" + (System.currentTimeMillis() - start) + "ms");
        }
    }

    private static boolean load(String path) {
        if (loadDat(datPath)) return true;
        BufferedReader br;
        TreeMap<Integer, TreeMap<Integer, Integer>> map = new TreeMap<Integer, TreeMap<Integer, Integer>>();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            String line;
            int total = 0;
            int maxWordId = CoreDictionary.trie.size();
            while ((line = br.readLine()) != null) {
                String[] params = line.split("\\s");
                String[] twoWord = params[0].split("@", 2);
                String a = twoWord[0];
                int idA = CoreDictionary.trie.exactMatchSearch(a);
                if (idA == -1) {
                    continue;
                }
                String b = twoWord[1];
                int idB = CoreDictionary.trie.exactMatchSearch(b);
                if (idB == -1) {
                    continue;
                }
                int freq = Integer.parseInt(params[1]);
                TreeMap<Integer, Integer> biMap = map.get(idA);
                if (biMap == null) {
                    biMap = new TreeMap<Integer, Integer>();
                    map.put(idA, biMap);
                }
                biMap.put(idB, freq);
                total += 2;
            }
            br.close();
            start = new int[maxWordId + 1];
            pair = new int[total];
            int offset = 0;
            for (int i = 0; i < maxWordId; ++i) {
                TreeMap<Integer, Integer> bMap = map.get(i);
                if (bMap != null) {
                    for (Map.Entry<Integer, Integer> entry : bMap.entrySet()) {
                        int index = offset << 1;
                        pair[index] = entry.getKey();
                        pair[index + 1] = entry.getValue();
                        ++offset;
                    }
                }
                start[i + 1] = offset;
            }
            logger.info("二元词典读取完毕：" + path + ",构建为TableBin结构");
        } catch (FileNotFoundException e) {
            logger.severe("二元词典" + path + "不存在！" + e);
            return false;
        } catch (IOException e) {
            logger.severe("二元词典" + path + "读取错误！" + e);
            return false;
        }
        logger.info("开始缓存二元词典到" + datPath);
        if (!saveDat(datPath)) {
            logger.warning("缓存二元词典到" + datPath + "失败！");
        }
        return true;
    }

    private static boolean saveDat(String path) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
            out.writeObject(start);
            out.writeObject(pair);
            out.close();
        } catch (Exception e) {
            logger.log(Level.WARNING, "在缓存" + path + "时发生异常", e);
            return false;
        }

        return true;
    }

    private static boolean loadDat(String path) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
            start = (int[]) in.readObject();
            if (CoreDictionary.trie.size() != start.length - 1)     // 目前CoreNatureDictionary.ngram.txt的缓存依赖于CoreNatureDictionary.txt的缓存
            {                                                       // 所以这里校验一下二者的一致性，不然可能导致下标越界或者ngram错乱的情况
                in.close();
                return false;
            }
            pair = (int[]) in.readObject();
            in.close();
        } catch (Exception e) {
            logger.warning("尝试载入缓存文件" + path + "发生异常[" + e + "]，下面将载入源文件并自动缓存……");
            return false;
        }
        return true;
    }

    /**
     * 二分搜索，由于二元接续前一个词固定时，后一个词比较少，所以二分也能取得很高的性能
     *
     * @param a         目标数组
     * @param fromIndex 开始下标
     * @param length    长度
     * @param key       词的id
     * @return 共现频次
     */
    private static int binarySearch(int[] a, int fromIndex, int length, int key) {
        int low = fromIndex;
        int high = fromIndex + length - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = a[mid << 1];

            if (midVal < key)
                low = mid + 1;
            else if (midVal > key)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found.
    }

    /**
     * 获取共现频次
     *
     * @param a 第一个词
     * @param b 第二个词
     * @return 第一个词@第二个词出现的频次
     */
    public static int getBiFrequency(String a, String b) {
        int idA = CoreDictionary.trie.exactMatchSearch(a);
        if (idA == -1) {
            return 0;
        }
        int idB = CoreDictionary.trie.exactMatchSearch(b);
        if (idB == -1) {
            return 0;
        }
        int index = binarySearch(pair, start[idA], start[idA + 1] - start[idA], idB);
        if (index < 0) return 0;
        index <<= 1;
        return pair[index + 1];
    }

    /**
     * 获取共现频次
     *
     * @param idA 第一个词的id
     * @param idB 第二个词的id
     * @return 共现频次
     */
    public static int getBiFrequency(int idA, int idB) {
        if (idA == -1 || idB == -1) {
            return 1000;   // -1表示用户词典，返回正值增加其亲和度
        }
        int index = binarySearch(pair, start[idA], start[idA + 1] - start[idA], idB);
        if (index < 0) return 0;
        index <<= 1;
        return pair[index + 1];
    }

    /**
     * 获取词语的ID
     *
     * @param a 词语
     * @return id
     */
    public static int getWordID(String a) {
        return CoreDictionary.trie.exactMatchSearch(a);
    }
}