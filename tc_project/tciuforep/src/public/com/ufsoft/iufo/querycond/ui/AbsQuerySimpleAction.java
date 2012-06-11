package com.ufsoft.iufo.querycond.ui;

import java.util.Arrays;
import java.util.Vector;

import nc.pub.iufo.accperiod.IUFODefaultNCAccSchemeUtil;
import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.TaskCache;
import nc.pub.iufo.cache.base.UnitCache;
import nc.ui.hbbb.pub.HBBBSysParaUtil;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.constants.IIUFOConstants;
import nc.ui.iufo.dataexchange.QueryResultExport;
import nc.ui.iufo.input.InputKeywordsUtil;
import nc.ui.iufo.server.param.ServerParamMngAction;
import nc.ui.iufo.web.reference.BusinessRefAction;
import nc.ui.iufo.web.reference.base.BDRefAction;
import nc.vo.iufo.code.CodeVO;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.pub.date.UFODate;
import nc.vo.iufo.task.TaskDefaultVO;
import nc.vo.iufo.task.TaskVO;
import nc.vo.iufo.unit.UnitInfoVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.RequestUtils;
import com.ufida.web.action.WebWindow;
import com.ufida.web.comp.Align;
import com.ufida.web.comp.Area;
import com.ufida.web.comp.WebButton;
import com.ufida.web.comp.WebCheckBox;
import com.ufida.web.comp.WebChoice;
import com.ufida.web.comp.WebDateRef;
import com.ufida.web.comp.WebHiddenField;
import com.ufida.web.comp.WebLabel;
import com.ufida.web.comp.WebTextField;
import com.ufida.web.comp.WebTextRef;
import com.ufida.web.comp.table.IWebTableModel;
import com.ufida.web.comp.table.Sort;
import com.ufida.web.comp.table.WebTable;
import com.ufida.web.container.WebGridLayout;
import com.ufida.web.container.WebPanel;
import com.ufida.web.html.Element;
import com.ufida.web.html.Script;
import com.ufida.web.html.StringElement;
import com.ufida.web.html.TD;
import com.ufida.web.html.Table;
import com.ufida.web.window.WebStatusBar;
import com.ufsoft.iufo.i18n.MultiLangUtil;
import com.ufsoft.iufo.querycond.vo.IQueryCondConstant;
import com.ufsoft.iufo.querycond.vo.QueryCondVO;
import com.ufsoft.iufo.resource.StringResource;

/**
 * ��ѯ����Ļ���action
 * @author weixl,Created on 2006-3-23
 */
public abstract class AbsQuerySimpleAction extends WithUnitTreeMultiFrameAction{
	//�洢�򵥲�ѯ������session��keyֵ
	private final static String QUERY_SIMPLE_COND="query_simple_cond_";
	
	//���ݲ�����ʽ�Ĳ���
	public static final String PARAM_OPER_TYPE="param_oper_type";
	
	//���ݲ�����ʽ���������ͣ�web��ʽ�ͱ����߷�ʽ
	public static final String OPER_TYPE_WEB="oper_type_web";
	public static final String OPER_TYPE_REPTOOL="oper_type_reptool";
	
	//����û�ѡ�е��е�checkbox��ֵ��hidden��id
	public static final String HID_OPER_CHECK_VALUE="hid_oper_check_value";
		
    public ActionForward execute(ActionForm actionForm) throws WebException {
    	processMenuVisible(actionForm);
        return new ActionForward(QueryModuRegiCenter.getShareInstance().getSimpleQueryUIClassName(getModuleType()));
    }	
	
	/**
	 * ajax��ȡ�˵�
	 * liuyy. 2006-07-27
	 */
	public Element getMenu(ActionForm form){		
		try {	
			String uiName = QueryModuRegiCenter.getShareInstance().getSimpleQueryUIClassName(getModuleType());
			try{
				getCurTaskId();
			}catch(Exception e){
				return new StringElement("");
			}
			processMenuVisible(form);
			WebWindow ui = (WebWindow) RequestUtils.applicationInstance(uiName);
			ui.processWindowInitial();
			return ui.getMenubar();
			
		} catch (Throwable e) {
		    AppDebug.debug(e);
		    return new StringElement(e.getMessage());
		}
		
	}
    
