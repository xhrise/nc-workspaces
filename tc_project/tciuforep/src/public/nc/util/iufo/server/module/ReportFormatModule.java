package nc.util.iufo.server.module;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import nc.pub.iufo.cache.IUFOCacheConstants;
import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.cache.ReportDirCache;
import nc.pub.iufo.cache.base.UICacheManager;
import nc.pub.iufo.exception.CommonException;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.server.center.AlertInfoForm;
import nc.ui.iufo.server.center.ExportModuleInfoAction;
import nc.ui.iufo.server.center.ImportModuleInfoAction;
import nc.ui.iufo.server.center.SaveExportInfoToSession;
import nc.ui.iufo.web.reference.BusinessRefAction;
import nc.util.iufo.iufo.resmng.IIUFOResMngConsants;
import nc.util.iufo.iufo.resmng.loader.IResTreeConstants;
import nc.util.iufo.iufo.resmng.loader.IUFOLoaderFactory;
import nc.util.iufo.pub.IDMaker;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.util.iufo.resmng.loader.LoaderParam;
import nc.util.iufo.server.file.FileInfoMng;
import nc.util.iufo.server.module.help.RepFormatImportCheckUtil;
import nc.util.iufo.server.module.help.RepFormatModuleFormulaUtil;
import nc.util.iufo.server.module.help.RepFormatModulePorcessPKUtil;
import nc.util.iufo.server.module.itf.IModuleExportFunc;
import nc.util.iufo.server.module.itf.IModuleExportUI;
import nc.util.iufo.server.module.itf.IModuleImportFunc;
import nc.util.iufo.server.module.itf.IModuleImportUI;
import nc.util.iufo.server.module.itf.IModuleMultiImportUI;
import nc.vo.iufo.authorization.IAuthorizeTypes;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iufo.pub.GlobalValue;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.iufo.user.UserInfoVO;
import nc.vo.iuforeport.rep.RepFormatModel;
import nc.vo.iuforeport.rep.ReportDirVO;
import nc.vo.iuforeport.rep.ReportVO;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.iufo.pub.tools.DateUtil;
import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.Align;
import com.ufida.web.comp.Area;
import com.ufida.web.comp.Position;
import com.ufida.web.comp.WebLabel;
import com.ufida.web.comp.WebTextField;
import com.ufida.web.comp.WebTextRef;
import com.ufida.web.comp.WebTree2List;
import com.ufida.web.comp.WebTree2ListModel;
import com.ufida.web.comp.table.WebTable;
import com.ufida.web.comp.table.WebTableModel;
import com.ufida.web.comp.tree.WebTreeModel;
import com.ufida.web.container.WebGridLayout;
import com.ufida.web.container.WebPanel;
import com.ufida.web.html.HR;
import com.ufida.web.util.WebGlobalValue;
import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.fmtplugin.key.KeywordModel;
import com.ufsoft.iufo.fmtplugin.xml.IUFOXMLImpExpUtil;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.web.IUFOAction;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.report.sysplugin.xml.IXmlParser;
import com.ufsoft.report.sysplugin.xml.XMLParserManager;
import com.ufsoft.table.CellsModel;

public class ReportFormatModule extends BaseModuleOper implements IModuleExportFunc,IModuleExportUI,IModuleImportFunc,IModuleImportUI,IModuleMultiImportUI{
	private final static String REP_NAME = "report_Name";

	private final static String REP_COMMENT = "note";

	private final static String REP_CREATOR = "report_Creator";

	private final static String REP_TIME = "report_Time";

	private final static String REP_CODE = "report_code";

	private final static String REP_MODEL_TAG = "model_tag";

	private final static String REP_INTRADE_TAG = "intrade_tag";

	private final static String REP_ANALYSIS_TAG = "analysis_tag";

	public final static String DIRECTORY_TEXT = "tfDir";

	//��ͷ��Ϣ�ڵ����ԣ���¼�������ƣ�
	private final static String HEAD_REPNAME = "repName";
	
	private boolean m_bUpdate = false;
	
	
	//�����������صļ�������
	/**
	 * �Ƿ��Ǳ���ģ��ģ��
	 */
	protected boolean isModel() {
		return false;
	}	
	
	/**
	 * Ԫ�صĸ�tagName
	 * @return
	 */
	private String getElementModuleTag() {
		return RegistCenter.REPFORMAT_TAG;
	}	
	
