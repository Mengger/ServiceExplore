package com.http.service;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;

import com.pjo.build.in.object.Request;
import com.pjo.build.in.object.Response;
import com.pjo.web.ServletRecord;
import com.servlerBean.ServlertContent;
import com.servlerBean.ServletContentCollection;
import com.until.ClassLoadingUntil;
import com.until.JspCompileServlet;

public class HanderDeal extends Thread {

	Socket socket;
	
	public HanderDeal(Socket socket){
		this.socket=socket;
	}
	
	public void run() {
		try {
			excute(socket.getInputStream(),socket.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void excute(InputStream inputStream, OutputStream outputStream) throws Exception {
		PrintStream op = new PrintStream(outputStream);

		op.println("HTTP/1.1 200 OK");
		Date now = new Date();
		op.println("Data:" + now);
		op.println("Server: MengerTest");
		op.println("Access-Control-Allow-Origin:*");

		op.println("Content-Type: text/css; charset=utf-8");
		op.println();

		/*System.out.println("**************************************************");
		Date start=new Date();
		System.out.println(start.getHours()+"-"+start.getMinutes()+"-"+start.getSeconds());
		System.out.println(Thread.currentThread().getId());
		Thread.sleep(10000l);
		Date end=new Date();
		System.out.println(end.getHours()+"-"+end.getMinutes()+"-"+end.getSeconds());
		System.out.println("**************************************************");
*/
		// 创建请求对象并解析
		Request request = new Request(inputStream);
		request.parse();

		// 创建响应对象
		Response response = new Response(outputStream);
		response.setRequest(request);
		String path = null;
		path = request.getRequestURI();
		String folderName = path.substring(1, path.indexOf("/", 1));
		ServlertContent servletContent = ServletContentCollection.servletContentList.get(folderName);
		if (path.contains(".jsp")) {
			File file = new File(HttpServer.WEB_ROOT, request.getRequestURI());
			if (file.exists()) {
				String jspPath = file.getPath();
				String uri = request.getRequestURI();
				JspCompileServlet jspCompile = new JspCompileServlet();
				String jspServlet = jspCompile.generateJspServlet(jspPath);
				// System.out.println("************************uri**************************"+uri);
				String folderPath = System.getProperty("user.dir") + File.separator + "temServlet" + File.separator
						+ uri.substring(1, uri.lastIndexOf("/")) + File.separator;
				// System.out.println("******folderPath*******"+folderPath);
				ClassLoadingUntil.ClassLoadingServlet("jspService", folderPath, request, response,
						"com.jsp.servlet." + jspServlet);
			}
		} else {
			if (null != servletContent) {
				boolean isFile = true;
				for (ServletRecord servletRecord : servletContent.getServletList()) {
					/*
					 * System.out.println(
					 * "servletRecord.getUrlpattern()*************"+
					 * servletRecord.getUrlpattern()); System.out.println(
					 * "path.substring(path.indexOf(\"/\",1))***************"+
					 * path.substring(path.indexOf("/",1)));
					 */

					if (path.substring(path.indexOf("/", 1)).equals(servletRecord.getUrlpattern())) {
						isFile = false;
						break;
					}
				}
				response.sendStaticResource(isFile, response);
			} else {
				response.sendStaticResource(true, response);
			}
		}

		/*
		 * for(String head:response.getHeader().keySet()){
		 * op.println(head+":"+response.getHeader().get(head)+"\t\r"); }
		 * 
		 * for(String head:response.getResponseHeader().keySet()){
		 * op.println(head+":"+response.getResponseHeader().get(head)+"\t\r"); }
		 * 
		 */

		outputStream.flush();
		op.flush();
		outputStream.close();
		op.close();
		//关闭socket  
        if(socket != null){
             socket.close();  
        }  
	}
}
