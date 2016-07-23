package org.xm.xmnlp;

import org.xm.xmnlp.seg.Segment;
import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.seg.viterbi.ViterbiSegment;
import org.xm.xmnlp.tokenizer.StandardTokenizer;
import org.xm.xmnlp.util.Predefine;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import static org.xm.xmnlp.util.Predefine.logger;

/**
 * Xmnlp : xuming nlp 自然语言处理工具包
 * 常用接口工具类
 *
 * Created by xuming on 2016/7/22.
 */
public class Xmnlp {
    public static final class Config {
        public static boolean DEBUG = false;
        public static String CoreDictionaryPath = "data/dictionary/CoreNatureDictionary.txt";
        public static String CoreDictionaryTransformMatrixDictionaryPath = "data/dictionary/CoreNatureDictionary.tr.txt";
        /**
         * 用户自定义词典路径
         */
        public static String CustomDictionaryPath[] = new String[]{"data/dictionary/custom/CustomDictionary.txt"};
        /**
         * 2元语法词典路径
         */
        public static String BiGramDictionaryPath = "data/dictionary/CoreNatureDictionary.ngram.txt";

        /**
         * 停用词词典路径
         */
        public static String CoreStopWordDictionaryPath = "data/dictionary/stopwords.txt";
        /**
         * 同义词词典路径
         */
        public static String CoreSynonymDictionaryDictionaryPath = "data/dictionary/synonym/CoreSynonym.txt";
        /**
         * 人名词典路径
         */
        public static String PersonDictionaryPath = "data/dictionary/person/nr.txt";
        /**
         * 人名词典转移矩阵路径
         */
        public static String PersonDictionaryTrPath = "data/dictionary/person/nr.tr.txt";
        /**
         * 地名词典路径
         */
        public static String PlaceDictionaryPath = "data/dictionary/place/ns.txt";
        /**
         * 地名词典转移矩阵路径
         */
        public static String PlaceDictionaryTrPath = "data/dictionary/place/ns.tr.txt";
        /**
         * 机构词典路径
         */
        public static String OrganizationDictionaryPath = "data/dictionary/organization/nt.txt";
        /**
         * 机构词典转移矩阵路径
         */
        public static String OrganizationDictionaryTrPath = "data/dictionary/organization/nt.tr.txt";
        /**
         * 繁简词典路径
         */
        public static String TraditionalChineseDictionaryPath = "data/dictionary/tc/TraditionalChinese.txt";
        /**
         * 声母韵母语调词典
         */
        public static String SYTDictionaryPath = "data/dictionary/pinyin/SYTDictionary.txt";

        /**
         * 拼音词典路径
         */
        public static String PinyinDictionaryPath = "data/dictionary/pinyin/pinyin.txt";

        /**
         * 音译人名词典
         */
        public static String TranslatedPersonDictionaryPath = "data/dictionary/person/nrf.txt";

        /**
         * 日本人名词典路径
         */
        public static String JapanesePersonDictionaryPath = "data/dictionary/person/nrj.txt";

        /**
         * 字符类型对应表
         */
        public static String CharTypePath = "data/dictionary/other/CharType.dat.yes";

        /**
         * 字符正规化表（全角转半角，繁体转简体）
         */
        public static String CharTablePath = "data/dictionary/other/CharTable.bin.yes";

        /**
         * 词-词性-依存关系模型
         */
        public static String WordNatureModelPath = "data/model/dependency/WordNature.txt";

        /**
         * 最大熵-依存关系模型
         */
        public static String MaxEntModelPath = "data/model/dependency/MaxEntModel.txt";
        /**
         * 神经网络依存模型路径
         */
        public static String NNParserModelPath = "data/model/dependency/NNParserModel.txt";
        /**
         * CRF分词模型
         */
        public static String CRFSegmentModelPath = "data/model/segment/CRFSegmentModel.txt";
        /**
         * HMM分词模型
         */
        public static String HMMSegmentModelPath = "data/model/segment/HMMSegmentModel.bin";
        /**
         * CRF依存模型
         */
        public static String CRFDependencyModelPath = "data/model/dependency/CRFDependencyModelMini.txt";
        /**
         * 分词结果是否展示词性
         */
        public static boolean ShowTermNature = true;
        /**
         * 是否执行字符正规化（繁体->简体，全角->半角，大写->小写），切换配置后必须删CustomDictionary.txt.bin缓存
         */
        public static boolean Normalization = false;

