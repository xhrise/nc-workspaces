/*
 * 创建日期 2006-4-13
 *
 */
package com.ufsoft.iufo.inputplugin.biz;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.ActionUIDes;

public abstract class AbsIufoUIBizMenuExt extends AbsIufoOpenedRepBizMenuExt {
    /**
     * @param ufoReport
     */
    public AbsIufoUIBizMenuExt(UfoReport ufoReport){
        super(ufoReport);
    }
    protected abstract String getImageFile();
    
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setName(getMenuName());
        uiDes1.setPaths(getPaths());
        uiDes1.setImageFile(getImageFile());
        uiDes1.setGroup(getGroup());
        
        //工具栏上添加
        if(!isInAddToolBar()){
            return new ActionUIDes[]{uiDes1};
        } else{
            ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
            uiDes2.setPaths(new String[]{});
            uiDes2.setToolBar(true);
            uiDes2.setGroup(getGroup());
            return new ActionUIDes[]{uiDes1,uiDes2};
        }
    }
    
    protected boolean isInAddToolBar(){
        return true;
    }

    public abstract Object[] getParams(UfoReport container);

}
