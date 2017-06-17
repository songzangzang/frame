package org.smart4j.framework.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


import org.apache.log4j.Logger;

/**
 * 类加载器
 * @author Administrator
 *
 */
public class ClassUtil {

	private static final Logger LOGGER = Logger.getLogger(ClassUtil.class);
	
	/**
	 * 获取类加载器
	 */
	public static ClassLoader getClassLoader(){
		return Thread.currentThread().getContextClassLoader();
	}
	
	/**
	 * 加载类
	 */
	public static Class<?> loadClass(String className, boolean isInitialized){
		Class<?> cls;
		try {
			cls = Class.forName(className, isInitialized, getClassLoader());

		} catch (ClassNotFoundException e) {
			LOGGER.error("加载类失败-load class failure", e);
			throw new RuntimeException();
		}
		
		return cls;
	}
	
	/**
	 * 获取指定包下的所有类
	 */
	public static Set<Class<?>> getClassSet(String packageName){
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		try {
			Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
			while(urls.hasMoreElements()){
				URL url = urls.nextElement();
				String protocol = url.getProtocol();
				if (protocol.equals("file")) {
					String packagePath = url.getPath().replaceAll("%20", "");
					addClass(classSet, packagePath, packageName);
				}else if (protocol.equals("jar")) {
					JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
					if (jarURLConnection != null) {
						JarFile jarFile = jarURLConnection.getJarFile();
						Enumeration<JarEntry> jarEntires = jarFile.entries();
						while (jarEntires.hasMoreElements()){
							JarEntry jarEntry = jarEntires.nextElement();
							String jarEntryName = jarEntry.getName();
							if (jarEntryName.endsWith(".class")) {
								String className = jarEntryName.substring(0, jarEntryName.lastIndexOf("."))
										.replaceAll("/", ".");
								doAddClass(classSet, className);
							}
						}
					}
				}
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return classSet;
	}
	
	private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName){
		File[] files = new File(packagePath).listFiles(new FileFilter() {
			public boolean accept(File file) {
				return (file.isFile() && file.getName().endsWith(".class") || file.isDirectory());
			}
		});
		
		for(File file : files){
			String fileName = file.getName();
			if (file.isFile()) {
				String className = fileName.substring(0, fileName.lastIndexOf("."));
				if (StringUtil.isNotEmpty(packageName)) {
					className = packageName + "." + className;
				}
				doAddClass(classSet, className);
			}else{
				String subPackagePath = fileName;
				if (StringUtil.isNotEmpty(packagePath)) {
					subPackagePath = packagePath + "/" + subPackagePath;
				}
				String subPackageName = fileName;
				if (StringUtil.isNotEmpty(packageName)) {
					subPackageName = packageName + "." + subPackageName;
				}
				addClass(classSet, subPackagePath, subPackageName);
			}
		}
	}
	
	private static void doAddClass(Set<Class<?>> classSet, String className){
		Class<?> cls = loadClass(className, false);
		classSet.add(cls);
	}
	
	
}
