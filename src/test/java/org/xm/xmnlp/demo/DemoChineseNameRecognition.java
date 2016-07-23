/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/12/7 19:25</create-date>
 *
 * <copyright file="DemoChineseNameRecoginiton.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014+ 上海林原信息科技有限公司. All Right Reserved+ http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package org.xm.xmnlp.demo;


import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.seg.Segment;
import org.xm.xmnlp.seg.domain.Term;

import java.util.List;

/**
 * 中国人名识别
 *
 */
public class DemoChineseNameRecognition {
    public static void main(String[] args) {
        Xmnlp.Config.Normalization = true;
        String[] testCase = new String[]{
                "签约仪式前，秦光荣、李纪恒、仇和等一同会见了参加签约的企业家。",
                "区长庄木弟新年致辞",
                "朱立伦：两岸都希望共创双赢 习朱历史会晤在ABC即",
                "陕西首富吴一坚被带走 与令计划妻子有交集",
                "据美国之音电台网站4月28日报道，8岁的凯瑟琳·克罗尔（凤甫娟）和很多华裔美国小朋友一样，小小年纪就开始学小提琴了。她的妈妈是位虎妈么？",
                "凯瑟琳和露西（庐瑞媛），跟她们的哥哥们有一些不同。",
                "王国强、高峰、汪洋、张朝阳光着头、韩寒、小四",
                "张浩和胡健康复员回家了",
                "王总和小丽结婚了",
                "编剧邵钧林和稽道青说",
                "这里有关天培的有关事迹",
                "龚学平等领导说,邓颖超生前杜绝超生，中国人名共和国完善了，小泉纯一郎要川岛芳子在不？龟山千广和王大人关系好？",
        };
        Segment segment = Xmnlp.newSegment().enableNameRecognize(true).enableCustomDictionary(false).enableJapaneseNameRecognize(false);
        // 新增的开启自定义词库功能可用，简单的日本名称通过名称词库也可以识别，但复杂的日本名称（eg：龟山千广）就需要开启日本名识别才能识别出来。
        for (String sentence : testCase) {
            List<Term> termList = segment.seg(sentence);
            System.out.println(termList);
        }
    }
}
