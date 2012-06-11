/*
 * 创建日期 2006-4-6
 *
 */
package com.ufsoft.iufo.inputplugin.biz.data;

import java.awt.Component;

import nc.pub.iufo.cache.base.UnitCache;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.vo.iufo.unit.UnitInfoVO;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.biz.AbsIufoUIBizMenuExt;
import com.ufsoft.iufo.inputplugin.biz.file.ChangeKeywordsExt;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
/**
 * 导入Iufo数据的IActionExt实现类
 * 
 * @author liulp
 *
 */
public class ImportIufoDataExt extends AbsIufoUIBizMenuExt implements IUfoContextKey{
    
    private boolean m_bRepCanModify = true;
    public boolean isRepCanModify() {
        return m_bRepCanModify;
    }

    public void setRepCanModify(boolean isRepCanModify) {
        this.m_bRepCanModify = isRepCanModify;
    }
    
    public ImportIufoDataExt(UfoReport ufoReport) {
        super(ufoReport);
    }

    @Override
	protected String getGroup() {
		return "impAndExp";
	}
    
    protected String[] getPaths() {
        return doGetImportMenuPaths();
    }

    protected String getMenuName() {
        return  MultiLangInput.getString("uiufotableinput0010");//"IUFO数据";
    }

    protected UfoCommand doGetCommand(UfoReport ufoReport) {
        return new ImportIufoDataCmd(ufoReport);
    }
    /**
     * modify by guogang 2007-11-29 在进行倒入IUFO数据的时候，进行关键字特别是单位检查的时候，应该是登陆单位
     */
    public Object[] getParams(UfoReport container) {
    	TableInputContextVO contextVO=null;
    	if(container.getContextVo() instanceof TableInputContextVO){
    	contextVO= (TableInputContextVO)container.getContextVo();
    	Object tableInputTransObj = contextVO.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj tableInput = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
		
    	if(tableInput!=null&&tableInput.getRepDataParam()!=null){
    		String strLoginUnitCode = contextVO.getAttribute(LOGIN_UNIT_CODE) == null ? null : (String)contextVO.getAttribute(LOGIN_UNIT_CODE);
    		UnitCache unitCache = IUFOUICacheManager.getSingleton().getUnitCache();
    		UnitInfoVO unitInfo=unitCache.getUnitInfoByCode(strLoginUnitCode);
    		tableInput.getRepDataParam().setOperUnitPK(unitInfo.getPK());
    	}
    	}
        return ChangeKeywordsExt.doGetInputKeyValus(container,container);
    }

    protected String getImageFile() {
        return null;
    }
    protected boolean isInAddToolBar(){
        return false;
    }    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return super.isEnabled(focusComp)&&isRepCanModify();
    }
}
