/**
 * Form1.java  5.0 
 * 
 * WebDeveloper自动生成.
 * 2006-01-17
 */
package nc.ui.bi.integration.dimension;

import nc.ui.iufo.exproperty.IExPropDataEditForm;
import nc.vo.iufo.exproperty.ExPropertyVO;

import com.ufida.web.action.ActionForm;

/**
 * 类作用描述文字
 * ll
 * 2006-01-17
 */
public class MemberDesignForm extends ActionForm implements IExPropDataEditForm{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7237598926824663006L;
	//
 	private String memcode;
 	private String memname;
 	private String calattr;
 	private String edittype;
 	private String memID;
 	private String UIType;
 	private String selectedTreeID;
 	
	public String getCalattr() {
		return calattr;
	}
	public void setCalattr(String calattr) {
		this.calattr = calattr;
	}
	public String getMemcode() {
		return memcode;
	}
	public void setMemcode(String memcode) {
		this.memcode = memcode;
	}
	public String getMemID() {
		return memID;
	}
	public void setMemID(String memID) {
		this.memID = memID;
	}
	public String getMemname() {
		return memname;
	}
	public void setMemname(String memname) {
		this.memname = memname;
	}
	public String getEdittype() {
		return edittype;
	}
	public void setEdittype(String edittype) {
		this.edittype = edittype;
	}
	
	 /**
     * 自定义属性相关 类属性
     */
    private ExPropertyVO[]  exPropVOs;
    //
    private String[]        strPropValues;
    //自定义属性的显示值，仅显示用
    private String[]        strPropDisValues;
    /**
 	 * 自定义属性模块ID
 	 */
 	private String m_strExPropModuleID = null;
    
 	/* （非 Javadoc）
     * @see nc.ui.iufo.exproperty.IExPropDataEditForm#getExPropModuleID()
     */
    public String getExPropModuleID() {
        return m_strExPropModuleID;
    }
    /* （非 Javadoc）
     * @see nc.ui.iufo.exproperty.IExPropDataEditForm#setExPropModuleID(java.lang.String)
     */
    public void setExPropModuleID(String strExPropModuleID) {
        this.m_strExPropModuleID = strExPropModuleID;        
    }
    /* （非 Javadoc）
     * @see nc.ui.iufo.exproperty.IExPropDataEditForm#getExPropValues()
     */
	public String[] getExPropValues(){
		return strPropValues;
	}
	
	/* （非 Javadoc）
     * @see nc.ui.iufo.exproperty.IExPropDataEditForm#setExPropValues(java.lang.String[])
     */
    public void setExPropValues(String[] strValues ){
		this.strPropValues = strValues;
	}
    /* （非 Javadoc）
     * @see nc.ui.iufo.exproperty.IExPropDataEditForm#getExPropVOs()
     */
	public ExPropertyVO[] getExPropVOs(){
		return exPropVOs;
	}
	/* （非 Javadoc）
     * @see nc.ui.iufo.exproperty.IExPropDataEditForm#setExPropVOs(nc.vo.iufo.exproperty.ExPropertyVO[])
     */
    public void setExPropVOs(ExPropertyVO[] exPropVOs ){
		this.exPropVOs = exPropVOs;
	}

    public String[] getExPropDisValues() {
        return strPropDisValues;
    }

    public void setExPropDisValues(String[] strExPropDisValues) {
        this.strPropDisValues = strExPropDisValues;        
    }
	public String getUIType() {
		return UIType;
	}
	public void setUIType(String type) {
		UIType = type;
	}
	public String getSelectedTreeID() {
		return selectedTreeID;
	}
	public void setSelectedTreeID(String selectedTreeID) {
		this.selectedTreeID = selectedTreeID;
	}	
 	
    /**
     * ActionForm属性值校验
     * 
     * @return 校验错误提示信息集合
     */
   // public Vector validate() {
   //     return null;
   // }

}
