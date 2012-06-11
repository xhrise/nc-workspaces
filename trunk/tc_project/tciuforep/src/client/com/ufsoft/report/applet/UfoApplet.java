package com.ufsoft.report.applet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.applet.MainApplet;
import com.ufsoft.report.UIUtilities;
import com.ufsoft.report.UfoReport;

public class UfoApplet extends MainApplet {

	private static final long serialVersionUID = -7280754924290977642L;

	/**
	 * 表格控件对象
	 */
	protected UfoReport m_oUfoReport = null;
	 
	/**
	 * 调试用参数Map
	 */
	private Map<String, String> m_appletDebugParams = null;

	public void init() {
		
	}

	/**
	 * 综合查询切换报表
	 * @author liuyy 
	 * 2007-10-12
	 * @param report 新报表
	 */
	public void setUfoReport(UfoReport report) {
		
		m_oUfoReport = report;
		
		UIUtilities.ufoReport2JRootPane(report, getRootPane());
		

	}

	public UfoReport getUfoReport() {
		return m_oUfoReport;
	}

	/**
	 * 为了便于调试报表录入工具的时候录入参数，重载此方法。     
	 * @param param
	 */
	public String getParameter(String param) {
		if (!isDebug()) {
			return super.getParameter(param);
		} else {
			if (m_appletDebugParams == null) {
				initDebugAppletParams();
			}
			return (String) m_appletDebugParams.get(param);
		}

	}

	/**
	 * 是否是调试状态
	 * @return
	 */
	protected boolean isDebug() {
		return "true".equalsIgnoreCase(super.getParameter("debug"));
	}

	/**
	 * 打印调试信息
	 * @param strDebugInfo
	 */
	protected void debug(String strDebugInfo) {
		//if(isDebug()){
		AppDebug.debug(strDebugInfo);//@devTools System.out.println(strDebugInfo);
		//}
	}

	/**
	 * 从表格录入配置文件里初始化参数
	 *
	 */
	private void initDebugAppletParams() {
		BufferedReader in = null;
		try {
			String FILENAME = "c:\\31debugtableinput.txt";
			in = new BufferedReader(new FileReader(FILENAME));
			String strLine = null;
			m_appletDebugParams = new HashMap<String, String>();
			//第一个等号到第一个空格之间为param，第2个等号到">"之间为value
			while ((strLine = in.readLine()) != null) {
				if (strLine.trim().length() == 0)
					continue;
				int pos1 = strLine.indexOf("=");
				int pos2 = strLine.indexOf(" ", pos1);
				int pos3 = strLine.indexOf("=", pos2);
				int pos4 = strLine.indexOf(">", pos2);
				m_appletDebugParams.put(strLine.substring(pos1 + 1, pos2),
						strLine.substring(pos3 + 1, pos4).trim());
			}
		} catch (Exception e) {
			AppDebug.debug(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
					AppDebug.debug(e1);
				}
			}
		}
	}

}
