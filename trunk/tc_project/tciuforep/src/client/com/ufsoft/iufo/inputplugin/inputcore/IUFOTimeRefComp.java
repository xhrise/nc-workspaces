package com.ufsoft.iufo.inputplugin.inputcore;

import java.util.Date;

import nc.util.iufo.reportdata.DateInputCheckUtil;

import com.ufsoft.table.re.RefTextField;
import com.ufsoft.table.re.TimeRefComp;

public class IUFOTimeRefComp extends TimeRefComp{
	
	private static final long serialVersionUID = 1L;
	
	private String strKeyTimeType=null;
	public IUFOTimeRefComp(String keyTimeType,Date defaultDate, RefTextField ref) {
		super(defaultDate, ref);
		this.strKeyTimeType=keyTimeType;
	}
	@Override
	public Object getValidateValue(String text) {
		if(DateInputCheckUtil.isValidDate(strKeyTimeType, text)){
			text=DateInputCheckUtil.convert2StandDate(strKeyTimeType, text);
		}else{
			return null;
		}
		return text;
	}
	@Override
	public void setDefaultValue(Object obj) {
		// TODO Auto-generated method stub
		super.setDefaultValue(obj);
	}

	
	

}
