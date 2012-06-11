/**
 * AbstractExportAction.java  5.0 
 * 
 * WebDeveloper自动生成.
 * 2006-01-18
 */
package nc.ui.iufo.dataexchange.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import nc.imp.tc.imp.QueryList;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.query.returnquery.ErrorRefreshAction;
import nc.ui.iufo.query.returnquery.ReportCommitAction;
import nc.ui.iufo.query.returnquery.ReportCommitBO_Client;
import nc.util.iufo.pub.IDMaker;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.query.returnquery.ReportCommitVO;
import nc.vo.iufo.task.TaskVO;
import nc.vo.iuforeport.rep.ReportVO;
import nc.vo.jcom.xml.XMLUtil;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.encoding.ser.JAFDataHandlerDeserializerFactory;
import org.apache.axis.encoding.ser.JAFDataHandlerSerializerFactory;
import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.WebException;
import com.ufida.web.action.Action;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.action.DownloadForward;
import com.ufida.web.action.ErrorForward;
import com.ufida.web.util.WebGlobalValue;
import com.ufsoft.iufo.querycond.ui.QuerySimpleForm;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.sysprop.ui.ISysProp;
import com.ufsoft.iufo.sysprop.ui.SysPropMng;
import com.ufsoft.iufo.web.DialogAction;
import com.ufsoft.iufo.web.IUFOAction;

/**
 * 作为所有的导出界面的基类 CaiJie 2006-01-18
 */
