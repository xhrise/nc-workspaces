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
 * ��ʽˢ���
 * 
 * @author �����
 * @version Create on 2008-3-19
 */
public class FormatBrushExt extends EditExt{
	/** ��ʽˢ�����Ĳ������������ */
	private final int CLICK_COUNT_TO_START = 2;
	/** ������ */
	private UfoReport m_report;
	/** �жϸ�ʽˢ�Ƿ���Ҫ�����Ĳ��� */
	private boolean b_isSeries = false;
	/** ������ */
	private CellsPane cellsPane = null;
	/** ��ʽˢ��ʶ */
	private boolean b_isUseBrush = false;
	/** ճ����״̬���� */
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
					BorderPlayRender.controlPlay(cellsPane);// �������Ʒ���
				}
				copy(getClipType(), getEditType());
				b_isUseBrush = true;// �ı��ʽˢ��״̬:����
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
							cellsPane.requestFocus();//�����ʽˢ����Ӧesc��
						if (e.getClickCount() >= CLICK_COUNT_TO_START) {
							btBursh.getModel().setPressed(true);
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
	 * ������������
	 * 
	 * @return MouseListener
	 */
	protected MouseListener createMouseListener() {
		return new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (b_isSeries) {// ˫����������ʱ����ʽˢ״̬�ǣ����Գ�������
					b_isUseBrush = true;
				}
				if (b_isUseBrush) {				
					getChoosePaste().choosePaste();//ճ����ʽ
					b_isUseBrush = false;// ִ��һ�κ�ʹ��ʽˢ�����ã����������(˫����ʽˢ��ť)����ʱ���ٱ�ɿ��ã����Լ�������
					if (!b_isSeries) {// ִ��һ��ʱ��ֹͣ����������ִ��ʱ������Ҫֹͣ
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
