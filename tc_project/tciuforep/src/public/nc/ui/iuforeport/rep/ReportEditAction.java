/*
 * �������� 2006-1-17
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
 * ����ģ��༭�����Action
 * @author liulp
 *
 */
public class ReportEditAction extends ResEditObjAction{
	public ReportEditAction(){
		super();
	}

	/* ���� Javadoc��
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#getSaveActionName()
	 */
	protected String getSaveActionName() {
		return ReportEditAction.class.getName();
	}

	/* ���� Javadoc��
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#getLabelObjName()
	 */
	protected String getLabelObjName() {
		return (getReportStr() + StringResource.getStringResource("miufo1001154"));  //"����"
	}

	/* ���� Javadoc��
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#getLabelObjNote()
	 */
	protected String getLabelObjNote() {
		return (getReportStr() + StringResource.getStringResource("miufo1002462"));  //"˵��"
	}

	/* ���� Javadoc��
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#isEditable(nc.vo.iufo.resmng.uitemplate.IResTreeObject)
	 */
	protected boolean isEditable(IResTreeObject resTreeObj) {
		return true;
	}

	/* ���� Javadoc��
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#doGetUpdateVO(nc.ui.iufo.resmng.uitemplate.IResTreeObjForm, nc.vo.pub.ValueObject)
	 */
	protected ValueObject doGetUpdateVO(IResTreeObjForm resTreeObjForm, ValueObject srcVO) {
		ReportForm reportFileObjForm = changetoReportFileObjForm(resTreeObjForm);
		
		ReportVO repVo = (ReportVO)srcVO.clone();
         //�����޸�ʱ��
         String date = DateUtil.getCurTime();
         repVo.setModifiedTime(date);
         //���汨�����ơ�˵��
         repVo.setName(resTreeObjForm.getName());
         repVo.setNote(resTreeObjForm.getNote());
         //#���汨�����
         repVo.setCode(reportFileObjForm.getReportCode());         
         //�Ƿ��ڲ����ױ�
         repVo.setIntrade(reportFileObjForm.isHBIntrade());
         //����� 2008-3-7��� ���ԭ�򣺱������ӵ�Ԫ��ʽ�Ƿ�����༭����
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

	/* ���� Javadoc��
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#doGetNewVO(java.lang.String, nc.ui.iufo.resmng.uitemplate.IResTreeObjForm, nc.ui.iufo.resmng.common.ResWebParam)
	 */
	protected ValueObject doGetNewVO(String strParentVOPK, IResTreeObjForm resTreeObjForm, ResWebParam resWebParam) {
		ReportVO repVo = AnaRepEditAction.getBaseNewReportVO(strParentVOPK,resTreeObjForm,resWebParam);
		
		//��������
		repVo.setRepType(ReportDirVO.REPORT_DIR_TYPE_DEFAULT);
		repVo.setModel(getModuleID().equals(IIUFOResMngConsants.MODULE_REPORT_MODEL));
		
    	ReportForm reportFileObjForm = changetoReportFileObjForm(resTreeObjForm);
        //#���汨�����
        repVo.setCode(reportFileObjForm.getReportCode());
        
        repVo.setIntrade(reportFileObjForm.isHBIntrade());
        
        //����� 2008-3-7��� ���ԭ�򣺱������ӵ�Ԫ��ʽ�Ƿ�����༭����
        repVo.setFormulaIsEdit(reportFileObjForm.getFormulaIsEdit());
        
        return repVo;
	}
	/**
	 * �õ�Form����
	 * ˵���� ��д���෽��
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#getFormName()
	 */
    public String getFormName() {
        return ReportForm.class.getName();
     }
    /**
     * �õ��½����޸ı���Ľ�������
	 * ˵���� ��д���෽��
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#getExecuteUI()
     */
	protected String getExecuteUI() {
		return ReportEditUI.class.getName();
	}
    /**
	 * ��д���෽��
     * @param form
     * @param selResTreeObj
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#initFormValue(ActionForm, IResTreeObject)
     */
    protected void initFormValue(ActionForm actionForm, IResTreeObject selResTreeObj) {
    	super.initFormValue(actionForm,selResTreeObj);
    	ReportForm reportFileObjForm = changetoReportFileObjForm(changetoResTreeObjForm(actionForm));

        //��ʾtextField��ֵ
        if (selResTreeObj != null && ResWebEnvKit.isModify(this) && selResTreeObj.getSrcVO()!=null) {
        	ReportVO repVO = (ReportVO)selResTreeObj.getSrcVO();

            reportFileObjForm.setName(selResTreeObj.getName());
        	reportFileObjForm.setReportCode(repVO.getCode());
        	reportFileObjForm.setHBIntrade(repVO.isIntrade());
        	//����� 2008-3-7��� ���ԭ�򣺱������ӵ�Ԫ��ʽ�Ƿ�����༭����
        	reportFileObjForm.setFormulaIsEdit(repVO.getFormulaIsEdit());
        }
        reportFileObjForm.setLblRepCode(getReportStr()+StringResource.getStringResource("miufo1003804"));//*+����
    }    
	
	private String getReportStr(){
        String strReportStr = null;
		if(!IIUFOResMngConsants.MODULE_REPORT_MODEL.equals(getModuleID())){
            strReportStr = StringResource.getStringResource("miufopublic131");  //"����"
        }else{
            strReportStr = ResMngHome.getInstance().getFileNameValue(getModuleID());
        }
        return strReportStr;
	}
	/**
     * FormֵУ��
     * @param actionForm
     * @return ֵУ��ʧ�ܵ���ʾ��Ϣ����
     */
     public String[] validate(ActionForm actionForm){
     	if(actionForm == null){
     		return null;
     	}
     	List<String> listStr = new ArrayList<String>();
        ReportForm reportFileObjForm = changetoReportFileObjForm(changetoResTreeObjForm(actionForm));;
     	//��������ı�������
        String strName = reportFileObjForm.getName();
        if(!checkName(strName)){
            listStr.add(StringResource.getStringResource("miufopublic404")); //"���ư����Ƿ��ַ�"
        }
        if(strName != null){
            byte[] bytersName = strName.getBytes();
            if(bytersName.length > CodeNameMaker.MAX_REPNAME_LENGTH){
                listStr.add(StringResource.getStringResource("miufo50rep003"));//�������Ƴ��ȳ���60(�����ַ�ռ2λ)��
            }
        }
        String strRepCode = reportFileObjForm.getReportCode();
        if(strRepCode != null){
            byte[] bytersCode = strRepCode.getBytes();
            if(bytersCode.length >CodeNameMaker.MAX_REPCODE_LENGTH){
                listStr.add(StringResource.getStringResource("miufo50rep001"));//������볤�ȳ���30(�����ַ�ռ2λ)��
            }
        }
        //��������ı������
        int nNotValidPos = checkRepCode(strRepCode);
        if(nNotValidPos>=1){
            listStr.add(StringResource.getStringResource("miufo50rep002",new String[]{Integer.valueOf(nNotValidPos).toString()}));//"�������ĵ�{0}���ַ��ǲ�֧�ֵ������ַ�"
        }
        
        //#ҵ��������
        //1,����������ͬһĿ¼�ﲻ���ظ�;2,�������ȫ��Ψһ
        //#web�齨�Ż��ύ�������ܱ�֤��validate�������getTableSelectID��getTreeSelectIDһֱ��ֵ;����ҵ��Form�ﴫ�ݹ�����ֵ
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
      * ��鱨������Ƿ������֧�ֵ������ַ�
      * @param sName
      * @return
      */
     public static int checkRepCode(String strRepCode) {
         if(strRepCode == null || strRepCode.length() <=0){
             return -1;
         }
         
         int nNotValidPos = -1;
        //2004-11-15 liuyy ֧������10�������ַ�
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
		//Ŀ¼���Ʋ��ܺ���'\',added by liulp 2002-06-19 15:09
		//; : ? <> * / \ | ��			
		String[] strDirUnsurport = { "\\", "/", ":", "*", "?", "\"", "<",
				">", "|", "~", 
				StringResource.getStringResource("miufopublic503"),//"��", 
				StringResource.getStringResource("miufopublic501"),//"��",
				StringResource.getStringResource("miufopublic504"),//"��", 
				StringResource.getStringResource("miufopublic502"),//"��",				
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
