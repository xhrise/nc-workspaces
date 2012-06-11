package com.ufida.report.rep.applet;
import com.ufida.iufo.pub.tools.AppDebug;

import java.net.URL;

import nc.bs.framework.ServiceConfig;

import com.ufsoft.iuforeport.tableinput.applet.ITableInputAppletParam;

/**
 * 创建日期： 2007-1-10 10:29:14
 * @author  chxiaowei 
 * @version 1.0
 * 
 * BI报表由WEB切换到报表工具查看的辅助工具类
 */
public class BILinkServletUtil {
	private final static String OPER_SERVLET_NAME = "com.ufida.report.rep.applet.BIReportOperServlet"; 
		
	public static Object linkBIReportOperServlet(String repID, String strSessionID){
		Object result = null;
	    java.io.InputStream is = null;
	    try {
	        // 得到URL值
			URL urlAddress = new java.net.URL(ServiceConfig.getBaseHttpURL()+ITableInputAppletParam.SERVICE_IUFO_URL + OPER_SERVLET_NAME);
			// 建立URLConnection 
			java.net.URLConnection urlc = urlAddress.openConnection();
			urlc.setRequestProperty("Cookie", "JSESSIONID=" + strSessionID);
	        urlc.setDoOutput(true);
	        // 建立URLConnection的输出流
	        java.util.zip.GZIPOutputStream gzo =
	            new java.util.zip.GZIPOutputStream(urlc.getOutputStream());
	        java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(gzo);
	        // 写流：	报表ID
	        oos.writeObject(repID);
	       	oos.flush();
	        oos.close(); 
	        gzo.finish();  
	        gzo.flush();
	
	        // 从URLConnection的输入流里获得业务处理Servet的计算结果
	        java.io.ObjectInputStream in = new java.io.ObjectInputStream(urlc.getInputStream());
	        result = in.readObject();
	        in.close();
	    } catch (Exception e) {
	AppDebug.debug(e);//@devTools         e.printStackTrace(System.out);
	    } finally {
	        if (is != null)
	        try {
	            is.close();
	        } catch (Exception e) {
	AppDebug.debug(e);//@devTools             e.printStackTrace(System.out);
	        }
	    }
	    
	    // 返回对象处理
	    if(result == null) {
		    return new Boolean(false);
	    }
	    return result;
	}
}
