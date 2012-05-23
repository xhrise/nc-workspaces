/**
 * AbstractInputKeywordsAction.java  5.0 
 * 
 * WebDeveloper�Զ�����.
 * 2006-02-23
 */
package nc.ui.iufo.input;
import java.util.HashMap;

import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.TaskCache;
import nc.pub.iufo.cache.base.UnitCache;
import nc.pub.iufo.exception.CommonException;
import nc.ui.hbbb.pub.GValue;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.constants.IIUFOConstants;
import nc.ui.iufo.data.MeasurePubDataBO_Client;
import nc.ui.iufo.dataexchange.AutoMultiSheetImportAction;
import nc.ui.iufo.resmng.common.ResUIBizHelper;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.task.TaskDefaultVO;
import nc.vo.iufo.task.TaskVO;
import nc.vo.iufo.user.UserInfoVO;
import nc.vo.iuforeport.rep.RepFormatModel;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.action.ErrorForward;
import com.ufsoft.iufo.fmtplugin.service.ReportFormatSrv;
import com.ufsoft.iufo.web.IUFOAction;

/**
 * ¼��ؼ��֡�����IUFO���ݽ������Action
 * weixl
 * 2006-02-23
 */
public abstract class AutoAbstractInputKeywordsAction extends InputActionEx {
    public final static String PARAM_OLD_UNITID="oldUnitId";
    public final static String PARAM_OPEN_NEWWINDOW="param_isopennew";
    
    /**
     * ��������ύ
     * weixl
     * 2006-02-23
     */
    public final ActionForward execute(ActionForm actionForm){
    	InputKeywordsForm form=(InputKeywordsForm)actionForm;
    	try{
    		postAct(form);
    	}
    	catch(Exception e){
    		AppDebug.debug(e);//@devTools e.printStackTrace();
    		return new ErrorForward(e.getMessage());
    	}
    	
    	//�õ�ת���URL
    	ActionForward fwd=new ActionForward(getInputActionClass(),IIUFOConstants.ACTION_METHOD_OPEN);
    	form.getSomeParam().getLinkString(fwd);
        addInputFwdParams(fwd);
    	fwd.setRedirect(true);
    	
    	//�����Ҫˢ�´��ڣ�ʹ�������ض��򣬲��ر�����    	
    	if (isInOpenWindow()){
			StringBuffer bufScript=new StringBuffer();
			bufScript.append("window.opener.show_waitingBar();");
			bufScript.append("window.opener._inputAjaxTable('"+fwd.genURI()+"',false,true);");
			bufScript.append("window.close();");
			return new CloseForward(bufScript.toString());
    	}
    	else {
    		String strOpen=getRequestParameter(PARAM_OPEN_NEWWINDOW);
    		if (strOpen==null || !strOpen.equalsIgnoreCase("true")){
    			fwd.setActionName("nc.ui.iufo.dataexchange.AutoMultiSheetImportAction");
    			fwd.setMethodName("execute");
    			
    			return fwd;
    		} else{
    			String uri = fwd.genURI();
    			return new CloseForward("window.opener.navigate('"+uri+"');window.close();");
    		}
    	}
    }
    
    /**
     * ת��¼���������������
     * @param fwd
     */
    protected void addInputFwdParams(ActionForward fwd) {
    }

    /**
     * ��¼��ؼ��ֽ���
     * @param actionForm
     * @return
     */
    public ActionForward open(ActionForm actionForm){
    	try{
    		return openKeywordsUI((InputKeywordsForm)actionForm);
    	}
    	catch(Exception e){
    		return new ErrorForward(e.getMessage());
    	}
    }
    
    /**
     * �����ύ������¼��Ĺؼ���ֵ���õ�aloneid������form��CSomeParam������
     * @param form
     * @throws Exception
     */
    private void postAct(InputKeywordsForm form) throws Exception{    	
        CSomeParam somePar =getCSomeParam();
        try{
        	if(somePar.getTaskId() == null)
        		somePar.setTaskId(getCurTaskId());
        }catch(Exception e){
        	AppDebug.debug(e);
        }
        
        //���ݹؼ�ֵ������MeasurePubDataVO
        MeasurePubDataVO pubVO = checkInputKeyword(this, somePar.getTaskId(),isCheckTimeInTask(),isCheckOtherKey(),true);
        somePar.setUnitId(pubVO.getUnitPK());

        //�滻����Դ����
        processDataSourceAccount(this,pubVO);
        
        //�õ�MeasurePubDataVO��Ĵ����ɸ��������Լ�����
        doCheckAloneID(somePar, pubVO);
    	form.setSomeParam(somePar);
    }
    
