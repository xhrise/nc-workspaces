/*
 * �������� 2006-7-4
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.segrep.segdef;

import com.ufida.web.action.ActionForm;
import com.ufida.web.comp.WebTree2ListModel;

/**
 * @author zyjun
 *
 */
public class SegDefEditForm extends ActionForm {
	private   String	strSegDefPK;
	private   String   strSegDefName;
	private   String   strQueryPK;
	/**
	 * Ŀ¼�У�
	 */
	private  String    strDirPK;

	/**
	 * ��֯ά��
	 */
	private  String    strOrgDimPK;
	/**
	 * �ֲ���������
	 */
	private  String    strOrgDimField;
	/**
	 * �Է���֯ά��
	 */
	private  String    strTradeOrgDimPK;
	/**
	 * �Է���֯ά�Ȼ�������
	 */
	private  String    strTradeOrgDimField;
	/**
	 * ��Ŀά��
	 */
	private  String    strSegItemDimPK;
	/**
	 * �ֲ�����
	 */
	private  String    strSegReportPK;
	/**
	 * �ֲ������ѯ
	 */
	private  String    strSegReportQueryPK;
	
	private  String[]	strOrgDimMembers;
	
	private  String 	strSegQueryDirPK;
	private  String		strSegReportDirPK;
	private  String		strSegQueryName;
	private  String		strSegReportName;
	
	private  WebTree2ListModel	listModel;
	private  String[][]	strDimItems;
	private  String[][]	strOrgDimFieldItems;
	private  String[][]	strTradeDimFieldItems;

	private  String		strQueryName;
	private  String		strSegQueryDirName;
	private  String     strSegRepDirName;
	
	
	public String getDirPK() {
		return strDirPK;
	}
	public void setDirPK(String strDirPK) {
		this.strDirPK = strDirPK;
	}
	public String getOrgDimField() {
		return strOrgDimField;
	}
	public void setOrgDimField(String strOrgDimField) {
		this.strOrgDimField = strOrgDimField;
	}
	public String[] getOrgDimMembers() {
		return strOrgDimMembers;
	}
	public void setOrgDimMembers(String[] strOrgDimMembers) {
		this.strOrgDimMembers = strOrgDimMembers;
	}
	public String getOrgDimPK() {
		return strOrgDimPK;
	}
	public void setOrgDimPK(String strOrgDimPK) {
		this.strOrgDimPK = strOrgDimPK;
	}
	public String getQueryPK() {
		return strQueryPK;
	}
	public void setQueryPK(String strQueryPK) {
		this.strQueryPK = strQueryPK;
	}
	public String getSegDefName() {
		return strSegDefName;
	}
	public void setSegDefName(String strSegDefName) {
		this.strSegDefName = strSegDefName;
	}
	public String getSegDefPK() {
		return strSegDefPK;
	}
	public void setSegDefPK(String strSegDefPK) {
		this.strSegDefPK = strSegDefPK;
	}
	public String getSegItemDimPK() {
		return strSegItemDimPK;
	}
	public void setSegItemDimPK(String strSegItemDimPK) {
		this.strSegItemDimPK = strSegItemDimPK;
	}
	public String getSegQueryDirPK() {
		return strSegQueryDirPK;
	}
	public void setSegQueryDirPK(String strSegQueryDirPK) {
		this.strSegQueryDirPK = strSegQueryDirPK;
	}
	public String getSegQueryName() {
		return strSegQueryName;
	}
	public void setSegQueryName(String strSegQueryName) {
		this.strSegQueryName = strSegQueryName;
	}
	public String getSegReportDirPK() {
		return strSegReportDirPK;
	}
	public void setSegReportDirPK(String strSegReportDirPK) {
		this.strSegReportDirPK = strSegReportDirPK;
	}
	public String getSegReportName() {
		return strSegReportName;
	}
	public void setSegReportName(String strSegReportName) {
		this.strSegReportName = strSegReportName;
	}
	public String getSegReportPK() {
		return strSegReportPK;
	}
	public void setSegReportPK(String strSegReportPK) {
		this.strSegReportPK = strSegReportPK;
	}
	public String getSegReportQueryPK() {
		return strSegReportQueryPK;
	}
	public void setSegReportQueryPK(String strSegReportQueryPK) {
		this.strSegReportQueryPK = strSegReportQueryPK;
	}
	public String getTradeOrgDimField() {
		return strTradeOrgDimField;
	}
	public void setTradeOrgDimField(String strTradeOrgDimField) {
		this.strTradeOrgDimField = strTradeOrgDimField;
	}
	public String getTradeOrgDimPK() {
		return strTradeOrgDimPK;
	}
	public void setTradeOrgDimPK(String strTradeOrgDimPK) {
		this.strTradeOrgDimPK = strTradeOrgDimPK;
	}
	
	public WebTree2ListModel getListModel() {
		return listModel;
	}
	public void setListModel(WebTree2ListModel listModel) {
		this.listModel = listModel;
	}
	
	public String[][] getDimItems() {
		return strDimItems;
	}
	public void setDimItems(String[][] strDimItems) {
		this.strDimItems = strDimItems;
	}
	
	public String[][] getOrgDimFieldItems() {
		return strOrgDimFieldItems;
	}
	public void setOrgDimFieldItems(String[][] strDimFieldItems) {
		this.strOrgDimFieldItems = strDimFieldItems;
	}
	
	public String[][] getTradeDimFieldItems() {
		return strTradeDimFieldItems;
	}
	public void setTradeDimFieldItems(String[][] strDimFieldItems) {
		this.strTradeDimFieldItems = strDimFieldItems;
	}
	public String getQueryName() {
		return strQueryName;
	}
	public void setQueryName(String strQueryName) {
		this.strQueryName = strQueryName;
	}
	public String getSegQueryDirName() {
		return strSegQueryDirName;
	}
	public void setSegQueryDirName(String strSegQueryDirName) {
		this.strSegQueryDirName = strSegQueryDirName;
	}
	public String getSegRepDirName() {
		return strSegRepDirName;
	}
	public void setSegRepDirName(String strSegRepDirName) {
		this.strSegRepDirName = strSegRepDirName;
	}
	
}
