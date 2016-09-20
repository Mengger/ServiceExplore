package com.http.service.servlet.test;

import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.IOException;  
import java.io.OutputStream;  
import java.io.PrintWriter;  
import java.util.Locale;  
  
import javax.servlet.ServletOutputStream;  
import javax.servlet.ServletResponse;  
  
public class ResponseTest implements ServletResponse{  
    private static final int BUFFER_SIZE = 1024;  
    private OutputStream output;  
    private RequestTest request;  
    private PrintWriter writer;  
  
    public ResponseTest(OutputStream output) {  
        this.output = output;  
    }  
  
    public void setRequest(RequestTest request) {  
        this.request = request;  
          
    }  
      
    public void sendStaticResource() throws IOException {  
        byte[] bytes = new byte[BUFFER_SIZE];  
        FileInputStream fis = null;  
        try {  
            /* request.getUri has been replaced by request.getRequestURI */  
            File file = new File(ConstantsTest.WEB_ROOT, request.getUri());  
            fis = new FileInputStream(file);  
            /* 
             * HTTP Response = Status-Line(( general-header | response-header | 
             * entity-header ) CRLF) CRLF [ message-body ] Status-Line = 
             * HTTP-Version SP Status-Code SP Reason-Phrase CRLF 
             */  
            int ch = fis.read(bytes, 0, BUFFER_SIZE);  
            while (ch != -1) {  
                output.write(bytes, 0, ch);  
                ch = fis.read(bytes, 0, BUFFER_SIZE);  
            }  
        } catch (FileNotFoundException e) {  
            String errorMessage = "HTTP/1.1 404 File Not Found\r\n"  
                    + "Content-Type: text/html\r\n" + "Content-Length: 23\r\n"  
                    + "\r\n" + "<h1>File Not Found</h1>";  
            output.write(errorMessage.getBytes());  
        } finally {  
            if (fis != null)  
                fis.close();  
        }  
    }  
        
    public void flushBuffer() throws IOException {  
        // TODO Auto-generated method stub  
          
    }  
  
    public int getBufferSize() {  
        // TODO Auto-generated method stub  
        return 0;  
    }  
  
    public String getCharacterEncoding() {  
        // TODO Auto-generated method stub  
        return null;  
    }  
  
    public String getContentType() {  
        // TODO Auto-generated method stub  
        return null;  
    }  
  
    public Locale getLocale() {  
        // TODO Auto-generated method stub  
        return null;  
    }  
  
    public ServletOutputStream getOutputStream() throws IOException {  
        // TODO Auto-generated method stub  
        return null;  
    }  
  
    public PrintWriter getWriter() throws IOException {  
        writer = new PrintWriter(output,true);  
        return writer;  
    }  
  
    public boolean isCommitted() {  
        // TODO Auto-generated method stub  
        return false;  
    }  
  
    public void reset() {  
        // TODO Auto-generated method stub  
          
    }  
  
    public void resetBuffer() {  
        // TODO Auto-generated method stub  
          
    }  
  
    public void setBufferSize(int arg0) {  
        // TODO Auto-generated method stub  
          
    }  
  
    public void setCharacterEncoding(String arg0) {  
        // TODO Auto-generated method stub  
          
    }  
  
    public void setContentLength(int arg0) {  
        // TODO Auto-generated method stub  
          
    }  
  
    public void setContentType(String arg0) {  
        // TODO Auto-generated method stub  
          
    }  
  
    public void setLocale(Locale arg0) {  
        // TODO Auto-generated method stub  
          
    }

	@Override
	public void setContentLengthLong(long arg0) {
		// TODO Auto-generated method stub
		
	}  
  
}  
