package org.xm.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/*
 * 停用词过滤,修正词性到用户词性.
 */
public class FilterModifWord {

	private static Set<String> FILTER = new HashSet<String>();

	private static String TAG = "#";

	private static boolean isTag = false;
	// 停用词正则表达式
	private static Pattern stopwordPattern;

	public static void insertStopWords(List<String> filterWords) {
		FILTER.addAll(filterWords);
	}

	public static void insertStopWord(String... filterWord) {
		for (String word : filterWord) {
			FILTER.add(word);
		}
	}

	public static void insertStopNatures(String... filterNatures) {
		isTag = true;
		for (String natureStr : filterNatures) {
			FILTER.add(TAG + natureStr);
		}

	}

	public static void insertStopRegex(String regex) {
		stopwordPattern = Pattern.compile(regex);
	}

	public static void removeStopRegex() {
		stopwordPattern = null;
	}


}