    /**
     * �滻��ǰ����Դ������Ϣ
     * @param pubVO����ǰ¼��Ĺؼ�����Ϣ
     * @throws Exception
     */
    public static void processDataSourceAccount(IUFOAction action,MeasurePubDataVO pubVO){
    	
        DataSourceVO dataSVo=(DataSourceVO)action.getSessionObject(IIUFOConstants.DefaultDSInSession);
        if(dataSVo != null &&
           (dataSVo.getType() != DataSourceVO.TYPE8X || dataSVo.getType() != DataSourceVO.TYPE8XDECENT)){
            String oldUnitId =action.getRequestParameter(PARAM_OLD_UNITID);

        	String strNewDSUnitPK = getNewDSUnitInfo(pubVO.getUnitPK(),oldUnitId);
        	if(strNewDSUnitPK != null){
        		dataSVo.setLoginUnit(strNewDSUnitPK);
        	}
        	dataSVo.setLoginDate(getNewDSDateInfo(pubVO.getInputDate(),action.getCurLoginDate()));
        	
            action.addSessionObject(IIUFOConstants.DefaultDSInSession,dataSVo);
        }
    }
    /**
     * ִ������Դ��Ϣ�ĸ���
     * @param dataSVo
     * @param pubVO
     * @param oldUnitId
     * @param strCurLoginDate
     */
    public static String  getNewDSUnitInfo(String strCurUnitPK,String oldUnitId){
    	UnitCache unitCache=IUFOUICacheManager.getSingleton().getUnitCache();
        if(strCurUnitPK == null || strCurUnitPK.trim().length() <= 0)
            strCurUnitPK = unitCache.getRootUnitInfo().getPK();

        if(oldUnitId == null || oldUnitId.equals("") || !oldUnitId.equals(strCurUnitPK)){
            strCurUnitPK = nc.ui.iufo.datasource.DataSourcePub.getDSUnitCode(strCurUnitPK);
            if(strCurUnitPK != null && strCurUnitPK.length() > 0)
                return strCurUnitPK;
        }
        return null;
    }
    
