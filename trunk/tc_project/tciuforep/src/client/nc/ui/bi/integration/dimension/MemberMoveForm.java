/**
 * Form1.java  5.0 
 * 
 * WebDeveloper�Զ�����.
 * 2006-01-17
 */
package nc.ui.bi.integration.dimension;

import com.ufida.web.action.ActionForm;

/**
 * ��������������
 * ll
 * 2006-01-17
 */
public class MemberMoveForm extends ActionForm{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7237598926824663006L;
	static String ID_memRef = "destMemID";
 	private String destMemID;
 	private String selectedTreeID;

 	public String getDestMemID(){
 		return destMemID;
 	}
 	public void setDestMemID(String destID){
 		this.destMemID = destID;
 	}
	public String getSelectedTreeID() {
		return selectedTreeID;
	}
	public void setSelectedTreeID(String selectedTreeID) {
		this.selectedTreeID = selectedTreeID;
	}	
 	
}
