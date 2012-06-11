/*
 * �������� 2006-2-13
 *
 */
package nc.ui.bi.integration.dimension;

import nc.ui.iufo.resmng.uitemplate.ResTreeObjForm;

import com.ufida.web.container.WebPanel;

/**
 * �������Form
 * @author ll
 */
public class DimensionForm extends ResTreeObjForm{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dimName;
	private String dimCode;
	private String dimType;
	private String dimNote;
	private String createType;
	private String refDimID;
	private WebPanel createTypePanel;
	private boolean modifyUI ;
	
	public String getCreateType() {
		return createType;
	}
	public void setCreateType(String createType) {
		this.createType = createType;
	}
	public String getDimName() {
		return dimName;
	}
	public void setDimName(String dimName) {
		this.dimName = dimName;
	}
	public String getDimNote() {
		return dimNote;
	}
	public void setDimNote(String dimNote) {
		this.dimNote = dimNote;
	}
	public String getDimType() {
		return dimType;
	}
	public void setDimType(String dimType) {
		this.dimType = dimType;
	}
	public String getDimCode() {
		return dimCode;
	}
	public void setDimCode(String dimCode) {
		this.dimCode = dimCode;
	}
	public String getRefDimID() {
		return refDimID;
	}
	public void setRefDimID(String refDimID) {
		this.refDimID = refDimID;
	}
	public WebPanel getCreateTypePanel() {
		return createTypePanel;
	}
	public void setCreateTypePanel(WebPanel createTypePanel) {
		this.createTypePanel = createTypePanel;
	}
	public boolean isModifyUI() {
		return modifyUI;
	}
	public void setModifyUI(boolean modifyUI) {
		this.modifyUI = modifyUI;
	}

	//NOTICE ע�⣺��ҪUI�ύ�����ԣ��������౾�������(��������Ŀǰ��������ύЧ��)��//����web��ܲ���
	//�������Ծ�����ҪUI�ύ�ĸ�������
	/**
     * �ұ�ѡ��
     */
 	private String bizTableSelectedID = null;
	/**
     *����NodeID
     */
 	private String bizTreeSelectedID = null;
	public String getBizTableSelectedID() {
		return bizTableSelectedID;
	}

	public String getBizTreeSelectedID() {
		return bizTreeSelectedID;
	}

	public void setBizTableSelectedID(String bizTableSelectedID) {
		this.bizTableSelectedID = bizTableSelectedID;		
	}

	public void setBizTreeSelectedID(String bizTreeSelectedID) {
		this.bizTreeSelectedID = bizTreeSelectedID;		
	}
}
