package com.until;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

import com.alibaba.common.lang.StringUtil;
import com.pjo.build.in.object.JspServletBean;

public class JspCompileServlet {

	public StringBuffer jspContent = new StringBuffer();
	public JspServletBean servletContent=new JspServletBean();

	/**
	 * 编译java成class
	 * @param name	java文件名字
	 * @param content	java内容
	 * @return
	 */
	private final boolean compile(String name, String projectName,String content) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(
				null, null, null);
		StrSrcJavaObject srcObject = new StrSrcJavaObject(name, content);
		Iterable<? extends JavaFileObject> fileObjects = Arrays
				.asList(srcObject);
		String flag = "-d";
		String outDir = "";
		File classPath = new File(System.getProperty("user.dir")+ File.separator + "temServlet"+ File.separator+projectName+File.separator);
		outDir = classPath.getAbsolutePath() + File.separator;
		
		System.out.println("**************content*****************"+content);
		
		Iterable<String> options = Arrays.asList(flag, outDir);
		CompilationTask task = compiler.getTask(null, fileManager, null,
				options, null, fileObjects);
		boolean result = task.call();
		return result;
	}

	private class StrSrcJavaObject extends SimpleJavaFileObject {
		private String content;

		public StrSrcJavaObject(String name, String content) {
			super(URI.create("string:///" + name.replace('.', '/')
					+ Kind.SOURCE.extension), Kind.SOURCE);
			this.content = content;
		}

		public CharSequence getCharContent(boolean ignoreEncodingErrors) {
			return content;
		}
	}

	/**
	 * 根据路径读取jsp文件
	 * @param jspPath jsp文件路径
	 */
	private void readJspFile(String jspPath) {
		File jspFile = new File(jspPath);
		if (jspFile.exists()) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(jspFile),"utf-8"));
				String tempString = null;
				// 一次读入一行，直到读入null为文件结束
				while ((tempString = reader.readLine()) != null) {
					jspContent.append("\t\r" + tempString);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e1) {
					}
				}
			}
		}
		
		/*System.out.println("*********************jspContent***************************");
		System.out.println(jspContent.toString());
		System.out.println("*********************jspContent***************************");*/
	}
	
	/**
	 * 1利用正则表达式将jsp中的包文件提取出来
	 * 并放入jspServletBean中
	 */
	public void regex(){
		if(jspContent.length()!=0){
			Pattern pattern=Pattern.compile("<%@.*%>|<%@[^%>]*(\\r|\\n).*%>");
			Matcher matcher = pattern.matcher(jspContent);
			while (matcher.find()) {
				String in = matcher.group();
				dealJspHead(in);
				jspContent =new StringBuffer(jspContent.toString().replace(in,"\t\r"));
			}
		}
	}

	/**
	 * 将jsp文件转换成对应的数组
	 */
	public void explainJspContent(){

		StringBuffer rtn=new StringBuffer();
		List<String> jspOutPrint=new ArrayList<String>();
		List<String> javaOutPrint=new ArrayList<String>();
		List<String> javaIsPrint =new ArrayList<String>();
		
		if(jspContent.length()!=0){			
			Pattern pattern=Pattern.compile("<%[^%>]*((\\n|\\r).*)*%>");
			Matcher matcher=pattern.matcher(jspContent);
			
			int startIndex=0;
			int endIndex=0;
			
			while (matcher.find()) {
				String in = matcher.group();
				if(in.contains("<%=")){
					javaIsPrint.add("isPrint");
					javaOutPrint.add(in.replace("<%=","").replace("%>",""));
				}else if(in.contains("<%")){
					javaIsPrint.add("Print");
					javaOutPrint.add(in.replace("<%","").replace("%>",""));
				}
				endIndex=matcher.start();
				jspOutPrint.add(jspContent.substring(startIndex,endIndex));
				startIndex=matcher.end();
			}
			jspOutPrint.add(jspContent.substring(startIndex));
		}
		
		for (int i = 0; i <jspOutPrint.size(); i++) {
			rtn.append(outPrintHtml(jspOutPrint.get(i)));
			if(javaIsPrint.size()>i){				
				if("isPrint".equals(javaIsPrint.get(i))){
					rtn.append(outPrintHtml(javaOutPrint.get(i)));
				}else if("Print".equals(javaIsPrint.get(i))){
					rtn.append(javaOutPrint.get(i));
				}
			}
		}
		
		servletContent.jspServerMethod=rtn;
	}
	
	public StringBuffer outPrintHtml(String in){
		String []outPrint=in.split("\r");
		StringBuffer rtn=new StringBuffer();
		for(String oo:outPrint){
			rtn.append("out.write(\"");
			rtn.append(oo.replace("\t", "\\t").replace("\"", "\\\""));
			rtn.append("\");");
			rtn.append("\t\r");
		}
		return rtn;
	}
	
	
	public void dealJspHead(String header){
		for(String needImport:regexRtn("import=\"[^\"]*\"", header)){
			servletContent.importContent.append(needImport.replace("="," ").replace("\"",""));
			servletContent.importContent.append(";\t\r");
		}
		if(null==servletContent.responseEconding){			
			for(String responseEconding:regexRtn("pageEncoding=\".*\"",header)){
				servletContent.responseEconding=responseEconding.replace("pageEncoding=\"","").replace("\"", "");
			}
		}		
	}
	
	/**
	 * 根据正则表达式选出符合正则表达式的字符串
	 * @param regularEx 正则表达式
	 * @param needRE	待选出的字符串
	 * @return
	 */
	public String[] regexRtn(String regularEx,String needRE){
		Pattern pattern=Pattern.compile(regularEx);
		Matcher matcher = pattern.matcher(needRE);
		List<String> rtn=new ArrayList<String>();
		while (matcher.find()) {
			rtn.add(matcher.group());
		}
		return rtn.toArray(new String[]{});
	}
	
	public void serialize(String path,String content){
		File file =new File(path);
		File parent = file.getParentFile(); 
		if(parent!=null&&!parent.exists()){ 
			parent.mkdirs(); 
		}
		FileOutputStream oos=null;
		try {
			file.createNewFile();
			oos= new FileOutputStream(file);
			oos.write(content.getBytes("UTF-8"));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				oos.flush();
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 將jsp转换成java并且编译
	 * @param jspPath jsp文件路径
	 * @return		  转换后的java的文件名
	 */
	public String generateJspServlet(String jspPath){
		String jspName=jspPath.substring(jspPath.lastIndexOf("\\")+1).replace(".jsp","");
		jspName=changeNameStyle(jspName);
		String absolutePath=jspPath.substring(jspPath.indexOf("\\webapps")).replace("\\webapps","");
		String projectName=absolutePath.substring(1,absolutePath.lastIndexOf("\\"));
		readJspFile(jspPath);
		servletContent.init();
		regex();
		explainJspContent();
		servletContent.packageContent="package com.jsp.servlet;";
		servletContent.jspName=jspName;
		String content=servletContent.generateContent().toString();
		//System.out.println(content);
		serialize("temServlet/"+projectName+"/com/jsp/servlet/"+jspName+".java", content);
		compile("com.jsp.servlet."+jspName,projectName, content);
		return jspName;
	}
	
	/**
	 * 将name_type转换成JspNameType的格式
	 * @param name
	 * @return
	 */
	public static String changeNameStyle(String name){
		String []names=name.split("_");
		String rtn="Jsp";
		for(String namePart:names){
			rtn=rtn+StringUtil.capitalize(namePart);
		}
		return rtn;
	}
	
}
