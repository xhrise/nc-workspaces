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

	/** 判断格式刷是否需要连续的操作 */
	private boolean b_isSeries = false;
	/** 格式刷标识 */
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
			BorderPlayRender.controlPlay(getCellsPane());// 动画控制方法
		}
		copy(getClipType(), getEditType());
		b_isUseBrush = true;// 改变格式刷的状态:可用
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
								cellsPane.requestFocus();//点击格式刷后，相应esc键
							if (e.getClickCount() >= 2) {
								btn.getModel().setPressed(true);
								b_isSeries = true;// 需要格式刷连续操作
							} else {// 连续操作结束，再点击格式刷，动画停止
								if (!b_isSeries) {
									return;
								}
								BorderPlayRender.stopPlay(cellsPane);
								b_isSeries = false;// 设置不连续操作
								b_isUseBrush = false;// 格式刷状态：不可用
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
				if (b_isSeries) {// 双击连续操作时，格式刷状态是：可以持续操作
					b_isUseBrush = true;
				}
				if (b_isUseBrush) {				
					getChoosePaste().choosePaste();//粘贴格式
					b_isUseBrush = false;// 执行一次后，使格式刷不可用：如果是连续(双击格式刷按钮)操作时，再变成可用，可以继续操作
					if (!b_isSeries) {// 执行一次时，停止动画，连续执行时，不需要停止
						BorderPlayRender.stopPlay(getCellsPane());
					}
				}
			}
			
		};
	}
	
	public void anchorChanged(CellsModel model, CellPosition oldAnchor,
			CellPosition newAnchor) {
		if (b_isSeries) {// 双击连续操作时，格式刷状态是：可以持续操作
			b_isUseBrush = true;
		}
		if (b_isUseBrush) {				
			getChoosePaste().choosePaste();//粘贴格式
			b_isUseBrush = false;// 执行一次后，使格式刷不可用：如果是连续(双击格式刷按钮)操作时，再变成可用，可以继续操作
			if (!b_isSeries) {// 执行一次时，停止动画，连续执行时，不需要停止
				BorderPlayRender.stopPlay(getCellsPane());
			}
		}
	}

	public void selectedChanged(CellsModel cellsModel,
			AreaPosition[] changedArea) {	
	}

}
