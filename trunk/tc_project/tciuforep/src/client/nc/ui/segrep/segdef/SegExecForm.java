/*
 * 创建日期 2006-7-17
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.segrep.segdef;

import nc.vo.bi.integration.dimension.DimensionVO;

import com.ufida.web.action.ActionForm;

public class SegExecForm extends ActionForm {
	
	private		String					strSegDefPK;
	private     DimensionVO[]			dimVOs;
	private		int						nDimNumbers;
	
	public DimensionVO[] getDimVOs() {
		return dimVOs;
	}
	public void setDimVOs(DimensionVO[] dimVOs) {
		this.dimVOs = dimVOs;
		if( dimVOs != null ){
			nDimNumbers = dimVOs.length;
		}else{
			nDimNumbers = 0;
		}
	}
	public String getSegDefPK() {
		return strSegDefPK;
	}
	public void setSegDefPK(String strSegDefPK) {
		this.strSegDefPK = strSegDefPK;
	}
	public int getDimNumbers() {
		return nDimNumbers;
	}
	public void setDimNumbers(int dimNumbers) {
		nDimNumbers = dimNumbers;
	}
//	public String getOperType() {
//		return strOperType;
//	}
//	public void setOperType(String strOperType) {
//		this.strOperType = strOperType;
//	}
	
	
}
