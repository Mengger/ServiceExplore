package com.http.service.servlet.test;

import java.io.IOException;  

public class StaticResourceProcessorTest {  
  
    public void process(RequestTest request, ResponseTest response) {  
        try{  
            response.sendStaticResource();  
        }catch(IOException e){  
            e.printStackTrace();  
        }  
          
    }  
  
}  