public abstract class AbstractExportNewAction extends DialogAction implements
		Serializable {

	public static Hashtable<String, String> m_hashSessionID = new Hashtable<String, String>();

	public static Hashtable<String, Boolean> m_hashExportID = new Hashtable<String, Boolean>();

	/**
	 * <MethodDescription> CaiJie 2006-01-18
	 */
	public ActionForward execute(ActionForm actionForm) throws WebException {
//		是否审核结果不通过不允许上报
		String strNotMustCheckPass = SysPropMng.getSysProp(ISysProp.SENDUP_CHECK).getValue();
		boolean bMustCheckPass=strNotMustCheckPass != null && strNotMustCheckPass.equals("false");
		
		//从界面上得到用户选择的上报信息及出错信息
		Vector<ReportCommitVO> vCommitVO=new Vector<ReportCommitVO>();
		Vector<String> vBalErrMsg=new Vector<String>();
		Vector<String> vCommitErrMsg=new Vector<String>();
		ActionForward fwd;
		try {
			fwd = new ReportCommitAction().loadCommitFromUI(vCommitVO,vBalErrMsg,vCommitErrMsg,new Hashtable<String,MeasurePubDataVO>(),bMustCheckPass,true);
			if (fwd!=null)
				return fwd;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
		if (vCommitErrMsg.size()>0){
			StringBuffer bufErrMsg=new StringBuffer();
			bufErrMsg.append(StringResource.getStringResource("miufotasknew00108")+"<BR>");
			for (int i=0;i<vCommitErrMsg.size();i++)
				bufErrMsg.append("&nbsp;&nbsp;&nbsp;&nbsp;("+(i+1)+")"+vCommitErrMsg.get(i)+"<BR>");
			vBalErrMsg.insertElementAt(bufErrMsg.toString(), 0);
			
			fwd=new ActionForward(ErrorRefreshAction.class.getName(),"");
			addRequestObject(ErrorRefreshAction.REQ_PARAM_ERROR,vBalErrMsg.toArray(new String[0]));
			fwd.addParameter(ErrorRefreshAction.PARAM_ERR_TYPE,CloseForward.CLOSE_REFRESH_MAIN);
			return fwd;
		}
		
		//从界面上得到的上报信息没有上报、请求标志信息，从数据库中读取记录将这些信息补全
		TaskVO taskVO=IUFOUICacheManager.getSingleton().getTaskCache().getTaskVO(getCurTaskId());
		String strTimeProp=IUFOUICacheManager.getSingleton().getKeyGroupCache().getByPK(taskVO.getKeyGroupId()).getTimeProp();
		
		ReportCommitVO[] commitVOs=vCommitVO.toArray(new ReportCommitVO[0]);
		Vector<ReportCommitVO> vExportCommit=new Vector<ReportCommitVO>();
		try {
			commitVOs=new ReportCommitAction().adjustCommit(commitVOs,strTimeProp,taskVO,vExportCommit);
//			数据库操作
			ReportCommitBO_Client.commit(commitVOs);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		nc.ui.iufo.dataexchange.base.AbstractExportForm form = (nc.ui.iufo.dataexchange.base.AbstractExportForm) actionForm;
		try {

			form.setDlgTitle(getTitle(this));
			form.setLabelHint(getHint());
			String strWebPath = getFileUrlPath(this) + File.separator
					+ innerCreateFile();
			form.setFileUrlPath(strWebPath);
			form.setHost(this.getRequestScheme() + "://" + this.getServerName()
					+ ":" + getServerPort());

		} catch (Exception e) {
			AppDebug.debug(e);
			return new ErrorForward(e.getMessage());
		}

		if (getExportUIClass().equals(UFOPrintUI.class.getName()))
			return new ActionForward(getExportUIClass());
		else
			return new DownloadForward((form.getFileUrlPath()).replace(
					File.separatorChar, "/".charAt(0)), form.getLabelHint());
	}

	/**
	 * 发送文件到NC502的客户端方法 add by yh for 2011年3月14日16:48:57
	 * 
	 * @param filename
	 * @throws ServiceException
	 * @throws RemoteException
	 * @throws Exception
	 */
	private void UploadFileClient(String filename, String date)
			throws ServiceException, RemoteException, Exception {

		String ncFileName = "Ufida_IUFO/service502ip";
		String xmlfile = "Ufida_IUFO/service502ip/ip.xml";
		org.w3c.dom.NodeList ip = null;
		org.w3c.dom.Document document = null;
		java.io.File xmlList = new java.io.File(xmlfile);

		File myFilePath = new File(ncFileName);
		if (!(myFilePath.exists())) {
			myFilePath.mkdirs();
		}

		// 如果xmlList.xml不存在则创建
		Boolean bl = xmlList.exists();
		if (!bl) {

			try {
				document = XMLUtil.newDocument();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			org.w3c.dom.Element element_ip = document.createElement("ip");
			element_ip
					.setTextContent("在此处设置WEBSERVICE服务器的IP地址，例如:127.0.0.1:80");
			document.appendChild(element_ip);
			this.toSave(document, xmlList.getPath());
		} else {
			try {
				document = XMLUtil.getDocumentBuilder().parse(xmlList);
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ip = document.getElementsByTagName("ip");
			ip.item(0).getTextContent();
		}

		String url = "http://" + ip.item(0).getTextContent()
				+ "/axis/UploadFileService.jws?wsdl";
		System.out.println("WEBSERVICE的地址：" + url);

		
		String path = filename; //filename;
		System.out.println(path);

		// 这样就相当于构造了一个带文件路径的File了
		String result = null;
		try {
			DataHandler handler = new DataHandler(new FileDataSource(path));
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(url);

			/**
			 * 注册异常类信息和序列化类 ns:FileUploadHandler 和 wsdd
			 * 配置文件中的typeMapping中的xmlns:hns="ns:FileUploadHandler" 的对应
			 * DataHandler 和 wsdd
			 * 配置文件中的typeMapping中的qname="hns:DataHandler"的DataHandler对应
			 */
			QName qn = new QName("ns:FileUploadHandler", "DataHandler");
			call.registerTypeMapping(DataHandler.class, qn,
					JAFDataHandlerSerializerFactory.class,
					JAFDataHandlerDeserializerFactory.class);
			call.setOperationName(new QName(url, "upload"));

			// 设置方法形参，注意的是参数1的type的DataHandler类型的，和上面的qn的类型是一样的
			call.addParameter("handler", qn, ParameterMode.IN);
			call.addParameter("fileName", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("date", XMLType.XSD_STRING, ParameterMode.IN);
			// 设置返回值类型，下面2种方法都可以
			call.setReturnClass(String.class);
			// call.setReturnType(XMLType.XSD_STRING);
			
			// 修改文件名称，进行BASE64加密传送
			sun.misc.BASE64Encoder encode = new sun.misc.BASE64Encoder();
			filename = encode.encode(filename.substring(filename.lastIndexOf("\\") + 1).getBytes());
			
			result = (String) call.invoke(new Object[] { handler,
					filename, date });
			// System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean checkUploadClient(String strCode, String date,
			String unit_code) {
		String ncFileName = "Ufida_IUFO/service502ip";
		String xmlfile = "Ufida_IUFO/service502ip/ip.xml";
		org.w3c.dom.NodeList ip = null;
		org.w3c.dom.Document document = null;
		java.io.File xmlList = new java.io.File(xmlfile);

		File myFilePath = new File(ncFileName);
		if (!(myFilePath.exists())) {
			myFilePath.mkdirs();
		}

		// 如果xmlList.xml不存在则创建
		Boolean bl = xmlList.exists();
		if (!bl) {

			try {
				document = XMLUtil.newDocument();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			org.w3c.dom.Element element_ip = document.createElement("ip");
			element_ip
					.setTextContent("在此处设置WEBSERVICE服务器的IP地址，例如:127.0.0.1:80");
			document.appendChild(element_ip);
			this.toSave(document, xmlList.getPath());
		} else {
			try {
				document = XMLUtil.getDocumentBuilder().parse(xmlList);
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ip = document.getElementsByTagName("ip");
			ip.item(0).getTextContent();
		}

		String url = "http://" + ip.item(0).getTextContent()
				+ "/axis/CancleCommitService.jws?wsdl";
		System.out.println("WEBSERVICE的地址：" + url);

		// 这样就相当于构造了一个带文件路径的File了
		boolean result = true;
		try {

			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(url);

			/**
			 * 注册异常类信息和序列化类 ns:FileUploadHandler 和 wsdd
			 * 配置文件中的typeMapping中的xmlns:hns="ns:FileUploadHandler" 的对应
			 * DataHandler 和 wsdd
			 * 配置文件中的typeMapping中的qname="hns:DataHandler"的DataHandler对应
			 */
			QName qn = new QName("ns:FileUploadHandler", "DataHandler");
			call.registerTypeMapping(DataHandler.class, qn,
					JAFDataHandlerSerializerFactory.class,
					JAFDataHandlerDeserializerFactory.class);
			call.setOperationName(new QName(url, "checkUpload"));

			// 设置方法形参，注意的是参数1的type的DataHandler类型的，和上面的qn的类型是一样的
			call.addParameter("strCode", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("date", XMLType.XSD_STRING, ParameterMode.IN);
			call
					.addParameter("unit_code", XMLType.XSD_STRING,
							ParameterMode.IN);
			// 设置返回值类型，下面2种方法都可以
			call.setReturnClass(Boolean.class);
			// call.setReturnType(XMLType.XSD_STRING);
			result = Boolean.parseBoolean(call.invoke(
					new Object[] { strCode, date, unit_code }).toString());
			// System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public void toSave(org.w3c.dom.Document document, String filename) {
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			PrintWriter pw = new PrintWriter(new FileOutputStream(filename));
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
		} catch (TransformerException mye) {
			mye.printStackTrace();
		} catch (IOException exp) {
			exp.printStackTrace();
		}
	}

	protected String innerCreateFile() throws Exception {
		String strFileName = createFile(this);
		ReportVO report = IUFOUICacheManager.getSingleton().getReportCache()
				.getByPK(getTableSelectedID().split("@")[1]);
		String RepID = report.getReportPK();
		String strCode = report.getCode(); 

		Calendar date = Calendar.getInstance();
		String strDate = ""
				+ (date.get(Calendar.HOUR_OF_DAY) >= 10 ? date
						.get(Calendar.HOUR_OF_DAY) : "0"
						+ date.get(Calendar.HOUR_OF_DAY))
				+ (date.get(Calendar.MINUTE) >= 10 ? date.get(Calendar.MINUTE)
						: "0" + date.get(Calendar.MINUTE))
				+ (date.get(Calendar.SECOND) >= 10 ? date.get(Calendar.SECOND)
						: "0" + date.get(Calendar.SECOND));
		String strNewFileName = strDate + "_" + strFileName;

		System.out.println(getFileServPath(this) + File.separator
				+ strNewFileName);

		// 添加上报文件复制到C:/Ufida_IUFO/shangbao/目录下的功能，并上报到接收服务器
		// modify by yh for 2011年3月10日16:12:35
		QuerySimpleForm form = (QuerySimpleForm) getSessionObject("query_simple_cond_nc.ui.iufo.query.tablequery.TableQuerySimpleAction@"
				+ getCurTaskId());
		String UITime = "";
		if(form == null)
			UITime = this.getCurLoginDate().substring(0 , 7);
		else
			UITime = form.getQueryDate().substring(0 , 7);
		
		String ncFileName = "Ufida_IUFO\\shangbao\\" + UITime
				+ "\\";
		// String jieshouFileName = "C:/Ufida_IUFO/shangbaojieshou/";
		File myFilePath = new File(ncFileName);
		if (!(myFilePath.exists())) {
			myFilePath.mkdirs();
		}

		FileInputStream fis = new FileInputStream(getFileServPath(this)
				+ File.separator + strFileName);

		FileOutputStream fos = new FileOutputStream(ncFileName + strCode
				+ ".xls");
		byte[] buff = new byte[10240];
		int readed = -1;
		while ((readed = fis.read(buff)) > 0)
			fos.write(buff, 0, readed);
		fis.close();
		fos.close();

		String filename = ncFileName + strCode + ".xls";
		
		// 直接验证公司为19901
		// modify by river for 2012-06-09
		String Unit_code =  "19901"; // new QueryList().getUnit_code(this.getTreeSelectedID());

		if (this.checkUploadClient(strCode, UITime, Unit_code))
			this.UploadFileClient(filename, UITime);
		else {
			// JOptionPane.showMessageDialog(null,
			// "您上报的报表在集团中已经存在并且为上报状态，请先进行报表的取消上报操作!", "报表上报提示",
			// JOptionPane.INFORMATION_MESSAGE);
			try {
				 QueryList queryList = new QueryList();
					String content = "您上报的报表 [ "+strCode+" ] 在集团中已经存在并且为上报状态，请先进行报表的取消上报操作!";
					String title = "报表上报提示" ;
					String randomVal = queryList.GenPk();
					queryList.insertReleaseinfo(content, title, randomVal);

					java.util.List<String> userList = queryList.getUserId();
					for (String userId : userList) {
						queryList.insertReleasetarget(randomVal, userId);
					}
			} catch (Exception ex) {
				AppDebug.debug(ex);
			}
		}

		File file = new File(getFileServPath(this) + File.separator
				+ strFileName);
		FileUtils.copyFile(file, new File(getFileServPath(this)
				+ File.separator + strNewFileName));
		// modify by wangyga 2008-8-6 删除服务器的临时文件
		file.delete();
		// file.deleteOnExit();此方法不能及时清除临时文件

		return strNewFileName;

	}

	/**
	 * Form值校验
	 * 
	 * @param actionForm
	 * @return 值校验失败的提示信息集合
	 */
	public String[] validate(ActionForm actionForm) {
		return null;

	}

	/**
	 * 关联Form
	 * 
	 */
	public String getFormName() {
		return nc.ui.iufo.dataexchange.base.AbstractExportForm.class.getName();
	}

	/**
	 * 生成下载的文件。 创建时间：(2002-03-29 17:36:28)
	 * 
	 * @return java.lang.String 不包含路径的文件的名称
	 * @param request
	 *            javax.servlet.http.HttpServletRequest
	 */
	protected abstract String createFile(IUFOAction action);

	/**
	 * 得到文件在服务器端的保存路径。 创建日期：(2002-04-19 15:28:45)
	 * 
	 * @return java.lang.String
	 * @param request
	 *            javax.servlet.http.HttpServletRequest
	 */
	public String getFileServPath(Action action) {
		StringBuffer sbuf = new StringBuffer();
		// 得到服务器端绝对路径。
		// sbuf.append(System.getProperty(IDataExchange.TempFilePath));

		String sbufs = WebGlobalValue.WORK_DIR;// System.getProperty(GlobalValue.CONTEXTKEY);
		sbufs = sbufs.substring(0, sbufs.length() - 1);
		sbuf.append(sbufs);

		// System.out.println(sbufs);
		// sbuf.append(IDataExchange.IUFO_PATH);

		// sbuf.append(System.getProperty(GlobalValue.CONTEXTKEY));
		// 创建一个临时目录。
		sbuf.append(getSubPath());
		sbuf.append(File.separator);
		sbuf.append(getExportID(action.getSessionId()));
		String strPath = sbuf.toString();
		// 检查路径是否存在.
		File file = new File(strPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return strPath;
	}

	/**
	 * 得到下载文件的连接地址的路径。 创建时间：(2002-03-29 13:51:35)
	 * 
	 * @return String 文件路径的Url，不为Null
	 * @param request
	 *            javax.servlet.http.HttpServletRequest
	 */
	public String getFileUrlPath(Action action) {
		StringBuffer strPath = new StringBuffer();
		// strPath.append(IDataExchange.IUFO_PATH);
		strPath.append(getSubPath());
		strPath.append(File.separator);
		strPath.append(getExportID(action.getSessionId()));
		return strPath.toString();
	}

	/**
	 * 下载的连接提示信息。 创建日期：(2002-06-12 13:41:48)
	 * 
	 * @return java.lang.String
	 */
	protected String getHint() {
		String strHint = StringResource.getStringResource("miufo1002419"); // "点击鼠标右键另存为"
		return strHint + "...";
	}

	/**
	 * 得到下载文件的连接地址的路径中的子路径。 创建日期：(2002-04-19 15:23:29)
	 * 
	 * @return java.lang.String
	 */
	public String getSubPath() {
		return IDataExchange.PACKAGE_PATH;
	}

	/**
	 * 设置浏览器标题。 创建日期：(2002-04-22 09:59:02)
	 * 
	 * @return java.lang.String
	 * @param request
	 *            javax.servlet.http.HttpServletRequest
	 */
	public abstract String getTitle(Action action);

	/**
	 * 根据sessionid产生可以用于IE打开的唯一ID值，解决Weblogic下无法直接打开导出的EXCEL文件问题
	 * 
	 * @param strSessionID
	 *            String
	 * @return String
	 */
	public static synchronized String getExportID(String strSessionID) {
		if (strSessionID == null)
			return strSessionID;

		String strExportID = (String) m_hashSessionID.get(strSessionID);
		if (strExportID != null)
			return strExportID;

		int iCount = 0;
		while (iCount < 40) {
			strExportID = IDMaker.makeID(30);
			if (m_hashExportID.get(strExportID) == null) {
				m_hashSessionID.put(strSessionID, strExportID);
				m_hashExportID.put(strExportID, new Boolean(true));
				return strExportID;
			}
			iCount++;
		}
		return strExportID;
	}

	protected String getExportUIClass() {
		return AbstractExportDlg.class.getName();
	}
}

/**
 * @WebDeveloper <?xml version="1.0" encoding='gb2312'?> <ActionVO
 *               Description="作为所有的导出界面的基类" name="AbstractExportAction"
 *               package="nc.ui.iufo.dataexchange"
 *               关联Form="nc.ui.iufo.dataexchange.AbstractExportForm"> <MethodsVO
 *               execute=""> </MethodsVO> </ActionVO>
 * @WebDeveloper
 */
