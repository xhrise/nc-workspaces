/**
 * Action1.java  5.0 
 * 
 * WebDeveloper�Զ�����.
 * 2006-01-17
 */
package nc.ui.bi.integration.dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import nc.itf.bi.exproperty.IBIExPropConstants;
import nc.ui.iufo.exproperty.ExPropException;
import nc.ui.iufo.exproperty.ExPropOperator;
import nc.ui.iufo.exproperty.IExPropDataEditForm;
import nc.ui.iufo.exproperty.IExPropOperator;
import nc.util.iufo.pub.IDMaker;
import nc.vo.bi.integration.dimension.DimMemberSrv;
import nc.vo.bi.integration.dimension.DimMemberVO;
import nc.vo.bi.integration.dimension.DimRescource;
import nc.vo.bi.integration.dimension.DimensionSrv;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.iufo.exproperty.ExPropertyVO;
import nc.vo.iufo.exproperty.IExPropDataObj;
import nc.vo.iufo.pub.InputvalueCheck;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.action.ErrorForward;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.web.DialogAction;

/**
 * �������������� ll 2006-01-17
 */
public class MemberDesignAction extends DialogAction {

	static final String KEY_DIM_ID = "key_of_dimension";

	static final String KEY_EDIT_TYPE = "key_of_edit_type";

	public static final String METHOD_EXCUTE = "execute";
	public static final String METHOD_UPDATE = "update";

	public static final String METHOD_SUBMIT = "submit";
	public static final String METHOD_NEXT = "next";
	
	public static final String EDITTYPE_MODIFY = "modify";
	public static final String EDITTYPE_NEW = "new";

	/**
	 * <MethodDescription> ll 2006-01-17
	 * @i18n mbidim00047=�Ѵﵽ��󼶴������������½�����
	 */
	public ActionForward execute(ActionForm actionForm) throws WebException {		
		MemberDesignForm form = (MemberDesignForm) actionForm;
		
		String dimID = getRequestParameter(KEY_DIM_ID);
		DimensionVO dimVO = (DimensionVO) DimensionSrv.getByID(dimID);
		String memID = DimUIToolKit.getMemberIDByTreeID(getTreeSelectedID());
		DimMemberVO memVO = (new DimMemberSrv(dimVO)).getByID(new String[] { memID })[0];

		if(memVO.getDepth() >= DimRescource.INT_MAX_FLDPRE_NUMBER-1){
			return new ErrorForward(StringResource.getStringResource("mbidim00047"));
		}
		
		form.setUIType(EDITTYPE_NEW);
		form.setSelectedTreeID(getTreeSelectedID());
		
		initExPropDataForm(dimID, null,(IExPropDataEditForm)form,false);
	
		ActionForward actionForward = new ActionForward(MemberDesignDlg.class.getName());
		return actionForward;
	}
	public ActionForward next(ActionForm actionForm) throws WebException {	
		doSubmit(actionForm);
		MemberDesignForm form = (MemberDesignForm) actionForm;
		form.setMemcode(null);
		form.setMemname(null);
		form.setSelectedTreeID(getRequestParameter("selectedTreeID"));
		return execute(actionForm);
	}
	
	public ActionForward update(ActionForm actionForm) throws WebException {		
		MemberDesignForm form = (MemberDesignForm) actionForm;
		
		String dimID = getRequestParameter(KEY_DIM_ID);
		DimensionVO dimVO = (DimensionVO) DimensionSrv.getByID(dimID);
		String memID = getTableSelectedID();
		DimMemberVO memVO = (new DimMemberSrv(dimVO)).getByID(new String[] { memID })[0];		
		
		form.setUIType(EDITTYPE_MODIFY);
		form.setMemcode(memVO.getMemcode());
		form.setMemname(memVO.getMemname());
		form.setCalattr(String.valueOf(memVO.getCalattr()));	
		form.setSelectedTreeID(getTreeSelectedID());
    	initExPropDataForm(dimID,memVO,(IExPropDataEditForm)form,true);	    	
		
		ActionForward actionForward = new ActionForward(MemberDesignDlg.class.getName());
		return actionForward;
	}

