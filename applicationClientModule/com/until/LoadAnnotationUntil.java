package com.until;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.pjo.web.ServletRecord;
import com.servlerBean.ServlertContent;
import com.servlerBean.ServletContentCollection;

public class LoadAnnotationUntil {
		
	public static List<String> getClassPath(String path){
		List<String> allClassPath=new ArrayList<String>();
		return getOpenFile(allClassPath, path);
	}
	
	/**
	 * 将filePath中的文件逐级打开，如果是class文件的话，直接放进path中
	 * @param path		存储class文件的路径
	 * @param filePath	需要逐级解开的文件的路径
	 * @return
	 */
	public static List<String> getOpenFile(List<String> path,String filePath){
		File files=new File(filePath);
		for(String fileName:files.list()){
			File file=new File(files,fileName);
			if(file.exists()&&file.isDirectory()){
				getOpenFile(path,file.getPath());
			}else if(file.exists()){
				String filePathInfo=file.getPath();
				if(".class".equals(filePathInfo.substring(filePathInfo.lastIndexOf(".")))){
					path.add(file.getPath());
				}
			}
		}
		return path;
	}
	/**
	 * 将注解的servlet的配置信息配置进servletContent中
	 * @param relativePath	工程的相对位置
	 * @param classPath		类的位置
	 * @param projectName	项目名称
	 * @throws ClassNotFoundException 
	 * @throws MalformedURLException 
	 */
	public static void LoadClassByClassPath(String path) throws ClassNotFoundException, MalformedURLException{
		int index=path.indexOf("WEB-INF\\classes");
		String relativePath=path.substring(0,index-1);
		String classPath=path.substring(index+16).replace(".class", "").replace("\\", ".");
		String projectName=relativePath.substring(relativePath.lastIndexOf("\\")+1);
		relativePath=relativePath+"\\WEB-INF\\classes\\";
		URLClassLoader loader;
		Class clazz=null;
		loader=new URLClassLoader(new URL[]{new URL("file:"+relativePath)});
		clazz=loader.loadClass(classPath);
		boolean condition=false;
		condition=clazz.isAnnotationPresent(WebServlet.class);
		if(condition){
			WebServlet webServlet=(WebServlet)clazz.getAnnotation(WebServlet.class);
			System.out.println(webServlet.value()[0]);
			ServletRecord servletRecord=new ServletRecord();
			servletRecord.setUrlpattern(webServlet.value()[0]);
			servletRecord.setServletClass(classPath);
			servletRecord.setServletName(webServlet.name());
			ServlertContent servletContent=ServletContentCollection.servletContentList.get(projectName);
			if(null==servletContent) servletContent=new ServlertContent();
			if(null!=servletContent.getServletList()){
				servletContent.getServletList().add(servletRecord);
			}else{
				List<ServletRecord> servletList=new ArrayList<ServletRecord>();
				servletList.add(servletRecord);
				servletContent.setServletList(servletList);;
			}
		}
	}
	
	public static void main(String[] args) {
		for(String filePath:getClassPath("C:\\Users\\wb-limeng.g\\Desktop\\aaaa\\webapps\\")){
		//	System.out.println(filePath);
			try {
				LoadClassByClassPath(filePath);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}