    /**
     * ִ������Դ��Ϣ�ĸ���
     * @param dataSVo
     * @param pubVO
     * @param oldUnitId
     * @param strCurLoginDate
     */
    public static String  getNewDSDateInfo(String strInputDate,String strCurLoginDate){
        if(strInputDate != null && strInputDate.length() > 0 &&
        		strInputDate.equalsIgnoreCase("0000-00-00") == false){
            return strInputDate;
        } else{
            return strCurLoginDate;
        }
    }
    /**
     * ��¼��ؼ��ֽ���
     * @param form
     * @return
     * @throws Exception
     */
    private ActionForward openKeywordsUI(InputKeywordsForm form) throws Exception{
        ActionForward actionForward = null;
        
        TaskCache taskCache = IUFOUICacheManager.getSingleton().getTaskCache();
        KeyGroupCache keyGroupCache = IUFOUICacheManager.getSingleton().getKeyGroupCache();
        
        //��ȡ�����еĹؼ���ֵ
        HashMap keywordMapInsession = (HashMap)getSessionObject(GValue.KEYWORDMAP_INSESSION);
		if(keywordMapInsession == null)
			keywordMapInsession = new HashMap();
		form.setKeywordMap(keywordMapInsession);
        
		String strTaskId =getRequestParameter(CSomeParam.PARAM_TASKID);
        boolean bAnaRep = isAnaRepLook(this);
        if(!bAnaRep && strTaskId==null)
        	strTaskId=getCurTaskId();
        
        CSomeParam somePar=getCSomeParam();
        somePar.setTaskId(strTaskId);
        
        //�õ������ȱʡֵ
        TaskDefaultVO taskDefault = taskCache.getTaskDefaultVO(strTaskId);
        
        //����aloneid�õ��û��ϴ�¼���ֵ
        MeasurePubDataVO pubVO = null;
        if(somePar!= null && somePar.getAloneId()!=null)
            pubVO=MeasurePubDataBO_Client.findByAloneID(somePar.getAloneId());

        //�õ��ؼ������
        TaskVO taskVO = taskCache.getTaskVO(strTaskId);
        String strKeyGroupID = getKeyGroupID(this,taskVO);
        KeyVO[] keys = null;
        KeyGroupVO keyGroupVO = keyGroupCache.getByPK(strKeyGroupID);
        if(keyGroupVO != null)
            keys = keyGroupVO.getKeys();
        
        //form�в���ֵ׼��
        form.setTaskDefault(taskDefault);
        form.setPubDataVO(pubVO);
        form.setCurDate(getCurLoginDate());
        form.setCurUserInfo(getCurUserInfo());
        form.setAnaReport(bAnaRep);
        form.setKeyVOs(keys);
        form.setSomeParam(somePar);
        
        initFormInfo4Dis(form);

        //û�йؼ��֣�ֱ��ת��¼�����
        if(keys == null || keys.length <= 0){
        	actionForward=new ActionForward(getInputActionClass(),IIUFOConstants.ACTION_METHOD_OPEN);
        	if (strTaskId!=null)
        		actionForward.addParameter(CSomeParam.PARAM_TASKID,strTaskId);
        	
        	MeasurePubDataVO pubData=new MeasurePubDataVO();
        	pubData.setKType(strKeyGroupID);
        	pubData.setKeyGroup(keyGroupVO);
        	
        	String strAloneID=MeasurePubDataBO_Client.getAloneID(pubData);
        	actionForward.addParameter(CSomeParam.PARAM_ALONEID, strAloneID);
            if(somePar.getRepId() != null){
                actionForward.addParameter(CSomeParam.PARAM_REPID,somePar.getRepId());
            }
        	actionForward.setRedirect(true);
        }
        //ת��¼��ؼ��ֽ���
        else{
        	actionForward=new ActionForward(getInputKeywordsUIClass());
        	//actionForward=new ActionForward(AbstractInputKeywordsAction.class.getName() , "execute");
        }
        return actionForward;
    }

    /**
     * ������ʾ�����һЩ��Ϣ:ȱʡû��
     * @param form
     */
    protected void initFormInfo4Dis(InputKeywordsForm form) {     
    }

    /**
     * �õ�CSomeParam�������������������
     * @return
     */
    protected CSomeParam getCSomeParam() {
        return CSomeParam.getParam(this);
    }
    

    /**
     * �жϵ����ALONEID�Ƿ���ڼ��Ƿ�����е�ALONEID��ͬ�����޸�CSomeParam�����������ڵ���iufo���ݽ���
     * @param strAloneID String
     * @param pubVO MeasurePubDataVO
     */
    protected final void checkAloneID(CSomeParam somePar, MeasurePubDataVO pubVO) throws Exception{
        String aloneId = somePar.getAloneId();
        UserInfoVO userInfo =getCurUserInfo();

        MeasurePubDataVO oldPubData = MeasurePubDataBO_Client.findByAloneID(aloneId);
        pubVO.setVer(oldPubData.getVer());
        pubVO.setFormulaID(oldPubData.getFormulaID());
        MeasurePubDataVO newMeasurePubDataVO = MeasurePubDataBO_Client.findByKeywords(pubVO);
        
        //�ж�Ҫ����Ĺؼ��������������Ƿ���ڣ������ڸ�����ʾ
        if(newMeasurePubDataVO != null){
        	//���Ҫ����Ĺؼ��������뵱ǰ�ؼ���������ͬ������������ʾ
            String newAloneId = newMeasurePubDataVO.getAloneID();
            if(newAloneId != null && newAloneId.equals(aloneId)){
                throw new CommonException("miufo1002845", new String[]{getHintMess()});  //"���ܵ��뵱ǰ" + getHintMess() +                                            "������"
            }
            
            //����һ���µ�UfoTableUtil���Ž�session��
            if(newAloneId != null){
                String strDynAloneID=getRequestParameter(AbstractRepInputAction.HID_DYN_ALONEID);
                
                pubVO.setAloneID(newAloneId);
                UfoTableUtil util = getTableUtil();
                ReportFormatSrv repFormatSrv=InputUtil.getReportFormatSrv(somePar.getRepId(), null, newAloneId, somePar.getUnitId(),userInfo.getID(),true,false,getCurOrgPK(),getCurLoginDate());
                repFormatSrv.getContextVO().setPubDataVO(oldPubData);
                util.setRepFormatSrv(repFormatSrv);
                util.setCanInput(true);
                util.setUserInfo(getCurUserInfo());
                InputActionUtil.addTableToSession(this,util);
                
                if (strDynAloneID!=null && strDynAloneID.trim().length()>0)
                	util.setStrDynAloneID(strDynAloneID);
            }
        } else{
            //û��ѡ��" + getHintMess() + "������"
            throw new CommonException("miufo1002846", new String[]{getHintMess()});  //"û��ѡ��" + getHintMess() +                                        "������"
        }
        somePar.setUnitId(oldPubData.getUnitPK());
        somePar.setFrom(CSomeParam.FROM_IMPORT);
    }    
    
