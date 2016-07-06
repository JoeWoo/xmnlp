package Demo;

import org.xm.segWord.SegMode;
import org.xm.segWord.Segmenter;

/**
 * Created by xuming on 2016/5/26.
 */
public class BaseDemo {

    public static void main(String[] args) {
        Segmenter segmenter = new Segmenter();
        String[] sentences =
                new String[] {"他在黎明起来了，黎明认识这个李明不輸入簡體字,點下面繁體字按鈕進行在線轉換、、你到底是何居心？这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。", "我不喜欢日本和服。", "雷猴回归人间。",
                        "东海县迅捷贸易有限责任公司"};
        for (String sentence : sentences) {
            System.out.println(segmenter.process(sentence, SegMode.SEARCH).toString());
        }

//        for (String sentence : sentences) {
//            System.out.println(segmenter.process(sentence, SegMode.INDEX).toString());
//        }
    }



}
