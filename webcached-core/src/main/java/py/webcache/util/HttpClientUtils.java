/**
 *
 */
package py.webcache.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;


/**
 *
 */
public class HttpClientUtils {
	public Logger log = LoggerFactory.getLogger(HttpClientUtils.class);
	private final String LINE_SEPARATOR = System.getProperty("line.separator");
	private Integer timeout = 2000;
	private String proxyHost = null; // 代理ip，null为不需要代理
	private Integer proxyPort = null; // 代理端口，null为不需要代理
	private boolean needProxy = false;
	private String userAgent = null;

	public HttpClientUtils() {

	}

	/**
	 * @param timeout   设置超时时间，连接超时和请求超时都用这一个
	 * @param proxyHost 代理ip，null为不需要代理
	 * @param proxyPort 代理端口，null为不需要代理
	 */
//	public HttpClientUtils(Integer timeout, String proxyHost, Integer proxyPort, String userAgent) {
//		this.timeout = timeout;
//		this.proxyHost = proxyHost;
//		this.proxyPort = proxyPort;
//		this.userAgent = userAgent;
//		needProxy = proxyHost != null && !proxyHost.isEmpty() && proxyPort != null;
//	}

	/**
	 * 读取响应string
	 *
	 * @param hr
	 * @param charset
	 * @param url           just for debug
	 * @param lineSeparator 以json为例，'\'是一个特殊字符，不过一般http响应的json不会包含换行符
	 * @return
	 */
	public String getResponseStr(HttpResponse hr, String charset, String url, String lineSeparator) {
		try {
			//默认使用当前系统的分隔符
			if (lineSeparator == null)
				lineSeparator = LINE_SEPARATOR;

			HttpEntity he = hr.getEntity();
			if (he != null) {
				StringWriter sw = new StringWriter();
				InputStream in = he.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in, charset));
				String line = null;
				while ((line = reader.readLine()) != null) {
					sw.write(line + lineSeparator);
				}
				reader.close();
				String respStr = sw.toString();
				sw.close();
				return respStr;
			}
		} catch (IOException e) {
			log.error("get http response io exception, url: " + url);
		}
		return "";

	}

	/**
	 * @param postUrl
	 * @param nameValuePairs
	 * @return
	 * @date 2014年12月2日 下午2:39:10
	 * @update
	 */
	public String httpPost(String postUrl, List<NameValuePair> nameValuePairs, Map<String, String> headers) {
		HttpClient client = new DefaultHttpClient();
		if (needProxy) {// 代理的设置
			HttpHost proxy = new HttpHost(proxyHost, proxyPort);
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
		client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, timeout); // 超时设置
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);//连接超时
		if (userAgent != null) {
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, userAgent);
		}
		HttpPost httppost = new HttpPost(postUrl);
		if (headers != null && headers.size() > 0) {
			httppost.setHeaders(assemblyHeader(headers));
		}
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
			HttpResponse resp = client.execute(httppost);
			String str = this.getResponseStr(resp, "utf-8", postUrl, "");
			//JSONObject jsonObj = JSONObject.fromObject(str);
			return str;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String httpGet(String getUrl){
		return httpGet(getUrl, null);
	}
	/**
	 * @param getUrl
	 * @return
	 * @Description: TODO
	 * @author liuhaibo
	 * @date 2014年12月2日 下午2:39:36
	 * @update
	 */
	public String httpGet(String getUrl, Map<String, String> headers) {
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			if (needProxy) {
				HttpHost proxy = new HttpHost(proxyHost, proxyPort);
				httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}
			httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, timeout); // 超时设置
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);//连接超时
			if (userAgent != null) {
				httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, userAgent);
			}
			HttpGet httpget = new HttpGet(getUrl);
			if (headers != null && headers.size() > 0) {
				httpget.setHeaders(assemblyHeader(headers));
			}
			HttpResponse httpResponse = httpclient.execute(httpget);
			String str = this.getResponseStr(httpResponse, "utf-8", getUrl, "");
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 下载链接所指向的资源（通常是图片），返回字节数组
	 *
	 * @param url
	 * @return
	 * @author pengyu
	 */
	public byte[] download(String url) {
		if (url == null || url.trim().length() == 0) {
			return null;
		}
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			if (needProxy) {
				HttpHost proxy = new HttpHost(proxyHost, proxyPort);
				httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}
			httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, timeout); // 超时设置
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);//连接超时
			HttpGet httpget = new HttpGet(url);
			HttpResponse httpResponse = httpclient.execute(httpget);
			byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity());
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getImgInfo(String imgUrl) {
		if (imgUrl == null || imgUrl.isEmpty()) {
			return null;
		}
		String jsonStr = httpGet(imgUrl + "@info");
		if (jsonStr == null) {
			return null;
		}
		JSONObject json = JSON.parseObject(jsonStr);
		JSONObject img = json.getJSONObject("img");
		Integer width = img.getInteger("width");
		Integer height = img.getInteger("height");
		return width + "," + height;
	}

	public void main(String[] args) {
		String imgInfo = getImgInfo("http://tpic.home.news.cn/xhCloudNewsPic/xhpic001/M09/48/33/wKhTglQgw6oEAAAAAAAAAAAAAAA583.jpg@");
		System.out.println(imgInfo);
	}

	private Header[] assemblyHeader(Map<String,String> headers) {
		if (headers == null || headers.size() == 0) {
			return null;
		}
		Header[] allHeader = new BasicHeader[headers.size()];
		int i = 0;
		for (String str : headers.keySet()) {
			allHeader[i] = new BasicHeader(str, headers.get(str));
			i++;
		}
		return allHeader;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}

	public void setNeedProxy(boolean needProxy) {
		this.needProxy = needProxy;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
}
