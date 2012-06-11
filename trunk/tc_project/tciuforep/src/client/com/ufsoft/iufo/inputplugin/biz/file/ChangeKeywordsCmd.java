/*
 * 创建日期 2006-4-6
 *
 */
package com.ufsoft.iufo.inputplugin.biz.file;

import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.TaskCache;
import nc.pub.iufo.cache.base.UnitCache;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.task.TaskVO;
import nc.vo.iufo.unit.UnitInfoVO;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.biz.AbsIufoBizCmd;
import com.ufsoft.iufo.inputplugin.biz.IInputBizOper;
import com.ufsoft.iufo.inputplugin.biz.RepSelectionPlugIn;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.iuforeport.tableinput.applet.TableInputAuth;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.UFOTable;
/**
 * 切换关键字命令
 * 
 * @author liulp
 *
 */
public class ChangeKeywordsCmd extends AbsIufoBizCmd implements IUfoContextKey{
	UfoReport m_ufoReport = null;

    public ChangeKeywordsCmd(UfoReport ufoReport) {
        super(ufoReport);
    	m_ufoReport = ufoReport;
    }
    
    protected  boolean isNeedCheckAloneID(){
        return false;        
    }
    protected boolean isNeedCheckReportPK(){
        return false;
    }
    
    protected boolean isValidParams(Object[] params) {
        //传入的参数是录入的各个关键字的值
        if(params == null){
            return false;
        }
        return true;
    }

    protected boolean isNeedCheckParams() {
        return true;
    }
    protected void executeIUFOBizCmd(UfoReport ufoReport, Object[] params) {
        
        String[] strKeyValues = (String[])params;
        Object tableInputTransObj = ((TableInputContextVO)ufoReport.getContextVo()).getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj tableInputTrans = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
		
//        TableInputTransObj tableInputTransObj = ((TableInputContextVO)ufoReport.getContextVo()).getInputTransObj();
        KeyVO[] selKeyVOs = getKeyVOs(tableInputTrans.getRepDataParam().getTaskPK());
        for(int i = 0; i< selKeyVOs.length; i++){
        	if(selKeyVOs[i].getKeywordPK().equals(KeyVO.CORP_PK)){
        		UnitCache unitCache = IUFOUICacheManager.getSingleton().getUnitCache();
        		UnitInfoVO unitInfoVO = unitCache.getUnitInfoByCode(strKeyValues[i]);
        		tableInputTrans.getRepDataParam().setOperUnitPK(unitInfoVO.getPK());
        		break;
        	}
        }
        
        //如果没有可选择的报表(包括该任务没有定义报表，或者对该任务中的报表不具有数据权限)，则显示一个空表
        int nRepOfNavPanel = initRepNavPanel();
        if(nRepOfNavPanel == 0){
        	UFOTable table = UFOTable.createFiniteTable(50,20);
            CellsModel cellsModel = table.getCellsModel();
            cellsModel.setDirty(false);
            m_ufoReport.getTable().setCurCellsModel(cellsModel);
            m_ufoReport.setReadOnly(true, new TableInputAuth(m_ufoReport));
            return;
        } 
        
        IInputBizOper inputMenuOper = new InputChangeKeywordsOper(ufoReport, strKeyValues);
        inputMenuOper.performBizTask(ITableInputMenuType.BIZ_TYPE_CHANGEKEYWORDDSUBMIT);      
    }

    private static KeyVO[] getKeyVOs(String strTaskPK){
        TaskVO taskVO = getTaskVO(strTaskPK);
         KeyVO[] keyVOs = null;
         if(taskVO != null){
             String strKeyGroupID = taskVO.getKeyGroupId();
             KeyGroupCache keyGroupCache = IUFOUICacheManager.getSingleton().getKeyGroupCache();
             KeyGroupVO keyGroupVO = keyGroupCache.getByPK(strKeyGroupID);
             if(keyGroupVO != null){
                 keyVOs = keyGroupVO.getKeys();
             }
         }
         return keyVOs;
     }
    
    private static TaskVO getTaskVO(String strTaskPK){
        TaskCache taskCache = IUFOUICacheManager.getSingleton().getTaskCache();
        TaskVO taskVO = taskCache.getTaskVO(strTaskPK);
        return taskVO;
    }
    
    private int initRepNavPanel(){
    	RepSelectionPlugIn repSelectionPlugIn = (RepSelectionPlugIn)m_ufoReport.getPluginManager().getPlugin(RepSelectionPlugIn.class.getName());
    	if(repSelectionPlugIn != null){
    		ChooseRepExt2 chooseReport = (ChooseRepExt2)(repSelectionPlugIn.getDescriptor().getExtensions()[0]);
    		return chooseReport.initRepNavPanel();    		
    	}
    	
    	return 0;
    }
}
