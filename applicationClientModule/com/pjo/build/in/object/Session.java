package com.pjo.build.in.object;

import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import com.until.readXML.SessionIdKey;

public class Session implements HttpSession {
	
	private Cookie cookie;
	private String sessionID;
	private long creationTime;
	private long lastAccessedTime;
	private Map<String, Object> attribute;
	private int maxInactiveInterval;
	
	public void generateNewSession(){
		if(isNew()){
			try {
				SessionIdKey desPlus = new SessionIdKey();
				this.sessionID=desPlus.encrypt(String.valueOf(hashCode()));
			} catch (Exception e) {
				this.sessionID=null;
				e.printStackTrace();
			}
			this.cookie=new Cookie("JSESSIONID", sessionID);
			this.creationTime= new Date().getTime();
		}else{
			this.lastAccessedTime=new Date().getTime();
		}
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
	public long getCreationTime() {
		return this.creationTime;
	}

	@Override
	public String getId() {
		return sessionID;
	}

	@Override
	public long getLastAccessedTime() {
		return this.lastAccessedTime;
	}

	@Override
	public int getMaxInactiveInterval() {
		return this.maxInactiveInterval;
	}

	@Override
	public ServletContext getServletContext() {
		return null;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		return null;
	}

	@Override
	public Object getValue(String arg0) {
		return this.attribute.get(arg0);
	}

	@Override
	public String[] getValueNames() {
		String[] str=null;
		return this.attribute.keySet().toArray(str);
	}

	@Override
	public void invalidate() {
		SessionBean.removeSession(sessionID);
	}

	@Override
	public boolean isNew() {
		return !SessionBean.isExist(sessionID);
	}

	@Override
	public void putValue(String arg0, Object arg1) {}

	@Override
	public void removeAttribute(String arg0) {
		this.attribute.remove(arg0);
	}

	@Override
	public void removeValue(String arg0) {}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		this.attribute.put(arg0, arg1);
	}

	@Override
	public void setMaxInactiveInterval(int arg0) {
		this.maxInactiveInterval=arg0;
	}

}
