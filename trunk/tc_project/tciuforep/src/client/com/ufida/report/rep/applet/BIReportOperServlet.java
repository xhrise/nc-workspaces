package com.ufida.report.rep.applet;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServlet;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.rep.model.BaseReportModel;

/**
 * �������ڣ� 2007-1-10 10:29:14
 * @author  chxiaowei 
 * @version 1.0
 * 
 * BI������WEB�л��������߲鿴ʱִ�еķ�����
 */
public class BIReportOperServlet extends HttpServlet {
	
	/**
	 * Process incoming HTTP POST requests
	 * @param request
	 *            Object that encapsulates the request to the servlet
	 * @param response
	 *            Object that encapsulates the response from the servlet
	 */
	public void doGet(javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)	
			throws javax.servlet.ServletException, java.io.IOException {

		performTask(request, response);

	}

	/**
	 * Process incoming HTTP POST requests
	 * @param request
	 *            Object that encapsulates the request to the servlet
	 * @param response
	 *            Object that encapsulates the response from the servlet
	 */
	public void doPost(javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws javax.servlet.ServletException, java.io.IOException {

		performTask(request, response);
		
	}
	
	/**
	 * ����BI������WEB����л���Appletʱ�Ĳ�������
	 * @param request
	 * @param response
	 */
	private void performTask(javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) {
		try {
		    ObjectInputStream ois =
				new ObjectInputStream(new GZIPInputStream(request.getInputStream()));

		    String repID = (String) ois.readObject();
			AppDebug.debug("[debug]: BIReportOperServlet repID = " + repID);
			
			BaseReportModel model = null;
			model = (BaseReportModel)request.getSession().getAttribute(repID);
			
			ObjectOutputStream objectoutputstream = new 
				ObjectOutputStream(response.getOutputStream());
			objectoutputstream.writeObject(model);
			
			objectoutputstream.flush();
	        objectoutputstream.close();
		} catch(Throwable e) {
			AppDebug.debug(e);//@devTools e.printStackTrace(System.out);
		}
	}
}
