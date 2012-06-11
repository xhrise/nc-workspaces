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
	 * @i18n miufohbbb00111=����Excel�������
	 * @i18n miufohbbb00112=����Excelʧ��:
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
	 * @i18n miufohbbb00113=���뵼��
	 * @i18n miufo1002712=����Excel����
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
     * �õ�����Excel��׼��������Ϣ(CellsModel)
     * @param container
     * @param ufoReport
     * @return
     */
    private Object[] doGetImportInfos(){
    	RepDataEditor editor=getRepDataEditor();
    	
        //#���ļ�ѡ���
    	File file=null;
    	Boolean isAutoCal=false;
    	ExcelFileChooserDialog excelDialog=new ExcelFileChooserDialog(editor);
    	excelDialog.show();
    	
    	if(excelDialog.getResult()!=UfoDialog.ID_OK)
    		return null;
    	
		file=excelDialog.getSelectedFile();
		isAutoCal=excelDialog.isAutoCal();
    	
        if(file == null){//�û�ȡ��������ֱ�ӷ���
        	return null;
        }
        
        //#�û�ѡ����ļ�������
        if(!file.exists()){
            UfoPublic.showErrorDialog(editor,MultiLangInput.getString("uiufotableinput0017"),MultiLangInput.getString("uiufotableinput0018"));//"�ļ�������","������"
            return null;
        }
        //#�õ�WorkBook
        HSSFWorkbook workBook = null;
        try {
            workBook = ImportExcelDataBizUtil.getImportWorkBook(file);
        } catch (Exception e) {
        	AppDebug.debug(e);//@devTools             e.printStackTrace(System.out);
            UfoPublic.showErrorDialog(editor,MultiLangInput.getString("uiufotableinput0019"),MultiLangInput.getString("uiufotableinput0018"));//"��ȡѡ�е�Excel�ļ�","������"
            return null;            
        }
        //�õ���ѡ�񱨱�����
        ChooseRepData[] chooseRepDatas = ChooseRepExt2.doGetChooseRepDatas(editor,true);
        if(chooseRepDatas == null || chooseRepDatas.length <=0){
            JOptionPane.showMessageDialog(editor,MultiLangInput.getString("uiufotableinput0020"));//"���������û�п�ѡ��ı���" 
            return null;
        }
        
        String strCurRepPK = editor.getRepDataParam().getReportPK();
        //#�õ��Զ�ƥ����Ϣ�����
        Hashtable<String, Object> matchMap = null;
        try {
            matchMap = ImportExcelDataBizUtil.doGetAutoMatchMap(chooseRepDatas,workBook,strCurRepPK);
            //���ƥ����Ϣ
            ImportExcelDataBizUtil.checkMatchMap(matchMap);
        } catch (TableInputException e) {
        	AppDebug.debug(e);//@devTools             e.printStackTrace(System.out);
            JOptionPane.showMessageDialog(editor,e.getMessage()); 
            return null;
        }
       
        List<Object[]> listImportInfos = null;
        List<String[]> array = null;
        if(matchMap.size() == 1){
            //ƥ������һ�ű�ҳ,����ǿ�ֱ�ӵ���ı�����׼����array
            ChooseRepData chooseRepData = ImportExcelDataBizUtil.getCurRepData(chooseRepDatas,strCurRepPK);
            if(chooseRepData == null){
                return null;
            }
            //�Ƿ��ǿ�ֱ��¼��ı���
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
            }else{//��ʾ�û����붯̬������չ����
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
            //����ƥ����棬�õ�ƥ�����󣬼��㵼��Excel��׼��������Ϣ(CellsModel)
            ImportExcelDataSettingDlg setDlg = new ImportExcelDataSettingDlg(editor,chooseRepDatas,matchMap);
            setDlg.setVisible(true);
            if (setDlg.getResult() == UfoDialog.ID_OK) {  
                array = setDlg.getResultArray();      
            }else{
                return null;
            }
        }
        
        //�õ�����Excel��׼��������Ϣ(CellsModel)
        listImportInfos = ImportExcelDataBizUtil.getImportInfos(array, workBook);  
        
        return new Object[]{listImportInfos,isAutoCal};
    }
}
 