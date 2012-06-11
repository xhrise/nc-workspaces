package com.ufsoft.iufo.inputplugin.ufobiz;

import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufsoft.report.util.MultiLang;

public abstract class AbsUfoBizMenuExt extends AbstractPluginAction {

    /**
     * 得到“数据”菜单路径
     * @return
     */
    protected String[] doGetDataMenuPaths(String strGroup){
        return new String[]{MultiLang.getString("data"),strGroup};//"数据"
    }
    
    /**
     * 得到“数据－>导入”菜单路径
     * @return
     */
    protected String[] doGetImportMenuPaths(String strGroup){
        return new String[]{MultiLang.getString("data"),MultiLang.getString("import"),strGroup};//"数据","导入"
    }
    
    /**
     * 得到“数据－>导出”菜单路径
     * @return
     */
    protected String[] doGetExportMenuPaths(String strGroup){
        return new String[]{MultiLang.getString("data"),MultiLang.getString("export"),strGroup};//"数据","导出"
    }
    
    protected RepDataEditor getRepDataEditor(){
    	return (RepDataEditor)getCurrentView();
    }
}
