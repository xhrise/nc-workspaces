/**
 * MultiSheetImportForm.java  5.0 
 * 
 * WebDeveloper自动生成.
 * 2006-01-19
 */
package nc.ui.iufo.dataexchange;

import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.table.IWebTableModel;

/**
 * 多表页Excel数据导入
 * CaiJie
 * 2006-01-19
 */
public class MultiSheetImportForm extends ActionForm {
	private static final long serialVersionUID = 3519872931509628690L;
	
	private ActionForward  backActionForward;
	private ActionForward  okActionForward;

	private IWebTableModel tableModel;
	private String[][] repChoiceItems; 
       
    public String[][] getRepChoiceItems() {
        return repChoiceItems;
    }
    public void setRepChoiceItems(String[][] repChoiceItems) {
        this.repChoiceItems = repChoiceItems;
    }
    public IWebTableModel getTableModel() {
        return tableModel;
    }
    public void setTableModel(IWebTableModel tableModel) {
        this.tableModel = tableModel;
    }
    public ActionForward getBackActionForward() {
        return backActionForward;
    }
    public void setBackActionForward(ActionForward backActionForward) {
        this.backActionForward = backActionForward;
    }
    public ActionForward getOkActionForward() {
        return okActionForward;
    }
    public void setOkActionForward(ActionForward okActionForward) {
        this.okActionForward = okActionForward;
    }
}
/**@WebDeveloper
<?xml version="1.0" encoding='gb2312'?>
    <FormVO Description="多表页Excel数据导入" name="MultiSheetImportForm" package="nc.ui.iufo.dataexchange">
      <FieldsVO>
      </FieldsVO>
    </FormVO>
@WebDeveloper*/