	/**
	 * �����Զ���������ص�������Ϣ
	 */
	private void initExPropDataForm(String dimID, IExPropDataObj extProperty,IExPropDataEditForm exPropDataEditForm,boolean bUpdate) {
	    //�����Զ�������ģ��ID
	    exPropDataEditForm.setExPropModuleID(getExPropModuleID());
	    //�����Զ������ԵĶ�����Ϣ��ֵ
	    ExPropertyVO[] allInputFormalExPropVOs = getAllInputFormalExPropVOs(dimID);
	    doSetSelfExPropsValue(exPropDataEditForm,bUpdate,allInputFormalExPropVOs,extProperty);
	}
	
	 /**
     * �����Զ������Ե�ֵ
     * 
     * @param exPropDataEditForm
     * @param bModify
     * @param allInputFormalExProps
     * @param exPropDataObj
     */
    private static void doSetSelfExPropsValue(
            IExPropDataEditForm exPropDataEditForm, boolean bModify,
            ExPropertyVO[] allInputFormalExProps, IExPropDataObj exPropDataObj) {
        //�����Զ���������
        exPropDataEditForm.setExPropVOs(allInputFormalExProps);
        //������ݲ������Զ�������ֵ����Ӧ�����˳��
        String[] strValues = null;
        String[] showValues = null;
        if (bModify && exPropDataObj != null) {
            //�����Զ���������Ϣ��������ֵ
            if (allInputFormalExProps != null) {
                strValues = new String[allInputFormalExProps.length];
                showValues = new String[allInputFormalExProps.length];                
                for (int i = 0; i < strValues.length; i++) {
                    strValues[i] = exPropDataObj.getPropValue(allInputFormalExProps[i] .getDBColumnName());
                    showValues[i] = strValues[i];
                    if(allInputFormalExProps[i].getType() == ExPropertyVO.TYPE_REF){
                    	try {
    						DimensionVO dimVO = DimensionSrv.getByID(allInputFormalExProps[i].getRefTypePK());
    						DimMemberSrv srv = new DimMemberSrv(dimVO);
    						DimMemberVO[] memVOs = srv.getByID(new String[]{strValues[i]});
    						showValues[i] = memVOs[0].getLabel();
    					} catch (Exception e) {						
    						AppDebug.debug(e);
    					}
                    }                    
                }
                exPropDataEditForm.setExPropValues(strValues);
                exPropDataEditForm.setExPropDisValues(showValues);//TODO ������������ʾֵ�ͺ�ֵ̨����                
            }
        }
    }
    
    
	public static ExPropertyVO[]  getAllInputFormalExPropVOs(String dimID){
	    IExPropOperator exPropOper = getExPropOper();
        try {
            return exPropOper.loadAllInputFormalExProps(dimID);
        } catch (ExPropException e) {
        	AppDebug.debug(e);//@devTools             e.printStackTrace(System.out);
            throw new WebException(e.getExResourceId());
        }
	}
	 
	private static IExPropOperator getExPropOper(){
	    return ExPropOperator.getExPropOper(getExPropModuleID());
	}
    private static String getExPropModuleID(){
        return IBIExPropConstants.EXPROP_MODULE_DIMENSION;
    }
	/**
	 * <MethodDescription> ll 2006-01-17
	 */
	public ActionForward submit(ActionForm actionForm) throws WebException {
		doSubmit(actionForm);
		return new CloseForward(CloseForward.CLOSE_REFRESH_PARENT_All);
	}

