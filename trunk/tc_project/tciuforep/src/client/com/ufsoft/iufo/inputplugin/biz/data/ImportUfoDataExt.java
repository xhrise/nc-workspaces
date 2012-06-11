/*
 * 创建日期 2006-4-6
 *
 */
package com.ufsoft.iufo.inputplugin.biz.data;

import java.awt.Component;

import com.ufsoft.iufo.inputplugin.biz.AbsIufoOpenedRepBizMenuExt;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

/**
 * 导入Ufo数据的IActionExt实现类
 * 
 * @author liulp
 *
 */
public class ImportUfoDataExt extends AbsIufoOpenedRepBizMenuExt{
    public ImportUfoDataExt(UfoReport ufoReport) {
        super(ufoReport);
    }
    private boolean m_bCanImportUfoData = false;
    private boolean hasRepCanModify = true;
   
    private boolean isHasRepCanModify(){
        return hasRepCanModify;
    }
    public void setHasRepCanModify(boolean bHasRepCanModify){
        this.hasRepCanModify = bHasRepCanModify;
    }

    public void setIsCanImportUfoData(boolean bIsCanImportUfoData){
        this.m_bCanImportUfoData = bIsCanImportUfoData;
    }
    
    private boolean isCanImportUfoData(){
        return this.m_bCanImportUfoData;
    }
    protected String[] getPaths() {
        return doGetImportMenuPaths();
    }

    protected String getMenuName() {
        return  MultiLangInput.getString("uiufotableinput0011");//"Ufo数据";
    }

    protected UfoCommand doGetCommand(UfoReport ufoReport) {
        return new ImportUfoDataCmd(ufoReport);
    }
    
    public Object[] getParams(UfoReport container) {
        return null;
    }
    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return super.isEnabled(focusComp) && isHasRepCanModify() && isCanImportUfoData();
    }
}
