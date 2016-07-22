package org.xm.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 预定义的静态全局变量
 * Created by xuming on 2016/7/22.
 */
public class Predefine {
    public static String PROPERTIES_PATH;


    /**
     * 日志组件
     */
    public static Logger logger = Logger.getLogger("Xmnlp");

    static {
        logger.setLevel(Level.WARNING);
    }
}
