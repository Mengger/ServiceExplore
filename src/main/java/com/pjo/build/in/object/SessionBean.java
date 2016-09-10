package com.pjo.build.in.object;

import java.util.HashMap;
import java.util.Map;

public class SessionBean {

	private static Map<String, Session> sessionBean;
	
	public void init(){
		sessionBean=new HashMap<String, Session>();
	}
	
	public static boolean isExist(String sessionId){
		if(null!=sessionBean.get(sessionId)){
			return false;
		}else{
			return true;
		}		
	}
	
	public static void removeSession(String sessionId){
		sessionBean.remove(sessionId);
	}
	
}
