/*
 * �������� 2006-4-18
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.bi.dataauth;

import com.ufida.web.action.ActionForm;

/**
 * @author zyjun
 *
 * 
 */
public class DataAuthMngForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private	String		dimPK;
	private	String  	repPK;
	
	private    String[][]  dimItems;
	
	public String getDimPK() {
		return dimPK;
	}
	public void setDimPK(String dimPK) {
		this.dimPK = dimPK;
	}
	public String getRepPK() {
		return repPK;
	}
	public void setRepPK(String repPK) {
		this.repPK = repPK;
	}
	
	public String[][] getDimItems() {
		return dimItems;
	}
	public void setDimItems(String[][] dimItems) {
		this.dimItems = dimItems;
	}
}