	/**
	 * ʵ��IModuleExportUI�ӿ�,���ɵ���ʱѡ�񱨱����
	 */
	public WebPanel createExportInterface(Object objIds,IUFOAction action) {
		try {
			if (objIds != null && (!(objIds instanceof Vector))) {
				return null;
			}

			//�õ�����PK����
			String[] strRepPKs = null;
			if (objIds != null) {
				Vector<String> vecIds = (Vector<String>) objIds;
				strRepPKs=vecIds.toArray(new String[0]);
			}
			
			UserInfoVO userVO=getUserVO();
			
			//��ѡ�񱨱�VO
			ReportCache repCache = IUFOUICacheManager.getSingleton().getReportCache();
			ReportVO[] selRepVOs = null;
			if (strRepPKs != null)
				selRepVOs = repCache.getByPks(strRepPKs);

			ArrayList<String[]> vDestItems=new ArrayList<String[]>();
			for (int i=0;selRepVOs!=null && i<selRepVOs.length;i++)
				vDestItems.add(new String[]{selRepVOs[i].getReportPK(),selRepVOs[i].getNameWithCode()});
	
			WebPanel content = new WebPanel();
			content.setLayout(new WebGridLayout(3, 1));
			
			//���ɱ���ѡ�����
			WebTree2List tree2List_Report = new WebTree2List();
			tree2List_Report.getSrcTree().setSize(250,400);
			tree2List_Report.getLstDest().setSize(250,400);
			
			WebTree2ListModel model=new WebTree2ListModel();
			tree2List_Report.setModel(model);

			model.setDestTitle(StringResource.getStringResource("miufotasknew00004"));// "��ѡ�񱨱�"
			model.setSrcTitle(StringResource.getStringResource("uiufotask00017"));
		
			model.setDestItems((String[][])vDestItems.toArray(new String[vDestItems.size()][2]));
			tree2List_Report.getBtnAdd().setOnClick("tree2list_rep_add('"+WebGlobalValue.LIST_SELECTED_ID+"')");
			
			LoaderParam loaderParam = new LoaderParam(userVO.getUnitId(), !isModel(), IAuthorizeTypes.AU_REPDIR_TYPE_VIEW,true);
			IResTreeObject[] tos = IUFOLoaderFactory.getTreeLoader(IResTreeConstants.TYPE_REPORTDIR_REFTREE_FOR_TASK,
					isModel()?IIUFOResMngConsants.MODULE_REPORT_MODEL:IIUFOResMngConsants.MODULE_REPORT_DIR, "").loadResTreeObjs(userVO.getID(),
					true, loaderParam, null);
			WebTreeModel treeModel = new WebTreeModel(tos);		
			model.setTreeModel(treeModel);
			
			content.add(tree2List_Report, new Area(1,1,1,1));
			content.add(new HR(),new Area(2,1,1,1));
			
			//���ɰ�ť
			ActionForward fwd=new ActionForward(ExportModuleInfoAction.class.getName(),"");
			content.add(createBtnGroup(fwd),new Area(new Position(3,1),new Position(3,1),new Align(Align.RIGHT)),WebGridLayout.NOSEPARATE);

			return content;
		} catch (Exception e) {
			AppDebug.debug(e);//@devTools e.printStackTrace(System.out);
			throw new CommonException("miufo1004007"); //"�޷����ɵ���ѡ��ҳ��"
		}
	}
	
	/**
	 * �Խӿ�IModuleExportUI��exportSubmit������ʵ�֣������û�ѡ�񵼳��ı���
	 */
	public void exportSubmit(String strModuleName, IUFOAction action) {
		try {
	        String[] strIds = action.getListValues(WebGlobalValue.LIST_SELECTED_ID);
	        Vector<String> vec = null;
	        if (strIds != null && strIds.length > 0) {
	            int nSize = strIds.length;
	            vec = new Vector<String>(nSize);
	            for (int i = 0; i < nSize; i++) {
	            	//��Ҫ�Եõ���idֵ���ض�
	            	strIds[i] = ResMngToolKit.getVOIDByTreeObjectID(strIds[i]);
	                vec.addElement(strIds[i]);
	            }
	        }
	        SaveExportInfoToSession.saveOtherExportData(action, strModuleName, vec);   
	    }catch (Exception e) {
	    	AppDebug.debug(e);//@devTools         e.printStackTrace(System.out);
	    }
	}	

