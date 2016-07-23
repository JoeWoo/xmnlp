package org.xm.xmnlp.demo;

import org.xm.xmnlp.Xmnlp;

/**
 * 第一个Demo，惊鸿一瞥
 */
public class DemoAtFirstSight {
    public static void main(String[] args) {
        System.out.println("首次编译运行时，HanLP会自动构建词典缓存，请稍候……");
        Xmnlp.Config.enableDebug();         // 为了避免你等得无聊，开启调试模式说点什么:-)
        System.out.println(Xmnlp.segment("你好，欢迎使用xmnlp自然语言处理包！接下来请从其他Demo中体验HanLP丰富的功能~"));
    }
}
