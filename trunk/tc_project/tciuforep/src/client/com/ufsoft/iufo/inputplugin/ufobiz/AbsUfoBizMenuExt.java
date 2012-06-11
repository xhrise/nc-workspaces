package com.ufsoft.iufo.inputplugin.ufobiz;

import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufsoft.report.util.MultiLang;

public abstract class AbsUfoBizMenuExt extends AbstractPluginAction {

    /**
     * �õ������ݡ��˵�·��
     * @return
     */
    protected String[] doGetDataMenuPaths(String strGroup){
        return new String[]{MultiLang.getString("data"),strGroup};//"����"
    }
    
    /**
     * �õ������ݣ�>���롱�˵�·��
     * @return
     */
    protected String[] doGetImportMenuPaths(String strGroup){
        return new String[]{MultiLang.getString("data"),MultiLang.getString("import"),strGroup};//"����","����"
    }
    
    /**
     * �õ������ݣ�>�������˵�·��
     * @return
     */
    protected String[] doGetExportMenuPaths(String strGroup){
        return new String[]{MultiLang.getString("data"),MultiLang.getString("export"),strGroup};//"����","����"
    }
    
    protected RepDataEditor getRepDataEditor(){
    	return (RepDataEditor)getCurrentView();
    }
}
