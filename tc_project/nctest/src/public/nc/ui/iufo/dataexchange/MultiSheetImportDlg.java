/**
 * MultiSheetImportDlg.java  5.0 
 * WebDeveloper自动生成.
 * 2006-01-19
 */
package nc.ui.iufo.dataexchange;

import com.ufida.web.WebException;
import com.ufida.web.comp.Area;
import com.ufida.web.comp.WebButton;
import com.ufida.web.comp.WebHiddenField;
import com.ufida.web.comp.WebLabel;
import com.ufida.web.comp.table.WebTable;
import com.ufida.web.container.WebFlowLayout;
import com.ufida.web.container.WebGridLayout;
import com.ufida.web.container.WebPanel;
import com.ufida.web.html.BR;
import com.ufida.web.window.WebDialog;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 多表页Excel数据导入
 * CaiJie
 * 2006-01-19
 */
public class MultiSheetImportDlg extends WebDialog {
	private static final long serialVersionUID = 5520730896577556732L;
	
	private WebPanel dlgPane = null;
    private WebPanel fieldPane = null;
    private WebLabel lbl1 = null;
    private WebTable table1 = null;
    private BR br1 = null;
    private WebPanel btnPane = null;
    private WebButton btnSubmit = null;
    private WebButton btnClose = null;

 
    /**
     * 设置页面表单数据和处理组件多语言
     * 每次页面刷新时调用
     * CaiJie
     * 2006-01-19
     */
    protected void setData() throws WebException {        
                                  
        btnClose.setValue(StringResource.getStringResource("miufopublic247"));
        btnSubmit.setValue(StringResource.getStringResource("miufopublic246"));
        lbl1.setValue(StringResource.getStringResource("miufo1002745"));

        setTitle(StringResource.getStringResource("miufo1002744"));

        //关联ActionForm数据
        nc.ui.iufo.dataexchange.MultiSheetImportForm form = (nc.ui.iufo.dataexchange.MultiSheetImportForm) getActionForm(nc.ui.iufo.dataexchange.MultiSheetImportForm.class.getName());
        if(form == null)
            return;

        getTable1().setModel(form.getTableModel());
        getBtnSubmit().setOnClick("button1_onclick_import('"+form.getOkActionForward().genURI()+"');");
        getBtnClose().setActionForward(form.getBackActionForward());
        
        WebHiddenField hid=new WebHiddenField();
        hid.setID("hid_sel_relation");
        hid.setName("hid_sel_relation");
        addHiddenField(hid);
    }
       
    /**
     * 初始化页面组件
     * 只在servlet实例化时调用一次.
     * 此方法中不得涉及任何多语言处理     
     * CaiJie
     * 2006-01-19
     */
    protected void initUI(){
    	
        setWindowWidth(800);
        setWindowHeight(600);
        disableAutoResize();

        setContentPane(getDlgPane());
   }


    protected WebLabel getLbl1(){     
        if(lbl1 == null){
            lbl1 = new WebLabel();
 
        }
        return lbl1;
    }
    

    protected WebTable getTable1(){     
        if(table1 == null){
            table1 = new WebTable();
            table1.setHeight(" expression(document.body.offsetHeight-150)");
            table1.setWidth(" expression(document.body.offsetWidth-15)");
 
        }
        return table1;
    }
    

    protected WebPanel getFieldPane(){     
        if(fieldPane == null){
            fieldPane = new WebPanel();
            fieldPane.setLayout(new WebGridLayout(2, 1));
            Area area = null;
            area = new Area(1, 1, 1, 1);
            fieldPane.add(getLbl1(), area);
            area = new Area(2, 1, 1, 1);
            fieldPane.add(getTable1(), area);
 
        }
        return fieldPane;
    }
    

    protected BR getBr1(){     
        if(br1 == null){
            br1 = new BR();
 
        }
        return br1;
    }
    

    protected WebButton getBtnSubmit(){     
        if(btnSubmit == null){
            btnSubmit = new WebButton(); 
        }
        return btnSubmit;
    }
    

    protected WebButton getBtnClose(){     
        if(btnClose == null){
            btnClose = new WebButton();           
        }
        return btnClose;
    }
    

    protected WebPanel getBtnPane(){     
        if(btnPane == null){
            btnPane = new WebPanel();
            btnPane.setLayout(new WebFlowLayout(WebFlowLayout.CENTER, WebFlowLayout.HORIZONTAL));
            btnPane.add(getBtnSubmit());
            btnPane.add(getBtnClose());
 
        }
        return btnPane;
    }
    

    protected WebPanel getDlgPane(){     
        if(dlgPane == null){
            dlgPane = new WebPanel();
            dlgPane.setLayout(new WebFlowLayout(WebFlowLayout.CENTER, WebFlowLayout.VERTICAL));
            dlgPane.add(getFieldPane());
            dlgPane.add(getBr1());
            dlgPane.add(getBtnPane());
 
        }
        return dlgPane;
    }
    

    
}
/**@WebDeveloper
<?xml version="1.0" encoding='gb2312'?>
    <DialogVO Description="多表页Excel数据导入" name="MultiSheetImportDlg" package="nc.ui.iufo.dataexchange" title="StringResource.getStringResource("miufo1002744")" 关联Form="nc.ui.iufo.dataexchange.MultiSheetImportForm">
      <WebPanel FlowDirection="VERTICAL" layout="WebFlowLayout" name="dlgPane">
        <LayoutVO name="layout1" type="WebFlowLayout">
          <WebPanel col="0" name="fieldPane" row="0">
            <LayoutVO name="layout1" type="WebGridLayout">
              <WebLabel col="0" name="lbl1" row="0" value="StringResource.getStringResource("miufo1002745")">
              </WebLabel>
              <WebTable col="0" name="table1" row="1">
                <LayoutVO name="layout2" type="WebFlowLayout">
                </LayoutVO>
              </WebTable>
            </LayoutVO>
          </WebPanel>
          <BR col="1" name="br1" row="0">
          </BR>
          <WebPanel col="2" layout="WebFlowLayout" name="btnPane" row="0">
            <LayoutVO name="layout2" type="WebFlowLayout">
              <WebButton ActionForward="nc.ui.iufo.dataexchange.ImportExcelCheckResultAction, execute" col="0" name="btnSubmit" row="0" value="StringResource.getStringResource("miufopublic246")">
              </WebButton>
              <WebCloseButton col="1" name="btnClose" row="0">
              </WebCloseButton>
            </LayoutVO>
          </WebPanel>
        </LayoutVO>
      </WebPanel>
    </DialogVO>
@WebDeveloper*/