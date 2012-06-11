/*
 * �������� 2006-4-5
 *
 */
package com.ufsoft.iufo.inputplugin.biz;

import com.ufsoft.iufo.inputplugin.biz.file.SaveRepDataExt;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPlugIn;
/**
 * ����¼����ļ��˵���������
 * 
 * @author liulp
 *
 */
public class InputFilePlugDes extends AbstractPlugDes{
	
    protected InputFilePlugDes(IPlugIn plugin) {
        super(plugin);
    }

    protected IExtension[] createExtensions() {
        //����ѡ��
//    	ICommandExt chooseReport = new ChooseRepExt(getReport());
//        //�˳�
//        ICommandExt extClose = new FileCloseExt(getReport());   
        //�л��ؼ���
//        ICommandExt changeKeywordsExt = new ChangeKeywordsExt(getReport());    
        //����Դ��Ϣ����
        ICommandExt dsInfoSetExt = new DSInfoSetExt(getReport());       
        
        //����(����)
        ICommandExt extSave = new SaveRepDataExt(getReport());
        return new IExtension[]{extSave, dsInfoSetExt};

    }

}
