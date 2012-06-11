package com.ufsoft.report.fmtplugin.formula;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import nc.util.iufo.pub.UFOString;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.comp.KToolBar;
import com.ufida.zior.comp.KToolBarButton;
import com.ufida.zior.view.Mainboard;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.script.AreaFmlExecutor;
import com.ufsoft.script.base.AreaFormulaModel;
import com.ufsoft.script.base.FormulaVO;
import com.ufsoft.script.exception.ParseException;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.CombinedCell;
import com.ufsoft.table.IArea;
import com.ufsoft.table.TableUtilities;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.event.CellsModelSelectedListener;

/**
 * Report ��ʽ�����,ȡ����V55֮ǰ��ToolBarFormulaComp
 * 
 * @author zhaopq
 * @created at 2009-4-9,����02:47:38
 * @since v5.6
 */
public class AreaFormulaToolBar extends KToolBar implements
		CellsModelSelectedListener {

	static final long serialVersionUID = 5809588378421371182L;

	private static String IMAGEPATH = "reportcore/";

	protected static final String EQUALS = "=";

	private JTextField areaTextField;

	private JTextField contentTextField;

	private JButton cancelButton;

	private JButton formulaButton;

	private JButton okButton;

	private Mainboard mainBoard;

	public Mainboard getMainBoard() {
		return mainBoard;
	}

	public void setMainBoard(Mainboard mainBoard) {
		this.mainBoard = mainBoard;
	}

	public AreaFormulaToolBar() {
	}

	public AreaFormulaToolBar(Mainboard mainBoard) {
		this.mainBoard = mainBoard;
		initialize();
		doCommonInit();
	}

	protected final void doCommonInit() {
		this.setName("toolBarFormulaComp");
		setBorder(BorderFactory.createEmptyBorder());
		setRollover(true);
		setFloatable(true);
		mainBoard.getEventManager().addListener(this);
		getAreaTextField().addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				super.keyTyped(e);
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					String strAreaPos = getAreaTextField().getText();
					if (strAreaPos == null || strAreaPos.equals("")) {
						return;
					}
					AreaPosition aimArea = TableUtilities
							.getAreaPosByString(areaTextField.getText());
					if (aimArea == null) { // ����������Ч������λ�û�ѡ���������Ч���ơ���
						UfoPublic.sendErrorMessage(MultiLang
								.getString("uiuforep0001109"), getCellsPane(),
								null);// "������Ч"
						return;
					}

					getCellsPane().getSelectionModel().clear();
					getCellsPane().getSelectionModel().setAnchorCell(
							aimArea.getStart());
					getCellsPane().getSelectionModel().setSelectedArea(aimArea);
					getCellsPane().requestFocus();

				}
			}
		});

		setFormulaComp();
	}

	protected ReportDesigner getReportDesigner() {
		if (mainBoard.getCurrentView() instanceof ReportDesigner) {
			return (ReportDesigner) mainBoard.getCurrentView();
		}
		return null;
	}

	protected CellsModel getCellsModel() {
		return getReportDesigner() == null ? null : getReportDesigner()
				.getCellsModel();
	}

	protected CellsPane getCellsPane() {
		return getReportDesigner() == null ? null : getReportDesigner()
				.getCellsPane();
	}

	protected void initialize() {
		this.add(getAreaTextField());
		this.add(getFormulaButton());
		this.add(getContentTextField());
		this.add(getOkButton());
		this.add(getCancelButton());
	}

	protected JTextField getAreaTextField() {
		if (areaTextField == null) {
			areaTextField = new JTextField();
			areaTextField.setPreferredSize(new java.awt.Dimension(65, 21));
			areaTextField.setMaximumSize(new java.awt.Dimension(65, 21));
			areaTextField.addFocusListener(new FocusListener() {
				public void focusGained(FocusEvent e) {
				}

				public void focusLost(FocusEvent e) {
					try {
						AreaPosition.getInstance(areaTextField.getText());
					} catch (Exception ex) {
						String area = getCellsModel().getSelectModel()
								.getSelectedArea().toString();
						areaTextField.setText(area);
					}
				}

			});
		}
		return areaTextField;
	}

	protected JTextField getContentTextField() {
		if (contentTextField == null) {
			contentTextField = new JTextField(){
				private static final long serialVersionUID = 1L;
				@Override
				public void setText(String t) {
					super.setText(" "+t);
				}
				@Override
				public String getText() {
					String strValue = super.getText();
					return strValue != null ? strValue.trim() : "";
				}
			};
			// contentTextField.setMaxLength(500);
			contentTextField.setPreferredSize(new java.awt.Dimension(600, 21));
			contentTextField.setMaximumSize(new java.awt.Dimension(600, 21));
			contentTextField.registerKeyboardAction(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getOkButton().doClick();
				}
			}, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), WHEN_FOCUSED);

			contentTextField.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					if (e.getKeyCode() != KeyEvent.VK_ENTER) {
						getCancelButton().setEnabled(true);
						getOkButton().setEnabled(true);
					}
				}
			});
		}
		return contentTextField;
	}

	protected JButton getFormulaButton() {
		if (formulaButton == null) {
			formulaButton = new KToolBarButton();
			formulaButton.setIcon(ResConst.getImageIcon(IMAGEPATH
					+ "calculate.gif"));
			formulaButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							getCancelButton().setEnabled(true);
							getOkButton().setEnabled(true);
							executeFormulaCmd();
							setFormulaComp();
						}
					});
		}
		return formulaButton;
	}

	protected void executeFormulaCmd() {
		new AreaFormulaActionHandler(getCellsPane()).execute(null);
	}

	protected JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new KToolBarButton();
			cancelButton.setIcon(ResConst
					.getImageIcon(IMAGEPATH + "cancel.gif"));
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					IArea area = getSelectedArea();
					if (isFormatState() && isFormulaCell(area)) {
						String fmlContent = getSelectedFormulaText(area);
						getContentTextField().setText(EQUALS + fmlContent);
					} else {
						CellPosition anchorPos = getCellsModel()
								.getSelectModel().getAnchorCell();
						Object value = getCellsModel().getCellValue(anchorPos);
						getContentTextField().setText(
								value == null ? "" : value.toString());
					}
					getCancelButton().setEnabled(false);
					getOkButton().setEnabled(false);
				}
			});
		}
		return cancelButton;
	}

	protected JButton getOkButton() {
		if (okButton == null) {
			okButton = new KToolBarButton();
			okButton.setIcon(ResConst.getImageIcon(IMAGEPATH + "ok.gif"));
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					AreaPosition aimArea = AreaPosition
							.getInstance(areaTextField.getText());
					String strEditCellFormula = contentTextField.getText();
					if (isFormatState() && isInputFormula(strEditCellFormula)) {
						setCellFormula(aimArea, strEditCellFormula);
					} else {
						setCellValue(aimArea, strEditCellFormula, e);
					}
					getCancelButton().setEnabled(false);
					getOkButton().setEnabled(false);
				}

			});
		}
		return okButton;
	}

	/**
	 * ����ʽ��ӵ�ָ����Ԫ
	 * 
	 * @param fmlArea
	 * @param strCellFormula
	 */
	protected void setCellFormula(AreaPosition fmlArea, String strCellFormula) {
		if (strCellFormula != null) {
			getCellsModel().clearArea(UFOTable.CELL_CONTENT,
					new IArea[] { fmlArea });
		}

		AreaFormulaModel formulaModel = AreaFormulaModel
				.getInstance(getCellsModel());
		IArea fmlRealArea = formulaModel.getRelatedFmlArea(fmlArea);// ת������ȷ�Ĺ�ʽ����
		if (fmlRealArea == null) {
			fmlRealArea = formulaModel.getRelatedFmlArea(fmlArea);
		}
		if (fmlRealArea != null && !fmlRealArea.isCell()) {
			getCellsModel().getSelectModel().setSelectedArea(
					(AreaPosition) fmlRealArea);
		} else {
			fmlRealArea = fmlArea;
		}

		if (strCellFormula == null || strCellFormula.trim().equals("")
				|| strCellFormula.trim().equals(EQUALS)) {
			getFormulaHandler().clearFormula(fmlRealArea);
		} else {
			boolean bAddCellFml = false;
			StringBuffer showErrMessage = new StringBuffer();
			try {
				int index = strCellFormula.indexOf(EQUALS);
				strCellFormula = strCellFormula.substring(index + 1);
				if (!checkFormula(strCellFormula))
					return;
				bAddCellFml = getFormulaHandler()
						.addDbDefFormula(showErrMessage, fmlRealArea,
								strCellFormula, null, true);
			} catch (ParseException ex) {
				AppDebug.debug(ex);
				bAddCellFml = false;
			}
			if (bAddCellFml == false)
				getFormulaHandler().clearFormula(fmlRealArea);
		}
	}

	/**
	 * ����Ԫ�ı���ӵ�ָ����Ԫֵ
	 * 
	 * @param areaPos
	 * @param strCellText
	 */
	protected void setCellValue(AreaPosition areaPos, String strCellText,
			EventObject e) {
		if (isFormatState()) {
			Cell c = getCellsModel().getCell(areaPos.getStart());
			if (c == null) {
				c = new Cell();
				c.setRow(areaPos.getStart().getRow());
				c.setCol(areaPos.getStart().getColumn());
			}
			if (!getCellsPane().isCellEditable(c.getRow(), c.getCol())) {// add
				// by
				// wangyga
				// 2008-7-17
				return;
			}
			c.setValue(strCellText);
			getCellsModel().setCell(areaPos.getStart().getRow(),
					areaPos.getStart().getColumn(), c);

		} else {
			// ��ϵ�Ԫ�������׵�Ԫ��ת��Ϊ�׵�Ԫ��
			int row = areaPos.getStart().getRow();
			int column = areaPos.getStart().getColumn();
			CombinedCell cc = getCellsModel().getCombinedAreaModel()
					.belongToCombinedCell(row, column);
			if (cc != null) {
				row = cc.getArea().getStart().getRow();
				column = cc.getArea().getStart().getColumn();
			}
			// �༭���ǿգ������жϵ�ǰ�༭�ɹ���
			if (!getCellsPane().isCellEditable(row, column)) {
				return;
			}

		}

	}

	/**
	 * ����ѡ��������¹�������ʽ�����ʾ״̬
	 */
	protected void setFormulaComp() {
		// ѡ��ģ��ê��ֻ��¼ѡ������ĵ�һ����Ԫ�����Եõ�ѡ������ʱ
		// ֻ�ܸ���ѡ��ê��ͨ��CellsModel��getArea��������
		IArea area = getSelectedArea();
		getAreaTextField().setText(area.toString());
		if (isFormatState() && isFormulaCell(area)) {
			String fmlContent = getSelectedFormulaText(area);
			getContentTextField().setText(EQUALS + fmlContent);
		} else {
			CellPosition anchorPos = getCellsModel().getSelectModel()
					.getAnchorCell();
			getContentTextField().setText(getFormatValue(anchorPos));
		}

		getFormulaButton().setEnabled(isFormatState());
		getCancelButton().setEnabled(false);
		getOkButton().setEnabled(false);
	}

	protected String getFormatValue(CellPosition cell) {
		if (cell == null)
			return null;
		Object value = getCellsModel().getCellValue(cell);
		// add by ����� 2008-5-4 �޸ĵ�Ԫ����Ϊdouble���ͣ���Ϊ8λ����ʱ���������ϲ��ÿ�ѧ������
		if (value instanceof Double) {
			IufoFormat format = (IufoFormat) getCellsModel().getCellIfNullNew(
					cell.getRow(), cell.getColumn()).getFormat();
			if (format != null) {
				value = format.getString((Double) value);
			}
		}
		return value != null ? value.toString() : "";
	}

	/**
	 * ����ê��ѡ��������(���������ʽ�������Ӧ��Ϊ������������)
	 * 
	 * @return
	 */
	protected IArea getSelectedArea() {
		// ѡ��ģ��ê��ֻ��¼ѡ������ĵ�һ����Ԫ�����Եõ�ѡ������ʱ
		// ֻ�ܸ���ѡ��ê��ͨ��CellsModel��getArea��������
		if (getCellsModel() == null || getCellsModel().getSelectModel() == null)
			return null;
		IArea area = getCellsModel().getSelectModel().getSelectedArea();
		// modify by guogang 2007-11-7 ���������ʽ�Ļ�ѡ��������Ϊ��
		AreaPosition selArea = getCellsModel().getSelectModel()
				.getSelectedArea();
		if (selArea != null) {
			area = selArea;
		}
		// modify end
		return area;

	}

	private boolean checkFormula(String strFmlContent) {
		if (UFOString.isEmpty(strFmlContent))
			return true;
		try {
			getFormulaHandler().parseUserDefFormula(null, strFmlContent);
		} catch (Exception e) {
			AppDebug.debug(e);
			return false;
		}
		return true;
	}

	/**
	 * ����ѡ����ʽ����ʾ�ı�
	 * 
	 * @param area
	 *            ��ʽ��������(���������ʽ�������Ӧ��Ϊ������������)
	 * @return
	 */
	protected String getSelectedFormulaText(IArea area) {
		FormulaVO formulaVO = AreaFormulaModel.getInstance(getCellsModel())
				.getDirectFml(area);
		return formulaVO == null ? "" : formulaVO.getContent();

	}

	/**
	 * ��鹫ʽ�༭��¼��ֵ�Ƿ�Ϊ��ʽ modify by wangyga
	 * �����һ���ַ���"="��,��ֻ��һ��"="������Ϊ�ǹ�ʽ(���԰�������Ⱥţ���һ���ǵȺ�ʱ���Ǳ༭��ʽ)
	 * 
	 * @param inputFormulaStr
	 *            ��ʽ�༭����¼���ַ���
	 * @return
	 */
	private boolean isInputFormula(String inputFormulaStr) {
		if (inputFormulaStr == null || inputFormulaStr.trim().length() < 2) {
			return false;
		}
		return (inputFormulaStr.trim().indexOf('=') == 0) ? true : false;

	}

	protected AreaFmlExecutor getFormulaHandler() {
		AreaFormulaModel formulaModel = AreaFormulaModel
				.getInstance(getCellsModel());
		return formulaModel.getAreaFmlExecutor();
	}

	protected boolean isFormulaCell(IArea area) {
		AreaFormulaModel formulaModel = AreaFormulaModel
				.getInstance(getCellsModel());
		FormulaVO fVO = formulaModel.getDirectFml(area);
		return (fVO != null) ? true : false;
	}

	/**
	 * �Ƿ��ʽ���״̬
	 */
	protected boolean isFormatState() {
		return getCellsPane().getOperationState() == ReportContextKey.OPERATION_FORMAT;
	}

	public void anchorChanged(CellsModel model, CellPosition oldAnchor,
			CellPosition newAnchor) {
		CellPosition cellPos = getCellsModel().getSelectModel().getAnchorCell();
		if (!getCellsPane().isCellEditable(cellPos.getRow(),
				cellPos.getColumn())) {
			contentTextField.setEditable(false);
		} else {
			contentTextField.setEditable(true);
		}
		setFormulaComp();

	}

	public void selectedChanged(CellsModel cellsModel,
			AreaPosition[] changedArea) {
	}

	@Override
	public void setEnabled(boolean enabled) {
		// @edit by wangyga at 2009-5-21,����07:41:08 ����������Ĵ�����
		for(Component comp : getComponents()){
			comp.setEnabled(enabled);
		}
		super.setEnabled(enabled);
	}
	
	

}
