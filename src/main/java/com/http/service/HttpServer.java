package com.http.service;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
            try{  
              
                
            	new HanderDeal(serverSocket.accept()).start();
              
            }catch(Exception e){  
                e.printStackTrace();  
                continue;  
            }  
        }  
    }  
    
   
}
