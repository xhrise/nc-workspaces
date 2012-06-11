/*
 * 创建日期 2006-4-5
 *
 */
package com.ufsoft.iufo.inputplugin.biz;

import com.ufsoft.iufo.inputplugin.biz.file.SaveRepDataExt;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPlugIn;
/**
 * 报表录入的文件菜单组插件描述
 * 
 * @author liulp
 *
 */
public class InputFilePlugDes extends AbstractPlugDes{
	
    protected InputFilePlugDes(IPlugIn plugin) {
        super(plugin);
    }

    protected IExtension[] createExtensions() {
        //报表选择
//    	ICommandExt chooseReport = new ChooseRepExt(getReport());
//        //退出
//        ICommandExt extClose = new FileCloseExt(getReport());   
        //切换关键字
//        ICommandExt changeKeywordsExt = new ChangeKeywordsExt(getReport());    
        //数据源信息配置
        ICommandExt dsInfoSetExt = new DSInfoSetExt(getReport());       
        
        //保存(数据)
        ICommandExt extSave = new SaveRepDataExt(getReport());
        return new IExtension[]{extSave, dsInfoSetExt};

    }

}
