package com.test;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class TestClassLoader {
	public static void main(String[] args) {

		String path = "C:/Users/wb-limeng.g/Desktop/aaaa/webapps/SuperMarket/WEB-INF/lib";
		String name = "oracle.jdbc.connector.OracleLocalTransaction";
		File file = new File(path);
		String[] jarPath = file.list();
		for (int i = 0; i < jarPath.length; i++) {
			try {
				URL url = new URL("file:" + path +"/"+ jarPath[i]);
				// 使用URL类加载器动态加载jar包
				URLClassLoader myClassLoader = new URLClassLoader(new URL[] { url });
				Class<?> clazz = myClassLoader.loadClass(name);
				break;
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
