/*
 * DynAreaSetExt.java
 * 创建日期 2005-1-27
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
     * 新添加的对象缓存AreaPosition-DynAreaCell
     */
    private Hashtable m_addedDynArea = new Hashtable();
    /**
     * 删除的对象患处:如果删除的对象是上一次调用updateReport()后新添加的,直接删除不放入此Map中
     */
    private ArrayList m_removedDynArea = new ArrayList();
    /**
     * 修改前对象缓存:AreaPosition-DynAreaCell
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
     * 将增加的、删除的、和更新的动态区域设置到动态区域模型中
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
            //删除动态区公式链
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
     * 动态区域设置实现类。
     * @param report
     * @param resultType -1为直接进入管理页面；DynAreaMngDlg.ID_ADD为右键添加动态区，DynAreaMngDlg.ID_UPDATE为修改动态区，DynAreaMngDlg.ID_DELETE为删除动态区。
     * @i18n uiiufofmt00001=不能添加动态区域:
     * @i18n uiiufofmt00002=不能删除动态区域:
     */
    protected void dynAreaMngImpl(UfoReport report, int resultType){
        m_addedDynArea.clear();
        m_updatedDynArea.clear();
        m_removedDynArea.clear();
        
        boolean bFirstLoop = true;
        
        //得到动态区域实体链            
        DynAreaModel model =  (DynAreaModel) m_dynAreaPlugIn.getDynAreaModel();  
        List<DynAreaCell> dynCells = new ArrayList<DynAreaCell>(Arrays.asList(model.getDynAreaCells()));
        for (;;) {                
            DynAreaMngDlg dlgDynAreaMng = new DynAreaMngDlg(getReport().getTable().getCells(),dynCells.toArray(new DynAreaCell[0])); 
//            if(bFirstLoop){
            	bFirstLoop = false;
            	switch (resultType) {
				case -1://直接进入管理页面。
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
                applyDynArea();//将增加的、删除的、和更新的动态区域设置到动态区域模型中
                break;
            } 
            if (nRs == UfoDialog.ID_CANCEL) { 
                break;
            } 
            if (nRs == DynAreaMngDlg.ID_ADD){ //添加动态区域                  
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
                        m_addedDynArea.put(newDynCell.getArea(),newDynCell);//添加道缓存
                        dynCells.add(newDynCell);
                    } catch (Exception e) {
                         UfoPublic.sendErrorMessage(StringResource.getStringResource("uiiufofmt00001") + e.getMessage(),getReport(),null);
                    } 
                }
                continue;
            }
            if (nRs == DynAreaMngDlg.ID_UPDATE) {//修改动态区域                      
                    DynAreaSetDlg dynAreaUpdateDlg = new DynAreaSetDlg(getReport(),getReport().getCellsModel());
                    dynAreaUpdateDlg.setModal(false);                        
                    DynAreaCell oldDynAreaCell = dlgDynAreaMng.getCurrentSelectedDynAreaCell();
                    dynAreaUpdateDlg.setArea(oldDynAreaCell.getArea());
                    dynAreaUpdateDlg.setDirection(oldDynAreaCell.getDynAreaVO().getDirection());
//                    if (model.getDynAreaSize() > 1) {//如果当前动态区域已经多于1个，方向不能更改
                    	//修改动态区时不允许修改方向。
                        dynAreaUpdateDlg.setDirectionRead();
//                    }
                    dynAreaUpdateDlg.show();
                    
                    if (dynAreaUpdateDlg.getResult() == UfoDialog.ID_OK) { 
                    	//加入模型中:如果修改的是新添入的区域对象,直接放在新对象Map中,否则放在更新对象Map中
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
            if (nRs == DynAreaMngDlg.ID_DELETE) {//删除动态区域                    
                DynAreaSetDlg dynAreaUpdateDlg = new DynAreaSetDlg(getReport(),getReport().getCellsModel());
                dynAreaUpdateDlg.setModal(false);
                DynAreaCell deleteDyaAreaCell = dlgDynAreaMng.getCurrentSelectedDynAreaCell();
//              如果删除的是新增加的区域,直接删除,否则缓存到m_removedDynArea中用于还原
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
     * 所有的操作都在m_dynAreaPlugIn的DynAreaMngDlg的复制模型cloneModel中进行,当用户点击DynAreaMngDlg的确定按钮后
     * 将cloneModel中的数据设置到m_dynAreaPlugIn的DynAreaMngDlg中.  
    
     * 
     */
    public Object[] getParams(UfoReport report) {
    	dynAreaMngImpl(report, -1);
    	return null;
    }
    
    /**
     * @return 返回报表工具。
     */
    private UfoReport getReport() {
        return this.getDynAreaPlugIn().getReport();
    }
    
    /**
     * @return 返回动态区域插件。
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
 