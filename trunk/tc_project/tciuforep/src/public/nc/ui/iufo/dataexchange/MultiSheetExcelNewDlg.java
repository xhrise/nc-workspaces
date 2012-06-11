/**
 * MultiSheetExcelDlg.java  5.0 
 * WebDeveloper自动生成.
 * 2006-01-18
 */
package nc.ui.iufo.dataexchange;

import nc.ui.iufo.dataexchange.base.ExcelAction;
import nc.ui.iufo.input.CSomeParam;
import nc.util.iufo.pub.UFOString;

import com.ufida.web.WebException;
import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.Align;
import com.ufida.web.comp.Area;
import com.ufida.web.comp.WebButton;
import com.ufida.web.comp.WebCheckBox;
import com.ufida.web.comp.WebChoice;
import com.ufida.web.comp.WebCloseButton;
import com.ufida.web.comp.WebHiddenField;
import com.ufida.web.comp.WebLabel;
import com.ufida.web.container.WebFlowLayout;
import com.ufida.web.container.WebGridLayout;
import com.ufida.web.container.WebPanel;
import com.ufida.web.html.Div;
import com.ufida.web.html.Script;
import com.ufida.web.html.TD;
import com.ufida.web.html.TR;
import com.ufida.web.html.Table;
import com.ufida.web.window.WebDialog;
import com.ufsoft.iufo.resource.StringResource;

/**
 * Excel格式数据大导出
 * CaiJie
 * 2006-01-18
 */
public class MultiSheetExcelNewDlg extends WebDialog {
	private static final long serialVersionUID = -9213400951907654964L;
	
	private WebPanel dlgPane = null;
    private WebPanel fieldPane = null;
    private WebLabel lbl1 = null;
    private WebChoice fileTypeChoice = null;
    private WebCheckBox zipCheckBox = null;
    private WebPanel btnPane = null;
    private WebButton btnSubmit = null;
    private WebButton btnClose=null;
    private WebHiddenField hidTableSelectID=null;
    private Div keyCheckDiv=null;

 
    /**
     * 设置页面表单数据和处理组件多语言
     * 每次页面刷新时调用
     * CaiJie
     * 2006-01-18
     */
    protected void setData() throws WebException {        
    	btnClose.setValue(StringResource.getStringResource("miufopublic247"));                      
        btnSubmit.setValue(StringResource.getStringResource("miufo1000094"));

        setTitle(StringResource.getStringResource("miufo1000090"));

        //关联ActionForm数据
        nc.ui.iufo.dataexchange.MultiSheetExcelForm form = (nc.ui.iufo.dataexchange.MultiSheetExcelForm) getActionForm(nc.ui.iufo.dataexchange.MultiSheetExcelForm.class.getName());
        if(form == null){
            return;
        }
        this.getFileTypeChoice().setItems(form.getFileTypes());   
        this.getFileTypeChoice().setValue(form.getSelectedFileType()); 
        
        zipCheckBox.setLabel(StringResource.getStringResource("miufo1000093"));
        zipCheckBox.setValue(new Boolean(form.isZipFile()));
                   
        getHidTableSelectID().setValue(UFOString.implode(form.getTableSelectIDs(),","));
        
        if (form.getAccSchemePK()!=null && form.getAccSchemePK().trim().length()>0)
        	getDlgPane().add(new WebHiddenField(CSomeParam.PARAM_ACCSCHEME,form.getAccSchemePK()));
    }
       
    /**
     * 初始化页面组件
     * 只在servlet实例化时调用一次.
     * 此方法中不得涉及任何多语言处理     
     * CaiJie
     * 2006-01-18
     */
    protected void initUI(){
        setWindowWidth(550);
        setWindowHeight(300);
        disableAutoResize();
//        enableAutoResize();

        setContentPane(getDlgPane());
	        
   }


    protected WebLabel getLbl1(){     
        if(lbl1 == null){
            lbl1 = new WebLabel(StringResource.getStringResource("miufo1002737"));
            
        }
        return lbl1;
    }
    