	private void doSubmit(ActionForm actionForm){
		MemberDesignForm form = (MemberDesignForm) actionForm;
		String editType = form.getUIType();
		boolean isModify = (editType != null && editType.equals(EDITTYPE_MODIFY));

			String dimID = getRequestParameter(KEY_DIM_ID);
			DimensionVO dimVO = (DimensionVO) DimensionSrv.getByID(dimID);
			DimMemberSrv srv = new DimMemberSrv(dimVO);
			// ����vo
			DimMemberVO vo = buildVO(form, dimID, srv, dimVO.getTablename());
			vo.setDimid(dimID);
			
			// ����
			if (isModify) {
				srv.update(new DimMemberVO[] { vo });
			} else {
				srv.create(new DimMemberVO[] { vo });			
			}
	}
	/*
	 * ����vo
	 */
	private DimMemberVO buildVO(MemberDesignForm form,String dimID, DimMemberSrv srv,String dim_tablename) {
		String sName = form.getMemname();
		String sCode = form.getMemcode();
		String sRule = form.getCalattr();
		String editType = form.getUIType();
		boolean isModify = (editType != null && editType.equals(EDITTYPE_MODIFY));	
		
		// ����vo
		DimMemberVO vo = null;
		DimMemberVO voParent = null;
	
		voParent = srv.getByID(new String[] { DimUIToolKit.getMemberIDByTreeID(getTreeSelectedID()) })[0];
		
		if (isModify == false) {
			vo = new DimMemberVO();
			vo.setMemberID(IDMaker.makeID(20));			
					
		} else {	
			vo = srv.getByID(new String[] { getTableSelectedID() })[0];			
//			vo = selectMemVO;
//			if(vo.getDepth().intValue()>0)
//				voParent = (DimMemberVO) srv.getByDrillType(vo, IMultiDimConst.DATA_DRILLUP, null)[0];
		}

		vo.setMemname(sName);
		vo.setMemcode(sCode);
		vo.setCalattr(new Integer(sRule));
		vo.setTableName(dim_tablename);
		
		try{
	    	ExPropertyVO[]	exProps = getAllInputFormalExPropVOs(dimID);	    	
	    	if( exProps != null ){	    	
	    		for( int i=0; i<exProps.length; i++ ){
	    			vo.setExPropValue(exProps[i].getDBColumnName(),getRequestParameter(exProps[i].getDBColumnName()));
	    		}
	    	}	    	
    	}catch(Exception e){
        	AppDebug.debug(e);
        	throw new WebException("miufounit00008");//�޷��õ��Զ��嵥λ�ṹ��Ϣ
        }
 
		// ���ݸ�������ɼ�������
		Object oLvl = null;
		Object oRule = null;
		int iLen = voParent== null?-1:voParent.getDepth().intValue();
		int iDepth = iLen + 1;
		for (int i = 0; i <= iLen; i++) {
			oLvl = voParent.getLevels()[i];
			vo.getLevels()[i] = (String) oLvl;
			if (i == 0 || i == 1)
				continue;
			oRule = voParent.getCalcAttrs()[i - 1];
			vo.getCalcAttrs()[i - 1] = new Integer(((Integer) oRule).intValue() * Integer.parseInt(sRule));
		}
		if(iDepth>0)//??
			vo.getCalcAttrs()[iDepth - 1] = new Integer(sRule);
		if (!isModify) {
			vo.getLevels()[iDepth] = vo.getID();
			vo.getCalcAttrs()[iDepth] = new Integer(sRule);
			vo.setDepth(new Integer(iDepth));
		}

		return vo;

	}


	/**
	 * @i18n mbidim00048=��Ա����
	 * @i18n mbidim00049=��Ա����
	 * @i18n mbidim00050=��Ա�����Ѿ�����
	 * @i18n mbidim00051=��Ա�����Ѿ�����
	 */
	@SuppressWarnings("unchecked")
	public void validate(ArrayList errors, ActionForm actionForm) {
		//�ж����ƺͱ����Ƿ��ظ�		
		MemberDesignForm	form = (MemberDesignForm)actionForm;
		String dimID = getRequestParameter(KEY_DIM_ID);
		DimensionVO dimVO = (DimensionVO) DimensionSrv.getByID(dimID);
		DimMemberSrv srv = new DimMemberSrv(dimVO);
		
		if (!InputvalueCheck.isValidName(form.getMemname())) {
			errors.add(StringResource.getStringResource("mbidim00048") + StringResource.getStringResource("miufo1003460")); // "���ڷǷ��ַ�"
		}
//		if (!InputvalueCheck.isValidName(form.getMemcode())) {
//			errors.add(StringResource.getStringResource("mbidim00049") + StringResource.getStringResource("miufo1003460")); // "���ڷǷ��ַ�"
//		}
		// У���Ա����,��InputvalueCheck.isValidName���������ַ�/.
		if (!isValidCode(form.getMemcode(), Arrays.asList('_', '-', ' ', '(', ')', '/', '.'))) {
			errors.add(StringResource.getStringResource("mbidim00049")
					+ StringResource.getStringResource("miufo1003460")); // "���ڷǷ��ַ�"
		}

		String memID = null;
		if(EDITTYPE_MODIFY.equalsIgnoreCase(form.getUIType()))
			memID = getTableSelectedID();
		if( !srv.isUsableCodeName(form.getMemcode(), true, memID)){
			errors.add(StringResource.getStringResource(StringResource.getStringResource("mbidim00050")));
		}
		if( !srv.isUsableCodeName(form.getMemname(), false, memID)){
			errors.add(StringResource.getStringResource(StringResource.getStringResource("mbidim00051")));
		}
		checkExPropValues(errors);		
	}
	
