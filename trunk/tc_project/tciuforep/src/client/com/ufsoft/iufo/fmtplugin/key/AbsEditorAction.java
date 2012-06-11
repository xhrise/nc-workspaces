package com.ufsoft.iufo.fmtplugin.key;

import java.awt.Container;

import javax.swing.SwingUtilities;

import com.ufida.dataset.IContext;
import com.ufida.zior.view.Viewer;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;

/**
 * 适配新的UI框架，需要通过编辑器打开设置界面的插件需要实现此类
 * @author wangyga
 * @created at 2009-3-11,下午02:10:58
 *
 */
public abstract class AbsEditorAction {

	Container parent = null;

	public AbsEditorAction(Container container) {
        if(container == null){
        	new IllegalArgumentException();
        }
		this.parent = container;
	}

	protected CellsModel getCellsModel() {
		if (parent instanceof UfoReport) {
			return ((UfoReport) parent).getCellsModel();
		} else if (parent instanceof CellsPane) {
			return ((CellsPane) parent).getDataModel();
		}
		return null;
	}
	
	protected CellsPane getCellsPane(){
		if (parent instanceof UfoReport) {
			return ((UfoReport) parent).getTable().getCells();
		} else if (parent instanceof CellsPane) {
			return (CellsPane)parent;
		}
		return null;
	}

	protected IContext getContextVo() {
		if (parent instanceof UfoReport) {
			return ((UfoReport) parent).getContextVo();
		} else if (parent instanceof CellsPane) {
			UfoReport report = (UfoReport)SwingUtilities.getAncestorOfClass(
					UfoReport.class, parent);
			if(report != null){
				return report.getContextVo();
			}else{
				Viewer viewer = (Viewer) SwingUtilities.getAncestorOfClass(
						Viewer.class, parent);
				if (viewer != null) {
					return viewer.getContext();
				}
			}			
		}
		return null;
	}

	protected Container getParent(){
		return parent;
	}
	
	public abstract Object[] getParams();

	public abstract void execute(Object[] params);

}
