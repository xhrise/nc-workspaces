/**
 * AbstractInputKeywordsAction.java  5.0 
 * 
 * WebDeveloper自动生成.
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
 * 录入关键字、导入IUFO数据界面基类Action
 * weixl
 * 2006-02-23
 */
public abstract class AutoAbstractInputKeywordsAction extends InputActionEx {
    public final static String PARAM_OLD_UNITID="oldUnitId";
    public final static String PARAM_OPEN_NEWWINDOW="param_isopennew";
    
    /**
     * 处理界面提交
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
    	
    	//得到转向的URL
    	ActionForward fwd=new ActionForward(getInputActionClass(),IIUFOConstants.ACTION_METHOD_OPEN);
    	form.getSomeParam().getLinkString(fwd);
        addInputFwdParams(fwd);
    	fwd.setRedirect(true);
    	
    	//如果需要刷新窗口，使父窗口重定向，并关闭自身    	
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
     * 转向到录入界面额外参数处理
     * @param fwd
     */
    protected void addInputFwdParams(ActionForward fwd) {
    }

    /**
     * 打开录入关键字界面
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
     * 处理提交，根据录入的关键字值，得到aloneid，置于form的CSomeParam参数中
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
        
        //根据关键值，生成MeasurePubDataVO
        MeasurePubDataVO pubVO = checkInputKeyword(this, somePar.getTaskId(),isCheckTimeInTask(),isCheckOtherKey(),true);
        somePar.setUnitId(pubVO.getUnitPK());

        //替换数据源帐套
        processDataSourceAccount(this,pubVO);
        
        //得到MeasurePubDataVO后的处理，由各派生类自己处理
        doCheckAloneID(somePar, pubVO);
    	form.setSomeParam(somePar);
    }
    
    /**
     * 替换当前数据源配置信息
     * @param pubVO，当前录入的关键字信息
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
     * 执行数据源信息的更新
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
     * 执行数据源信息的更新
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
     * 打开录入关键字界面
     * @param form
     * @return
     * @throws Exception
     */
    private ActionForward openKeywordsUI(InputKeywordsForm form) throws Exception{
        ActionForward actionForward = null;
        
        TaskCache taskCache = IUFOUICacheManager.getSingleton().getTaskCache();
        KeyGroupCache keyGroupCache = IUFOUICacheManager.getSingleton().getKeyGroupCache();
        
        //获取缓存中的关键字值
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
        
        //得到任务的缺省值
        TaskDefaultVO taskDefault = taskCache.getTaskDefaultVO(strTaskId);
        
        //根据aloneid得到用户上次录入的值
        MeasurePubDataVO pubVO = null;
        if(somePar!= null && somePar.getAloneId()!=null)
            pubVO=MeasurePubDataBO_Client.findByAloneID(somePar.getAloneId());

        //得到关键字组合
        TaskVO taskVO = taskCache.getTaskVO(strTaskId);
        String strKeyGroupID = getKeyGroupID(this,taskVO);
        KeyVO[] keys = null;
        KeyGroupVO keyGroupVO = keyGroupCache.getByPK(strKeyGroupID);
        if(keyGroupVO != null)
            keys = keyGroupVO.getKeys();
        
        //form中参数值准备
        form.setTaskDefault(taskDefault);
        form.setPubDataVO(pubVO);
        form.setCurDate(getCurLoginDate());
        form.setCurUserInfo(getCurUserInfo());
        form.setAnaReport(bAnaRep);
        form.setKeyVOs(keys);
        form.setSomeParam(somePar);
        
        initFormInfo4Dis(form);

        //没有关键字，直接转到录入界面
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
        //转到录入关键字界面
        else{
        	actionForward=new ActionForward(getInputKeywordsUIClass());
        	//actionForward=new ActionForward(AbstractInputKeywordsAction.class.getName() , "execute");
        }
        return actionForward;
    }

    /**
     * 设置显示界面的一些信息:缺省没有
     * @param form
     */
    protected void initFormInfo4Dis(InputKeywordsForm form) {     
    }

    /**
     * 得到CSomeParam参数，派生类可以重载
     * @return
     */
    protected CSomeParam getCSomeParam() {
        return CSomeParam.getParam(this);
    }
    

    /**
     * 判断导入的ALONEID是否存在及是否和已有的ALONEID相同，并修改CSomeParam，本方法用于导入iufo数据界面
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
        
        //判断要导入的关键字条件的数据是否存在，不存在给出提示
        if(newMeasurePubDataVO != null){
        	//如果要导入的关键字条件与当前关键字条件相同，给出出错提示
            String newAloneId = newMeasurePubDataVO.getAloneID();
            if(newAloneId != null && newAloneId.equals(aloneId)){
                throw new CommonException("miufo1002845", new String[]{getHintMess()});  //"不能导入当前" + getHintMess() +                                            "的数据"
            }
            
            //生成一个新的UfoTableUtil，放进session中
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
            //没有选定" + getHintMess() + "的数据"
            throw new CommonException("miufo1002846", new String[]{getHintMess()});  //"没有选定" + getHintMess() +                                        "的数据"
        }
        somePar.setUnitId(oldPubData.getUnitPK());
        somePar.setFrom(CSomeParam.FROM_IMPORT);
    }    
    
   /**
    * Form值校验
    * @param actionForm
    * @return 值校验失败的提示信息集合
    */
    public String[] validate(ActionForm actionForm){
       return null;
       
    }        
       
   /**
    * 关联Form
    *
    */   
    public String getFormName(){
        return nc.ui.iufo.input.InputKeywordsForm.class.getName();
    }
    
