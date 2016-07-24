package org.xm.xmnlp.dictionary;


import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.corpus.tag.Nature;
import static org.xm.xmnlp.util.Predefine.logger;

/**
 * 核心词典词性转移矩阵
 */
public class CoreDictionaryTransformMatrixDictionary {
    public static TransformMatrixDictionary<Nature> transformMatrixDictionary;

    static {
        transformMatrixDictionary = new TransformMatrixDictionary<Nature>(Nature.class);
        long start = System.currentTimeMillis();
        if (!transformMatrixDictionary.load(Xmnlp.Config.CoreDictionaryTransformMatrixDictionaryPath)) {
            System.err.println("加载核心词典词性转移矩阵" + Xmnlp.Config.CoreDictionaryTransformMatrixDictionaryPath + "失败");
            System.exit(-1);
        } else {
            logger.info("加载核心词典词性转移矩阵" + Xmnlp.Config.CoreDictionaryTransformMatrixDictionaryPath + "成功，耗时：" + (System.currentTimeMillis() - start) + " ms");
        }
    }
}
