package org.xm.xmnlp.demo;


import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.seg.Segment;
import org.xm.xmnlp.seg.domain.Term;

import java.util.List;

/**
 * 机构名识别
 */
public class DemoOrganizationRecognition {
    public static void main(String[] args) {
        String[] testCase = new String[]{
                "我在上海辉煌有限公司兼职工作，",
                "我经常在台川喜宴餐厅吃饭，",
                "偶尔去开元地中海影城看电影。",
        };
        Segment segment = Xmnlp.newSegment().enableOrganizationRecognize(true);
        for (String sentence : testCase) {
            List<Term> termList = segment.seg(sentence);
            System.out.println(termList);
        }
    }
}