	/*��
	 * �õ��б�ı�ģ��
	 */
	public final IWebTableModel getTableModel(ActionForm actionForm,int[] iTotalPages) {
		String strTaskID=getCurTaskId();
		String strUnitID=getTreeSelectedID();

		//��ȡdoFind�������������������ͣ�true:��ʾ�û������˲��Ұ�ť;false:��ʾ��ǰ��ѯ����Ϊ�߼���ѯ����;
		//��������ʾ��ǰ��ѯ����Ϊ�򵥲�ѯ����
		String strFind=getRequestParameter("doFind");
		QueryCondVO queryCond=null;
		QuerySimpleForm form=null;
		
		TaskCache taskCache=IUFOUICacheManager.getSingleton().getTaskCache();
		KeyGroupCache kgCache=IUFOUICacheManager.getSingleton().getKeyGroupCache();
		
		TaskVO task=taskCache.getTaskVO(strTaskID);
		KeyVO[] keys=kgCache.getByPK(task.getKeyGroupId()).getKeys();
		
		//��Ӧ�򵥲��Ұ�ť����ȡ�û�ѡ��Ĳ�ѯ����
		if (strFind!=null && strFind.equalsIgnoreCase("true")){
			form=new QuerySimpleForm();
			form.setTaskID(getCurTaskId());
			String strTaskTimeProp=kgCache.getByPK(task.getKeyGroupId()).getTimeProp();
			form.setKeys(keys);
			form.setTaskDefault(new TaskDefaultVO());
			
			String strInputState=getRequestParameter(AbsQueryComplexCondUI.CHOICE_INPUTSTATE);
			if (strInputState!=null)
				form.setInputState(Integer.parseInt(strInputState));
			
			for (int i=0;i<keys.length;i++){
	        	if (showOtherKey()==false && keys[i].getKeywordPK().equals(KeyVO.CORP_PK)==false && keys[i].getTTimeKeyIndex()<0)
	        		continue;	
	        	
				String strVal=getRequestParameter("id_key"+i);
				if (strVal!=null && strVal.trim().length()==0)
					strVal=null;
				
				
				if (keys[i].getTTimeKeyIndex()>=0){
					StringBuffer bufInputDate=new StringBuffer();
					StringBuffer bufCheckedValue=new StringBuffer();
					String strError=InputKeywordsUtil.checkInputKeyValue(keys[i], strVal, task, strTaskTimeProp, true, null, null, bufInputDate, bufCheckedValue, null);
					if (bufInputDate.length()>0)
						form.setQueryDate(bufInputDate.toString());
					else
						form.setQueryDate(bufCheckedValue.toString());
				}
				else if (keys[i].getKeywordPK().equals(KeyVO.CORP_PK))
					form.setHasSubUnit(strVal!=null && strVal.equalsIgnoreCase("true"));
				else{
					if (strVal!=null && keys[i].getKeywordPK().equals(KeyVO.DIC_CORP_PK)){
						UnitCache unitCache=IUFOUICacheManager.getSingleton().getUnitCache();
						UnitInfoVO unitInfo=unitCache.getUnitInfoByCode(strVal);
						if (unitInfo!=null && unitInfo.isInOrg(getCurOrgPK()))
							strVal=unitInfo.getCode();
						else
							strVal=null;
					}
					form.getTaskDefault().setKeywordValueByIndex(i+1,strVal);
				}
			}
			//�����񱣴�򵥲�ѯ����
			Object obj = getClass().getName();
			addSessionObject(QUERY_SIMPLE_COND+getClass().getName()+"@"+strTaskID,form);
		}
		else{
			//�Ӹ��Ӳ�ѯ��������ˢ�¶����������ݿ���ȡ��ѯ����
			if (strFind!=null && strFind.equalsIgnoreCase("false")){
				try{
					queryCond=QueryCondBO_Client.loadQueryCond(getCurUserInfo().getID(),strTaskID,getModuleType(),getCurOrgPK());
				}
				catch(Exception e){
					AppDebug.debug(e);
				}
			}
			//��session��ȡ�򵥲�ѯ����
			else{
				form=(QuerySimpleForm)getSessionObject(QUERY_SIMPLE_COND+getClass().getName()+"@"+strTaskID);
			}
		}
		
		//���򵥲�ѯ����ת��Ϊͨ�ò�ѯ����
		if (queryCond==null)
			queryCond=convertQuerySimpleFormToQueryCond(form,strUnitID);
		else
			queryCond.setTaskKeyVOs(keys);
		
		//�õ������б����ݵĽӿ�
		IQueryDataLoad queryLoad=QueryModuRegiCenter.getShareInstance().getQueryDataLoad(getModuleType());
		
		try{
			//�򵥲�ѯʱ��Ҫ�Ӹ��Ӳ�ѯ�������ҵ��б�����Ϣ
			if (strFind==null || strFind.equals("false")==false){
				QueryCondVO complexCond=QueryCondBO_Client.loadQueryCond(getCurUserInfo().getID(),strTaskID,getModuleType(),getCurOrgPK());
				if (complexCond!=null)
					queryCond.setShowColumnIndexes(complexCond.getShowColumnIndexes());
			}
			
			//Ϊ�˵���Excel��Ҫ����IExcelExport�ӿڱ�����session�е�keyֵ�������ֵΪ�գ���ʾ�б����ݲ���Ҫ����
			String strExportSessID=QueryModuRegiCenter.getShareInstance().getExportSessionID(getModuleType());
			queryCond.setLoginUnitID(getCurUserInfo().getUnitId());
			if (strExportSessID!=null){
				QueryResultExport export=new QueryResultExport();
				export.setCurUserInfoVO(getCurUserInfo());
				export.setModuleType(getModuleType());
				export.setQueryCond(queryCond);			
				export.setTreeSelectID(strUnitID);
				export.setLoginOrgPK(getCurOrgPK());
				addSessionObject(strExportSessID,export);
			}
			
			//�������ݺ�̨������
			Sort sort = getTableSort();
			if (sort!=null){
				sort.setColumnName(StringResource.getStringResource(sort.getColumnName()));
			}
			//�õ���ģ�ͣ������ݱ�������Ȩ�޽��й���
			int iPage=getPage();
			int iPageNum=getRowNumPerPage();
			int iStartRow=(iPage-1)*iPageNum;
			int iEndRow=iPage*iPageNum-1;
			return QueryTableUIUtil.getTableModel(getCurUserInfo(),queryLoad,queryCond,queryLoad.isViewByReport(this),strUnitID,getCurOrgPK(),sort,MultiLangUtil.getLanguage(),iStartRow,iEndRow,iTotalPages);
		}
		catch(Exception e){
			AppDebug.debug(e);
			return null;
		}
	}
	
