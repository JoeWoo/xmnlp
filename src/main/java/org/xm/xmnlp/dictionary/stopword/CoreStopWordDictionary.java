package org.xm.xmnlp.dictionary.stopword;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.corpus.io.ByteArray;
import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.util.Predefine;
import org.xm.xmnlp.util.TextUtil;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.ListIterator;

/**
 * 核心停用词词典
 * Created by xuming on 2016/7/26.
 */
public class CoreStopWordDictionary {
    static StopWordDictionary dictionary;

    static {
        ByteArray byteArray = ByteArray.createByteArray(Xmnlp.Config.CoreStopWordDictionaryPath + Predefine.BIN_EXT);
        if (byteArray == null) {
            try {
                dictionary = new StopWordDictionary(new File(Xmnlp.Config.CoreStopWordDictionaryPath));
                DataOutputStream out = new DataOutputStream(new FileOutputStream(Xmnlp.Config.CoreStopWordDictionaryPath + Predefine.BIN_EXT));
                dictionary.save(out);
                out.close();
            } catch (Exception e) {
                System.err.println("载入停用词词典：" + Xmnlp.Config.CoreStopWordDictionaryPath + "失败" + TextUtil.exceptionToString(e));
            }
        } else {
            dictionary = new StopWordDictionary();
            dictionary.load(byteArray);
        }
    }

    public static boolean contains(String key) {
        return dictionary.contains(key);
    }

    public static Filter FILTER = new Filter() {
        @Override
        public boolean shouldInclude(Term term) {
            //去掉停用词
            String nature = term.nature != null ? term.nature.toString() : "空";
            char firstChar = nature.charAt(0);
            switch (firstChar) {
                case 'm':
                case 'b':
                case 'c':
                case 'e':
                case 'o':
                case 'p':
                case 'q':
                case 'u':
                case 'y':
                case 'z':
                case 'r':
                case 'w': {
                    return false;
                }
                default: {
                    if (term.word.length() > 1 && !CoreStopWordDictionary.contains(term.word)) {
                        return true;
                    }
                }
                break;
            }
            return false;
        }
    };

    /**
     * 是否应当将这个term纳入计算
     *
     * @param term
     * @return 是否应当
     */
    public static boolean shouldInclude(Term term) {
        return FILTER.shouldInclude(term);
    }

    /**
     * 是否应当去掉这个词
     *
     * @param term 词
     * @return 是否应当去掉
     */
    public static boolean shouldRemove(Term term) {
        return !shouldInclude(term);
    }

    /**
     * 加入停用词到停用词词典中
     *
     * @param stopword 停用词
     * @return 词典是否发生了改变
     */
    public static boolean add(String stopword) {
        return dictionary.add(stopword);
    }

    /**
     * 从停用词词典删除词
     *
     * @param stopword 停用词
     * @return 删除是否成功
     */
    public static boolean remove(String stopword) {
        return dictionary.remove(stopword);
    }

    /**
     * 应用过滤，去除不符合要求的结果
     *
     * @param termList 所有数据列表
     */
    public static void apply(List<Term> termList) {
        ListIterator<Term> listIterator = termList.listIterator();
        while (listIterator.hasNext()) {
            if (shouldRemove(listIterator.next())) listIterator.remove();
        }
    }


}
