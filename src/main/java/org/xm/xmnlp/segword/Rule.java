package org.xm.xmnlp.segword;


import org.xm.xmnlp.util.StaticUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

/**
 * 规则分词
 *
 * xuming
 */
public class Rule {

    private static final HashMap<String, String[]> dictionary = StaticUtils.getRuleDicMap();


    /**
     * @Title: 反向最大匹配
     */
    public Vector<String> ReverseMaxSeg(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        int max_words = 16;
        Vector<String> results = new Vector<String>();
        while (input.length() > 0) {
            String temp;
            //temp就是待分词的短语
            if (input.length() < max_words) {
                temp = input;
            } else {
                temp = input.substring(input.length() - max_words);
            }
            while (temp.length() > 0) {
                if (dictionary.get(temp) != null || temp.length() == 1) {
                    //如果在字典中找到某个词，这个词被加入到分词结果中同时从原始输入中删除这个词
                    results.add(temp);
                    input = input.substring(0, input.length() - temp.length());
                    break;
                } else {
                    //待分词短语从左向右不断变短
                    temp = temp.substring(1);
                }
            }
        }
        Collections.reverse(results);
        return results;
    }


    /**
     * @Title: 正向最大匹配
     */
    public Vector<String> ForwardMaxSeg(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        int max_words = 16;
        Vector<String> results = new Vector<String>();
        while (input.length() > 0) {
            String temp;
            if (input.length() < max_words) {
                temp = input;
            } else {
                temp = input.substring(0, max_words);
            }
            while (temp.length() > 0) {
                if (dictionary.get(temp) != null || temp.length() == 1) {
                    results.add(temp);
                    input = input.substring(temp.length());
                    break;
                } else {
                    temp = temp.substring(0, temp.length() - 1);
                }
            }
        }
        return results;
    }

    /**
     * @Title: 双向匹配分词 Bidirectional maximum matching
     * fresults 正向匹配的分词结果
     * bresults 逆向匹配的分词结果
     */
    public Vector<String> BIMaxSeg(String input) {
        Vector<String> fresults = ForwardMaxSeg(input);
        Vector<String> bresults = ReverseMaxSeg(input);
        //如果正反向分词结果词数不同，则取分词数量较少的那个
        if (fresults.size() != bresults.size()) {
            if (fresults.size() > bresults.size())
                return bresults;
            else
                return fresults;
        }
        //如果分词结果词数相同
        else {
            //如果正反向的分词结果相同，就说明没有歧义，可返回任意一个
            int i, FSingle = 0, BSingle = 0;
            boolean isSame = true;
            for (i = 0; i < fresults.size(); i++) {
                if (!fresults.get(i).equals(bresults.get(i)))
                    isSame = false;
                if (fresults.get(i).length() == 1)
                    FSingle += 1;
                if (bresults.get(i).length() == 1)
                    BSingle += 1;
            }
            if (isSame)
                return fresults;
            else {
                //分词结果不同，返回其中单字较少的那个
                if (BSingle > FSingle)
                    return fresults;
                else
                    return bresults;
            }
        }
    }

}
