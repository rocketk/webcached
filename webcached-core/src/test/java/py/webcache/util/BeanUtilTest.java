package py.webcache.util;

import org.junit.Assert;
import org.junit.Test;
import py.webcache.config.pojo.Condition;

/**
 * Created by pengyu on 2016/6/16.
 */
public class BeanUtilTest {

    @Test
    public void testEqualsWithEachField() throws Exception {
        Condition c1 = new Condition(Condition.Type.paramEqual, "name", "value");
        Condition c2 = new Condition(Condition.Type.paramEqual, "name2", "value");
        Condition c3 = new Condition(Condition.Type.paramEqual, "name", "value3");
        Condition c4 = new Condition(Condition.Type.nonParam, null, null);
        Condition c5 = new Condition(Condition.Type.nonParam, null, null);
        Condition c6 = new Condition(Condition.Type.paramEqual, null, null);
        Assert.assertFalse(BeanUtil.equalsWithEachField(c1, c2));
        Assert.assertFalse(BeanUtil.equalsWithEachField(c1, c3));
        Assert.assertFalse(BeanUtil.equalsWithEachField(c4, c6));
        Assert.assertTrue(BeanUtil.equalsWithEachField(c4, c5));
    }

    @Test
    public void testEquals() throws Exception {
        String str1 = "asdf";
        String str2 = new String("asdf");
        Assert.assertTrue(BeanUtil.equals(str1, str2));
        str2 = "aa";
        Assert.assertFalse(BeanUtil.equals(str1, str2));
        Assert.assertFalse(BeanUtil.equals(null, str2));
        Assert.assertFalse(BeanUtil.equals(str1, null));
    }
}