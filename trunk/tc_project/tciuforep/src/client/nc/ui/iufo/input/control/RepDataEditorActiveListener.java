package nc.ui.iufo.input.control;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.FocusEvent;

import nc.ui.iufo.input.edit.RepDataEditor;
import nc.ui.iufo.input.view.NavRepTreeViewer;
import nc.ui.iufo.input.view.NavUnitTreeViewer;
import nc.ui.iufo.pub.UfoPublic;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.tree.SearchInputPanel;

import com.ufida.zior.view.Mainboard;
import com.ufida.zior.view.Viewer;
import com.ufida.zior.view.event.ViewerListener;
import com.ufsoft.iufo.inputplugin.biz.WindowNavUtil;
import com.ufsoft.table.CellPosition;

/**
 * 窗口激活状态改变的监听类
 * @author weixl
 *
 */
public class RepDataEditorActiveListener extends ViewerListener.Sub {

	public void onActive(Viewer newActiveView, Viewer oldActiveView) {			
		if (newActiveView==null || newActiveView instanceof RepDataEditor ==false){
			if (newActiveView!=null){
				UITree tree=null;
				if (newActiveView instanceof NavUnitTreeViewer)
					tree=(UITree)((NavUnitTreeViewer)newActiveView).getTree();
				
				if (newActiveView instanceof NavRepTreeViewer)
					tree=(UITree)((NavRepTreeViewer)newActiveView).getTree();
				
				//此处解决在单位树或报表树查找时，如果切换单位或报表树，则查找编辑框不消失的情况
				if (tree!=null && tree.getRootPane()!=null && tree.getRootPane().getLayeredPane()!=null){
					int iCount=tree.getRootPane().getLayeredPane().getComponentCount();
					for (int i=0;i<iCount;i++){
						Component comp=tree.getRootPane().getLayeredPane().getComponent(i);
						//将SearchInputPanel，即查找编辑框清除掉
						if (comp instanceof SearchInputPanel){
							tree.getRootPane().getLayeredPane().remove(comp);
							tree.getRootPane().getLayeredPane().paintImmediately(comp.getBounds());
							tree.requestFocus();
							break;
						}
					}	
				}
				
				class Refresh implements Runnable{
					UITree tree;
					Refresh(UITree tree){
						this.tree=tree;
					}
					
					public void run() {
						tree.requestFocus();
					}
				}
				if (tree!=null){
					EventQueue.invokeLater(new Refresh(tree));
				}
			}
			return;
		}
		
		Mainboard mainBoard=newActiveView.getMainboard();
		RepDataEditor newEditor=(RepDataEditor)newActiveView;
		RepDataControler controler=RepDataControler.getInstance(newEditor.getMainboard());
		
		//取得上次活跃的报表数据窗口
		RepDataEditor oldEditor=controler.getLastActiveRepDataEditor();
		if (oldActiveView!=null && oldActiveView instanceof RepDataEditor)
			oldEditor=(RepDataEditor)oldActiveView;
		
		if (oldEditor!=null && oldEditor.getKeyCondPane()!=null)
			oldEditor.getKeyCondPane().doQueryWhenKeyValueChange(new FocusEvent(oldEditor,FocusEvent.FOCUS_GAINED));
		
		//设置新的报表数据活跃窗口
		controler.setLastActiveRepDataEditor(newEditor);
		
		//有一些处理需要在窗口激活后执行，此处做此处理
		newEditor.afterEditorActive();
		
		//如果两次活跃的的窗口不相同，则刷新审核结果与公式追踪窗口
		if (newEditor!=oldEditor){
			controler.doRefreshCheckResultPane(mainBoard);
			
			if(controler.isCanFormulaTrace()){
				CellPosition selPos = newEditor.getCellsModel().getSelectModel().getSelectedArea().getStart();
				WindowNavUtil.refreshNavPanel(newEditor, selPos);
				newEditor.setTraceCells(null);
			}
		}
		
		//如果支持联动，则单位树与报表树自动定位
		boolean bUnitChange=false;
		if (mainBoard.getView(RepDataControler.NAV_UNIT_TREE_ID)!=null && newEditor.getPubData()!=null && newEditor.getPubData().getUnitPK()!=null){
			bUnitChange=!UfoPublic.strIsEqual(controler.getSelectedUnitPK(),newEditor.getPubData().getUnitPK());
			controler.setSelectedUnitPK(newEditor.getPubData().getUnitPK());
			((NavUnitTreeViewer)mainBoard.getView(RepDataControler.NAV_UNIT_TREE_ID)).selectUnitTreeNode();
		}
		
		if (mainBoard.getView(RepDataControler.NAV_REP_TREE_ID)!=null && newEditor.getRepPK()!=null && newEditor.getTaskPK()!=null){
			controler.setSelectedRepPK(newEditor.getRepPK());
			controler.setSelectedTaskPK(newEditor.getTaskPK());
			if (bUnitChange)
				((NavRepTreeViewer)mainBoard.getView(RepDataControler.NAV_REP_TREE_ID)).refresh();
			((NavRepTreeViewer)mainBoard.getView(RepDataControler.NAV_REP_TREE_ID)).selectRepTreeNode();
		}
	}
}
