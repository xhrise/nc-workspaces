package com.ufsoft.iufo.inputplugin.inputcore;

import java.util.Date;

import com.ufsoft.report.resource.ResConst;
import com.ufsoft.table.re.RefTextField;

public class TimeRefTextField extends RefTextField {
	private static final long serialVersionUID = -812563085235920766L;
	private Date defaultDate=null;
	private String keyTimeType=null;
	
	public TimeRefTextField(String keyTimeType,Date defaultDate){
		super();
		setRightIcon(ResConst.getImageIcon("reportcore/date_down.gif"));
		this.keyTimeType=keyTimeType;
		this.defaultDate=defaultDate;
	}
	
	protected void initRef() {
		super.initRef();
		setRefComp(new IUFOTimeRefComp(keyTimeType,defaultDate,this),defaultDate);
	}

	
}