   /**
    * FormֵУ��
    * @param actionForm
    * @return ֵУ��ʧ�ܵ���ʾ��Ϣ����
    */
    public String[] validate(ActionForm actionForm){
       return null;
       
    }        
       
   /**
    * ����Form
    *
    */   
    public String getFormName(){
        return nc.ui.iufo.input.InputKeywordsForm.class.getName();
    }
    
    /**
     * �ж��Ƿ��Ƿ���������
     * @param action
     * @return
     */
    private final static boolean isAnaRepLook(IUFOAction action){
        boolean bReturn = false;

        String strOperType =action.getRequestParameter(CSomeParam.PARAM_OPERTYPE);
        if(strOperType == null){
            try{
                strOperType = ResUIBizHelper.getMenuType(action);
            } catch(Exception e){}
        }
        if(strOperType != null &&strOperType.equals(CSomeParam.STR_OPERATION[CSomeParam.VIEW_ANALYSIS])){
            bReturn = true;
        }
        return bReturn;
    }    
    
    /**
     * �õ���ǰ�򿪱�Ĺؼ������pk
     * @param action
     * @param taskVO
     * @return
     */
    public final static String getKeyGroupID(IUFOAction action, TaskVO taskVO){
        String strKeyGroupID = null;
        if(!isAnaRepLook(action)){
            if(taskVO != null){
                strKeyGroupID = taskVO.getKeyGroupId();
            }
        } else{
            String[] strRepIDs = null;
            boolean bSubmit = false;
            String strOperType=action.getRequestParameter(CSomeParam.PARAM_OPERTYPE);
            if(strOperType != null){
                bSubmit = true;
            } else{
                strOperType = ResUIBizHelper.getMenuType(action);
            }
            if(strOperType != null){
                if(bSubmit){
                    strRepIDs = new String[1];
                    strRepIDs[0] =action.getRequestParameter(CSomeParam.PARAM_REPID);
                } else{
                    try{
                        strRepIDs = action.getTableSelectedIDs();//ResMngUIHelper.getSelectItem(request);
                        strRepIDs = ResMngToolKit.getVOIDs(strRepIDs);
                    } catch(Exception e){
                        strRepIDs = new String[1];
                        strRepIDs[0]=action.getRequestParameter(CSomeParam.PARAM_REPID);
                    }
                }
            }
            //�жϱ����Ƿ����
            ReportVO[] repVOs = IUFOUICacheManager.getSingleton().getReportCache().getByPks(strRepIDs);
            if(repVOs == null || repVOs.length <1 || repVOs[0] == null){
                //�����ѱ�ɾ��
                throw new CommonException("miufo1002847");  //"�����ѱ�ɾ��"
            }
            //�жϱ����ʽ�Ƿ����
            RepFormatModel[] repFmtModels = IUFOUICacheManager.getSingleton().getRepFormatCache().getFormatByPks(strRepIDs);
            if(repFmtModels != null && repFmtModels.length >= 0 && repFmtModels[0] != null && repFmtModels[0].getFormatModel() != null){
                strKeyGroupID = repFmtModels[0].getMainKeyCombPK();
            } else{
                //������û����Ƹ�ʽ���޷��鿴����
                throw new CommonException("miufo1002848");  //"������û����Ƹ�ʽ���޷��鿴����"
            }
        }
        return strKeyGroupID;
    }    
    
