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
        // TODO 自动生成方法存根

    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#shutdown()
     */
    public void shutdown() {
        // TODO 自动生成方法存根

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
        // TODO 自动生成方法存根
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDataRender(java.lang.String)
     */
    public SheetCellRenderer getDataRender(String extFmtName) {
        // TODO 自动生成方法存根
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDataEditor(java.lang.String)
     */
    public SheetCellEditor getDataEditor(String extFmtName) {
        // TODO 自动生成方法存根
        return null;
    }

    /*
     * @see com.ufsoft.table.UserActionListner#actionPerform(com.ufsoft.table.UserUIEvent)
     */
    public void actionPerform(UserUIEvent e) {
        // TODO 自动生成方法存根

    }

    /*
     * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
     */
    public String isSupport(int source, EventObject e)
            throws ForbidedOprException {
        // TODO 自动生成方法存根
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
     */
    public IPluginDescriptor createDescriptor() {
        // TODO 自动生成方法存根
        return new AbstractPlugDes(this){

            protected IExtension[] createExtensions() {
        		//文件－>打开，关闭，保存，另存|清除缓存|页面设置，打印预览，打印    
        	    ICommandExt extOpen = new FileOpenExt(getReport());//打开	
        	    ICommandExt extOpenToolBar = new FileOpenExt(getReport()){
        	        /*
        	         * @see com.ufsoft.report.plugin.IActionExt#getUIDes()
        	         */
        	        public ActionUIDes getUIDes() {
        	            ActionUIDes uiDes = super.getUIDes();
        	            uiDes.setToolBar(true);
        	            return uiDes;
        	        }
        	    };//打开	
        		ICommandExt extClose = new FileCloseExt(getReport());//关闭
        		ICommandExt extSave = new FileSaveExt(getReport());//保存	
        		
                return new IExtension[]{extOpen, extClose, extSave};
            }
            
        };
    }
   
	/**
	 * 序列化存储报表
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