	/**
	 * ʵ��IModuleImportUI�ӿ�,���ɵ���ʱѡ�񱨱����
	 */
	public WebPanel createImportInterface(Element elem, Vector vecSelIds,String strModuleName) {		
		WebPanel content = new WebPanel();
		content.setLayout(new WebGridLayout(5, 2));

		//�õ�������Ϣ���
		WebTable table = getDefaultImportTable(elem, vecSelIds,strModuleName);
		table.setHeight(" expression(document.body.offsetHeight/2)");
		table.setWidth(" expression(document.body.offsetWidth-15)");

		//������
		int iRowNum = 1;
		content.add(table, new Area(iRowNum,1,2,1,new Align(Align.CENTER)));

		//���뱨��Ŀ¼ѡ���
		iRowNum++;
		WebLabel label = new WebLabel(StringResource.getStringResource("miufo1000769")); //"����Ŀ¼��"
		content.add(label,new Area(iRowNum,1,1,1));

		WebTextRef tfDir = new WebTextRef();
		((WebTextField)tfDir.getRefFld()).setMaxlength(128);
		((WebTextField)tfDir.getRefFld()).setSize(30);
		((WebTextField)tfDir.getRefFld()).setVld_NoNull(true);
		((WebTextField)tfDir.getRefFld()).setVld_label_id(StringResource.getStringResource("miufopublic364"));
		tfDir.setID(DIRECTORY_ID);
		tfDir.setName(DIRECTORY_ID);
		((WebTextField)tfDir.getRefFld()).setReadOnly(true);
		
		content.add(tfDir,new Area(iRowNum,2,1,1));
		
		//������ѡ��ı���Ŀ¼
		if (getImportDirId()!=null && getImportDirId().trim().length()>0){
			try{
				ReportDirVO dirVO=IUFOUICacheManager.getSingleton().getReportDirCache().getReportDir(getImportDirId());
				if (dirVO!=null){
					tfDir.setValue(getImportDirId());
					((WebTextField)tfDir.getRefFld()).setValue(dirVO.getName());
				}
			}
			catch(Exception e){
				AppDebug.debug(e);
			}
		}		

		//��Ӧ������ģ�壬��Ŀ¼���ղ���ͬ
		if (!isModel()) {
			ActionForward actFwd = new ActionForward(BusinessRefAction.class.getName(),"getReportDirTreeRef");
			actFwd.addParameter(WebGlobalValue.PARAM_NEED_AUTYPE,Integer.toString(IAuthorizeTypes.AU_REPDIR_TYPE_FULL));  //��Ҫ��ȫ����Ȩ�޵ı���Ŀ¼����
			tfDir.setActionForward(actFwd);
		}else{ 
			tfDir.setActionForward(new ActionForward(BusinessRefAction.class.getName(),"getReportModelDirTreeRef"));
		}

		iRowNum++;
		content.add(new WebLabel("&nbsp;"), new Area(iRowNum,1,1,1));

		//����button
		iRowNum++;
		ActionForward fwd=new ActionForward(ImportModuleInfoAction.class.getName(),"");
		content.add(createBtnGroup(fwd), new Area(new Position(iRowNum,1),new Position(iRowNum,2),new Align(Align.RIGHT)), WebGridLayout.HORIZONTAL);

		return content;
	}	
	
	/**
	 * ʵ�ֽӿ�IModuleImportUI�ķ���
	 */
	public void importSubmit(String strModuleName,IUFOAction action) {
		//���ø���ķ����ύѡ����б��е�����
		super.importSubmit(strModuleName, action);
		//�ύѡ��ĵ��뱨���ŵ�Ŀ¼
		importDirIdSubmit(strModuleName, action);
	}	
	
	/**
	 * �õ�����ʱ���ļ��а����ı����б�
	 */
	public WebTable getDefaultImportTable(Element elem, Vector vecSelIds,String strModuleName) {
		//����ͷ
		WebTable table = new WebTable();
		table.setHeight(" expression(document.body.offsetHeight/2)");
		table.setWidth(" expression(document.body.offsetWidth-10)");
		
		WebTableModel model=new WebTableModel();
		table.setModel(model);
		
		String[] strHeadIDs={"miufopublic324","miufopublic121"};
		String[] strHeads=new String[strHeadIDs.length];
		for (int i=0;i<strHeadIDs.length;i++){
			strHeads[i]=StringResource.getStringResource(strHeadIDs[i]);
		}
		model.setColumns(strHeads);

		NodeList nodelist = elem.getElementsByTagName(FileInfoMng.MODULESELECTID);
		if (nodelist == null || nodelist.getLength()<=0)
			return table;
		
		int length = nodelist.getLength();
		boolean[] bSelecteds=new boolean[length];
		Object[][] rowDatas=new Object[length][3];
		for (int i = 0; i < length; i++) {
			Element elemSel = (Element) nodelist.item(i);
			String strRepname = elemSel.getAttribute(HEAD_REPNAME);
			String strRepCode = elemSel.getFirstChild().getNodeValue();

			rowDatas[i][0]=strRepCode;
			if (vecSelIds != null && vecSelIds.contains(strRepCode))
				bSelecteds[i]=true;
			
			rowDatas[i][1]=strRepCode;
			rowDatas[i][2]=strRepname;
		}
		model.setDatas(rowDatas);
		model.setSelecteds(bSelecteds);

		return table;
	}	
	
