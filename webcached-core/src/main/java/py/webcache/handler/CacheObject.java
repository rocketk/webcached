package py.webcache.handler;

import java.io.Serializable;

/**
 * Created by pengyu on 2016/5/12.
 */
public class CacheObject implements Serializable {
    public long serialVersionUID = 1L;
    private String key;
    /* request info begin */
//    private String uri;
//    private String method;
//    private Map<String, String[]> paramMap;
    /* request info end */
    /* response info begin */
    private String content;
    private String contentType;
    private String characterEncoding;
    /* response info end */
    /* cache info begin */
    private long putTime; // 放入时间，毫秒值
    private int expiredTime; // 有效期，秒
    private int cost; // 已经消耗了多少时间，秒
    private int left; // 还剩多少时间，秒
    /* cache info end */

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
//
//    public String getUri() {
//        return uri;
//    }
//
//    public void setUri(String uri) {
//        this.uri = uri;
//    }
//
//    public String getMethod() {
//        return method;
//    }
//
//    public void setMethod(String method) {
//        this.method = method;
//    }
//
//    public Map<String, String[]> getParamMap() {
//        return paramMap;
//    }
//
//    public void setParamMap(Map<String, String[]> paramMap) {
//        this.paramMap = paramMap;
//    }

    public long getPutTime() {
        return putTime;
    }

    public void setPutTime(long putTime) {
        this.putTime = putTime;
    }

    public int getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(int expiredTime) {
        this.expiredTime = expiredTime;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }
}
