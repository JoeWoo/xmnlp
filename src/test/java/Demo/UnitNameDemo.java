package Demo;

import org.xm.dic.WordDict;
import org.xm.segWord.SegMode;
import org.xm.segWord.Segmenter;

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
                        "庆云县宏远运输有限公司\n" +
                        "义乌市高迪酒业有限公司\n" +
                        "江阴双夏进出口有限公司\n" +
                        "太原桓通贸易有限公司\n" +
                        "杭州创力电器有限公司\n" +
                        "广州市晟法贸易有限公司\n" +
                        "武汉依之莎卫浴有限公司\n" +
                        "常州市润华调节器材有限公司\n" +
                        "江西婺华园蜂业有限责任公司\n" +
                        "北京新世纪认证有限公司\n" +
                        "南通华钰电力配套机械制造有限公司\n" +
                        "宜昌市奔朗贸易有限责任公司\n" +
                        "寿光市临峰能源有限责任公司\n" +
                        "新疆天拓工贸有限公司\n" +
                        "成都星瑞农业有限公司\n" +
                        "嵩县华伊印刷有限公司\n" +
                        "清河县奥尼特羊绒纺织有限公司\n" +
                        "常州市创达热固塑料有限公司\n" +
                        "上海瑞禾房地产发展有限公司\n" +
                        "深圳市中建物资有限公司\n" +
                        "淄博铭宝不锈钢有限公司\n" +
                        "沈阳中圣商贸有限公司\n" +
                        "珠海市德兴达文具有限公司\n" +
                        "广州市金海泰制衣有限公司\n" +
                        "博罗县园洲朗高设计有限公司\n" +
                        "北京惠美利康商贸有限公司\n" +
                        "江苏亿腾化工有限公司\n" +
                        "济南中和本草医药技术有限公司\n",
                        "东海县迅捷贸易有限责任公司"};
        System.out.println("----------------------");
        for (String sentence : sentences) {
            System.out.println(segmenter.process(sentence, SegMode.SEARCH).toString());
        }
    }


}
