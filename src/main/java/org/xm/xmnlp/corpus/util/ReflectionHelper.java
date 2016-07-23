package org.xm.xmnlp.corpus.util;

import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 修改final static域的反射工具
 */
public class ReflectionHelper {
    private static final String MODIFIERS_FIELD = "modifiers";

    private static final ReflectionFactory reflection =
            ReflectionFactory.getReflectionFactory();

    public static void setStaticFinalField(
            Field field, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        // 获得 public 权限
        field.setAccessible(true);
        // 将modifiers域设为非final,这样就可以修改了
        Field modifiersField = Field.class.getDeclaredField(MODIFIERS_FIELD);
        modifiersField.setAccessible(true);
        int modifiers = modifiersField.getInt(field);
        // 去掉 final 标志位
        modifiers &= ~Modifier.FINAL;
        modifiersField.setInt(field, modifiers);
        FieldAccessor fa = reflection.newFieldAccessor(field, false);
        fa.set(null, value);
    }
}
