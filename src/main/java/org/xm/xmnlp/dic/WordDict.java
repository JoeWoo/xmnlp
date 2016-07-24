package org.xm.xmnlp.dic;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;


/**
 * 加载jieba分词词库和模型
 * Created by xuming on 2016/7/6.
 */
public class WordDict {
    private static WordDict wordDict;
    private static final String MAIN_DICT = "/core.txt";
    private static String USER_DICT = "/user.txt";

    public final Map<String, Double> freqs = new HashMap<String, Double>();
    public final Map<String, String> natures = new HashMap<String, String>();
    public final Set<String> loadedPath = new HashSet<String>();
    private Double minFreq = Double.MAX_VALUE;
    private Double total = 0.0;
    private DictSegment dict;

    private WordDict() {
        this.loadCoreDict();
        this.loadUserDict(USER_DICT);
    }

    public static WordDict getInstance() {
        if (wordDict == null) {
            synchronized (WordDict.class) {
                if (wordDict == null) {
                    wordDict = new WordDict();
                    return wordDict;
                }
            }
        }
        return wordDict;
    }

    public void initUserDic(String userDicPath) {
        System.out.println("initialize user dictionary:" + userDicPath);
        synchronized (WordDict.class) {
            if (loadedPath.contains(userDicPath))
                return;
//            System.err.println(String.format(Locale.getDefault(), "loading dict %s", userDicPath.toString()));
            wordDict.loadUserDict(userDicPath);
            loadedPath.add(userDicPath);
        }
    }

    public void loadCoreDict() {
        dict = new DictSegment((char) 0);
        InputStream is = this.getClass().getResourceAsStream(MAIN_DICT);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            long s = System.currentTimeMillis();
            while (br.ready()) {
                String line = br.readLine();
                String[] tokens = line.split("[\t ]+");

                if (tokens.length < 2)
                    continue;

                String word = tokens[0];
                double freq = Double.valueOf(tokens[1]);
                String nature = String.valueOf(tokens[2]);
                total += freq;
                word = addWord(word);
                freqs.put(word, freq);
                natures.put(word, nature);
            }
            // normalize
            for (Map.Entry<String, Double> entry : freqs.entrySet()) {
                entry.setValue((Math.log(entry.getValue() / total)));
                minFreq = Math.min(entry.getValue(), minFreq);
            }
            System.out.println(String.format(Locale.getDefault(), "main dict load finished, time elapsed %d ms",
                    System.currentTimeMillis() - s));
        } catch (IOException e) {
            System.err.println(String.format(Locale.getDefault(), "%s load failure!", MAIN_DICT));
        } finally {
            try {
                if (null != is)
                    is.close();
            } catch (IOException e) {
                System.err.println(String.format(Locale.getDefault(), "%s close failure!", MAIN_DICT));
            }
        }
    }

    private String addWord(String word) {
        if (null != word && !"".equals(word.trim())) {
            String key = word.trim().toLowerCase(Locale.getDefault());
            dict.fillSegment(key.toCharArray());
            return key;
        } else
            return null;
    }


    public void loadUserDict(String userDictPath) {
        loadUserDict(userDictPath, Charset.forName("UTF-8"));
    }


    /**
     * 加载自定义词典
     *
     * @param userDictPath
     * @param charset
     */
    public void loadUserDict(String userDictPath, Charset charset) {
        InputStream is = this.getClass().getResourceAsStream(userDictPath);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, charset)); //Files.newBufferedReader(userDict, charset);
            long s = System.currentTimeMillis();
            int count = 0;
            while (br.ready()) {
                String line = br.readLine();
                String[] tokens = line.split("[\t ]+");//空格和tab都允许的正则写法
                if (tokens.length < 2) {// Ignore empty line
                    continue;
                }
                String word = tokens[0];
                double freq = 3.0d;
                String nature = "";
                if (tokens.length == 2) freq = Double.valueOf(tokens[1]);
                if (tokens.length == 3) nature = String.valueOf(tokens[2]);
                word = addWord(word);
                total += freq;
                freqs.put(word, Math.log(freq / total));
                natures.put(word, nature);
                count++;
            }
            System.out.println(String.format(Locale.getDefault(), "user dict %s load finished, total words:%d, time elapsed:%dms", userDictPath.toString(), count, System.currentTimeMillis() - s));
            if (br != null) br.close();
        } catch (IOException e) {
            System.err.println(String.format(Locale.getDefault(), "%s: load user dict failure!", userDictPath.toString()));
        } finally {
            try {
                if (null != is)
                    is.close();
            } catch (IOException e) {
                System.err.println(String.format(Locale.getDefault(), "%s close failure!", MAIN_DICT));
            }
        }
    }


    public DictSegment getTrie() {
        return this.dict;
    }


    public boolean containsWord(String word) {
        return freqs.containsKey(word);
    }

    public boolean containsNature(String word) {
        return natures.containsKey(word);
    }


    public Double getFreq(String key) {
        if (containsWord(key))
            return freqs.get(key);
        else
            return minFreq;
    }

    public String getNature(String key) {
        if (containsNature(key))
            return natures.get(key);
        else
            return "";
    }


}
