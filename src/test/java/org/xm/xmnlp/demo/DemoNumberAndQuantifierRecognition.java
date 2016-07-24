package org.xm.xmnlp.demo;


import org.xm.xmnlp.tokenizer.StandardTokenizer;

/**
 * 演示数词和数量词识别
 */
public class DemoNumberAndQuantifierRecognition {
    public static void main(String[] args) {
        StandardTokenizer.SEGMENT.enableNumberQuantifierRecognize(true);
        String[] testCase = new String[]
                {
                        "十九元套餐包括什么23只玫瑰4353个小人二十二元钱买吃的",
                        "九千九百九十九朵玫瑰",
                        "壹佰块都不给我",
                        "９０１２３４５６７８只蚂蚁",
                        "牛奶三〇〇克*2",
                        "ChinaJoy“扫黄”细则露胸超2厘米罚款",
                };
        for (String sentence : testCase) {
            System.out.println(StandardTokenizer.segment(sentence));
        }
    }
}
