package com.until;

import java.lang.reflect.Method;
import java.net.URL;

import com.pjo.build.in.object.Request;
import com.pjo.build.in.object.Response;

public class ClassLoadingUntil {

    /**
     * 加载servlet方法
     * @param servletMethod 加载的方法名
     * @param folderPath	加载路径
     * @param response		相应容器
     */
    public static void ClassLoadingServlet(String servletMethod,String folderPath,Request request,Response response,String servletPath){
    	/*System.out.println("*************"+servletMethod+"**************");
    	System.out.println("*************"+folderPath+"**************");
    	System.out.println("*************"+servletPath+"**************");*/
    	ServletClassLoading loader;
		try {
			loader=new ServletClassLoading(new URL[]{new URL("file:"+folderPath)},folderPath);
			if("GET".equals(servletMethod)){
				servletMethod="doGet";
			}else if("POST".equals(servletMethod)){
				servletMethod="doPost";
			}else if("jspService".equals(servletMethod)){
				servletMethod="jspService";
			}
		//	System.out.println("*************"+servletMethod+"*********************");
			Class clazz=loader.loadClass(servletPath);
			clazz.getMethod("init",null).invoke(clazz.newInstance(),null);//servlet创建自动加载方法
			Method methodSer=clazz.getDeclaredMethod(servletMethod,javax.servlet.http.HttpServletRequest.class,javax.servlet.http.HttpServletResponse.class);
			methodSer.setAccessible(true);
			methodSer.invoke(clazz.newInstance(),request,response);//调用servlet请求
			clazz.getMethod("destroy",null).invoke(clazz.newInstance(),null);//调用servlet销毁方法
		} catch (Exception e) {
			e.printStackTrace();
		}
         
    }
}
