package com.ufsoft.report.sysplugin.headersize;

import java.util.EventObject;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * 行、列设置插件
 * 实现的功能有：行高、列宽的设置功能
 *            隐藏行、列的功能
 *            取消隐藏行、列的功能
 * @author zzl 2005-5-25
 */
public class HeaderSizePlugin extends AbstractPlugIn {


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
        // TODO 自动生成方法存根

    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#isDirty()
     */
    public boolean isDirty() {
        // TODO 自动生成方法存根
        return false;
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
            	ICommandExt rowSizeExt = new AbsActionExt(){
					public UfoCommand getCommand() {
						return null;
					}
					@Override
					public Object[] getParams(UfoReport container) {
						return null;
					}
					@Override
					public ActionUIDes[] getUIDesArr() {
						ActionUIDes uiDes = new ActionUIDes();
						uiDes.setName(MultiLang.getString("Row"));
						uiDes.setPaths(new String[]{MultiLang.getString("format")});
						uiDes.setDirectory(true);
						uiDes.setGroup("headerSizeExt");
						return new ActionUIDes[]{uiDes};
					}					
				};
				ICommandExt colSizeExt = new AbsActionExt(){
					public UfoCommand getCommand() {
						return null;
					}
					@Override
					public Object[] getParams(UfoReport container) {
						return null;
					}
					@Override
					public ActionUIDes[] getUIDesArr() {
						ActionUIDes uiDes = new ActionUIDes();
						uiDes.setName(MultiLang.getString("Column"));
						uiDes.setPaths(new String[]{MultiLang.getString("format")});
						uiDes.setDirectory(true);
						uiDes.setGroup("headerSizeExt");
						return new ActionUIDes[]{uiDes};
					}					
				};
        		ICommandExt extSetRowHeight = new SetRowHeightExt(getReport()); 
        		ICommandExt extSetColWidth = new SetColWidthExt(getReport());
        		ICommandExt setRowPreferredSizeExt = new SetRowPreferredSizeExt(getReport()); 
        		ICommandExt setColPreferredSizeExt = new SetColPreferredSizeExt(getReport());
        		// add by guogang 2007-6-14 增加隐藏和取消隐藏行、列的功能
        		ICommandExt hiddenRows = new HiddenRowsExt(getReport());
        		ICommandExt hiddenCols = new HiddenColsExt(getReport());
        		ICommandExt cancelHiddenRows = new CancelHiddenRowExt(getReport());
        		ICommandExt cancelHiddenCols = new CancelHiddenColExt(getReport());
        		// add end
                return new IExtension[]{
//                		rowSizeExt,
//                		colSizeExt,
                		extSetRowHeight, extSetColWidth, setRowPreferredSizeExt, setColPreferredSizeExt, hiddenRows,hiddenCols,cancelHiddenRows,cancelHiddenCols};
            }
            
        };
    }
}
