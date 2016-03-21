import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import com.http.service.HttpServer;
import com.servlerBean.ServletContentCollection;
import com.until.LoadAnnotationUntil;
import com.until.ReadXml;
import com.until.WarUtils;

public class Main {
	
	public static void main(String[] args) {
		Main main=new Main();
		main.initService();
		HttpServer server = new HttpServer();  
		server.await(); 
	}
	
	/**
	 * 初始化war包
	 * 解压war包
	 * 加载web-xml信息
	 */
	public void initService(){
		ReadXml readXml=new ReadXml();
		File file=new File("webapps");
		String[] fileNames=file.list();
		for (int i = 0; i < fileNames.length; i++) {
			if(fileNames[i].contains(".war")){
				String fileFloder=fileNames[i].substring(0,fileNames[i].indexOf("."));
				try {
					WarUtils.unzip("webapps/"+fileNames[i],"webapps/"+fileFloder);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String path="webapps/"+fileFloder+"/WEB-INF/web.xml";
				readXml.setPath(path);
				ServletContentCollection.servletContentList.put(fileFloder,readXml.read());
				for(String newPath:LoadAnnotationUntil.getClassPath(HttpServer.WEB_ROOT+fileFloder+"//")){
					try {
						System.out.println("开始加载："+newPath);
						LoadAnnotationUntil.LoadClassByClassPath(newPath);
					} catch (Exception e) {
						System.out.println("注解加载错误："+newPath);
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * 让java执行命令
	 * @param order 命令
	 */
	static void runInit(String order){
		Runtime run = Runtime.getRuntime();//返回与当前 Java 应用程序相关的运行时对象  
        try {  
            Process p = run.exec(order);// 启动另一个进程来执行命令  
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());  
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));  
            String lineStr;  
            while ((lineStr = inBr.readLine()) != null)  
                //获得命令执行后在控制台的输出信息  
                System.out.println(lineStr);// 打印输出信息  
            //检查命令是否执行失败。  
            if (p.waitFor() != 0) {  
                if (p.exitValue() == 1)//p.exitValue()==0表示正常结束，1：非正常结束  
                    System.err.println("命令执行失败!");  
            }  
            inBr.close();  
            in.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	}

}