package com.ufsoft.report.sysplugin.edit;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.menu.UFButton;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.EditParameter;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.re.BorderPlayRender;

/**
 * <pre>
 * </pre>
 * 
 * 格式刷插件
 * 
 * @author 王宇光
 * @version Create on 2008-3-19
 */
public class FormatBrushExt extends EditExt{
	/** 格式刷连续的操作：点击次数 */
	private final int CLICK_COUNT_TO_START = 2;
	/** 报表工具 */
	private UfoReport m_report;
	/** 判断格式刷是否需要连续的操作 */
	private boolean b_isSeries = false;
	/** 表格组件 */
	private CellsPane cellsPane = null;
	/** 格式刷标识 */
	private boolean b_isUseBrush = false;
	/** 粘贴的状态对象 */
    private AbsChoosePaste choosePaste = null;
    
	public FormatBrushExt(UfoReport report) {
		super(report,EditParameter.BRUSH,EditParameter.CELL_FORMAT);
		if (report == null) {
			throw new IllegalArgumentException(MultiLang.getString("miufo1004052"));
		}
		this.m_report = report;
		this.cellsPane = m_report.getTable().getCells();
		cellsPane.addMouseListener(createMouseListener());
		cellsPane.addKeyListener(createKeyListener());
	}

	@Override
	public UfoCommand getCommand() {
		return new UfoCommand() {
			public void execute(Object[] params) {
				if (!b_isSeries) {
					BorderPlayRender.controlPlay(cellsPane);// 动画控制方法
				}
				copy(getClipType(), getEditType());
				b_isUseBrush = true;// 改变格式刷的状态:可用
			}
		};
	}

	@Override
	public Object[] getParams(UfoReport container) {
		return new Object[] { container };
	}

	/**
	 * override
	 * 
	 * @see AbsActionExt.initListenerByComp();
	 */
	public void initListenerByComp(final Component stateChangeComp) {
		if (stateChangeComp != null) {
			if (stateChangeComp instanceof JButton) {
				final JButton btBursh = (UFButton) stateChangeComp;
				btBursh.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if(!cellsPane.isFocusOwner())
							cellsPane.requestFocus();//点击格式刷后，相应esc键
						if (e.getClickCount() >= CLICK_COUNT_TO_START) {
							btBursh.getModel().setPressed(true);
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
			}

		}
	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes1 = new ActionUIDes();
		uiDes1.setToolBar(true);
		uiDes1.setGroup(MultiLang.getString("edit"));
		uiDes1.setName("");
		uiDes1.setTooltip(MultiLang.getString("miufo1004056"));
		uiDes1.setImageFile("reportcore/format_brush.gif");
		return new ActionUIDes[] { uiDes1 };
	}

	/**
	 * 创建鼠标监听器
	 * 
	 * @return MouseListener
	 */
	protected MouseListener createMouseListener() {
		return new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (b_isSeries) {// 双击连续操作时，格式刷状态是：可以持续操作
					b_isUseBrush = true;
				}
				if (b_isUseBrush) {				
					getChoosePaste().choosePaste();//粘贴格式
					b_isUseBrush = false;// 执行一次后，使格式刷不可用：如果是连续(双击格式刷按钮)操作时，再变成可用，可以继续操作
					if (!b_isSeries) {// 执行一次时，停止动画，连续执行时，不需要停止
						BorderPlayRender.stopPlay(cellsPane);
					}
				}

			}

		};
	}

	private KeyListener createKeyListener(){
		return new KeyAdapter(){			
			 public void keyPressed(KeyEvent e) {
				 if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
						b_isUseBrush = false;
			 }
		};
	}
		
	private AbsChoosePaste getChoosePaste(){
		if (choosePaste == null) {
			choosePaste = new EditPasteFormat(m_report, false);
		}	
		return choosePaste;
	}
	
	private int getClipType() {
		return UFOTable.CELL_FORMAT;
	}

	private int getEditType() {
		return EditParameter.BRUSH;
	}

	protected boolean isSeries() {
		return b_isSeries;
	}

	protected void setSeries(boolean series) {
		b_isSeries = series;
	}

	protected boolean isUseBrush() {
		return b_isUseBrush;
	}

	protected void setUseBrush(boolean useBrush) {
		b_isUseBrush = useBrush;
	}
}
