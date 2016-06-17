package py.webcache.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pengyu on 2016/6/14.
 */
public class StrExpressionUtil {

    /**
     * 解析字符串rawText中的表达式，使用map中的值替换这些表达式
     * @param rawText
     * @param map
     * @return
     */
    public static String parseExpression(String rawText, Map<String, String> map) {
        if (rawText != null && map != null) {
            StringBuilder sb = new StringBuilder();
            StringBuilder sbkey = new StringBuilder();
            boolean readExpression = false;
            for (int i = 0; i < rawText.length(); i++) {
                char c = rawText.charAt(i);
                // 当遇到美元符号，并且下一个字符是左花括号时，进入表达式
                if (c == '$' && i < rawText.length() - 1 && rawText.charAt(i + 1) == '{') {
                    if (readExpression) {
                        throw new IllegalArgumentException(String.format("expression cannot be nested, at %d, the original String: %s", i, rawText));
                    }
                    readExpression = true;
                    i++;
                    continue;
                } else { // 需要判断当前字符是表达式字符还是一般字符
                    if (readExpression) { // 表达式字符
                        if (c == '}') { // 判断是否来到结尾
                            String key = sbkey.toString().trim();
                            if (key == null || key.length() == 0) {
                                throw new IllegalArgumentException(String.format("the expression is empty, at %d, the original String: %s", i, rawText));
                            }
                            boolean blankIfUndefined = key.endsWith("!");
                            String value = map.get(key);
                            if (value == null) {
                                if (!blankIfUndefined) {
                                    throw new IllegalArgumentException(String.format("variable is undefined: %s, at %d, the original String: %s", key, i, rawText));
                                }
                                // else blank
                            } else {
                                sb.append(value);
                            }
                            sbkey.delete(0, sbkey.length());
                            readExpression = false;
                        } else { // 未到结尾
                            sbkey.append(c);
                            if (i >= rawText.length() - 1) { // 表达式尚未结尾，但字符串已经到结尾
                                throw new IllegalArgumentException(String.format("'}' is required, the original String: %s", rawText));
                            }
                        }
                    } else {
                        sb.append(c);
                    }
                }
            }
            return sb.toString();
        }
        return rawText;
    }

    /**
     * 解析字符串rawText中的表达式，使用map中的值替换这些表达式，注意，map中的value是数组，也就是说会替换成多个结果，这些结果将同样以数组的形式返回
     * @param rawText 包含表达式的字符串，可以有0~1个表达式，超过则会出现异常
     * @param map 要替换的值
     * @return
     */
    public static List<String> parseExpressionWithMultiValue(String rawText, Map<String, String[]> map) {
        // TODO untested
        if (rawText != null && map != null) {
            String patternString = "\\$\\{([a-zA-Z_]\\w*)\\}";

            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(rawText);

            // 判断有多少个匹配项，如果大于1，则报错
            if (matcher.find()) {
                if (matcher.find()) {
                    throw new IllegalArgumentException("number of expressions in argument rawText could not be greater than one");
                }
            }
            matcher.reset(); // 重置

            StringBuffer sb = new StringBuffer();
            List<String> stringList = new ArrayList<>();
            if (matcher.find()) {
                String[] replacements = map.get(matcher.group(1));
                for (String replacement : replacements) {
                    matcher.appendReplacement(sb, replacement);
                    matcher.appendTail(sb);
                    matcher.reset(); // 重置，否则再次调用appendReplacement时会报错
                    matcher.find(); // 重新find，否则再次调用appendReplacement时会报错
                    stringList.add(sb.toString());
                    sb.delete(0, sb.length());
                }
            }
            return stringList;
        }
        return new ArrayList<>();
    }

}
