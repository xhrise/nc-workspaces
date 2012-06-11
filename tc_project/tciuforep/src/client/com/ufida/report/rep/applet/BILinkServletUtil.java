package com.ufida.report.rep.applet;
import com.ufida.iufo.pub.tools.AppDebug;

import java.net.URL;

import nc.bs.framework.ServiceConfig;

import com.ufsoft.iuforeport.tableinput.applet.ITableInputAppletParam;

/**
 * �������ڣ� 2007-1-10 10:29:14
 * @author  chxiaowei 
 * @version 1.0
 * 
 * BI������WEB�л��������߲鿴�ĸ���������
 */
public class BILinkServletUtil {
	private final static String OPER_SERVLET_NAME = "com.ufida.report.rep.applet.BIReportOperServlet"; 
		
	public static Object linkBIReportOperServlet(String repID, String strSessionID){
		Object result = null;
	    java.io.InputStream is = null;
	    try {
	        // �õ�URLֵ
			URL urlAddress = new java.net.URL(ServiceConfig.getBaseHttpURL()+ITableInputAppletParam.SERVICE_IUFO_URL + OPER_SERVLET_NAME);
			// ����URLConnection 
			java.net.URLConnection urlc = urlAddress.openConnection();
			urlc.setRequestProperty("Cookie", "JSESSIONID=" + strSessionID);
	        urlc.setDoOutput(true);
	        // ����URLConnection�������
	        java.util.zip.GZIPOutputStream gzo =
	            new java.util.zip.GZIPOutputStream(urlc.getOutputStream());
	        java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(gzo);
	        // д����	����ID
	        oos.writeObject(repID);
	       	oos.flush();
	        oos.close(); 
	        gzo.finish();  
	        gzo.flush();
	
	        // ��URLConnection������������ҵ����Servet�ļ�����
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
	    
	    // ���ض�����
	    if(result == null) {
		    return new Boolean(false);
	    }
	    return result;
	}
}
