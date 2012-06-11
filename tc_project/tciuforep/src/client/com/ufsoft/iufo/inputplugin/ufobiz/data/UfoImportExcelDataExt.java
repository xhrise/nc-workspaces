package com.ufsoft.iufo.inputplugin.ufobiz.data;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.input.edit.RepDataEditor;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.console.ActionHandler;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.inputplugin.biz.data.ImportExcelDataBizUtil;
import com.ufsoft.iufo.inputplugin.biz.data.ImportExcelDataSettingDlg;
import com.ufsoft.iufo.inputplugin.biz.data.InputDynEndRowDialog;
import com.ufsoft.iufo.inputplugin.biz.file.ChooseRepData;
import com.ufsoft.iufo.inputplugin.biz.file.ChooseRepExt2;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.inputplugin.ufobiz.AbsUfoBizCmd;
import com.ufsoft.iufo.inputplugin.ufobiz.AbsUfoOpenedRepBizMenuExt;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.repdatainput.RepDataOperResultVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputException;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.sysplugin.excel.ExcelFileChooserDialog;
import com.ufsoft.report.util.UfoPublic;

public class UfoImportExcelDataExt extends AbsUfoOpenedRepBizMenuExt {

	/**
	 * @i18n miufohbbb00111=导入Excel数据完成
	 * @i18n miufohbbb00112=导入Excel失败:
	 */
	public void execute(ActionEvent e) {
		RepDataEditor editor=getRepDataEditor();
		try{
	    	if (!AbsUfoBizCmd.stopCellEditing(editor))
	    		return;
	    	
			Object[] objs=doGetImportInfos();
			if (objs==null)
				return;
			
			List<Object[]> vParams=(List<Object[]>)objs[0];
			boolean bAutoCal=(Boolean)objs[1];
			
    		RepDataOperResultVO resultVO=(RepDataOperResultVO)ActionHandler.execWithZip("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "importExcelData",
					new Object[]{editor.getRepDataParam(),RepDataControler.getInstance(editor.getMainboard()).getLoginEnv(editor.getMainboard()),vParams,bAutoCal});
			editor.setCellsModel(resultVO.getCellsModel());
			editor.setHashDynAloneID(resultVO.getHashDynAloneID());
			UfoPublic.sendMessage(StringResource.getStringResource("miufohbbb00111"),editor); 
		}catch(Exception te){
			AppDebug.debug(te);
			UfoPublic.sendErrorMessage(StringResource.getStringResource("miufohbbb00112")+te.getMessage(), editor, null);
		}
	}

