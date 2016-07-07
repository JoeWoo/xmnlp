package org.xm.util;


import org.xm.dic.DicReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by mingzai on 2016/6/21.
 */
public class StaticUtils {

    private static HashMap<String, String[]> cnMap = null;

    public static HashMap<String, String[]> getRuleDicMap() {
        if (cnMap != null) {
            return cnMap;
        }
        try {
            initRuleDicMap();
        } catch (Exception e) {
            e.printStackTrace();
            cnMap = new HashMap<String, String[]>();
        }
        return cnMap;
    }

    public static void initRuleDicMap() throws NumberFormatException, IOException {
        BufferedReader br = null;
        try {
            cnMap = new HashMap<String, String[]>();
            br = StaticUtils.getRuleDicReader();
            String temp = null;
            String[] strs = null;
            String[] cna = null;
            while ((temp = br.readLine()) != null) {
                strs = temp.split("[\t]");
                cna = new String[2];
                if(strs.length == 2){
                    cna[0] = strs[1];
                }
                if(strs.length == 3){
                    cna[1] = strs[2].trim();
                }
                cnMap.put(strs[0], cna);
            }
        } finally {
            if (br != null)
                br.close();
        }
    }

    /**
     * 词典
     *
     * @return
     */
    public static BufferedReader getRuleDicReader() {
        return DicReader.getReader("ruledict.txt");
    }
}
