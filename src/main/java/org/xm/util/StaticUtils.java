package org.xm.util;


import org.xm.dic.DicReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by mingzai on 2016/6/21.
 */
public class StaticUtils {

    private static HashMap<String, int[]> cnMap = null;

    public static HashMap<String, int[]> getDicMap() {

        if (cnMap != null) {
            return cnMap;
        }
        try {
            initDic();
        } catch (Exception e) {
            e.printStackTrace();
            cnMap = new HashMap<String, int[]>();
        }
        return cnMap;
    }

    public static void initDic() throws NumberFormatException, IOException {
        // TODO Auto-generated method stub
        BufferedReader br = null;
        try {
            cnMap = new HashMap<String, int[]>();
            br = StaticUtils.getDicReader();
            String temp = null;
            String[] strs = null;
            int[] cna = null;
            while ((temp = br.readLine()) != null) {
                strs = temp.split("\t");
                cna = new int[2];
                cna[0] = Integer.parseInt(strs[1]);
                cna[1] = Integer.parseInt(strs[2]);
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
    public static BufferedReader getDicReader() {
        return DicReader.getReader("core.dic");
    }
}
