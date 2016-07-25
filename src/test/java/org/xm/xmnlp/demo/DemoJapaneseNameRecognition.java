package org.xm.xmnlp.demo;


import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.seg.Segment;
import org.xm.xmnlp.seg.domain.Term;

import java.util.List;

/**
 * 日本人名识别
 */
public class DemoJapaneseNameRecognition {
    public static void main(String[] args) {
        String[] testCase = new String[]{
                "北川景子参演了林诣彬导演的《速度与激情3》",
                "林志玲亮相网友:确定不是波多野结衣？",
                "龟山千广和近藤公园在龟山公园里喝酒赏花朋友川岛芳子房在家里",
        };
        Segment segment = Xmnlp.newSegment().enableJapaneseNameRecognize(false);
        for (String sentence : testCase) {
            List<Term> termList = segment.seg(sentence);
            System.out.println(termList);
        }
    }
}
