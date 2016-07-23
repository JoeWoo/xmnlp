package org.xm.xmnlp.demo;

import org.xm.xmnlp.dic.WordDict;
import org.xm.xmnlp.segword.SegMode;
import org.xm.xmnlp.segword.Segmenter;

import java.nio.file.Paths;

/**
 * Created by xuming on 2016/5/26.
 */
public class UnitNameDemo {

    public static void main(String[] args) {



        WordDict.getInstance().init(Paths.get("conf"));
        Segmenter segmenter = new Segmenter();
        String[] sentences =
                new String[]{"不輸入簡體字,阿丁说你很好，" +
                        "汕头市和亨玩具有限公司\n" +
                        "江西婺华园蜂业有限责任公司\n" };
        System.out.println("----------------------");
        for (String sentence : sentences) {
            System.out.println(segmenter.process(sentence, SegMode.SEARCH).toString());
        }
    }


}
