package com.pjo.build.in.object;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.http.service.HttpServer;
import com.until.ClassLoadingUntil;

public class Response implements HttpServletResponse {
	
	private static final int BUFFER_SIZE = 1024;  
    private Request request;
    private OutputStream outputStream;
    private Map<String, String> responseHeader;
    private Map<String, String> Header;
    
    public Response(){
    }
    
    public Map<String, String> getResponseHeader() {
		return responseHeader;
	}

	public Map<String, String> getHeader() {
		return Header;
	}

	public Response(OutputStream outputStream) {  
        this.outputStream =outputStream;  
    }  
  
    public void setRequest(Request request) {  
        this.request = request;  
    }  
    
    public void initResponse(){
    	this.responseHeader=new HashMap<String, String>();
    	this.Header=new HashMap<String, String>();
    	this.Header.put("Content-Type", "text/html");
    	this.Header.put("Server", "ContentLI");
    	this.Header.put("Date", new Date().toGMTString());
    } 
  
    public void sendStaticResource(boolean isFile,Response response) throws IOException{
    	initResponse();
    	if(this.request.exitSession){
    		Cookie sessionCookie= new Cookie("JSESSIONID",request.getRequestedSessionId());
    		sessionCookie.setPath(request.getRequestURI());
    		addCookie(sessionCookie);
    	}
        if(isFile){
        	returnFile();
        }else{
        	process(response);
        }
    }  
	
    public void returnFile() throws IOException{
    	 byte bytes[] = new byte[BUFFER_SIZE];  
         FileInputStream fis = null;  
         try{  
             File file = new File(HttpServer.WEB_ROOT,request.getRequestURI());  
             if(file.exists()){
            	 if(request.getRequestURI().contains(".")){
            		 String uriType=request.getRequestURI().substring(request.getRequestURI().lastIndexOf("."));
            		 if("js".equals(uriType)){
            			 this.Header.put("Content-Type","application/x-javascript");
            		 }else if("jpeg".equals(uriType)||"png".equals(uriType)||"jpg".equals(uriType)||"gif".equals(uriType)){
            			 this.Header.put("Content-Type","image/"+uriType);
            		 }else if("css".equals(uriType)){
            			 this.Header.put("Content-Type", "text/css");
            		 }
            	 }
                 fis = new FileInputStream(file);  
                 int ch = fis.read(bytes,0,BUFFER_SIZE);  
                 while(ch != -1){  
                     outputStream.write(bytes, 0, ch);  
                     ch = fis.read(bytes,0,BUFFER_SIZE);  
                 }  
             }else{  
            	 String errorMessage = "<h1>艹,眼睛找瞎了都没找到，一定是你的输入出了问题。恩，一定是这样的！！！</h1>";  
            	 outputStream.write(errorMessage.getBytes());  
             }  
         }catch(Exception e){  
         	e.printStackTrace(); 
         }finally{  
             if(fis != null)  
             fis.close();  
         } 
    }
    
    
    /**
     * 用请求调用servlet中的方法
     * @param response
     */
    public void process(Response response) {
    	String uri=request.getRequestURI();
    	String servletName=null;
    	servletName=uri.substring(uri.lastIndexOf("/")+1);
    	String folderPath=HttpServer.WEB_ROOT+uri.substring(1,uri.indexOf("/",1))+"\\WEB-INF\\classes\\"/*+request.getServletPath().substring(0,request.getServletPath().lastIndexOf(".")).replace(".","\\")+"\\"*/;
    	ClassLoadingUntil.ClassLoadingServlet(request.getMethod(),folderPath,this.request,response,request.getServletPath());
    }  
    
  
	@Override
	public void flushBuffer() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getBufferSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return this.responseHeader.get("Content-Type");
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		// TODO Auto-generated method stub
		return new PrintWriter(outputStream);
	}

	@Override
	public boolean isCommitted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetBuffer() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBufferSize(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCharacterEncoding(String arg0) {
		String contentType=this.Header.get("Content-Type");
		if(contentType.contains(";charset=")){
			this.Header.put("Content-Type",contentType.split(";")[0]+";charset="+arg0);
		}else{
			this.Header.put("Content-Type", contentType+";charset="+arg0);
		}
	}

	@Override
	public void setContentLength(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setContentType(String arg0) {
		this.Header.put("Content-Type", arg0);
		this.responseHeader.put("Content-Type", arg0);
	}

	@Override
	public void setLocale(Locale arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addCookie(Cookie arg0) {
		StringBuffer cookieValue=new StringBuffer();
		cookieValue.append(arg0.getName()).append("=").append(arg0.getValue());
		if(null!=arg0.getPath()){
			cookieValue.append(";").append(arg0.getPath());
		}
		this.responseHeader.put("Set-Cookie", String.valueOf(cookieValue));
	}

	@Override
	public void addDateHeader(String arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addHeader(String arg0, String arg1) {
		this.responseHeader.put(arg0, arg1);
	}

	@Override
	public void addIntHeader(String arg0, int arg1) {
		this.responseHeader.put(arg0, String.valueOf(arg1));
	}

	@Override
	public boolean containsHeader(String arg0) {
		boolean condition=false;
		if(null!=this.responseHeader.get(arg0))
			condition=true;
		return condition;
	}

	@Override
	public String encodeRedirectURL(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeRedirectUrl(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeURL(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeUrl(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHeader(String arg0) {
		// TODO Auto-generated method stub
		return this.responseHeader.get(arg0);
	}

	@Override
	public Collection<String> getHeaderNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getHeaders(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void sendError(int arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendError(int arg0, String arg1) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendRedirect(String arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDateHeader(String arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setHeader(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setIntHeader(String arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStatus(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStatus(int arg0, String arg1) {
		// TODO Auto-generated method stub

	}

}
