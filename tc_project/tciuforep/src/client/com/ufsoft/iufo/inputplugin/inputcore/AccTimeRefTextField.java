package com.ufsoft.iufo.inputplugin.inputcore;

import nc.vo.iufo.keydef.KeyVO;

import com.ufsoft.table.re.RefTextField;

/**
 * 
 * @author wangyga
 *
 */
public class AccTimeRefTextField extends RefTextField{
	private static final long serialVersionUID = 1L;
	
	private String strDefaultDate=null;
	private String strAccPeriodPK=null;
	private String strAccPeriodType=null;

	public AccTimeRefTextField(String strDefaultDate,String strAccPreiodPk, String strAccPeriodType){
		super();
		this.strDefaultDate=strDefaultDate;
		this.strAccPeriodPK=strAccPreiodPk;
		this.strAccPeriodType=strAccPeriodType;	
	}

	protected void initRef() {
		super.initRef();
		
		if(KeyVO.ACC_YEAR_PK.equals(strAccPeriodType)){
			setRefComp(new AccPeriodYearRefComp(strAccPeriodPK,strAccPeriodType,strDefaultDate),strDefaultDate);
		}else{
			setRefComp(new AccPeriodRefComp(strAccPeriodPK,strAccPeriodType,strDefaultDate,null),strDefaultDate);
		}	
	}
	
	
}
