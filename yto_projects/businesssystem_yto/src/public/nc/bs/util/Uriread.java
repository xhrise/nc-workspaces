package nc.bs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import nc.bs.framework.common.NCLocator;
import nc.itf.yto.util.IFilePost;

public class Uriread {
	
	private static Properties prop = null;
	private static Map<String, String> propList = null;
	
	// http post 发送地址
	public static String uriPath(){
		
		
//		return "http://localhost:8080/servletT/servlet/JasperTest"; //　本地服务地址
		
//		return "http://192.168.2.146/IPlatformWeb/"; // YTO内网服务地址
		
//		return "http://10.1.200.64:8088/IPlatformWeb/"; // YTO外网服务地址
		
//		return "http://10.1.200.64:8088/IPlatformWebTest"; // YTO测试环境地址
		
//		return "http://116.228.70.241:8088/IPlatformWebTest";
		
		try {
			return NCLocator.getInstance().lookup(IFilePost.class).GetURI();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String loginUriPath(){
		
//		return "http://192.168.0.200:8080/servletT/servlet/JasperTest"; //　本地服务地址
		
//		return "http://192.168.2.146/IPlatformWeb/"; // YTO内网服务地址
		
		
//		return "http://10.1.200.64:8088/IPlatformWeb/"; // YTO外网服务地址
		
//		return "http://10.1.200.64:8088/IPlatformWebTest"; // YTO测试环境地址
		
//		return "http://116.228.70.241:8088/IPlatformWebTest";
		
		try {
			return NCLocator.getInstance().lookup(IFilePost.class).GetURI();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static String setProp( String key , String value , String filePath) throws Exception {
		File myFilePath = null;
		
		try{
			myFilePath = new File(filePath.substring(0 , filePath.lastIndexOf("\\")));
		}catch(Exception ex){
			myFilePath = new File(filePath.substring(0 , filePath.lastIndexOf("/")));
		}
		
		if (!(myFilePath.exists())) {
			myFilePath.mkdirs();
		}
		
		
		File file = new File(filePath);
		FileOutputStream fos = null;
		InputStream is = null;
		if (!file.exists()) {
			file.createNewFile();
		}

		is = new FileInputStream(filePath);
		prop = new Properties();
		prop.load(is);
		is.close();

		//if (prop.keySet().size() == 0) {
			fos = new FileOutputStream(filePath);
			prop.setProperty(key, value);
			prop.store(fos, " ");
			fos.close();
		//}

			
		return value;
	}
	
	private static String getPropByProperties(String key , String filePath) throws Exception {
		try {
		InputStream is = new FileInputStream(filePath);
		prop = new Properties();
		prop.load(is);
		is.close();

		//propList = new HashMap<String, String>();
		if(prop.containsKey(key))
			return prop.getProperty(key);
		} catch(Exception e){}
		
		return null;
	}
}
