package org.xm.xmnlp.demo;


import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.seg.Segment;
import org.xm.xmnlp.seg.domain.Term;

import java.util.List;

/**
 * 地名识别
 */
public class DemoPlaceRecognition {
    public static void main(String[] args) {
        String[] testCase = new String[]{
                "蓝翔给宁夏固原市彭阳县红河镇黑牛沟村捐赠了挖掘机，杭州娃哈哈集团有限公司\n" +
                        "大冶有色金属集团控股有限公司",
        };
        System.out.println("未开启地名识别的效果：" + Xmnlp.segment("蓝翔给宁夏固原市彭阳县红河镇黑牛沟村捐赠了挖掘机，"));

        Segment segment = Xmnlp.newSegment().enablePlaceRecognize(true).enableOrganizationRecognize(true);
        for (String sentence : testCase) {
            List<Term> termList = segment.seg(sentence);
            System.out.println(termList);
        }
    }
}
