package org.xm.xmnlp.demo;


import org.xm.xmnlp.tokenizer.StandardTokenizer;

/**
 * 演示标准分词效果，即：最短路分词器
 */
public class DemoStandardTokenizer {
    public static void main(String[] args) {
        String text = "举办纪念活动铭记二战历史，不忘战争带给人类的深重灾难，是为了防止悲剧重演，确保和平永驻；" +
                "铭记二战历史，更是为了提醒国际社会，需要共同捍卫二战胜利成果和国际公平正义，" +
                "必须警惕和抵制在历史认知和维护战后国际秩序问题上的倒行逆施。";
        System.out.println(StandardTokenizer.segment(text));
        // 测试分词速度，让大家对性能有一个直观的认识
        long start = System.currentTimeMillis();
        int pressure = 100000;
        for (int i = 0; i < pressure; ++i) {
            StandardTokenizer.segment(text);
        }
        double costTime = (System.currentTimeMillis() - start) / (double) 1000;
        System.out.printf("StandardTokenizer分词速度：%.2f字每秒\n", text.length() * pressure / costTime);
    }
}