    public final static MeasurePubDataVO checkInputKeyword(IUFOAction action, String taskId,boolean bCheckTimeInTask,boolean bCheckOtherKey,boolean bCheckUnit) throws Exception{
        TaskCache taskCache = IUFOUICacheManager.getSingleton().getTaskCache();
        KeyGroupCache kgCache=IUFOUICacheManager.getSingleton().getKeyGroupCache();
        
        TaskVO taskVO = taskCache.getTaskVO(taskId);
        UserInfoVO user =action.getCurUserInfo();
        
        //�õ��ؼ������PK:����������ģ�Ҳ�����Ƿ��������(����������)
        String strKeyGroupID = getKeyGroupID(action, taskVO);
        KeyGroupVO keyGroup=kgCache.getByPK(strKeyGroupID);
        KeyVO[] keys=keyGroup.getKeys();
        
        String[] strToBeChecdedValues = new String[keys.length];
        for(int i = 0; i < keys.length; i++){
            String strID = "id_key" + i;
            strToBeChecdedValues[i] =action.getRequestParameter(strID);
            if (strToBeChecdedValues[i]==null || strToBeChecdedValues[i].trim().length()<=0){
                strToBeChecdedValues[i] = action.getRequestParameter(strID+"Ref");
            }
        }
        
        return innerCheckInputKeyword(user,taskVO,strKeyGroupID,strToBeChecdedValues,bCheckTimeInTask,bCheckOtherKey,bCheckUnit,action.getCurOrgPK());
    }
    
    /**
     * ���¼��Ĺؼ����Ƿ���ȷ,������¼��Ĺؼ���ֵ�õ�MeasurePubDataVO
     * @param action,
     * @param taskId,��ǰ����
     * @param bCheckTimeInTask���Ƿ�Ҫ���¼���ڼ���������Ч����
     * @param bCheckOtherKey���ж��Ƿ�Ҫ������λ��ʱ����������ؼ���
     * @return
     * @throws Exception
     */
    public final static MeasurePubDataVO innerCheckInputKeyword(UserInfoVO user,TaskVO taskVO,String strKeyGroupID,String[] strToBeChecdedValues,boolean bCheckTimeInTask,boolean bCheckOtherKey,boolean bCheckUnit,String strOrgPK) throws Exception{
        KeyGroupCache kgCache=IUFOUICacheManager.getSingleton().getKeyGroupCache();
        
        KeyGroupVO keyGroup=kgCache.getByPK(strKeyGroupID);
        
        //�õ��ؼ�����ϵĹؼ�������ֵ
        String strTaskAttr = keyGroup.getTimeProp();
        KeyVO[] keys =keyGroup.getKeys();
        
        //�û�������ڼ�͵�λpk
        StringBuffer sbInputDate = new StringBuffer();
        StringBuffer sbUnitCode = new StringBuffer();
        
        String[] strKeyVals = new String[keys.length];
        for(int i = 0,iKeyPos=0; i < keys.length; i++,iKeyPos++){
            KeyVO keyVO = keys[i];
            
            //�Ƿ�����Ҫ���Ĺؼ���ֵ
        	boolean bNeedCheck = isKeyNeedCheck(bCheckOtherKey,bCheckUnit, keyVO);            
            if(!bNeedCheck){
            	iKeyPos--;
                continue;
            }
        	
            //��ȡ����ؼ���ֵ����ķ������Թ�web¼��ͱ�����¼��ͳһ���� liulp,2006-04-13
            StringBuffer sbCheckedValue = new StringBuffer();
            String strCurCheckErr = InputKeywordsUtil.checkInputKeyValue(
                keys[i],strToBeChecdedValues[iKeyPos],taskVO,strTaskAttr,bCheckTimeInTask,user.getUnitId(),
                sbUnitCode, sbInputDate,sbCheckedValue,strOrgPK);
            if(strCurCheckErr!=null){
                throw new WebException(strCurCheckErr);
            }
            if(sbCheckedValue !=null){
                strKeyVals[i] = sbCheckedValue.toString();
            }
        }

        MeasurePubDataVO pubVO = new MeasurePubDataVO();
        pubVO.setKType(strKeyGroupID);
        pubVO.setKeyGroup(keyGroup);

        if(sbUnitCode.length() > 0){
            pubVO.setUnitPK(sbUnitCode.toString());
        }
        if(sbInputDate.length() > 0){
            pubVO.setInputDate(sbInputDate.toString());
        } else{
            pubVO.setInputDate("0000-00-00");

        }
        pubVO.setKeywords(strKeyVals);
        return pubVO;
    }
    
