package com.until;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ServletClassLoading extends URLClassLoader {

	private String realPath;

	public ServletClassLoading(URL[] urls,String realPath) {
		super(urls);
		System.out.println(urls[0].getPath());
		this.realPath=realPath;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			Class<?> rtn=super.findClass(name);
			return rtn;
		} catch (ClassNotFoundException e) {
			System.out.println("父类查找失败，启动子类查找");
			// TODO: handle exception
		}
		
		Class clazz = null;
		this.findLoadedClass(name); // 父类已加载
		if (clazz == null) { // 检查该类是否已被加载过
			clazz= getClassData(name); // 根据类的二进制名称,获得该class文件的字节码数组
			if (clazz == null) {
				throw new ClassNotFoundException();
			}
		}
		return clazz;
	}

	private Class<?> getClassData(String name) {
		realPath=realPath.replace("\\WEB-INF\\classes\\","\\WEB-INF\\lib\\");
		File file = new File(realPath);
		System.out.println("*****realPath****"+realPath);
		String[] jarPath = file.list();
		for (int i = 0; i < jarPath.length; i++) {
			try {
				URL url = new URL("file:" + realPath+ jarPath[i]);
				// 使用URL类加载器动态加载jar包
				URLClassLoader myClassLoader = new URLClassLoader(new URL[] { url });
				Class<?> clazz = myClassLoader.loadClass(name);
				return clazz;
			} catch (ClassNotFoundException e) {
				System.out.println("子类查找器开始第"+i+"次查找，失败！");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

}