	/**
	 * @i18n mbidim00052=���յ�ά�Ȳ�����
	 * @i18n mbidim00053=���յ��ֶβ�����
	 * @i18n mbidim00054=�޷��õ�ά�Ƚṹ�ֶ���Ϣ
	 */
	@SuppressWarnings("unchecked")
	private void  checkExPropValues(ArrayList aryError){
        // ��������, �����������Ƿ����		
    	try{
    		String dimID = getRequestParameter(KEY_DIM_ID);
    		ExPropertyVO[]  exProps = getAllInputFormalExPropVOs(dimID);
	        if( exProps != null ){
	        	for( int i=0; i< exProps.length; i++ ){
	                if (exProps[i].getType() == ExPropertyVO.TYPE_REF) {
	                	DimensionVO dimVO = null;
	                    try{	                    	
	                    	String	strRefDimID = exProps[i].getRefTypePK();
	                    	if((strRefDimID != null) && (strRefDimID.trim().length() != 0)){
	                    		dimVO = (DimensionVO) DimensionSrv.getByID(strRefDimID);	
	                    		if(dimVO == null){
	    	                    	aryError.add(StringResource.getStringResource("mbidim00052"));//���յ�ά�Ȳ�����	                    	
	    	                    }else{	    	                    	
	    	                    	 String strValue = getRequestParameter(exProps[i].getDBColumnName());
	    	                    	 if((strValue != null) && (strValue.trim().length() != 0)){
	    	                    		 DimMemberVO[] vos = null;
		    	                    	 DimMemberSrv dimMemSrv = new DimMemberSrv(dimVO);
	    	                    		 try{	    	                    			 
	 	    	                    		vos = dimMemSrv.getByID(new String[]{strValue});	                    		 
	 	    	                    	 }catch(Exception e){
	 	    	 	                    	AppDebug.debug(e);
	 	    	 	                    	//aryError.add("���յ��ֶβ�����");//���յ��ֶβ�����
	 	    	 	                    }
	 	    	                    	 if(vos == null || vos.length == 0|| vos[0] == null){
	 	    	                    		 aryError.add(StringResource.getStringResource("mbidim00053"));//���յ��ֶβ�����
	 	    	                    	 }
	    	                    	 }	    	                    	 
	    	                    }	    
	                    	}
	                    	
	                    }catch(Exception e){
	                    	AppDebug.debug(e);
	                    	//aryError.add("�޷��õ�����ά��");//�޷��õ�����ά��
	                    }
	                    
	                    
	                }
	        	}
	        }
    	}catch(Exception e){
    		AppDebug.debug(e);
    		aryError.add(StringResource.getStringResource("mbidim00054"));//�޷��õ�ά�Ƚṹ�ֶ���Ϣ");
    	}      	
    }
	
	/**
	 * ����Form
	 * 
	 */
	public String getFormName() {
		return MemberDesignForm.class.getName();
	}
	/**
	 * ������ַ���Χ��Ӣ����ĸ,�����Լ�ָ�����ַ�. �˷���Ӧ�÷���InputvalueCheck����
	 * 
	 * @param sName
	 *            java.lang.String
	 * @param cArray
	 *            char[]
	 */
	public final static boolean isValidCode(String sCode, Collection validChars) {
		if (sCode == null) {
			return false;
		} else {
			char[] chars = sCode.toCharArray();
			char cCur = ' ';
			for (int i = 0; i < chars.length; i++) {
				cCur = chars[i];
				if (!Character.isLetterOrDigit(cCur)) {
					if (!validChars.contains(cCur))
						return false;
				}
			}
		}
		return true;
	}
}
 