	/**
	 * ʵ��IModuleExportFunc�ӿڵķ���,���������ʽ
	 */
	public Element[] getDataElements(Object[] selObj,Map hashRetKey) {
		Element[] rtnElements = new Element[selObj.length];
		for (int i = 0; i < selObj.length; i++) {
			String repPK = (String) selObj[i];
			rtnElements[i] = getDocument().createElement(getElementModuleTag());
			ReportCache reportCache = CacheProxy.getSingleton().getReportCache();
			ReportVO reportVO = (ReportVO) reportCache.get(repPK);
			
			//���ɱ���Ļ�����Ϣ
			setAttribute(rtnElements[i], SELECTID, reportVO.getCode());
			setAttribute(rtnElements[i], REP_NAME, reportVO.getName());
			setAttribute(rtnElements[i], REP_COMMENT, reportVO.getNote());
			setAttribute(rtnElements[i], REP_CODE, reportVO.getCode());
			setAttribute(rtnElements[i], "keyCombPK", reportVO.getKeyCombPK());
			setAttribute(rtnElements[i], "modifyTime", reportVO.getModifiedTime());
			setAttribute(rtnElements[i],REP_INTRADE_TAG,""+reportVO.isIntrade());
			
			//���ر����ʽģ��
			UfoContextVO context = new UfoContextVO();
			context.setAttribute(ReportContextKey.REPORT_PK,repPK);
			CellsModel cellsModel = CellsModelOperator.getFormatModelByPKWithDataProcess(context);
//			CellsModelOperator.getFormatModelByPKWithDataProcess �ڲ�����clone���� liuyy. 2007-12-03
//			cellsModel=(CellsModel)DeepCopyUtil.getDeepCopyBySerializable(cellsModel);
			
			//ת��ģ�͹�ʽ,��PK̬ת��Ϊ�û�̬
			RepFormatModuleFormulaUtil.convertFormulas(reportVO,cellsModel,getDataSourceVO(),false);
			
			//ת����XML
			XMLParserManager manager = IUFOXMLImpExpUtil.getManager();
			IXmlParser paser = manager.getXmlParser(CellsModel.class);
			paser.objectToXml(rtnElements[i],cellsModel);
		}
		return rtnElements;
	}		
	
	/**
	 * ʵ�ֽӿ�IModuleExportFunc�ķ���
	 */
	public void makeModuleList(Object obj, Element elemModule, Document doc,Map<String,String> hashFileName,Map<Object,String> hashDetailPK) {
		if (obj == null || elemModule == null)
			return;
		try {
			Vector vecIds = (Vector) obj;
			if (vecIds==null || vecIds.size()<=0)
				return;

			ReportCache repCache =IUFOUICacheManager.getSingleton().getReportCache();

			//��ģ��ڵ������ѡ�е�ģ������.
			for (int j = 0; j < vecIds.size(); j++) {
				String	 	strRepPK = (String) vecIds.elementAt(j);
				//��Ҫ��������ԴPK��Ϊ����PK
				strRepPK  = ResMngToolKit.getVOIDByTreeObjectID(strRepPK);
				ReportVO   	repVO = (ReportVO)repCache.get(strRepPK);
				if( repVO != null){
					String strRepCode = repVO.getCode();
					String strRepName = repVO.getName();

					//����ģ��
					Element elem = FileInfoMng.addMessage(FileInfoMng.MODULESELECTID, strRepCode,elemModule, doc);
					addFileInfo(elem,repVO.getCode(),hashFileName);
					FileInfoMng.setAttribute(elem, HEAD_REPNAME, strRepName);
					elemModule.appendChild(elem);
				}
			}
		} catch (Exception e) {
			AppDebug.debug(e);//@devTools e.printStackTrace(System.out);
		}
	}	
	