	/**
	 * �õ�ȱʡ�ļ򵥲�ѯ����
	 * @return
	 */
	private QuerySimpleForm getDefaultQueryForm(){
		QuerySimpleForm form=new QuerySimpleForm();
		
		TaskCache taskCache=IUFOUICacheManager.getSingleton().getTaskCache();
		form.setTaskID(getCurTaskId());
		form.setTaskDefault(taskCache.getTaskDefaultVO(getCurTaskId()));
		
		TaskVO task = taskCache.getTaskVO(getCurTaskId());
		KeyGroupVO keyGroup=IUFOUICacheManager.getSingleton().getKeyGroupCache().getByPK(task.getKeyGroupId());
		form.setKeys(keyGroup==null?null:keyGroup.getKeys());
		//���Ӵ������ڼ�����ʱ��ؼ���ֵ���߼���֧ added by liulp 2008-06-17
		String strQueryTTimeValue = QueryConditionUIUtil.getCurLoginTTimeValue(this);
		form.setQueryDate(strQueryTTimeValue);
		
		return form;
	}
	
	/**
	 * ���򵥲�ѯ����ת����ͨ�ò�ѯ����
	 * @param form���򵥲�ѯ����
	 * @param strUnitID������ѡ�еĵ�λ
	 * @return
	 */
	protected QueryCondVO convertQuerySimpleFormToQueryCond(QuerySimpleForm form,String strUnitID){
		QueryCondVO queryCond=new QueryCondVO();
		//����򵥲�ѯ����Ϊ��
		if (form==null)
			form=getDefaultQueryForm();
		
		//���δѡ�����нڵ㣬�����ò�ѯ������Ч�����������б��е�����
		queryCond.setInited(true);
		if (strUnitID==null || strUnitID.trim().length()<=0)
			queryCond.setInited(false);

		//��������ת������ʼʱ�䡢���񡢵�¼��λ��ģ�����͡������йؼ��֡�¼��״̬����ѯ�ı�����ѯ�ĵ�λ��Χ
		queryCond.setStartTime(form.getQueryDate());
		queryCond.setEndTime(form.getQueryDate());
		queryCond.setTaskId(form.getTaskID());
		queryCond.setLoginUnitID(strUnitID);
		queryCond.setModuleType(getModuleType());
		queryCond.setTaskKeyVOs(form.getKeys());
		queryCond.setInputState(form.getInputState());
		queryCond.setRepIDs(IUFOUICacheManager.getSingleton().getTaskCache().getReportIdsByTaskId(form.getTaskID()));
		
		if (form.isHasSubUnit())
			queryCond.setUnitIds(new String[]{strUnitID+"@"});
		else
			queryCond.setUnitIds(new String[]{strUnitID});
		
		//���ɵ�λ��ʱ����Ĺؼ�������
		TaskDefaultVO taskDefault=form.getTaskDefault();
		KeyVO[] taskKeys=form.getKeys();
		Vector<String> vKeyCond=new Vector<String>();
		if (taskDefault!=null && taskKeys!=null && showOtherKey()){
			for (int i=0;i<taskKeys.length;i++){
				String strKeyValue=taskDefault.getKeywordValueByIndex(i+1);
				if (taskKeys[i].getKeywordPK().equals(KeyVO.CORP_PK)==true 
						|| taskKeys[i].getTTimeKeyIndex()>=0 || strKeyValue==null){
					continue;
				}
				String strOneKeyCond=SaveQueryCondUtil.getOneKeyCond(taskKeys[i].getKeywordPK(),"=",strKeyValue);
				vKeyCond.add(strOneKeyCond);
			}
		}
		if (vKeyCond.size()>0)
			queryCond.setKeyConds(vKeyCond.toArray(new String[0]));
		
		return queryCond;
	}
	
