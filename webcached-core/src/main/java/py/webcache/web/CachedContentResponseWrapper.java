package py.webcache.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.webcache.util.IOUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

public class CachedContentResponseWrapper extends HttpServletResponseWrapper implements CachedContentHttpServletResponse {
    public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";

    /**
     * @param response
     * @param appendToOriResponse 只输出到response buf，还是同时输出到response buf和原来的response
     */
    public CachedContentResponseWrapper(HttpServletResponse response, boolean appendToOriResponse) {
        super(response);
        this.appendToOriResponse = appendToOriResponse;
    }

    private ResponseServletOutputStream stream;
    private PrintWriter writer;
    private int httpStatus = 200;
    //只输出到response buf，还是同时输出到response buf和原来的response
    private boolean appendToOriResponse;

    private HttpServletResponse getOriResponse() {
        return (HttpServletResponse) (getResponse());
    }

    //留空，因为此wrapper会修改内容，导致content-length值不定，设置一个固定值就会导致浏览器解析错误
    @Override
    public void setContentLength(int len) {
    }


    public ResponseServletOutputStream createOutputStream() throws IOException {
        return (new ResponseServletOutputStream(getOriResponse(), appendToOriResponse));
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException(
                    "getWriter() has already been called for this response");
        }
        if (stream == null) {
            stream = createOutputStream();
        }
        return stream;
    }

    public String getCharacterEncoding() {
        String characterEncoding = super.getCharacterEncoding();
        if (characterEncoding == null)
            characterEncoding = DEFAULT_CHARACTER_ENCODING;
        return characterEncoding;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer != null) {
            return writer;
        }
        if (stream != null) {
            throw new IllegalStateException(
                    "getOutputStream() has already been called for this response");
        }
        stream = createOutputStream();
        writer = new ResponsePrintWriter(stream, getCharacterEncoding());
        return writer;
    }

    public ByteArrayOutputStream getBuffer() {
        return stream.getBuffer();
    }

    @Override
    public void resetBuffer() {
        getBuffer().reset();
        super.resetBuffer();
    }

    @Override
    public void reset() {
        super.reset();
        httpStatus = 200;
        resetBuffer();
    }

    @Override
    public void sendError(int sc) throws IOException {
        httpStatus = sc;
        super.sendError(sc);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        httpStatus = sc;
        super.sendError(sc, msg);
    }

    @Override
    public void setStatus(int sc) {
        httpStatus = sc;
        super.setStatus(sc);
    }

    @Override
    public int getStatus() {
        return httpStatus;
    }


    public byte[] toByteArray() {
        return this.getBuffer().toByteArray();
    }

    @Override
    public String getContentAsString() {
        try {
            return new String(getBuffer().toByteArray(), getCharacterEncoding());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return super.toString();
    }

    private static class ResponseServletOutputStream extends ServletOutputStream {
        private static final Logger log = LoggerFactory.getLogger(ResponseServletOutputStream.class);
        private HttpServletResponse originalResponse;
        //buf在这里定义，主要是为了方便关闭
        private ByteArrayOutputStream respWrapperBuf = new ByteArrayOutputStream();
        ;
        private boolean appendToOriResponse;

        public ResponseServletOutputStream(HttpServletResponse response, boolean appendToOriResponse) {
            this.originalResponse = response;
            this.appendToOriResponse = appendToOriResponse;
        }

        @Override
        public void write(int b) throws IOException {
            //log.warn("---------------write(int arg0)");
            respWrapperBuf.write(b);
            if (appendToOriResponse)
                originalResponse.getOutputStream().write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            //log.warn(" write(byte b[], int off, int len) ");
            respWrapperBuf.write(b, off, len);
            if (appendToOriResponse)
                originalResponse.getOutputStream().write(b, off, len);
        }

        private ByteArrayOutputStream getBuffer() {
            return respWrapperBuf;
        }

        @Override
        public void close() throws IOException {
            IOUtil.closeIOStream(respWrapperBuf);
            super.close();

        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }
    }

    private static class ResponsePrintWriter extends PrintWriter {
        private static final Logger log = LoggerFactory.getLogger(ResponseServletOutputStream.class);

        private ResponsePrintWriter(OutputStream buf, String characterEncoding)
                throws UnsupportedEncodingException {
            super(new OutputStreamWriter(buf, characterEncoding));
        }

        //重写此方法，主要是增加flush方法
        @Override
        public void write(char buf[], int off, int len) {
            //	log.warn(" char buf[], int off, int len");
            super.write(buf, off, len);
            super.flush();
        }

        @Override
        public void write(String s, int off, int len) {
            //log.warn(" String s, int off, int len");
            super.write(s, off, len);
            super.flush();
        }

        @Override
        public void write(int c) {
            //log.warn(" write(int c) ");
            super.write(c);
            super.flush();
        }
    }
}