	/**
	 * ʵ��IModuleImportFunc�ӿڵķ�����У�鱨���Ƿ���Ե���
	 */
	public int checkKey(Element importElement,StringBuffer strErrMsg,int iPrevCheckType) {
		if (importElement==null){
			strErrMsg.append(StringResource.getStringResource("miufoserv043"));
			return AlertInfoForm.MSG_TYPE_FAILURE;
		}
		
		if (iPrevCheckType==AlertInfoForm.MSG_TYPE_DELMEASURE || iPrevCheckType==AlertInfoForm.MSG_TYPE_SUCCESS)
			return AlertInfoForm.MSG_TYPE_SUCCESS;
		
		setDataElement(importElement);	

		//�õ����������Ϣ
		ReportVO repVO =getReportVOFromElem(getDataElement());
		
		//�õ�����Ҫ�����Ŀ¼���ж�Ŀ¼�Ƿ����
		ReportDirVO repDirVO = getRepDirVO();
		if (repDirVO == null) {
			strErrMsg.append(StringResource.getStringResource("miufoserv034"));
			return AlertInfoForm.MSG_TYPE_FAILURE;
		}
		
		String strRepCode = repVO.getCode();
		ReportCache repCache =IUFOUICacheManager.getSingleton().getReportCache();
		try {
			//���ڷǷ�������ı����ҵ�ͬ���������ı���
			ReportVO existReport=null;
			if( repVO.getRepType() != ReportDirVO.REPORT_DIR_TYPE_ANALYSIS){
				String strExistRepPK=repCache.getRepPKByCode(strRepCode,isModel());
				if (strExistRepPK!=null)
					existReport=repCache.getByPks(new String[]{strExistRepPK})[0];
			}
			
			//���ͬ����ı�����ڣ���ģ���ֱ�Ӹ���������Ϣ���Ա���Ҫ����һ�����ж�
			if (existReport!=null) {
				//ģ��ֱ�ӱ���
				if (isModel()){
					//"�������\""strRepCode"\"��Ŀ������Ѿ�����,���ܵ���!"
					strErrMsg.append(StringResource.getStringResource("miufoexpnew00061", new String[] { strRepCode }));
					return AlertInfoForm.MSG_TYPE_FAILURE;
				}
	
				//���������ʽ
				CellsModel cellsModel =getCellsModelByDataElement();
				
				//��ָ�ꡢ�ؼ��������õĴ�����Ӵ�������ת���ɴ���PK,�ж��Ƿ���Щ����ϵͳ�в�����
				RepFormatModulePorcessPKUtil.processKeyMeasureCodeRef(cellsModel,strErrMsg);
				if (strErrMsg.length()>0){
					return AlertInfoForm.MSG_TYPE_FAILURE;
				}
				
				if (iPrevCheckType<AlertInfoForm.MSG_TYPE_INPUTCODE){
					//ѯ���û��Ƿ���Ը���ԭ���ı���
					strErrMsg.append(StringResource.getStringResource("miufoexpnew00052",new String[]{repVO.getCode()}));
					return AlertInfoForm.MSG_TYPE_INPUTCODE;					
				}
				
				//�ж϶�ԭ��������Ŀ¼�Ƿ����޸�Ȩ��
				int iAuthType=IAuthorizeTypes.AU_REPDIR_TYPE_FULL;
				if (existReport.getRepDir().equals(getImportDirId()))
					iAuthType=IAuthorizeTypes.AU_REPDIR_TYPE_MODIFY;
				if (!RepFormatImportCheckUtil.isHaveModifyRepRight(existReport.getRepDir(),getUserVO(),getOrgPK(),iAuthType)){
					strErrMsg.append(StringResource.getStringResource(iAuthType==IAuthorizeTypes.AU_REPDIR_TYPE_MODIFY?"miufoexpnew00008":"miufoexpnew00066",new String[]{strRepCode}));
					return AlertInfoForm.MSG_TYPE_FAILURE;
				}				
				
				//�жϵ���Ŀ¼���Ƿ������������ͬ�����벻��ͬ�ı���
				boolean bExistSameNameRep=isExistSameNameRep(getImportDirId(),strRepCode,repVO.getName());
				if (iPrevCheckType<AlertInfoForm.MSG_TYPE_INPUTNAME && bExistSameNameRep) { //�����ظ�,��ʾ�û������µ����ƣ�����ʾ�û��Ƿ���Ը���ԭ���ı���
					strErrMsg.append(StringResource.getStringResource("miufoexpnew00058", new String[]{repVO.getName()}));
					return AlertInfoForm.MSG_TYPE_INPUTNAME;
				}				
				
				//�жϸñ������Ƿ�ɾ����̬����ָ��
				String[] strCheckMsg=RepFormatImportCheckUtil.checkMeasDynAreaConsitent(strRepCode,cellsModel,isModel());
				if (strCheckMsg!=null && strCheckMsg.length>0){
					for (int i=0;i<strCheckMsg.length;i++){
						strErrMsg.append(strCheckMsg[i]);
						strErrMsg.append("\r\n");
					}
					return AlertInfoForm.MSG_TYPE_DELMEASURE;
				}
				return AlertInfoForm.MSG_TYPE_SUCCESS;
			}
			else {
				//���������ʽ
				CellsModel cellsModel =getCellsModelByDataElement();
				
				//��ָ�ꡢ�ؼ��������õĴ�����Ӵ�������ת���ɴ���PK,�ж��Ƿ���Щ����ϵͳ�в�����
				RepFormatModulePorcessPKUtil.processKeyMeasureCodeRef(cellsModel,strErrMsg);
				if (strErrMsg.length()>0){
					return AlertInfoForm.MSG_TYPE_FAILURE;
				}	
				
				String strDirId = getImportDirId();
				String strRepName = repVO.getName();
				boolean bFindName = repCache.isExistRepNameByDir(strRepName, strDirId);
				if (bFindName) { //�����ظ�,��ʾ�û������µ����ƣ�����ʾ�û��Ƿ���Ը���ԭ���ı���
					if (isModel()==false)
						strErrMsg.append(StringResource.getStringResource("miufoexpnew00058", new String[] { repVO.getName() }));
					else
						strErrMsg.append(StringResource.getStringResource("miufoexpnew00059", new String[] { repVO.getName() }));
					return AlertInfoForm.MSG_TYPE_INPUTNAME;
				}	
				else{
					if (iPrevCheckType<0){
						if (isModel()==false)
							strErrMsg.append(StringResource.getStringResource("miufoexpnew00051",new String[]{repVO.getNameWithCode()}));
						else
							strErrMsg.append(StringResource.getStringResource("miufoexpnew00060",new String[]{repVO.getNameWithCode()}));
					}
					return AlertInfoForm.MSG_TYPE_SUCCESS;
				}
			}
		} catch (Exception e) {
			AppDebug.debug(e);//@devTools e.printStackTrace(System.out);
			//"�����ʽУ������г����쳣"
			strErrMsg.append(StringResource.getStringResource("miufopublic450"));
			return AlertInfoForm.MSG_TYPE_FAILURE;
		}
	}	
	