	public ActionForward ajaxTable(ActionForm actionForm) {
		String text = "";
		try{	
			int[] iTotalPages=new int[1];
			IWebTableModel tableModel = getTableModel(actionForm,iTotalPages);

			paginal(tableModel,iTotalPages[0]);
			
			WebTable table = new WebTable();
	    	table.setSizeType(WebTable.TYPE_OFFSET);
			table.setModel(tableModel);
			tableModel.setSortMode(IWebTableModel.SORT_SERVER);
			tableModel.setSort(getTableSort());
		
			QuerySimpleForm form=(QuerySimpleForm)getSessionObject(QUERY_SIMPLE_COND+getClass().getName()+"@"+getCurTaskId());
			if (form==null)
				form=getDefaultQueryForm();
			WebPanel top=genRightTopPane(form,getModuleType(),showOtherKey());
			
			Table el = new Table();
			el.setWidth("100%");
			el.setNoBorder();

			TD td = el.appendTR().appendTD();
			td.addElement(top);
			td = el.appendTR().appendTD();
			td.addElement(table);

			Script script=getOnLoadScript();
					
			if (script!=null){
				text=script.toString();
			}
			text = el.toString() + text;	
			
		} catch(Throwable e){
			AppDebug.debug(e);
			text = e.getMessage();//"    ��ȡ���ģ�ʹ���: " +
		}
		ajax(text);
		return null;
	}
	
