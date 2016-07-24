package org.xm.xmnlp.corpus.dictionary;


import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.AbstractMap;
import java.util.Map;

import static org.xm.xmnlp.util.Predefine.logger;


/**
 * 满足 key=value 格式的词典，其中“=”可以自定义
 *
 */
public class StringDictionary extends SimpleDictionary<String> {
    /**
     * key value之间的分隔符
     */
    protected String separator;

    public StringDictionary(String separator) {
        this.separator = separator;
    }

    public StringDictionary() {
        this("=");
    }

    @Override
    protected Map.Entry<String, String> onGenerateEntry(String line) {
        String[] paramArray = line.split(separator, 2);
        if (paramArray.length != 2) {
            logger.warning("词典有一行读取错误： " + line);
            return null;
        }
        return new AbstractMap.SimpleEntry<String, String>(paramArray[0], paramArray[1]);
    }

    /**
     * 保存词典
     *
     * @param path
     * @return 是否成功
     */
    public boolean save(String path) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)));
            for (Map.Entry<String, String> entry : trie.entrySet()) {
                bw.write(entry.getKey());
                bw.write(separator);
                bw.write(entry.getValue());
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            logger.warning("保存词典到" + path + "失败");
            return true;
        }
        return false;
    }

    /**
     * 将自己逆转过来返回
     *
     * @return
     */
    public StringDictionary reverse() {
        StringDictionary dictionary = new StringDictionary(separator);
        for (Map.Entry<String, String> entry : entrySet()) {
            dictionary.trie.put(entry.getValue(), entry.getKey());
        }

        return dictionary;
    }
}
