
package com.ufsoft.report.constant;

import java.net.URL;

import com.ufsoft.report.UfoReport;

/**
 * 报表工具运行环境。
 * 记录报表工具运行期间需要的静态数据。
 * @author wupeng
 * 2004-10-13
 */
public class Environment {
	/**
	 * 标记当前产品是否是演示版
	 */
	private static boolean m_bDemo;
//	/**
//	 * 报表工具的实例
//	 */
//	private static UfoReport s_UfoReport;
	
//	/**
//	 * 记录获取资源的Url.
//	 */
//	private static java.net.URL s_sUrl = null;
	
    /**标记是否有HR产品的授权*/
    private static boolean hrReport = true;
    /**标记是否有网络报表产品的授权*/
    private static boolean netReport = true;
    /**
     * 记录用户登陆的时间.
     */
    public static String loginTime = null;
    /**是否是调试状态*/
	public static boolean DEBUG = false;
	/**
	 * 当前产品是否演示版
	 * @return Returns the m_bDemo.
	 * wupeng
	 * 2004-10-13
	 */
	public static boolean isDemo() {
		return m_bDemo;
	}
	/**
	 * 设置当前产品版本类型是否演示版
	 * @param demo The m_bDemo to set.
	 */
	public static void setDemo(boolean demo) {
		m_bDemo = demo;
	}
	
//	/**
//	 * 得到报表实例
//	 * @return UfoReport
//	 */
//	public static UfoReport getReport(){
//		return s_UfoReport;
//	}
//	/**
//	 * 设置报表实例
//	 * @param rpt
//	 */
//	public static void setReport(UfoReport rpt){
//		s_UfoReport = rpt;
//	}
	
//	/**
//	 * 得到服务器Url
//	 * @return java.net.URL
//	 */
//	public static java.net.URL getUrl(){
//		return s_sUrl;
//	}
//	/**
//	 * 设置服务器Url
//	 * @param url
//	 */
//	public static void setUrl(URL url){
//		s_sUrl = url;
//	}

    /**
     * 设置HR授权
     * @param hr
     */
    public static void setHRReport(boolean hr){
        hrReport = hr;
    }

    /**
     * 设置网络报表授权
     * @param net
     */
    public static void setNetReport(boolean net){
        netReport = net;
    }

    /**
     * 是否HR授权
     * @return boolean
     */
    public static boolean isHRReport(){
        return hrReport;
    }

    /**
     * 是否网络报表授权
     * @return boolean
     */
    public static boolean isNetReport(){
        return netReport;
    }
}