    /**
     * 判断是否是分析表数据
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
     * 得到当前打开表的关键字组合pk
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
            //判断报表是否存在
            ReportVO[] repVOs = IUFOUICacheManager.getSingleton().getReportCache().getByPks(strRepIDs);
            if(repVOs == null || repVOs.length <1 || repVOs[0] == null){
                //报表已被删除
                throw new CommonException("miufo1002847");  //"报表已被删除"
            }
            //判断报表格式是否存在
            RepFormatModel[] repFmtModels = IUFOUICacheManager.getSingleton().getRepFormatCache().getFormatByPks(strRepIDs);
            if(repFmtModels != null && repFmtModels.length >= 0 && repFmtModels[0] != null && repFmtModels[0].getFormatModel() != null){
                strKeyGroupID = repFmtModels[0].getMainKeyCombPK();
            } else{
                //分析表还没有设计格式，无法查看数据
                throw new CommonException("miufo1002848");  //"分析表还没有设计格式，无法查看数据"
            }
        }
        return strKeyGroupID;
    }    
    
    public final static MeasurePubDataVO checkInputKeyword(IUFOAction action, String taskId,boolean bCheckTimeInTask,boolean bCheckOtherKey,boolean bCheckUnit) throws Exception{
        TaskCache taskCache = IUFOUICacheManager.getSingleton().getTaskCache();
        KeyGroupCache kgCache=IUFOUICacheManager.getSingleton().getKeyGroupCache();
        
        TaskVO taskVO = taskCache.getTaskVO(taskId);
        UserInfoVO user =action.getCurUserInfo();
        
        //得到关键字组合PK:可能是任务的，也可能是分析报表的(不属于任务)
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
     * 检查录入的关键字是否正确,并根据录入的关键字值得到MeasurePubDataVO
     * @param action,
     * @param taskId,当前任务
     * @param bCheckTimeInTask，是否要检查录入期间在任务有效期内
     * @param bCheckOtherKey，判断是否要检查除单位、时间外的其他关键字
     * @return
     * @throws Exception
     */
    public final static MeasurePubDataVO innerCheckInputKeyword(UserInfoVO user,TaskVO taskVO,String strKeyGroupID,String[] strToBeChecdedValues,boolean bCheckTimeInTask,boolean bCheckOtherKey,boolean bCheckUnit,String strOrgPK) throws Exception{
        KeyGroupCache kgCache=IUFOUICacheManager.getSingleton().getKeyGroupCache();
        
        KeyGroupVO keyGroup=kgCache.getByPK(strKeyGroupID);
        
        //得到关键字组合的关键字数组值
        String strTaskAttr = keyGroup.getTimeProp();
        KeyVO[] keys =keyGroup.getKeys();
        
        //用户输入的期间和单位pk
        StringBuffer sbInputDate = new StringBuffer();
        StringBuffer sbUnitCode = new StringBuffer();
        
        String[] strKeyVals = new String[keys.length];
        for(int i = 0,iKeyPos=0; i < keys.length; i++,iKeyPos++){
            KeyVO keyVO = keys[i];
            
            //是否是需要检查的关键字值
        	boolean bNeedCheck = isKeyNeedCheck(bCheckOtherKey,bCheckUnit, keyVO);            
            if(!bNeedCheck){
            	iKeyPos--;
                continue;
            }
        	
            //抽取检验关键字值输入的方法，以供web录入和报表工具录入统一调用 liulp,2006-04-13
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
     * 是否是需要检查的关键字值
     * @param bCheckOtherKey 除了合并报表的可录入关键字，其他关键字是否还需检查
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
     * 是否是合并报表任务的可录入关键字
     * @param bHBBBData 是否为HBBB数据
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
     * 获得当前关键字组合的时间属性
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
     * 获得关键字组合的关键字数组值
     * @param strKeyGroupID
     * @return
     */
    public static KeyVO[] getKeys(String strKeyGroupID){
        KeyGroupCache keyGroupCache = IUFOUICacheManager.getSingleton().getKeyGroupCache();
        KeyGroupVO keyGroupVO = keyGroupCache.getByPK(strKeyGroupID);
        
        //得到关键字组合数组值
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
     * 界面上是否允许录入除单位、时间外的其他关键字
     * @return
     */
    protected boolean isCheckOtherKey(){
    	return true;
    }
    
    //以下为必须重载的方法
    
    /**
     * 得到UfoTableUtil的方法，在有些界面，需要返回UfoTableUtil的派生类实例
     */
    protected abstract UfoTableUtil getTableUtil();
  
    /**
     * 出错提示的开头
     * @return
     */
    protected abstract String getHintMess();   
    
    /**
     * 是否需要检查录入期间在任务有效期内
     * @return
     */
    protected abstract boolean isCheckTimeInTask();
    
    /**
     * 指明提交后是需要刷新父窗口还是重定向到父窗口
     * @return
     */
    protected abstract boolean isInOpenWindow();
    
    /**
     * 对录入的关键字值做进一步处理的方法
     * @param somePar
     * @param pubData
     * @throws Exception
     */
    protected abstract void doCheckAloneID(CSomeParam somePar, MeasurePubDataVO pubData) throws Exception;
    
    /**
     * 录入关键字界面具体的UI类
     * @return
     */
    protected abstract String getInputKeywordsUIClass();
    
    /**
     * 录入报表数据界面的Action
     * @return
     */
    protected abstract String getInputActionClass();
}

/**@WebDeveloper
<?xml version="1.0" encoding='gb2312'?>
    <ActionVO name="AbstractInputKeywordsAction" package="nc.ui.iufo.input" 关联Form="nc.ui.iufo.input.InputKeywordsForm">
      <MethodsVO execute="">
      </MethodsVO>
    </ActionVO>
@WebDeveloper*/