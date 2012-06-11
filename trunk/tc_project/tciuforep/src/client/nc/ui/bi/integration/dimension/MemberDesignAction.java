/**
 * Action1.java  5.0 
 * 
 * WebDeveloper自动生成.
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
 * 类作用描述文字 ll 2006-01-17
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
	 * @i18n mbidim00047=已达到最大级次数，不能再新建级次
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
	 * 设置自定义属性相关的数据信息
	 */
	private void initExPropDataForm(String dimID, IExPropDataObj extProperty,IExPropDataEditForm exPropDataEditForm,boolean bUpdate) {
	    //设置自定义属性模块ID
	    exPropDataEditForm.setExPropModuleID(getExPropModuleID());
	    //设置自定义属性的定义信息和值
	    ExPropertyVO[] allInputFormalExPropVOs = getAllInputFormalExPropVOs(dimID);
	    doSetSelfExPropsValue(exPropDataEditForm,bUpdate,allInputFormalExPropVOs,extProperty);
	}
	
	 /**
     * 设置自定义属性的值
     * 
     * @param exPropDataEditForm
     * @param bModify
     * @param allInputFormalExProps
     * @param exPropDataObj
     */
    private static void doSetSelfExPropsValue(
            IExPropDataEditForm exPropDataEditForm, boolean bModify,
            ExPropertyVO[] allInputFormalExProps, IExPropDataObj exPropDataObj) {
        //设置自定义数定义
        exPropDataEditForm.setExPropVOs(allInputFormalExProps);
        //获得数据并设置自定义属性值（对应定义的顺序）
        String[] strValues = null;
        String[] showValues = null;
        if (bModify && exPropDataObj != null) {
            //根据自定义属性信息设置属性值
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
                exPropDataEditForm.setExPropDisValues(showValues);//TODO 参照类型有显示值和后台值区分                
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
			// 构造vo
			DimMemberVO vo = buildVO(form, dimID, srv, dimVO.getTablename());
			vo.setDimid(dimID);
			
			// 新增
			if (isModify) {
				srv.update(new DimMemberVO[] { vo });
			} else {
				srv.create(new DimMemberVO[] { vo });			
			}
	}
	/*
	 * 构建vo
	 */
	private DimMemberVO buildVO(MemberDesignForm form,String dimID, DimMemberSrv srv,String dim_tablename) {
		String sName = form.getMemname();
		String sCode = form.getMemcode();
		String sRule = form.getCalattr();
		String editType = form.getUIType();
		boolean isModify = (editType != null && editType.equals(EDITTYPE_MODIFY));	
		
		// 构造vo
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
        	throw new WebException("miufounit00008");//无法得到自定义单位结构信息
        }
 
		// 根据父结点生成级次属性
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
	 * @i18n mbidim00048=成员名称
	 * @i18n mbidim00049=成员编码
	 * @i18n mbidim00050=成员编码已经存在
	 * @i18n mbidim00051=成员名称已经存在
	 */
	@SuppressWarnings("unchecked")
	public void validate(ArrayList errors, ActionForm actionForm) {
		//判断名称和编码是否重复		
		MemberDesignForm	form = (MemberDesignForm)actionForm;
		String dimID = getRequestParameter(KEY_DIM_ID);
		DimensionVO dimVO = (DimensionVO) DimensionSrv.getByID(dimID);
		DimMemberSrv srv = new DimMemberSrv(dimVO);
		
		if (!InputvalueCheck.isValidName(form.getMemname())) {
			errors.add(StringResource.getStringResource("mbidim00048") + StringResource.getStringResource("miufo1003460")); // "存在非法字符"
		}
//		if (!InputvalueCheck.isValidName(form.getMemcode())) {
//			errors.add(StringResource.getStringResource("mbidim00049") + StringResource.getStringResource("miufo1003460")); // "存在非法字符"
//		}
		// 校验成员编码,比InputvalueCheck.isValidName多了两个字符/.
		if (!isValidCode(form.getMemcode(), Arrays.asList('_', '-', ' ', '(', ')', '/', '.'))) {
			errors.add(StringResource.getStringResource("mbidim00049")
					+ StringResource.getStringResource("miufo1003460")); // "存在非法字符"
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
	 * @i18n mbidim00052=参照的维度不存在
	 * @i18n mbidim00053=参照的字段不存在
	 * @i18n mbidim00054=无法得到维度结构字段信息
	 */
	@SuppressWarnings("unchecked")
	private void  checkExPropValues(ArrayList aryError){
        // 其他属性, 检查参照内容是否存在		
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
	    	                    	aryError.add(StringResource.getStringResource("mbidim00052"));//参照的维度不存在	                    	
	    	                    }else{	    	                    	
	    	                    	 String strValue = getRequestParameter(exProps[i].getDBColumnName());
	    	                    	 if((strValue != null) && (strValue.trim().length() != 0)){
	    	                    		 DimMemberVO[] vos = null;
		    	                    	 DimMemberSrv dimMemSrv = new DimMemberSrv(dimVO);
	    	                    		 try{	    	                    			 
	 	    	                    		vos = dimMemSrv.getByID(new String[]{strValue});	                    		 
	 	    	                    	 }catch(Exception e){
	 	    	 	                    	AppDebug.debug(e);
	 	    	 	                    	//aryError.add("参照的字段不存在");//参照的字段不存在
	 	    	 	                    }
	 	    	                    	 if(vos == null || vos.length == 0|| vos[0] == null){
	 	    	                    		 aryError.add(StringResource.getStringResource("mbidim00053"));//参照的字段不存在
	 	    	                    	 }
	    	                    	 }	    	                    	 
	    	                    }	    
	                    	}
	                    	
	                    }catch(Exception e){
	                    	AppDebug.debug(e);
	                    	//aryError.add("无法得到参照维度");//无法得到参照维度
	                    }
	                    
	                    
	                }
	        	}
	        }
    	}catch(Exception e){
    		AppDebug.debug(e);
    		aryError.add(StringResource.getStringResource("mbidim00054"));//无法得到维度结构字段信息");
    	}      	
    }
	
	/**
	 * 关联Form
	 * 
	 */
	public String getFormName() {
		return MemberDesignForm.class.getName();
	}
	/**
	 * 允许的字符范围是英文字母,数字以及指定的字符. 此方法应该放在InputvalueCheck类中
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
 