    /**
     * �ṩ�Ա�ģ�͵��ڴ��ҳ����
     * @param tableModel
     * @param modelData
     */       
    public void paginal(IWebTableModel tableModel,int num){
    	
		if(tableModel == null || !tableModel.isPagable() || tableModel.getDatas() == null){
    		return;
    	}
    	
    	int page = getPage();        	
    	
		int paginal = getRowNumPerPage();
		if(num <= paginal){
			return;
		}
	
		tableModel.setPaginal(paginal);
		tableModel.setCurPage(page);
		tableModel.setTotalNum(num);
		
    }
	
	/**
	 * ����ѡ��򵥲�ѯ��������
	 * @param form���û���ǰѡ��ļ򵥲�ѯ����
	 * @param iModuleType��ģ������
	 * @param bShowOtherKey���Ƿ���Ҫ��ʾ��λ��ʱ����������ؼ��֣����ںϲ���������ģ�飬����Ҫ��ʾ��λ��ʱ��ؼ���
	 * @return
	 * @i18n miufo00831=��
	 * @i18n uiufohbfunc0033=�汾��
	 */
	private WebPanel genRightTopPane(QuerySimpleForm form,int iModuleType,boolean bShowOtherKey){
		String strTaskId = form.getTaskID();
		TaskVO task = IUFOUICacheManager.getSingleton().getTaskCache().getTaskVO(strTaskId);
		KeyVO[] keys=form.getKeys();
		if (keys==null)
			keys=new KeyVO[0];
		
		//�жϽ����Ƿ���Ҫ��ʾ�߼���ѯ��ť
		boolean bShowComplexCond=QueryModuRegiCenter.getShareInstance().isShowComplexCond(iModuleType);
		
		//����Panel,������
		WebPanel panel=new WebPanel();
		WebGridLayout layout=new WebGridLayout(1,2*(keys.length+5));
		layout.setAlign(Align.ALIGN_LEFT);
		layout.setPadding(3);
		panel.setLayout(layout);
		
		//�õ���λ��ʱ��ؼ��ּ������ؼ���
		KeyVO timeKey=null;
		KeyVO unitKey=null;
		Vector<KeyVO> vKey=new Vector<KeyVO>();
		Vector<KeyVO> vSourKey=new Vector<KeyVO>(Arrays.asList(keys));
		for (int i=0;i<keys.length;i++){
			if (keys[i].getKeywordPK().equals(KeyVO.CORP_PK))
				unitKey=keys[i];
			else if (keys[i].getTTimeKeyIndex()>=0)
				timeKey=keys[i];
			else
				vKey.add(keys[i]);
		}
		
		//��ʱ��ؼ��ַ�����ǰ�棬��λ�ؼ��ַ��������
		if (timeKey!=null)
			vKey.insertElementAt(timeKey,0);
		if (unitKey!=null)
			vKey.add(unitKey);
		
		TaskDefaultVO taskDefault=form.getTaskDefault();
		
		//��������Ĺؼ���˳������ʾ
		int iPos=1;
		for (int i=0;i<vKey.size();i++){
			KeyVO key=(KeyVO)vKey.get(i);
			
			//�ж��Ƿ���ʾ����λ��ʱ����������ؼ���
        	if (bShowOtherKey==false &&key.getKeywordPK().equals(KeyVO.CORP_PK)==false && key.getTimeKeyIndex()<0)
        		continue;			
			
        	//�õ��ؼ�����ԭ�ؼ�������е�λ�ã�TaskDefaultVOҪ���ݴ�λ��ȡֵ��TaskDefaultVOδ�ṩ���ݹؼ���ȡֵ�ķ���
			int iSourIndex=vSourKey.indexOf(key);
			
			//�༭�ؼ���Label���ơ����ն�Ӧ��ActionForward����ʾ��ֵ
        	Element txt=null;
        	String strName=null;
        	ActionForward fwd=null;
        	String strValue=null;
			
            if(key.getKeywordPK().equals(KeyVO.CORP_PK)){ //��λ
                strName=null;
                WebCheckBox check=new WebCheckBox();
                check.setLabel(StringResource.getStringResource("miuforq008"));
                txt=check;
            } else if(key.getKeywordPK().equals(KeyVO.DIC_CORP_PK)){//�Է���λ
            	strName=StringResource.getStringResource("miufopublic276");
                txt=new WebTextRef();
                ((WebTextRef)txt).setShowValue(true);
                fwd=new ActionForward(BusinessRefAction.class.getName(),"getUnitCodeTreeRef");
                
                if (taskDefault!=null)
                	strValue=taskDefault.getKeywordValueByIndex(iSourIndex+1);
                if (strValue!=null){
                	UnitInfoVO unitInfo=IUFOUICacheManager.getSingleton().getUnitCache().getUnitInfoByCode(strValue);
                	if (unitInfo!=null && unitInfo.isInOrg(getCurOrgPK()))
                		strValue=unitInfo.getCode();
                	else
                		strValue=null;
                }
                	
            } else if(key.getType() == KeyVO.TYPE_TIME){//��Ȼʱ��
            	strName=StringResource.getStringResource("miufopublic328");
            	txt=new WebDateRef();
            	strValue=form.getQueryDate();
            } else if(key.isAccPeriodKey()){//����ڼ�
            	strName=key.getName();
            	txt=new WebTextRef();
            	((WebTextRef)txt).setShowValue(true);
            	strValue = form.getQueryDate();
            	String strAccPeriodPk = task.getAccPeriodScheme();
            	if(strAccPeriodPk == null || strAccPeriodPk.trim().length() == 0){
            		strAccPeriodPk = IUFODefaultNCAccSchemeUtil.getInstance().getIUFODefaultNCAccScheme();
            	}
            	fwd=new ActionForward(BDRefAction.class.getName(),"getAccPeriodRef");           	
            	fwd.addParameter(BDRefAction.PARAM_CUR_PEROIDSCHEME,strAccPeriodPk);
            	fwd.addParameter(BDRefAction.PARAM_CUR_PEROIDKEY,key.getKeywordPK());
            	if(strValue != null && strValue.trim().length() > 0){//add by wangyga 2008-7-14 �����յ�ֵ
            		fwd.addParameter(BDRefAction.REF_TEXT_VALUE, strValue);
            	}   
//            	strValue=AccPeriodSchemeUtil.getAccPeriodByNatDate(task.getAccPeriodScheme(), keys[i].getKeywordPK(), form.getQueryDate());            	
            }else if (key.getRef()!=null){//����ؼ���
            	strName=key.getName();
            	txt=new WebTextRef();
            	fwd=new ActionForward(BusinessRefAction.class.getName(),"getCodeRef");
            	fwd.addParameter(BDRefAction.CODE_ID,key.getRef());
                fwd.addParameter(BDRefAction.CODE_REF_RETURN_TYPE,CodeVO.ReturnType_Code);
                fwd.addParameter(BDRefAction.CODE_REF_TYPE, CodeVO.REFTYPE_ALL);
            }
            else{
            	strName=key.getName();
            	txt=new WebTextField();
            	((WebTextField)txt).setMaxlength(key.getLen());
            }
            
            if (strValue==null && taskDefault!=null)
            	strValue=taskDefault.getKeywordValueByIndex(iSourIndex+1);
            
            //������ԭ�ؼ�������е�λ�ø��༭�ؼ������������ύ�Ĵ���
            String strElementID="id_key"+iSourIndex;
            if (txt instanceof WebTextRef){
            	((WebTextRef)txt).setActionForward(fwd);
            	((WebTextField)((WebTextRef)txt).getRefFld()).setMaxlength(128);
            	((WebTextField)((WebTextRef)txt).getRefFld()).setSize("10");
            	((WebTextRef)txt).setValue(strValue);
            	((WebTextRef)txt).setID(strElementID);
            	((WebTextRef)txt).setName(strElementID);
            }
            else if (txt instanceof WebDateRef){
            	((WebTextField)((WebDateRef)txt).getRefFld()).setMaxlength(10);
            	((WebTextField)((WebDateRef)txt).getRefFld()).setSize("8");
            	((WebDateRef)txt).setValue(strValue);
            	((WebDateRef)txt).setID(strElementID);
            	((WebDateRef)txt).setName(strElementID);            	
            }
            else if (txt instanceof WebTextField){
            	((WebTextField)txt).setID(strElementID);
            	((WebTextField)txt).setName(strElementID);
            	((WebTextField)txt).setSize("10");
            	((WebTextField)txt).setValue(strValue);
            }
            else if (txt instanceof WebCheckBox){
            	((WebCheckBox)txt).setID(strElementID);
            	((WebCheckBox)txt).setName(strElementID);  
            	
            	if (form.isHasSubUnit())
            		((WebCheckBox)txt).setChecked(true);
            }
            if (strName==null)
            	panel.add(txt,new Area(1,iPos++,1,1));
            else{
            	panel.add(new WebLabel(strName+StringResource.getStringResource("miufo00831")),new Area(1,iPos++,1,1));
            	panel.add(txt,new Area(1,iPos++,1,1));
            }
		}
        WebChoice verChoice = getVerChoice(iModuleType);
		if(verChoice != null){
			panel.add(new WebLabel(StringResource.getStringResource("uiufohbfunc0033")),new Area(1,iPos++,1,1));
			panel.add(verChoice,new Area(1,iPos++,1,1));
		}
		
		//��ʾ�߼���ѯ������ť�ģ���Ҫ���"¼��״̬"������
		if (bShowComplexCond){
			WebChoice chInputState=new WebChoice();
			chInputState.setID(AbsQueryComplexCondUI.CHOICE_INPUTSTATE);
			chInputState.setName(AbsQueryComplexCondUI.CHOICE_INPUTSTATE);
			
			String[] strStateIDs={"miufoinputnew00041","miufoinputnew00042","miufoinputnew00043"};
			String[][] strItems=new String[strStateIDs.length][2];
			for (int i=0;i<strItems.length;i++){
				strItems[i][0]=""+i;
				strItems[i][1]=StringResource.getStringResource(strStateIDs[i]);
			}
			chInputState.setItems(strItems);
			chInputState.setValue(""+form.getInputState());
			
			panel.add(new WebLabel(StringResource.getStringResource("miufopublic240")+StringResource.getStringResource("miufo00831")),new Area(1,iPos++,1,1));
			panel.add(chInputState,new Area(1,iPos++,1,1));
		}
		
		//��Ӳ��Ұ�ť
		WebButton btnFind=new WebButton(StringResource.getStringResource("miufo1001590"));
		btnFind.setOnClick("simpleQueryAjaxTable()");
		btnFind.setID("find");
		btnFind.setName("find");
		panel.add(btnFind,new Area(1,iPos++,1,1));
		
		//��Ӹ߼���ѯ��ť
		if (bShowComplexCond && keys.length>0){
			WebButton btnComplexCond=new WebButton(StringResource.getStringResource("miuforq005"));
			btnComplexCond.setID("complexCond");
			btnComplexCond.setName("complexCond");
			
			String strComplexConClass=QueryModuRegiCenter.getShareInstance().getComplexQueryCondActionClassName(iModuleType);
			ActionForward fwd=new ActionForward(strComplexConClass,IIUFOConstants.ACTION_METHOD_OPEN);
			btnComplexCond.setActionForward(fwd);
			btnComplexCond.setOpenNewWindow(true);
			panel.add(btnComplexCond,new Area(1,iPos++,1,1));
		}
		
		//����hidden,һ���洢�û�ѡ�е��е�checkbox��ֵ��һ���洢�û�ѡ�еĲ������ͣ�Web��ʽ�򱨱��߷�ʽ��
		WebHiddenField hidCheckValue=new WebHiddenField();
		hidCheckValue.setID(HID_OPER_CHECK_VALUE);
		hidCheckValue.setName(HID_OPER_CHECK_VALUE);
		panel.add(hidCheckValue,new Area(1,iPos++,1,1));
		
		WebHiddenField hidOperType=new WebHiddenField();
		hidOperType.setID(PARAM_OPER_TYPE);
		hidOperType.setName(PARAM_OPER_TYPE);
		panel.add(hidOperType,new Area(1,iPos++,1,1));
		
		return panel;
	}
	
