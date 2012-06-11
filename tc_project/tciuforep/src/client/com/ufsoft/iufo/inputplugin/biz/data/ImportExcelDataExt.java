/*
 * 创建日期 2006-4-6
 *
 */
package com.ufsoft.iufo.inputplugin.biz.data;
import com.ufida.iufo.pub.tools.AppDebug;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ufsoft.iufo.inputplugin.biz.AbsIufoOpenedRepBizMenuExt;
import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iufo.inputplugin.biz.file.ChooseRepData;
import com.ufsoft.iufo.inputplugin.biz.file.ChooseRepExt2;

import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iuforeport.tableinput.applet.TableInputException;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.sysplugin.excel.ExcelFileChooserDialog;
import com.ufsoft.report.sysplugin.excel.ExcelImpCmd;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;

/**
 * 导入Excel数据的IActionExt实现类
 * 
 * @author liulp
 *
 */
public class ImportExcelDataExt extends AbsIufoOpenedRepBizMenuExt{
    private boolean hasRepCanModify = true;
    
    public ImportExcelDataExt(UfoReport ufoReport) {
        super(ufoReport);
    }
    
    private boolean isHasRepCanModify(){
        return hasRepCanModify;
    }
    public void setHasRepCanModify(boolean bHasRepCanModify){
        this.hasRepCanModify = bHasRepCanModify;
    }

    @Override
	protected String getGroup() {
		return "impAndExp";
	}
    
    protected String[] getPaths() {
        return doGetImportMenuPaths();
    }

    protected String getMenuName() {
        return  MultiLangInput.getString("uiufotableinput0009");//"Excel数据";
    }

    protected UfoCommand doGetCommand(UfoReport ufoReport) {
        return new ImportExcelDataCmd(ufoReport);
    }
    public Object[] getParams(UfoReport container) {
        return doGetImportInfos(container,container);
    }
    /**
     * 得到导入Excel的准备数据信息(CellsModel)
     * @param container
     * @param ufoReport
     * @return
     */
    private static Object[] doGetImportInfos(Container container,UfoReport ufoReport){
        //#打开文件选择框
    	File file=null;
    	Boolean isAutoCal=false;
//      File file = ExcelImpCmd.doGetExcelFile(ufoReport);
    	ExcelFileChooserDialog excelDialog=new ExcelFileChooserDialog(ufoReport);
    	excelDialog.show();
    	
    	if(excelDialog.getResult()==UfoDialog.ID_OK){
    		file=excelDialog.getSelectedFile();
    		isAutoCal=excelDialog.isAutoCal();
    	
        if(file == null){//用户取消导入则直接返回
        	return null;
        }
        //#用户选择的文件不存在
        if(!file.exists()){
            UfoPublic.showErrorDialog(ufoReport,MultiLangInput.getString("uiufotableinput0017"),MultiLangInput.getString("uiufotableinput0018"));//"文件不存在","出错了"
            return null;
        }
        //#得到WorkBook
        HSSFWorkbook workBook = null;
        try {
            workBook = ImportExcelDataBizUtil.getImportWorkBook(file);
        } catch (Exception e) {
        	AppDebug.debug(e);//@devTools             e.printStackTrace(System.out);
            UfoPublic.showErrorDialog(ufoReport,MultiLangInput.getString("uiufotableinput0019"),MultiLangInput.getString("uiufotableinput0018"));//"读取选中的Excel文件","出错了"
            return null;            
        }
        //得到可选择报表数据
        ChooseRepData[] chooseRepDatas = ChooseRepExt2.doGetChooseRepDatas(ufoReport,true);
        if(chooseRepDatas == null || chooseRepDatas.length <=0){
            JOptionPane.showMessageDialog(container,MultiLangInput.getString("uiufotableinput0020"));//"该任务可能没有可选择的报表" 
            return null;
        }
        TableInputTransObj transObj = InputBizOper.doGetTransObj(ufoReport);
        String strCurRepPK = transObj.getRepDataParam().getReportPK();
        //#得到自动匹配信息并检查
        Hashtable<String, Object> matchMap = null;
        try {
            matchMap = ImportExcelDataBizUtil.doGetAutoMatchMap(chooseRepDatas,workBook,strCurRepPK);
            //检查匹配信息
            ImportExcelDataBizUtil.checkMatchMap(matchMap);
        } catch (TableInputException e) {
AppDebug.debug(e);//@devTools             e.printStackTrace(System.out);
            JOptionPane.showMessageDialog(container,e.getMessage()); 
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
            	InputDynEndRowDialog inputDynEndRowDialog = new InputDynEndRowDialog(ufoReport);
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
            ImportExcelDataSettingDlg setDlg = new ImportExcelDataSettingDlg(container,chooseRepDatas,matchMap);
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
    	return null;
    }
    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return super.isEnabled(focusComp) && isHasRepCanModify();
    }
    /**
     * override
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(getMenuName());
        uiDes.setPaths(getPaths());
        ActionUIDes uiDes1 = (ActionUIDes) uiDes.clone();
	    uiDes1.setPaths(new String[]{});
	    uiDes1.setName(MultiLang.getString("import")+MultiLangInput.getString("uiufotableinput0009"));
	    uiDes1.setTooltip(MultiLang.getString("import")+MultiLangInput.getString("uiufotableinput0009"));
	    uiDes1.setImageFile("reportcore/import.gif");
	    uiDes1.setToolBar(true);
	    uiDes1.setGroup(MultiLang.getString("file"));
        return new ActionUIDes[]{uiDes,uiDes1};
    }
}
