package com.ufsoft.report.sysplugin.file;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.EventObject;

import javax.swing.JOptionPane;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * 
 * @author zzl 2005-5-24
 */
public class FilePlugin extends AbstractPlugIn{

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#startup()
     */
    public void startup() {
        // TODO �Զ����ɷ������

    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#shutdown()
     */
    public void shutdown() {
        // TODO �Զ����ɷ������

    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#store()
     */
    public void store() {
		String filePath = "C:\\SeriralReport.rep";
		SerializeReport(filePath);
		setDirty(false);

    }
    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getSupportData()
     */
    public String[] getSupportData() {
        // TODO �Զ����ɷ������
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDataRender(java.lang.String)
     */
    public SheetCellRenderer getDataRender(String extFmtName) {
        // TODO �Զ����ɷ������
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDataEditor(java.lang.String)
     */
    public SheetCellEditor getDataEditor(String extFmtName) {
        // TODO �Զ����ɷ������
        return null;
    }

    /*
     * @see com.ufsoft.table.UserActionListner#actionPerform(com.ufsoft.table.UserUIEvent)
     */
    public void actionPerform(UserUIEvent e) {
        // TODO �Զ����ɷ������

    }

    /*
     * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
     */
    public String isSupport(int source, EventObject e)
            throws ForbidedOprException {
        // TODO �Զ����ɷ������
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
     */
    public IPluginDescriptor createDescriptor() {
        // TODO �Զ����ɷ������
        return new AbstractPlugDes(this){

            protected IExtension[] createExtensions() {
        		//�ļ���>�򿪣��رգ����棬���|�������|ҳ�����ã���ӡԤ������ӡ    
        	    ICommandExt extOpen = new FileOpenExt(getReport());//��	
        	    ICommandExt extOpenToolBar = new FileOpenExt(getReport()){
        	        /*
        	         * @see com.ufsoft.report.plugin.IActionExt#getUIDes()
        	         */
        	        public ActionUIDes getUIDes() {
        	            ActionUIDes uiDes = super.getUIDes();
        	            uiDes.setToolBar(true);
        	            return uiDes;
        	        }
        	    };//��	
        		ICommandExt extClose = new FileCloseExt(getReport());//�ر�
        		ICommandExt extSave = new FileSaveExt(getReport());//����	
        		
                return new IExtension[]{extOpen, extClose, extSave};
            }
            
        };
    }
   
	/**
	 * ���л��洢����
	 * 
	 * @param filePath
	 *            CaiJie 2004-10-19
	 */
	private void SerializeReport(String filePath) {
		if (filePath == null)
			return;
		try {
			CellsModel cellsModel = getReport().getCellsModel();

			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(filePath));
			out.writeObject(cellsModel);
			out.close();
		} catch (Exception e) {
			AppDebug.debug(e);
			JOptionPane.showMessageDialog(getReport(), e.getMessage());
		}
	}
}