	/**
	 * ʵ�ֽӿ�IModuleImportFunc�ķ���,���뱨���ʽ
	 */
	public void importData(Element element) {
		if (element==null)
			return;
		
		setDataElement(element);

		try {
			ReportVO repVO = getReportVO();
			if (repVO != null) {
				//�õ������ʽģ��
				CellsModel fModel =getCellsModelByDataElement();

				//���ӱ���Ĵ���ʱ��ͱ���ؼ������
				repVO.setTime(DateUtil.getCurTime());
				repVO.setKeyCombPK(KeywordModel.getInstance(fModel).getMainKeyCombPK());
				
				//�ȴ����������±�����Ҫ��Ϣ
				ReportCache repCache =IUFOUICacheManager.getSingleton().getReportCache();				
				if (m_bUpdate == false)
					repCache.add(repVO);
				else
					repCache.updateRepBaseInfo(repVO);	
				
				//�ٸ��±����ʽ
				if (fModel!=null)
					innerImportData(fModel, repVO,getDataSourceVO(),null,m_bUpdate,isModel());
			}		
		} catch (CommonException e) {
			throw e;
		} catch (Exception e) {
			String strErrMsg=StringResource.getStringResource("miufo1002440");
			throw new CommonException(strErrMsg+":"+e.getMessage()); //"���뱨���ʽ�����쳣"
		}	
	}	

	/**
	 * ��XML�ļ����ݵõ�CellsModel
	 * @return
	 */
	private CellsModel getCellsModelByDataElement(){
		XMLParserManager manager = IUFOXMLImpExpUtil.getManager();
		IXmlParser parser = manager.getXmlParser(CellsModel.class);
		CellsModel cellsModel = (CellsModel) parser.xmlToObject(getDataElement());
		return cellsModel;
	}

