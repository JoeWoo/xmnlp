package org.xm.xmnlp.demo;

import org.xm.xmnlp.Xmnlp;

/**
 * Xmnlp第一个Demo，启动加载词典到缓存
 *
 * Created by xuming on 2016/7/20.
 */
public class DemoAtFirstSight {
    public static void main(String[] args) {
        System.out.println("首次编译运行时，自动构建词典缓存，请稍候……");
        Xmnlp.Config.enableDebug();         // 为了避免你等得无聊，开启调试模式说点什么:-)
        System.out.println(Xmnlp.segment("你好，欢迎使用xmnlp自然语言处理包！接下来请从其他Demo中体验nlp丰富的功能~"));
    }
}
