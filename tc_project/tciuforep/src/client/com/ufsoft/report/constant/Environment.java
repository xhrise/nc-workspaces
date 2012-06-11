
package com.ufsoft.report.constant;

import java.net.URL;

import com.ufsoft.report.UfoReport;

/**
 * ���������л�����
 * ��¼�����������ڼ���Ҫ�ľ�̬���ݡ�
 * @author wupeng
 * 2004-10-13
 */
public class Environment {
	/**
	 * ��ǵ�ǰ��Ʒ�Ƿ�����ʾ��
	 */
	private static boolean m_bDemo;
//	/**
//	 * �����ߵ�ʵ��
//	 */
//	private static UfoReport s_UfoReport;
	
//	/**
//	 * ��¼��ȡ��Դ��Url.
//	 */
//	private static java.net.URL s_sUrl = null;
	
    /**����Ƿ���HR��Ʒ����Ȩ*/
    private static boolean hrReport = true;
    /**����Ƿ������籨���Ʒ����Ȩ*/
    private static boolean netReport = true;
    /**
     * ��¼�û���½��ʱ��.
     */
    public static String loginTime = null;
    /**�Ƿ��ǵ���״̬*/
	public static boolean DEBUG = false;
	/**
	 * ��ǰ��Ʒ�Ƿ���ʾ��
	 * @return Returns the m_bDemo.
	 * wupeng
	 * 2004-10-13
	 */
	public static boolean isDemo() {
		return m_bDemo;
	}
	/**
	 * ���õ�ǰ��Ʒ�汾�����Ƿ���ʾ��
	 * @param demo The m_bDemo to set.
	 */
	public static void setDemo(boolean demo) {
		m_bDemo = demo;
	}
	
//	/**
//	 * �õ�����ʵ��
//	 * @return UfoReport
//	 */
//	public static UfoReport getReport(){
//		return s_UfoReport;
//	}
//	/**
//	 * ���ñ���ʵ��
//	 * @param rpt
//	 */
//	public static void setReport(UfoReport rpt){
//		s_UfoReport = rpt;
//	}
	
//	/**
//	 * �õ�������Url
//	 * @return java.net.URL
//	 */
//	public static java.net.URL getUrl(){
//		return s_sUrl;
//	}
//	/**
//	 * ���÷�����Url
//	 * @param url
//	 */
//	public static void setUrl(URL url){
//		s_sUrl = url;
//	}

    /**
     * ����HR��Ȩ
     * @param hr
     */
    public static void setHRReport(boolean hr){
        hrReport = hr;
    }

    /**
     * �������籨����Ȩ
     * @param net
     */
    public static void setNetReport(boolean net){
        netReport = net;
    }

    /**
     * �Ƿ�HR��Ȩ
     * @return boolean
     */
    public static boolean isHRReport(){
        return hrReport;
    }

    /**
     * �Ƿ����籨����Ȩ
     * @return boolean
     */
    public static boolean isNetReport(){
        return netReport;
    }
}
