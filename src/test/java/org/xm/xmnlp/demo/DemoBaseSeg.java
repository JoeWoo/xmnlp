package org.xm.xmnlp.demo;

import org.xm.xmnlp.seg.Config;
import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.tokenizer.StandardTokenizer;

import java.util.List;

/**
 * Created by mingzai on 2016/7/23.
 */
public class DemoBaseSeg {
    public static void main(String[] args) {
//        Xmnlp.Config.Normalization = true;
        Config config = new Config();
        config.useCustomDictionary = false;
        List<Term> termList = StandardTokenizer.segment("商品和服务,符号,你好%都是符号，特殊符号￥％，ｘｉ大写转小写：F字母区别FFDr，進行在線轉換，中国人名共和国万岁,著名胡同在刘家湾？");
        System.out.println(termList);
    }
}
