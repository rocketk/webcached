package py.webcache.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Scanner;

/**
 * 用来关闭io流的工具
 * @author Peng Yu
 *
 */
public class IOUtil {
	private static final Logger logger = LoggerFactory.getLogger(IOUtil.class);
	public static final String CLASSPATH_URL_PREFIX = "classpath:";
	//之前测试得到的一个值
	public static final int DEFAULT_BUF_SIZE=11000;
	public static void closeIOStream(Object... objs) {
		for (int i = 0; i < objs.length; i++) {
			Object obj = objs[i];
			if (obj == null)
				continue;
			try {
				logger.debug("closeIOStream: "+ obj);
				//实现Closeable的类有InputStream,OutputStream,Reader(InputStreamReader 继承Reader)
				if (obj instanceof Closeable) {
					Closeable c= (Closeable)obj;
					c.close();
					c=null;
				}else if (obj instanceof Scanner){
					Scanner s= (Scanner)obj;
					s.close();
					s=null;
				}else{
					logger.error("closeIOStream error 发现不支持的类型，请增加对此类型的处理: "+ obj.getClass() );
				}
			} catch (IOException e) {
				logger.error("closeIOStream error");
			}finally{
				obj=null;
			}
		}
	}
	/**
	 * 支持classpath，示例: classpath:/jdbc.properties
	 * 
	 * 处理各种路径前缀，例如classpath，另见： org.springframework.util.ResourceUtils
	 * @author 李星
	 * 2015-1-2 下午12:10:27

	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream readFile(String path) throws FileNotFoundException{
		if (path.startsWith(CLASSPATH_URL_PREFIX)) {
			path = path.substring(CLASSPATH_URL_PREFIX.length());
			if(path.startsWith("/"))
				return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		}
		return new FileInputStream(path);
	}
	public static byte[] stream2bytes(InputStream in,boolean closeIn) throws IOException {
		return stream2bytes(in,DEFAULT_BUF_SIZE,closeIn);
	}
	public static byte[] stream2bytes(InputStream in,int bufSize,boolean closeIn) throws IOException {
		ByteArrayOutputStream baos = stream2baos(in,bufSize,closeIn);
		return baos.toByteArray();
	}
	public static ByteArrayOutputStream stream2baos(InputStream in,  int bufSize,boolean closeIn) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		stream2Stream(in, baos, bufSize,closeIn);
		return baos;
	}
	/**
	 * @author 李星
	 * 2014-11-7 下午4:28:41
	 * @param in  不会关闭in,调用方法后需要手动关闭
	 * @param out 不会关闭out,调用方法后需要手动关闭
	 * @param bufSize  InputStream每次读入的buf size
	 * @throws IOException
	 */
	public static  void stream2Stream(InputStream in , OutputStream out, int bufSize, boolean closeIn) throws IOException{
		byte[] bs = new byte[bufSize];
		int len=0;
		while ( (len=in.read(bs))!=-1) {
			out.write(bs, 0, len);
		}
		if(closeIn)
			closeIOStream(in);
	}
}
