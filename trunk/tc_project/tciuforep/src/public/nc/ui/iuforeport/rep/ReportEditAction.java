/*
 * 创建日期 2006-1-17
 *
 */
package nc.ui.iuforeport.rep;
import java.util.ArrayList;
import java.util.List;

import nc.ui.iufo.analysisrep.AnaRepEditAction;
import nc.ui.iufo.analysisrep.RepFiledImpl;
import nc.ui.iufo.resmng.common.ResWebEnvKit;
import nc.ui.iufo.resmng.common.ResWebParam;
import nc.ui.iufo.resmng.common.UISrvException;
import nc.ui.iufo.resmng.uitemplate.IResTreeObjForm;
import nc.ui.iufo.resmng.uitemplate.ResEditObjAction;
import nc.ui.iufo.resmng.uitemplate.describer.ResMngHome;
import nc.util.iufo.iufo.resmng.IIUFOResMngConsants;
import nc.util.iufo.pub.CodeNameMaker;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.iuforeport.rep.ReportDirVO;
import nc.vo.iuforeport.rep.ReportVO;
import nc.vo.pub.ValueObject;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.iufo.pub.tools.DateUtil;
import com.ufida.web.action.ActionForm;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 
 * 报表模块编辑报表的Action
 * @author liulp
 *
 */
public class ReportEditAction extends ResEditObjAction{
	public ReportEditAction(){
		super();
	}

	/* （非 Javadoc）
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#getSaveActionName()
	 */
	protected String getSaveActionName() {
		return ReportEditAction.class.getName();
	}

	/* （非 Javadoc）
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#getLabelObjName()
	 */
	protected String getLabelObjName() {
		return (getReportStr() + StringResource.getStringResource("miufo1001154"));  //"名称"
	}

	/* （非 Javadoc）
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#getLabelObjNote()
	 */
	protected String getLabelObjNote() {
		return (getReportStr() + StringResource.getStringResource("miufo1002462"));  //"说明"
	}

	/* （非 Javadoc）
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#isEditable(nc.vo.iufo.resmng.uitemplate.IResTreeObject)
	 */
	protected boolean isEditable(IResTreeObject resTreeObj) {
		return true;
	}

	/* （非 Javadoc）
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#doGetUpdateVO(nc.ui.iufo.resmng.uitemplate.IResTreeObjForm, nc.vo.pub.ValueObject)
	 */
	protected ValueObject doGetUpdateVO(IResTreeObjForm resTreeObjForm, ValueObject srcVO) {
		ReportForm reportFileObjForm = changetoReportFileObjForm(resTreeObjForm);
		
		ReportVO repVo = (ReportVO)srcVO.clone();
         //设置修改时间
         String date = DateUtil.getCurTime();
         repVo.setModifiedTime(date);
         //保存报表名称、说明
         repVo.setName(resTreeObjForm.getName());
         repVo.setNote(resTreeObjForm.getNote());
         //#保存报表编码
         repVo.setCode(reportFileObjForm.getReportCode());         
         //是否内部交易表
         repVo.setIntrade(reportFileObjForm.isHBIntrade());
         //王宇光 2008-3-7添加 添加原因：报表增加单元公式是否允许编辑属性
         repVo.setFormulaIsEdit(reportFileObjForm.getFormulaIsEdit());
         
         return repVo;
	}

	/**
	 * 
	 * @param resTreeObjForm
	 * @return
	 */
	protected ReportForm changetoReportFileObjForm(IResTreeObjForm resTreeObjForm) {
		return (ReportForm)resTreeObjForm;
	}

	/* （非 Javadoc）
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#doGetNewVO(java.lang.String, nc.ui.iufo.resmng.uitemplate.IResTreeObjForm, nc.ui.iufo.resmng.common.ResWebParam)
	 */
	protected ValueObject doGetNewVO(String strParentVOPK, IResTreeObjForm resTreeObjForm, ResWebParam resWebParam) {
		ReportVO repVo = AnaRepEditAction.getBaseNewReportVO(strParentVOPK,resTreeObjForm,resWebParam);
		
		//设置类型
		repVo.setRepType(ReportDirVO.REPORT_DIR_TYPE_DEFAULT);
		repVo.setModel(getModuleID().equals(IIUFOResMngConsants.MODULE_REPORT_MODEL));
		
    	ReportForm reportFileObjForm = changetoReportFileObjForm(resTreeObjForm);
        //#保存报表编码
        repVo.setCode(reportFileObjForm.getReportCode());
        
        repVo.setIntrade(reportFileObjForm.isHBIntrade());
        
        //王宇光 2008-3-7添加 添加原因：报表增加单元公式是否允许编辑属性
        repVo.setFormulaIsEdit(reportFileObjForm.getFormulaIsEdit());
        
        return repVo;
	}
	/**
	 * 得到Form名称
	 * 说明： 重写父类方法
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#getFormName()
	 */
    public String getFormName() {
        return ReportForm.class.getName();
     }
    /**
     * 得到新建或修改报表的界面类名
	 * 说明： 重写父类方法
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#getExecuteUI()
     */
	protected String getExecuteUI() {
		return ReportEditUI.class.getName();
	}
    /**
	 * 重写父类方法
     * @param form
     * @param selResTreeObj
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#initFormValue(ActionForm, IResTreeObject)
     */
    protected void initFormValue(ActionForm actionForm, IResTreeObject selResTreeObj) {
    	super.initFormValue(actionForm,selResTreeObj);
    	ReportForm reportFileObjForm = changetoReportFileObjForm(changetoResTreeObjForm(actionForm));

        //显示textField的值
        if (selResTreeObj != null && ResWebEnvKit.isModify(this) && selResTreeObj.getSrcVO()!=null) {
        	ReportVO repVO = (ReportVO)selResTreeObj.getSrcVO();

            reportFileObjForm.setName(selResTreeObj.getName());
        	reportFileObjForm.setReportCode(repVO.getCode());
        	reportFileObjForm.setHBIntrade(repVO.isIntrade());
        	//王宇光 2008-3-7添加 添加原因：报表增加单元公式是否允许编辑属性
        	reportFileObjForm.setFormulaIsEdit(repVO.getFormulaIsEdit());
        }
        reportFileObjForm.setLblRepCode(getReportStr()+StringResource.getStringResource("miufo1003804"));//*+编码
    }    
	
