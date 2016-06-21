package org.xm.segWord;

import org.xm.util.StaticUtils;

import java.util.HashMap;

/**
 * Created by mingzai on 2016/6/21.
 * 规则分词
 */
public class Rule {

    private static final HashMap<String, int[]> dictionary = StaticUtils.getDicMap();


    /**
     *
     * @Title: 反向最大匹配
     * @Author: xuming
     * @Description:
     * @date:2016/6/21 22:50
     * @param input String
     * @return void
     */
    public static String ReverseMaxSegmentation(String input){
        if(input==null || input.isEmpty()){
            return null;
        }
        int max_words = input.length();
        StringBuffer result = new StringBuffer();
        while(input.length()>0) {
            String temp;
            //temp就是待分词的短语
            if( input.length()<max_words ) {
                temp = input;
            }else {
                temp = input.substring(input.length() - max_words);
            }

            while(temp.length()>0) {
                if(dictionary.get(temp)!=null || temp.length()==1) {
                    //如果在字典中找到某个词，这个词被加入到分词结果中同时从原始输入中删除这个词
                    result = new StringBuffer(temp).append("/").append(result);
                    input = input.substring(0, input.length() - temp.length());
                    break;
                }
                else{
                    //待分词短语从左向右不断变短
                    temp = temp.substring(1);
                }
            }
        }
        System.out.println(result);
        return result.toString();
    }


    /**
     *
     * @Title: 正向最大匹配
     * @Author: xuming
     * @Description:
     * @date:2016/6/21 22:51
     * @param input String
     * @return void
     */
    public static String ForwardMaxSegmentation(String input) {
        StringBuffer result = new StringBuffer();

        if(input==null || input.isEmpty()){
            return null;
        }
        int max_words = input.length();


        while(input.length()>0) {
            String temp;
            if( input.length()<max_words ) {
                temp = input;
            }else {
                temp = input.substring(0, max_words);
            }
            while(temp.length()>0) {
                if(dictionary.get(temp)!=null || temp.length()==1) {
                    result = result.append(temp).append("/");
                    input = input.substring(temp.length());
                    break;
                }
                else{
                    temp = temp.substring(0,temp.length()-1);
                }
            }
        }
        System.out.println(result);
        return result.toString();
    }

    public static void main(String[] args){
        System.out.println("start");
        ForwardMaxSegmentation("北京天安门广场人民币种");
        ReverseMaxSegmentation("北京天安门广场人民币种");
    }

}
