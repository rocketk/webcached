package py.webcache.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by pengyu on 2016/6/16.
 */
public class StrExpressionUtilTest {

    @Test
    public void testParseExpression() throws Exception {
        List<String> list = new ArrayList<String>();
        list.add("aaa${key}bbb");
        list.add("${key}bbb");
        list.add("aaa${key}");
        list.add("{key}");
        list.add("aaa${key123!}${key1}bbb");
        list.add("aaa${key}-$${key1}bbb");
        list.add("aaa${${key1}}bbb");
        list.add("aaa${key1bbb");
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", "AAA");
        map.put("key1", "BBB");
        map.put("key2", "CCC");
        map.put("key3", "DDD");
        for (String s : list) {
            try {
                System.out.println(String.format("%s \t-->\t%s", s, StrExpressionUtil.parseExpression(s, map)));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("true".equals(null));
    }

    @Test
    public void testParseExpressionWithMultiValue() throws Exception {
        Map<String, String[]> map = new HashMap();
        map.put("key", new String[]{"AAA"});
        map.put("key1", new String[]{"AAA", "BBB"});
        map.put("key2", new String[]{"AAA", "BBB", "CCC"});
        Assert.assertArrayEquals(new String[]{"AAA"}, StrExpressionUtil.parseExpressionWithMultiValue("${key}", map));
        Assert.assertArrayEquals(new String[]{"AAA", "BBB"}, StrExpressionUtil.parseExpressionWithMultiValue("${key1}", map));
        Assert.assertArrayEquals(new String[]{"AAA", "BBB", "CCC"}, StrExpressionUtil.parseExpressionWithMultiValue("${key2}", map));
    }


}