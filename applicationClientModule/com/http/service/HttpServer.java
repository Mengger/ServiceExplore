package com.http.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.pjo.build.in.object.Request;
import com.pjo.build.in.object.Response;
import com.pjo.web.ServletRecord;
import com.servlerBean.ServlertContent;
import com.servlerBean.ServletContentCollection;
import com.until.ClassLoadingUntil;
import com.until.JspCompileServlet;

public class HttpServer {
	/** 
     * WEB_ROOT是存放HTML文件的目录 
     */  
    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webapps"+ File.separator;  
      
    //关闭命令  
    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";  
      
    //收到关闭命令  
    private boolean shutdown = false;  
  
    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
        HttpServer server = new HttpServer();  
        server.await();  
  
    }  
 
    public void await() {  
        ServerSocket serverSocket = null;  
        int port = 8888;  
        try{      
            serverSocket = new ServerSocket(port);   
        }catch(IOException e){  
            e.printStackTrace();  
            System.exit(1);  
        }  
        //等待请求  
        while(!shutdown){  
            Socket socket = null;  
            InputStream inputStream = null;  
            OutputStream outputStream = null;  
            try{  
              
                socket = serverSocket.accept();  
                inputStream = socket.getInputStream();  
                outputStream = socket.getOutputStream();  
                OutputStreamWriter  osw = new OutputStreamWriter(outputStream,"utf-8");   
                
                osw.write("HTTP/1.1 200 OK\r\n");
                osw.write("Server: Apache-Coyote/1.1\r\n");
                osw.write("Set-Cookie: JSESSIONID=03493794995CE31A0F131787B6C6CBB2; Path=/; HttpOnly\r\n");
                osw.write("Content-Type: text/html;charset=UTF-8\r\n");
                osw.write("Transfer-Encoding: chunked\r\n");
                osw.write("Date: Tue, 19 May 2015 02:48:27 GMT\r\n");
                osw.write("\r\n");
                
                //创建请求对象并解析  
                Request request = new Request(inputStream);  
                request.parse();  
                  
                //创建响应对象  
                Response response = new Response(outputStream);  
                response.setRequest(request); 
                String path=null;
                path=request.getRequestURI();
                String folderName=path.substring(1,path.indexOf("/",1));
                ServlertContent servletContent=ServletContentCollection.servletContentList.get(folderName);
                if(path.contains(".jsp")){
                	File file = new File(HttpServer.WEB_ROOT,request.getRequestURI());
                	if(file.exists()){
                		String jspPath=file.getPath();
                		String uri=request.getRequestURI();
                		JspCompileServlet jspCompile=new JspCompileServlet();
                		String jspServlet=jspCompile.generateJspServlet(jspPath);
                		//System.out.println("************************uri**************************"+uri);
                		String folderPath=System.getProperty("user.dir")+File.separator +"temServlet"+File.separator+uri.substring(1,uri.lastIndexOf("/"))+File.separator;
                		//System.out.println("******folderPath*******"+folderPath);
                		ClassLoadingUntil.ClassLoadingServlet("jspService", folderPath,request, response ,"com.jsp.servlet."+jspServlet); 
                	}
                }else{                	
                	if(null!=servletContent){
                		boolean isFile=true;
                		for(ServletRecord servletRecord:servletContent.getServletList()){
                			/*System.out.println("servletRecord.getUrlpattern()*************"+servletRecord.getUrlpattern());
                			System.out.println("path.substring(path.indexOf(\"/\",1))***************"+path.substring(path.indexOf("/",1)));*/
                
                			if(path.substring(path.indexOf("/",1)).equals(servletRecord.getUrlpattern())){
                				isFile=false;
                				break;
                			}
                		}
                		response.sendStaticResource(isFile,response); 
                	}else{
                		response.sendStaticResource(true,response); 
                	}
                }
                
               
                
             /*   for(String head:response.getHeader().keySet()){
                	outputStream.write((head+":"+response.getHeader().get(head)+"\t\r").getBytes());
                }
                
                for(String head:response.getResponseHeader().keySet()){
                	outputStream.write((head+":"+response.getResponseHeader().get(head)+"\t\r").getBytes());
                }
                */
                
                 socket.setReceiveBufferSize(25188);
                 socket.setSendBufferSize(43800);
                 socket.setOOBInline(true);
                 socket.setKeepAlive(false);
                 socket.setPerformancePreferences(1,0,1);
                 socket.setReuseAddress(true);
                 socket.setSoLinger(true,25);
                 socket.setSoTimeout(5000);
                 socket.setTcpNoDelay(true);
                 socket.setTrafficClass(28);
                 
                 
                 osw.flush();
                 osw.close();
                outputStream.flush();
                outputStream.close();
                //关闭socket  
                if(socket != null){
                     socket.close();  
                }  
                    
                //检查URI是否是一个关闭命令  
                shutdown = SHUTDOWN_COMMAND.equals(request.getRequestURI());  
                  
            }catch(Exception e){  
                e.printStackTrace();  
                continue;  
            }  
        }  
    }  
}