	public ActionForward ajaxValidateCond(ActionForm form){
		String text = "";
		try{	
			String strTaskID=getCurTaskId();
			TaskCache taskCache=IUFOUICacheManager.getSingleton().getTaskCache();
			KeyGroupCache kgCache=IUFOUICacheManager.getSingleton().getKeyGroupCache();
			
			TaskVO task=taskCache.getTaskVO(strTaskID);
			KeyVO[] keys=kgCache.getByPK(task.getKeyGroupId()).getKeys();
			String strTaskTimeProp=kgCache.getByPK(task.getKeyGroupId()).getTimeProp();
			
			for (int i=0;i<keys.length;i++){
	        	if (showOtherKey()==false && keys[i].getKeywordPK().equals(KeyVO.CORP_PK)==false && keys[i].getTTimeKeyIndex()<0)
	        		continue;	
	        	
				String strVal=getRequestParameter("id_key"+i);
				if (strVal!=null && strVal.trim().length()==0)
					continue;
				
				if (keys[i].getKeywordPK().equals(KeyVO.CORP_PK))
					continue;
				
				String strError=InputKeywordsUtil.checkInputKeyValue(keys[i], strVal, task, strTaskTimeProp, true, getCurUserInfo().getUnitId(), new StringBuffer(),new StringBuffer(), new StringBuffer(), getCurOrgPK());
				if (strError!=null && strError.trim().length()>=0){
					ajax(strError);
					return null;
				}
			}			
			
		} catch(Throwable e){
			AppDebug.debug(e);
			text = e.getMessage();//"    ��ȡ���ģ�ʹ���: " +
		}
		ajax(text);
		return null;
	}
	