    /**
     * �Ƿ�����Ҫ���Ĺؼ���ֵ
     * @param bCheckOtherKey ���˺ϲ�����Ŀ�¼��ؼ��֣������ؼ����Ƿ�����
     * @param keyVO
     * @return
     */
    public static boolean isKeyNeedCheck(boolean bCheckOtherKey,boolean bCheckUnit,KeyVO keyVO) {
        boolean bNeedCheck = true;
        if (bCheckUnit==false && keyVO.getKeywordPK().equals(KeyVO.CORP_PK)){
        	bNeedCheck=false;
        }
        else if (bCheckOtherKey==false && !isInputableKey4HBBB(keyVO)){
            bNeedCheck = false;
        }
        return bNeedCheck;
    }  
    
    /**
     * �Ƿ��Ǻϲ���������Ŀ�¼��ؼ���
     * @param bHBBBData �Ƿ�ΪHBBB����
     * @param keyVO
     * @return
     */
    public static boolean isInputableKey4HBBB(KeyVO keyVO) {
        boolean bKey4HBBB = false;
        if (keyVO.getKeywordPK().equals(KeyVO.CORP_PK) || keyVO.getTimeKeyIndex() >=0){
            bKey4HBBB = true;
        }
        return bKey4HBBB;
    }  
    
    /**
     * ��õ�ǰ�ؼ�����ϵ�ʱ������
     * @param strKeyGroupID
     * @return
     */
    public static String getTimeAttr(String strKeyGroupID){
        KeyGroupCache keyGroupCache = IUFOUICacheManager.getSingleton().getKeyGroupCache();
        KeyGroupVO keyGroupVO = keyGroupCache.getByPK(strKeyGroupID);
        String strTaskAttr = null;
        if(keyGroupVO != null){
            strTaskAttr = keyGroupVO.getTimeProp();
        }
        return strTaskAttr;
    }
    
    /**
     * ��ùؼ�����ϵĹؼ�������ֵ
     * @param strKeyGroupID
     * @return
     */
    public static KeyVO[] getKeys(String strKeyGroupID){
        KeyGroupCache keyGroupCache = IUFOUICacheManager.getSingleton().getKeyGroupCache();
        KeyGroupVO keyGroupVO = keyGroupCache.getByPK(strKeyGroupID);
        
        //�õ��ؼ����������ֵ
        KeyVO[] keys = null;
        if(keyGroupVO != null){
            keys = keyGroupVO.getKeys();
        }
        if(keys == null){
            keys = new KeyVO[0];
        }
        
        return keys;
    }
    
    /**
     * �������Ƿ�����¼�����λ��ʱ����������ؼ���
     * @return
     */
    protected boolean isCheckOtherKey(){
    	return true;
    }
    
    //����Ϊ�������صķ���
    
    /**
     * �õ�UfoTableUtil�ķ���������Щ���棬��Ҫ����UfoTableUtil��������ʵ��
     */
    protected abstract UfoTableUtil getTableUtil();
  
    /**
     * ������ʾ�Ŀ�ͷ
     * @return
     */
    protected abstract String getHintMess();   
    
    /**
     * �Ƿ���Ҫ���¼���ڼ���������Ч����
     * @return
     */
    protected abstract boolean isCheckTimeInTask();
    
    /**
     * ָ���ύ������Ҫˢ�¸����ڻ����ض��򵽸�����
     * @return
     */
    protected abstract boolean isInOpenWindow();
    
    /**
     * ��¼��Ĺؼ���ֵ����һ������ķ���
     * @param somePar
     * @param pubData
     * @throws Exception
     */
    protected abstract void doCheckAloneID(CSomeParam somePar, MeasurePubDataVO pubData) throws Exception;
    
    /**
     * ¼��ؼ��ֽ�������UI��
     * @return
     */
    protected abstract String getInputKeywordsUIClass();
    
    /**
     * ¼�뱨�����ݽ����Action
     * @return
     */
    protected abstract String getInputActionClass();
}

/**@WebDeveloper
<?xml version="1.0" encoding='gb2312'?>
    <ActionVO name="AbstractInputKeywordsAction" package="nc.ui.iufo.input" ����Form="nc.ui.iufo.input.InputKeywordsForm">
      <MethodsVO execute="">
      </MethodsVO>
    </ActionVO>
@WebDeveloper*/