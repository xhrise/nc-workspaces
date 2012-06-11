package com.ufsoft.iufo.inputplugin.ref;

import java.util.Hashtable;

/**
 * 参照参数对象
 * @created by wangyga at 2008-12-26,下午12:36:32
 *
 */

public class RefParam {

	/**
	 * 参照类型
	 */
	public static final int COIN_REF_CODE = 1;
	
	public static final int UNIT_REF_CODE = 2;
	
	public static final int TIME_REF_CODE = 3;
	
	public static final int ACC_REF_CODE = 4;
	
//	public static final int ACC_MONTH_REF_CODE = 5;
//	
//	public static final int ACC_SEASON_REF_CODE = 6;
	
	public int iRefType = UNIT_REF_CODE;
	
	/**
	 * 参照属性参数
	 */
	public static final String ORG_PK = "key_ORG_PK";
	public static final String CUR_UNIT_CODE = "key_CUR_UNIT_CODE"; 
	
	public static final String ACC_PREIOD_PK = "key_ACC_PREIOD_PK";
	public static final String ACC_PERIOD_TYPE = "key_ACC_PERIOD_TYPE";
	
	private Hashtable<String, Object> m_attribute = new Hashtable<String, Object>();
	
	public RefParam(int iRefType){
		this.iRefType = iRefType;
	}
	
	public void setAttribute(String key, Object value){
		if(key == null || value == null){
			return;
		} 
		m_attribute.put(key, value);
	 
    }
	
	public Object getAttribute(String key){
		if(key == null){
			return null;
		}
    	return m_attribute.get(key);
    }
	
	public void removeAttribute(String key){
		if(key == null){
			return;
		}
    	m_attribute.remove(key);
    }

	public int getRefType() {
		return iRefType;
	}
	 
}