	/**
	 * ����״̬���ķ���
	 */
	public WebStatusBar getStatusBar(ActionForm actionForm) {
		return ServerParamMngAction.getStatusBar(this,true,false);
	}	
	
	/**
	 * �򵥲�ѯ�������棬�Ƿ���ʾ����λ��ʱ����������ؼ��֣����ںϲ��������ݲ�ѯģ�飬��ֵΪfalse
	 * @return
	 */
	protected boolean showOtherKey(){
		return true;
	}
	
	protected void processMenuVisible(ActionForm actionForm){
	}
	
	/**
	 * �ý����Ӧ��ģ������
	 * @return
	 */
	abstract protected int getModuleType();
	
	/**
	 * ����onLoad�Ľű�
	 * @return
	 */
	abstract protected Script getOnLoadScript();
	/**
	 * �汾�������б�
	 * @param iModuleType
	 * @return
	 * @i18n miufoweb0018=ȫ��
	 * @i18n uiufofurl530114=���𱨱������
	 * @i18n uiufohb0015=�ϲ����������
	 */
	public WebChoice getVerChoice(int iModuleType){
		boolean bShowVerLst = iModuleType ==IQueryCondConstant.MODULE_ADJUSTREPORT;
		if(bShowVerLst){
			String strVer = getRequestParameter(AbsQueryComplexCondUI.CHOICE_VERSION);
			WebChoice choice = new WebChoice();
			choice.setItems(new String[][]{{IQueryCondConstant.VER_ALL_ADJUST+"",StringResource.getStringResource("miufoweb0018")},{IQueryCondConstant.VER_ADJUST+"",StringResource.getStringResource("uiufofurl530114")},{IQueryCondConstant.VER_HBBB_ADJUST+"",StringResource.getStringResource("uiufohb0015")}});
			choice.setID(AbsQueryComplexCondUI.CHOICE_VERSION);
			choice.setName(AbsQueryComplexCondUI.CHOICE_VERSION);
			if(strVer!=null)
				choice.setValue(strVer);
			return choice;
		}else
			return null;
	}
}
 