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
 * ���ڼ���״̬�ı�ļ�����
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
				
				//�˴�����ڵ�λ���򱨱�������ʱ������л���λ�򱨱���������ұ༭����ʧ�����
				if (tree!=null && tree.getRootPane()!=null && tree.getRootPane().getLayeredPane()!=null){
					int iCount=tree.getRootPane().getLayeredPane().getComponentCount();
					for (int i=0;i<iCount;i++){
						Component comp=tree.getRootPane().getLayeredPane().getComponent(i);
						//��SearchInputPanel�������ұ༭�������
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
		
		//ȡ���ϴλ�Ծ�ı������ݴ���
		RepDataEditor oldEditor=controler.getLastActiveRepDataEditor();
		if (oldActiveView!=null && oldActiveView instanceof RepDataEditor)
			oldEditor=(RepDataEditor)oldActiveView;
		
		if (oldEditor!=null && oldEditor.getKeyCondPane()!=null)
			oldEditor.getKeyCondPane().doQueryWhenKeyValueChange(new FocusEvent(oldEditor,FocusEvent.FOCUS_GAINED));
		
		//�����µı������ݻ�Ծ����
		controler.setLastActiveRepDataEditor(newEditor);
		
		//��һЩ������Ҫ�ڴ��ڼ����ִ�У��˴����˴���
		newEditor.afterEditorActive();
		
		//������λ�Ծ�ĵĴ��ڲ���ͬ����ˢ����˽���빫ʽ׷�ٴ���
		if (newEditor!=oldEditor){
			controler.doRefreshCheckResultPane(mainBoard);
			
			if(controler.isCanFormulaTrace()){
				CellPosition selPos = newEditor.getCellsModel().getSelectModel().getSelectedArea().getStart();
				WindowNavUtil.refreshNavPanel(newEditor, selPos);
				newEditor.setTraceCells(null);
			}
		}
		
		//���֧����������λ���뱨�����Զ���λ
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