	private String getReportStr(){
        String strReportStr = null;
		if(!IIUFOResMngConsants.MODULE_REPORT_MODEL.equals(getModuleID())){
            strReportStr = StringResource.getStringResource("miufopublic131");  //"报表"
        }else{
            strReportStr = ResMngHome.getInstance().getFileNameValue(getModuleID());
        }
        return strReportStr;
	}
	/**
     * Form值校验
     * @param actionForm
     * @return 值校验失败的提示信息集合
     */
     public String[] validate(ActionForm actionForm){
     	if(actionForm == null){
     		return null;
     	}
     	List<String> listStr = new ArrayList<String>();
        ReportForm reportFileObjForm = changetoReportFileObjForm(changetoResTreeObjForm(actionForm));;
     	//检验输入的报表名称
        String strName = reportFileObjForm.getName();
        if(!checkName(strName)){
            listStr.add(StringResource.getStringResource("miufopublic404")); //"名称包含非法字符"
        }
        if(strName != null){
            byte[] bytersName = strName.getBytes();
            if(bytersName.length > CodeNameMaker.MAX_REPNAME_LENGTH){
                listStr.add(StringResource.getStringResource("miufo50rep003"));//报表名称长度超过60(汉字字符占2位)！
            }
        }
        String strRepCode = reportFileObjForm.getReportCode();
        if(strRepCode != null){
            byte[] bytersCode = strRepCode.getBytes();
            if(bytersCode.length >CodeNameMaker.MAX_REPCODE_LENGTH){
                listStr.add(StringResource.getStringResource("miufo50rep001"));//报表编码长度超过30(汉字字符占2位)！
            }
        }
        //检验输入的报表编码
        int nNotValidPos = checkRepCode(strRepCode);
        if(nNotValidPos>=1){
            listStr.add(StringResource.getStringResource("miufo50rep002",new String[]{Integer.valueOf(nNotValidPos).toString()}));//"报表编码的第{0}个字符是不支持的特殊字符"
        }
        
        //#业务规则检验
        //1,报表名称在同一目录里不能重复;2,报表编码全局唯一
        //#web组建优化提交处理：不能保证在validate方法里的getTableSelectID和getTreeSelectID一直有值;改用业务Form里传递过来的值
        String strReportPK = null;
        if(isModify(actionForm)){
            strReportPK = ResMngToolKit.getVOIDByTreeObjectID(reportFileObjForm.getBizTableSelectedID());//reportFileObjForm.getID());//getTableSelectedID());
        }
        
        String strRepDirPK = ResMngToolKit.getVOIDByTreeObjectID(reportFileObjForm.getBizTreeSelectedID());//repVO.getRepDir();//ResMngToolKit.getVOIDByTreeObjectID(getTreeSelectedID());
        try {
            RepFiledImpl.chekRepBizRule(getModuleID(),strReportPK,strName,strRepCode,strRepDirPK);
        } catch (UISrvException e) {
        	AppDebug.debug(e);//@devTools             e.printStackTrace(System.out);
            listStr.add(e.getMessage());
        }
        
        if(listStr.size() > 0){
        	String[] strReturns = new String[listStr.size()];
        	listStr.toArray(strReturns);
        	return strReturns;
        }
        return null;     	
     }
     
     /**
      * 检查报表编码是否包含不支持的特殊字符
      * @param sName
      * @return
      */
     public static int checkRepCode(String strRepCode) {
         if(strRepCode == null || strRepCode.length() <=0){
             return -1;
         }
         
         int nNotValidPos = -1;
        //2004-11-15 liuyy 支持下面10个特殊字符
        char[] arrSurport = { '{', '}', '[', ']', '(', ')', '_', '-', '/', '\\'};

        boolean bSupport = false;
        char[] chars = strRepCode.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char cCode = chars[i];
            if (Character.isLetterOrDigit(cCode)) {
                continue;
            }
            
            bSupport = false;
            for (int j = 0; j < arrSurport.length; j++) {
                if (cCode == arrSurport[j]) {
                    bSupport = true;
                    break;
                }
            }
            
            if(!bSupport){
                nNotValidPos = i+1;
                break;
            }
        }

        return nNotValidPos;
    }
     
     public static boolean checkName(String strName){
     	if(strName == null || strName.length() <=0){
     		return true;
     	}
		//目录名称不能含有'\',added by liulp 2002-06-19 15:09
		//; : ? <> * / \ | ～			
		String[] strDirUnsurport = { "\\", "/", ":", "*", "?", "\"", "<",
				">", "|", "~", 
				StringResource.getStringResource("miufopublic503"),//"：", 
				StringResource.getStringResource("miufopublic501"),//"？",
				StringResource.getStringResource("miufopublic504"),//"＊", 
				StringResource.getStringResource("miufopublic502"),//"〉",				
				StringResource.getStringResource("miufo1002466"),
				StringResource.getStringResource("miufo1002456"),
				StringResource.getStringResource("miufo1002457") };

		for (int i = 0; i < strDirUnsurport.length; i++) {
			if (strName.indexOf(strDirUnsurport[i]) >= 0) {
				return false;
			}
		}
		return true;
     }    
}
