package py.webcache.util;

import java.lang.reflect.Field;

/**
 * Created by pengyu on 2016/6/16.
 */
public class BeanUtil {
    /**
     * 根据两个对象每个字段是否相等，来判断对象是否相等
     * @param a
     * @param b
     * @param <T>
     * @return
     */
    public static <T> boolean equalsWithEachField(T a, T b) {
        if (a == b) {
            return true;
        }
        if (a != null) {
            Class clazz = a.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    if (!equals(field.get(a), field.get(b))) {
                        return false;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else {
            return false;
        }
        return true;
    }

    public static <T> boolean equals(T a, T b) {
        if (a == b) {
            return true;
        }
        if (a != null && a.equals(b)) {
            return true;
        }
        return false;
    }
}