	/**
	 * @i18n miufohbbb00113=导入导出
	 * @i18n miufo1002712=导入Excel数据
	 */
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor pad = new PluginActionDescriptor(MultiLangInput.getString("uiufotableinput0009"));
		pad.setIcon("images/reportcore/import.gif");
		pad.setGroupPaths(doGetImportMenuPaths(StringResource.getStringResource("miufohbbb00113")));
//		pad.setGroupPaths(doGetImportMenuPaths("ddd"));
		pad.setToolTipText(StringResource.getStringResource("miufo1002712"));
		pad.setExtensionPoints(XPOINT.MENU,XPOINT.TOOLBAR);
		pad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,KeyEvent.CTRL_MASK));
		pad.setMemonic('E');
		pad.setShowDialog(true);
		return pad;
	}

	public boolean isEnabled() {
		boolean bEnabled=super.isEnabled();
		if (bEnabled==false)
			return bEnabled;
		
		RepDataEditor editor=(RepDataEditor)getCurrentView();
		return editor.getMenuState()!=null && editor.getMenuState().isRepCanModify() && editor.getMenuState().isCommited()==false;
	}
	
    public Object[] getParams() {
        return doGetImportInfos();
    }
   
    /**
     * 得到导入Excel的准备数据信息(CellsModel)
     * @param container
     * @param ufoReport
     * @return
     */
    private Object[] doGetImportInfos(){
    	RepDataEditor editor=getRepDataEditor();
    	
        //#打开文件选择框
    	File file=null;
    	Boolean isAutoCal=false;
    	ExcelFileChooserDialog excelDialog=new ExcelFileChooserDialog(editor);
    	excelDialog.show();
    	
    	if(excelDialog.getResult()!=UfoDialog.ID_OK)
    		return null;
    	
		file=excelDialog.getSelectedFile();
		isAutoCal=excelDialog.isAutoCal();
    	
        if(file == null){//用户取消导入则直接返回
        	return null;
        }
        
        //#用户选择的文件不存在
        if(!file.exists()){
            UfoPublic.showErrorDialog(editor,MultiLangInput.getString("uiufotableinput0017"),MultiLangInput.getString("uiufotableinput0018"));//"文件不存在","出错了"
            return null;
        }
        //#得到WorkBook
        HSSFWorkbook workBook = null;
        try {
            workBook = ImportExcelDataBizUtil.getImportWorkBook(file);
        } catch (Exception e) {
        	AppDebug.debug(e);//@devTools             e.printStackTrace(System.out);
            UfoPublic.showErrorDialog(editor,MultiLangInput.getString("uiufotableinput0019"),MultiLangInput.getString("uiufotableinput0018"));//"读取选中的Excel文件","出错了"
            return null;            
        }
        //得到可选择报表数据
        ChooseRepData[] chooseRepDatas = ChooseRepExt2.doGetChooseRepDatas(editor,true);
        if(chooseRepDatas == null || chooseRepDatas.length <=0){
            JOptionPane.showMessageDialog(editor,MultiLangInput.getString("uiufotableinput0020"));//"该任务可能没有可选择的报表" 
            return null;
        }
        
        String strCurRepPK = editor.getRepDataParam().getReportPK();
        //#得到自动匹配信息并检查
        Hashtable<String, Object> matchMap = null;
        try {
            matchMap = ImportExcelDataBizUtil.doGetAutoMatchMap(chooseRepDatas,workBook,strCurRepPK);
            //检查匹配信息
            ImportExcelDataBizUtil.checkMatchMap(matchMap);
        } catch (TableInputException e) {
        	AppDebug.debug(e);//@devTools             e.printStackTrace(System.out);
            JOptionPane.showMessageDialog(editor,e.getMessage()); 
            return null;
        }
       
        List<Object[]> listImportInfos = null;
        List<String[]> array = null;
        if(matchMap.size() == 1){
            //匹配上了一张表页,如果是可直接导入的报表，则准备好array
            ChooseRepData chooseRepData = ImportExcelDataBizUtil.getCurRepData(chooseRepDatas,strCurRepPK);
            if(chooseRepData == null){
                return null;
            }
            //是否是可直接录入的报表
            String[] selStrs = new String[3];
            selStrs[0] = (String)matchMap.keySet().iterator().next();
            selStrs[1] = ((String[])matchMap.get(selStrs[0]))[1];
            
            if(chooseRepData.isCanImportDirected()){
               if (selStrs[0]!=null && selStrs[0].trim().equalsIgnoreCase("null")==false
                        && selStrs[1]!=null && selStrs[1].trim().equalsIgnoreCase("null")==false){
                    selStrs[2] = "-1";
                    array = new ArrayList<String[]>();
                    array.add(selStrs);                 
                }
            }else{//提示用户输入动态区域扩展行数
            	InputDynEndRowDialog inputDynEndRowDialog = new InputDynEndRowDialog(editor);
            	inputDynEndRowDialog.show();
            	if(inputDynEndRowDialog.getSelectOption() != JOptionPane.OK_OPTION){
            		return null;
            	}
            	
            	selStrs[2] = inputDynEndRowDialog.getText();
            	array = new ArrayList<String[]>();
                array.add(selStrs);
            }
        }else{
            //弹出匹配界面，得到匹配结果后，计算导入Excel的准备数据信息(CellsModel)
            ImportExcelDataSettingDlg setDlg = new ImportExcelDataSettingDlg(editor,chooseRepDatas,matchMap);
            setDlg.setVisible(true);
            if (setDlg.getResult() == UfoDialog.ID_OK) {  
                array = setDlg.getResultArray();      
            }else{
                return null;
            }
        }
        
        //得到导入Excel的准备数据信息(CellsModel)
        listImportInfos = ImportExcelDataBizUtil.getImportInfos(array, workBook);  
        
        return new Object[]{listImportInfos,isAutoCal};
    }
}
 