    protected WebChoice getFileTypeChoice(){     
        if(fileTypeChoice == null){
            fileTypeChoice = new WebChoice();
            fileTypeChoice.setWidth("150px");
            fileTypeChoice.setName(MultiSheetExcelAction1.FILETYPE);
            fileTypeChoice.setID(MultiSheetExcelAction1.FILETYPE);
            fileTypeChoice.setOnChange("fileTypeChange(this, document.getElementById('"+ExcelAction.ZIPFILE+"'),document.getElementById('"+ExcelAction.ZIPFILE+"cbox'));");

		      Script spt = new Script("fileTypeChange", new String[]{"typeList", "zipCheck","zipCheckBox"});
		      spt.addFuncLine("{");
		      spt.addFuncLine("	  var type=typeList.value;");
		      spt.addFuncLine("	  if (type==\"XLS\")");
		      spt.addFuncLine("    {");
		      spt.addFuncLine("	      zipCheckBox.disabled=false;$('keyCheckDiv').style.display = '' ; ");
		      spt.addFuncLine("    }");
		      spt.addFuncLine("    else");
		      spt.addFuncLine("    {  $('keyCheckDiv').style.display = 'none' ; ");
		      spt.addFuncLine("	      zipCheckBox.disabled=true; " );
		      spt.addFuncLine("	      zipCheckBox.checked=true;");
		      spt.addFuncLine("       zipCheck.value=\"TRUE\";");
		      spt.addFuncLine("    }");
		      spt.addFuncLine("} ");	
		      getDocument().appendScript(spt);            
	      
                
        }
        return fileTypeChoice;
    }
    

    protected WebCheckBox getZipCheckBox(){     
        if(zipCheckBox == null){
            zipCheckBox = new WebCheckBox();
            zipCheckBox.setName(ExcelAction.ZIPFILE);
            zipCheckBox.setID(ExcelAction.ZIPFILE);           
        }
        return zipCheckBox;
    }
    

    protected WebPanel getFieldPane(){     
        if(fieldPane == null){
            fieldPane = new WebPanel();
            fieldPane.setLayout(new WebGridLayout(3, 2));
            Area area = null;
            area = new Area(1, 1, 1, 1);
            fieldPane.add(getLbl1(), area);
            area = new Area(1, 2, 1, 1);
            fieldPane.add(getFileTypeChoice(), area);
            area=new Area(2,1,2,1,new Align(Align.LEFT));
            
            fieldPane.add(getKeyCheckDiv(), area);
            area = new Area(3, 1, 1, 1,new Align(Align.LEFT));
            fieldPane.add(getZipCheckBox(), area);
 
        }
        return fieldPane;
    }
    
    protected Div getKeyCheckDiv(){
//    	if (keyCheckDiv == null) {
			keyCheckDiv = new Div();
			keyCheckDiv.setID("keyCheckDiv");
			keyCheckDiv.setStyle("display:1");
			nc.ui.iufo.dataexchange.MultiSheetExcelForm form = (nc.ui.iufo.dataexchange.MultiSheetExcelForm) getActionForm(nc.ui.iufo.dataexchange.MultiSheetExcelForm.class
					.getName());
			String[][] checkBoxs = form.getSheetNames();
			int nums=checkBoxs.length;
			Div leftDiv = new Div();
			Div rightDiv = new Div();
			keyCheckDiv.addElement(leftDiv);
			keyCheckDiv.addElement(rightDiv);
			
			String hint = StringResource.getStringResource("miufo1004046");
			int col ;
			if (checkBoxs != null && nums > 0) {
				leftDiv.addElement(new WebLabel(hint));
				leftDiv.setStyle("float:left; clear:left; width:30%; margin:0;valign:middle;");
				rightDiv.setStyle("float:right; clear:right; width:70%; margin:0");
				
				Table table = new Table();
				TR tr = table.appendTR();
				for (int i = 0; i < nums; i++) {
					
					col = i % 2;
					if(i!=0 && col == 0){
						tr = table.appendTR();
					}
					TD td = tr.appendTD();
					td.setStyle("width:33%;align:left;");
					if (checkBoxs[i] != null) {
						td.addElement(new WebCheckBox("keyword"+checkBoxs[i][0],
								checkBoxs[i][1]));
					}
				}
				rightDiv.addElement(table);
			}
//		}
    	
    	return keyCheckDiv;
    }
    protected WebHiddenField getHidTableSelectID(){
    	if (hidTableSelectID==null){
    		hidTableSelectID=new WebHiddenField();
    		hidTableSelectID.setID(MultiSheetExcelAction.MYTABLESELECTID);
    		hidTableSelectID.setName(MultiSheetExcelAction.MYTABLESELECTID);
    	}
    	return hidTableSelectID;
    }

