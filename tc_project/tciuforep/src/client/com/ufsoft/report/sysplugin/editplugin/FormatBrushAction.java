package com.ufsoft.report.sysplugin.editplugin;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.ICompentFactory;
import com.ufida.zior.plugin.IPlugin;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.EditParameter;
import com.ufsoft.table.event.CellsModelSelectedListener;
import com.ufsoft.table.event.MouseEventAdapter;
import com.ufsoft.table.event.MouseListenerAdapter;
import com.ufsoft.table.re.BorderPlayRender;

public class FormatBrushAction extends EditAction implements CellsModelSelectedListener{

	/** �жϸ�ʽˢ�Ƿ���Ҫ�����Ĳ��� */
	private boolean b_isSeries = false;
	/** ��ʽˢ��ʶ */
	private boolean b_isUseBrush = false;

    public FormatBrushAction(IPlugin plugin){
    	plugin.getMainboard().getEventManager().addListener(createMouseListener());
    }
    
	@Override
	protected int getClipType() {
		return EditParameter.CELL_FORMAT;
	}

	@Override
	protected int getEditType() {
		return EditParameter.COPY;
	}
	
	@Override
	public void execute(ActionEvent e) {
		if (!b_isSeries) {
			BorderPlayRender.controlPlay(getCellsPane());// �������Ʒ���
		}
		copy(getClipType(), getEditType());
		b_isUseBrush = true;// �ı��ʽˢ��״̬:����
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor des = new PluginActionDescriptor(MultiLang.getString("miufo1004056"));
		des.setExtensionPoints(XPOINT.TOOLBAR);
		des.setGroupPaths(new String[]{MultiLang.getString("edit")});
		des.setIcon("/images/reportcore/format_brush.gif");
		des.setCompentFactory(createComponentFactory());
		des.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.SHIFT_MASK + KeyEvent.CTRL_MASK));
		return des;
	}

	private AbsPasteExecutor getChoosePaste(){
		return new PasteFormat(getMainboard(), false);
	}
		
	private ICompentFactory createComponentFactory(){
		return new DefaultCompentFactory(){

			public JComponent createComponet(XPOINT point, String[] paths,
					JComponent root, AbstractAction action) {
				 final JButton btn = (JButton)super.createToolBarItem(paths, root, action);
		            btn.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							CellsPane cellsPane = getCellsPane();
							if(cellsPane == null){
								return;
							}
							if(!cellsPane.isFocusOwner())
								cellsPane.requestFocus();//�����ʽˢ����Ӧesc��
							if (e.getClickCount() >= 2) {
								btn.getModel().setPressed(true);
								b_isSeries = true;// ��Ҫ��ʽˢ��������
							} else {// ���������������ٵ����ʽˢ������ֹͣ
								if (!b_isSeries) {
									return;
								}
								BorderPlayRender.stopPlay(cellsPane);
								b_isSeries = false;// ���ò���������
								b_isUseBrush = false;// ��ʽˢ״̬��������
							}
						}
					});
					return btn;
			}
			
		};
	}

	private EventListener createMouseListener(){
		return new MouseListenerAdapter(){
			@Override
			public void mouseReleased(MouseEventAdapter e) {
				if (b_isSeries) {// ˫����������ʱ����ʽˢ״̬�ǣ����Գ�������
					b_isUseBrush = true;
				}
				if (b_isUseBrush) {				
					getChoosePaste().choosePaste();//ճ����ʽ
					b_isUseBrush = false;// ִ��һ�κ�ʹ��ʽˢ�����ã����������(˫����ʽˢ��ť)����ʱ���ٱ�ɿ��ã����Լ�������
					if (!b_isSeries) {// ִ��һ��ʱ��ֹͣ����������ִ��ʱ������Ҫֹͣ
						BorderPlayRender.stopPlay(getCellsPane());
					}
				}
			}
			
		};
	}
	
	public void anchorChanged(CellsModel model, CellPosition oldAnchor,
			CellPosition newAnchor) {
		if (b_isSeries) {// ˫����������ʱ����ʽˢ״̬�ǣ����Գ�������
			b_isUseBrush = true;
		}
		if (b_isUseBrush) {				
			getChoosePaste().choosePaste();//ճ����ʽ
			b_isUseBrush = false;// ִ��һ�κ�ʹ��ʽˢ�����ã����������(˫����ʽˢ��ť)����ʱ���ٱ�ɿ��ã����Լ�������
			if (!b_isSeries) {// ִ��һ��ʱ��ֹͣ����������ִ��ʱ������Ҫֹͣ
				BorderPlayRender.stopPlay(getCellsPane());
			}
		}
	}

	public void selectedChanged(CellsModel cellsModel,
			AreaPosition[] changedArea) {	
	}

}
