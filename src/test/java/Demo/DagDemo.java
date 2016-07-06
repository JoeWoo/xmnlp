package Demo;

import org.xm.segWord.Segmenter;

/**
 * Created by xuming on 2016/7/6.
 */
public class DagDemo {
    public static void main(String[] args){
        Segmenter segmenter = new Segmenter();
        String[] sentences =
                new String[] {"他在黎明起来了，黎明认识\n" +
                        "东海县迅捷贸易有限责任公司"};
        for (String sentence : sentences) {
            System.out.println(segmenter.createDAG(sentence));

        }
    }
}