	/**
	 * �õ�����Ӧ�õ����Ŀ¼����
	 * @return
	 */
	private nc.vo.iuforeport.rep.ReportDirVO getRepDirVO() {
		String repDirPK = getImportDirId();
		ReportDirCache cache =IUFOUICacheManager.getSingleton().getReportDirCache();
		return cache.getReportDir(repDirPK);
	}

	/**
	 * �õ����������Ϣ
	 * @return
	 */
	private ReportVO getReportVO() {
		ReportVO repVO = getReportVOFromElem(getDataElement());
		String strChangeName = getRepName();
		//��¼���뱨��͵���ʱ�ı�������Ƿ����ı�
		if (strChangeName != null) {
			repVO.setName(strChangeName);
		}
		
		//���ڷ�������Ҫ�������ɱ������
		ReportCache repCache = (ReportCache) UICacheManager.getSingleton().getCache(IUFOCacheConstants.ReportCacheObjName);
		if( repVO.getRepType() == ReportDirVO.REPORT_DIR_TYPE_ANALYSIS){
			//�������ɱ������
			while(true){
				String strCode = IDMaker.makeID(GlobalValue.ID_LEN);
				if( repCache.getByCode(strCode, false)== null){
					repVO.setCode(strCode);
					break;
				}
			}
		}	
		//��鱨���Ƿ����
		String strRepPK=null;
		ReportVO repFindVO = repCache.getByCode(repVO.getCode(), repVO.isModel());
		if (repFindVO != null) {
			strRepPK = repFindVO.getReportPK();
			m_bUpdate = true; //���ø��±��
		} else {
			while (true) {
				strRepPK = IDMaker.makeID(nc.vo.iufo.pub.GlobalValue.ID_LEN);
				if (repCache.get(strRepPK) == null) {
					break;
				}
			}
		}
		//����pk
		repVO.setReportPK(strRepPK);
		//����ǰ�û��͵�λ��Ϊ������λ���û�
		repVO.setUserPK(getUserVO().getID());

		return repVO;
	}

	/**
	 * �õ�����Ļ�����Ϣ
	 * @param elem
	 * @return
	 */
	private ReportVO getReportVOFromElem(Element elem) {
		ReportCache repCache=IUFOUICacheManager.getSingleton().getReportCache();
		
		//����ReportVO
		ReportVO repVO = new ReportVO();
		repVO.setName(elem.getAttribute(REP_NAME));
		repVO.setTime(elem.getAttribute(REP_TIME));
		repVO.setNote(elem.getAttribute(REP_COMMENT));
		repVO.setUserPK(elem.getAttribute(REP_CREATOR));

		String strCode = elem.getAttribute(REP_CODE);
		if (strCode != null && !strCode.equals("")) {
			repVO.setCode(strCode);
		}
		
		if (getRepCode()!=null)
			repVO.setCode(getRepCode());
		
		if (getRepName()!=null)
			repVO.setName(getRepName());		

		//���ñ���Ŀ¼id
		if (isMoveRepDir()==false && repCache.getByCode(repVO.getCode(),isModel())!=null){
			ReportVO report=repCache.getByCode(repVO.getCode(),isModel());
			setImportDirId(report.getRepDir());
		}
		
		String strRepDirId = getImportDirId();
		repVO.setRepDir(strRepDirId);

		//�Ƿ���ģ��
		String strTag = elem.getAttribute(REP_MODEL_TAG);
		repVO.setModel(strTag.equals("1") ? true : false);
		
		//�ڲ�����
		strTag = elem.getAttribute(REP_INTRADE_TAG);
		repVO.setIntrade((strTag!=null && strTag.equalsIgnoreCase("true")) ? true : false);

		//�Ƿ��Ƿ����� 2004-05-14
		strTag = elem.getAttribute(REP_ANALYSIS_TAG);
		if (strTag != null && strTag.length() > 0) {
			try {
				int nType = Integer.parseInt(strTag);
				repVO.setRepType(nType);
			} catch (Exception e) {
			}
		}
		
		return repVO;
	}
	