    protected WebButton getBtnSubmit(){     
        if(btnSubmit == null){
            btnSubmit = new WebButton();
            ActionForward fwd = getSubmitActionForward();
            btnSubmit.setActionForward(fwd);
 
        }
        return btnSubmit;
    }
    
    protected WebButton getBtnClose(){     
        if(btnClose == null){
        	btnClose = new WebCloseButton();           
        }
        return btnClose;
    }      
    
    protected WebPanel getBtnPane(){     
        if(btnPane == null){
            btnPane = new WebPanel();
            btnPane.setLayout(new WebFlowLayout(WebFlowLayout.CENTER, WebFlowLayout.HORIZONTAL));
            btnPane.add(getBtnSubmit());
            btnPane.add(getBtnClose());
            btnPane.add(getHidTableSelectID());
        }
        return btnPane;
    }
    

    protected WebPanel getDlgPane(){     
        if(dlgPane == null){
            dlgPane = new WebPanel();
            dlgPane.setLayout(new WebFlowLayout(WebFlowLayout.CENTER, WebFlowLayout.VERTICAL));
            dlgPane.add(getFieldPane());
            dlgPane.add(getBtnPane());
 
        }
        return dlgPane;
    }
    
    protected ActionForward getSubmitActionForward(){
    	return new ActionForward(nc.ui.iufo.dataexchange.MultiSheetExcelNewAction.class.getName(), "action");
    }
    @Override
	protected void clear() {
		super.clear();
		fieldPane = null;
		fileTypeChoice = null;
	}
}
/**@WebDeveloper
<?xml version="1.0" encoding='gb2312'?>
    <DialogVO Description="Excel格式数据大导出" name="MultiSheetExcelDlg" package="nc.ui.iufo.dataexchange" 关联Form="nc.ui.iufo.dataexchange.MultiSheetExcelForm">
      <WebPanel FlowDirection="VERTICAL" layout="WebFlowLayout" name="dlgPane">
        <LayoutVO name="layout1" type="WebFlowLayout">
          <WebPanel col="0" name="fieldPane" row="0">
            <LayoutVO name="layout1" type="WebGridLayout">
              <WebLabel col="0" name="lbl1" row="0">
              </WebLabel>
              <WebChoice col="1" name="fileTypeChoice" row="0" 关联Form属性="selectedFileType">
              </WebChoice>
              <WebCheckBox col="0" name="zipCheckBox" row="2" 关联Form属性="zipFile">
              </WebCheckBox>
            </LayoutVO>
          </WebPanel>
          <WebPanel col="1" layout="WebFlowLayout" name="btnPane" row="0">
            <LayoutVO name="layout2" type="WebFlowLayout">
              <WebButton ActionForward="nc.ui.iufo.dataexchange.MultiSheetExcelAction, action" col="0" name="btnSubmit" row="0">
              </WebButton>
            </LayoutVO>
          </WebPanel>
        </LayoutVO>
      </WebPanel>
    </DialogVO>
@WebDeveloper*/