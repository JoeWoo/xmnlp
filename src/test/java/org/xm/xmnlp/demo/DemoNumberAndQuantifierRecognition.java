package org.xm.xmnlp.demo;


import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.tokenizer.StandardTokenizer;

/**
 * 演示数词和数量词识别
 */
public class DemoNumberAndQuantifierRecognition {
    public static void main(String[] args) {
        String[] testCase = new String[]
                {
                        "十九元套餐包括什么",
                        "九千九百九十九朵玫瑰",
                        "壹佰块都不给我",
                        "９０１２３４５６７８只蚂蚁",
                        "牛奶三〇〇克*2",
                        "ChinaJoy“扫黄”细则露胸超2厘米罚款",
                        "23只玫瑰4353个小人二十一元钱买吃的",
                };
        System.out.println("未开启数词和数量词识别的效果：" + Xmnlp.segment("十九元套餐包括什么九千九百九十九朵玫瑰壹佰块都不给我"));
        System.out.println("-----------------");
        StandardTokenizer.SEGMENT.enableNumberQuantifierRecognize(true);
        for (String sentence : testCase) {
            System.out.println(StandardTokenizer.segment(sentence));
        }
    }
}
