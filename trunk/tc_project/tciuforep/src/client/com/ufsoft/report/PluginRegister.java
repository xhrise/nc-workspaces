package com.ufsoft.report;

import java.util.Hashtable;

public abstract class PluginRegister {

	//属主报表
	private UfoReport _report = null;

	/**
	 * 扩展属性
	 */
	private Hashtable params = null;


	public PluginRegister() { 
	}
	
	public PluginRegister(Hashtable params) { //UfoReport report, 
		this.params = params;
	}

	protected UfoReport getReport() {
		return _report;
	}
	
	public void setReport(UfoReport report){
		_report = report;
	}

	protected Object getParamter(Object key) {
		if(params == null){
			return null;
		}
		return params.get(key);
	}

	public abstract void register();

}
