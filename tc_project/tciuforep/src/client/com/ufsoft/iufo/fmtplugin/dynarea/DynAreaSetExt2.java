/*
 * DynAreaSetExt.java
 * �������� 2005-1-27
 * Created by CaiJie
 */
package com.ufsoft.iufo.fmtplugin.dynarea;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formula.FormulaDefPlugin;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.formula.UfoFmlExecutor;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;

/**
 * @author caijie 
 * @since 3.1
 */
public class DynAreaSetExt2 extends AbsActionExt implements IUfoContextKey{// IMainMenuExt {
    
     DynAreaDefPlugIn m_dynAreaPlugIn = null;
    /**
     * ����ӵĶ��󻺴�AreaPosition-DynAreaCell
     */
    private Hashtable m_addedDynArea = new Hashtable();
    /**
     * ɾ���Ķ��󻼴�:���ɾ���Ķ�������һ�ε���updateReport()������ӵ�,ֱ��ɾ���������Map��
     */
    private ArrayList m_removedDynArea = new ArrayList();
    /**
     * �޸�ǰ���󻺴�:AreaPosition-DynAreaCell
     */
    private Hashtable m_updatedDynArea = new Hashtable();
    
    public DynAreaSetExt2(DynAreaDefPlugIn dynAreaPlug){
        m_dynAreaPlugIn = dynAreaPlug;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ufsoft.report.menu.ICommandExt#getCommand()
     */
    public UfoCommand getCommand() {

        return null;
    }
    /**
     * �����ӵġ�ɾ���ġ��͸��µĶ�̬�������õ���̬����ģ����
     */
    private void applyDynArea(){
        DynAreaModel model =  m_dynAreaPlugIn.getDynAreaModel(); 
        CellsModel cellsModel = getReport().getCellsModel();
        
        String strRepId = getReport().getContextVo().getAttribute(REPORT_PK) == null ? null : (String)getReport().getContextVo().getAttribute(REPORT_PK);
		FormulaModel formulaModel = FormulaModel.getInstance(cellsModel);
		UfoFmlExecutor formulaExecutor = formulaModel.getUfoFmlExecutor();
		
        for(Iterator iter = m_removedDynArea.iterator();iter.hasNext();){
        	String strDynPK=((DynAreaCell)iter.next()).getDynAreaVO().getDynamicAreaPK();
            model.removeDynArea(strRepId,strDynPK);
            //ɾ����̬����ʽ��
//            FormulaDefPlugin formPlugIn=(FormulaDefPlugin) getReport().getPluginManager().getPlugin(FormulaDefPlugin.class.getName());
//            if(formPlugIn!=null && formPlugIn.getFmlExecutor()!=null){
//            	formPlugIn.getFmlExecutor().removeDynamicArea(strDynPK);
//            }
            formulaExecutor.removeDynamicArea(strDynPK);
            m_dynAreaPlugIn.setDirty(true);
        }
 		for(Enumeration enumer = m_updatedDynArea.keys(); enumer.hasMoreElements();){
            AreaPosition area = (AreaPosition) enumer.nextElement();
            model.updateDynArea(strRepId,area,(DynAreaCell)m_updatedDynArea.get(area));
            m_dynAreaPlugIn.setDirty(true);
        }
 		for(Enumeration enumer = m_addedDynArea.keys(); enumer.hasMoreElements();){
           AreaPosition area = (AreaPosition) enumer.nextElement();
           model.addDynArea((DynAreaCell)m_addedDynArea.get(area));
           m_dynAreaPlugIn.setDirty(true);
        }	
    }
    /**
     * ��̬��������ʵ���ࡣ
     * @param report
     * @param resultType -1Ϊֱ�ӽ������ҳ�棻DynAreaMngDlg.ID_ADDΪ�Ҽ���Ӷ�̬����DynAreaMngDlg.ID_UPDATEΪ�޸Ķ�̬����DynAreaMngDlg.ID_DELETEΪɾ����̬����
     * @i18n uiiufofmt00001=������Ӷ�̬����:
     * @i18n uiiufofmt00002=����ɾ����̬����:
     */
    protected void dynAreaMngImpl(UfoReport report, int resultType){
        m_addedDynArea.clear();
        m_updatedDynArea.clear();
        m_removedDynArea.clear();
        
        boolean bFirstLoop = true;
        
        //�õ���̬����ʵ����            
        DynAreaModel model =  (DynAreaModel) m_dynAreaPlugIn.getDynAreaModel();  
        List<DynAreaCell> dynCells = new ArrayList<DynAreaCell>(Arrays.asList(model.getDynAreaCells()));
        for (;;) {                
            DynAreaMngDlg dlgDynAreaMng = new DynAreaMngDlg(getReport().getTable().getCells(),dynCells.toArray(new DynAreaCell[0])); 
//            if(bFirstLoop){
            	bFirstLoop = false;
            	switch (resultType) {
				case -1://ֱ�ӽ������ҳ�档
		            dlgDynAreaMng.show(); 
					break;
				case DynAreaMngDlg.ID_ADD:
					break;
				case DynAreaMngDlg.ID_UPDATE:
				case DynAreaMngDlg.ID_DELETE:
					//TODO 
//					dlgDynAreaMng.setCurrentSelectedDynAreaCell(getSelectDyn);
					break;
				default:
					dlgDynAreaMng.show(); 
					break;
//				}
            } 
            
            int nRs = dlgDynAreaMng.getResult();
            if (nRs == UfoDialog.ID_OK) {                    
                applyDynArea();//�����ӵġ�ɾ���ġ��͸��µĶ�̬�������õ���̬����ģ����
                break;
            } 
            if (nRs == UfoDialog.ID_CANCEL) { 
                break;
            } 
            if (nRs == DynAreaMngDlg.ID_ADD){ //��Ӷ�̬����                  
                DynAreaSetDlg dynAreaSetDlg = new DynAreaSetDlg(getReport(),getReport().getCellsModel());
                dynAreaSetDlg.setModal(false);                    
                if (dlgDynAreaMng.getDynAreaDirection() != DynAreaVO.DIRECTION_UNDEFINED) {
                    dynAreaSetDlg.setDirection(dlgDynAreaMng.getDynAreaDirection());
                    dynAreaSetDlg.setDirectionRead();                        
                }

                dynAreaSetDlg.show();
              
                if (dynAreaSetDlg.getResult() == UfoDialog.ID_OK) {                        
                    DynAreaVO newDynAreaVO = new DynAreaVO(dynAreaSetDlg.getArea(),dynAreaSetDlg.getDirection()); 
                    DynAreaCell newDynCell = new DynAreaCell(newDynAreaVO.getOriArea(),newDynAreaVO);
                    try {                                            
                        m_addedDynArea.put(newDynCell.getArea(),newDynCell);//��ӵ�����
                        dynCells.add(newDynCell);
                    } catch (Exception e) {
                         UfoPublic.sendErrorMessage(StringResource.getStringResource("uiiufofmt00001") + e.getMessage(),getReport(),null);
                    } 
                }
                continue;
            }
            if (nRs == DynAreaMngDlg.ID_UPDATE) {//�޸Ķ�̬����                      
                    DynAreaSetDlg dynAreaUpdateDlg = new DynAreaSetDlg(getReport(),getReport().getCellsModel());
                    dynAreaUpdateDlg.setModal(false);                        
                    DynAreaCell oldDynAreaCell = dlgDynAreaMng.getCurrentSelectedDynAreaCell();
                    dynAreaUpdateDlg.setArea(oldDynAreaCell.getArea());
                    dynAreaUpdateDlg.setDirection(oldDynAreaCell.getDynAreaVO().getDirection());
//                    if (model.getDynAreaSize() > 1) {//�����ǰ��̬�����Ѿ�����1���������ܸ���
                    	//�޸Ķ�̬��ʱ�������޸ķ���
                        dynAreaUpdateDlg.setDirectionRead();
//                    }
                    dynAreaUpdateDlg.show();
                    
                    if (dynAreaUpdateDlg.getResult() == UfoDialog.ID_OK) { 
                    	//����ģ����:����޸ĵ�����������������,ֱ�ӷ����¶���Map��,������ڸ��¶���Map��
                            if(m_addedDynArea.get(oldDynAreaCell.getArea()) != null){
                                m_addedDynArea.remove(oldDynAreaCell.getArea());
                                oldDynAreaCell.setArea(dynAreaUpdateDlg.getArea());
                                m_addedDynArea.put(dynAreaUpdateDlg.getArea(), oldDynAreaCell);
                            }else{  
                            	String errorStr = model.checkUpdate(oldDynAreaCell, dynAreaUpdateDlg.getArea(), dynAreaUpdateDlg.getDirection());
                                if(errorStr == null){
                                	DynAreaCell newDynAreaCell = (DynAreaCell) oldDynAreaCell.clone();
                                	newDynAreaCell.setArea(dynAreaUpdateDlg.getArea());
                                	newDynAreaCell.getDynAreaVO().setOriArea(dynAreaUpdateDlg.getArea());
                                	m_updatedDynArea.put(dynAreaUpdateDlg.getArea(), newDynAreaCell);
                                	int index = dynCells.indexOf(oldDynAreaCell);
                                	dynCells.remove(oldDynAreaCell);
                                	dynCells.add(index,newDynAreaCell);
                                }else{
                                	UfoPublic.sendErrorMessage(errorStr,getReport(),null);
                                }
                            	
                            }
                    }
                    continue;
             }       
            if (nRs == DynAreaMngDlg.ID_DELETE) {//ɾ����̬����                    
                DynAreaSetDlg dynAreaUpdateDlg = new DynAreaSetDlg(getReport(),getReport().getCellsModel());
                dynAreaUpdateDlg.setModal(false);
                DynAreaCell deleteDyaAreaCell = dlgDynAreaMng.getCurrentSelectedDynAreaCell();
//              ���ɾ�����������ӵ�����,ֱ��ɾ��,���򻺴浽m_removedDynArea�����ڻ�ԭ
                try {
                    model.checkRemove(deleteDyaAreaCell);
                    if(m_addedDynArea.get(deleteDyaAreaCell.getArea()) == null){
                            m_removedDynArea.add(deleteDyaAreaCell);
                    }else{
                        m_addedDynArea.remove(deleteDyaAreaCell.getArea());
                    }
                    dynCells.remove(deleteDyaAreaCell);
                } catch (Exception e) {
                    UfoPublic.sendErrorMessage(StringResource.getStringResource("uiiufofmt00002") + e.getMessage(),getReport(),e);
                }                    
                continue;
         }       
        }     
    }
    
    /**
     * ���еĲ�������m_dynAreaPlugIn��DynAreaMngDlg�ĸ���ģ��cloneModel�н���,���û����DynAreaMngDlg��ȷ����ť��
     * ��cloneModel�е��������õ�m_dynAreaPlugIn��DynAreaMngDlg��.  
    
     * 
     */
    public Object[] getParams(UfoReport report) {
    	dynAreaMngImpl(report, -1);
    	return null;
    }
    
    /**
     * @return ���ر����ߡ�
     */
    private UfoReport getReport() {
        return this.getDynAreaPlugIn().getReport();
    }
    
    /**
     * @return ���ض�̬��������
     */
    private DynAreaDefPlugIn getDynAreaPlugIn() {
        return m_dynAreaPlugIn;
    }


    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(StringResource.getStringResource("miufo1001666"));
        uiDes.setPaths(new String[]{MultiLang.getString("format")});
        return new ActionUIDes[]{uiDes};
    }
}
 