package com.http.service.servlet.test;
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  
import java.net.InetAddress;  
import java.net.ServerSocket;  
import java.net.Socket;  
  
public class HttpServer1Test {  
    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";  
    private boolean shutdown = false;  
      
    public static void main(String[] args) {  
        HttpServer1Test server = new HttpServer1Test();  
        server.await();  
    }  
  
    private void await() {  
        ServerSocket serverSocket = null;  
        int port = 8080;  
        try{  
            serverSocket = new ServerSocket(port,1,InetAddress.getByName("127.0.0.1"));  
        }catch(IOException e){  
            e.printStackTrace();  
            System.exit(1);  
        }  
          
        while(!shutdown){  
            Socket socket = null;  
            InputStream input = null;  
            OutputStream output = null;  
            try{  
                socket = serverSocket.accept();  
                input = socket.getInputStream();  
                output = socket.getOutputStream();  
                  
                RequestTest request = new RequestTest(input);  
                request.parse();  
                  
                ResponseTest response = new ResponseTest(output);  
                response.setRequest(request);  
                  
                if(request.getUri().startsWith("/servlet/")){  
                    ServletProcessor1Test processor = new ServletProcessor1Test();  
                    processor.process(request,response);  
                }else{  
                    StaticResourceProcessorTest processor = new StaticResourceProcessorTest();  
                    processor.process(request,response);  
                }  
                  
                socket.close();  
                  
                shutdown = request.getUri().equals(SHUTDOWN_COMMAND);  
                  
            }catch(IOException e){  
                e.printStackTrace();  
                System.exit(1);  
            }  
        }  
          
    }  
  
}  