        static {
            Properties p = new Properties();
            try {
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                if (loader == null) {
                    loader = Xmnlp.Config.class.getClassLoader();
                }
                p.load(new InputStreamReader(Predefine.PROPERTIES_PATH == null ?
                        loader.getResourceAsStream("xmnlp.properties")
                        : new FileInputStream(Predefine.PROPERTIES_PATH), "UTF-8"));
                String root = p.getProperty("root", "").replaceAll("\\\\", "/");
                if (!root.endsWith("/")) root += "/";
                CoreDictionaryPath = root + p.getProperty("CoreDictionaryPath", CoreDictionaryPath);
                CoreDictionaryTransformMatrixDictionaryPath = root + p.getProperty("CoreDictionaryTransformMatrixDictionaryPath", CoreDictionaryTransformMatrixDictionaryPath);
                BiGramDictionaryPath = root + p.getProperty("BiGramDictionaryPath", BiGramDictionaryPath);
                CoreStopWordDictionaryPath = root + p.getProperty("CoreStopWordDictionaryPath", CoreStopWordDictionaryPath);
                CoreSynonymDictionaryDictionaryPath = root + p.getProperty("CoreSynonymDictionaryDictionaryPath", CoreSynonymDictionaryDictionaryPath);
                PersonDictionaryPath = root + p.getProperty("PersonDictionaryPath", PersonDictionaryPath);
                PersonDictionaryTrPath = root + p.getProperty("PersonDictionaryTrPath", PersonDictionaryTrPath);
                String[] pathArray = p.getProperty("CustomDictionaryPath", "dictionary/custom/CustomDictionary.txt").split(";");
                String prePath = root;
                for (int i = 0; i < pathArray.length; ++i) {
                    if (pathArray[i].startsWith(" ")) {
                        pathArray[i] = prePath + pathArray[i].trim();
                    } else {
                        pathArray[i] = root + pathArray[i];
                        int lastSplash = pathArray[i].lastIndexOf('/');
                        if (lastSplash != -1) {
                            prePath = pathArray[i].substring(0, lastSplash + 1);
                        }
                    }
                }
                CustomDictionaryPath = pathArray;
                TraditionalChineseDictionaryPath = root + p.getProperty("TraditionalChineseDictionaryPath", TraditionalChineseDictionaryPath);
                SYTDictionaryPath = root + p.getProperty("SYTDictionaryPath", SYTDictionaryPath);
                PinyinDictionaryPath = root + p.getProperty("PinyinDictionaryPath", PinyinDictionaryPath);
                TranslatedPersonDictionaryPath = root + p.getProperty("TranslatedPersonDictionaryPath", TranslatedPersonDictionaryPath);
                JapanesePersonDictionaryPath = root + p.getProperty("JapanesePersonDictionaryPath", JapanesePersonDictionaryPath);
                PlaceDictionaryPath = root + p.getProperty("PlaceDictionaryPath", PlaceDictionaryPath);
                PlaceDictionaryTrPath = root + p.getProperty("PlaceDictionaryTrPath", PlaceDictionaryTrPath);
                OrganizationDictionaryPath = root + p.getProperty("OrganizationDictionaryPath", OrganizationDictionaryPath);
                OrganizationDictionaryTrPath = root + p.getProperty("OrganizationDictionaryTrPath", OrganizationDictionaryTrPath);
                CharTypePath = root + p.getProperty("CharTypePath", CharTypePath);
                CharTablePath = root + p.getProperty("CharTablePath", CharTablePath);
                WordNatureModelPath = root + p.getProperty("WordNatureModelPath", WordNatureModelPath);
                MaxEntModelPath = root + p.getProperty("MaxEntModelPath", MaxEntModelPath);
                NNParserModelPath = root + p.getProperty("NNParserModelPath", NNParserModelPath);
                CRFSegmentModelPath = root + p.getProperty("CRFSegmentModelPath", CRFSegmentModelPath);
                CRFDependencyModelPath = root + p.getProperty("CRFDependencyModelPath", CRFDependencyModelPath);
                HMMSegmentModelPath = root + p.getProperty("HMMSegmentModelPath", HMMSegmentModelPath);
                ShowTermNature = "true".equals(p.getProperty("ShowTermNature", "true"));
                Normalization = "true".equals(p.getProperty("Normalization", "false"));
            } catch (Exception e) {
                StringBuffer sbInfo = new StringBuffer("----------tips--------\n " +
                        "make sure the xmnlp.properties is exist.");
                String classPath = (String) System.getProperties().get("java.class.path");
                if (classPath != null) {
                    for (String path : classPath.split(File.pathSeparator)) {
                        if (new File(path).isDirectory()) {
                            sbInfo.append(path).append('\n');
                        }
                    }
                }
//                sbInfo.append("Web项目则请放到下列目录：\n" +
//                        "Webapp/WEB-INF/lib\n" +
//                        "Webapp/WEB-INF/classes\n" +
//                        "Appserver/lib\n" +
//                        "JRE/lib\n");
//                sbInfo.append("并且编辑root=PARENT/path/to/your/data\n");
                sbInfo.append("现在Xmnlp将尝试从").append(System.getProperties().get("user.dir")).append("读取data……");
//                logger.severe("没有找到Xmnlp.properties，可能会导致找不到data\n" + sbInfo);

            }
        }

        /**
         * 开启调试模式(会降低性能)
         */
        public static void enableDebug() {
            enableDebug(true);
        }

        /**
         * 开启调试模式(会降低性能)
         *
         * @param enable
         */
        public static void enableDebug(boolean enable) {
            DEBUG = enable;
            if (DEBUG) {
                logger.setLevel(Level.ALL);
            } else {
                logger.setLevel(Level.OFF);
            }
        }

    }

    private Xmnlp() {
    }


    /**
     * 分词
     *
     * @param text 文本
     * @return 切分后的单词
     */
    public static List<Term> segment(String text) {
        return StandardTokenizer.segment(text.toCharArray());
    }

    /**
     * 创建一个分词器<br>
     * 这是一个工厂方法<br>
     * 与直接new一个分词器相比，使用本方法的好处是，以后升级了，总能用上最合适的分词器
     *
     * @return 一个分词器
     */
    public static Segment newSegment() {
        return new ViterbiSegment();   // Viterbi分词器是目前效率和效果的最佳平衡
    }

}
