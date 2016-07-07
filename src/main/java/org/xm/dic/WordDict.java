package org.xm.dic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


/**
 * Created by xuming on 2016/7/6.
 */
public class WordDict {
    private static WordDict wordDict;
    private static final String MAIN_DICT="/core.txt";
    private static String USER_DICT_SUFFIX = ".dict";

    public final Map<String,Double> freqs = new HashMap<String,Double>();
    public final Map<String,String> natures = new HashMap<String,String>();
    public final Set<String> loadedPath = new HashSet<String>();
    private Double minFreq = Double.MAX_VALUE;
    private Double total = 0.0;
    private DictSegment dict;

    // 用户自定义词典
    public static final Map<String, Object> DIC = new HashMap<String, Object>();

    // CRF模型
    public static final Map<String, Object> CRF = new HashMap<String, Object>();
    /**
     * 用户自定义词典的加载,如果是路径就扫描路径下的dic文件
     */
    public static String ambiguityLibrary = "library/ambiguity.di";


    private WordDict(){this.loadDict();}

    public static WordDict getInstance(){
        if(wordDict == null){
            synchronized (WordDict.class){
                if(wordDict == null){
                    wordDict = new WordDict();
                    return wordDict;
                }
            }

        }
        return wordDict;
    }
    public void init(Path configFile) {
        String abspath = configFile.toAbsolutePath().toString();
        System.out.println("initialize user dictionary:" + abspath);
        synchronized (WordDict.class) {
            if (loadedPath.contains(abspath))
                return;

            DirectoryStream<Path> stream;
            try {
                stream = Files.newDirectoryStream(configFile, String.format(Locale.getDefault(), "*%s", USER_DICT_SUFFIX));
                for (Path path: stream){
                    System.err.println(String.format(Locale.getDefault(), "loading dict %s", path.toString()));
                    wordDict.loadUserDict(path);
                }
                loadedPath.add(abspath);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
                System.err.println(String.format(Locale.getDefault(), "%s: load user dict failure!", configFile.toString()));
            }
        }
    }
    public void loadDict() {
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
        }
        catch (IOException e) {
            System.err.println(String.format(Locale.getDefault(), "%s load failure!", MAIN_DICT));
        }
        finally {
            try {
                if (null != is)
                    is.close();
            }
            catch (IOException e) {
                System.err.println(String.format(Locale.getDefault(), "%s close failure!", MAIN_DICT));
            }
        }
    }
    private String addWord(String word) {
        if (null != word && !"".equals(word.trim())) {
            String key = word.trim().toLowerCase(Locale.getDefault());
            dict.fillSegment(key.toCharArray());
            return key;
        }
        else
            return null;
    }


   public void loadUserDict(Path userDict) {
        loadUserDict(userDict, StandardCharsets.UTF_8);
    }


    public void loadUserDict(Path userDict, Charset charset) {
        try {
            BufferedReader br = Files.newBufferedReader(userDict, charset);
            long s = System.currentTimeMillis();
            int count = 0;


            while (br.ready()) {
                String line = br.readLine();
                String[] tokens = line.split("[\t ]+");

                if (tokens.length < 2) {
                    // Ignore empty line
                    continue;
                }

                String word = tokens[0];

                double freq = 3.0d;
                String nature ="";
                if (tokens.length == 2)
                    freq = Double.valueOf(tokens[1]);
                if (tokens.length == 3)
                    nature = String.valueOf(tokens[2]);
                word = addWord(word);
                total += freq;
                freqs.put(word, Math.log(freq / total));
                natures.put(word, nature);
                count++;
            }
            System.out.println(String.format(Locale.getDefault(), "user dict %s load finished, total words:%d, time elapsed:%dms", userDict.toString(), count, System.currentTimeMillis() - s));
            br.close();
        }
        catch (IOException e) {
            System.err.println(String.format(Locale.getDefault(), "%s: load user dict failure!", userDict.toString()));
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
    /**
     * 人名词典
     *
     * @return
     */
    public static BufferedReader getPersonReader() {
        return DicReader.getReader("person.txt");
    }

}
