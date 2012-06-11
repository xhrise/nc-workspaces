/**
 * AbstractExportAction.java  5.0 
 * 
 * WebDeveloper�Զ�����.
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
 * ��Ϊ���еĵ�������Ļ��� CaiJie 2006-01-18
 */
public abstract class AbstractExportNewAction extends DialogAction implements
		Serializable {

	public static Hashtable<String, String> m_hashSessionID = new Hashtable<String, String>();

	public static Hashtable<String, Boolean> m_hashExportID = new Hashtable<String, Boolean>();

	/**
	 * <MethodDescription> CaiJie 2006-01-18
	 */
	public ActionForward execute(ActionForm actionForm) throws WebException {
//		�Ƿ���˽����ͨ���������ϱ�
		String strNotMustCheckPass = SysPropMng.getSysProp(ISysProp.SENDUP_CHECK).getValue();
		boolean bMustCheckPass=strNotMustCheckPass != null && strNotMustCheckPass.equals("false");
		
		//�ӽ����ϵõ��û�ѡ����ϱ���Ϣ��������Ϣ
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
		
		//�ӽ����ϵõ����ϱ���Ϣû���ϱ��������־��Ϣ�������ݿ��ж�ȡ��¼����Щ��Ϣ��ȫ
		TaskVO taskVO=IUFOUICacheManager.getSingleton().getTaskCache().getTaskVO(getCurTaskId());
		String strTimeProp=IUFOUICacheManager.getSingleton().getKeyGroupCache().getByPK(taskVO.getKeyGroupId()).getTimeProp();
		
		ReportCommitVO[] commitVOs=vCommitVO.toArray(new ReportCommitVO[0]);
		Vector<ReportCommitVO> vExportCommit=new Vector<ReportCommitVO>();
		try {
			commitVOs=new ReportCommitAction().adjustCommit(commitVOs,strTimeProp,taskVO,vExportCommit);
//			���ݿ����
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
	 * �����ļ���NC502�Ŀͻ��˷��� add by yh for 2011��3��14��16:48:57
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

		// ���xmlList.xml�������򴴽�
		Boolean bl = xmlList.exists();
		if (!bl) {

			try {
				document = XMLUtil.newDocument();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			org.w3c.dom.Element element_ip = document.createElement("ip");
			element_ip
					.setTextContent("�ڴ˴�����WEBSERVICE��������IP��ַ������:127.0.0.1:80");
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
		System.out.println("WEBSERVICE�ĵ�ַ��" + url);

		
		String path = filename; //filename;
		System.out.println(path);

		// �������൱�ڹ�����һ�����ļ�·����File��
		String result = null;
		try {
			DataHandler handler = new DataHandler(new FileDataSource(path));
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(url);

			/**
			 * ע���쳣����Ϣ�����л��� ns:FileUploadHandler �� wsdd
			 * �����ļ��е�typeMapping�е�xmlns:hns="ns:FileUploadHandler" �Ķ�Ӧ
			 * DataHandler �� wsdd
			 * �����ļ��е�typeMapping�е�qname="hns:DataHandler"��DataHandler��Ӧ
			 */
			QName qn = new QName("ns:FileUploadHandler", "DataHandler");
			call.registerTypeMapping(DataHandler.class, qn,
					JAFDataHandlerSerializerFactory.class,
					JAFDataHandlerDeserializerFactory.class);
			call.setOperationName(new QName(url, "upload"));

			// ���÷����βΣ�ע����ǲ���1��type��DataHandler���͵ģ��������qn��������һ����
			call.addParameter("handler", qn, ParameterMode.IN);
			call.addParameter("fileName", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("date", XMLType.XSD_STRING, ParameterMode.IN);
			// ���÷���ֵ���ͣ�����2�ַ���������
			call.setReturnClass(String.class);
			// call.setReturnType(XMLType.XSD_STRING);
			
			// �޸��ļ����ƣ�����BASE64���ܴ���
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

		// ���xmlList.xml�������򴴽�
		Boolean bl = xmlList.exists();
		if (!bl) {

			try {
				document = XMLUtil.newDocument();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			org.w3c.dom.Element element_ip = document.createElement("ip");
			element_ip
					.setTextContent("�ڴ˴�����WEBSERVICE��������IP��ַ������:127.0.0.1:80");
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
		System.out.println("WEBSERVICE�ĵ�ַ��" + url);

		// �������൱�ڹ�����һ�����ļ�·����File��
		boolean result = true;
		try {

			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(url);

			/**
			 * ע���쳣����Ϣ�����л��� ns:FileUploadHandler �� wsdd
			 * �����ļ��е�typeMapping�е�xmlns:hns="ns:FileUploadHandler" �Ķ�Ӧ
			 * DataHandler �� wsdd
			 * �����ļ��е�typeMapping�е�qname="hns:DataHandler"��DataHandler��Ӧ
			 */
			QName qn = new QName("ns:FileUploadHandler", "DataHandler");
			call.registerTypeMapping(DataHandler.class, qn,
					JAFDataHandlerSerializerFactory.class,
					JAFDataHandlerDeserializerFactory.class);
			call.setOperationName(new QName(url, "checkUpload"));

			// ���÷����βΣ�ע����ǲ���1��type��DataHandler���͵ģ��������qn��������һ����
			call.addParameter("strCode", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("date", XMLType.XSD_STRING, ParameterMode.IN);
			call
					.addParameter("unit_code", XMLType.XSD_STRING,
							ParameterMode.IN);
			// ���÷���ֵ���ͣ�����2�ַ���������
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

		// ����ϱ��ļ����Ƶ�C:/Ufida_IUFO/shangbao/Ŀ¼�µĹ��ܣ����ϱ������շ�����
		// modify by yh for 2011��3��10��16:12:35
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
		
		// ֱ����֤��˾Ϊ19901
		// modify by river for 2012-06-09
		String Unit_code =  "19901"; // new QueryList().getUnit_code(this.getTreeSelectedID());

		if (this.checkUploadClient(strCode, UITime, Unit_code))
			this.UploadFileClient(filename, UITime);
		else {
			// JOptionPane.showMessageDialog(null,
			// "���ϱ��ı����ڼ������Ѿ����ڲ���Ϊ�ϱ�״̬�����Ƚ��б����ȡ���ϱ�����!", "�����ϱ���ʾ",
			// JOptionPane.INFORMATION_MESSAGE);
			try {
				 QueryList queryList = new QueryList();
					String content = "���ϱ��ı��� [ "+strCode+" ] �ڼ������Ѿ����ڲ���Ϊ�ϱ�״̬�����Ƚ��б����ȡ���ϱ�����!";
					String title = "�����ϱ���ʾ" ;
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
		// modify by wangyga 2008-8-6 ɾ������������ʱ�ļ�
		file.delete();
		// file.deleteOnExit();�˷������ܼ�ʱ�����ʱ�ļ�

		return strNewFileName;

	}

	/**
	 * FormֵУ��
	 * 
	 * @param actionForm
	 * @return ֵУ��ʧ�ܵ���ʾ��Ϣ����
	 */
	public String[] validate(ActionForm actionForm) {
		return null;

	}

	/**
	 * ����Form
	 * 
	 */
	public String getFormName() {
		return nc.ui.iufo.dataexchange.base.AbstractExportForm.class.getName();
	}

	/**
	 * �������ص��ļ��� ����ʱ�䣺(2002-03-29 17:36:28)
	 * 
	 * @return java.lang.String ������·�����ļ�������
	 * @param request
	 *            javax.servlet.http.HttpServletRequest
	 */
	protected abstract String createFile(IUFOAction action);

	/**
	 * �õ��ļ��ڷ������˵ı���·���� �������ڣ�(2002-04-19 15:28:45)
	 * 
	 * @return java.lang.String
	 * @param request
	 *            javax.servlet.http.HttpServletRequest
	 */
	public String getFileServPath(Action action) {
		StringBuffer sbuf = new StringBuffer();
		// �õ��������˾���·����
		// sbuf.append(System.getProperty(IDataExchange.TempFilePath));

		String sbufs = WebGlobalValue.WORK_DIR;// System.getProperty(GlobalValue.CONTEXTKEY);
		sbufs = sbufs.substring(0, sbufs.length() - 1);
		sbuf.append(sbufs);

		// System.out.println(sbufs);
		// sbuf.append(IDataExchange.IUFO_PATH);

		// sbuf.append(System.getProperty(GlobalValue.CONTEXTKEY));
		// ����һ����ʱĿ¼��
		sbuf.append(getSubPath());
		sbuf.append(File.separator);
		sbuf.append(getExportID(action.getSessionId()));
		String strPath = sbuf.toString();
		// ���·���Ƿ����.
		File file = new File(strPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return strPath;
	}

	/**
	 * �õ������ļ������ӵ�ַ��·���� ����ʱ�䣺(2002-03-29 13:51:35)
	 * 
	 * @return String �ļ�·����Url����ΪNull
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
	 * ���ص�������ʾ��Ϣ�� �������ڣ�(2002-06-12 13:41:48)
	 * 
	 * @return java.lang.String
	 */
	protected String getHint() {
		String strHint = StringResource.getStringResource("miufo1002419"); // "�������Ҽ����Ϊ"
		return strHint + "...";
	}

	/**
	 * �õ������ļ������ӵ�ַ��·���е���·���� �������ڣ�(2002-04-19 15:23:29)
	 * 
	 * @return java.lang.String
	 */
	public String getSubPath() {
		return IDataExchange.PACKAGE_PATH;
	}

	/**
	 * ������������⡣ �������ڣ�(2002-04-22 09:59:02)
	 * 
	 * @return java.lang.String
	 * @param request
	 *            javax.servlet.http.HttpServletRequest
	 */
	public abstract String getTitle(Action action);

	/**
	 * ����sessionid������������IE�򿪵�ΨһIDֵ�����Weblogic���޷�ֱ�Ӵ򿪵�����EXCEL�ļ�����
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
 *               Description="��Ϊ���еĵ�������Ļ���" name="AbstractExportAction"
 *               package="nc.ui.iufo.dataexchange"
 *               ����Form="nc.ui.iufo.dataexchange.AbstractExportForm"> <MethodsVO
 *               execute=""> </MethodsVO> </ActionVO>
 * @WebDeveloper
 */
