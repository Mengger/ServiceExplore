package com.pjo.build.in.object;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.pjo.web.ServletRecord;
import com.servlerBean.ServlertContent;
import com.servlerBean.ServletContentCollection;

public class Request implements HttpServletRequest {

	private InputStream inputStream;
	private Map<String, String[]> parameterMap;
	private Map<String, Object> attribute=new HashMap<String, Object>();
	private String characterEncoding;
	private String requestMethod;
	private Map<String, String> HeaderMap;
	private String uri;
	boolean exitSession=false;
	
	public Request() {
	}
	
	public Request(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void parse() {
		HeaderMap=new HashMap<String, String>();
		StringBuffer request = new StringBuffer(2048);
		int i;
		byte[] buffer = new byte[2048];
		try {
			i = inputStream.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
			i = -1;
		}
		for (int j = 0; j < i; j++) {
			request.append((char) buffer[j]);
		}
		parseInputToRequest(request.toString());
		parseRequest(request.toString());
	}
	
	public void parseInputToRequest(String requestString){
		String [] str=requestString.split("\n");
		this.HeaderMap.put("headContent", str[0]);
    	for(String s:str){
    		if(s.contains(":")){
    			String []content=s.split(":");
    			this.HeaderMap.put(content[0].trim(), content[1].trim());
    		}
    	}
	}

	private void parseRequest(String requestString) {
		int index1, index2;
		index1 = requestString.indexOf(" ");
		this.requestMethod=requestString.substring(0,index1);
		if (index1 != -1) {
			index2 = requestString.indexOf(" ", index1 + 1);
			if (index2 > index1)
				uri=requestString.substring(index1 + 1, index2);
		}

		if("GET".equals(this.requestMethod)){
			try {
				uri = URLDecoder.decode(uri, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String headContent=null;
			headContent=uri.trim();
			if(headContent.contains("?"))
				parseURLParm(headContent.split("?")[1]);
		}else if("POST".equals(this.requestMethod)){
			int contentLength=Integer.parseInt(HeaderMap.get("Content-Length"));
			int length=requestString.length();
			String contentParam=requestString.substring(length-contentLength,length);
			parseURLParm(contentParam);
		}
	}

	
	/**
	 * 将请求的参数依次放入parameter中
	 * @param params
	 */
	public void parseURLParm(String params){
		parameterMap=new HashMap<String, String[]>();
		String [] paramArray=params.split("&");
		for(String param:paramArray){
			String[] keyValue=param.split("=");
			if(null!=parameterMap.get(keyValue[0])){
				List<String> list = new ArrayList(Arrays.asList(parameterMap.get(keyValue[0])));
				list.add(keyValue[1]);
				parameterMap.put(keyValue[0],(String[])list.toArray());
			}else{
				String[] values={keyValue[1]};
				parameterMap.put(keyValue[0],values);
			}
		}
	}

	@Override
	public AsyncContext getAsyncContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAttribute(String arg0) {
		return this.attribute.get(arg0);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return null;
	}

	@Override
	public String getCharacterEncoding() {
		return this.characterEncoding;
	}

	@Override
	public int getContentLength() {
		return Integer.parseInt(HeaderMap.get("Content-Length"));
	}

	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public DispatcherType getDispatcherType() {
		return null;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return null;
	}

	@Override
	public String getLocalAddr() {
		String localInfo=null;
		localInfo=this.HeaderMap.get("Host");
		if(localInfo.contains(":")){
			return localInfo.split(":")[0].trim();
		}
		return localInfo;
	}

	@Override
	public String getLocalName() {
		return getLocalAddr();
	}

	@Override
	public int getLocalPort() {
		String localInfo=null;
		localInfo=this.HeaderMap.get("Host");
		if(localInfo.contains(":")){
			return Integer.parseInt(localInfo.split(":")[1].trim());
		}
		return 80;
	}

	@Override
	public Locale getLocale() {
		Locale locale=null;
		String language=null;
		language=this.HeaderMap.get("Accept-Language").trim();
		if(null!=language){
			if(language.contains(";")&&language.contains(",")){
				String[] languageInfo=language.split(";")[0].trim().split(",")[0].trim().split("-");
				locale=new Locale(languageInfo[0], languageInfo[1]);
			}
		}
		return locale;
	}

	@Override
	public Enumeration<Locale> getLocales() {
		return null;
	}

	@Override
	public String getParameter(String arg0) {
		String[] rtn = this.parameterMap.get(arg0);
		if (null != rtn) {
			return rtn[0];
		} else {
			return null;
		}
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return this.parameterMap;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getParameterValues(String arg0) {
		return this.parameterMap.get(arg0);
	}

	@Override
	public String getProtocol() {
		String headContent=null;
		headContent=this.HeaderMap.get("headContent");
		return headContent.substring(headContent.indexOf(" ",headContent.indexOf(" ")+1)+1);
	}

	@Override
	public BufferedReader getReader() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRealPath(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteHost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRemotePort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScheme() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getServerPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAsyncStarted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAsyncSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeAttribute(String arg0) {
		this.attribute.remove(arg0);
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		this.attribute.put(arg0, arg1);
	}

	@Override
	public void setCharacterEncoding(String arg0)
			throws UnsupportedEncodingException {
		this.characterEncoding = arg0;
	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AsyncContext startAsync(ServletRequest arg0, ServletResponse arg1)
			throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean authenticate(HttpServletResponse arg0) throws IOException,
			ServletException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getAuthType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContextPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cookie[] getCookies() {
		Cookie[] rtnCookie=null;
		String cookiesInfo=null;
		cookiesInfo=this.HeaderMap.get("Cookie");
		if(null!=cookiesInfo){
			String[] cookies=cookiesInfo.split(";");
			int cookieSize=cookies.length;
			rtnCookie=new Cookie[cookieSize];
			int index=0;
			for(String cookieInfo:cookies){
				String[] cookie=cookieInfo.trim().split("=");
				Cookie cook=new Cookie(cookie[0], cookie[1]);
				rtnCookie[index]=cook;
				index++;
			}
		}
		return rtnCookie;
	}

	@Override
	public long getDateHeader(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getHeader(String arg0) {
		// TODO Auto-generated method stub
		return this.HeaderMap.get(arg0);
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<String> getHeaders(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIntHeader(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return requestMethod;
	}

	@Override
	public Part getPart(String arg0) throws IOException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPathInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPathTranslated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQueryString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestURI() {
		// TODO Auto-generated method stub
		return this.uri;
	}

	@Override
	public StringBuffer getRequestURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestedSessionId() {
		return getSession().getId();
	}

	@Override
	public String getServletPath() {
		// TODO Auto-generated method stub
		ServlertContent servletContent=ServletContentCollection.servletContentList.get(uri.substring(1,uri.indexOf("/",1)));
		if(null!=servletContent){
			String urlPattern=uri.substring(uri.indexOf("/",1));
			List<ServletRecord> servletList=servletContent.getServletList();
			for(ServletRecord servlet :servletList){
				if(servlet.getUrlpattern().equals(urlPattern)){
					return servlet.getServletClass();
				}
			}
		}
		return null;
	}

	@Override
	public HttpSession getSession() {
		exitSession=true;
		HttpSession session=null;
		for(Cookie cookie:getCookies()){
			if("JSESSIONID".equals(cookie.getName())){
				session=new Session(cookie.getValue());
				return session;
			}
		}
		return new Session();
	}

	@Override
	public HttpSession getSession(boolean arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUserInRole(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void login(String arg0, String arg1) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void logout() throws ServletException {
		// TODO Auto-generated method stub

	}

}
