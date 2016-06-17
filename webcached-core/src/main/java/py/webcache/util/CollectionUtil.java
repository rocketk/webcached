package py.webcache.util;


import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by pengyu on 2016/5/19.
 */
public class CollectionUtil {
    public static Set<Integer> arrayToSetAsInteger(String arrayStr) {
        if (arrayStr != null && arrayStr.length() > 0) {
            String[] statusArr = arrayStr.split(",");
            if (statusArr != null && statusArr.length > 0) {
                Set<Integer> set = new HashSet<>();
                for (String one : statusArr) {
                    set.add(Integer.valueOf(one));
                }
                return set;
            }
        }
        return null;
    }

    public static Set<String> arrayToSetAsString(String arrayStr) {
        if (arrayStr != null && arrayStr.length() > 0) {
            String[] statusArr = arrayStr.split(",");
            if (statusArr != null && statusArr.length > 0) {
                Set<String> set = new HashSet<>();
                for (String one : statusArr) {
                    set.add(one);
                }
                return set;
            }
        }
        return null;
    }

    /**
     * list转换成map，需保证元素对象中存在一个主键
     *
     * @param list
     * @param keyFieldName
     * @param keyFieldClass
     * @param <K>
     * @param <V>
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static <K, V> Map<K, V> listToMap(List<V> list, String keyFieldName, Class<K> keyFieldClass) throws NoSuchFieldException, IllegalAccessException {
        Map<K, V> map = new HashMap();
        if (list != null && list.size() > 0 && keyFieldName != null && keyFieldName.length() > 0 && keyFieldClass != null) {
            for (V v : list) {
                Field field = v.getClass().getDeclaredField(keyFieldName);
                field.setAccessible(true);
                K key = (K) field.get(v);
                if (key == null) {
                    throw new NullPointerException("the value of the keyField '" + keyFieldName + "' is null");
                }
                map.put(key, v);
            }
        } else {
            throw new IllegalArgumentException("the arguments should not be null or empty");
        }
        return map;
    }

    /**
     * 对一个set集合，按照V类型的某个字段来分组
     *
     * @param set
     * @param keyFieldName
     * @param keyfieldClass
     * @param <K>
     * @param <V>
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static <K, V> Map<K, Set<V>> groupSetToMap(Set<V> set, String keyFieldName, Class<K> keyfieldClass) throws NoSuchFieldException, IllegalAccessException {
        if (set == null) {
            return null;
        }
        Map<K, Set<V>> map = new HashMap();
        for (V v : set) {
            Field field = v.getClass().getDeclaredField(keyFieldName);
            field.setAccessible(true);
            K key = (K) field.get(v);
            Set<V> subSet = map.get(key);
            if (subSet == null) {
                subSet = new HashSet<>();
                map.put(key, subSet);
            }
            subSet.add(v);
        }
        return map;
    }

    public static <K, V> Map<K, V> groupSetToUniqueMap(Set<V> set, String keyFieldName, Class<K> keyfieldClass) throws NoSuchFieldException, IllegalAccessException {
        if (set == null) {
            return null;
        }
        Map<K, V> map = new HashMap();
        for (V v : set) {
            Field field = v.getClass().getDeclaredField(keyFieldName);
            field.setAccessible(true);
            K key = (K) field.get(v);
            map.put(key, v);
        }
        return map;
    }

    /**
     * 判断字符串数组是否为空
     * 只要有元素，并且至少有一个非空串元素，则返回true
     *
     * @param a
     * @return
     */
    public static boolean isStrArrEmpty(String[] a) {
        if (a != null && a.length > 0) {
            for (String s : a) {
                if (s != null && s.length() > 0) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 遍历map中的每一个value，这些value也是集合类对象（Iterable或数组），对这些集合的元素再进行遍历，并且使用给定的方法来处理这些元素
     * @param map
     * @param travelers
     * @param <K>
     * @param <V>
     */
    public static <K, V> void travers(Map<K, V> map, Travelers<K, V> travelers) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            V v = entry.getValue();
            if (v != null) {
                if (v instanceof Iterable) {
                    Iterable iterable = (Iterable) v;
                    for (Object o : iterable) {
                        travelers.dosomething(entry.getKey(), v, o);
                    }
                } else if (v.getClass().isArray()) {
                    Object[] array = (Object[]) v;
                    for (Object o : array) {
                        travelers.dosomething(entry.getKey(), v, o);
                    }
                } else {
                    throw new RuntimeException(String.format("%s is not a iterable object nor an array", v.getClass().getName()));
                }
            }
        }
    }

    public static interface Travelers<K, V> {
        public void dosomething(K k, V v, Object o);
    }

}
