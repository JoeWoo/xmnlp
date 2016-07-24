package org.xm.xmnlp.dictionary.pinyin;

import java.util.ArrayList;
import java.util.List;

/**
 * 拼音相关转换工具类
 */
public class PinyinUtil {
    /**
     * Convert tone numbers to tone marks using Unicode <br/><br/>
     * <p/>
     * <b>Algorithm for determining location of tone mark</b><br/>
     * <p/>
     * A simple algorithm for determining the vowel on which the tone mark
     * appears is as follows:<br/>
     * <p/>
     * <ol>
     * <li>First, look for an "a" or an "e". If either vowel appears, it takes
     * the tone mark. There are no possible pinyin syllables that contain both
     * an "a" and an "e".
     * <p/>
     * <li>If there is no "a" or "e", look for an "ou". If "ou" appears, then
     * the "o" takes the tone mark.
     * <p/>
     * <li>If none of the above cases hold, then the last vowel in the syllable
     * takes the tone mark.
     * <p/>
     * </ol>
     *
     * @param pinyinStr the ascii represention with tone numbers
     * @return the unicode represention with tone marks
     */
    public static String convertToneNumber2ToneMark(final String pinyinStr) {
        String lowerCasePinyinStr = pinyinStr.toLowerCase();

        if (lowerCasePinyinStr.matches("[a-z]*[1-5]?")) {
            final char defautlCharValue = '$';
            final int defautlIndexValue = -1;

            char unmarkedVowel = defautlCharValue;
            int indexOfUnmarkedVowel = defautlIndexValue;

            final char charA = 'a';
            final char charE = 'e';
            final String ouStr = "ou";
            final String allUnmarkedVowelStr = "aeiouv";
            final String allMarkedVowelStr = "āáǎàaēéěèeīíǐìiōóǒòoūúǔùuǖǘǚǜü";

            if (lowerCasePinyinStr.matches("[a-z]*[1-5]")) {

                int tuneNumber =
                        Character.getNumericValue(lowerCasePinyinStr.charAt(lowerCasePinyinStr.length() - 1));

                int indexOfA = lowerCasePinyinStr.indexOf(charA);
                int indexOfE = lowerCasePinyinStr.indexOf(charE);
                int ouIndex = lowerCasePinyinStr.indexOf(ouStr);

                if (-1 != indexOfA) {
                    indexOfUnmarkedVowel = indexOfA;
                    unmarkedVowel = charA;
                } else if (-1 != indexOfE) {
                    indexOfUnmarkedVowel = indexOfE;
                    unmarkedVowel = charE;
                } else if (-1 != ouIndex) {
                    indexOfUnmarkedVowel = ouIndex;
                    unmarkedVowel = ouStr.charAt(0);
                } else {
                    for (int i = lowerCasePinyinStr.length() - 1; i >= 0; i--) {
                        if (String.valueOf(lowerCasePinyinStr.charAt(i)).matches(
                                "[" + allUnmarkedVowelStr + "]")) {
                            indexOfUnmarkedVowel = i;
                            unmarkedVowel = lowerCasePinyinStr.charAt(i);
                            break;
                        }
                    }
                }

                if ((defautlCharValue != unmarkedVowel) && (defautlIndexValue != indexOfUnmarkedVowel)) {
                    int rowIndex = allUnmarkedVowelStr.indexOf(unmarkedVowel);
                    int columnIndex = tuneNumber - 1;

                    int vowelLocation = rowIndex * 5 + columnIndex;

                    char markedVowel = allMarkedVowelStr.charAt(vowelLocation);

                    StringBuffer resultBuffer = new StringBuffer();

                    resultBuffer.append(lowerCasePinyinStr.substring(0, indexOfUnmarkedVowel).replaceAll("v",
                            "ü"));
                    resultBuffer.append(markedVowel);
                    resultBuffer.append(lowerCasePinyinStr.substring(indexOfUnmarkedVowel + 1,
                            lowerCasePinyinStr.length() - 1).replaceAll("v", "ü"));

                    return resultBuffer.toString();

                } else
                // error happens in the procedure of locating vowel
                {
                    return lowerCasePinyinStr;
                }
            } else
            // input string has no any tune number
            {
                // only replace v with ü (umlat) character
                return lowerCasePinyinStr.replaceAll("v", "ü");
            }
        } else
        // bad format
        {
            return lowerCasePinyinStr;
        }
    }

    /**
     * 将列表转为数组
     *
     * @param pinyinList
     * @return
     */
    public static Pinyin[] convertList2Array(List<Pinyin> pinyinList) {
        return pinyinList.toArray(new Pinyin[0]);
    }

    public static Pinyin removeTone(Pinyin p) {
        return Pinyin.none5;
    }

    /**
     * 转换List<Pinyin> pinyinList到List<String>，其中的String为带声调符号形式
     *
     * @param pinyinList
     * @return
     */
    public static List<String> convertPinyinList2TonePinyinList(List<Pinyin> pinyinList) {
        List<String> tonePinyinList = new ArrayList<String>(pinyinList.size());
        for (Pinyin pinyin : pinyinList) {
            tonePinyinList.add(pinyin.getPinyinWithToneMark());
        }

        return tonePinyinList;
    }
}