	/**
	 * �ж�ָ��Ŀ¼�£��Ƿ����������ΪstrRepName,�����벻ΪstrRepCode�ı���
	 * @param strDirId��Ҫ���ҵ�Ŀ¼PK
	 * @param strRepCode,�Ƚϵı������
	 * @param strRepName���Ƚϵı�������
	 * @return
	 */
	private boolean isExistSameNameRep(String strDirId,String strRepCode,String strRepName){
		ReportCache repCache=(ReportCache) UICacheManager.getSingleton().getCache(IUFOCacheConstants.ReportCacheObjName);
		
		ReportVO[] reports= repCache.getReportsByDirPK(strDirId);
		for (int i=0;reports!=null && i<reports.length;i++){
			if (reports[i]!=null && reports[i].getName()!=null 
					&& reports[i].getName().equalsIgnoreCase(strRepName) && strRepCode.equalsIgnoreCase(reports[i].getCode())==false){
				return true;
			}
		}
		return false;
	}		
		
	/**
	 * Ϊ�˶༶���������뱨���ʽ�뱨���������XML�ļ����ã���ȡ���˹�������
	 * @param fModel���Ѿ���XML�ļ��м��س��ĸ�ʽģ��
	 * @param repVO���������
	 * @param dataSource����ǰ����Դ
	 * @param strOldRepPK������ʱ�����PK��ͨ����ָ���е�repPK���PK���Ƚϣ������ж�ָ���Ƿ��Ǳ����ָ��
	 * @param bUpdate���Ƿ����½�����״̬
	 * @param bModel���Ƿ���ģ���
	 * @throws Exception
	 */
	private static void innerImportData(CellsModel fModel,ReportVO repVO,DataSourceVO dataSource,String strOldRepPK,boolean bUpdate,boolean bModel) throws Exception{
		RepFormatModulePorcessPKUtil.processKeyMeasureCodeRef(fModel,new StringBuffer());
		Hashtable<String,String> hashKeyPKMap=RepFormatModulePorcessPKUtil.processKeywordModel(fModel,repVO);
		Hashtable<String,String> hashMeasPKMap=RepFormatModulePorcessPKUtil.processMeasureModel(fModel,repVO,strOldRepPK,bModel);
		RepFormatModulePorcessPKUtil.processFormulaModel(fModel);
		RepFormatModulePorcessPKUtil.processPrintSet(fModel);
		RepFormatModulePorcessPKUtil.processReportBusinessQuery(fModel,hashMeasPKMap,hashKeyPKMap);
		
		RepFormatModel repFormatModel = new RepFormatModel();
		repFormatModel.setFormatModel(fModel);
		repFormatModel.setReportPK(repVO.getReportPK());
		
		if (bUpdate){
			ReportVO oldReport=IUFOUICacheManager.getSingleton().getReportCache().getByPks(new String[]{repVO.getReportPK()})[0];
			repFormatModel.setMainKeyCombPK(oldReport.getKeyCombPK());
		}
		
		IUFOUICacheManager.getSingleton().getRepFormatCache().updateRepFormat(repFormatModel,DateUtil.getCurTime());
		
		//ת����ʽ������ʹ�����Ҫ�󣨽���ʽ�е�ָ��������תΪ���룩
		repFormatModel=CacheProxy.getSingleton().getRepFormatCache().getFormatByPk(repVO.getReportPK());
		RepFormatModuleFormulaUtil.convertFormulas(repVO, repFormatModel.getFormatModel(),dataSource,true);
		
		if (strOldRepPK!=null){
			RepFormatModuleFormulaUtil.replaceMeasPKs4TransReps(repFormatModel.getFormatModel(),hashMeasPKMap);
		}
		IUFOUICacheManager.getSingleton().getRepFormatCache().updateRepFormat(repFormatModel,DateUtil.getCurTime());
	}
	
	/**
	 * Ϊ�������ͨ��ֱ�ӵ���XML�ļ������뱨�������Ĺ��߷���
	 * @param cellModel
	 * @param repVO
	 * @param strOldRepPK
	 * @param dataSource
	 * @i18n miufo00510=��
	 */
	public static void importFromXMLFile(CellsModel cellModel,ReportVO repVO,String strOldRepPK,DataSourceVO dataSource) {
		try{
			innerImportData(cellModel,repVO,dataSource,strOldRepPK,false,false);
		} catch (CommonException e) {
			throw e;
		} catch (Exception e) {
			AppDebug.debug(e);//@devTools e.printStackTrace(System.out);
			String strErrMsg=StringResource.getStringResource("miufo1002440");
			throw new CommonException(strErrMsg+StringResource.getStringResource("miufo00510")+e.getMessage()); //"���뱨���ʽ�����쳣"
		}	
	}	
}
 